package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.auto.model.Series;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.widget.PinnedHeaderExpandableListView.OnHeaderUpdateListener;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-17
 * FIXME
 * Todo 车系列表的适配器
 */
public class AutoSeriesListAdapter extends BaseExpandableListAdapter implements OnHeaderUpdateListener {
    private ArrayList<SeriesGroup> mData;
    private Context mContext;

    public AutoSeriesListAdapter(ArrayList<SeriesGroup> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public void notifyData(ArrayList<SeriesGroup> mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).series.size();
    }

    @Override
    public SeriesGroup getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Series getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).series.get(childPosition);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_car_group, parent, false);
        convertView.setClickable(true);
        TextView textView = (TextView) convertView.findViewById(R.id.select_car_model_group);
        textView.setText(getGroup(groupPosition).brandName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_car_child, parent, false);
            TextView mModelNameView = (TextView) convertView.findViewById(R.id.select_car_model_child);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
            imageView.setVisibility(View.GONE);
            childHolder = new ChildHolder(mModelNameView, imageView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.setValue(getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.item_select_car_model_group, null);
        headerView.setLayoutParams
                (new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(true);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        if (firstVisibleGroupPos == -1) return;
        TextView textView = (TextView) headerView.findViewById(R.id.select_car_model_group);
        textView.setText(getGroup(firstVisibleGroupPos).brandName);
    }

    class ChildHolder {

        TextView nameView;
        ImageView imageView;

        ChildHolder(TextView mModelNameView, ImageView imageView) {
            this.nameView = mModelNameView;
            this.imageView = imageView;
        }

        public void setValue(Series series) {
            nameView.setText(series.seriesName);
            ImageUtil.setImageSquare(mContext, imageView, series.seriesThumb);
        }
    }
}
