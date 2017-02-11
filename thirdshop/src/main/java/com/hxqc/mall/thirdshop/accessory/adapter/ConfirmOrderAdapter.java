package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.ConfirmOrderItem;
import com.hxqc.mall.thirdshop.accessory.views.ShopInOrder;

import java.util.ArrayList;

/**
 * 说明:确认订单适配器
 *
 * @author: 吕飞
 * @since: 2016-02-24
 * Copyright:恒信汽车电子商务有限公司
 */
public class ConfirmOrderAdapter extends BaseAdapter {
    ArrayList<ConfirmOrderItem> mConfirmOrders;
    Context mContext;
    LayoutInflater mLayoutInflater;
    public ArrayList<EditText> mNotesViews = new ArrayList<>();

    public ConfirmOrderAdapter(Context context, ArrayList<ConfirmOrderItem> confirmOrders) {
        mContext = context;
        mConfirmOrders = confirmOrders;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mConfirmOrders.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_confirm_order, null);
            viewHolder = new ViewHolder();
            viewHolder.mShopInOrderView = (ShopInOrder) convertView.findViewById(R.id.shop_in_order);
            viewHolder.mProductNumView = (TextView) convertView.findViewById(R.id.product_num);
            viewHolder.mAmountOrderView = (TextView) convertView.findViewById(R.id.amount_order);
            viewHolder.mPackageCutView = (TextView) convertView.findViewById(R.id.package_cut);
            viewHolder.mAmountToPayView = (TextView) convertView.findViewById(R.id.amount_to_pay);
            viewHolder.mShopPayView = (TextView) convertView.findViewById(R.id.shop_pay);
            viewHolder.mSubscriptionView = (TextView) convertView.findViewById(R.id.subscription);
            viewHolder.mShopAddressLayoutView = (LinearLayout) convertView.findViewById(R.id.shop_address_layout);
            viewHolder.mShopAddressView = (TextView) convertView.findViewById(R.id.shop_address);
            viewHolder.mShopPhoneLayoutView = (LinearLayout) convertView.findViewById(R.id.shop_phone_layout);
            viewHolder.mShopPhoneView = (TextView) convertView.findViewById(R.id.shop_phone);
            viewHolder.mNotesView = (EditText) convertView.findViewById(R.id.notes);
            viewHolder.mBottomView = (LinearLayout) convertView.findViewById(R.id.bottom);
            if (mNotesViews.size() < mConfirmOrders.size()) {
                mNotesViews.add(viewHolder.mNotesView);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OtherUtil.setVisible(viewHolder.mBottomView, !(position == mConfirmOrders.size() - 1));
        viewHolder.mProductNumView.setText(mConfirmOrders.get(position).getTotalCount());
        viewHolder.mAmountOrderView.setText(OtherUtil.amountFormat(mConfirmOrders.get(position).orderTotalAmount, true));
        viewHolder.mPackageCutView.setText("-" + OtherUtil.amountFormat(mConfirmOrders.get(position).packageDiscount, true));
        viewHolder.mAmountToPayView.setText(OtherUtil.amountFormat(mConfirmOrders.get(position).actualPaymentAmount, true));
//        viewHolder.mShopPayView.setText(OtherUtil.amountFormat(mConfirmOrders.get(position).onShopPaymentAmount, true));
//        viewHolder.mSubscriptionView.setText(OtherUtil.amountFormat(mConfirmOrders.get(position).subscription, true));
//        viewHolder.mShopAddressView.setText(mConfirmOrders.get(position).shopInfo.shopLocation.address);
//        viewHolder.mShopPhoneView.setText(mConfirmOrders.get(position).shopInfo.shopLocation.tel);
//        viewHolder.mShopInOrderView.fillData(mConfirmOrders.get(position).itemListInShop, mConfirmOrders.get(position).shopInfo.shopLocation.name, false);
//        viewHolder.mShopAddressLayoutView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivitySwitcherAccessory.toAMapNvai(mContext, mConfirmOrders.get(position).shopInfo.shopLocation);
//            }
//        });
//        viewHolder.mShopPhoneLayoutView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OtherUtil.callPhone(mContext, mConfirmOrders.get(position).shopInfo.shopLocation.tel);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        ShopInOrder mShopInOrderView;//订单中的一个店铺的商品
        LinearLayout mShopAddressLayoutView;//店铺地址点击区
        TextView mShopAddressView;//店铺地址
        LinearLayout mShopPhoneLayoutView;//店铺电话点击区
        TextView mShopPhoneView;//店铺电话
        TextView mSubscriptionView;//订金
        TextView mProductNumView;//商品数量
        TextView mAmountOrderView;//订单总额
        TextView mPackageCutView;//套餐优惠
        TextView mAmountToPayView;//应付款
        TextView mShopPayView;//到店需付金额
        EditText mNotesView;//备注
        LinearLayout mBottomView;//顶部空白
    }
}
