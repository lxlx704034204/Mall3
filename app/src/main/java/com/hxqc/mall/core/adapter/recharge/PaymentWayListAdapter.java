package com.hxqc.mall.core.adapter.recharge;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.thirdshop.views.PaymentPieceView;
import com.hxqc.pay.model.PaymentMethod;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-10
 * FIXME
 * Todo 支付方式的适配器
 */
public class PaymentWayListAdapter extends BaseAdapter {

    private ArrayList<PaymentMethod> mData;
    private Context mContext;

    public PaymentWayListAdapter(ArrayList<PaymentMethod> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取已选择的
     *
     * @return
     */
    public PaymentMethod getSelectedWay() {
        PaymentMethod paymentMethod = null;
        if (mData == null)
            return null;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        PaymentMethod paymentMethod = mData.get(position);
        PaymentPieceView paymentPieceView = new PaymentPieceView(mContext);
        paymentPieceView.setValues(paymentMethod.thumb, paymentMethod.title);
        paymentPieceView.setChecked(paymentMethod.selected);
        paymentPieceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).selected = false;
                }
                mData.get(position).selected = true;
                notifyDataSetChanged();
            }
        });
        return paymentPieceView;
    }
}
