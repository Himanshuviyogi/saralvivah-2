package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.model.Message_item;
import com.advance.sanatani_vivah.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Message_item> list= null;
    private List<Message_item> contactListFiltered;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list;
                } else {
                    List<Message_item> filteredList = new ArrayList<>();
                    for (Message_item row : list) {
                        if (row.getSender().toLowerCase().contains(charString.toLowerCase()) || row.getReceiver().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Message_item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_msg, tv_date;
        public ImageView img_profile;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_msg = view.findViewById(R.id.tv_msg);
            tv_date = view.findViewById(R.id.tv_date);
            img_profile = view.findViewById(R.id.img_profile);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public MessageAdapter(Context context,List<Message_item> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered=list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Message_item item=contactListFiltered.get(position);

        holder.tv_name.setText(item.getDisplay_name());
        String msg="";
        if (item.getContent().length()>=25){
            msg=item.getContent().substring(0, 25)+"...<font color=#a30412>Read more</font>";
        }else {
            msg=item.getContent();
        }
        holder.tv_msg.setText(Html.fromHtml(msg));
        holder.tv_date.setText(item.getSent_on());
        if (!item.getImage().equals(""))
            Picasso.get().load(item.getImage()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.img_profile);
        else
            holder.img_profile.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public void removeItem(int position) {
        contactListFiltered.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Message_item item, int position) {
        contactListFiltered.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
