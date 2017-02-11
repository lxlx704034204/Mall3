package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.newenergy.bean.PromotionAuto;
import com.hxqc.newenergy.view.DealsView;

import java.util.ArrayList;

/**
 * 说明: 新能源汽车销售列表适配器
 * author: 何玉
 * since: 2016年3月9日
 * Copyright:恒信汽车电子商务有限公司
 */
public class EVCarDealsAdapter extends BaseAdapter {

    private Context mContext;

    public EVCarDealsAdapter() {
        super();
    }

    ArrayList< PromotionAuto > mDeealsList;
    ArrayList< DealsView > mDealsViews;

    public EVCarDealsAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList< PromotionAuto > list) {
        mDeealsList = list;
        mDealsViews = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDeealsList == null ? 0 : mDeealsList.size();
    }

    @Override
    public PromotionAuto getItem(int position) {
        return mDeealsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new DealsView(mContext);
            mDealsViews.add((DealsView) convertView);
        }
        ((DealsView) convertView).setData(mDeealsList.get(position));

        return convertView;
    }

    public void stopTimer() {
        if (mDealsViews == null) return;
        for (DealsView dealsView : mDealsViews) {
            dealsView.stopTimer();
        }
    }
}
