package com.advance.sanatani_vivah.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.CurrentPlanItem;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CurrentPlanActivity extends AppCompatActivity {

    private Button btn_upgrade;
    private TextView tv_no_data;
    private List<CurrentPlanItem> list = new ArrayList<>();
    private currentAdapter adapter;
    private SessionManager session;
    private Common common;
    private RelativeLayout progressBar;
    private ListView lv_plan;
    private boolean isFromSuccessPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Current Plan");
        toolbar.setNavigationOnClickListener(v -> backPressed());

        session = new SessionManager(this);
        common = new Common(this);

        isFromSuccessPayment = getIntent().getBooleanExtra("isFromSuccessPayment", false);

        progressBar = findViewById(R.id.loader);
        lv_plan = findViewById(R.id.lv_plan);
        btn_upgrade = findViewById(R.id.btn_upgrade);
        tv_no_data = findViewById(R.id.tv_no_data);

        btn_upgrade.setOnClickListener(view -> startActivity(new Intent(CurrentPlanActivity.this, PlanListActivity.class)));

        adapter = new currentAdapter(this, list);
        lv_plan.setAdapter(adapter);

        getPlanData();
    }

    private void getPlanData() {
        common.showProgressRelativeLayout(progressBar);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequest(AppConstants.check_plan, param, response -> {
            common.hideProgressRelativeLayout(progressBar);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                MyApplication.setPlan(object.getBoolean("is_show"));
                if (object.getString("status").equals("success")) {
                    lv_plan.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                    JSONObject obj = object.getJSONObject("data");
                    CurrentPlanItem item = new CurrentPlanItem();
                    item.setPlan_name(obj.getString("plan_name").toUpperCase());
                    item.setCurrency(obj.getString("currency"));
                    item.setPlan_amount(obj.getString("plan_amount"));
                    item.setPlan_activated(changeDate(obj.getString("plan_activated"), "MMM dd, yyyy"));
                    item.setPlan_expired(changeDate(obj.getString("plan_expired"), "MMM dd, yyyy"));
                    item.setPlan_duration(obj.getString("plan_duration") + " Days");

                    item.setMessage_used(obj.getString("message_used"));
                    item.setMessage(obj.getString("message"));

                    item.setContacts(obj.getString("contacts"));
                    item.setContacts_used(obj.getString("contacts_used"));

                    item.setChat(obj.getString("chat"));
                    item.setProfile(obj.getString("profile"));
                    item.setProfile_used(obj.getString("profile_used"));
                    item.setTax_percentage(obj.getString("tax_percentage"));
                    item.setTax_name(obj.getString("tax_name"));
                    item.setGrand_total(obj.getString("grand_total"));
                    item.setTax_amount(obj.getString("tax_amount"));
                    item.setDiscount_amount(obj.getString("discount_amount"));
                    item.setOffer_per(obj.getString("offer_per"));

                    list.add(item);
                    adapter.notifyDataSetChanged();

                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    lv_plan.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(progressBar);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    public String changeDate(String time, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    class currentAdapter extends ArrayAdapter<CurrentPlanItem> {
        Context context;
        List<CurrentPlanItem> list;

        public currentAdapter(Context context, List<CurrentPlanItem> list) {
            super(context, R.layout.current_plan_item, list);
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.current_plan_item, null, true);

            TextView tv_prise = convertView.findViewById(R.id.tv_prise);
            TextView tv_active_on = convertView.findViewById(R.id.tv_active_on);
            TextView tv_exp_on = convertView.findViewById(R.id.tv_exp_on);
            TextView tv_duration = convertView.findViewById(R.id.tv_duration);
            TextView tv_message = convertView.findViewById(R.id.tv_message);
            TextView tv_contact = convertView.findViewById(R.id.tv_contact);
            TextView tv_name = convertView.findViewById(R.id.tv_name);
            TextView lblOffer = convertView.findViewById(R.id.lblOffer);
            TextView tv_offer = convertView.findViewById(R.id.tv_offer);

            RelativeLayout lay_gst = convertView.findViewById(R.id.lay_gst);

            TextView tv_chat = convertView.findViewById(R.id.tv_chat);
            TextView tv_pro = convertView.findViewById(R.id.tv_pro);
            TextView tv_gst = convertView.findViewById(R.id.tv_gst);
            TextView tv_t_amt = convertView.findViewById(R.id.tv_t_amt);
            TextView gst_label = convertView.findViewById(R.id.gst_label);
            TextView tv_disc = convertView.findViewById(R.id.tv_disc);

            CurrentPlanItem item = list.get(position);

            tv_name.setText(item.getPlan_name());
            tv_chat.setText(item.getChat());

            int pro_use = Integer.parseInt(item.getProfile_used());
            int pro_t = Integer.parseInt(item.getProfile());
            int avl_pro = pro_t - pro_use;
            String pro = avl_pro + " out of " + item.getProfile();
            tv_pro.setText(pro);

            if (item.getDiscount_amount().equals("0")) {
                tv_disc.setText("N/A");
            } else {
                tv_disc.setText(item.getCurrency() + " " + item.getDiscount_amount());
            }

            if (item.getOffer_per().equals("0")) {
                lblOffer.setText("Offer Amount");
                tv_offer.setText("N/A");
            } else {
                lblOffer.setText("Offer Amount (" + item.getOffer_per() + "% Off)");

                float planAmount = Float.parseFloat(item.getPlan_amount());
                float planDiscount = Float.parseFloat(item.getOffer_per());
                float finalOffAmount = planAmount * planDiscount / 100;

                tv_offer.setText(item.getCurrency() + " " +new DecimalFormat("##.##").format(finalOffAmount));
            }

            String gst = item.getCurrency() + " " + item.getTax_amount();
            tv_gst.setText(gst);

            String tamt = item.getCurrency() + " " + item.getGrand_total();
            tv_t_amt.setText(tamt);

            if (item.getTax_name().equals("")) {
                lay_gst.setVisibility(View.GONE);
            } else {
                lay_gst.setVisibility(View.VISIBLE);
                String gst_lbl = item.getTax_name() + " (" + item.getTax_percentage() + "%)";
                gst_label.setText(gst_lbl);
            }

            String prise = item.getCurrency() + " " + item.getPlan_amount();
            tv_prise.setText(prise);
            tv_active_on.setText(item.getPlan_activated());
            tv_exp_on.setText(item.getPlan_expired());
            tv_duration.setText(item.getPlan_duration());

            int msg_use = Integer.parseInt(item.getMessage_used());
            int msg_t = Integer.parseInt(item.getMessage());
            int avl_msg = msg_t - msg_use;
            String msg = avl_msg + " out of " + item.getMessage();
            tv_message.setText(msg);

            int cont_use = Integer.parseInt(item.getContacts_used());
            int cont_t = Integer.parseInt(item.getContacts());
            int avl_cont = cont_t - cont_use;
            String cont = avl_cont + " out of " + item.getContacts();
            tv_contact.setText(cont);

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void backPressed() {
        if (isFromSuccessPayment) {
            Intent i = new Intent(this, DashboardActivity.class);
            i.putExtra("isFromSuccessPayment", true);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            finish();
        }
    }
}
