package com.hxqc.mall.thirdshop.accessory4s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 16
 * Des: 分期购车车系列表adapter
 * FIXME
 * Todo
 */

public class InstallmentBuyingSeriesAdapter extends RecyclerView.Adapter<InstallmentBuyingSeriesAdapter.ItemViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public InstallmentBuyingSeriesAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_installment_buying_series, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        itemClickListener(holder,position);
    }

    private void itemClickListener(ItemViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherAccessory4S.toInstallmentBuyingModel(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.item_installment_buying_img);
            itemView.findViewById(R.id.item_installment_buying_auto_model);
            itemView.findViewById(R.id.item_installment_buying_auto_shop);
            itemView.findViewById(R.id.item_installment_buying_auto_shop_price);
//            itemView.findViewById(R.id.item_installment_buying_down_payment);
//            itemView.findViewById(R.id.item_installment_buying_down_payment_pb);
//            itemView.findViewById(R.id.item_installment_buying_periods);
//            itemView.findViewById(R.id.item_installment_buying_periods_pb);
            itemView.findViewById(R.id.item_installment_buying_down_payment_money);
            itemView.findViewById(R.id.item_installment_buying_monthly);
        }
    }
}
