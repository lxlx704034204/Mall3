package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.Brand;
import com.hxqc.mall.thirdshop.maintenance.model.BrandGroup;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author: HuJunJie
 * Date: 2015-03-23
 * FIXME
 * Todo 品牌列表
 */
public class BrandExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BrandGroup > brandArrayList;

    public BrandExpandableAdapter(Context context, ArrayList<BrandGroup> brandArrayList) {
        this.brandArrayList = brandArrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyData(ArrayList<BrandGroup> brandArrayList) {
        this.brandArrayList = brandArrayList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return brandArrayList == null ? 0 : brandArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).group.size();
    }

    @Override
    public BrandGroup getGroup(int groupPosition) {
        return brandArrayList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_brandexpandable_group, parent, false);
        }
        convertView.setClickable(true);
        TextView textView = (TextView) convertView.findViewById(R.id.auto_brand_group_name);
        textView.setText(getGroup(groupPosition).groupTag);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_brandexpandable_child, parent, false);
            ImageView mBrandLogoView = (ImageView) convertView.findViewById(R.id.item_auto_logo);
            TextView mBrandNameView = (TextView) convertView.findViewById(R.id.item_auto_name);
            childHolder = new ChildHolder(mBrandLogoView, mBrandNameView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.setValue(getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public Brand getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).group.get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = layoutInflater.inflate(R.layout.item_brandexpandable_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(true);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int groupPosition) {
        if (groupPosition == -1) return;
        TextView textView = (TextView) headerView.findViewById(R.id.auto_brand_group_name);
        textView.setText(getGroup(groupPosition).groupTag);
    }

    class ChildHolder {
        TextView mBrandNameView;
        ImageView mBrandLogoView;

        ChildHolder(ImageView mBrandLogoView, TextView mBrandNameView) {
            this.mBrandLogoView = mBrandLogoView;
            this.mBrandNameView = mBrandNameView;
        }

        public void setValue(Brand autoBrand) {
            mBrandNameView.setText(autoBrand.brandName);
            ImageUtil.setImage(context, mBrandLogoView, autoBrand.brandThumb);
        }

    }
}
