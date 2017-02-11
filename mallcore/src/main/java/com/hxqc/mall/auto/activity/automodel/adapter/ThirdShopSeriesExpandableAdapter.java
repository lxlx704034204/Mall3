package com.hxqc.mall.auto.activity.automodel.adapter;

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
import com.hxqc.mall.core.R;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author: 胡仲俊
 * Date: 2016-04-20
 * FIXME
 * Todo 车系列表
 */
public class ThirdShopSeriesExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    private static final String TAG = "ThirdShopSeriesExpandableAdapter";
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<SeriesGroup> seriesArrayList;

    public ThirdShopSeriesExpandableAdapter(Context context, ArrayList<SeriesGroup> seriesArrayList) {
        this.seriesArrayList = seriesArrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyData(ArrayList<SeriesGroup> seriesArrayList) {
        this.seriesArrayList = seriesArrayList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return seriesArrayList == null ? 0 : seriesArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return seriesArrayList == null ? 0 : (
                seriesArrayList.get(groupPosition) == null ? 0 : seriesArrayList.get(groupPosition).series.size());
    }

    @Override
    public SeriesGroup getGroup(int groupPosition) {
        return seriesArrayList.get(groupPosition);
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
        textView.setText(getGroup(groupPosition).brandName);
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
    public Series getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).series.get(childPosition);
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
        textView.setText(getGroup(groupPosition).brandName);
    }

    class ChildHolder {
        TextView mBrandNameView;
        ImageView mBrandLogoView;

        ChildHolder(ImageView mBrandLogoView, TextView mBrandNameView) {
            this.mBrandLogoView = mBrandLogoView;
            this.mBrandNameView = mBrandNameView;
        }

        public void setValue(Series autoSeries) {
            mBrandNameView.setText(autoSeries.seriesName);
            //缺少失败的图片和预加载图片
            mBrandLogoView.setVisibility(View.GONE);
/*            if (!TextUtils.isEmpty(autoSeries.seriesThumb)) {
                Picasso.with(context).load(autoSeries.seriesThumb).placeholder(R.drawable.pic_normal).error(R.drawable.pic_normal).into(mBrandLogoView);
            } else {
                mBrandLogoView.setImageResource(R.drawable.pic_normal);
            }*/
        }

    }
}
