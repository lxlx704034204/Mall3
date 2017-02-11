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

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.CityGroup;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * 说明:城市筛选适配器
 *
 * @author: 吕飞
 * @since: 2015-07-13
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseCityAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    Context mContext;
    ArrayList<CityGroup> cityGroups;
    LayoutInflater mLayoutInflater;

    public ChooseCityAdapter(Context mContext, ArrayList<CityGroup> cityGroups) {
        this.mContext = mContext;
        this.cityGroups = cityGroups;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return cityGroups.get(groupPosition).group.size();
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
        TextView mCityHeadView = (TextView) convertView.findViewById(R.id.list_head);
        mCityHeadView.setText(getGroup(groupPosition).groupTag);
        return convertView;
    }

    @Override
    public CityGroup getGroup(int groupPosition) {
        return cityGroups.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChooseCityHolder mChooseCityHolder;
        String mCity = getChild(groupPosition, childPosition);
        if (convertView == null) {
            mChooseCityHolder = new ChooseCityHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_choose_city_child, parent, false);
            mChooseCityHolder.mCityNameView = (TextView) convertView.findViewById(R.id.city_name);
            mChooseCityHolder.mLocView = (ImageView) convertView.findViewById(R.id.loc_icon);
            mChooseCityHolder.mLineView = convertView.findViewById(R.id.line);
            convertView.setTag(mChooseCityHolder);
        } else {
            mChooseCityHolder = (ChooseCityHolder) convertView.getTag();
        }
        mChooseCityHolder.mCityNameView.setText(mCity);
        if (groupPosition == 0) {
            mChooseCityHolder.mLocView.setVisibility(View.VISIBLE);
        } else {
            mChooseCityHolder.mLocView.setVisibility(View.GONE);
        }
        if (childPosition == cityGroups.get(groupPosition).group.size() - 1) {
            mChooseCityHolder.mLineView.setVisibility(View.GONE);
        } else {
            mChooseCityHolder.mLineView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return cityGroups.get(groupPosition).group.get(childPosition).city_name;
    }

    @Override
    public int getGroupCount() {
        return cityGroups.size();
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
        TextView mCityHeadView = (TextView) headerView.findViewById(R.id.list_head);
        mCityHeadView.setText(getGroup(firstVisibleGroupPos).groupTag);
    }

    class ChooseCityHolder {
        TextView mCityNameView;
        ImageView mLocView;
        View mLineView;
    }
}

