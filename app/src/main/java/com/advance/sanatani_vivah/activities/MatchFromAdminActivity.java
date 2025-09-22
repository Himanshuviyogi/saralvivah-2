package com.advance.sanatani_vivah.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.advance.sanatani_vivah.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchFromAdminActivity extends AppCompatActivity implements CardStackAdapter.ItemListener, CardStackListener {

    private Common common;
    private List<DashboardItem> list = new ArrayList<>();
    private SessionManager session;
    private RelativeLayout loader;

    private boolean continue_request;
    private int page = 0;
    private TextView tv_no_data;

    //swipe card stack
    private CardStackView cardStack;
    public CardStackAdapter cardsAdapter;
    private List<CardItem> cardItems = new ArrayList<>();
    private CardStackLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_match_from_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recommended Match");
       // toolbar.setNavigationOnClickListener(v -> finish());

        common = new Common(this);
        session = new SessionManager(this);

        cardStack = findViewById(R.id.swipeStack);
        manager = new CardStackLayoutManager(getApplicationContext(), this);
        loader = findViewById(R.id.loader);
        tv_no_data = findViewById(R.id.tv_no_data);


        initializeRecyclerView();
        page = page + 1;
        getData(page);
    }

    private void initializeRecyclerView() {
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
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

    int total_count;
    private void getData(int page) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequest(AppConstants.received_match_from_admin, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                total_count = object.getInt("total_count");
                continue_request = object.getBoolean("continue_request");

                Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
                gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();

                JsonParser jsonParser = new JsonParser();
                JsonObject data = (JsonObject)jsonParser.parse(object.toString());

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
                }else{
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