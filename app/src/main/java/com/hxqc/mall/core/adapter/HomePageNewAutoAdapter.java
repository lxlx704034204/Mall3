package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.NewAutoForHome;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:首页新车列表
 *
 * author: 吕飞
 * since: 2015-06-18
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class HomePageNewAutoAdapter extends BaseAdapter {
    ArrayList<NewAutoForHome > mNewAutos;
    Context mContext;
    LayoutInflater mInflater;

    public HomePageNewAutoAdapter(ArrayList<NewAutoForHome> mNewAutos, Context context) {
        this.mNewAutos = mNewAutos;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mNewAutos == null ? 0 : mNewAutos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewAutoHolder mNewAutoHolder;
        NewAutoForHome mNewAuto = mNewAutos.get(position);
        if (convertView == null) {
            mNewAutoHolder = new NewAutoHolder();
            convertView = mInflater.inflate(R.layout.item_wish_list, parent, false);
            mNewAutoHolder.mAutoModelView = (TextView) convertView.findViewById(R.id.auto_model);
            mNewAutoHolder.mAutoPriceView = (TextView) convertView.findViewById(R.id.auto_price);
            mNewAutoHolder.mCornerView = (ImageView) convertView.findViewById(R.id.corner);
            mNewAutoHolder.mAutoImageView = (ImageView) convertView.findViewById(R.id.auto_image);
            convertView.setTag(mNewAutoHolder);
        } else {
            mNewAutoHolder = (NewAutoHolder) convertView.getTag();
        }
        mNewAutoHolder.mAutoModelView.setText(mNewAuto.itemName);
        mNewAutoHolder.mAutoPriceView.setText(mNewAuto.getItemPrice());
        mNewAutoHolder.mCornerView.setVisibility(View.VISIBLE);
        ImageUtil.setImage(mContext, mNewAutoHolder.mAutoImageView, mNewAuto.itemThumb);
        return convertView;
    }

    public class NewAutoHolder {
        TextView mAutoModelView;//车型
        TextView mAutoPriceView;//价格
        ImageView mAutoImageView;//车图像
        ImageView mCornerView;//角标
    }
}
