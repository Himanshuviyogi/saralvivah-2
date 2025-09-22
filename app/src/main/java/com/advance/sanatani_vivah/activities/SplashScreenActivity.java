package com.advance.sanatani_vivah.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;

import com.advance.sanatani_vivah.utility.AppConstants;
import com.google.gson.JsonObject;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;

import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashScreenActivity extends AppCompatActivity {
    private SessionManager session;
    private Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        session = new SessionManager(this);
        common = new Common(this);

        ApplicationData.getSharedInstance().setDisplayWidth(Common.getDisplayWidth(this));

       // setUpFirebaseConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initZegoCloudService();
        generateToken();
    }

//    private void setUpFirebaseConfig() {
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().build();
//        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
//
//        mFirebaseRemoteConfig.fetchAndActivate()
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                       // boolean updated = task.getResult();
//                       // Toast.makeText(SplashScreenActivity.this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show();
//                        String baseUrl = mFirebaseRemoteConfig.getString("base_url");
//                        AppDebugLog.print("baseUrl : " + baseUrl);
//                        session.setBaseUrl(SessionManager.KEY_BASE_URL,baseUrl);
//                    } else {
//                        AppDebugLog.print("Fetch failed");
//                       // Toast.makeText(SplashScreenActivity.this, "Fetch failed", Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//    }

    private void generateToken() {
        //volleyRequest.getResponseInJsonStringUsingVolley(GET_TOKEN_TAG,AppConstants.BASE_URL+AppConstants.GET_TOKEN,null);

        HashMap<String, String> param = new HashMap<>();
        param.put("appversion", Common.getAppVersionName(this));
        param.put("device_type", "android");
        //param.put("user-agent", "NI-AAPP");

        AppDebugLog.print("Params in getToken : " + param.toString());

        Retrofit retrofit = RetrofitClient.getClient();
        AppApiService service = retrofit.create(AppApiService.class);
        Call<JsonObject> call = service.getToken(param);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppDebugLog.print("response in generateToken : " + response.body());
                JsonObject data = response.body();
                if (data != null) {
                    if (data.has("tocken"))
                        session.setUserData(SessionManager.TOKEN, data.get("tocken").getAsString());

                    if (data.has("is_force_update") && data.get("is_force_update").getAsBoolean()) {
                        openForceUpdateDialog();
                        return;
                    }

                    if (data.has("status") && data.get("status").getAsString().equalsIgnoreCase("success")) {
                        session.saveDrawerMenuArrayObject(data.toString());

                    }

                    startTimer();
                } else {
                    startTimer();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in generateToken : " + t.getMessage());
                startTimer();
            }
        });
    }

    private void openForceUpdateDialog() {
        String updateUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available!!!")
                .setCancelable(false)
                .setMessage("Please update application for better use.")
                .setPositiveButton("Update", (dialog1, which) -> redirectStore(updateUrl))
                .create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void closeScreen() {
        AppDebugLog.print("Firebase Token : " + session.getLoginData(SessionManager.KEY_DEVICE_TOKEN));

        Intent intent = null;
        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
            intent = new Intent(MyApplication.getInstance(), DashboardActivity.class);
            intent.putExtra("isFromSplash", true);
            startActivity(intent);
            finish();
        } else {

            if (session.isIntroDone()) {
                intent = new Intent(MyApplication.getInstance(), WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(MyApplication.getInstance(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void startTimer() {
        CountDownTimer lTimer = new CountDownTimer(300, 300) {
            public void onFinish() {
                closeScreen();
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }
        }.start();
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
}
