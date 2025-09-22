package com.advance.sanatani_vivah.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderAdapter;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.model.PlanDatum;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PremiumPlanAdapter extends CardSliderAdapter<PremiumPlanAdapter.ViewHolder> {

    private ArrayList<PlanDatum> arrayList;

    private ItemListener myListener;

    public PremiumPlanAdapter(ArrayList<PlanDatum> list) {
        this.arrayList = list;
    }

    public void setListener(ItemListener listener) {
        myListener = listener;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_premium_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindVH(ViewHolder viewHolder, int i) {

        PlanDatum item = arrayList.get(i);

        String amountType = "₹";
        if (item.getPlanAmountType().equalsIgnoreCase("USD")) amountType = "$";

        float planAmount = Float.parseFloat(item.getPlanAmount());
        float planDiscount = Float.parseFloat(item.getOfferPer());
        float finalPlanAmount = planAmount;

        viewHolder.lblPlanName.setText(item.getPlanName());
        viewHolder.lblPlanDuration.setText(item.getPlanDuration() + " Days");
       // viewHolder.lblPlanAmount.setText(item.getPlanAmountType() + " " + item.getPlanAmount());

        if (planDiscount > 0) {
            finalPlanAmount = planAmount - ((planAmount * planDiscount) / 100);
            viewHolder.tv_org_prise.setPaintFlags(viewHolder.tv_org_prise.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tv_org_prise.setText(amountType + "" + item.getPlanAmount());
            viewHolder.tv_org_prise.setVisibility(View.VISIBLE);
            viewHolder.tv_discount.setVisibility(View.VISIBLE);
            viewHolder.tv_discount.setText(item.getOfferPer() + "%");
        } else {
            viewHolder.tv_org_prise.setVisibility(View.GONE);
            viewHolder.tv_discount.setVisibility(View.GONE);
        }

        viewHolder.lblPlanAmount.setText(amountType + "" + finalPlanAmount);

        float perDayAmount =(finalPlanAmount / Integer.parseInt(item.getPlanDuration()));
        viewHolder.tv_per_day.setText(amountType + "" + new DecimalFormat("##.##").format(perDayAmount));

        viewHolder.lblMessages.setText("Allowed Message - "+ item.getPlanMsg());
        viewHolder.lblContactsOffer.setText("Allowed Contacts - "+ item.getPlanContacts());
        viewHolder.lblViewProfile.setText("Allowed View Profiles - "+ item.getProfile());
        if(item.getChat().equalsIgnoreCase("Yes")){
            viewHolder.layChat.setVisibility(View.VISIBLE);
        }else{
            viewHolder.layChat.setVisibility(View.GONE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lblPlanName, lblPlanDuration, lblPlanAmount,tv_discount,tv_org_prise,tv_per_day;
        private Button btnContinue;
        private TextView lblContactsOffer;
        private TextView lblMessages;
        private TextView lblViewProfile;
        private TextView layChat;

        public ViewHolder(View view) {
            super(view);

            lblPlanName = view.findViewById(R.id.lblPlanName);
            lblPlanDuration = view.findViewById(R.id.lblPlanDuration);
            lblPlanAmount = view.findViewById(R.id.lblPlanAmount);
            lblContactsOffer = view.findViewById(R.id.lblContactsOffer);
            lblMessages = view.findViewById(R.id.lblMessages);
            tv_discount = view.findViewById(R.id.tv_discount);
            tv_org_prise = view.findViewById(R.id.tv_org_prise);
            tv_per_day = view.findViewById(R.id.tv_per_day);
            lblViewProfile = view.findViewById(R.id.lblViewProfile);
            layChat = view.findViewById(R.id.layChat);

            btnContinue = view.findViewById(R.id.btnContinue);

            btnContinue.setOnClickListener(v->{
                myListener.itemClicked(arrayList.get(getAbsoluteAdapterPosition()),getItemCount());
            });


        }
    }

    public interface ItemListener {
        void itemClicked(PlanDatum premiumPlanBean, int position);
    }
}
