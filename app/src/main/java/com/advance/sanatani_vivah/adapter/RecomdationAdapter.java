package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.R;

import java.util.List;

public class RecomdationAdapter extends ArrayAdapter<DashboardItem> implements OnLikeListener{
    Context context;
    List<DashboardItem> list;
    Common common;
    public RecomdationAdapter(Context context,List<DashboardItem> list) {
        super(context, R.layout.recomdation_item, list);
        this.context = context;
        this.list=list;
        common=new Common(context);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.recomdation_item, null, true);

        final LikeButton btn_interest = (LikeButton) rowView.findViewById(R.id.btn_interest);
        LikeButton btn_like = (LikeButton) rowView.findViewById(R.id.btn_like);
        LikeButton btn_block = (LikeButton) rowView.findViewById(R.id.btn_id);
        LikeButton btn_chat = (LikeButton) rowView.findViewById(R.id.btn_chat);

        btn_like.setOnLikeListener(this);
        btn_block.setOnLikeListener(this);
        btn_interest.setOnLikeListener(this);

        ImageView img_profile=(ImageView) rowView.findViewById(R.id.img_profile);
        TextView tv_name=(TextView) rowView.findViewById(R.id.tv_name);
        TextView tv_detail = (TextView) rowView.findViewById(R.id.tv_detail);

        //img_profile.setImageResource(list.get(position).getIcon());
        img_profile.setImageResource(list.get(position).getIcon());
        tv_name.setText(list.get(position).getName().toUpperCase());

        String about="27 Year,5'6'' Height,Gujarati-Hindu,Orlando-Canada...<font color='#ff041a'>Read More</font>";
        tv_detail.setText(Html.fromHtml(about));

        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OtherUserProfileActivity.class));
            }
        });

        return rowView;
    }

    @Override
    public void liked(final LikeButton likeButton) {
        switch (likeButton.getId()){
            case R.id.btn_like:
                common.showAlert("Like","You Like This Profile.",R.drawable.heart_fill_pink);
                break;
            case R.id.btn_id:
                common.showAlert("Block","You Block This Profile.",R.drawable.ban);
                break;
            case R.id.btn_interest:
                likeButton.setLiked(false);
                LayoutInflater inflater1= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View vv = inflater1.inflate(R.layout.bottom_sheet_interest, null,true);
                //context.getLayoutInflater().inflate(R.layout.bottom_sheet_interest, null);

                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(vv);
                dialog.show();

                Button send=(Button)vv.findViewById(R.id.btn_send_intr);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        common.showAlert("Interest","You sent Interest to This Profile.",R.drawable.check_fill_green);
                        likeButton.setLiked(true);
                    }
                });
                break;
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        switch (likeButton.getId()){
            case R.id.btn_like:
                common.showAlert("Unlike","You Unlike This Profile.",R.drawable.heart_gray_fill);
                break;
            case R.id.btn_id:
                common.showAlert("Unblock","You Unblock This Profile.",R.drawable.ban_gry);
                break;

            case R.id.btn_interest:
                likeButton.setLiked(true);
                common.showToast("You already sent interest to this user.");
                break;
        }
    }
}
