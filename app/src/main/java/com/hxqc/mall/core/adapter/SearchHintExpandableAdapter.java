package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.model.Series;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie Date: 2015-03-31
 * FIXME Todo 搜索提示
 */
public class SearchHintExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList< Brand > brands;

    public SearchHintExpandableAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public SearchHintExpandableAdapter(Context context, ArrayList< Brand > brands) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.brands = brands;
    }

    public void notifyData(ArrayList< Brand > brands) {
        this.brands = brands;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return brands == null ? 0 : brands.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return brands == null ? 0 : brands.get(groupPosition).seriesItem.size();
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
        TextView mTextView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_searchhint_group, parent,false);
            mTextView = (TextView) convertView.findViewById(R.id.search_hint_group_label);
            convertView.setTag(mTextView);
        } else {
            mTextView = (TextView) convertView.getTag();
        }
        convertView.setClickable(true);
        mTextView.setText(getGroup(groupPosition).brandName);
        return convertView;
    }

    @Override
    public Brand getGroup(int groupPosition) {
        return brands.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_searchhint_child,parent,false);
        }

        TextView mLabelView = (TextView) convertView.findViewById(R.id.search_hint_child_label);
        Series mAutoSeries = getChild(groupPosition, childPosition);
        mLabelView.setText(mAutoSeries.getSeriesName());
        convertView.setTag(mAutoSeries);
        return convertView;
    }

    @Override
    public Series getChild(int groupPosition, int childPosition) {
        return brands.get(groupPosition).seriesItem.get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = layoutInflater.inflate(R.layout.item_searchhint_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(true);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int groupPosition) {
        if (groupPosition == -1) return;
        TextView mTextView = (TextView) headerView.findViewById(R.id.search_hint_group_label);
        mTextView.setText(getGroup(groupPosition).brandName);
    }

}
