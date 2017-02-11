package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.order.OrderModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.views.order.OrderBottom;
import com.hxqc.mall.views.order.OrderDescriptionForMyOrder;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * 说明:我的订单数据适配器
 *
 * author: 吕飞
 * since: 2015-04-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class UserOrderAdapter extends RecyclerView.Adapter {
    ArrayList<OrderModel > mUserOrders;
    Context mContext;
    public UserOrderAdapter(ArrayList<OrderModel> mUserOrders, Context mContext) {
        this.mUserOrders = mUserOrders;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final OrderModel mUserOrder = mUserOrders.get(position);
        showHead(mUserOrder, holder);
        if (position == 0) {
            ((ViewHolder) holder).view.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).view.setVisibility(View.VISIBLE);
        }
        if (mUserOrder.orderType.equals("0")) {
            ((ViewHolder) holder).mOrderTypeView.setImageResource(R.drawable.normal_order_type);
        } else if (mUserOrder.orderType.equals("1")) {
            ((ViewHolder) holder).mOrderTypeView.setImageResource(R.drawable.special_order_type);
        }

        ((ViewHolder) holder).mOrderDescriptionView.initOrderDescription(mUserOrder);
        if (((ViewHolder) holder).mOrderBottomView.hideBottom(mUserOrder)) {
            ((ViewHolder) holder).mOrderBottomView.setVisibility(View.GONE);
            ((ViewHolder) holder).mLineView.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).mOrderBottomView.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).mLineView.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).mOrderBottomView.initBottom(mContext, mUserOrder,false);
        }
        ((ViewHolder) holder).mOrderDescriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toOrderDetail(mContext, mUserOrder.orderID);
            }
        });
    }

    private void showHead(OrderModel mUserOrder, RecyclerView.ViewHolder holder) {
        ((ViewHolder) holder).mOrderIdView.setText(mContext.getResources().getString(R.string.me_order_id) + mUserOrder.orderID);
        ((ViewHolder) holder).mOrderStatusView.setText(mUserOrder.getOrderStatus());
        if (mUserOrder.orderStatusCode.equals("100") || mUserOrder.orderStatusCode.equals("110") || mUserOrder.orderStatusCode.equals("140") || mUserOrder.orderStatusCode.equals("150") || mUserOrder.orderStatusCode.equals("160")) {
            ((ViewHolder) holder).mOrderStatusView.setTextColor(mContext.getResources().getColor(R.color.special_button));
        } else {
            ((ViewHolder) holder).mOrderStatusView.setTextColor(mContext.getResources().getColor(R.color.main_and_price));
        }
    }


    @Override
    public int getItemCount() {
        return mUserOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mOrderTypeView;
        View view;
        TextView mOrderIdView;
        TextView mOrderStatusView;
        OrderBottom mOrderBottomView;
        OrderDescriptionForMyOrder mOrderDescriptionView;
        View mLineView;

        public ViewHolder(View v) {
            super(v);
            view = v.findViewById(R.id.divider_line);
            mOrderTypeView = (ImageView) v.findViewById(R.id.order_type);
            mOrderIdView = (TextView) v.findViewById(R.id.order_id);
            mOrderBottomView = (OrderBottom) v.findViewById(R.id.order_bottom);
            mOrderStatusView = (TextView) v.findViewById(R.id.order_status);
            mOrderDescriptionView = (OrderDescriptionForMyOrder) v.findViewById(R.id.order_description);
            mLineView = v.findViewById(R.id.line);
        }
    }
}
