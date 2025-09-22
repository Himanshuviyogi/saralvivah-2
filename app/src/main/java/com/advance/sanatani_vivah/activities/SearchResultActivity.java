package com.advance.sanatani_vivah.activities;

import android.app.AlertDialog;
import android.os.Bundle;

import com.advance.sanatani_vivah.model.DashboardItemNew;
import com.advance.sanatani_vivah.retrofit.ApiRequestResponse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.advance.sanatani_vivah.adapter.CommonListAdapterNew;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements CommonListAdapterNew.ItemListener, ApiRequestResponse.AppApiRequestCallbacks {
    private RecyclerView recyclerView;
    private HashMap<String, String> param;
    private Common common;
    private List<DashboardItemNew> list = new ArrayList<>();
    private SessionManager session;
    private RelativeLayout loader;
    private CommonListAdapterNew adapter;
    private boolean continue_request;
    private int page = 0;
    private TextView tv_no_data;

    private int currentVisibleItemCount;
    private int currentFirstVisibleItem;
    private int totalItem;

    Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Result");

        common = new Common(this);
        session = new SessionManager(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("searchData")) {
                param = Common.getBeanObjectFromJsonString(getIntent().getStringExtra("searchData"));
                AppDebugLog.print("param in SearchResultActivity : " + param.toString());
            }
        }

        loader = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerView);
        tv_no_data = findViewById(R.id.tv_no_data);

        initializeRecyclerView();
        page = page + 1;
        getData(page);
    }

    @Override
    public void onPause() {
        common.hideProgressRelativeLayout(loader);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        common.hideProgressRelativeLayout(loader);
        super.onDestroy();
    }

    private void initializeRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CommonListAdapterNew(this, list);
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
                            getData(page);
                        }
                    }
                }
            }
        });
    }

    private void getData(int page) {
        common.showProgressRelativeLayout(loader);

        ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
        apiRequestResponse.postRequest(this, AppConstants.search_result + page, param, this, "SEARCH_RESULT");

    }

    private void sendRequest(String int_msg, String matri_id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("interest_message", int_msg);
        param.put("receiver_id", matri_id);
        param.put("requester_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequest(AppConstants.photo_password_request, param, response -> {
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
    public void alertPhotoPassword(String matri_id) {
        final String[] arr = new String[]{"We found your profile to be a good match. Please accept photo password request to proceed further.",
                "I am interested in your profile. I would like to view photo now, accept photo request."};
        final String[] selected = {"We found your profile to be a good match. Please accept photo password request to proceed further."};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

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

    @Override
    public void likeRequest(final String tag, String matri_id, int index) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("other_id", matri_id);
        param.put("like_status", tag);

        common.makePostRequest(AppConstants.like_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (tag.equals("Yes")) {
                    common.showAlert("Like", object.getString("errmessage"), R.drawable.heart_fill_pink);
                } else
                    common.showAlert("Unlike", object.getString("errmessage"), R.drawable.heart_gray_fill);
                if (object.getString("status").equals("success")) {

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

    @Override
    public void interestRequest(String matri_id, String int_msg, final LikeButton button) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("receiver", matri_id);
        param.put("message", int_msg);

        common.makePostRequest(AppConstants.send_interest, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);

                if (object.getString("status").equals("success")) {
                    button.setLiked(true);
                    common.showAlert("Interest", object.getString("errmessage"), R.drawable.check_fill_green);
                } else
                    common.showAlert("Interest", object.getString("errmessage"), R.drawable.check_gray_fill);

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
    public void blockRequest(final String tag, String id) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        if (tag.equals("remove")) {
            param.put("unblockuserid", id);
        } else
            param.put("blockuserid", id);

        param.put("blacklist_action", tag);

        common.makePostRequest(AppConstants.block_user, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (tag.equals("add")) {
                    common.showAlert("Block", object.getString("errmessage"), R.drawable.ban);
                } else
                    common.showAlert("Unblock", object.getString("errmessage"), R.drawable.ban_gry);

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
    public void shortlistRequest(final String tag, String id) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        if (tag.equals("remove")) {
            param.put("shortlisteduserid", id);
        } else
            param.put("shortlistuserid", id);

        param.put("shortlist_action", tag);

        common.makePostRequestTime(AppConstants.shortlist_user, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (tag.equals("add")) {
                    common.showAlert("Shortlist", object.getString("errmessage"), R.drawable.star_fill_yellow);
                } else {
                    common.showAlert("Remove From Shortlist", object.getString("errmessage"), R.drawable.star_gray_fill);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        common.hideProgressRelativeLayout(loader);
        super.onBackPressed();
    }


    @Override
    public void onSuccess(JsonObject data, @Nullable String requestTag) {
        common.hideProgressRelativeLayout(loader);
        switch (requestTag) {
            case "SEARCH_RESULT":
                int total_count = data.get("total_count").getAsInt();
                getSupportActionBar().setTitle("Search Result (" + total_count + " Members)");
                continue_request = data.get("continue_request").getAsBoolean();

                if (total_count != 0) {
                    tv_no_data.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    if (data.get("data").isJsonArray()) {
                        List<DashboardItemNew> tempArrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<DashboardItemNew>>() {
                        }.getType());

                        //when tempArrayList size > 0
                        if (tempArrayList.size() > 0 && list.size() > 0) {
                            list.addAll(tempArrayList);
                            adapter.notifyDataSetChanged();
                        }

                        //when activity start
                        if (list.size() == 0) {
                            list.addAll(tempArrayList);
                            adapter.notifyDataSetChanged();
                            AppDebugLog.print("list size : " + list.size());
                        }

                        //when new product list size == 0
                        if (tempArrayList.size() == 0 && list.size() > 0) {
                            //not more data available on server
                            continue_request = false;
                        }
                    }

                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                break;
        }

    }

    @Override
    public void onError(boolean isError, @Nullable String requestTag) {
        common.hideProgressRelativeLayout(loader);
    }
}
