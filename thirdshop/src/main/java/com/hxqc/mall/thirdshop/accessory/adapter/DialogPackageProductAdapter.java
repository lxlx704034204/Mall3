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
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProduct;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProducts;

/**
 * 优惠套餐列表Dialog弹窗ListView Adapter
 * Created by huangyi on 15/12/18.
 */
public class DialogPackageProductAdapter extends BaseAdapter {

    Context mContext;
    SinglePackageProducts mProducts;
    LayoutInflater mLayoutInflater;

    public DialogPackageProductAdapter(Context mContext, SinglePackageProducts mProducts) {
        this.mContext = mContext;
        this.mProducts = mProducts;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mProducts.products.size();
    }

    @Override
    public SinglePackageProduct getItem(int position) {
        return mProducts.products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_dialog_package_product, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mStatusView = (ImageView) convertView.findViewById(R.id.dialog_package_product_status);
            mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.dialog_package_product_photo);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.dialog_package_product_name);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.dialog_package_product_price);
            mViewHolder.mSpecificationView = (TextView) convertView.findViewById(R.id.dialog_package_product_specification);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        SinglePackageProduct model = mProducts.products.get(position);

        if (model.isTempChecked) {
            mViewHolder.mStatusView.setImageResource(R.drawable.ic_package_product_selected);
        } else {
            mViewHolder.mStatusView.setImageResource(R.drawable.ic_package_product_normal);
        }

        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.productInfo.smallPhoto);

        mViewHolder.mNameView.setText(model.productInfo.name);
        mViewHolder.mPriceView.setText(OtherUtil.amountFormat(model.productInfo.price, true));
        mViewHolder.mSpecificationView.setText(model.title);
        return convertView;
    }

    class ViewHolder {
        ImageView mStatusView;
        ImageView mPhotoView;
        TextView mNameView;
        TextView mPriceView;
        TextView mSpecificationView;
    }
}
