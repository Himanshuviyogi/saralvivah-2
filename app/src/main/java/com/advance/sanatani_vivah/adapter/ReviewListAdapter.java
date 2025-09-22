package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.model.ReviewBean;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {
    private Context context;
    private List<ReviewBean> arrayList = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lblTitle,lblDescription,lblDate;

        public MyViewHolder(View view) {
            super(view);
            lblTitle = view.findViewById(R.id.lblTitle);
            lblDescription = view.findViewById(R.id.lblDescription);
            lblDate = view.findViewById(R.id.lblDate);
        }
    }

    public ReviewListAdapter(Context context, List<ReviewBean> list) {
        this.context = context;
        this.arrayList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_review_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ReviewBean item = arrayList.get(position);

        holder.lblTitle.setText(item.getrTitle());
        holder.lblDescription.setText(item.getrMessage());
        holder.lblDate.setText(item.getrDate());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
