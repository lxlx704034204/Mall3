package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.views.HotBrandChildView;

import java.util.ArrayList;

/**
 * 说明:最近浏览
 *
 * author: 吕飞
 * since: 2015-04-17
 * Copyright:恒信汽车电子商务有限公司
 */
public class HomePageBrandAdapter extends BaseAdapter {
    ArrayList< Brand > mBrands;
    Context mContext;

    public HomePageBrandAdapter(Context context) {
        this.mContext = context;
    }

    public void setBrands(ArrayList< Brand > brands) {
        this.mBrands = brands;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBrands == null ? 0 : mBrands.size() > 16 ? 16 : mBrands.size();
    }

    @Override
    public Brand getItem(int position) {
        return mBrands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new HotBrandChildView(mContext);
        }
        ((HotBrandChildView) convertView).setBrand(getItem(position));
        return convertView;
    }
}
