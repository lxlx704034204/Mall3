package com.hxqc.mall.usedcar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.fragment.ChooseFragment;
import com.hxqc.mall.usedcar.model.Brand;
import com.hxqc.mall.usedcar.model.BrandGroup;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * 说明:用品筛选适配器
 *
 * @author: 吕飞
 * @since: 2015-07-13
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseBrandAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    Context mContext;
    ArrayList<BrandGroup> brandGroups;
    LayoutInflater mLayoutInflater;
    int mType;

    public ChooseBrandAdapter(Context mContext, ArrayList<BrandGroup> brandGroups, int type) {
        this.mContext = mContext;
        this.brandGroups = brandGroups;
        mType = type;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return brandGroups.get(groupPosition).group.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.item_choose_group, parent, false);
        TextView mBrandHeadView = (TextView) convertView.findViewById(R.id.list_head);
        mBrandHeadView.setText(getGroup(groupPosition).groupTag);
        return convertView;
    }

    @Override
    public BrandGroup getGroup(int groupPosition) {
        return brandGroups.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChooseBrandHolder mChooseBrandHolder;
        Brand mBrand = getChild(groupPosition, childPosition);
        if (convertView == null) {
            mChooseBrandHolder = new ChooseBrandHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_choose_brand_child, parent, false);
            mChooseBrandHolder.mBrandNameView = (TextView) convertView.findViewById(R.id.brand_name);
            mChooseBrandHolder.mBrandIconView = (ImageView) convertView.findViewById(R.id.brand_icon);
            mChooseBrandHolder.mLine = convertView.findViewById(R.id.line);
            convertView.setTag(mChooseBrandHolder);
        } else {
            mChooseBrandHolder = (ChooseBrandHolder) convertView.getTag();
        }
        mChooseBrandHolder.mBrandNameView.setText(mBrand.brand_name);
        OtherUtil.setVisible(mChooseBrandHolder.mBrandIconView, mType != ChooseFragment.VALUATION);

        ImageUtil.setImage(mContext, mChooseBrandHolder.mBrandIconView, mBrand.brand_logurl);
        if (isLastChild) {
            mChooseBrandHolder.mLine.setVisibility(View.GONE);
        } else {
            mChooseBrandHolder.mLine.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public Brand getChild(int groupPosition, int childPosition) {
        return brandGroups.get(groupPosition).group.get(childPosition);
    }

    @Override
    public int getGroupCount() {
        return brandGroups.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        @SuppressLint("InflateParams") View headerView = mLayoutInflater.inflate(R.layout.item_choose_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(false);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        if (firstVisibleGroupPos == -1) return;
        TextView mBrandHeadView = (TextView) headerView.findViewById(R.id.list_head);
        mBrandHeadView.setText(getGroup(firstVisibleGroupPos).groupTag);
    }

    class ChooseBrandHolder {
        TextView mBrandNameView;
        ImageView mBrandIconView;
        View mLine;
    }
}

