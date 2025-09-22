package com.advance.sanatani_vivah.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.islamkhsh.CardSliderIndicator;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import androidx.cardview.widget.CardView;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.PremiumPlanAdapter;
import com.advance.sanatani_vivah.model.PlanDatum;
import com.advance.sanatani_vivah.model.PlanItem;
import com.advance.sanatani_vivah.model.PremiumPlanBean;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanListActivity extends AppCompatActivity implements PremiumPlanAdapter.ItemListener {
    private List<PlanItem> list = new ArrayList<>();
    private Button btn_payment;
    private Common common;
    private RelativeLayout loader;
    private TextView tv_no_data;
    private CardSliderIndicator indicator;

    private TextView lblHighlighted;

    private CardView cardView;

    private String qrCodeListStr = "";
    private String bankDetailsListStr = "";

    //slides cards
    private CardSliderViewPager cardSliderViewPager;
    private ArrayList<PlanDatum> arrayListPlan = new ArrayList<PlanDatum>();
    private PremiumPlanAdapter adapterPlan;

    private LinearLayout layoutPlanCategory, layoutPlanDurations;
    private HorizontalScrollView layoutPlanDurationsMain;
    private int radioWidth = 0;

    private ArrayList<PremiumPlanBean> arrayListCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Membership Plan");
        toolbar.setNavigationOnClickListener(v -> finish());

        common = new Common(this);
        loader = findViewById(R.id.loader);
        indicator = findViewById(R.id.indicator);
        btn_payment = findViewById(R.id.btn_id);
        tv_no_data = findViewById(R.id.tv_no_data);
        cardView = findViewById(R.id.cardView);
        layoutPlanCategory = findViewById(R.id.layoutPlanCategory);
        layoutPlanDurations = findViewById(R.id.layoutPlanDurations);
        layoutPlanDurationsMain = findViewById(R.id.layoutPlanDurationsMain);

        lblHighlighted = findViewById(R.id.lblHighlighted);

        getPlanData();

        btn_payment.setOnClickListener(view -> {
            String plan_amount = "";
            JSONObject object = null;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isIs_select()) {
                    plan_amount = list.get(i).getPrise();
                    object = list.get(i).getPlan_object();
                }

            }
            if (object == null) {
                Toast.makeText(getApplicationContext(), "Please Select Plan", Toast.LENGTH_SHORT).show();
                return;
            }
            if (plan_amount.equals("0")) {
                Toast.makeText(getApplicationContext(), "Please Contact To admin", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PlanListActivity.this, ContactUsActivity.class);
                i.putExtra("page_tag", "form");
                startActivity(i);
            } else {
                Intent i = new Intent(PlanListActivity.this, MakePaymentsActivity.class);
                i.putExtra("plan_data", object.toString());
                startActivity(i);
            }
        });
    }

    //TODO Add dynamic toogle button
    private void addDynamicButtonsInLayout(PremiumPlanBean premiumPlanBean, LinearLayout layout, boolean isLabel) {
        if (layout == null) return;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cell_label_toggle, null);
        TextView btnOption = rowView.findViewById(R.id.btnOption);

        btnOption.setTag(premiumPlanBean);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonLayoutParams.setMargins(0, 0, 0, 0);
        btnOption.setWidth(radioWidth);

        if (!isLabel) {
            btnOption.setText(premiumPlanBean.getCategoryName());
            btnOption.setTextColor(ContextCompat.getColor(this, R.color.black));
        }else{
            btnOption.setText(premiumPlanBean.getExtraText());
        }

        btnOption.setLayoutParams(buttonLayoutParams);
        btnOption.setOnClickListener(view -> {
            if (view.getTag() != null && !isLabel) {
                LinearLayout parentLayout = (LinearLayout) view.getParent();

                for (int i = 0; i < layout.getChildCount(); i++) {
                    TextView toggleButton = (TextView) layout.getChildAt(i);
                    PremiumPlanBean premiumPlanBean1 = (PremiumPlanBean) toggleButton.getTag();
                    PremiumPlanBean premiumPlanBean2 = (PremiumPlanBean) view.getTag();
                    if (premiumPlanBean1.getId().equals(premiumPlanBean2.getId())) {
                      //  cardView.setShadowColor(Color.parseColor(premiumPlanBean.getColor()));
                        toggleButton.setBackgroundColor(Color.parseColor(premiumPlanBean.getColor()));
                        toggleButton.setTextColor(ContextCompat.getColor(this, R.color.white));

                        arrayListPlan.clear();
                        arrayListPlan.addAll(arrayListCategory.get(i).getPlanData());
                        initPlanSlider(premiumPlanBean2);
                    } else {
                        toggleButton.setTextColor(Color.parseColor(premiumPlanBean.getColor()));
                        toggleButton.setBackgroundResource(R.color.transparent);
                    }
                }
            }
        });
        // Add the new row before the add field button.
        layout.addView(rowView);
    }

    private void getPlanData() {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.plan_list, new HashMap<String, String>(), response -> {
            if (loader != null)
                common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);

            try {
                JSONObject object = new JSONObject(response);

                Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();

                JsonParser jsonParser = new JsonParser();
                JsonObject data = (JsonObject) jsonParser.parse(object.toString());

                if (object.getString("status").equals("success")) {
                    if (data.has("scan_pay") && data.get("scan_pay").isJsonArray()) {
                        qrCodeListStr = data.get("scan_pay").getAsJsonArray().toString();
                    }
                    if (data.has("offline_payment") && data.get("offline_payment").isJsonArray()) {
                        bankDetailsListStr = data.get("offline_payment").getAsJsonArray().toString();
                    }
                    if (data.get("plan_data").isJsonArray()) {
                        arrayListCategory = gson.fromJson(data.getAsJsonArray("plan_data"), new TypeToken<List<PremiumPlanBean>>() {
                        }.getType());

                        if (arrayListCategory.size() != 0) {
                            radioWidth = (Common.getDisplayWidth(this)
                                    - Common.convertDpToPixels(20, this))
                                    / arrayListCategory.size();

                            for (PremiumPlanBean premiumPlanBean : arrayListCategory) {
                                addDynamicButtonsInLayout(premiumPlanBean, layoutPlanCategory, false);
                                addDynamicButtonsInLayout(premiumPlanBean, layoutPlanDurations, true);
                            }
                            layoutPlanCategory.getChildAt(0).callOnClick();

                            AppDebugLog.print("size : " + arrayListCategory.size());

                            layoutPlanCategory.setVisibility(View.VISIBLE);
                            layoutPlanDurationsMain.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
                        } else {
                            tv_no_data.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
                } else {
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

    private void initPlanSlider(PremiumPlanBean premiumPlanBean) {
        AppDebugLog.print("position & list size : "+arrayListPlan.size());
        if(arrayListPlan.size() > 0){
            if (adapterPlan  == null) {
                cardSliderViewPager = findViewById(R.id.viewPager);
                adapterPlan = new PremiumPlanAdapter(arrayListPlan);
                cardSliderViewPager.setAdapter(adapterPlan);
                adapterPlan.setListener(this);
            } else {
                adapterPlan.notifyDataSetChanged();
            }
            tv_no_data.setVisibility(View.GONE);
            indicator.setVisibility(View.VISIBLE);
            cardSliderViewPager.setVisibility(View.VISIBLE);
        }else{
            tv_no_data.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.GONE);
            cardSliderViewPager.setVisibility(View.GONE);
        }

        if(premiumPlanBean.getOfferText()!=null && premiumPlanBean.getOfferText().length() > 0) {
                lblHighlighted.setVisibility(View.VISIBLE);
                lblHighlighted.setText(premiumPlanBean.getOfferText());
        }else{
            lblHighlighted.setVisibility(View.GONE);
        }
    }

    @Override
    public void itemClicked(PlanDatum premiumPlanBean, int position) {
        if (premiumPlanBean == null) {
            Toast.makeText(getApplicationContext(), "Please Select Plan", Toast.LENGTH_SHORT).show();
            return;
        }
        if (premiumPlanBean.getPlanAmount().equals("0")) {
            Toast.makeText(getApplicationContext(), "Please Contact To admin", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(PlanListActivity.this, ContactUsActivity.class);
            i.putExtra("page_tag", "form");
            startActivity(i);
        } else {

            Gson gson = new Gson();
            String premiumPlanBeanStr = gson.toJson(premiumPlanBean);
            Intent i = new Intent(PlanListActivity.this, MakePaymentsActivity.class);
            i.putExtra("qr_list",qrCodeListStr);
            i.putExtra("bank_detail_list",bankDetailsListStr);
            i.putExtra("plan_data", premiumPlanBeanStr);
            startActivity(i);
        }
    }
}
