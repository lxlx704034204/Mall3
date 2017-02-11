package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackage;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProduct;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProducts;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * 优惠套餐列表ListView Adapter
 * Created by huangyi on 15/12/17.
 */
public class PackageListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<SinglePackage> mPackageList;
    LayoutInflater mLayoutInflater;
    PackageListAdapter.OnClickListener mOnClickListener;
    public PackageListAdapter(Context mContext, ArrayList<SinglePackage> mPackageList, PackageListAdapter.OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mPackageList = mPackageList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public int getCount() {
        return mPackageList.size();
    }

    @Override
    public SinglePackage getItem(int position) {
        return mPackageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_package_list, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.package_list_name);
            mViewHolder.mExpriceView = (TextView) convertView.findViewById(R.id.package_list_exprice);
            mViewHolder.mSavePriceView = (TextView) convertView.findViewById(R.id.package_list_save_price);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.package_list_price);
            mViewHolder.mAddCarView = (TextView) convertView.findViewById(R.id.package_list_add_car);
            mViewHolder.mListView = (ListViewNoSlide) convertView.findViewById(R.id.package_list_single);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        SinglePackage model = mPackageList.get(position);

        mViewHolder.mNameView.setText(model.packageName);

        float price = 0.00f; //单品价格
        for (SinglePackageProducts products : model.productList) {
            for (SinglePackageProduct product : products.products) {
                if (product.isChecked) {
                    price += Float.valueOf(product.productInfo.price);
                }
            }
        }
        mViewHolder.mExpriceView.setText(OtherUtil.amountFormat(price, true)); //原价
        mViewHolder.mExpriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mViewHolder.mSavePriceView.setText("立减" + model.packageCut + "元");
        mViewHolder.mPriceView.setText(OtherUtil.amountFormat(price - Float.parseFloat(model.packageCut), true)); //套餐价格

        mViewHolder.mAddCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnClickListener) mOnClickListener.onClickForCar(view, position);
            }
        });
        mViewHolder.mListView.setAdapter(new SinglePackageAdapter(mContext, model, position, mOnClickListener));
        return convertView;
    }

    public interface OnClickListener {

        /**
         * 查看商品详情
         * @param view
         * @param position1 PackageListAdapter position (外层 ListView)
         * @param position2 SinglePackageAdapter position (内层 ListViewNoSlide)
         */
        void onClickForDetail(View view, int position1, int position2);

        /**
         * @param view mArrowParentView LinearLayout
         * @param position1 PackageListAdapter position (外层 ListView)
         * @param position2 SinglePackageAdapter position (内层 ListViewNoSlide)
         */
        void onClickForArrow(View view, int position1, int position2);

        /**
         * @param view 加入购物 TextView
         * @param position PackageListAdapter position (外层 ListView)
         */
        void onClickForCar(View view, int position);
    }

    class ViewHolder {
        TextView mNameView;
        TextView mExpriceView;
        TextView mSavePriceView;
        TextView mPriceView;
        TextView mAddCarView;
        ListViewNoSlide mListView;
    }
}
