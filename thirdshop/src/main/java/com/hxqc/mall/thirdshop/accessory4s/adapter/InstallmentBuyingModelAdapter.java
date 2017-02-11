package com.hxqc.mall.thirdshop.accessory4s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.auto.adapter.HFRecyclerView;
import com.hxqc.mall.auto.model.Record;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;


/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 23
 * Des: 分期购车车型列表adapter
 * FIXME
 * Todo
 */
public class InstallmentBuyingModelAdapter extends HFRecyclerView<Record> {

    private static final String TAG = "RepairRecordAdapterV3";
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private int defFlag = 0;

    public InstallmentBuyingModelAdapter(Context context) {
        super(new ArrayList<Record>(), true, false);
        this.mContext = context;
    }

    /*public void notifyData(ArrayList<Record> data, MyAuto myAutoV2, NextMaintenance nextMaintenance) {
        this.mData = data;
        this.myAutoV2 = myAutoV2;
        this.notifyDataSetChanged();
        this.mNextMaintenance = nextMaintenance;
    }*/

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_installment_buying_model, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_installment_buying_head, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebugLog.i(TAG, "position:" + position);
        if (holder instanceof ItemViewHolder) {

        } else if (holder instanceof HeaderViewHolder) {
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.item_installment_buying_auto_model);
            itemView.findViewById(R.id.item_installment_buying_auto_shop_price);
            itemView.findViewById(R.id.item_installment_buying_manufacturer_price);
            itemView.findViewById(R.id.item_installment_buying_down_payment);
            itemView.findViewById(R.id.item_installment_buying_periods);
            itemView.findViewById(R.id.item_installment_buying_down_payment_money);
            itemView.findViewById(R.id.item_installment_buying_monthly);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.item_installment_buying_img);
            itemView.findViewById(R.id.item_installment_buying_auto_model);
            itemView.findViewById(R.id.item_installment_buying_auto_shop_price);
            itemView.findViewById(R.id.item_installment_buying_manufacturer_price);
            itemView.findViewById(R.id.item_installment_buying_auto_shop);
        }
    }
}
