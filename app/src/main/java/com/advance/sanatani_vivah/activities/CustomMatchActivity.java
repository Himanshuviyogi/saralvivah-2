package com.advance.sanatani_vivah.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.cardStack.CardItem;
import com.advance.sanatani_vivah.cardStack.CardStackAdapter;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomMatchActivity extends AppCompatActivity implements SpinnerListener, CardStackAdapter.ItemListener, CardStackListener {
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout layoutDistance;
    private MultiSpinnerSearch spin_mari, spin_complex, spin_tongue, spin_religion, spin_caste, spin_country, spin_edu;
    private TextView tv_min_height, tv_max_height, search_tv_min_age, search_tv_max_age, search_tv_min_area, search_tv_max_area;
    private CrystalRangeSeekbar range_height, search_range_age,search_range_area;
    private Button btn_save_search;
    private Common common;
    private SessionManager session;
    private String mari_id = "", religion_id = "", tongue_id = "", country_id = "", edu_id = "", height_from = "",
            height_to = "", complex_id = "", caste_id = "", age_from, age_to, area_from, area_to;
    private HashMap<String, String> height_map = new HashMap<>();
    private RelativeLayout loader;
    private List<DashboardItem> list = new ArrayList<>();
    private int page = 0;
    private TextView tv_no_data;
    private ImageView btnClose;
    private Toolbar toolbar;

    //swipe card stack
    private CardStackView cardStack;
    public CardStackAdapter cardsAdapter;
    private List<CardItem> cardItems = new ArrayList<>();
    private CardStackLayoutManager manager;

    private String type = "recommended";
    private String subMenuTitle = "Recommended Match";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_match);

        subMenuTitle = getIntent().getStringExtra("sub_menu_title");
        if(subMenuTitle.equalsIgnoreCase("Premium Match")) {
            type = "premium-match";
        }else if(subMenuTitle.equalsIgnoreCase("NearBy Match")) {
            type = "near-by-me";
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(subMenuTitle);

        common = new Common(this);
        session = new SessionManager(this);

        cardStack = findViewById(R.id.swipeStack);
        manager = new CardStackLayoutManager(getApplicationContext(), this);
        loader = findViewById(R.id.loader);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        tv_no_data = findViewById(R.id.tv_no_data);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(view -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            toolbar.setVisibility(View.VISIBLE);
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        toolbar.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        toolbar.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        toolbar.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        btn_save_search = findViewById(R.id.btn_save_search);
        spin_mari = findViewById(R.id.spin_mari);
        spin_complex = findViewById(R.id.spin_complex);
        spin_tongue = findViewById(R.id.spin_tongue);
        spin_religion = findViewById(R.id.spin_religion);
        spin_caste = findViewById(R.id.spin_caste);
        spin_country = findViewById(R.id.spin_country);
        spin_edu = findViewById(R.id.spin_edu);
        tv_min_height = findViewById(R.id.search_tv_min_height);
        tv_max_height = findViewById(R.id.search_tv_max_height);

        search_tv_min_age = findViewById(R.id.search_tv_min_age);
        search_tv_max_age = findViewById(R.id.search_tv_max_age);

        search_tv_min_area = findViewById(R.id.search_tv_min_area);
        search_tv_max_area = findViewById(R.id.search_tv_max_area);

        layoutDistance = findViewById(R.id.layoutDistance);
        layoutDistance.setVisibility(View.GONE);
        if(type.equalsIgnoreCase("near-by-me")) {
            layoutDistance.setVisibility(View.VISIBLE);
        }

        initializeRecyclerView();

        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_save_search.setOnClickListener(view -> checkData());
    }

    private void checkData() {
        if (mari_id.equals("") || mari_id.equals(",") || mari_id.equals("0")) {
            common.showToast("Please select marital status.");
            return;
        }
        if (religion_id.equals("") || religion_id.equals(",") || religion_id.equals("0")) {
            common.showToast("Please select religion.");
            return;
        }
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        HashMap<String, String> param = new HashMap<>();
        param.put("looking_for", getValue(mari_id));
        param.put("part_frm_age", getValue(age_from));
        param.put("part_to_age", getValue(age_to));
        param.put("part_height", getValue(height_from));
        param.put("part_height_to", getValue(height_to));
        param.put("distance_from", getValue(area_from));
        param.put("distance_to", getValue(area_to));
        param.put("part_complexion", getValue(complex_id));
        param.put("part_mother_tongue", getValue(tongue_id));
        param.put("part_religion", getValue(religion_id));
        param.put("part_caste", getValue(caste_id));
        param.put("part_country_living", getValue(country_id));
        param.put("part_education", getValue(edu_id));
        param.put("match_type", type);
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        Log.d("resp", param.toString());
        submitData(param);
    }

    private String getValue(String val) {
        if (val == null || val.equals("0")) return "";
        else return val;
    }

    private void submitData(HashMap<String, String> param) {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.save_matches, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    list.clear();
                    page = 0;
                    page = page + 1;
                    getListData(page);
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

    private void getMyProfile() {
        if (loader != null || loader.getVisibility() == View.GONE) {
            common.showProgressRelativeLayout(loader);
        }

        final HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequest(AppConstants.get_my_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    JSONObject data = object.getJSONObject("data");

                    mari_id = data.getString("looking_for");
                    height_from = data.getString("part_height");
                    height_to = data.getString("part_height_to");
                    complex_id = data.getString("part_complexion");
                    tongue_id = data.getString("part_mother_tongue");
                    religion_id = data.getString("part_religion");
                    caste_id = data.getString("part_caste");
                    country_id = data.getString("part_country_living");
                    edu_id = data.getString("part_education");
                    age_from = data.getString("part_frm_age");
                    age_to = data.getString("part_to_age");

                    search_range_age.setMinStartValue(Float.parseFloat(age_from)).setMaxStartValue(Float.parseFloat(age_to)).apply();
                    range_height.setMinStartValue(Float.parseFloat(height_from)).setMaxStartValue(Float.parseFloat(height_to)).apply();

                    spin_mari.setSelection(mari_id);
                    spin_complex.setSelection(complex_id);
                    spin_tongue.setSelection(tongue_id);
                    spin_religion.setSelection(religion_id);
                    if (!religion_id.equals("") && !religion_id.equals("null")) {
                        getDependentList("caste_list", data.getString("part_religion"));
                        common.hideProgressRelativeLayout(loader);
                    }
                    spin_country.setSelection(country_id);
                    spin_edu.setSelection(edu_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
            page = page + 1;
            getListData(page);

        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void initializeRecyclerView() {
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.5f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);

        cardsAdapter = new CardStackAdapter(cardItems, this);
        cardsAdapter.setListener(this);
        cardStack.setLayoutManager(manager);
        cardStack.setAdapter(cardsAdapter);
        cardsAdapter.setLoadMoreListener(() -> {
            cardStack.post(() -> {
                AppDebugLog.print("load more SEARCH_NOW SEARCH_NOW ");
                //when total count is less
                if (cardItems.size() < total_count)
                    loadMore();// a method which requests remote data
            });
        });
    }

    /**
     * Load more task on scroll task list
     */
    private void loadMore() {
        page++;
        AppDebugLog.print("In loadMore");
        getListData(page);
    }

    int total_count;

    private void getListData(int page) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequest(AppConstants.search_now + page, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                total_count = object.getInt("total_count");
                getSupportActionBar().setTitle(subMenuTitle + " (" + total_count + " Members)");

                Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();

                JsonParser jsonParser = new JsonParser();
                JsonObject data = (JsonObject) jsonParser.parse(object.toString());

                if (total_count != 0) {
                    List<CardItem> tempArrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<CardItem>>() {
                    }.getType());

                    //when tempArrayList size > 0
                    if (tempArrayList.size() > 0 && cardItems.size() > 0) {
                        loadPaginateData(tempArrayList);
                    }

                    //when activity start
                    if (cardItems.size() == 0) {
                        cardItems.addAll(tempArrayList);
                        cardsAdapter.notifyDataChanged();
                        AppDebugLog.print("list size : " + cardItems.size());
                    }

                    //when new product list size == 0
                    if (tempArrayList.size() == 0 && cardItems.size() > 0) {
                        //not more data available on server
                        cardsAdapter.setMoreDataAvailable(false);
                        cardsAdapter.notifyDataChanged();
                    }

                    if (page == 1) {
                        if (tv_no_data == null)
                            tv_no_data = findViewById(R.id.tv_no_data);
                        if (cardItems.size() == 0) {
                            tv_no_data.setVisibility(View.VISIBLE);
                        } else {
                            tv_no_data.setVisibility(View.GONE);
                        }
                    }
                } else {
                    cardStack.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
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

    private void initData() throws JSONException {
        if (MyApplication.getSpinData() != null) {
            search_range_age = findViewById(R.id.search_range_age);
            search_range_area = findViewById(R.id.search_range_area);

            search_range_age.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                age_to = String.valueOf(maxValue);
                age_from = String.valueOf(minValue);

                search_tv_max_age.setText(maxValue + " Years");
                search_tv_min_age.setText(minValue + " Years");
            });

            search_range_area.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                area_from = String.valueOf(minValue);
                area_to = String.valueOf(maxValue);

                search_tv_max_area.setText(maxValue + " km");
                search_tv_min_area.setText(minValue + " km");
            });

            range_height = findViewById(R.id.search_range_height);
            AppDebugLog.print("MyApplication.getSpinData() : " + MyApplication.getSpinData().getJSONArray("height_list"));
            JSONArray arr = MyApplication.getSpinData().getJSONArray("height_list");
            JSONObject obj = arr.getJSONObject(1);
            JSONObject obj1 = arr.getJSONObject(arr.length() - 1);
            range_height.setMinStartValue(Float.parseFloat(obj.getString("id"))).setMaxStartValue(Float.parseFloat(obj1.getString("id"))).apply();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                if (i == 0) {

                } else if (object.getString("id").equals("85")) {
                    height_map.put(object.getString("id"), "Above 7ft");
                } else {
                    height_map.put(object.getString("id"), object.getString("val"));
                }
            }

            range_height.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {

                height_from = String.valueOf(minValue);
                height_to = String.valueOf(maxValue);
                tv_min_height.setText(disHeight(height_from));
                tv_max_height.setText(disHeight(height_to));
            });

            setupSearchDropDown(spin_mari, "Marital Status", "marital_status");
            setupSearchDropDown(spin_religion, "Religion", "religion_list");
            setupInitializeSearchDropDown(spin_caste, "Caste");
            setupSearchDropDown(spin_complex, "Complexion", "complexion");
            setupSearchDropDown(spin_tongue, "Mother Tongue", "mothertongue_list");
            setupSearchDropDown(spin_country, "Country", "country_list");
            setupSearchDropDown(spin_edu, "Education", "education_list");

            getMyProfile();
        } else {
            getList();
        }
    }

    private void getList() {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.common_list, new HashMap<String, String>(), response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));

                MyApplication.setSpinData(object);
                initData();
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

    private String disHeight(String val) {
        return height_map.get(val);
    }

    private void getDependentList(final String tag, String id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("get_list", tag);
        param.put("currnet_val", id);
        param.put("multivar", "multi");
        param.put("retun_for", "json");

        JsonParser jsonParser = new JsonParser();

        common.makePostRequest(AppConstants.common_depedent_list, param, response -> {
            Log.d("resp", response + "   ");
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    switch (tag) {
                        case "caste_list":
                            JsonArray jsonArray = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                            spin_caste.setItems(spin_caste, common.getSpinnerListFromArray(jsonArray), -1, this, "Caste");
                            if (!caste_id.equals("")) spin_caste.setSelection(caste_id);
                            break;
                    }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_match_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.filter) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            toolbar.setVisibility(View.GONE);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSearchDropDown(MultiSpinnerSearch spinner, String hint, String listJsonKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(MyApplication.getSpinData().toString());
        spinner.setItems(spinner, common.getSpinnerListFromArray(gsonObject.get(listJsonKey).getAsJsonArray()), -1, this, hint);
    }

    private void setupInitializeSearchDropDown(MultiSpinnerSearch spinner, String hint) {
        spinner.setItems(spinner, new ArrayList<>(), -1, this, hint);
    }

    @Override
    public void onItemsSelected(MultiSpinnerSearch singleSpinnerSearch) {
        Common.hideSoftKeyboard(this);
        if (singleSpinnerSearch == null) return;
        if (singleSpinnerSearch.getSelectedIdsInString() == null) return;

        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_mari:
                mari_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_religion:
                religion_id = singleSpinnerSearch.getSelectedIdsInString();
                if (religion_id != null && !religion_id.equals("0")) {
                    getDependentList("caste_list", religion_id);
                } else {
                    setupInitializeSearchDropDown(spin_caste, "Caste");
                }
                break;
            case R.id.spin_caste:
                caste_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_tongue:
                tongue_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_country:
                country_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_complex:
                complex_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_edu:
                edu_id = singleSpinnerSearch.getSelectedIdsInString();
                break;

        }
    }

    @Override
    public void onItemsSelected(SingleSpinnerSearch singleSpinnerSearch, KeyPairBoolData item) {

    }

    private void loadPaginateData(List<CardItem> newList) {

        AppDebugLog.print("loadPaginateData cardItems size :" + cardItems.size());
        AppDebugLog.print("loadPaginateData getSpots size :" + cardsAdapter.getSpots().size());

        int oldCount = cardsAdapter.getSpots().size() + 1;
        List<CardItem> old = cardsAdapter.getSpots();
        List<CardItem> totalWithNew = new ArrayList<>();
        old.addAll(newList);
        AppDebugLog.print("loadPaginateData old :" + old.size());
        totalWithNew = old;
        AppDebugLog.print("loadPaginateData totalWithNew :" + totalWithNew.size());
        cardsAdapter.setSpots(totalWithNew);
        AppDebugLog.print("loadPaginateData after set spot :" + cardsAdapter.getSpots().size());
        cardsAdapter.notifyItemRangeInserted(oldCount, totalWithNew.size());
    }

    @Override
    public void previuosClicked(int position) {
        RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new DecelerateInterpolator())
                .build();
        manager.setRewindAnimationSetting(setting);
        cardStack.rewind();
    }

    @Override
    public void nextClicked(int position) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new DecelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStack.swipe();
    }

    @Override
    public void shareClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void sendMessageClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void moreClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void showPhotosClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void callWhatsappClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void connectClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void itemClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void notNowClicked(int position, @Nullable CardItem item) {

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    int count = 0;

    @Override
    public void onCardSwiped(Direction direction) {
        try {
            if (manager.getTopPosition() == cardsAdapter.getItemCount() - 5) {
                AppDebugLog.print("call here page 2 api");
                AppDebugLog.print("cardItems.size() : " + cardItems.size());

                if (cardItems.size() < total_count)
                    loadMore();// a method which requests remote data
            }
        } catch (Exception e) {
            cardsAdapter.notifyDataChanged();
        }

        count++;
        if (count == cardItems.size()) {
            // do your work
            tv_no_data.setVisibility(View.VISIBLE);
        } else {
            tv_no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

}
