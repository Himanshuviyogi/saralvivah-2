package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.FirstCategoryListAdapter;
import com.advance.sanatani_vivah.model.CategoryModel;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FirstVendorCategoryListActivity extends AppCompatActivity {

    private SessionManager session;
    private Common common;
    private RelativeLayout loader;

    private ArrayList<CategoryModel> arrayList = new ArrayList<>();

    private FirstCategoryListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView lblNoRecordsFound;
    private EditText txtSearch;
    private String categoryImageUrl = "";

    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_vendor_category_list);
        initialize();
    }

    private void initialize() {
        setToolbar();

        loader = findViewById(R.id.loader);
        lblNoRecordsFound = findViewById(R.id.lblNoRecordsFound);
        recyclerView = findViewById(R.id.recyclerView);
        common = new Common(this);
        session = new SessionManager(this);
        retrofit = RetrofitClient.getClient();
        appApiService = retrofit.create(AppApiService.class);

        getCategoryList();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vendor Categories");

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void getCategoryList() {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Common.showToast(getString(R.string.err_msg_no_intenet_connection));
            return;
        }

        common.showProgressRelativeLayout(loader);

        Map<String, String> params = new HashMap<>();
        params.put("user_agent", AppConstants.USER_AGENT);
        params.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));

        for (String string : params.values()) {
            AppDebugLog.print("params : " + string + "\n");
        }

        Call<JsonObject> call = appApiService.getVendorCategoryList(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                common.hideProgressRelativeLayout(loader);
                JsonObject data = response.body();

                AppDebugLog.print("response in getCategoryList : " + data);
                if (data != null) {
                    categoryImageUrl = data.get("category_imageUrl").getAsString();
                    if (data.get("status").getAsString().equalsIgnoreCase("success")) {
                        Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
                        arrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<CategoryModel>>() {
                        }.getType());
                        initializeRecyclerView(categoryImageUrl);
                        setVisibilityNoRecordsFound();
                    } else {
                        setVisibilityNoRecordsFound();
                        //Common.showToast(getString(R.string.err_msg_something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in getCategoryList : " + t.getMessage());
                Common.showToast(getString(R.string.err_msg_something_went_wrong));
                common.hideProgressRelativeLayout(loader);
            }
        });
    }

    private void initializeRecyclerView(String categoryImageUrl) {
        adapter = new FirstCategoryListAdapter(this, arrayList, categoryImageUrl);
        recyclerView.setAdapter(adapter);
    }


    private void setVisibilityNoRecordsFound() {
        if (arrayList.size() == 0)
            lblNoRecordsFound.setVisibility(View.VISIBLE);
        else
            lblNoRecordsFound.setVisibility(View.GONE);
    }
}