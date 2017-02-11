package com.hxqc.mall.thirdshop.accessory4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductInfo4S;
import com.hxqc.mall.thirdshop.accessory4s.views.ShoppingCartInvalid4S;
import com.hxqc.mall.thirdshop.accessory4s.views.ShoppingCartValid4S;

import java.util.ArrayList;

/**
 * 说明:购物车适配器
 *
 * @author: 吕飞
 * @since: 2016-03-11
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShoppingCart4SAdapter extends BaseAdapter {
    private static final int VALID = 0;
    private static final int INVALID = 1;
    private ArrayList<ProductInfo4S> mProductInfos;
    private LayoutInflater mLayoutInflater;

    public ShoppingCart4SAdapter(Context context, ArrayList<ProductInfo4S> productInfos) {
        mProductInfos = productInfos;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mProductInfos.size();
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
            convertView = mLayoutInflater.inflate(R.layout.item_shopping_cart_4s, null);
            viewHolder = new ViewHolder();
            viewHolder.mShoppingCartValidView = (ShoppingCartValid4S) convertView.findViewById(R.id.shopping_cart_valid);
            viewHolder.mShoppingCartInvalidView = (ShoppingCartInvalid4S) convertView.findViewById(R.id.shopping_cart_invalid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OtherUtil.setVisible(viewHolder.mShoppingCartValidView, getItemViewType(position) == VALID);
        OtherUtil.setVisible(viewHolder.mShoppingCartInvalidView, getItemViewType(position) == INVALID);
        if (getItemViewType(position) == VALID) {
            viewHolder.mShoppingCartValidView.fillData(mProductInfos, position, this);
        } else {
            viewHolder.mShoppingCartInvalidView.fillData(mProductInfos, position, this);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mProductInfos.get(position).isInvalid) {
            return INVALID;
        } else {
            return VALID;
        }
    }

    class ViewHolder {
        ShoppingCartValid4S mShoppingCartValidView;
        ShoppingCartInvalid4S mShoppingCartInvalidView;
    }
}
