package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.VendorListActivity;
import com.advance.sanatani_vivah.model.CategoryModel;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FirstCategoryListAdapter extends RecyclerView.Adapter<FirstCategoryListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<CategoryModel> arrayList = null;
    private List<CategoryModel> filterArrayList;
    private String categoryImageUrl = "";

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterArrayList = arrayList;
                } else {
                    List<CategoryModel> filteredList = new ArrayList<>();
                    for (CategoryModel row : arrayList) {
                        if (row.getCategoryName().toLowerCase().contains(charString.toLowerCase())) {
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
                filterArrayList = (ArrayList<CategoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public FirstCategoryListAdapter(Context context, List<CategoryModel> list, String categoryImageUrl) {
        this.context = context;
        this.arrayList = list;
        this.filterArrayList = list;
        this.categoryImageUrl = categoryImageUrl;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_first_category_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CategoryModel item = filterArrayList.get(position);

        holder.lblCategory.setText(item.getCategoryName());
        if (item.getCategoryImage() != null && item.getCategoryImage().length() > 0) {
            Picasso.get().load(categoryImageUrl + item.getCategoryImage()).placeholder(R.drawable.ic_cover_place_holder).error(R.drawable.ic_cover_place_holder).into(holder.imgCategory);
            AppDebugLog.print("image url : " + categoryImageUrl + item.getCategoryImage());
        } else
            holder.imgCategory.setImageResource(R.drawable.ic_cover_place_holder);
    }

    @Override
    public int getItemCount() {
        return filterArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView lblCategory;
        public ImageView imgCategory;

        public MyViewHolder(View view) {
            super(view);
            lblCategory = view.findViewById(R.id.lblCategory);
            imgCategory = view.findViewById(R.id.imgCategory);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CategoryModel categoryModel = filterArrayList.get(getAdapterPosition());
            Intent intent = new Intent(context, VendorListActivity.class);
            intent.putExtra("category_id", categoryModel.getId());
            context.startActivity(intent);
        }
    }

}
