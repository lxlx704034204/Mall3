package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryBigCategory;
import com.hxqc.mall.thirdshop.accessory.model.AccessorySmallCategory;
import com.hxqc.mall.thirdshop.accessory.model.ProductFitList;
import com.hxqc.mall.thirdshop.accessory.model.Series;
import com.hxqc.mall.thirdshop.accessory4s.model.ShopList;

import java.util.ArrayList;

/**
 * 用品销售 筛选条 品牌 Right, 品类 Left Right, 选车型Fragment, 共用adapter
 * Created by huangyi on 16/5/30.
 */
public class CustomConditionAdapter extends BaseAdapter {

    Type mType;
    ArrayList mList;
    LayoutInflater mLayoutInflater;

    public CustomConditionAdapter(Context mContext, Type mType, ArrayList mList) {
        this.mType = mType;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_custom_brand_expandable, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mParentView = (LinearLayout) convertView.findViewById(R.id.parent);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mValueView = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (Type.SERIES == mType) {
            Series model = (Series) mList.get(position);
            if (model.isChecked) {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#dcddde"));
            } else {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#e9eaeb"));
            }
            mViewHolder.mNameView.setText(model.seriesName);

        } else if (Type.ACCESSORY_BIG_CATEGORY == mType) {
            AccessoryBigCategory model = (AccessoryBigCategory) mList.get(position);
            if (model.isChecked) {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#e9eaeb"));
                mViewHolder.mNameView.setTextColor(Color.parseColor("#ff7043"));
            } else {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mViewHolder.mNameView.setTextColor(Color.parseColor("#de000000"));
            }
            mViewHolder.mNameView.setText(model.class1stName);

        } else if (Type.ACCESSORY_SMALL_CATEGORY == mType) {
            AccessorySmallCategory model = (AccessorySmallCategory) mList.get(position);
            if (model.isChecked) {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#dcddde"));
            } else {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#e9eaeb"));
            }
            mViewHolder.mNameView.setText(model.class2ndName);

        } else if (Type.SHOP_LIST == mType) {
            ShopList model = (ShopList) mList.get(position);
            if (model.isChecked) {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#e9eaeb"));
                mViewHolder.mNameView.setTextColor(Color.parseColor("#ff7043"));
            } else {
                mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mViewHolder.mNameView.setTextColor(Color.parseColor("#de000000"));
            }
            mViewHolder.mNameView.setText(model.shopTitle);
            mViewHolder.mValueView.setVisibility(View.VISIBLE);
            mViewHolder.mValueView.setText(model.distance + "km");

        } else if (Type.PRODUCT_FIT_LIST == mType) {
            ProductFitList model = (ProductFitList) mList.get(position);
            mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            mViewHolder.mNameView.setText(model.fitDescription);
        }

        return convertView;
    }

    public enum Type {
        SERIES,
        ACCESSORY_BIG_CATEGORY,
        ACCESSORY_SMALL_CATEGORY,
        SHOP_LIST,
        PRODUCT_FIT_LIST
    }

    class ViewHolder {
        LinearLayout mParentView;
        TextView mNameView;
        TextView mValueView;
    }

}
