package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.ModuleForHome;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:首页顶部菜单
 *
 * author: 吕飞
 * since: 2015-06-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class HomePageTopAdapter extends BaseAdapter {
    ArrayList<ModuleForHome> mModules;
    Context mContext;
    LayoutInflater mInflater;

    public HomePageTopAdapter(ArrayList<ModuleForHome> mModules, Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mModules = mModules;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return mModules == null ? 0 : mModules.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModuleHolder mModuleHolder;
        ModuleForHome mModule = getItem(position);
        if (convertView == null) {
            mModuleHolder = new ModuleHolder();
            convertView = mInflater.inflate(R.layout.item_home_page_top, parent, false);
            mModuleHolder.mTopIconView = (ImageView) convertView.findViewById(R.id.top_icon);
            mModuleHolder.mTopTextView = (TextView) convertView.findViewById(R.id.top_text);
            convertView.setTag(mModuleHolder);
        } else {
            mModuleHolder = (ModuleHolder) convertView.getTag();
        }
        mModuleHolder.mTopTextView.setText(mModule.moduleTitle);
        // TODO: 2015/6/23  
//        int[] img={R.drawable.ic_home_nav_newcar,R.drawable.ic_home_nav_sales,R.drawable.ic_home_nav_usedcars,R.drawable.ic_home_nav_insurance,R.drawable.ic_home_nav_finance,R.drawable.ic_home_nav_accessory};
//        mModuleHolder.mTopIconView.setImageResource(img[position]);
        ImageUtil.setImage(mContext, mModuleHolder.mTopIconView, mModule.moduleThumb);
        return convertView;
    }

    @Override
    public ModuleForHome getItem(int position) {
        return mModules.get(position);
    }

    public class ModuleHolder {
        ImageView mTopIconView;//图标
        TextView mTopTextView;//文字描述
    }
}
