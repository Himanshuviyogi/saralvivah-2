package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DeleteProfileActivity extends AppCompatActivity {
    private EditText et_about;
    private Button btn_submit;
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delete Profile");
        toolbar.setNavigationOnClickListener(v -> finish());

        common = new Common(this);
        session = new SessionManager(this);

        loader = findViewById(R.id.loader);
        btn_submit = findViewById(R.id.btn_id);
        et_about = findViewById(R.id.et_about);

        btn_submit.setOnClickListener(view -> deleteRequest());

    }

    private void deleteRequest() {
        final String reason = et_about.getText().toString().trim();
        if (TextUtils.isEmpty(reason)) {
            et_about.setError("Please enter reason");
            return;
        }
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("reason", reason);
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequest(AppConstants.delete_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    et_about.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });

    }
}
