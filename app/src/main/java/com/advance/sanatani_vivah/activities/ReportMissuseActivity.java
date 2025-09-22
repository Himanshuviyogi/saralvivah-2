package com.advance.sanatani_vivah.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ReportMissuseActivity extends AppCompatActivity {

    private EditText et_about;
    private Button btn_submit;
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_missuse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report Misuse");

        common = new Common(this);
        session = new SessionManager(this);

        loader = findViewById(R.id.loader);
        et_about = (EditText) findViewById(R.id.et_about);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_about.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    et_about.setError("Please enter about misuse");
                    return;
                }
                submitAdmin(msg);
            }
        });
    }

    private void submitAdmin(String msg) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("message", msg);

        common.makePostRequest(AppConstants.contact_admin, param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                common.hideProgressRelativeLayout(loader);
                Log.d("resp", response);
                try {
                    JSONObject object = new JSONObject(response);
                    common.showToast(object.getString("msg"));
                    if (object.getString("status").equals("success")) {
                        et_about.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    common.showToast(getString(R.string.err_msg_try_again_later));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.hideProgressRelativeLayout(loader);
                if(error.networkResponse!=null) {
             common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
}
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
