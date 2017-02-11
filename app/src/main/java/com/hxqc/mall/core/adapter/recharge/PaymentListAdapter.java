package com.hxqc.mall.core.adapter.recharge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.thirdshop.views.PaymentPieceView;
import com.hxqc.pay.model.PaymentMethod;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-09
 * FIXME
 * Todo 支付方式的适配器
 */
@Deprecated
public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder> {
    private ArrayList<PaymentMethod> mData;
    private Context mContext;

    public PaymentListAdapter(ArrayList<PaymentMethod> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PaymentPieceView paymentPieceView = new PaymentPieceView(mContext);
        return new ViewHolder(paymentPieceView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PaymentMethod paymentMethod = mData.get(position);
        holder.view.setValues(paymentMethod.thumb, paymentMethod.title);
        holder.view.setChecked(paymentMethod.selected);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).selected = false;
                }
                mData.get(position).selected = true;
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取已选择的
     *
     * @return
     */
    public PaymentMethod getSelectedWay() {
        PaymentMethod paymentMethod = null;
        for (PaymentMethod p : mData) {
            if (p.selected) {
                paymentMethod = new PaymentMethod();
                paymentMethod.selected = true;
                paymentMethod.paymentID = p.paymentID;
                paymentMethod.thumb = p.thumb;
                paymentMethod.title = p.title;
            }
        }
        return paymentMethod;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private PaymentPieceView view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = (PaymentPieceView) itemView;
        }
    }
}
