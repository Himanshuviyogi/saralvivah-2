package com.advance.sanatani_vivah.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.advance.sanatani_vivah.fcm.NotificationChannels;
import com.advance.sanatani_vivah.utility.FontsOverride;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;
    private static JSONObject spinData;
    private static String spinDataStr;
    private static boolean isApprove=true;
    private static boolean is_plan=false;
    private Activity currentActivity;
    public static boolean isFromAddedReview=false;

    private static Context context;

    private FirebaseAnalytics mFirebaseAnalytics;

    private SessionManager session;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mInstance = this;
        session = new SessionManager(context);
        setupActivityListener();
        //TODO Fixed Picasso error
        setPicassoSingleInstance();

        FontsOverride.overrideFont(getApplicationContext(), "SERIF", "font/roboto_regular.ttf");

        FirebaseApp.initializeApp(context);

        NotificationChannels notificationChannels = new NotificationChannels();
        notificationChannels.createNotificationChannels(context);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "new event");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static Context getAppContext() {
        //AppDebugLog.print("context : " + context);
        return context;
    }

    public void setPicassoSingleInstance() {
        try {
            Picasso.setSingletonInstance(new Picasso.Builder(context).build());
        }catch (Exception e){

        }
    }

    public static Context getContext() {
        return context;
    }


    public ViewGroup getCurrentView(){
       // ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        //omponentName cn = am.getRunningTasks(1).get(0).topActivity;

       // Log.e("resp",cn.toString());
       return currentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public Activity getCurrentActivity(){
        return this.currentActivity;
    }

    public static boolean isApprove() {
        return isApprove;
    }

    public static void setApprove(boolean approve) {
        isApprove = approve;
    }

    public static JSONObject getSpinData() {
        return spinData;
    }

    public static void setSpinData(JSONObject spinData) {

        try {
            MyApplication.spinData = spinData.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getSpinDataStr() {
        return spinDataStr;
    }

    public static void setSpinDataStr(String spinData) {
        MyApplication.spinDataStr = spinData;
    }

    public static void setPlan(boolean is_plan){
        MyApplication.is_plan=is_plan;
    }

    public static boolean getPlan(){
        return is_plan;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppDebugLog.print("in onActivityCreated");
            }
            @Override
            public void onActivityStarted(Activity activity) {
                AppDebugLog.print("in onActivityStarted");
                if (++activityReferences == 1 && !isActivityChangingConfigurations && session.isLoggedIn()) {
                    // App enters foreground
                    AppDebugLog.print("App enters foreground");
                    statusChangeRequest("online");
                }
            }
            @Override
            public void onActivityResumed(Activity activity) {
                AppDebugLog.print("in onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                AppDebugLog.print("in onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                isActivityChangingConfigurations = activity.isChangingConfigurations();
                if (--activityReferences == 0 && !isActivityChangingConfigurations) {
                    // App enters background
                    AppDebugLog.print("App enters background");
                    statusChangeRequest("offline");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                AppDebugLog.print("in onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppDebugLog.print("in onActivityDestroyed");
            }
        });
    }


    private void statusChangeRequest(String status) {
        HashMap<String, String> param = new HashMap<>();
        param.put("status", status);
        Common common = new Common(this);
        common.makePostRequest(AppConstants.STATUS_CHANGE_REQUEST, param, response -> {
            try {
                AppDebugLog.print("response in change status : "+response);
            } catch (Exception e) {
                e.printStackTrace();
                AppDebugLog.print("response in change status : "+response);
            }
        }, error -> {

        });
    }
}
