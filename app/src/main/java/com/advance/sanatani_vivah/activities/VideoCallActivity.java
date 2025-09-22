package com.advance.sanatani_vivah.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.internal.IncomingCallButtonListener;
import com.zegocloud.uikit.prebuilt.call.invite.internal.OutgoingCallButtonListener;
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType;
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser;
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.ArrayList;
import java.util.List;

public class VideoCallActivity extends AppCompatActivity {

    private String otherUserID = ""; //NI2

    private boolean isVoiceCall = false;
    private boolean isVideoCall = false;

    ZegoSendCallInvitationButton newVideoCall;
    ZegoSendCallInvitationButton newVoiceCall;

    private IntentFilter intentFilter;
    private BroadcastReceiver br;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setStatusBarColor(ContextCompat.getColor(VideoCallActivity.this, R.color.black));
        View decorView = getWindow().getDecorView(); //set status background black
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set status text  light

        LocalBroadcastManager.getInstance(this).registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Handler().postDelayed(() -> {
            //finish();
        }, 5000);
    }

    public class FinishActivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler().postDelayed(() -> {
                finish();
            }, 200);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_video_call);

        intentFilter = new IntentFilter(AppConstants.AV_CALL_FINISH_ACTIVITY);
        br = new FinishActivityBroadcastReceiver();

        newVideoCall = findViewById(R.id.new_video_call);
        newVoiceCall = findViewById(R.id.new_voice_call);

        otherUserID = getIntent().getStringExtra("other_matri_id");

        AppDebugLog.print("otherUserID : "+otherUserID);

        isVoiceCall = getIntent().getBooleanExtra("is_voice_call", false);
        isVideoCall = getIntent().getBooleanExtra("is_video_call", false);

        initCallInviteService();
        initVideoButton();
        initVoiceButton();

        new Handler().postDelayed(() -> {
            if (isVideoCall) {
                newVideoCall.performClick();
            } else {
                newVoiceCall.performClick();
            }
        }, 100);

    }

    public void initCallInviteService() {
        String myUserID = new SessionManager(this).getLoginData(SessionManager.KEY_MATRI_ID);
        String userID = myUserID;
        String userName = "user_" + myUserID;

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
//        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;

        // ringtones
        callInvitationConfig.incomingCallRingtone = "incoming_call";
        callInvitationConfig.outgoingCallRingtone = "outgoing_call";

        // button text
        callInvitationConfig.innerText.incomingCallPageDeclineButton = "Decline";
        callInvitationConfig.innerText.incomingCallPageAcceptButton = "Accept";

        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), AppConstants.appID, AppConstants.appSign, userID, userName,
                callInvitationConfig);

        ZegoUIKitPrebuiltCallInvitationService.addIncomingCallButtonListener(new IncomingCallButtonListener() {
            @Override
            public void onIncomingCallDeclineButtonPressed() {
                AppDebugLog.print("onIncomingCallDeclineButtonPressed : ");
                //finish();
                //AVCallFinishActivity();
            }

            @Override
            public void onIncomingCallAcceptButtonPressed() {
                AppDebugLog.print("onIncomingCallAcceptButtonPressed : ");
            }
        });

        ZegoUIKitPrebuiltCallInvitationService.addOutgoingCallButtonListener(new OutgoingCallButtonListener() {
            @Override
            public void onOutgoingCallCancelButtonPressed() {
                AppDebugLog.print("onOutgoingCallCancelButtonPressed : ");
                finish();
                //AVCallFinishActivity();
            }
        });

        ZegoUIKitPrebuiltCallInvitationService.addInvitationCallListener(new ZegoInvitationCallListener() {
            @Override
            public void onIncomingCallReceived(String callID, ZegoCallUser caller, ZegoCallType callType, List<ZegoCallUser> callees) {
                AppDebugLog.print("onIncomingCallReceived : ");
                //finish();
            }

            @Override
            public void onIncomingCallCanceled(String callID, ZegoCallUser caller) {
                AppDebugLog.print("onIncomingCallCanceled : ");
                //finish();
                //AVCallFinishActivity();
            }

            @Override
            public void onIncomingCallTimeout(String callID, ZegoCallUser caller) {
                AppDebugLog.print("onIncomingCallTimeout : ");
                //finish();
                //AVCallFinishActivity();
            }

            @Override
            public void onOutgoingCallAccepted(String callID, ZegoCallUser callee) {
                AppDebugLog.print("onOutgoingCallAccepted : ");
                //finish();
            }

            @Override
            public void onOutgoingCallRejectedCauseBusy(String callID, ZegoCallUser callee) {
                AppDebugLog.print("onOutgoingCallRejectedCauseBusy : ");
                finish();
                //AVCallFinishActivity();
            }

            @Override
            public void onOutgoingCallDeclined(String callID, ZegoCallUser callee) {
                AppDebugLog.print("onOutgoingCallDeclined : ");
                finish();
                //AVCallFinishActivity();
            }

            @Override
            public void onOutgoingCallTimeout(String callID, List<ZegoCallUser> callees) {
                AppDebugLog.print("onOutgoingCallTimeout : ");
                finish();
                //AVCallFinishActivity();
            }
        });
    }

    private void initVideoButton() {
        newVideoCall.setIsVideoCall(true);
        newVideoCall.setOnClickListener(v -> {
            String[] split = otherUserID.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
            for (String userID : split) {
                String userName = "user_" + userID;
                users.add(new ZegoUIKitUser(userID, userName));
            }
            newVideoCall.setInvitees(users);
        });
    }

    private void initVoiceButton() {
        newVoiceCall.setIsVideoCall(false);
        newVoiceCall.setOnClickListener(v -> {
            String[] split = otherUserID.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
            for (String userID : split) {
                String userName = "user_" + userID;
                users.add(new ZegoUIKitUser(userID, userName));
            }
            newVoiceCall.setInvitees(users);
        });
    }
}