package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText et_email;
    private Button btn_reset;
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
        toolbar.setNavigationOnClickListener(v -> finish());

        common = new Common(this);
        session = new SessionManager(this);

        loader = findViewById(R.id.loader);
        et_email = findViewById(R.id.et_email);
        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(view -> validData());

    }

    private void validData() {
        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Please enter email");
            return;
        }
        if (!isValidEmail(email)) {
            et_email.setError("Please enter valid email");
            return;
        }
        forgotApi(email);
    }

    private void forgotApi(String email) {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        common.showProgressRelativeLayout(loader);

        Common.hideSoftKeyboard(this);

        HashMap<String, String> params = new HashMap<>();
        params.put("username", email);

        common.makePostRequest(AppConstants.forgot, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resp", response);
                common.hideProgressRelativeLayout(loader);
                try {
                    JSONObject object = new JSONObject(response);
                    session.setUserData(SessionManager.TOKEN, object.getString("token"));
                    if (!object.getString("status").equals("error")) {
                        et_email.setText("");
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), object.getString("errmessage"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    common.showToast(getString(R.string.err_msg_try_again_later));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.hideProgressRelativeLayout(loader);
                if (error.networkResponse != null) {
                    common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
                }
            }
        });
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
