package com.advance.sanatani_vivah.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.BankDetailsListAdapter;
import com.advance.sanatani_vivah.adapter.QRCodeListAdapter;
import com.advance.sanatani_vivah.custom.NonScrollListView;
import com.advance.sanatani_vivah.model.BankDetailsBean;
import com.advance.sanatani_vivah.model.PlanDatum;
import com.advance.sanatani_vivah.model.QRCodeBean;
import com.advance.sanatani_vivah.model.paymentItem;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakePaymentsActivity extends AppCompatActivity {

    private String plan_name = "", plan_id = "";
    private TextView tv_name, tv_duration, tv_amount, tv_tax, tv_discount, tv_total, tv_label_tex,tv_off;
    private LinearLayout lay_two, lay_code;
    private EditText et_code;
    private LinearLayout lay_bank;
    private NonScrollListView lv_method;
    private Common common;
    private Button btn_pay, btn_redeem;
    private JSONObject plan_data;
    private float total_amt;
    private RelativeLayout loader;
    private SessionManager session;
    private String tax_applicable, service_tax;
    private List<paymentItem> list = new ArrayList<>();
    private MethodAdapter adapter;
    private PlanDatum premiumPlanBean;
    private float planFinalPlanAmount;

    private CardView layoutScanPay, layoutOfflinePay;
    private RecyclerView qrCodeRecyclerView, bankDetailRecyclerView;
    private List<QRCodeBean> qrCodeList = new ArrayList<>();
    private List<BankDetailsBean> bankDetailList = new ArrayList<>();
    private QRCodeListAdapter qrListAdapter;
    private BankDetailsListAdapter bankDetailsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payments);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Make Payment");
        toolbar.setNavigationOnClickListener(view -> finish());

        common = new Common(this);
        session = new SessionManager(this);

        loader = findViewById(R.id.loader);
        tv_duration = findViewById(R.id.tv_duration);
        tv_amount = findViewById(R.id.tv_amount);
        tv_tax = findViewById(R.id.tv_tax);
        tv_discount = findViewById(R.id.tv_discount);
        tv_total = findViewById(R.id.tv_total);
        tv_label_tex = findViewById(R.id.tv_label_tex);//,
        tv_off = findViewById(R.id.tv_off);
        lay_two = findViewById(R.id.lay_two);
        lay_code = findViewById(R.id.lay_code);

        et_code = findViewById(R.id.et_code);
        btn_pay = findViewById(R.id.btn_pay);
        btn_redeem = findViewById(R.id.btn_redeem);
        lv_method = findViewById(R.id.lv_method);
        lay_bank = findViewById(R.id.lay_bank);
        tv_name = findViewById(R.id.tv_name);

        layoutScanPay = findViewById(R.id.layoutScanPay);
        layoutOfflinePay = findViewById(R.id.layoutOfflinePay);
        qrCodeRecyclerView = findViewById(R.id.qrCodeRecyclerView);
        bankDetailRecyclerView = findViewById(R.id.bankDetailRecyclerView);

        Bundle bundle = getIntent().getExtras();
        Gson gsonObj = new Gson();
        if (bundle != null && bundle.containsKey("plan_data")) {
            premiumPlanBean = gsonObj.fromJson(bundle.getString("plan_data"), PlanDatum.class);
            plan_name = premiumPlanBean.getPlanName();
            plan_id = premiumPlanBean.getId();
            tv_name.setText(plan_name.toUpperCase());

            float planAmount = Float.parseFloat(premiumPlanBean.getPlanAmount());
            float planDiscount = Float.parseFloat(premiumPlanBean.getOfferPer());
            planFinalPlanAmount = planAmount;

            tv_duration.setText(premiumPlanBean.getPlanDuration() + " Days");
            tv_amount.setText(premiumPlanBean.getPlanAmountType() + " " + planFinalPlanAmount);
            tv_discount.setText(premiumPlanBean.getPlanAmountType() + " 0");

            if (planDiscount > 0) {
                float tempOffAmt = (planAmount * planDiscount) / 100;
                planFinalPlanAmount = planAmount - tempOffAmt;
                tv_off.setText(premiumPlanBean.getPlanAmountType() + " " + tempOffAmt);
            }else{
                tv_off.setText(premiumPlanBean.getPlanAmountType() + " 0");
            }

            //tv_duration.setText(premiumPlanBean.getPlanDuration() + " Days");
            //tv_amount.setText(premiumPlanBean.getPlanAmountType() + " " + premiumPlanBean.getPlanAmount());
            //tv_discount.setText(premiumPlanBean.getPlanAmountType() + " 0");
        }

        lay_bank.setVisibility(View.GONE);
        if (bundle != null && bundle.containsKey("bank_detail_list")) {
            String bankDetailListStr = bundle.getString("bank_detail_list");
            try {
                JSONArray jsonArray = new JSONArray(bankDetailListStr);
                bankDetailList = gsonObj.fromJson(jsonArray.toString(), new TypeToken<List<BankDetailsBean>>() {
                }.getType());

                initBankDetailsRecyclerView(bankDetailList);
                layoutOfflinePay.setVisibility(View.VISIBLE);

                lay_bank.setVisibility(View.VISIBLE);

                AppDebugLog.print("bankDetailList size : "+bankDetailList.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            layoutOfflinePay.setVisibility(View.GONE);
        }
        if (bundle != null && bundle.containsKey("qr_list")) {
            try {
                String qrListStr = bundle.getString("qr_list");
                JSONArray jsonArray = new JSONArray(qrListStr);
                qrCodeList = gsonObj.fromJson(jsonArray.toString(), new TypeToken<List<QRCodeBean>>() {
                }.getType());

                initQRCodeRecyclerView(qrCodeList);
                layoutScanPay.setVisibility(View.VISIBLE);
                lay_bank.setVisibility(View.VISIBLE);

                AppDebugLog.print("qrCodeList size : "+qrCodeList.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            layoutScanPay.setVisibility(View.GONE);
        }

        btn_redeem.setOnClickListener(view -> {
            String code = et_code.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                et_code.setError("Please enter code");
                return;
            }
            checkCoupan(code);
        });

        getData();

        btn_pay.setOnClickListener(view -> {
            String method = "";
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isSelected()) {
                    method = list.get(i).getName();
                }
            }
            if (method.isEmpty()) {
                common.showToast("Please select payment method first");
                return;
            }
            Intent i = new Intent(getApplicationContext(), PaymentWebView.class);
            i.putExtra("Total_amount", String.valueOf(total_amt));
            i.putExtra("Method", method);
            i.putExtra("Plan_id", plan_id);
            i.putExtra("plan_name", plan_name);
            startActivity(i);
        });
    }

    private void initQRCodeRecyclerView(List<QRCodeBean> qrCodeList) {
        qrListAdapter = new QRCodeListAdapter(this, qrCodeList);
        qrCodeRecyclerView.setAdapter(qrListAdapter);
    }

    private void initBankDetailsRecyclerView(List<BankDetailsBean> bankDetailList) {
        bankDetailsListAdapter = new BankDetailsListAdapter(this, bankDetailList);
        bankDetailRecyclerView.setAdapter(bankDetailsListAdapter);
    }

    private void getPaymentMethod() {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.get_payment_method, new HashMap<String, String>(), response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray plan_data = object.getJSONArray("plan_data");
                    for (int i = 0; i < plan_data.length(); i++) {
                        JSONObject obj = plan_data.getJSONObject(i);
                        String name = obj.getString("name");
                        String logo = obj.getString("logo");
                        if (name.equals("BankDetails")) {
                            lay_bank.setVisibility(View.VISIBLE);
                        } else {
                            paymentItem item = new paymentItem(name, logo);
                            list.add(item);
                        }
                    }
                    adapter = new MethodAdapter(MakePaymentsActivity.this, list);
                    lv_method.setAdapter(adapter);
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

    private void getData() {
        common.showProgressRelativeLayout(loader);

        common.makePostRequest(AppConstants.site_data, new HashMap<String, String>(), response -> {
            Log.d("resp", response);
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                JSONObject config_data = object.getJSONObject("config_data");
                tax_applicable = config_data.getString("tax_applicable");

                if (tax_applicable.equals("Yes")) {
                    lay_two.setVisibility(View.VISIBLE);
                    service_tax = config_data.getString("service_tax");
                    //float amt = Float.parseFloat(premiumPlanBean.getPlanAmount());
                    float amt = planFinalPlanAmount;
                    float tax_total = (amt * Float.parseFloat(service_tax)) / 100;

                    tv_tax.setText(premiumPlanBean.getPlanAmountType() + " " + new DecimalFormat("##.##").format(tax_total));
                    total_amt = tax_total + amt;

                    tv_total.setText(premiumPlanBean.getPlanAmountType() + " " + new DecimalFormat("##.##").format(total_amt));
                    tv_label_tex.setText(config_data.getString("tax_name") + " (" + service_tax + "%)");

                } else {
                    //total_amt = Float.parseFloat(premiumPlanBean.getPlanAmount());
                    total_amt = planFinalPlanAmount;
                    tv_total.setText(premiumPlanBean.getPlanAmountType() + " " + new DecimalFormat("##.##").format(total_amt));

                    lay_two.setVisibility(View.GONE);
                }
                getPaymentMethod();

            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
            //getPaymentMethod();

        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void checkCoupan(final String code) {
        Common.hideSoftKeyboard(this);
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("plan_id", plan_id);
        param.put("couponcode", code);
        common.makePostRequest(AppConstants.check_coupan, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("message"));
                if (object.getString("status").equals("success")) {

                    String discount_amount = object.getString("discount_amount");

                    //float amt = Float.parseFloat(premiumPlanBean.getPlanAmount());
                    float amt = planFinalPlanAmount;
                    float disc_total = amt - Float.parseFloat(discount_amount);
                    if (tax_applicable.equals("Yes")) {
                        float tax_total = (disc_total * Float.parseFloat(service_tax)) / 100;
                        total_amt = tax_total + disc_total;
                        tv_tax.setText(premiumPlanBean.getPlanAmountType() + " " + new DecimalFormat("##.##").format(tax_total));
                    } else {
                        total_amt = total_amt - Float.parseFloat(discount_amount);
                    }
                    tv_discount.setText(premiumPlanBean.getPlanAmountType() + " " + discount_amount + " (" + code + ")");
                    tv_discount.setTextColor(Color.GREEN);

                    tv_total.setText(premiumPlanBean.getPlanAmountType() + " " + new DecimalFormat("##.##").format(total_amt));

                    et_code.setText("");
                    lay_code.setVisibility(View.GONE);

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

    public class MethodAdapter extends ArrayAdapter<paymentItem> {
        Context context;
        List<paymentItem> list;
        Common common;

        public MethodAdapter(Context context, List<paymentItem> list) {
            super(context, R.layout.method_item, list);
            this.context = context;
            this.list = list;
            common = new Common(context);
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.method_item, null, true);

            ImageView img_logo = rowView.findViewById(R.id.img_logo);
            TextView tv_name = rowView.findViewById(R.id.tv_name);
            final RadioButton rad_method = rowView.findViewById(R.id.rad_method);

            final paymentItem item = list.get(position);

            rad_method.setChecked(item.isSelected());
            tv_name.setText(item.getName());
            if (!item.getLogo().isEmpty())
                Picasso.get().load(item.getLogo()).into(img_logo);

            rowView.setOnClickListener(view1 -> {
                for (int i = 0; i < list.size(); i++) {
                    if (i == position)
                        list.get(i).setSelected(true);
                    else
                        list.get(i).setSelected(false);
                }
                notifyDataSetChanged();
            });

            return rowView;
        }

    }

}
