package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hxqc.mall.core.model.PromotionForHome;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:首页活动列表
 *
 * author: 吕飞
 * since: 2015-06-25
 * Copyright:恒信汽车电子商务有限公司
 */
public class HomePagePromotionAdapter extends BaseAdapter {
    ArrayList<PromotionForHome > mPromotions;
    Context mContext;
    LayoutInflater mInflater;

    public HomePagePromotionAdapter(ArrayList<PromotionForHome> mPromotions, Context context) {
        this.mPromotions = mPromotions;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mPromotions == null ? 0 : mPromotions.size();
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
        PromotionHolder mPromotionHolder;
        PromotionForHome mPromotion = mPromotions.get(position);
        if (convertView == null) {
            mPromotionHolder = new PromotionHolder();
            convertView = mInflater.inflate(R.layout.item_home_page_promotion, parent, false);
            mPromotionHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(mPromotionHolder);
        } else {
            mPromotionHolder = (PromotionHolder) convertView.getTag();
        }
        ImageUtil.setImage(mContext, mPromotionHolder.mImageView, mPromotion.itemPic);
        return convertView;
    }

    public class PromotionHolder {
        ImageView mImageView;//图像
    }
}
