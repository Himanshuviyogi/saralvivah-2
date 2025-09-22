package com.advance.sanatani_vivah.activities;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText et_old_password, et_new_password, et_con_password;
    private Button btn_submit;
    SessionManager session;
    private RelativeLayout loader;
    Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        session = new SessionManager(this);
        common = new Common(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        toolbar.setNavigationOnClickListener(v -> finish());

        loader = findViewById(R.id.loader);

        et_old_password = findViewById(R.id.et_old_password);
        et_new_password = findViewById(R.id.et_new_password);
        et_con_password = findViewById(R.id.et_con_password);
        btn_submit = findViewById(R.id.btn_id);

        btn_submit.setOnClickListener(view -> validData());

        et_old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    et_old_password.setError("Please enter password");
                    return;
                }
            }
        });

        et_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    et_new_password.setError("Please enter password");
                    return;
                }
            }
        });

        et_con_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    et_con_password.setError("Please enter password");
                    return;
                }
            }
        });

    }

    private void validData() {
        String old = et_old_password.getText().toString().trim();
        String newpass = et_new_password.getText().toString().trim();
        String con = et_con_password.getText().toString().trim();
        boolean isvalid = true;
        if (TextUtils.isEmpty(old)) {
            et_old_password.setError("Please enter old password");
            isvalid = false;
        }
        if (TextUtils.isEmpty(newpass)) {
            et_new_password.setError("Please enter new password");
            isvalid = false;
        }
        if (newpass.length() < 6) {
            et_new_password.setError("Please enter atleast 6 character");
            isvalid = false;
        }
        if (TextUtils.isEmpty(con)) {
            et_con_password.setError("Please enter confirm password");
            isvalid = false;
        }
        if (con.length() < 6) {
            et_con_password.setError("Please enter atleast 6 character");
            isvalid = false;
        }
        if (!newpass.equals(con)) {
            et_con_password.setError("New password and confirm password not match");
            isvalid = false;
        }

        if (isvalid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("old_pass", old);
            param.put("new_pass", newpass);
            param.put("cnfm_pass", con);
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            changeApi(param);
        }
    }

    private void changeApi(final HashMap<String, String> param) {

        common.showProgressRelativeLayout(loader);

        common.makePostRequest(AppConstants.change_password, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (!object.getString("status").equals("error")) {
                    session.setUserData(SessionManager.KEY_PASSWORD, param.get("cnfm_pass"));
                    finish();
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
