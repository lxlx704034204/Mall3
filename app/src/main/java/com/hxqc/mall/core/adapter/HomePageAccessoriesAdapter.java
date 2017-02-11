package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.AccessoryForHome;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:首页用品列表
 *
 * author: 吕飞
 * since: 2015-06-18
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class HomePageAccessoriesAdapter extends BaseAdapter {
    ArrayList<AccessoryForHome > mAccessories;
    Context mContext;
    LayoutInflater mInflater;

    public HomePageAccessoriesAdapter(ArrayList<AccessoryForHome> mAccessories, Context context) {
        this.mAccessories = mAccessories;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mAccessories == null ? 0 : mAccessories.size();
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
        AccessoryHolder mAccessoryHolder;
        AccessoryForHome mAccessory = mAccessories.get(position);
        if (convertView == null) {
            mAccessoryHolder = new AccessoryHolder();
            convertView = mInflater.inflate(R.layout.item_home_page_accessories, parent, false);
            mAccessoryHolder.mAccessoryNameView = (TextView) convertView.findViewById(R.id.accessory_name);
            mAccessoryHolder.mAccessoryDescriptionView = (TextView) convertView.findViewById(R.id.accessory_description);
            mAccessoryHolder.mAccessoryImageView = (ImageView) convertView.findViewById(R.id.accessory_image);
            convertView.setTag(mAccessoryHolder);
        } else {
            mAccessoryHolder = (AccessoryHolder) convertView.getTag();
        }
        mAccessoryHolder.mAccessoryNameView.setText(mAccessory.title);
        mAccessoryHolder.mAccessoryDescriptionView.setText(mAccessory.subtitle);
        ImageUtil.setImage(mContext, mAccessoryHolder.mAccessoryImageView, mAccessory.itemPic);
        return convertView;
    }

    public class AccessoryHolder {
        TextView mAccessoryNameView;//名字
        TextView mAccessoryDescriptionView;//描述
        ImageView mAccessoryImageView;//图像
    }
}
