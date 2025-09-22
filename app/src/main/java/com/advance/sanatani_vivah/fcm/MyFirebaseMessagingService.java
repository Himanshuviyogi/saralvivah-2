package com.advance.sanatani_vivah.fcm;

import static com.advance.sanatani_vivah.utility.AppConstants.CONVERSATION_LIST_UPDATE;
import static com.advance.sanatani_vivah.utility.AppConstants.MESSAGE_LIST_UPDATE;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.advance.sanatani_vivah.utility.ApplicationData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.advance.sanatani_vivah.activities.CurrentPlanActivity;
import com.advance.sanatani_vivah.activities.DashboardActivity;
import com.advance.sanatani_vivah.activities.ExpressInterestActivity;
import com.advance.sanatani_vivah.activities.ManagePhotosActivity;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.QuickMessageActivity;
import com.advance.sanatani_vivah.activities.UploadIdAndHoroscopeActivity;
import com.advance.sanatani_vivah.activities.UploadVideoActivity;
import com.advance.sanatani_vivah.activities.ViewMyProfileActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String KEY_OTHER_ID = "other_id";
    private static final String KEY_INTEREST_TAG = "interest_tag";
    private static final String KEY_MATRI_ID = "matri_id";
    private static final String KEY_PHOTO_PASSWORD_TAG = "ppassword_tag";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";
    private static final String KEY_NOTIFICATION_TYPE = "noti_type";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CHAT = "chat";
    private static final String KEY_PHOTO_URL = "photoUrl";
    private static final String KEY_CONVERSATION_ID = "conversation_id";
    public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String KEY_CHAT_MESSAGE = "chat_message";
    private static final String KEY_CHAT_DATE = "chat_date";
    private static final String KEY_CHAT_STATUS = "chat_status";

    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        AppDebugLog.print("FCM Token : " + s);
        SessionManager session = new SessionManager(this);
        session.setUserData(SessionManager.KEY_DEVICE_TOKEN, s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Log.e(TAG, "From: " +remoteMessage.getData());

        if (remoteMessage == null)
            return;

        AppDebugLog.print("remoteMessage in onMessageReceived : " + remoteMessage);
        handleDataMessage(remoteMessage);

    }

    private void handleDataMessage(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        if (data.containsKey(KEY_NOTIFICATION_TYPE)) {
            if (data.get(KEY_NOTIFICATION_TYPE).equals(KEY_MESSAGE)) {
                Intent pushNotification1 = new Intent(AppConstants.OUICK_TAG);
                pushNotification1.putExtra(KEY_MESSAGE, new HashMap<String, String>(data));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification1);

                Intent pushNotification2 = new Intent(AppConstants.CHAT_TAG);
                pushNotification2.putExtra(KEY_MESSAGE, new HashMap<String, String>(data));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification2);
            }

            if (Objects.requireNonNull(data.get(KEY_NOTIFICATION_TYPE)).equalsIgnoreCase(KEY_CHAT)) {
                Intent pushNotification1 = new Intent(AppConstants.CHAT_LIST_TAG);
                pushNotification1.putExtra(KEY_CHAT, new HashMap<String, String>(data));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification1);

                Intent pushNotification2 = new Intent(AppConstants.CHAT_CONVERSATION_TAG);
                pushNotification2.putExtra(KEY_CHAT, new HashMap<String, String>(data));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification2);
            }
        }

        //logout current user if login in other app with same credentials
        if (data.containsKey(KEY_NOTIFICATION_TYPE)) {
            AppDebugLog.print("Noti type : "+data.get(KEY_NOTIFICATION_TYPE));
            if (data.get(KEY_NOTIFICATION_TYPE).equals(KEY_MESSAGE)) {
                Intent pushNotification1 = new Intent(AppConstants.OUICK_TAG);
                pushNotification1.putExtra(KEY_MESSAGE, new HashMap<String, String>(data));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification1);

                Intent pushNotification2 = new Intent(AppConstants.CHAT_TAG);
                pushNotification2.putExtra(KEY_MESSAGE, new HashMap<String, String>(data));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification2);
            }

            if (Objects.requireNonNull(data.get(KEY_NOTIFICATION_TYPE)).equalsIgnoreCase(KEY_CHAT)) {
                if (data.containsKey(KEY_OTHER_ID)) {
                    AppDebugLog.print("Chat Message data : "+data.toString());
                    updateMemberList(getApplicationContext(),data.get(KEY_OTHER_ID));
                    updateConversationList(getApplicationContext(),
                            data.get(KEY_OTHER_ID),
                            data.get(KEY_CHAT_MESSAGE),
                            data.get(KEY_CHAT_DATE),
                            data.get(KEY_CONVERSATION_ID));
                    AppDebugLog.print("ApplicationData.currentChatMatriId in handleDataMessage : "+ ApplicationData.currentChatMatriId);
                    AppDebugLog.print("ApplicationData.currentChatMatriId in handleDataMessage : "+data.get(KEY_OTHER_ID));
                    AppDebugLog.print("ApplicationData.currentChatMatriId in handleDataMessage : "+data.get(KEY_CONVERSATION_ID));
                    if(ApplicationData.currentChatMatriId.equalsIgnoreCase(data.get(KEY_OTHER_ID))) {
                        return;
                    }
                }
            }

            if (Objects.requireNonNull(data.get(KEY_NOTIFICATION_TYPE)).equalsIgnoreCase(KEY_CHAT_STATUS)) {
                updateMemberList(getApplicationContext(),"");
                return;
            }
        }

        //logout current user if login in other app with same credentials
        if (Objects.requireNonNull(data.get(KEY_NOTIFICATION_TYPE)).equals("session_expire")) {
            SessionManager sessionManager = new SessionManager(MyApplication.getContext());
            sessionManager.logoutUser();
            return;
        }

        Log.e(TAG, "From " + data);
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        Intent pendinIntent = null;
        boolean isShowActionInNotification = false;

        switch (Objects.requireNonNull(data.get(KEY_NOTIFICATION_TYPE))) {
            case "payment_received":
                pendinIntent = new Intent(getApplicationContext(), CurrentPlanActivity.class);
                break;

            case "featured_profile":
                pendinIntent = new Intent(getApplicationContext(), ViewMyProfileActivity.class);
                break;

            case "profile_photo_approval":
                pendinIntent = new Intent(getApplicationContext(), ManagePhotosActivity.class);
                break;

            case "id_proof_photo_approval":
                pendinIntent = new Intent(getApplicationContext(), UploadIdAndHoroscopeActivity.class);
                break;

            case "video_approval":
                pendinIntent = new Intent(getApplicationContext(), UploadVideoActivity.class);
                break;

            case "plan_expired":
                pendinIntent = new Intent(getApplicationContext(), CurrentPlanActivity.class);
                break;

            case "viewed_contact_details":
                pendinIntent = new Intent(getApplicationContext(), OtherUserProfileActivity.class);
                break;

            case "viewed_profile":
                pendinIntent = new Intent(getApplicationContext(), OtherUserProfileActivity.class);
                break;

            case "add_shortlist":
                pendinIntent = new Intent(getApplicationContext(), OtherUserProfileActivity.class);
                break;

            case "interest_receive":
                pendinIntent = new Intent(getApplicationContext(), ExpressInterestActivity.class);
                break;

            case "message":
                pendinIntent = new Intent(getApplicationContext(), QuickMessageActivity.class);
                break;

            case "reminder_interest_receive":
                pendinIntent = new Intent(getApplicationContext(), ExpressInterestActivity.class);
                break;

            case "accepted_interest_receive":
                pendinIntent = new Intent(getApplicationContext(), ExpressInterestActivity.class);
                break;

            case "rejected_interest_receive":
                pendinIntent = new Intent(getApplicationContext(), ExpressInterestActivity.class);
                break;

            default:
                pendinIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                break;
        }

        if (data.containsKey(KEY_OTHER_ID))
            pendinIntent.putExtra(KEY_OTHER_ID, data.get(KEY_OTHER_ID));
        if (data.containsKey(KEY_INTEREST_TAG))
            pendinIntent.putExtra(KEY_INTEREST_TAG, data.get(KEY_INTEREST_TAG));
        if (data.containsKey(KEY_MATRI_ID))
            pendinIntent.putExtra(KEY_MATRI_ID, data.get(KEY_MATRI_ID));
        if (data.containsKey(KEY_PHOTO_PASSWORD_TAG))
            pendinIntent.putExtra(KEY_PHOTO_PASSWORD_TAG, data.get(KEY_PHOTO_PASSWORD_TAG));
        if (data.containsKey(KEY_CONVERSATION_ID))
            pendinIntent.putExtra("CONVERSATION_ID", data.get(KEY_CONVERSATION_ID));

        if (data.containsKey(KEY_PHOTO_URL)) {
            showNotificationMessageWithBigImage(getApplicationContext(), data.get(KEY_TITLE), data.get(KEY_BODY), ts.toString(), pendinIntent, data.get(KEY_PHOTO_URL));
        } else {
            showNotificationMessage(getApplicationContext(), data.get(KEY_TITLE), data.get(KEY_BODY), ts.toString(), pendinIntent);
        }

    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        Spanned spannedMsg = Html.fromHtml(message);
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, spannedMsg.toString(), timeStamp, intent);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void updateMemberList(Context context,String otherId) {
        Intent intent = new Intent(MESSAGE_LIST_UPDATE);
        intent.putExtra("OTHER_MATRI_ID",otherId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void updateConversationList(Context context,String otherId,String msg,String date,String conversationId) {
        Intent intent = new Intent(CONVERSATION_LIST_UPDATE);
        intent.putExtra("OTHER_MATRI_ID",otherId);
        intent.putExtra("CONVERSATION_ID",conversationId);
        intent.putExtra("MESSAGE",msg);
        intent.putExtra("DATE",date);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
