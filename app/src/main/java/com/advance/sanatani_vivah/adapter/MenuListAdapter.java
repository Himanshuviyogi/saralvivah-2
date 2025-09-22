package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.advance.sanatani_vivah.model.MenuChildBean;
import com.advance.sanatani_vivah.model.MenuGroupBean;
import com.advance.sanatani_vivah.R;

import java.util.List;

public class MenuListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MenuGroupBean> list;
    private Resources res;

    public MenuListAdapter(Context c, List<MenuGroupBean> list) {
        this.context = c;
        this.list = list;
        this.res = context.getResources();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getDrawerChildList().size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getDrawerChildList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        GroupViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new GroupViewHolder();

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nav_list_header, null);

            viewHolder.img_plus = convertView.findViewById(R.id.img_plus);
            viewHolder.img_item = convertView.findViewById(R.id.img_item);
            viewHolder.tv_header = convertView.findViewById(R.id.tv_header);
            viewHolder.constraintLayout = convertView.findViewById(R.id.constraintLayout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        MenuGroupBean menuGroupBean = list.get(groupPosition);

        if (menuGroupBean.isExpandable == 1) {
            viewHolder.img_plus.setVisibility(View.VISIBLE);
            if (isExpanded) {
                viewHolder.img_plus.setImageResource(R.drawable.ic_collapse_drawer);
            } else {
                viewHolder.img_plus.setImageResource(R.drawable.ic_expand_drawer);
            }
        } else {
            viewHolder.img_plus.setVisibility(View.GONE);
        }

        if (menuGroupBean.getMenuImg() != null && menuGroupBean.getMenuImg().length() > 0) {
            try {
                viewHolder.img_item.setImageDrawable(res.getDrawable(res.getIdentifier(menuGroupBean.getMenuImg(), "drawable", context.getPackageName())));
            }catch (Exception e) {
                viewHolder.img_item.setImageResource(R.drawable.ic_profile_new);
            }
        } else {
            viewHolder.img_item.setImageResource(R.drawable.ic_profile_new);
        }

        if(groupPosition % 2 != 0) {
            viewHolder.constraintLayout.setBackgroundResource(R.color.color_even_drawer_section);
        }else{
            viewHolder.constraintLayout.setBackgroundResource(R.color.white);
        }

        viewHolder.tv_header.setText(menuGroupBean.getMenuTitle());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        ChildViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ChildViewHolder();

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nav_list_child, null);

            viewHolder.tv_child = convertView.findViewById(R.id.tv_child);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        MenuChildBean menuChildBean = list.get(groupPosition).getDrawerChildList().get(childPosition);

        viewHolder.tv_child.setText(menuChildBean.getSubMenuTitle());

        return convertView;
    }

    static class GroupViewHolder {
        TextView tv_header;
        ImageView img_item, img_plus;
        ConstraintLayout constraintLayout;
    }

    static class ChildViewHolder {
        TextView tv_child;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
