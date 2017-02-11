package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackage;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProduct;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProducts;

/**
 * 单个优惠套餐ListViewNoSlide Adapter
 * Created by huangyi on 15/12/17.
 */
public class SinglePackageAdapter extends BaseAdapter {

    Context mContext;
    SinglePackage mSinglePackage;
    LayoutInflater mLayoutInflater;
    PackageListAdapter.OnClickListener mOnClickListener;
    int mPosition1;

    public SinglePackageAdapter(Context mContext, SinglePackage mSinglePackage, int mPosition1, PackageListAdapter.OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mSinglePackage = mSinglePackage;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mPosition1 = mPosition1;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public int getCount() {
        return mSinglePackage.productList.size();
    }

    @Override
    public SinglePackageProducts getItem(int position) {
        return mSinglePackage.productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_single_package, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.single_package_photo);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.single_package_name);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.single_package_price);
            mViewHolder.mNumberView = (TextView) convertView.findViewById(R.id.single_package_number);
            mViewHolder.mSpecificationView = (TextView) convertView.findViewById(R.id.single_package_specification);
            mViewHolder.mArrowView = (ImageView) convertView.findViewById(R.id.single_package_arrow);
            mViewHolder.mArrowParentView = (LinearLayout) convertView.findViewById(R.id.single_package_arrow_parent);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        SinglePackageProducts model = mSinglePackage.productList.get(position);

        for (SinglePackageProduct single : model.products) {
            if (single.isChecked) {
                ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, single.productInfo.smallPhoto);
                mViewHolder.mNameView.setText(single.productInfo.name);
                mViewHolder.mPriceView.setText(OtherUtil.amountFormat(single.productInfo.price, true));
                mViewHolder.mNumberView.setText(single.productInfo.getProductNum());
                mViewHolder.mSpecificationView.setText(single.title);
                break;
            }
        }

        mViewHolder.mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnClickListener) mOnClickListener.onClickForDetail(view, mPosition1, position);
            }
        });
        mViewHolder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnClickListener) mOnClickListener.onClickForDetail(view, mPosition1, position);
            }
        });

        if (model.products.size() > 1) {
            mViewHolder.mArrowView.setVisibility(View.VISIBLE);
            mViewHolder.mArrowParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mOnClickListener)
                        mOnClickListener.onClickForArrow(view, mPosition1, position);
                }
            });
        } else {
            mViewHolder.mArrowView.setVisibility(View.GONE);
            mViewHolder.mArrowParentView.setOnClickListener(null);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView mPhotoView;
        TextView mNameView;
        TextView mPriceView;
        TextView mNumberView;
        TextView mSpecificationView;
        ImageView mArrowView;
        LinearLayout mArrowParentView;
    }
}
