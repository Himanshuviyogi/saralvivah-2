package com.advance.sanatani_vivah.activities;


public class LoginActivityNew  {}
//        extends AppCompatActivity implements View.OnClickListener, TimeCompletedListner {
//    private String LOGIN_TAG = "login";
//    private Button btnGenerateOtp, btnVerify;
//    private EditText txtMobileNumber;
//    private AVLoadingIndicatorView progressBar;
//    private Common common;
//    private SessionManager session;
//    private String country_code = "+91";
//    private CountryCodePicker spin_code;
//    private LinearLayout layoutFirst, layoutSecond;
//    private TextView lblVerifyNumber;
//    private OtpView otpView;
//
//    private TimerTextView timerTextView;
//    private TextView btnResend;
//
//    private String sendOtp;
//    private Toolbar toolbar;
//
//    //TODO Firebase Phone Auth Step4
//    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
//
//    private static final int STATE_INITIALIZED = 1;
//    private static final int STATE_CODE_SENT = 2;
//    private static final int STATE_VERIFY_FAILED = 3;
//    private static final int STATE_VERIFY_SUCCESS = 4;
//    private static final int STATE_SIGNIN_FAILED = 5;
//    private static final int STATE_SIGNIN_SUCCESS = 6;
//
//    private FirebaseAuth mAuth;
//
//    private boolean isMobileNumberVerified = false;
//    private boolean mVerificationInProgress = false;
//    private String mVerificationId;
//    private PhoneAuthProvider.ForceResendingToken mResendToken;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    //end
//
//    private String latitude = "";
//    private String longitude = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//        setContentView(R.layout.activity_login_with_otp);
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        toolbar.setNavigationOnClickListener(v -> {
//            onBackPressedd();
//        });
//
//        common = new Common(this);
//        session = new SessionManager(this);
//
//        initData();
//
//        progressBar = findViewById(R.id.progressBar);
//    }
//
//    private void initData() {
//        timerTextView = findViewById(R.id.timerTextView);
//        btnResend = findViewById(R.id.btnResend);
//        layoutFirst = findViewById(R.id.layoutFirst);
//        layoutSecond = findViewById(R.id.layoutSecond);
//        txtMobileNumber = findViewById(R.id.txtMobileNumber);
//        spin_code = findViewById(R.id.spin_code);
//        btnGenerateOtp = findViewById(R.id.btnGenerateOtp);
//        btnVerify = findViewById(R.id.btnVerify);
//        lblVerifyNumber = findViewById(R.id.lblVerifyNumber);
//        otpView = findViewById(R.id.otpView);
//
//        spin_code.setOnCountryChangeListener(country -> {
//            country_code = country.getPhoneCode();
//        });
//
//        latitude = getIntent().getStringExtra("latitude");
//        longitude = getIntent().getStringExtra("longitude");
//
//        AppDebugLog.print("latitude : " + latitude);
//        AppDebugLog.print("longitude : " + longitude);
//
//        btnGenerateOtp.setOnClickListener(this);
//        btnResend.setOnClickListener(this);
//        btnVerify.setOnClickListener(this);
//
//        layoutFirst.setVisibility(View.VISIBLE);
//        layoutSecond.setVisibility(View.GONE);
//
//        //TODO Firebase Phone Auth Step5
//        firebaseMobileAuthSetUp();
//        //end
//    }
//
//    private void setTimerInTextView() {
//        btnResend.setVisibility(View.GONE);
//        timerTextView.setVisibility(View.VISIBLE);
//        long futureTimestamp = System.currentTimeMillis() + (60 * 1000);
//        timerTextView.setEndTime(futureTimestamp);
//        timerTextView.setListener(this);
//    }
//
//    @Override
//    public void timeFinished() {
//        btnResend.setVisibility(View.VISIBLE);
//        timerTextView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        if (id == R.id.btnGenerateOtp) {
//            validData();
//        } else if (id == R.id.btnResend) {
//            common.showProgressLayout(progressBar);
//            resendVerificationCode(country_code + txtMobileNumber.getText().toString(), mResendToken);
//        } else if (id == R.id.btnVerify) {
//            if (otpView.length() == 6) {
//                common.showProgressLayout(progressBar);
//                verifyPhoneNumberWithCode(mVerificationId, otpView.getText().toString());
//            }
//        }
//    }
//
//    private void validData() {
//        String mobileNumber = txtMobileNumber.getText().toString().trim();
//
//        boolean isvalid = true;
//        if (TextUtils.isEmpty(mobileNumber)) {
//            txtMobileNumber.setError("Please enter mobile number");
//            isvalid = false;
//        } else {
//            if (mobileNumber.length() < 10) {
//                txtMobileNumber.setError("Please enter valid mobile number");
//                isvalid = false;
//            }
//        }
//
//        if (isvalid) {
//            mVerificationInProgress = true;
//            sendOTP(country_code + txtMobileNumber.getText().toString());
//        }
//    }
//
//    private void sendOTP(String mobileNumber) {
//        common.showProgressLayout(progressBar);
//        mAuth = FirebaseAuth.getInstance();
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(mobileNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private void firebaseMobileAuthSetUp() {
//        mAuth = FirebaseAuth.getInstance();
//        // Initialize phone auth callbacks
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verificaiton without user action.
//                mVerificationInProgress = false;
//
//                //             updateUI(STATE_VERIFY_SUCCESS, credential);
////                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                mVerificationInProgress = false;
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // mPhoneNumberField.setError("Invalid phone number.");
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    Snackbar.make(findViewById(android.R.id.content), "The SMS quota for the project has been exceeded", Snackbar.LENGTH_SHORT).show();
//                }
//                updateUI(STATE_VERIFY_FAILED);
//            }
//
//            @Override
//            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//
//                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                updateUI(STATE_CODE_SENT);
//            }
//        };
//
//        otpView.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override public void afterTextChanged(Editable editable) {
//                if (editable.length() == 6) {
//                    common.showProgressLayout(progressBar);
//                    verifyPhoneNumberWithCode(mVerificationId, otpView.getText().toString());
//                }
//            }
//        });
//    }
//
//    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)       // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)
//                        .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private void verifyPhoneNumberWithCode(String verificationId, String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//        signInWithPhoneAuthCredential(credential);
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    FirebaseUser user = task.getResult().getUser();
//                    updateUI(STATE_SIGNIN_SUCCESS, user);
//                } else {
//                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                        Toast.makeText(LoginActivityNew.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        //mVerificationField.setError(task.getException().getMessage());
//                    }
//                    updateUI(STATE_SIGNIN_FAILED);
//                }
//            }
//        });
//    }
//
//    private void updateUI(int uiState) {
//        updateUI(uiState, mAuth.getCurrentUser(), null);
//    }
//
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            updateUI(STATE_SIGNIN_SUCCESS, user);
//        } else {
//            updateUI(STATE_INITIALIZED);
//        }
//    }
//
//    private void updateUI(int uiState, FirebaseUser user) {
//        updateUI(uiState, user, null);
//    }
//
//    private void updateUI(int uiState, PhoneAuthCredential cred) {
//        updateUI(uiState, null, cred);
//    }
//
//    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
//        switch (uiState) {
//            case STATE_INITIALIZED:
//                AppDebugLog.print("STATE_INITIALIZED");
//                break;
//            case STATE_CODE_SENT:
//                AppDebugLog.print("STATE_CODE_SENT");
//                Toast.makeText(LoginActivityNew.this, "OTP sent on " + country_code + txtMobileNumber.getText().toString(), Toast.LENGTH_LONG).show();
//
//                common.hideProgressLayout(progressBar);
//                toolbar.setTitle("Verify OTP");
//                otpView.requestFocus();
//                lblVerifyNumber.setText(String.format(getString(R.string.lbl_verify_otp_dialog), country_code + "-" + txtMobileNumber.getText().toString()));
//
//                layoutFirst.setVisibility(View.GONE);
//                otpView.setText("");
//                layoutSecond.setVisibility(View.VISIBLE);
//                setTimerInTextView();
//                break;
//            case STATE_VERIFY_FAILED:
//                AppDebugLog.print("STATE_VERIFY_FAILED");
//                common.hideProgressLayout(progressBar);
//                break;
//            case STATE_VERIFY_SUCCESS:
//                AppDebugLog.print("STATE_VERIFY_SUCCESS");
//                // Set the verification text based on the credential
//                if (cred != null) {
//                    if (cred.getSmsCode() != null) {
//                        otpView.setText(cred.getSmsCode());
//                    } else {
//                        otpView.setText(R.string.instant_validation);
//                    }
//                }
//                break;
//            case STATE_SIGNIN_FAILED:
//                AppDebugLog.print("STATE_SIGNIN_FAILED");
//                common.hideProgressLayout(progressBar);
//                break;
//            case STATE_SIGNIN_SUCCESS:
//                isMobileNumberVerified = true;
//                AppDebugLog.print("STATE_SIGNIN_SUCCESS");
//                common.hideProgressLayout(progressBar);
//
//                Toast.makeText(LoginActivityNew.this, "Mobile Number Verification Successful", Toast.LENGTH_LONG);
//                loginApi(false);
//                // Np-op, handled by sign-in check
//                break;
//        }
//    }
//
//    private void loginApi(boolean isCheckForMobileNumber) {
//        if (!ConnectionDetector.isConnectingToInternet(this)) {
//            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        Common.hideSoftKeyboard(this);
//        common.showProgressLayout(progressBar);
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("country_code", country_code);
//        params.put("mobile", txtMobileNumber.getText().toString());
//        params.put("is_mobile_login", "Yes");
//        if (latitude != null && longitude != null) {
//            params.put("latitude", latitude);
//            params.put("longitude", longitude);
//        } else {
//            params.put("latitude", "");
//            params.put("longitude", "");
//        }
//        params.put("android_device_id", session.getLoginData(SessionManager.KEY_DEVICE_TOKEN));
//        Log.d("resp", params.toString());
//
//        common.makePostRequestWithTag(AppConstants.login, params, response -> {
//            Log.d("resp", response);
//            common.hideProgressLayout(progressBar);
//            try {
//                JSONObject object = new JSONObject(response);
////                if (isCheckForMobileNumber) {
////                    if (object.getString("status").equalsIgnoreCase("success")) {
////                        mVerificationInProgress = true;
////                        sendOTP(country_code + txtMobileNumber.getText().toString());
////                    } else {
////                        otpView.setText("");
////                        startRegisterScreen();
////
//////                        layoutFirst.setVisibility(View.VISIBLE);
//////                        layoutSecond.setVisibility(View.GONE);
////                        //showDialog(object.getString("errmessage"));
////                    }
////                } else {
//                    Toast.makeText(getApplicationContext(), object.getString("errmessage"), Toast.LENGTH_SHORT).show();
//                    session.setUserData(SessionManager.TOKEN, object.getString("token"));
//
//                    if (object.getString("status").equals("success")) {
//                        JSONObject user_data = object.getJSONObject("user_data");
//
//                        AppDebugLog.print("Gender in login : " + user_data.getString("gender"));
//                        session.createLoginSession(user_data.getString("id"));
//                        session.setUserData(SessionManager.KEY_EMAIL, user_data.getString("email"));
//                        session.setUserData(SessionManager.KEY_username, user_data.getString("username"));
//                        session.setUserData(SessionManager.KEY_GENDER, user_data.getString("gender"));
//                        session.setUserData(SessionManager.KEY_MATRI_ID, user_data.getString("matri_id"));
//                        session.setUserData(SessionManager.KEY_PLAN_STATUS, user_data.getString("plan_status"));
//                        session.setUserData(SessionManager.LOGIN_WITH, "local");
//
////                        startActivity(new Intent(LoginActivityNew.this, DashboardActivity.class));
////                        finish();
//
//                        Intent i = new Intent(LoginActivityNew.this, DashboardActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                    }else{
//                        isVerifyDone = true;
//                        startRegisterScreen();
//                    }
////                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                common.showToast(getString(R.string.err_msg_try_again_later));
//            }
//
//        }, error -> {
//            Log.d("resp", error.getMessage() + "   ");
//            common.hideProgressLayout(progressBar);
//            if (error.networkResponse != null) {
//                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
//            }
//        }, LOGIN_TAG);
//    }
//
//    @Override
//    public void onBackPressed() {
//        onBackPressedd();
//    }
//
//    private void onBackPressedd() {
//        common.hideProgressLayout(progressBar);
//        if (layoutSecond.getVisibility() == View.VISIBLE) {
//            toolbar.setTitle("Login with OTP");
//            layoutFirst.setVisibility(View.VISIBLE);
//            layoutSecond.setVisibility(View.GONE);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    private void showDialog(String message) {
//        new AlertDialog.Builder(this)
//                .setTitle("Message")
//                .setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton("Ok", (dialog, which) -> {
//                    Intent intent = new Intent(this, RegisterFirstActivity.class);
//                    intent.putExtra("mobile_number", txtMobileNumber.getText().toString().trim());
//                    intent.putExtra("country_code", country_code);
//                    startActivity(intent);
//                }).show();
//    }
//
//    boolean isVerifyDone = false;
//    @Override protected void onResume() {
//        super.onResume();
//        if(isVerifyDone) {
//            isVerifyDone = false;
//            otpView.setText("");
//            layoutFirst.setVisibility(View.VISIBLE);
//            layoutSecond.setVisibility(View.GONE);
//        }
//    }
//
//    private void startRegisterScreen() {
//        Intent intent = new Intent(this, RegisterFirstActivity.class);
//        intent.putExtra("mobile_number", txtMobileNumber.getText().toString().trim());
//        intent.putExtra("country_code", country_code);
//        intent.putExtra("latitude", latitude);
//        intent.putExtra("longitude", longitude);
//        startActivity(intent);
//    }
//
//}
