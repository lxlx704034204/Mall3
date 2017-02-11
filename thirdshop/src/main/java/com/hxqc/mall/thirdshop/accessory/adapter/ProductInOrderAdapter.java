package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.ProductInfo;
import com.hxqc.mall.thirdshop.accessory.views.ProductInOrder;

import java.util.ArrayList;

/**
 * 说明:订单中的商品
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ProductInOrderAdapter extends BaseAdapter {
    ArrayList<ProductInfo> mProductInOrders;
    boolean mToProductDetail;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public ProductInOrderAdapter(Context context, ArrayList<ProductInfo> productInOrders, boolean toProductDetail) {
        mContext = context;
        mProductInOrders = productInOrders;
        mToProductDetail = toProductDetail;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mProductInOrders.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_product_in_order, null);
            viewHolder = new ViewHolder();
            viewHolder.mProductInOrderView = (ProductInOrder) convertView.findViewById(R.id.product_in_order);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mProductInOrderView.fillData(mProductInOrders.get(position), mToProductDetail);
        return convertView;
    }

    class ViewHolder {
        ProductInOrder mProductInOrderView;
    }
}
