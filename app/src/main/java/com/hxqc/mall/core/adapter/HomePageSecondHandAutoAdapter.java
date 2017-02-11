package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.UsedAutoForHome;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:首页二手车列表
 *
 * author: 吕飞
 * since: 2015-06-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class HomePageSecondHandAutoAdapter extends BaseAdapter {
    ArrayList<UsedAutoForHome > mUsedAutos;
    Context mContext;
    LayoutInflater mInflater;

    public HomePageSecondHandAutoAdapter(ArrayList<UsedAutoForHome> mUsedAutos, Context context) {
        this.mUsedAutos = mUsedAutos;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mUsedAutos == null ? 0 : mUsedAutos.size();
    }

    @Override
    public UsedAutoForHome getItem(int position) {
        return mUsedAutos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UsedAutoHolder mUsedAutoHolder;
        UsedAutoForHome mUsedAuto = mUsedAutos.get(position);
        if (convertView == null) {
            mUsedAutoHolder = new UsedAutoHolder();
            convertView = mInflater.inflate(R.layout.item_home_page_second_hand_auto, parent, false);
            mUsedAutoHolder.mAutoModelView = (TextView) convertView.findViewById(R.id.auto_model);
            mUsedAutoHolder.mAutoPriceView = (TextView) convertView.findViewById(R.id.auto_price);
            mUsedAutoHolder.mAutoImageView = (ImageView) convertView.findViewById(R.id.auto_image);
            mUsedAutoHolder.mFirstShowTimeView = (TextView) convertView.findViewById(R.id.first_show_time);
            mUsedAutoHolder.mMileageView = (TextView) convertView.findViewById(R.id.mileage);
            convertView.setTag(mUsedAutoHolder);
        } else {
            mUsedAutoHolder = (UsedAutoHolder) convertView.getTag();
        }
        mUsedAutoHolder.mAutoModelView.setText(mUsedAuto.itemName);
        mUsedAutoHolder.mAutoPriceView.setText(mUsedAuto.getItemPrice());
        mUsedAutoHolder.mFirstShowTimeView.setText(mUsedAuto.getFirstShowTime());
        mUsedAutoHolder.mMileageView.setText(mUsedAuto.getItemMileage());
        ImageUtil.setImage(mContext, mUsedAutoHolder.mAutoImageView, mUsedAuto.itemThumb);
        return convertView;
    }

    public class UsedAutoHolder {
        TextView mAutoModelView;//车型
        TextView mAutoPriceView;//价格
        ImageView mAutoImageView;//车图像
        TextView mFirstShowTimeView;//上牌时间
        TextView mMileageView;//里程数
    }
}
