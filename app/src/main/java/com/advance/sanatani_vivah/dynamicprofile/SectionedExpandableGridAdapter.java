package com.advance.sanatani_vivah.dynamicprofile;

import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;

import java.util.ArrayList;

/**
 * Created by lenovo on 2/23/2016.
 */
public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;
    private Common common;

    private Resources res;

    //use for in my profile display edit button & other user profile hide edit button
    private boolean isEditEnabled;

    //listeners
    private final ItemClickListener mItemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.layout_section;
    private static final int VIEW_TYPE_ITEM = R.layout.layout_item; //TODO : change this

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                          final GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener,
                                          SectionStateChangeListener sectionStateChangeListener, boolean isEditEnabled) {
        mContext = context;
        common = new Common(context);
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;
        res = context.getResources();

        this.isEditEnabled = isEditEnabled;

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof ViewProfileSectionBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM:
                final ViewProfileFieldsBean item = (ViewProfileFieldsBean) mDataArrayList.get(position);

                holder.lblName.setText(item.getTitle());

                if (isEditEnabled && item.getTitle().equalsIgnoreCase("Weight")) {
                    String weight;
                    if (checkField(item.getValue()).equals("N/A")) {
                        weight = "N/A";
                    } else {
                        if(item.getValue().contains("kg")) {
                            weight = item.getValue();
                        }else{
                            weight = item.getValue() + " Kg";
                        }
                    }
                    holder.lblValue.setText(weight);
                } else if (isEditEnabled && item.getTitle().equalsIgnoreCase("Height")) {
                    if (!checkField(item.getValue()).equals("N/A")) {
                        holder.lblValue.setText(common.calculateHeight(item.getValue()));
                    } else
                        holder.lblValue.setText("N/A");
                } else if (item.getTitle().equalsIgnoreCase("Height Preference")) {
                    if (!checkField(item.getValue()).equals("N/A")) {
                        if (item.getValue().split("to").length == 2) {
                            String from = item.getValue().split("to")[0].trim();
                            String to = item.getValue().split("to")[1].trim();
                            String fHeight = "", tHeight = "";
                            if (!checkField(from).equals("N/A")) {
                                fHeight = common.calculateHeight(from);
                            }
                            if (!checkField(to).equals("N/A")) {
                                tHeight = common.calculateHeight(to);
                            }
                            holder.lblValue.setText(fHeight + " to " + tHeight);
                        } else {
                            holder.lblValue.setText("N/A");
                        }
                    } else {
                        holder.lblValue.setText("N/A");
                    }
                } else if (isEditEnabled && item.getTitle().equalsIgnoreCase("Star") && item.getId().equalsIgnoreCase("star")) {
                    if (!checkField(item.getValue()).equals("N/A")) {
                        holder.lblValue.setText(ApplicationData.myProfileStarStr);
                    } else {
                        holder.lblValue.setText("N/A");
                    }
                } else if (isEditEnabled && item.getTitle().equalsIgnoreCase("Moonsign")) {
                    if (!checkField(item.getValue()).equals("N/A")) {
                        holder.lblValue.setText(ApplicationData.myProfileMoonSignStr);
                    } else {
                        holder.lblValue.setText("N/A");
                    }
                } else {
                    holder.lblValue.setText(checkField(item.getValue()));
                }

                holder.view.setOnClickListener(v -> mItemClickListener.itemClicked(item));
                break;

            case VIEW_TYPE_SECTION:
                final ViewProfileSectionBean viewProfileSectionBean = (ViewProfileSectionBean) mDataArrayList.get(position);
                holder.lblSection.setText(viewProfileSectionBean.getName());
                AppDebugLog.print("Section : " + viewProfileSectionBean.getId());
                AppDebugLog.print("Section : " + viewProfileSectionBean.getName());
                holder.lblSection.setOnClickListener(v -> {

                    mItemClickListener.lastSectionExpand(viewProfileSectionBean);

                    if(!isEditEnabled && viewProfileSectionBean.getId().equals("contact_info")){
                        if(viewProfileSectionBean.isContactVisible){
                            mSectionStateChangeListener.onSectionStateChanged(viewProfileSectionBean, !viewProfileSectionBean.isExpanded);
                        }else{
                            mItemClickListener.viewContact(viewProfileSectionBean);
                        }
                    } else if(position>0){
                        mSectionStateChangeListener.onSectionStateChanged(viewProfileSectionBean, !viewProfileSectionBean.isExpanded);
                    }

//                    if (!isEditEnabled && viewProfileSectionBean.getId().equals("location_info") && viewProfileSectionBean.getViewProfileFieldList().size() == 0) {
//                        Toast.makeText(mContext, R.string.err_msg_for_contact_details, Toast.LENGTH_LONG).show();
//                    }


                });
                holder.imgSection.setOnClickListener(v -> {
                    if(!isEditEnabled && viewProfileSectionBean.getId().equals("contact_info")){
                        if(viewProfileSectionBean.isContactVisible){
                            mSectionStateChangeListener.onSectionStateChanged(viewProfileSectionBean, !viewProfileSectionBean.isExpanded);
                        }else{
                            mItemClickListener.viewContact(viewProfileSectionBean);
                        }
                    } else if(position>0){
                        mSectionStateChangeListener.onSectionStateChanged(viewProfileSectionBean, !viewProfileSectionBean.isExpanded);
                    }
                });

                if (isEditEnabled) {
                    holder.btnEdit.setVisibility(View.VISIBLE);
                    holder.btnEdit.setOnClickListener(v -> {
                        mItemClickListener.itemClicked(viewProfileSectionBean);
                    });
                } else {
                    holder.btnEdit.setVisibility(View.GONE);
                }

                if (viewProfileSectionBean.getId() != null && viewProfileSectionBean.getId().length() > 0) {
                    try {
                        holder.imgSection.setImageDrawable(res.getDrawable(res.getIdentifier(viewProfileSectionBean.getId().toLowerCase(), "drawable", mContext.getPackageName())));
                    } catch (Exception e) {
                        holder.imgSection.setImageResource(R.drawable.basic_info);
                    }
                } else {
                    holder.imgSection.setImageResource(R.drawable.basic_info);
                }
                break;
        }
    }

    private String checkField(String val) {
        if (val.equals("") || val.equals("null")) {
            return "N/A";
        }
        return val;
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        //common
        View view;
        int viewType;

        //for section
        TextView lblSection;
        ImageView btnEdit, imgSection;
        View bottomDivider;

        //for item
        TextView lblName, lblValue;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                lblName = view.findViewById(R.id.lblName);
                lblValue = view.findViewById(R.id.lblValue);
                bottomDivider = view.findViewById(R.id.bottomDivider);
            } else {
                lblSection = view.findViewById(R.id.lblSection);
                btnEdit = view.findViewById(R.id.btnEdit);
                imgSection = view.findViewById(R.id.imgSection);
            }
        }
    }
}
