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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.ChatConversationActivityNew;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.ChatMemberBean;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatMemberListAdapter extends RecyclerView.Adapter<ChatMemberListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<ChatMemberBean> list= null;
    private List<ChatMemberBean> contactListFiltered;

    private SessionManager session;

    public ChatMemberListAdapter(Context context, List<ChatMemberBean> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered=list;
        session = new SessionManager(context);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list;
                } else {
                    List<ChatMemberBean> filteredList = new ArrayList<>();
                    for (ChatMemberBean row : list) {
                        if (row.getMatriId().toLowerCase().contains(charString.toLowerCase()) || row.getLastMessage().contains(charSequence)) {
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
                contactListFiltered = (ArrayList<ChatMemberBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_member_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ChatMemberBean item=contactListFiltered.get(position);

        holder.tv_name.setText(item.getMatriId());
//        String msg="";
//        if (item.getLastMessage().length()>=25){
//            msg=item.getLastMessage().substring(0, 25)+"...<font color=#a30412>Read more</font>";
//        }else {
//            msg=item.getLastMessage();
//        }
        if(item.getIsOnline() == 1) {
            holder.imgStatus.setImageResource(R.color.green);
        }else{
            holder.imgStatus.setImageResource(R.color.gray_1);
        }
        if(item.getIsRead().equalsIgnoreCase("Yes")) {
            holder.tv_msg.setTextColor(ContextCompat.getColor(context,R.color.black));
        }else{
            holder.tv_msg.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        holder.tv_msg.setText(Html.fromHtml(item.getLastMessage()));

        holder.tv_date.setText(item.getLastMessageDate());
        if (!item.getMemberPhotoUrl().equals(""))
            Picasso.get().load(item.getMemberPhotoUrl()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.img_profile);
        else
            holder.img_profile.setImageResource(R.drawable.placeholder);

        if(item.getUnreadMessageCount() > 0) {
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_count.setText(""+item.getUnreadMessageCount());
        }else{
            holder.tv_count.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_msg, tv_date,tv_count;
        public ImageView img_profile,imgStatus;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            tv_count = view.findViewById(R.id.tv_count);
            tv_name = view.findViewById(R.id.tv_name);
            tv_msg = view.findViewById(R.id.tv_msg);
            tv_date = view.findViewById(R.id.tv_date);
            imgStatus = view.findViewById(R.id.imgStatus);
            img_profile = view.findViewById(R.id.img_profile);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            viewForeground.setOnClickListener(view1 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, ChatConversationActivityNew.class);
                    i.putExtra("CONVERSATION_ID", list.get(getAbsoluteAdapterPosition()).getConversationId());
                    i.putExtra("OTHER_ID", list.get(getAbsoluteAdapterPosition()).getMemberId());
                    i.putExtra("OTHER_MATRI_ID", list.get(getAbsoluteAdapterPosition()).getMatriId());
                    if (list.get(getAbsoluteAdapterPosition()).getIsOnline() == 1) {
                        i.putExtra("STATUS", "Online");
                    } else {
                        i.putExtra("STATUS", "Ofline");
                    }
                    context.startActivity(i);
                }else{
                    Common.showToast("Please upgrade your membership plan to chat.");
                }
            });
        }
    }

    public void removeItem(int position) {
        contactListFiltered.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ChatMemberBean item, int position) {
        contactListFiltered.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
