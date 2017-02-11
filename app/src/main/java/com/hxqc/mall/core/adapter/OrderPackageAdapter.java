package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.widget.ListViewNoSlide;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:订单详情中的套餐
 * <p/>
 * author: 吕飞
 * since: 2015-06-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderPackageAdapter extends BaseAdapter {
    ArrayList<AutoPackage> mPackages;
    Context mContext;
    LayoutInflater mInflater;

    public OrderPackageAdapter(ArrayList<AutoPackage> mPackages, Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mPackages = mPackages;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return mPackages == null ? 0 : mPackages.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PackageHolder mPackageHolder;
        final AutoPackage mPackage = getItem(position);
        if (convertView == null) {
            mPackageHolder = new PackageHolder();
            convertView = mInflater.inflate(R.layout.item_package, parent, false);
            mPackageHolder.mPackageTitleView = (TextView) convertView.findViewById(R.id.package_title);
            mPackageHolder.mStatusView = (ImageView) convertView.findViewById(R.id.status);
            mPackageHolder.mPackagePriceView = (TextView) convertView.findViewById(R.id.package_price);
            mPackageHolder.mAccessoryListView = (ListViewNoSlide) convertView.findViewById(R.id.accessory_list);
            convertView.setTag(mPackageHolder);
        } else {
            mPackageHolder = (PackageHolder) convertView.getTag();
        }
        mPackageHolder.mPackageTitleView.setText(mPackage.title);
        mPackageHolder.mStatusView.setVisibility(View.VISIBLE);
        switch (mPackage.status) {
            case "-10":
                mPackageHolder.mStatusView.setImageResource(R.drawable.tag_cancel);
                break;
            case "10":
                mPackageHolder.mStatusView.setImageResource(R.drawable.tag_orders);
                break;
            case "20":
                mPackageHolder.mStatusView.setImageResource(R.drawable.tag_installed);
                break;
        }
        mPackageHolder.mPackagePriceView.setText(OtherUtil.stringToMoney(mPackage.getAmount()));
        QuickAdapter<Accessory> mQuickAdapter = new QuickAdapter<Accessory>(mContext, R.layout.item_accessory, mPackage.accessory) {
            @Override
            protected void convert(BaseAdapterHelper helper, Accessory item) {
                helper.setText(R.id.accessory_title, item.title);
//                if (mPackage.packageID.equals("0")) {
                helper.setVisible(R.id.accessory_price, true);
                helper.setText(R.id.accessory_price, String.format("%s  x  %d", OtherUtil.stringToMoney(item.price),  item.count));
//                } else {
//                    helper.setVisible(R.id.accessory_price, false);
//                }
            }
        };
        mPackageHolder.mAccessoryListView.setAdapter(mQuickAdapter);
        return convertView;
    }

    @Override
    public AutoPackage getItem(int position) {
        return mPackages.get(position);
    }

    public class PackageHolder {
        TextView mPackageTitleView;
        ImageView mStatusView;
        TextView mPackagePriceView;
        ListViewNoSlide mAccessoryListView;
    }
}
