package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.model.BankDetailsBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BankDetailsListAdapter extends RecyclerView.Adapter<BankDetailsListAdapter.MyViewHolder> {
    private Context context;
    private List<BankDetailsBean> arrayList = null;

    public BankDetailsListAdapter(Context context, List<BankDetailsBean> list) {
        this.context = context;
        this.arrayList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bank_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BankDetailsBean item = arrayList.get(position);

        Picasso.get().load(item.getLogo()).placeholder(R.drawable.ic_bank_placeholder).error(R.drawable.ic_bank_placeholder).into(holder.imgBank);
        holder.tvBankLabel.setText(
                "Account No        : " + item.getAccountNo() + "\n" +
                "Account Name  : " + item.getAccountName() + "\n" +
                "Account Type    : " + item.getAccountType() + "\n" +
                "IFSC Code          : " + item.getIfscCode());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBank;
        private TextView tvBankLabel;

        public MyViewHolder(View view) {
            super(view);
            imgBank = itemView.findViewById(R.id.imgBank);
            tvBankLabel = itemView.findViewById(R.id.tvBankLabel);

        }
    }
}
