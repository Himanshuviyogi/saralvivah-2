
package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.model.ChatConversationBean;
import com.advance.sanatani_vivah.model.CreateConversationBean;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;


public class ConversationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatConversationBean> arrayList;
    private CreateConversationBean createConversationBean;
    private ItemListener myListener;
    private Context mContext;

    public final int TYPE_DATA = 0;
    public final int TYPE_LOAD = 1;
    private static final int VIEW_TYPE_MESSAGE_SENT = 2;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 3;

    private SessionManager session;
    private int placeHolderId;
    private int myPlaceHolderId;

    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    public ConversationListAdapter(Context mContext, List<ChatConversationBean> arrayList, CreateConversationBean createConversationBean) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        session = new SessionManager(mContext);
        placeHolderId = session.getPlaceHolderDrawableId();
        myPlaceHolderId = session.getMyPlaceHolderDrawableId();
        this.createConversationBean = createConversationBean;
    }

    public void setListener(ItemListener listener) {
        myListener = listener;
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!arrayList.get(position).getId().equals("loading")) {
            ChatConversationBean conversationBean = arrayList.get(position);
            if (conversationBean.getIsSender()) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                // If some other user sent the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        } else {
            return TYPE_LOAD;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_LOAD) {
            return new LoadHolder(inflater.inflate(R.layout.cell_load_list, parent, false));
        } else if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            return new SenderViewHolder(inflater.inflate(R.layout.chat_conversation_me, parent, false));
        } else {
            return new ReceiverViewHolder(inflater.inflate(R.layout.chat_conversation_other, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == VIEW_TYPE_MESSAGE_SENT) {
            ChatConversationBean conversationBean = arrayList.get(position);

            if (conversationBean.getSendOn() != null && conversationBean.getSendOn().length() > 0) {
                Date chatTime = Common.getDateFromDateString(AppConstants.fullDateTimeFormat, conversationBean.getSendOn());
                ((SenderViewHolder) holder).lblTime.setText(Common.getFormattedDate(chatTime.getTime(), true));
            }

            String msg =conversationBean.getMessage(); //StringEscapeUtils.unescapeJava(conversationBean.getMessage());
            AppDebugLog.print("message : " + msg);
            ((SenderViewHolder) holder).lblMessage.setText(msg);
            if (createConversationBean.getSenderMemberPhotoUrl() != null && createConversationBean.getSenderMemberPhotoUrl().length() > 0) {
                Picasso.get()
                        .load(createConversationBean.getSenderMemberPhotoUrl())
                        .placeholder(myPlaceHolderId)
                        .error(myPlaceHolderId)
                        .fit()
                        // .resize(0, imageWidthHeight)
                        .centerCrop()
                        .into(((SenderViewHolder) holder).imgProfile);
            } else {
                ((SenderViewHolder) holder).imgProfile.setImageResource(myPlaceHolderId);
            }
        }

        if (getItemViewType(position) == VIEW_TYPE_MESSAGE_RECEIVED) {
            ChatConversationBean conversationBean = arrayList.get(position);

            if (conversationBean.getSendOn() != null && conversationBean.getSendOn().length() > 0) {
                Date chatTime = Common.getDateFromDateString(AppConstants.fullDateTimeFormat, conversationBean.getSendOn());
                ((ReceiverViewHolder) holder).lblTime.setText(Common.getFormattedDate(chatTime.getTime(), true));
            }else{
                Date chatTime = new Date();
                ((ReceiverViewHolder) holder).lblTime.setText(Common.getFormattedDate(chatTime.getTime(), true));
            }

            String msg = conversationBean.getMessage();//StringEscapeUtils.unescapeJava(conversationBean.getMessage());
            ((ReceiverViewHolder) holder).lblMessage.setText(msg);

            if (createConversationBean.getReceiverMemberPhotoUrl() != null && createConversationBean.getReceiverMemberPhotoUrl().length() > 0) {
                Picasso.get()
                        .load(createConversationBean.getReceiverMemberPhotoUrl())
                        .placeholder(placeHolderId)
                        .error(placeHolderId)
                        .fit()
                        // .resize(0, imageWidthHeight)
                        .centerCrop()
                        .into(((ReceiverViewHolder) holder).imgProfile);
            } else {
                ((ReceiverViewHolder) holder).imgProfile.setImageResource(placeHolderId);
            }
        }
    }


    private String getValueString(String str) {
        String tempStr = mContext.getString(R.string.lbl_not_available);
        if (str != null && str.length() > 0) return str;
        else return tempStr;
    }

    public interface ItemListener {
      //  void deleteMessage(ChatConversationBean item, int position);
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView lblMessage, lblTime;
        private ImageView imgProfile;

        public SenderViewHolder(View view) {
            super(view);

            imgProfile = view.findViewById(R.id.imgProfile);
            lblMessage = view.findViewById(R.id.lblMessage);
            lblTime = view.findViewById(R.id.lblTime);

            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            AppDebugLog.print("onLongClick view id : " + v.getId());
            if (myListener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                ChatConversationBean conversationBean = arrayList.get(getAbsoluteAdapterPosition());
                //myListener.deleteMessage(conversationBean, getAbsoluteAdapterPosition());
            }
            return true;
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView lblMessage;
        private TextView lblTime;
        private ImageView imgProfile;

        public ReceiverViewHolder(View view) {
            super(view);

            imgProfile = view.findViewById(R.id.imgProfile);
            lblMessage = view.findViewById(R.id.lblMessage);
            lblTime = view.findViewById(R.id.lblTime);

            //view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            AppDebugLog.print("onLongClick view id : " + v.getId());
            if (myListener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                ChatConversationBean conversationBean = arrayList.get(getAbsoluteAdapterPosition());
                //myListener.deleteMessage(conversationBean, getAbsoluteAdapterPosition());
            }
            return true;
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
                                