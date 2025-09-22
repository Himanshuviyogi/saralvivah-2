package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.VendorListAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.VendorModel;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VendorListActivity extends AppCompatActivity implements SpinnerListener {

    private SessionManager session;
    private RelativeLayout loader;
    private RelativeLayout layoutFilterProgressBar;

    private Common common;

    private VendorListAdapter adapter;
    private ArrayList<VendorModel> arrayList = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView lblNoRecordsFound;
    private EditText txtSearch;

    private ImageView btnFilter;
    private Button btnFilterSubmit;
    private Button btnFilterReset;
    private ImageView btnClose;
    private BottomSheetBehavior<View> bottomSheetBehavior = null;

    private SingleSpinnerSearch spinCountry, spinState, spinCity, spinCategory;
    private String country_id = "", state_id = "", city_id = "", category_id = "";
    private String imageUrl;

    private String selectedCategory;
    private boolean isFilterOn;
    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);
        initialize();
    }

    private void initialize() {
        setToolbar();

        common = new Common(this);

        selectedCategory = getIntent().getStringExtra("category_id");

        loader = findViewById(R.id.loader);
        lblNoRecordsFound = findViewById(R.id.lblNoRecordsFound);
        recyclerView = findViewById(R.id.recyclerView);
        txtSearch = findViewById(R.id.txtSearch);
        btnFilter = findViewById(R.id.btnFilter);
        btnFilterSubmit = findViewById(R.id.btnFilterSubmit);
        btnFilterReset = findViewById(R.id.btnFilterReset);
        btnClose = findViewById(R.id.btnClose);

        spinCountry = findViewById(R.id.spinCountry);
        spinState = findViewById(R.id.spinState);
        spinCity = findViewById(R.id.spinCity);
        spinCategory = findViewById(R.id.spinCategory);

        session = new SessionManager(this);
        retrofit = RetrofitClient.getClient();
        appApiService = retrofit.create(AppApiService.class);

        RelativeLayout bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        getCategoryList("");

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnFilter.setOnClickListener(view -> {
            country_id = "";
            state_id = "";
            city_id = "";
            openFilterScreen();
        });

        btnClose.setOnClickListener(view -> {
            closeFilterScreen();
        });

        btnFilterReset.setOnClickListener(view -> {
            isFilterOn = false;
            resetFilter();
        });

        btnFilterSubmit.setOnClickListener(view -> {
            closeFilterScreen();
            if (country_id != null && country_id.length() > 0) {
                isFilterOn = true;
                if (state_id != null && state_id.length() > 0) {
                    getCategoryList(txtSearch.getText().toString());
                }

            } else {
                isFilterOn = false;
                getCategoryList(txtSearch.getText().toString());
            }

        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vendors");

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void resetFilter() {
        country_id = "";
        state_id = "";
        city_id = "";
    }

    private void setSpinner() {
        if (MyApplication.getSpinData() != null) {

            spinCountry = findViewById(R.id.spinCountry);
            setupSearchDropDown(spinCountry, "Country", "country_list");

            spinState = findViewById(R.id.spinState);
            setupInitializeSearchDropDown(spinState, "State");

            spinCity = findViewById(R.id.spinCity);
            setupInitializeSearchDropDown(spinCity, "City");

        } else {
            getList();
        }
    }

    private void setupSearchDropDown(SingleSpinnerSearch spinner, String hint, String listJsonKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(MyApplication.getSpinData().toString());
        spinner.setItems(spinner, common.getSpinnerListFromArray(gsonObject.get(listJsonKey).getAsJsonArray()), -1, this, hint);
    }

    private void setupInitializeSearchDropDown(SingleSpinnerSearch spinner, String hint) {
        spinner.setItems(spinner, new ArrayList<>(), -1, this, hint);
    }

    private void openFilterScreen() {
        setSpinner();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void closeFilterScreen() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void getList() {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }
        showFilterProgressLayout();
        common.makePostRequest(AppConstants.common_list, new HashMap<String, String>(), response -> {
            hideFilterProgressLayout();
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                MyApplication.setSpinData(object);
                setSpinner();
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }

        }, error -> {
            Log.d("resp", error.getMessage() + "   ");
            hideFilterProgressLayout();
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void getCategoryList(String searchKeyword) {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Common.showToast(getString(R.string.err_msg_no_intenet_connection));
            return;
        }

        common.showProgressRelativeLayout(loader);

        Map<String, String> params = new HashMap<>();
        params.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
        params.put("user_agent", AppConstants.USER_AGENT);
        params.put("wed_category_id", selectedCategory);
        if (isFilterOn) {
            params.put("wed_country_id", country_id);
            if (state_id != null) params.put("wed_state_id", state_id);
            else params.put("wed_state_id", "");
            if (city_id != null) params.put("wed_city_id", city_id);
            else params.put("wed_city_id", "");
            params.put("wed_keyword", searchKeyword);
            params.put("is_search", "Yes");
        } else {
            params.put("wed_country_id", "");
            params.put("wed_state_id", "");
            params.put("wed_city_id", "");
            params.put("wed_keyword", "");
            params.put("is_search", "No");
        }

        for (String string : params.values()) {
            AppDebugLog.print("params : " + string + "\n");
        }

        Call<JsonObject> call = appApiService.getVendorList(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                common.hideProgressRelativeLayout(loader);
                JsonObject data = response.body();

                AppDebugLog.print("response in getCategoryList : " + data);
                if (data != null) {
                    if (data.get("status").getAsString().equalsIgnoreCase("success")) {

                        imageUrl = data.get("imageUrl").getAsString();

                        Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
                        arrayList.clear();
                        arrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<VendorModel>>() {
                        }.getType());
                        initializeRecyclerView(imageUrl);
                        setVisibilityNoRecordsFound();
                    } else if (data.get("status").getAsString().equalsIgnoreCase("error")) {
                        arrayList.clear();
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        setVisibilityNoRecordsFound();
                    } else {
                        arrayList.clear();
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        setVisibilityNoRecordsFound();
                        Common.showToast(getString(R.string.err_msg_something_went_wrong));
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

    private void initializeRecyclerView(String imageUrl) {
        adapter = new VendorListAdapter(this, arrayList,imageUrl);
        recyclerView.setAdapter(adapter);
    }

    private void getDepedentList(final String tag, final String id) {
        showFilterProgressLayout();
        HashMap<String, String> param = new HashMap<>();
        param.put("get_list", tag);
        param.put("currnet_val", id);
        param.put("multivar", "");
        param.put("retun_for", "");

        common.makePostRequest(AppConstants.common_depedent_list, param, response -> {
            hideFilterProgressLayout();

            JsonParser jsonParser = new JsonParser();
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    switch (tag) {
                        case "state_list":
                            JsonArray jsonArray1 = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                            spinState.setItems(spinState, common.getSpinnerListFromArray(jsonArray1), -1, this, "State");
                            break;
                        case "city_list":
                            JsonArray jsonArray2 = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                            spinCity.setItems(spinCity, common.getSpinnerListFromArray(jsonArray2), -1, this, "City");
                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            hideFilterProgressLayout();
            Log.d("resp", error.getMessage() + "   ");
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }


    private void setVisibilityNoRecordsFound() {
        if (arrayList.size() == 0)
            lblNoRecordsFound.setVisibility(View.VISIBLE);
        else
            lblNoRecordsFound.setVisibility(View.GONE);
    }

    //Todo show progress layout
    private void showFilterProgressLayout() {
        layoutFilterProgressBar = findViewById(R.id.layoutFilterProgressBar);
        layoutFilterProgressBar.setVisibility(View.VISIBLE);
    }

    //Todo hide progress layout
    private void hideFilterProgressLayout() {
        if (layoutFilterProgressBar != null)
            layoutFilterProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemsSelected(MultiSpinnerSearch multiSpinnerSearch) {

    }

    @Override
    public void onItemsSelected(SingleSpinnerSearch singleSpinnerSearch, KeyPairBoolData item) {
        Common.hideSoftKeyboard(this);
        if (item == null) return;
        if (item.getId() == null) return;

        AppDebugLog.print("religion_id in onItemsSelected : " + item.getId());
        switch (singleSpinnerSearch.getId()) {
            case R.id.spinCountry:
                country_id = item.getId();
                if (country_id != null && (country_id.equals("") || !country_id.equals("0"))) {
                    getDepedentList("state_list", country_id);
                } else {
                    state_id = "";
                    city_id = "";
                    setupInitializeSearchDropDown(spinState, "State");
                    setupInitializeSearchDropDown(spinCity, "City");
                }
                break;
            case R.id.spinState:
                state_id = item.getId();
                if (state_id != null && (state_id.equals("") || !state_id.equals("0"))) {
                    getDepedentList("city_list", state_id);
                } else {
                    city_id = "";
                    setupInitializeSearchDropDown(spinCity, "City");
                }
                break;
            case R.id.spinCity:
                city_id = item.getId();
                break;

            case R.id.spinCategory:
                category_id = item.getId();
                break;
        }
    }
}