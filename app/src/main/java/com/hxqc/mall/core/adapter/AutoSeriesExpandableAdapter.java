package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.AutoSeriesGroup;
import com.hxqc.mall.core.model.Series;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-03-13
 * FIXME
 * Todo 整车--》车系Adapter PinnedHeaderExpandableListView
 */
public class AutoSeriesExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView
        .OnHeaderUpdateListener {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList< AutoSeriesGroup > autoSeriesGroup;

    public AutoSeriesExpandableAdapter(Context context, ArrayList< AutoSeriesGroup > autoSeriesGroup) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.autoSeriesGroup = autoSeriesGroup;
    }

    public void notifyData(ArrayList< AutoSeriesGroup > autoSeriesGroup) {
        this.autoSeriesGroup = autoSeriesGroup;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return autoSeriesGroup == null ? 0 : autoSeriesGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return autoSeriesGroup == null ? 0 : (
                autoSeriesGroup.get(groupPosition) == null ? 0 : autoSeriesGroup.get(groupPosition).group.size());
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
            convertView = layoutInflater.inflate(R.layout.item_main_autoseries_group, parent, false);
        convertView.setClickable(true);
        TextView textView = (TextView) convertView.findViewById(R.id.auto_series_group_name);
        textView.setText(getGroup(groupPosition).groupTag);
        return convertView;
    }

    @Override
    public AutoSeriesGroup getGroup(int groupPosition) {
        return autoSeriesGroup.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.item_main_autoseries_child, null);
            ImageView mSeriesLogoView = (ImageView) convertView
                    .findViewById(R.id.auto_series_logo);
            TextView mSeriesNameView = (TextView) convertView
                    .findViewById(R.id.auto_series_name);
            TextView mPriceRangeView = (TextView) convertView
                    .findViewById(R.id.auto_series_price);
            TextView mBatteryLifeView = (TextView) convertView.findViewById(R.id.auto_series_battery_life);

            childHolder = new ChildHolder(mSeriesLogoView, mSeriesNameView,
                    mPriceRangeView, mBatteryLifeView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.setValue(getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public Series getChild(int groupPosition, int childPosition) {
        return autoSeriesGroup.get(groupPosition).group.get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = layoutInflater.inflate(R.layout.item_main_autoseries_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView
                .LayoutParams.WRAP_CONTENT));
        headerView.setClickable(true);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int groupPosition) {
        if (groupPosition == -1) return;
        TextView textView = (TextView) headerView
                .findViewById(R.id.auto_series_group_name);

        textView.setText(getGroup(groupPosition).groupTag);
    }

    class ChildHolder {
        ImageView mSeriesLogoView;
        TextView mSeriesNameView;
        TextView mPriceRangeView;
        TextView mBatteryLifeView;


        public ChildHolder(ImageView mSeriesLogoView, TextView mSeriesNameView, TextView mPriceRangeView, TextView
                mBatteryLifeView) {
            this.mSeriesLogoView = mSeriesLogoView;
            this.mSeriesNameView = mSeriesNameView;
            this.mPriceRangeView = mPriceRangeView;
            this.mBatteryLifeView = mBatteryLifeView;
        }

        public void setValue(Series autoAutoSeries) {
            mPriceRangeView.setText(autoAutoSeries.getPriceRange());
            mSeriesNameView.setText(String.format("%s系列车型", autoAutoSeries.getSeriesName()));
            if (TextUtils.isEmpty(autoAutoSeries.getSeriesThumb())) {
                mSeriesLogoView.setImageResource(R.drawable.pic_normal);
            } else {
                ImageUtil.setImage(context, mSeriesLogoView, autoAutoSeries.getSeriesThumb());
            }
            if (!TextUtils.isEmpty(autoAutoSeries.batteryLife)) {
                mBatteryLifeView.setVisibility(View.VISIBLE);
                mBatteryLifeView.setText(String.format("纯电续航：%s公里", autoAutoSeries.batteryLife));
            } else {
                mBatteryLifeView.setVisibility(View.GONE);
            }
        }

    }
}
