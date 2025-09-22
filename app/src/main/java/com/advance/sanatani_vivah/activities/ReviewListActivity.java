package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.ReviewListAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.ReviewBean;
import com.advance.sanatani_vivah.model.VendorParentModel;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class ReviewListActivity extends AppCompatActivity {

    private SessionManager session;
    private RelativeLayout loader;

    private ArrayList<ReviewBean> arrayList = new ArrayList<>();

    private ReviewListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView lblNoRecordsFound;

    private Button btnReview;

    private VendorParentModel vendorParentModel = null;

    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        initialize();
    }

    @Override protected void onResume() {
        super.onResume();
        if(MyApplication.isFromAddedReview) {
            MyApplication.isFromAddedReview = true;
            finish();
        }
    }

    private void initialize() {
        setToolbar();

        session = new SessionManager(this);

        String json = getIntent().getStringExtra("vendorModel");
        vendorParentModel = Common.getModelFromString(json);

        btnReview = findViewById(R.id.btnReview);

        loader = findViewById(R.id.loader);
        lblNoRecordsFound = findViewById(R.id.lblNoRecordsFound);
        recyclerView = findViewById(R.id.recyclerView);
        session = new SessionManager(this);
        retrofit = RetrofitClient.getClient();
        appApiService = retrofit.create(AppApiService.class);

        //getReviewList();
        arrayList = vendorParentModel.getVendorReviewList();

        btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddReviewActivity.class);
            intent.putExtra("vendor_id", vendorParentModel.getVendorModel().getId());
            startActivity(intent);
        });

        initializeRecyclerView();
        setVisibilityNoRecordsFound();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reviews");

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

//    private void getReviewList() {
//        if (!ConnectionDetector.isConnectingToInternet(this)) {
//            Common.showToast(this, getString(R.string.err_msg_no_intenet_connection));
//            return;
//        }
//
//        common.showProgressRelativeLayout(loader);
//
//        Map<String, String> params = new HashMap<>();
//        params.put("user_agent", Utils.USER_AGENT);
//        params.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
//        params.put("vendor_id", selectedVendorId);
//
//        for (String string : params.values()) {
//            AppDebugLog.print("params : " + string + "\n");
//        }
//
//        Call<JsonObject> call = appApiService.getVendorReviewList(params);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                common.hideProgressRelativeLayout(loader);
//                JsonObject data = response.body();
//
//                AppDebugLog.print("response in getCategoryList : " + data);
//                if (data != null) {
//                    if (data.get("status").getAsString().equalsIgnoreCase("success")) {
//                        Gson gson = new GsonBuilder().setDateFormat(Utils.GSONDateTimeFormat).create();
//                        arrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<CategoryModel>>() {
//                        }.getType());
//                        initializeRecyclerView();
//                        setVisibilityNoRecordsFound();
//                    } else {
//                        Common.showToast(ReviewListActivity.this, getString(R.string.err_msg_something_went_wrong));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                AppDebugLog.print("error in getCategoryList : " + t.getMessage());
//                Common.showToast(ReviewListActivity.this, getString(R.string.err_msg_something_went_wrong));
//                common.hideProgressRelativeLayout(loader);
//            }
//        });
//    }

    private void initializeRecyclerView() {
        adapter = new ReviewListAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
    }


    private void setVisibilityNoRecordsFound() {
        if (arrayList.size() == 0)
            lblNoRecordsFound.setVisibility(View.VISIBLE);
        else
            lblNoRecordsFound.setVisibility(View.GONE);
    }
}