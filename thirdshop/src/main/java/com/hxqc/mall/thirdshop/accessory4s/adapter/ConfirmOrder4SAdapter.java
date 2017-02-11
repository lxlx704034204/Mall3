package com.hxqc.mall.thirdshop.accessory4s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.activity.ConfirmOrder4SActivity;
import com.hxqc.mall.thirdshop.accessory4s.model.ConfirmOrderItem4S;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.accessory4s.views.ShopInOrder4S;

import java.util.ArrayList;

/**
 * 说明:确认订单适配器
 *
 * @author: 吕飞
 * @since: 2016-02-24
 * Copyright:恒信汽车电子商务有限公司
 */
public class ConfirmOrder4SAdapter extends RecyclerView.Adapter {
    ArrayList<ConfirmOrderItem4S> mConfirmOrders;
    Context mContext;

    public ConfirmOrder4SAdapter(Context context, ArrayList<ConfirmOrderItem4S> confirmOrders) {
        mContext = context;
        mConfirmOrders = confirmOrders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_order_4s, parent, false);
        return new ViewHolder(v, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OtherUtil.setVisible(((ViewHolder) holder).mBottomView, !(position == mConfirmOrders.size() - 1));
        ((ViewHolder) holder).mShopTitleView.setText(mConfirmOrders.get(position).shopInfo.shopTitle);
        ((ViewHolder) holder).mProductNumView.setText(mConfirmOrders.get(position).getTotalCount());
        ((ViewHolder) holder).mAmountOrderView.setText(OtherUtil.amountFormat(mConfirmOrders.get(position).orderAmount, true));
        ((ViewHolder) holder).mPackageCutView.setText("-" + OtherUtil.amountFormat(mConfirmOrders.get(position).packageDiscount, true));
        ((ViewHolder) holder).mAmountToPayView.setText(OtherUtil.amountFormat(mConfirmOrders.get(position).payAmount, true));
        ((ViewHolder) holder).mShopAddressView.setText(mConfirmOrders.get(position).shopInfo.address);
        ((ViewHolder) holder).mShopPhoneView.setText(mConfirmOrders.get(position).shopInfo.tel);
        ((ViewHolder) holder).mShopInOrderView.fillData(mConfirmOrders.get(position).productList, false);
        ((ViewHolder) holder).mMyCustomEditTextListener.updatePosition(position);
        ((ViewHolder) holder).mShopAddressLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherAccessory4S.toAMapNvai(mContext, new PickupPointT(mConfirmOrders.get(position).shopInfo.address, mConfirmOrders.get(position).shopInfo.latitude, mConfirmOrders.get(position).shopInfo.longitude, mConfirmOrders.get(position).shopInfo.tel));
            }
        });
        ((ViewHolder) holder).mShopPhoneLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtil.callPhone(mContext, mConfirmOrders.get(position).shopInfo.tel);
            }
        });
        ((ViewHolder) holder).mNotesView.setText(((ConfirmOrder4SActivity) mContext).mRemarks.get(position));
    }

    @Override
    public int getItemCount() {
        return mConfirmOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShopInOrder4S mShopInOrderView;//订单中的一个店铺的商品
        LinearLayout mShopAddressLayoutView;//店铺地址点击区
        TextView mShopAddressView;//店铺地址
        LinearLayout mShopPhoneLayoutView;//店铺电话点击区
        TextView mShopPhoneView;//店铺电话
        TextView mProductNumView;//商品数量
        TextView mAmountOrderView;//订单总额
        TextView mPackageCutView;//套餐优惠
        TextView mAmountToPayView;//应付款
        EditText mNotesView;//备注
        LinearLayout mBottomView;//底部空白
        TextView mShopTitleView;//店铺名字
        MyCustomEditTextListener mMyCustomEditTextListener;

        public ViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
            super(v);
            mShopInOrderView = (ShopInOrder4S) v.findViewById(R.id.shop_in_order);
            mProductNumView = (TextView) v.findViewById(R.id.product_num);
            mAmountOrderView = (TextView) v.findViewById(R.id.amount_order);
            mPackageCutView = (TextView) v.findViewById(R.id.package_cut);
            mAmountToPayView = (TextView) v.findViewById(R.id.amount_to_pay);
            mShopTitleView = (TextView) v.findViewById(R.id.shop_title);
            mShopAddressLayoutView = (LinearLayout) v.findViewById(R.id.shop_address_layout);
            mShopAddressView = (TextView) v.findViewById(R.id.shop_address);
            mShopPhoneLayoutView = (LinearLayout) v.findViewById(R.id.shop_phone_layout);
            mShopPhoneView = (TextView) v.findViewById(R.id.shop_phone);
            mNotesView = (EditText) v.findViewById(R.id.notes);
            mBottomView = (LinearLayout) v.findViewById(R.id.bottom);
            mMyCustomEditTextListener = myCustomEditTextListener;
            mNotesView.addTextChangedListener(myCustomEditTextListener);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            ((ConfirmOrder4SActivity) mContext).mRemarks.set(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
