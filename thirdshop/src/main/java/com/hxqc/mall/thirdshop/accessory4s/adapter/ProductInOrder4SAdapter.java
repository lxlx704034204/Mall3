package com.hxqc.mall.thirdshop.accessory4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductInfo4S;
import com.hxqc.mall.thirdshop.accessory4s.views.ProductInOrder4S;

import java.util.ArrayList;

/**
 * 说明:订单中的商品
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ProductInOrder4SAdapter extends BaseAdapter {
    private ArrayList<ProductInfo4S> mProductInOrders;
    private boolean mToProductDetail;
    private LayoutInflater mLayoutInflater;

    public ProductInOrder4SAdapter(Context context, ArrayList<ProductInfo4S> productInOrders, boolean toProductDetail) {
        mProductInOrders = productInOrders;
        mToProductDetail = toProductDetail;
        mLayoutInflater = LayoutInflater.from(context);
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
            convertView = mLayoutInflater.inflate(R.layout.item_product_in_order_4s, null);
            viewHolder = new ViewHolder();
            viewHolder.mProductInOrderView = (ProductInOrder4S) convertView.findViewById(R.id.product_in_order);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mProductInOrderView.fillData(mProductInOrders.get(position), mToProductDetail);
        return convertView;
    }

    class ViewHolder {
        ProductInOrder4S mProductInOrderView;
    }
}
