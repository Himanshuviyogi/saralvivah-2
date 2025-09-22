package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.SuccessStoryActivity;
import com.advance.sanatani_vivah.model.SuccessStoryBean;
import com.advance.sanatani_vivah.utility.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SuccessStoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SuccessStoryBean> arrayList;
    private ItemListener myListener;
    private Context mContext;
    private String weddingphotoUrl;

    public final int TYPE_DATA = 0;
    public final int TYPE_LOAD = 1;

    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    private int deviceWidth = 0;

    public SuccessStoryListAdapter(Context mContext, List<SuccessStoryBean> arrayList,String weddingphotoUrl) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.weddingphotoUrl = weddingphotoUrl;

        deviceWidth = Common.getDisplayWidth((SuccessStoryActivity) mContext) - Common.convertDpToPixels(10, mContext);
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
        // if (!arrayList.get(position).getBridename().equals(mContext.getString(R.string.str_loading))) {
        return TYPE_DATA;
        //    } else {
        //      return TYPE_LOAD;
        // }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // if (viewType == TYPE_DATA) {
        return new ViewHolder(inflater.inflate(R.layout.cell_success_story_list, parent, false));
        //  } else {
        //    return new LoadHolder(inflater.inflate(R.layout.cell_load, parent, false));
        // }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
//            isLoading = true;
//            loadMoreListener.onLoadMore();
//        }

//        if (getItemViewType(position) == TYPE_DATA) {
        // StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        // layoutParams.setFullSpan(true);

        SuccessStoryBean successStoryBean = arrayList.get(position);

        ((ViewHolder) holder).lblTitle.setText(successStoryBean.getGroomname() + " ~ " + successStoryBean.getBridename());

        String message = successStoryBean.getSuccessmessage();

        if (message.length() > 200) {
            if (!successStoryBean.isDisplayFullMsg()) {
                message = successStoryBean.getSuccessmessage().substring(0, 200) + "...";
                ((ViewHolder) holder).lblDetail.setText(Html.fromHtml(message + mContext.getString(R.string.lbl_read_more)));
            } else {
                ((ViewHolder) holder).lblDetail.setText(Html.fromHtml(message));
            }
        } else {
            ((ViewHolder) holder).lblDetail.setText(Html.fromHtml(message));
        }

        if (successStoryBean.getWeddingphotoType().equalsIgnoreCase("photo")) {
            ((ViewHolder) holder).videoThumbNailLayout.setVisibility(View.GONE);
            ((ViewHolder) holder).imgProfile.setVisibility(View.VISIBLE);
            if (successStoryBean.getWeddingphoto() != null) {
                Picasso.get()
                        .load(weddingphotoUrl+successStoryBean.getWeddingphoto())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .resize(deviceWidth, 0)
                        .centerInside()
                        .into(((ViewHolder) holder).imgProfile);
            } else {
                ((ViewHolder) holder).imgProfile.setImageResource(R.drawable.ic_placeholder);
            }
        } else {
            //   ((ViewHolder) holder).videoThumbNailLayout.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).imgProfile.setVisibility(View.GONE);
            Glide.with(mContext)
                    .asBitmap()
                    .load(successStoryBean.getOgImage())
                    .into(((ViewHolder) holder).imgThumbnail);
        }
//        }
    }

    public interface ItemListener {
        void onItemClick(SuccessStoryBean item);

        void startVideoDialog(SuccessStoryBean item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblTitle, lblDetail;
        private ImageView imgProfile;
        private FrameLayout videoThumbNailLayout;
        private ImageView btnPlay;
        private ImageView imgThumbnail;

        public ViewHolder(View view) {
            super(view);

            lblTitle = view.findViewById(R.id.lblTitle);
            lblDetail = view.findViewById(R.id.lblDetail);
            imgProfile = view.findViewById(R.id.imgProfile);
            videoThumbNailLayout = view.findViewById(R.id.videoThumbNailLayout);
            btnPlay = view.findViewById(R.id.btnPlay);
            imgThumbnail = view.findViewById(R.id.imgThumbnail);

            //  itemView.setOnClickListener(this);

            lblDetail.setOnClickListener(this);
            imgThumbnail.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.lblDetail) {
                if (getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                    SuccessStoryBean successStoryBean = arrayList.get(getAbsoluteAdapterPosition());
                    successStoryBean.setSuccessmessage(successStoryBean.getSuccessmessage());
                    successStoryBean.setDisplayFullMsg(true);
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            } else if (v.getId() == R.id.imgThumbnail) {
                if (myListener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                    myListener.startVideoDialog(arrayList.get(getAbsoluteAdapterPosition()));
                }
            } else {
                if (myListener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                    SuccessStoryBean successStoryBean = arrayList.get(getAbsoluteAdapterPosition());
                    myListener.onItemClick(successStoryBean);
                }
            }
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
                                