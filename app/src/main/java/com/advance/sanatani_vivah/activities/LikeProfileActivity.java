package com.advance.sanatani_vivah.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.sanatani_vivah.adapter.LikeListAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LikeProfileActivity extends AppCompatActivity implements LikeListAdapter.ItemListener {
    private RecyclerView recyclerView;
    private List<DashboardItem> list = new ArrayList<>();
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;
    private boolean continue_request;
    private TextView tv_no_data;
    private LikeListAdapter adapter;
    private int page = 0;

    private int currentVisibleItemCount;
    private int currentFirstVisibleItem;
    private int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Liked Profile");

        common = new Common(this);
        session = new SessionManager(this);

        loader = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerView);
        tv_no_data = findViewById(R.id.tv_no_data);

        initializeRecyclerView();
        page = page + 1;
        getListData(page);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new LikeListAdapter(this, list);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    currentVisibleItemCount = mLayoutManager.getChildCount();
                    totalItem = mLayoutManager.getItemCount();
                    currentFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (continue_request) {
                        if ((currentVisibleItemCount + currentFirstVisibleItem) >= totalItem) {
                            continue_request = false;
                            if (loader != null)
                                common.hideProgressRelativeLayout(loader);
                            page = page + 1;
                            getListData(page);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().cancelPendingRequests("req");
        super.onBackPressed();
    }

    private void getListData(int page) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("action", "like");

        common.makePostRequestTime(AppConstants.like_profile_list + page, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                int total_count = object.getInt("total_count");
                if (total_count != 0) {
                    tv_no_data.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    continue_request = object.getBoolean("continue_request");
                    if (list.size() != total_count) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);

                            DashboardItem item = new DashboardItem();
                            item.setId(obj.getString("id"));
                            item.setMatri_id(obj.getString("matri_id"));
                            item.setName(obj.getString("username"));

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String description = Common.getDetailsFromValue(obj.getString("profileby"),common.getAge(obj.getString("birthdate"), sdf) + " Years ", obj.getString("height"),
                                        obj.getString("caste_name"), obj.getString("religion_name"),
                                        obj.getString("city_name"), obj.getString("country_name"),obj.getString("education_name"));
                            item.setAbout(description);
                            item.setImage_approval(obj.getString("photo1_approve"));
                            item.setImage(obj.getString("photo1"));
                            item.setUser_id(obj.getString("user_id"));
                            item.setPhoto_view_status(obj.getString("photo_view_status"));
                            item.setPhoto_view_count(obj.getString("photo_view_count"));
                            item.setBadge(obj.getString("badge"));
                            item.setBadgeUrl(obj.getString("badgeUrl"));
                            item.setColor(obj.getString("color"));
                            item.setPhotoUrl(obj.getString("photoUrl"));

                            list.add(item);
                        }
                        if(list.size() < 10) continue_request = false;
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
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

    private void sendRequest(String int_msg, String matri_id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("interest_message", int_msg);
        param.put("receiver_id", matri_id);
        param.put("requester_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequestTime(AppConstants.photo_password_request, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                Toast.makeText(getApplicationContext(), object.getString("errmessage"), Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override public void itemClicked(DashboardItem object, int position) {

    }

    @Override public void removeLike(int position, String matriId) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("like_status", "No");
        param.put("other_id", matriId);

        common.makePostRequestTime(AppConstants.like_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            AppDebugLog.print("response in removeLike : " + response);
            try {
                JSONObject object = new JSONObject(response);

                if (object.getString("status").equals("success")) {
                    common.showAlert("Unlike", object.getString("errmessage"), R.drawable.heart_fill_pink);
                    // for refresh dashboard
                    ApplicationData.getSharedInstance().isProfileChanged = true;
                    list.remove(position);
                    if (list.size() == 0) {
                        tv_no_data.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }

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

    @Override public void alertPhotoPassword(final String matri_id) {
        final String[] arr = new String[]{"We found your profile to be a good match. Please accept photo password request to proceed further.",
                "I am interested in your profile. I would like to view photo now, accept photo request."};
        final String[] selected = {"We found your profile to be a good match. Please accept photo password request to proceed further."};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(LikeProfileActivity.this);

        alt_bld.setTitle("Photos View Request");
        alt_bld.setSingleChoiceItems(arr, 0, (dialog, item) -> {

            //dialog.dismiss();// dismiss the alertbox after chose option
            selected[0] = arr[item];
        });
        alt_bld.setPositiveButton("Send", (dialogInterface, i) -> sendRequest(selected[0], matri_id));
        alt_bld.setNegativeButton("Cancel", (dialogInterface, i) -> {
            //alertpassword(password,url);
        });
        AlertDialog alert = alt_bld.create();
        alert.show();

    }
}
