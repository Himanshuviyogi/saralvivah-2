package com.advance.sanatani_vivah.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

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
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.ContactUsActivity;
import com.advance.sanatani_vivah.activities.CustomMatchActivity;
import com.advance.sanatani_vivah.activities.DashboardActivity;
import com.advance.sanatani_vivah.activities.DeleteProfileActivity;
import com.advance.sanatani_vivah.activities.ExpressInterestActivity;
import com.advance.sanatani_vivah.activities.LikeProfileActivity;
import com.advance.sanatani_vivah.activities.ManageAccountActivity;
import com.advance.sanatani_vivah.activities.ManagePhotosActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.activities.QuickMessageActivity;
import com.advance.sanatani_vivah.activities.ShortlistedProfileActivity;
import com.advance.sanatani_vivah.activities.SuccessStoryActivity;
import com.advance.sanatani_vivah.activities.ViewMyProfileActivity;
import com.advance.sanatani_vivah.activities.ViewedProfileActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class DashboardFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private DashboardActivity activity;
    private View view;
    private SessionManager session;
    private Common common;
    private int placeHolder;
    private RelativeLayout loader;

    private ImageView imgProfile;
    private TextView lblCard1, lblCard1Sub, lblCard2, lblCard2Sub,
            lblCard3, lblCard3Sub, lblCard4, lblCard4Sub;
    private TextView tvName, tvMatriId, tvDesignation, tvViewProfile, btnQuickSetting,
            btnCustomerSupport, btnSucessStory, btnPrivacySettings, btnDeleteProfile, btnViewedProfile, btnSetCurrentLocation;

    private NestedScrollView layoutNestedScrollView;
    // private CardView btnMyPhotos;
    private TextView btnViewAllCard1, btnViewAllCard2, btnViewAllCard3, btnViewAllCard4;
    private LinearLayout goToInBox, goToShortList, goToMatches, goToInterest;
    private LinearLayout layoutQuickSetting;
    private TextView txtFocus;
    private CardView layoutGoToMembership;
    private Button btnBecomePremiumMember;
    private TextView lblInboxCount, lblShortlistCount, lblMatchesCount, lblInterestCount;
//    private CardView layoutInboxCount, layoutShortlistCount, layoutMatchesCount, layoutInterestCount;
    //
    private ImageView img1, img2, img3, img4;
    //location
    private final int TAG_PERMISSIONS = 124;
    private final int GPS_REQUEST = 333;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private LocationRequest locationRequest;

    private boolean isFirstTimeApiCall = true;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_new, container, false);
        initialize();
        return view;
    }

    public void initialize() {

        activity = ((DashboardActivity) getActivity());
        session = new SessionManager(activity);
        common = new Common(activity);

        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            placeHolder = R.drawable.female;
        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
            placeHolder = R.drawable.male;
        }

        layoutGoToMembership = view.findViewById(R.id.layoutGoToMembership);
        btnBecomePremiumMember = view.findViewById(R.id.btnBecomePremiumMember);
        btnSetCurrentLocation = view.findViewById(R.id.btnSetCurrentLocation);

//        layoutInboxCount = view.findViewById(R.id.layoutInboxCount);
//        layoutShortlistCount = view.findViewById(R.id.layoutShortlistCount);
//        layoutMatchesCount = view.findViewById(R.id.layoutMatchesCount);
//        layoutInterestCount = view.findViewById(R.id.layoutInterestCount);

        lblInboxCount = view.findViewById(R.id.lblInboxCount);
        lblShortlistCount = view.findViewById(R.id.lblShortlistCount);
        lblMatchesCount = view.findViewById(R.id.lblMatchesCount);
        lblInterestCount = view.findViewById(R.id.lblInterestCount);

        loader = view.findViewById(R.id.loader);

        btnViewedProfile = view.findViewById(R.id.btnViewedProfile);

        imgProfile = view.findViewById(R.id.imgProfile);
        imgProfile.setImageResource(placeHolder);
        //btnMyPhotos = view.findViewById(R.id.btnMyPhotos);
        tvName = view.findViewById(R.id.tvName);
        tvMatriId = view.findViewById(R.id.tvMatriId);
        tvDesignation = view.findViewById(R.id.tvDesignation);
        tvViewProfile = view.findViewById(R.id.tvViewProfile);
        btnCustomerSupport = view.findViewById(R.id.btnCustomerSupport);
        layoutNestedScrollView = view.findViewById(R.id.layoutNestedScrollView);
        btnPrivacySettings = view.findViewById(R.id.btnPrivacySettings);
        btnDeleteProfile = view.findViewById(R.id.btnDeleteProfile);
        btnQuickSetting = view.findViewById(R.id.btnQuickSetting);

        lblCard1 = view.findViewById(R.id.lblCard1);
        lblCard1Sub = view.findViewById(R.id.lblCard1Sub);
        lblCard2 = view.findViewById(R.id.lblCard2);
        lblCard2Sub = view.findViewById(R.id.lblCard2Sub);
        lblCard3 = view.findViewById(R.id.lblCard3);
        lblCard3Sub = view.findViewById(R.id.lblCard3Sub);
        lblCard4 = view.findViewById(R.id.lblCard4);
        lblCard4Sub = view.findViewById(R.id.lblCard4Sub);

        btnViewAllCard1 = view.findViewById(R.id.btnViewAllCard1);
        btnViewAllCard2 = view.findViewById(R.id.btnViewAllCard2);
        btnViewAllCard3 = view.findViewById(R.id.btnViewAllCard3);
        btnViewAllCard4 = view.findViewById(R.id.btnViewAllCard4);

        txtFocus = view.findViewById(R.id.txtFocus);
        btnSucessStory = view.findViewById(R.id.btnSucessStory);
        layoutQuickSetting = view.findViewById(R.id.layoutQuickSetting);

        goToInBox = view.findViewById(R.id.goToInBox);
        goToShortList = view.findViewById(R.id.goToShortList);
        goToMatches = view.findViewById(R.id.goToMatches);
        goToInterest = view.findViewById(R.id.goToInterest);

        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);

        //getMyprofile();

        //btnMyPhotos.setOnClickListener(this);
        tvViewProfile.setOnClickListener(this);
        goToInBox.setOnClickListener(this);
        goToShortList.setOnClickListener(this);
        goToMatches.setOnClickListener(this);
        goToInterest.setOnClickListener(this);
        btnViewAllCard1.setOnClickListener(this);
        btnViewAllCard2.setOnClickListener(this);
        btnViewAllCard3.setOnClickListener(this);
        btnViewAllCard4.setOnClickListener(this);
        btnQuickSetting.setOnClickListener(this);
        btnCustomerSupport.setOnClickListener(this);
        btnPrivacySettings.setOnClickListener(this);
        btnDeleteProfile.setOnClickListener(this);
        btnSucessStory.setOnClickListener(this);
        btnBecomePremiumMember.setOnClickListener(this);
        btnSetCurrentLocation.setOnClickListener(this);
    }

    @Override public void onResume() {
        super.onResume();
        AppDebugLog.print("in dashboard fragment");
        getMyprofile();
    }

    private void getMyprofile() {
        if (isFirstTimeApiCall) {
            common.showProgressRelativeLayout(loader);
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        common.makePostRequest(AppConstants.get_my_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            AppDebugLog.print("get_my_profile response : " + response.toString());
            try {
                JSONObject object = new JSONObject(response);
                JSONObject data = object.getJSONObject("data");

                tvName.setText(data.getString("username"));
                tvMatriId.setText(data.getString("matri_id"));
                tvDesignation.setText(data.getString("designation_name"));

                if (!data.getString("photo1").equals(""))
                    Picasso.get().load(data.getString("photo1")).placeholder(placeHolder).error(placeHolder).into(imgProfile);
                else {
                    imgProfile.setImageResource(placeHolder);
                }

                lblInboxCount.setVisibility(View.GONE);
                lblMatchesCount.setVisibility(View.GONE);
                lblInterestCount.setVisibility(View.GONE);
                lblShortlistCount.setVisibility(View.GONE);

                if (data.has("unread_message_count") && data.getString("unread_message_count").length() > 0) {
                    String unreadMsgCount = data.getString("unread_message_count");
                    if (!unreadMsgCount.equalsIgnoreCase("0")) {
                        lblInboxCount.setText(unreadMsgCount);
                        lblInboxCount.setVisibility(View.VISIBLE);
                    }
                }
                if (data.has("member_match_count") && data.getString("member_match_count").length() > 0) {
                    String matchCount = data.getString("member_match_count");
                    if (!matchCount.equalsIgnoreCase("0")) {
                        lblMatchesCount.setText(matchCount);
                        lblMatchesCount.setVisibility(View.VISIBLE);
                    }
                }
                if (data.has("interest_received_count") && data.getString("interest_received_count").length() > 0) {
                    String interestSentCount = data.getString("interest_received_count");
                    if (!interestSentCount.equalsIgnoreCase("0")) {
                        lblInterestCount.setText(interestSentCount);
                        lblInterestCount.setVisibility(View.VISIBLE);
                    }
                }
                if (data.has("interest_sent_count") && data.getString("interest_sent_count").length() > 0) {
                    String interestSentCount = data.getString("interest_sent_count");
                    lblCard1.setText("Interests sent (" + interestSentCount + ")");
                    lblCard1Sub.setText("You have sent " + interestSentCount + " interest(s).");
                }
                if (data.has("my_profile_view_by_other") && data.getString("my_profile_view_by_other").length() > 0) {
                    String otherUserCount = data.getString("my_profile_view_by_other");
                    lblCard2.setText("My profile viewed (" + otherUserCount + ")");
                    lblCard2Sub.setText("My profile viewed by " + otherUserCount + " other profile(s).");
                }
                if (data.has("member_likes") && data.getString("member_likes").length() > 0) {
                    String memberLikes = data.getString("member_likes");
                    lblCard3.setText("Liked Profile (" + memberLikes + ")");
                    lblCard3Sub.setText("You have " + memberLikes + " liked profile(s).");
                }
                if (data.has("shortlist_count") && data.getString("shortlist_count").length() > 0) {
                    String shortlistCount = data.getString("shortlist_count");
                    if (!shortlistCount.equalsIgnoreCase("0")) {
                        lblShortlistCount.setText(shortlistCount);
                        lblShortlistCount.setVisibility(View.VISIBLE);
                    }
                    lblCard4.setText("Shortlisted Profile (" + shortlistCount + ")");
                    lblCard4Sub.setText("You have " + shortlistCount + " shortlisted profile(s).");
                }

                if (isFirstTimeApiCall) {
                    getCurrentPlan();
                }
                activity.setMyProfile(data);
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void getCurrentPlan() {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        common.makePostRequest(AppConstants.check_plan, param, response -> {
            AppDebugLog.print("plan response : " + response);
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                MyApplication.setPlan(object.getBoolean("is_show"));

                if (object.getBoolean("is_show")) {
                    layoutGoToMembership.setVisibility(View.GONE);
                } else {
                    layoutGoToMembership.setVisibility(View.VISIBLE);
                }

                isFirstTimeApiCall = false;
            } catch (JSONException e) {
                common.hideProgressRelativeLayout(loader);
                e.printStackTrace();
            }
            //  pd.dismiss();
        }, error -> {
            common.hideProgressRelativeLayout(loader);
        });
    }

    private void setCurrentLocation(Location currentLocation) {
        if (currentLocation != null) {
            common.showProgressRelativeLayout(loader);
            HashMap<String, String> param = new HashMap<>();
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            param.put("latitude", String.valueOf(currentLocation.getLatitude()));
            param.put("longitude", String.valueOf(currentLocation.getLongitude()));
            common.makePostRequest(AppConstants.update_location, param, response -> {
                AppDebugLog.print("update location response : " + response);
                common.hideProgressRelativeLayout(loader);
                try {
                    JSONObject object = new JSONObject(response);

                } catch (JSONException e) {
                    common.hideProgressRelativeLayout(loader);
                    e.printStackTrace();
                }
                //  pd.dismiss();
            }, error -> {
                common.hideProgressRelativeLayout(loader);
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyPhotos:
                startActivity(new Intent(activity, ManagePhotosActivity.class));
                break;

            case R.id.tvViewProfile:
                startActivity(new Intent(activity, ViewMyProfileActivity.class));
                break;

            case R.id.btnBecomePremiumMember:
                startActivity(new Intent(activity, PlanListActivity.class));
                break;

            case R.id.goToInBox:
                Intent intent = new Intent(activity, QuickMessageActivity.class);
                setBgOnClick(img1, goToInBox, intent);
                break;

            case R.id.goToShortList:
                Intent intent1 = new Intent(activity, ShortlistedProfileActivity.class);
                setBgOnClick(img2, goToShortList, intent1);
                break;

            case R.id.goToMatches:
                Intent intent2 = new Intent(activity, CustomMatchActivity.class);
                intent2.putExtra("sub_menu_title", "Recommended Match");
                setBgOnClick(img3, goToMatches, intent2);
                break;

            case R.id.goToInterest:
                Intent intent3 = new Intent(activity, ExpressInterestActivity.class);
                intent3.putExtra("interest_tag", "receive");
                setBgOnClick(img4, goToInterest, intent3);
                break;

            case R.id.btnViewAllCard1:
                Intent intent4 = new Intent(activity, ExpressInterestActivity.class);
                startActivity(intent4);
                break;

            case R.id.btnViewAllCard2:
                Intent intent5 = new Intent(activity, ViewedProfileActivity.class);
                intent5.putExtra(AppConstants.KEY_INTENT, "my_profile");
                startActivity(intent5);
                break;

            case R.id.btnViewAllCard3:
                startActivity(new Intent(activity, LikeProfileActivity.class));
                break;

            case R.id.btnViewAllCard4:
                startActivity(new Intent(activity, ShortlistedProfileActivity.class));
                break;

            case R.id.btnQuickSetting:
                if (layoutQuickSetting.getVisibility() == View.VISIBLE) {
                    layoutQuickSetting.setVisibility(View.GONE);
                    txtFocus.clearFocus();
                } else {
                    layoutQuickSetting.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        txtFocus.requestFocus();
                        int height = (layoutQuickSetting.getHeight() / 2) * Common.convertDpToPixels(100, requireContext());
                        layoutNestedScrollView.scrollTo(0, layoutNestedScrollView.getBottom() + height);
                    }, 1);
                }
                break;

            case R.id.btnCustomerSupport:
                startActivity(new Intent(activity, ContactUsActivity.class));
                break;

            case R.id.btnPrivacySettings:
                startActivity(new Intent(activity, ManageAccountActivity.class));
                break;

            case R.id.btnDeleteProfile:
                startActivity(new Intent(activity, DeleteProfileActivity.class));
                break;

            case R.id.btnSucessStory:
                startActivity(new Intent(activity, SuccessStoryActivity.class));
                break;

            case R.id.btnSetCurrentLocation:
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Update Location");
                alert.setMessage("This action set your current location for get better matches in near you.");
                alert.setPositiveButton("Set", (dialogInterface, i) -> getLocationAccess());
                alert.setNegativeButton("Cancel", null);
                alert.show();
                break;
        }
    }

    @AfterPermissionGranted(TAG_PERMISSIONS)
    private void getLocationAccess() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (EasyPermissions.hasPermissions(activity, perms)) {
            createLocationRequest();
        } else {
            // Do not have permissions, request them now
            ActivityCompat.requestPermissions(activity, perms, TAG_PERMISSIONS);
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
        SettingsClient client = LocationServices.getSettingsClient(activity);
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
                    ((ResolvableApiException) e).startResolutionForResult(activity, GPS_REQUEST);
                } catch (IntentSender.SendIntentException sendIntentException) {
                    sendIntentException.printStackTrace();
                }
            }
        });
    }

    private void getCurrentLocationFromFusedLocationLibrary() {
        //fusion library
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
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
                    setCurrentLocation(currentLocation);
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

    private void setBgOnClick(ImageView img, LinearLayout view, Intent intent) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                img.setBackground(ContextCompat.getDrawable(activity, R.drawable.background_with_blue_shadow));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                img.setBackground(ContextCompat.getDrawable(activity, R.drawable.background_with_shadow3));
                startActivity(intent);
            }
        });
        alphaAnimator.setDuration(200);
        alphaAnimator.start();
    }
}