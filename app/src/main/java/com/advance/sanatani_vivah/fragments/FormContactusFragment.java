package com.advance.sanatani_vivah.fragments;

import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hbb20.CountryCodePicker;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.advance.sanatani_vivah.utility.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FormContactusFragment extends Fragment {
    private Common common;
    private EditText et_f_name, et_email, et_mobile, et_subject, et_about;
    private CountryCodePicker spin_code;
    private Button btn_submit;
    private SessionManager session;
    private RelativeLayout loader;
    private CoordinatorLayout main;

    public FormContactusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common = new Common(getActivity());
        session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_contactus, container, false);

        loader = view.findViewById(R.id.loader);
        et_f_name = view.findViewById(R.id.et_f_name);
        et_email = view.findViewById(R.id.et_email);
        et_subject = view.findViewById(R.id.et_subject);
        spin_code = view.findViewById(R.id.spin_code);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_about = view.findViewById(R.id.et_about);
        btn_submit = view.findViewById(R.id.btn_id);
        main = view.findViewById(R.id.main);

        btn_submit.setOnClickListener(view1 -> validData());

        return view;
    }

    private void validData() {
        String name = et_f_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String code = spin_code.getSelectedCountryCodeWithPlus();
        String mobile = et_mobile.getText().toString().trim();
        String subject = et_subject.getText().toString().trim();
        String message = et_about.getText().toString().trim();
        boolean isvalid = true;
        if (TextUtils.isEmpty(name)) {
            et_f_name.setError("Please enter name");
            isvalid = false;
        }
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Please enter email");
            isvalid = false;
        }
        if (!common.isValidEmail(email)) {
            et_email.setError("Please enter valid email");
            isvalid = false;
        }
        if (TextUtils.isEmpty(mobile)) {
            et_mobile.setError("Please enter mobile number");
            isvalid = false;
        }
        if (mobile.length() < 8) {
            et_mobile.setError("Please enter valid mobile number");
            isvalid = false;
        }
        if (TextUtils.isEmpty(subject)) {
            et_subject.setError("Please enter subject");
            isvalid = false;
        }
        if (TextUtils.isEmpty(message)) {
            et_about.setError("Please enter message");
            isvalid = false;
        }
        if (isvalid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("name", name);
            param.put("email", email);
            param.put("country_code", code);
            param.put("phone", mobile);
            param.put("subject", subject);
            param.put("description", message);
            submitData(param);
        }
    }

    private void submitData(HashMap<String, String> param) {
        common.showProgressRelativeLayout(loader);

        common.makePostRequest(AppConstants.contact_form, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    et_about.setText("");
                    et_f_name.setText("");
                    et_email.setText("");
                    et_subject.setText("");
                    et_mobile.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> common.hideProgressRelativeLayout(loader));

    }

}
