package com.advance.sanatani_vivah.utility;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.advance.sanatani_vivah.R;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.advance.sanatani_vivah.activities.LoginActivity;
import com.advance.sanatani_vivah.application.MyApplication;

import java.util.HashMap;


public class SessionManager {
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MegaPref";

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_INTRO_DONE = "IsIntroDone";

    public static final String KEY_PASSWORD = "password";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_BASE_URL = "base_url";
    public static final String KEY_LOGINID = "loginId";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_username = "userName";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_MATRI_ID = "Matri_id";
    public static final String KEY_PLAN_STATUS = "plan_status";
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String TOKEN = "token";
    public static final String HEADER_TOKEN = "headertoken";
    public static final String TIME_ZONE = "timezone";
    public static final String LOGIN_WITH = "login_with";
    public static final String FB_ID = "fb_id";
    public static final String GOOGLE_ID = "google_id";
    public static final String DEVICE_ID = "Muslim_Firebase_id";
    public static final String SKIP_ID = "skip_id";
    public static final String DRAWER_MENU_DATA = "drawer_menu_data";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String password, String user_id) {//String password,
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        //editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_LOGINID, email);
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();
    }

    public void createLoginSession(String user_id){//String password,
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        //editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_USER_ID, user_id);

        // commit changes
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    public String getLoginData(String key) {
        return pref.getString(key, "");
    }

    public void setUserData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getBaseUrl() {
        return pref.getString(KEY_BASE_URL, "");
    }

    public void setBaseUrl(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    //TODO if other user profile image not available then this method is given other user gender related place holder.
    public int getPlaceHolderDrawableId() {
        int placeHolderImageId;
        if (getLoginData(SessionManager.KEY_GENDER).equalsIgnoreCase("Male")) {
            placeHolderImageId = R.drawable.female;
        } else {
            placeHolderImageId = R.drawable.male;
        }
        return placeHolderImageId;
    }

    //TODO if user own profile image not available then this method is given own gender related place holder.
    public int getMyPlaceHolderDrawableId() {
        int placeHolderImageId;
        if (getLoginData(SessionManager.KEY_GENDER).equalsIgnoreCase("Male")) {
            placeHolderImageId = R.drawable.male;
        } else {
            placeHolderImageId = R.drawable.female;
        }
        return placeHolderImageId;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        new Thread(() -> {
            //TODO DELETE FCM Token
            FirebaseInstallations.getInstance().delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AppDebugLog.print("Installation deleted");
                        } else {
                            AppDebugLog.print("Unable to delete Installation");
                        }
                    });

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            AppDebugLog.print("Fetching FCM registration token failed : " + task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        new SessionManager(MyApplication.getInstance()).setUserData(KEY_DEVICE_TOKEN, token);
                        AppDebugLog.print("new tocken : " + token);
                    });

        }).start();

        Intent i = new Intent(_context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isIntroDone() {
        return pref.getBoolean(IS_INTRO_DONE, false);
    }

    public void setIntroDone() {
        editor.putBoolean(IS_INTRO_DONE, true);
        editor.commit();
    }

    /**
     * Save and get Bean in SharedPreference
     */
    public void saveDrawerMenuArrayObject(String data) {
        this.editor.putString(DRAWER_MENU_DATA, data);
        this.editor.apply();     // This line is IMPORTANT !!!
    }

    public String getDrawerMenuArrayObject() {
        return pref.getString(DRAWER_MENU_DATA, null);
    }

}