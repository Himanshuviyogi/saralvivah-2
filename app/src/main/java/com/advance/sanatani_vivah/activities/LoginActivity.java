package com.advance.sanatani_vivah.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks{
    private String LOGIN_TAG = "login";
    private Button btn_login, btnLoginWithOtp;
    private TextInputEditText et_user, et_password;
    private TextView tv_forgot, btn_signup;
    private RelativeLayout loader;
    private Common common;
    private SessionManager session;
    private TextInputLayout pass_input, id_input;

    //location
    private final int TAG_PERMISSIONS = 124;
    private final int GPS_REQUEST = 333;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        common = new Common(this);
        session = new SessionManager(this);

        initData();

        loader = findViewById(R.id.loader);
        pass_input = findViewById(R.id.pass_input);
        id_input = findViewById(R.id.id_input);

        singleTextView(btn_signup);
    }

    private void initData() {
        et_user = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        tv_forgot = findViewById(R.id.tv_forgot);

        btn_login = findViewById(R.id.btn_id);
        btnLoginWithOtp = findViewById(R.id.btnLoginWithOtp);
        btn_signup = findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(this);
        btnLoginWithOtp.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);

        getLocationAccess();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_id) {
            validData();
        } else if (id == R.id.btn_signup) {
            startActivity(new Intent(LoginActivity.this, RegisterFirstActivity.class));
            //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } else if (id == R.id.tv_forgot) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        }else if (id == R.id.btnLoginWithOtp) {
            Intent intent = new Intent(LoginActivity.this, LoginWithOtpActivity.class);
            if(currentLocation !=null) {
                intent.putExtra("latitude",String.valueOf(currentLocation.getLatitude()));
                intent.putExtra("longitude",String.valueOf(currentLocation.getLongitude()));
                AppDebugLog.print("latitude : "+String.valueOf(currentLocation.getLatitude()));
                AppDebugLog.print("longitude : "+String.valueOf(currentLocation.getLongitude()));
            }else{
                intent.putExtra("lat","");
                intent.putExtra("lon","");
            }
            startActivity(intent);
        }
    }

    private void validData() {
        String username = et_user.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        boolean isvalid = true;
        if (TextUtils.isEmpty(username)) {
            et_user.setError("Please enter email or matriId");
            isvalid = false;
        }
        if (TextUtils.isEmpty(password)) {
            et_password.setError("Please enter password");
            isvalid = false;
        }
        if (password.length() < 6) {
            et_password.setError("Please enter atleast 6 character");
            isvalid = false;
        }
        if (isvalid) {
            loginApi(username, password);
        }
    }

    private void loginApi(final String username, final String password) {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        Common.hideSoftKeyboard(this);
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        if(currentLocation !=null) {
            params.put("latitude", String.valueOf(currentLocation.getLatitude()));
            params.put("longitude", String.valueOf(currentLocation.getLongitude()));
        }else{
            params.put("latitude", "");
            params.put("longitude", "");
        }
        params.put("android_device_id", session.getLoginData(SessionManager.KEY_DEVICE_TOKEN));
        Log.d("resp", params.toString());

        common.makePostRequestWithTag(AppConstants.login, params, response -> {
            Log.d("resp", response);
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("token"));
                Toast.makeText(getApplicationContext(), object.getString("errmessage"), Toast.LENGTH_SHORT).show();
                if (object.getString("status").equals("success")) {
                    JSONObject user_data = object.getJSONObject("user_data");

                    AppDebugLog.print("Gender in login : " + user_data.getString("gender"));
                    session.createLoginSession(username, password, user_data.getString("id"));
                    session.setUserData(SessionManager.KEY_EMAIL, user_data.getString("email"));
                    session.setUserData(SessionManager.KEY_username, user_data.getString("username"));
                    session.setUserData(SessionManager.KEY_GENDER, user_data.getString("gender"));
                    session.setUserData(SessionManager.KEY_MATRI_ID, user_data.getString("matri_id"));
                    session.setUserData(SessionManager.KEY_PLAN_STATUS, user_data.getString("plan_status"));
                    session.setUserData(SessionManager.LOGIN_WITH, "local");

//                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
//                    finish();

                    initZegoCloudService();

                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }

        }, error -> {
            Log.d("resp", error.getMessage() + "   ");
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        }, LOGIN_TAG);

    }

    private void initZegoCloudService() {
        if (session.isLoggedIn()) {
            String myUserID = new SessionManager(this).getLoginData(SessionManager.KEY_MATRI_ID);
            String userName = "user_" + myUserID;

            ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

            //ringtones
            callInvitationConfig.incomingCallRingtone = "incoming_call";
            callInvitationConfig.outgoingCallRingtone = "outgoing_call";

            // button text
            callInvitationConfig.innerText.incomingCallPageDeclineButton = "Decline";
            callInvitationConfig.innerText.incomingCallPageAcceptButton = "Accept";

            ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), AppConstants.appID, AppConstants.appSign, myUserID, userName, callInvitationConfig);
        }
    }

//    @Override
//    public void onBackPressed() {
//        common.hideProgressRelativeLayout(loader);
//        MyApplication.getInstance().cancelPendingRequests(LOGIN_TAG);
//        startActivity(new Intent(LoginActivity.this, IntroActivity.class));
//    }

    private void singleTextView(TextView textView) {
        String clickableTextStr = "Sign Up";
        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append("New User? " + clickableTextStr);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this, RegisterFirstActivity.class));
                //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(LoginActivity.this, R.color.red));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - clickableTextStr.length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(ContextCompat.getColor(LoginActivity.this, R.color.transparent));
        textView.setText(spanText, TextView.BufferType.SPANNABLE);
        //  lblTerms.setText(Html.fromHtml(getString(R.string.lbl_service_request)), TextView.BufferType.SPANNABLE);

    }

    @AfterPermissionGranted(TAG_PERMISSIONS)
    private void getLocationAccess() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            createLocationRequest();
        } else {
            // Do not have permissions, request them now
            ActivityCompat.requestPermissions(this, perms, TAG_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

            }
            if (requestCode == GPS_REQUEST) {
                getCurrentLocationFromFusedLocationLibrary();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            getCurrentLocationFromFusedLocationLibrary();
        });

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                try {
                    ((ResolvableApiException) e).startResolutionForResult(this, GPS_REQUEST);
                } catch (IntentSender.SendIntentException sendIntentException) {
                    sendIntentException.printStackTrace();
                }
            }
        });
    }

    private void getCurrentLocationFromFusedLocationLibrary() {
        //fusion library
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                if (locationResult.getLastLocation() != null) {
                    currentLocation = locationResult.getLastLocation();
                    AppDebugLog.print("narjis location : " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                    //getMyprofile();
                    removeLocationUpdateCallback();
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void removeLocationUpdateCallback() {
        if (fusedLocationClient != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
