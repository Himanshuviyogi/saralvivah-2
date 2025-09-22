package com.advance.sanatani_vivah.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.hbb20.CountryCodePicker;
import com.mukesh.OtpView;
//import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OTPRequestDialogFragment extends DialogFragment {
    private static final String RESEND_OTP_REQUEST = "resend_otp_request";

    private Dialog dialog;
    private RelativeLayout layoutProgressBar;
    private View view;

    private Common common;
    private SessionManager session;

    private OtpView otpView;
    private TextView tvMobileNumber;
    private LinearLayout layoutMobileNumber;
    private CountryCodePicker spin_code;
    private EditText txtPhoneNumber;
    private TextView btnResendOTP;
    private String sendOtp;
    Button btnVerify;
    Button btnCancel;

    public static OTPRequestDialogFragment newInstance() {
        return  new OTPRequestDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_verify_otp, container, false);

        session = new SessionManager(getActivity());
        common = new Common(getActivity());

        btnVerify = view.findViewById(R.id.btnVerify);
        btnCancel = view.findViewById(R.id.btnVerifyCancel);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        spin_code = view.findViewById(R.id.spin_code);
        layoutMobileNumber = view.findViewById(R.id.layoutMobileNumber);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        btnResendOTP = view.findViewById(R.id.btnResendOTP);

        otpView = view.findViewById(R.id.otpView);

        otpView.setVisibility(View.GONE);
        tvMobileNumber.setVisibility(View.GONE);
        btnResendOTP.setVisibility(View.GONE);
        layoutMobileNumber.setVisibility(View.VISIBLE);

        btnVerify.setText("Send OTP");

        SessionManager session = new SessionManager(getActivity());
        tvMobileNumber.setText("OTP sent to "+session.getLoginData("full_mobile"));

        String[] arr_mob = session.getLoginData("full_mobile").split("-");

        if (arr_mob.length == 2) {
            txtPhoneNumber.setText(arr_mob[1]);
            spin_code.setCountryForPhoneCode(Integer.parseInt(arr_mob[0]));
        }

        otpView.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void afterTextChanged(Editable editable) {
                if(editable.length() == 6) {
                    showProgressLayout();
                    verifyPhoneNumberWithCode(otpView.getText().toString());
                }
            }
        });

        btnVerify.setOnClickListener(view -> {
            if (dialog != null) {
                if(btnVerify.getText().toString().equalsIgnoreCase("Send OTP")) {
                    sendOTP(session.getLoginData("full_mobile"));
                }else{
                    if (otpView.length() == 6) {
                        showProgressLayout();
                        verifyPhoneNumberWithCode(otpView.getText().toString());
                    }
                }
            }
        });

        btnCancel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });

        btnResendOTP.setOnClickListener(view1 -> {
            sendOTP(session.getLoginData("full_mobile"));
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        //request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    //Todo show progress layout
    private void showProgressLayout() {
        layoutProgressBar = view.findViewById(R.id.layoutProgressBar);
        layoutProgressBar.setVisibility(View.VISIBLE);
    }

    //Todo hide progress layout
    private void hideProgressLayout() {
        if (layoutProgressBar != null)
            layoutProgressBar.setVisibility(View.GONE);
    }

    private void sendOTP(String mobileNumber) {
        showProgressLayout();
        String[] arr_mob = mobileNumber.split("-");

        HashMap<String, String> params = new HashMap<>();
        params.put("android_device_id", session.getLoginData(SessionManager.KEY_DEVICE_TOKEN));
        params.put("user_agent", AppConstants.USER_AGENT);
        params.put("csrf_new_matrimonial", "70c0313fa86f9b4e960d8bfe24edd7a7");
        params.put("country_code", arr_mob[0]);
        params.put("mobile_number", arr_mob[1]);

        common.makePostRequestWithTag(AppConstants.generate_otp, params, response -> {
            AppDebugLog.print("login with otp response : " + response);
            hideProgressLayout();
            try {
                JSONObject object = new JSONObject(response);
                if (object.has("status") && object.getString("status").equals("success")) {
                    Toast.makeText(requireActivity(), object.getString("errmessage"), Toast.LENGTH_SHORT).show();
                    otpView.requestFocus();
                    sendOtp = object.getString("otp_varify");
                    tvMobileNumber.setText(String.format(getString(R.string.lbl_verify_otp_dialog), arr_mob[0] + "-" + arr_mob[1]));

                    btnVerify.setText("Verify OTP");
                    otpView.setVisibility(View.VISIBLE);
                    tvMobileNumber.setVisibility(View.VISIBLE);
                    btnResendOTP.setVisibility(View.VISIBLE);
                    layoutMobileNumber.setVisibility(View.GONE);
                } else {
                    if (object.has("errmessage")) {
                        Toast.makeText(requireActivity(), object.getString("errmessage"), Toast.LENGTH_SHORT).show();
                    }
                    if (!object.has("errmessage") && object.has("error_meessage")) {
                        Toast.makeText(requireActivity(), object.getString("error_meessage"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }

        }, error -> {
            Log.d("resp", error.getMessage() + "   ");
            hideProgressLayout();
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        }, "LOGIN_TAG");
    }

    private void verifyPhoneNumberWithCode(String code) {
        if(sendOtp.equals(code)){
            setVerifyStatus();
        }else{
            common.showToast("Invalid OTP");
        }
    }


    private void setVerifyStatus() {

        String[] arr_mob = session.getLoginData("full_mobile").split("-");

        showProgressLayout();
        HashMap<String, String> params = new HashMap<>();
        params.put("otp_varify", sendOtp);
        params.put("otp_mobile", otpView.getText().toString());
        params.put("mobile_number", arr_mob[0] + "-" + arr_mob[1]);
        params.put("user_id", session.getLoginData(SessionManager.KEY_USER_ID));
        common.makePostRequest(AppConstants.verify_otp, params, response -> {
            hideProgressLayout();
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if(object.getString("status").equalsIgnoreCase("success")) {
                    dialog.dismiss();
                }
            } catch (JSONException e) {
                hideProgressLayout();
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            hideProgressLayout();
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }
}
