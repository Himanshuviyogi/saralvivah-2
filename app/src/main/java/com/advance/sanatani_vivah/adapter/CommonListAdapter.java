package com.advance.sanatani_vivah.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.like.LikeButton;
import com.like.OnLikeListener;
//import androidx.cardview.widget.CardView;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.ConversationActivity;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.utility.Common;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

public class CommonListAdapter extends RecyclerView.Adapter<CommonListAdapter.ViewHolder> {
    public Context mContext;
    private List<DashboardItem> arrayList;
    private ItemListener myListener;

    int placeHolder = 0;
    private Common common;

    public CommonListAdapter(Context mContext, List<DashboardItem> arrayList) {
        if (mContext == null) return;
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.common = new Common(mContext);
    }

    public void setListener(ItemListener listener) {
        myListener = listener;
    }

    public interface ItemListener {
        void alertPhotoPassword(String matriId);
        void likeRequest(String value,String name,int position);
        void shortlistRequest(String value,String name);
        void interestRequest(String value,String name,LikeButton button);
        void blockRequest(String value,String name);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.recomdation_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = arrayList.get(position);

        holder.tv_name.setText(item.getName().toUpperCase());

        String description = Common.getDetailsFromValue(item.getProfileCreatedBy(),item.getAge(), item.getHeight(),
                item.getCaste(), item.getReligion(),
                item.getState(), item.getCountry(),item.getEducation());
        holder.tv_detail.setText(Html.fromHtml(description));

        common.setImage(item.getPhoto_view_count(),item.getPhoto_view_status(), item.getImage_approval(),
                item.getPhotoUrl()+item.getImage(), holder.img_profile, null,20);

        if(item.getBadge().length() > 0){
            Picasso.get().load(item.getBadgeUrl()+item.getBadge())
                    .placeholder(R.drawable.ic_transparent_placeholder)
                    .error(R.drawable.ic_transparent_placeholder)
                    .into(holder.imgPLanStamp);
            holder.imgPLanStamp.setVisibility(View.VISIBLE);
        }else{
            holder.imgPLanStamp.setVisibility(View.GONE);
        }

        if(item.getColor().length() > 0){
            //holder.cardView.setShadowColor(Color.parseColor(""+item.getColor()));
        }

        try {
            if (item.getAction().getString("is_like").equals("Yes"))
                holder.btn_like.setLiked(true);
            else
                holder.btn_like.setLiked(false);

            if (item.getAction().getInt("is_block") == 1)
                holder.btn_block.setLiked(true);
            else
                holder.btn_block.setLiked(false);

            if (!item.getAction().getString("is_interest").equals(""))
                holder.btn_interest.setLiked(true);
            else
                holder.btn_interest.setLiked(false);

            if (item.getAction().getInt("is_shortlist") == 1)
                holder.btn_short.setLiked(true);
            else
                holder.btn_short.setLiked(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener {
        CardView cardView;
        ImageView imgPLanStamp;
        TextView tv_name;
        TextView tv_detail;
        ImageView img_profile;

        LikeButton btn_interest;
        LikeButton btn_like;
        LikeButton btn_block;
        LikeButton btn_chat;
        LikeButton btn_short;

        public ViewHolder(@NonNull View rowView) {
            super(rowView);

            cardView = rowView.findViewById(R.id.cardView);
            imgPLanStamp = rowView.findViewById(R.id.imgPLanStamp);
            tv_name = rowView.findViewById(R.id.tv_name);
            tv_detail = rowView.findViewById(R.id.tv_detail);
            img_profile = rowView.findViewById(R.id.img_profile);
            btn_interest = rowView.findViewById(R.id.btn_interest);
            btn_like = rowView.findViewById(R.id.btn_like);
            btn_block = rowView.findViewById(R.id.btn_id);
            btn_chat = rowView.findViewById(R.id.btn_chat);
            btn_short = rowView.findViewById(R.id.btn_short);

            img_profile.setOnClickListener(this);
            tv_detail.setOnClickListener(this);
            tv_name.setOnClickListener(this);
            btn_chat.setOnClickListener(this);

            btn_like.setOnLikeListener(this);
            btn_block.setOnLikeListener(this);
            btn_interest.setOnLikeListener(this);
            btn_short.setOnLikeListener(this);
        }

        @Override public void onClick(View view) {
            DashboardItem item = arrayList.get(getAbsoluteAdapterPosition());
            if(view.getId() == R.id.img_profile) {
                if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("0")) {
                    myListener.alertPhotoPassword(item.getMatri_id());
                } else if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("1") && item.getImage_approval().equals("APPROVED")) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.show_image_alert);
                    TouchImageView img_url = dialog.findViewById(R.id.img_url);
                    Picasso.get().load(item.getImage()).placeholder(placeHolder).error(placeHolder).into(img_url);
                    dialog.show();
                } else {
                    openScreenAsPerPlan(item);
                }
            } else if(view.getId() == R.id.btn_chat) {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(mContext, ConversationActivity.class);
                    i.putExtra("matri_id", item.getName());
                    mContext.startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to chat with this member.");
                    mContext.startActivity(new Intent(mContext, PlanListActivity.class));
                }
            } else if(view.getId() == R.id.tv_detail) {
                openScreenAsPerPlan(item);
            } else if(view.getId() == R.id.tv_name) {
                openScreenAsPerPlan(item);
            }
        }

        @Override public void liked(LikeButton likeButton) {
            if(arrayList.size() == 0) return;
            DashboardItem item = arrayList.get(getAbsoluteAdapterPosition());
            if(likeButton.getId() == R.id.btn_like) {
                likeRequest("Yes",item);
            } else if(likeButton.getId() == R.id.btn_interest) {
                likeButton.setLiked(false);
                LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View vv = inflater1.inflate(R.layout.bottom_sheet_interest, null, true);
                final RadioGroup grp_interest = vv.findViewById(R.id.grp_interest);
                final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
                dialog.setContentView(vv);
                dialog.show();

                Button send = vv.findViewById(R.id.btn_send_intr);
                send.setOnClickListener(view12 -> {
                    dialog.dismiss();
                    if (grp_interest.getCheckedRadioButtonId() != -1) {
                        RadioButton btn = vv.findViewById(grp_interest.getCheckedRadioButtonId());
                        myListener.interestRequest(item.getName(), btn.getText().toString().trim(), likeButton);
                    }
                });
            } else if(likeButton.getId() == R.id.btn_short) {
                myListener.shortlistRequest("add", item.getName());
            } else if(likeButton.getId() == R.id.btn_id) {
                blockRequest(1,"add", item);
            }
        }

        @Override public void unLiked(LikeButton likeButton) {
            if(arrayList.size() == 0) return;
            DashboardItem item = arrayList.get(getAbsoluteAdapterPosition());
            if(likeButton.getId() == R.id.btn_like) {
                likeRequest("No",item);
            } else if(likeButton.getId() == R.id.btn_interest) {
                likeButton.setLiked(true);
                common.showToast("You already sent interest to this user.");
            } else if(likeButton.getId() == R.id.btn_short) {
                myListener.shortlistRequest("remove", item.getName());
            } else if(likeButton.getId() == R.id.btn_id) {
                blockRequest(0,"remove", item);
            }
        }

        private void blockRequest(int val,String value,DashboardItem item) {
            try {
                item.getAction().put("is_block",val);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myListener.blockRequest(value, item.getName());
        }

        private void likeRequest(String value,DashboardItem item) {
            try {
                item.getAction().put("is_like",value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myListener.likeRequest(value, item.getName(), getAbsoluteAdapterPosition());
        }

        private void openScreenAsPerPlan(DashboardItem item) {
            if (MyApplication.getPlan()) {
                Intent i = new Intent(mContext, OtherUserProfileActivity.class);
                i.putExtra("other_id", item.getId());
                mContext.startActivity(i);
            } else {
                common.showToast("Please upgrade your membership to chat with this member.");
                mContext.startActivity(new Intent(mContext, PlanListActivity.class));
            }
        }
    }

}
