package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chaek.android.RatingBar;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.VendorDetailsActivity;
import com.advance.sanatani_vivah.model.VendorModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<VendorModel> arrayList = null;
    private List<VendorModel> filterArrayList;
    private String imageUrl;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterArrayList = arrayList;
                } else {
                    List<VendorModel> filteredList = new ArrayList<>();
                    for (VendorModel row : arrayList) {
                        if (row.getPlannerName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    filterArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterArrayList = (ArrayList<VendorModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView,imgVerify;
        private TextView lblName, lblDescription, lblAddress, lblRating;
        private RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            imageView = itemView.findViewById(R.id.imageView);
            imgVerify = itemView.findViewById(R.id.imgVerify);
            lblName = itemView.findViewById(R.id.tv_sp_name_cell);
            lblAddress = itemView.findViewById(R.id.tv_sp_address_cell);
            lblDescription = itemView.findViewById(R.id.lblDescription);
            lblRating = itemView.findViewById(R.id.lblRating);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            view.setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            VendorModel vendorModel = filterArrayList.get(getAdapterPosition());
            Intent intent = new Intent(context, VendorDetailsActivity.class);
            intent.putExtra("vendor_id",vendorModel.getId());
            intent.putExtra("imageUrl",imageUrl);
            context.startActivity(intent);
        }
    }

    public VendorListAdapter(Context context, List<VendorModel> list,String imageUrl) {
        this.context = context;
        this.arrayList = list;
        this.filterArrayList = list;
        this.imageUrl = imageUrl;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vendor_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final VendorModel item = filterArrayList.get(position);

        if (item.getImage() != null && item.getImage().length() > 0) {
            Picasso.get().load(imageUrl+item.getImage()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_placeholder);
        }

        if (item.getVerifiedBySW() != null && item.getVerifiedBySW().equalsIgnoreCase("Yes")) {
            holder.imgVerify.setVisibility(View.VISIBLE);
        }else{
            holder.imgVerify.setVisibility(View.GONE);
        }

        if (item.getPlannerName() != null && item.getPlannerName().length() > 0) {
             holder.lblName.setText(item.getPlannerName());
        }
        if (item.getAddress()!= null && item.getAddress().length() > 0) {
             holder.lblAddress.setText(item.getAddress());
        }
        if (item.getDescription()!= null && item.getDescription().length() > 0) {
            holder.lblDescription.setText((Html.fromHtml(item.getDescription())));
        }
        if (item.getWeddingPlannerReviewsCount()!= null && item.getWeddingPlannerReviewsCount().length() > 0) {
            holder.lblRating.setText(item.getWeddingPlannerReviewsCount() + " Reviews");
        }
        if (item.getWeddingPlannerAverage()!= null && item.getWeddingPlannerAverage().length() > 0) {
            holder.ratingBar.setScore(Math.round(Float.valueOf(item.getWeddingPlannerAverage())));
        }
    }

    @Override
    public int getItemCount() {
        return filterArrayList.size();
    }
}
