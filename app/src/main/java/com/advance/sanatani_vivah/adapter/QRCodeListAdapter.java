package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.model.QRCodeBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QRCodeListAdapter extends RecyclerView.Adapter<QRCodeListAdapter.MyViewHolder> {
    private Context context;
    private List<QRCodeBean> arrayList = null;

    public QRCodeListAdapter(Context context, List<QRCodeBean> list) {
        this.context = context;
        this.arrayList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_qr_code, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final QRCodeBean item = arrayList.get(position);

        Picasso.get().load(item.getLogo()).placeholder(R.drawable.ic_bank_placeholder).error(R.drawable.ic_bank_placeholder).into(holder.imgCom);
        Picasso.get().load(item.getQrCode()).placeholder(R.drawable.ic_bank_placeholder).error(R.drawable.ic_bank_placeholder).into(holder.imgQr);

        holder.lblUpi.setText(item.getUpiId());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private ImageView imgCom,imgQr;
        private TextView lblUpi;

        public MyViewHolder(View view) {
            super(view);
            imgCom = itemView.findViewById(R.id.imgCom);
            imgQr = itemView.findViewById(R.id.imgQr);
            lblUpi = itemView.findViewById(R.id.lblUpi);
        }

    }
}
