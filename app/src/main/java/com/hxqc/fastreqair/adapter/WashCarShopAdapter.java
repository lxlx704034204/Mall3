package com.hxqc.fastreqair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.fastreqair.model.ChargeItem;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * @Author : 钟学东
 * @Since : 2016-05-18
 * FIXME
 * Todo
 */
public class WashCarShopAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChargeItem> chargeItems;
    public WashCarShopAdapter(Context context ,ArrayList<ChargeItem> chargeItems ){
        this.context = context;
        this.chargeItems = chargeItems;
    }

    @Override
    public int getCount() {
        return chargeItems.size();
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
        ViewHolder holder;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wash_car_adapter,null);
            holder = new ViewHolder();
            holder.mItemNameView = (TextView) convertView.findViewById(R.id.item_name);
            holder.mMoneyView = (TextView) convertView.findViewById(R.id.money);
//            holder.mPayView = (Button) convertView.findViewById(R.id.button_pay);
            convertView.setTag(holder);
        }
        holder.mItemNameView.setText(chargeItems.get(position).name);
        holder.mMoneyView.setText(OtherUtil.amountFormat((float) chargeItems.get(position).amount,true));

        return convertView;
    }

    class ViewHolder{
        TextView mItemNameView;
        TextView mMoneyView;
//        Button  mPayView;
    }
}
