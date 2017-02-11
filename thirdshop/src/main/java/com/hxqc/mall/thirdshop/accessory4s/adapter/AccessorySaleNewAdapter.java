package com.hxqc.mall.thirdshop.accessory4s.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductList4S;

import java.util.ArrayList;

/**
 * 用品销售 用品列表 ListView adapter
 * Created by huangyi on 16/3/28.
 */
public class AccessorySaleNewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ProductList4S> mProductList;
    boolean hasTitle;
    LayoutInflater mLayoutInflater;

    public AccessorySaleNewAdapter(Context mContext, ArrayList<ProductList4S> mProductList, boolean hasTitle) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.hasTitle = hasTitle;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == mProductList ? 0 : mProductList.size();
    }

    @Override
    public ProductList4S getItem(int position) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_accessory_sale_new, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.accessory_sale_photo);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.accessory_sale_name);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.accessory_sale_price);
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.accessory_sale_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ProductList4S model = mProductList.get(position);

        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.smallPhoto);
        mViewHolder.mNameView.setText(model.name);
        mViewHolder.mPriceView.setText(model.price);
        if (hasTitle && !TextUtils.isEmpty(model.shopTitle)) {
            mViewHolder.mTitleView.setVisibility(View.VISIBLE);
            mViewHolder.mTitleView.setText(model.shopTitle);
        } else {
            mViewHolder.mTitleView.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mPhotoView;
        TextView mNameView;
        TextView mPriceView;
        TextView mTitleView;
    }

}
