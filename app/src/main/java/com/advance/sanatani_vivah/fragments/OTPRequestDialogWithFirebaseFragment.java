package com.advance.sanatani_vivah.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.hbb20.CountryCodePicker;
import com.mukesh.OtpView;
//import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OTPRequestDialogWithFirebaseFragment extends DialogFragment {
    private static final String RESEND_OTP_REQUEST = "resend_otp_request";

    private Dialog dialog;
    private RelativeLayout layoutProgressBar;
    private View view;
    private FirebaseAuth mAuth;

    private Common common;
    private SessionManager session;

    private OtpView otpView;
    private TextView tvMobileNumber;
    private LinearLayout layoutMobileNumber;
    private CountryCodePicker spin_code;
    private EditText txtPhoneNumber;
    private TextView btnResendOTP;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    public static OTPRequestDialogWithFirebaseFragment newInstance() {
        return  new OTPRequestDialogWithFirebaseFragment();
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

        Button btnVerify = view.findViewById(R.id.btnVerify);
        Button btnCancel = view.findViewById(R.id.btnVerifyCancel);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        spin_code = view.findViewById(R.id.spin_code);
        layoutMobileNumber = view.findViewById(R.id.layoutMobileNumber);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        btnResendOTP = view.findViewById(R.id.btnResendOTP);

        otpView = view.findViewById(R.id.otpView);

        mAuth = FirebaseAuth.getInstance();

//        btnResend = view.findViewById(R.id.btnResend);
//        btnResend.setOnClickListener(view -> {
//        });

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
            try {
                spin_code.setCountryForPhoneCode(Integer.parseInt(arr_mob[0].replace(" ", "")));
            }catch(Exception e) {
                AppDebugLog.print("exception in parsing");
            }
        }

        otpView.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void afterTextChanged(Editable editable) {
                if(editable.length() == 6) {
                    showProgressLayout();
                    verifyPhoneNumberWithCode(mVerificationId, otpView.getText().toString());
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
                        verifyPhoneNumberWithCode(mVerificationId, otpView.getText().toString());
                    }
                }
            }
        });

        btnCancel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });

        btnResendOTP.setOnClickListener(view1 -> {
            showProgressLayout();
            resendVerificationCode(session.getLoginData("full_mobile"), mResendToken);
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
        mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobileNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            ApplicationData.isOTPProcess = false;
            hideProgressLayout();
            setVerifyStatus();
            //dialog.dismiss();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            hideProgressLayout();
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
            hideProgressLayout();
            mVerificationId = verificationId;
            mResendToken = token;
            otpView.setVisibility(View.VISIBLE);
            tvMobileNumber.setVisibility(View.VISIBLE);
            btnResendOTP.setVisibility(View.VISIBLE);
            layoutMobileNumber.setVisibility(View.GONE);
        }
    };

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                ApplicationData.isOTPProcess = false;
                FirebaseUser user = task.getResult().getUser();
                //api call
                Common.hideSoftKeyboard(getActivity());
                setVerifyStatus();
            } else {
                hideProgressLayout();
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setVerifyStatus() {
        showProgressLayout();
        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        common.makePostRequest(AppConstants.verify_otp_firebase, param, response -> {
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                hideProgressLayout();
                if(object.getString("status").equalsIgnoreCase("success")) {
                    common.showToast("Congratulations!! Your mobile number has been verified.");
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
