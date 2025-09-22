package com.advance.sanatani_vivah.utility;

import android.annotation.SuppressLint;

import com.advance.sanatani_vivah.application.MyApplication;


/**
 * All com temporary data stored in this class
 * Created by Nasirali on 02-02-2019.
 */


@SuppressLint("SimpleDateFormat")
public class ApplicationData {
    private static ApplicationData sharedInstance;
    private static SessionManager sessionManager;
    public boolean isProfileChanged = false;
    public static boolean isOTPProcess = false;
    public static String myProfileStarStr = "";
    public static String myProfileMoonSignStr = "";
    public static String currentChatUser = "";

    private int displayWidth = 0;
    private int displayHeight = 0;

    public static String currentChatMatriId = "";

    public static boolean isAllowVideoCall = false;
    public static boolean isAllowAudioCall = false;

    // new 07-12-2023 Maherbanali Suthar
    public static boolean isCallByMe = false;
    public static long totalCallDuration = 0;
    public static int callType = 0;

    public static int totalVideoCallSecond = 0;
    public static int totalAudioCallSecond = 0;

    private void initialize() {

    }

    private ApplicationData() {

    }

    public static ApplicationData getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ApplicationData();
            sharedInstance.initialize();
        }
        return sharedInstance;
    }

    public static SessionManager getSession() {
        return sessionManager = new SessionManager(MyApplication.getAppContext());
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }
}
