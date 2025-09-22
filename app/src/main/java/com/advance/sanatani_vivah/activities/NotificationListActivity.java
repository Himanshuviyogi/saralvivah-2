package com.advance.sanatani_vivah.activities;

import static com.advance.sanatani_vivah.utility.AppConstants.GSONDateTimeFormat;
import static com.advance.sanatani_vivah.utility.AppConstants.MESSAGE_LIST_UPDATE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.NotificationListAdapter;
import com.advance.sanatani_vivah.model.NotificationBean;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationListAdapter adapter;
    private ArrayList<NotificationBean> arrayList = new ArrayList<>();
    private RelativeLayout loader;
    private TextView lblNoRecords;
    private int page = 1;
    private int totalCount = 0;

    private IntentFilter intentFilter;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(br, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        loader = findViewById(R.id.loader);
        lblNoRecords = findViewById(R.id.lblNoRecords);

        intentFilter = new IntentFilter(MESSAGE_LIST_UPDATE);
        br = new UpdateBroadcastReceiver();

        initializeRecyclerView();

        getNotificationList();
    }

    public class UpdateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppDebugLog.print("update list : ");
            arrayList.clear();
            page = 1;
            getNotificationList();
        }
    }


    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationListAdapter(this, arrayList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setLoadMoreListener(() -> {
            recyclerView.post(() -> {
                //when total count is less
                if (arrayList.size() < totalCount)
                    loadMore();// a method which requests remote data
            });
            //Calling loadMore function in Runnable to fix the
            // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * Load more task on scroll task list
     */
    private void loadMore() {
        page++;
        //add loading progress view
        AppDebugLog.print("In loadMore");
        NotificationBean requestBean = new NotificationBean();
        requestBean.setId(getString(R.string.str_loading));
        arrayList.add(requestBean);
        getNotificationList();
        adapter.notifyItemInserted(arrayList.size() - 1);
    }

    private void getNotificationList() {
        SessionManager session =new SessionManager(this);
        Common common = new Common(this);
        if (page == 1)
            common.showProgressRelativeLayout(loader);

        //volleyRequest.getResponseInJsonStringUsingVolley(GET_TOKEN_TAG,AppConstants.BASE_URL+AppConstants.GET_TOKEN,null);

        HashMap<String, String> param = new HashMap<>();
        param.put("app_type", "Android");
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("user_agent", "NI-AAPP");
        param.put("page_number", String.valueOf(page));
        param.put("csrf_new_matrimonial", "7f96f7fe5f0dd83bfdc6ac42f2994961");

        AppDebugLog.print("Params in getToken : "+param.toString());

        Retrofit retrofit = RetrofitClient.getClient();
        AppApiService service = retrofit.create(AppApiService.class);
        Call<JsonObject> call = service.getNotificationList(param);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                common.hideProgressRelativeLayout(loader);
                AppDebugLog.print("response in getNotificationList : " + response.body());
                JsonObject data = response.body();

                if(data.has("total_count") && data.get("total_count").getAsString() !=null) {
                    totalCount = Integer.parseInt(data.get("total_count").getAsString());
                }

                Gson gson = new GsonBuilder().setDateFormat(GSONDateTimeFormat).create();
                List<NotificationBean> tempArrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<NotificationBean>>() {
                }.getType());

                AppDebugLog.print("arrayList size : " + tempArrayList.size());

                //when tempArrayList size > 0
                if (tempArrayList.size() > 0 && arrayList.size() > 0) {
                    //remove loading view
                    arrayList.remove(arrayList.size() - 1);
                    arrayList.addAll(tempArrayList);
                    adapter.notifyDataChanged();
                }

                //when activity start
                if (arrayList.size() == 0) {
                    arrayList.addAll(tempArrayList);
                    adapter.notifyDataChanged();
                    if (arrayList.size() == 0) {
                        lblNoRecords.setVisibility(View.VISIBLE);
                    }
                }

                //when new product list size == 0
                if (tempArrayList.size() == 0 && arrayList.size() > 0) {
                    //not more data available on server
                    adapter.setMoreDataAvailable(false);
                    //remove loading view
                    arrayList.remove(arrayList.size() - 1);
                    adapter.notifyDataChanged();
                    lblNoRecords.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in generateToken : " + t.getMessage());
                common.hideProgressRelativeLayout(loader);
                //not more data available on server
                adapter.setMoreDataAvailable(false);
                if (arrayList.size() > 0) {
                    //remove loading view
                    arrayList.remove(arrayList.size() - 1);
                    adapter.notifyDataChanged();
                }else{
                    lblNoRecords.setVisibility(View.GONE);
                }
            }
        });
    }
}