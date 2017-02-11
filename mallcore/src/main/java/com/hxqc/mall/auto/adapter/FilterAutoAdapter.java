package com.hxqc.mall.auto.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.hxqc.mall.auto.model.FilterGroup;
import com.hxqc.mall.core.views.FilterFactorView;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author: HuJunJie
 * Date: 2015-03-26
 * FIXME
 * Todo 找车Adapter
 */
public class FilterAutoAdapter extends BaseAdapter {

    ArrayList< FilterGroup > mFilterGroups;
    Context context;

    public FilterAutoAdapter(Context context, ArrayList< FilterGroup > mFilterGroups) {
        this.mFilterGroups = mFilterGroups;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mFilterGroups.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = LayoutInflater.from(context).inflate(R.layout.item_filter_factor, parent,false);
//        TextView mLabelView = (TextView) convertView.findViewById(R.id.filter_factor_label);
//        TextView mTagView = (TextView) convertView.findViewById(R.id.filter_factor_tag);
//        FilterGroup filterGroup = getItem(position);
//        mLabelView.setText(filterGroup.filterLabel);
//        if (filterGroup.getDefaultFilter() != null) {
//            mTagView.setText(filterGroup.getDefaultFilter().getLabel());
//            mTagView.setTextColor(context.getResources().getColor(R.color.cursor_orange));
//        } else {
//            mTagView.setText("不限");
//            mTagView.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
//        }
        if (convertView == null) {
            convertView = new FilterFactorView(context);
        }
        FilterGroup filterGroup = getItem(position);
        FilterFactorView filterFactorView = (FilterFactorView) convertView;
        filterFactorView.setLabelString(filterGroup.filterLabel);
        if (filterGroup.getDefaultFilter() != null) {
            filterFactorView.setTagString(filterGroup.getDefaultFilter().getLabel(),true);
            DebugLog.d("FilterAutoAdapter",filterGroup.getDefaultFilter().getLabel());
        } else {
            filterFactorView.setTagString("不限",false);
        }
        return convertView;
    }

    @Override
    public FilterGroup getItem(int position) {
        return mFilterGroups.get(position);
    }
}
