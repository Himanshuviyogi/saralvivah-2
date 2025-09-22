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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.utility.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.ViewHolder> {
    public Context mContext;
    private List<DashboardItem> arrayList;
    private ItemListener myListener;

    int placeHolder = 0;
    private Common common;

    public LikeListAdapter(Context mContext, List<DashboardItem> arrayList) {
        if (mContext == null) return;
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.common = new Common(mContext);
    }

    public void setListener(ItemListener listener) {
        myListener = listener;
    }

    public interface ItemListener {
        void itemClicked(DashboardItem object, int position);
        void removeLike(int position,String matriId);
        void alertPhotoPassword(String matriId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.like_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = arrayList.get(position);

        holder.tv_name.setText(item.getMatri_id().toUpperCase());

        common.setImage(item.getPhoto_view_count(), item.getPhoto_view_status(), item.getImage_approval(),
                item.getPhotoUrl() + item.getImage(), holder.img_profile, null,68);

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
          //  holder.cardView.setShadowColor(Color.parseColor(""+item.getColor()));
        }

        //String about=item.getAbout()+"...<font color='#ff041a'>Read More</font>";
        holder.tv_detail.setText(Html.fromHtml(item.getAbout()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView imgPLanStamp;
        TextView tv_name;
        TextView tv_detail;
        ImageView img_profile;
        LikeButton btn_like;

        //for multi selection
        private ImageView layoutBg;

        public ViewHolder(@NonNull View rowView) {
            super(rowView);

            cardView = rowView.findViewById(R.id.cardView);
            imgPLanStamp = rowView.findViewById(R.id.imgPLanStamp);
            tv_name = rowView.findViewById(R.id.tv_name);
            tv_detail = rowView.findViewById(R.id.tv_detail);
            img_profile = rowView.findViewById(R.id.img_profile);
            btn_like = rowView.findViewById(R.id.btn_like);

            img_profile.setOnClickListener(this);
            tv_detail.setOnClickListener(this);
            tv_name.setOnClickListener(this);
            btn_like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if(arrayList.size() > 0) {
                        DashboardItem item = arrayList.get(getAbsoluteAdapterPosition());
                        myListener.removeLike(getAbsoluteAdapterPosition(), item.getMatri_id());
                    }
                }
            });
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
            } else if(view.getId() == R.id.tv_detail) {
                openScreenAsPerPlan(item);
            } else if(view.getId() == R.id.tv_name) {
                openScreenAsPerPlan(item);
            }
        }

        private void openScreenAsPerPlan(DashboardItem item) {
            if (MyApplication.getPlan()) {
                Intent i = new Intent(mContext, OtherUserProfileActivity.class);
                i.putExtra("other_id", item.getUser_id());
                mContext.startActivity(i);
            } else {
                common.showToast("Please upgrade your membership to chat with this member.");
                mContext.startActivity(new Intent(mContext, PlanListActivity.class));
            }
        }
    }

}
