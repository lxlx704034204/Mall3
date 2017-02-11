package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.ProductList;

import java.util.ArrayList;

/**
 * 用品销售 用品列表 ListView adapter
 * Created by huangyi on 16/3/28.
 */
public class AccessorySaleAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ProductList> mProductList;
    LayoutInflater mLayoutInflater;

    public AccessorySaleAdapter(Context mContext, ArrayList<ProductList> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == mProductList ? 0 : mProductList.size();
    }

    @Override
    public ProductList getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_accessory_sale_hy, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.accessory_sale_photo);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.accessory_sale_name);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.accessory_sale_price);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ProductList model = mProductList.get(position);

        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.productPhoto);
        mViewHolder.mNameView.setText(model.productName);
        mViewHolder.mPriceView.setText(OtherUtil.amountFormat(model.productPrice, true));
        return convertView;
    }

    class ViewHolder {
        ImageView mPhotoView;
        TextView mNameView;
        TextView mPriceView;
    }

}
