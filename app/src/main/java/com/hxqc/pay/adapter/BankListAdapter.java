package com.hxqc.pay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.pay.model.PaymentMethod;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Created by CPR062 on 2015/3/6.
 * <p/>
 * 支付方式列表 adapter
 */
public class BankListAdapter extends BaseAdapter {

    Context context;
    ArrayList< PaymentMethod > paymentList;
//    String[] bank = new String[]{"微信支付", "支付宝支付", "信用卡", "储蓄卡", "信用卡", "储蓄卡", "信用卡", "储蓄卡", "信用卡", "储蓄卡"};
    String balanceShowText = "余额：¥ 0";

    public BankListAdapter(ArrayList< PaymentMethod > paymentList, Context context,String bSHow) {
        this.paymentList = paymentList;
        this.context = context;
        this.balanceShowText = bSHow;
    }

    @Override
    public int getCount() {
        return paymentList.size();
    }

    @Override
    public PaymentMethod getItem(int position) {
        return paymentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        BankListHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = (BankListHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_bank_list, null);
            holder = new BankListHolder();
            holder.payMethod = (TextView) view.findViewById(R.id.tv_pay_method);
            holder.icon = (ImageView) view.findViewById(R.id.iv_bank_logo);
            holder.rightTip = (TextView) view.findViewById(R.id.balance_show);
            view.setTag(holder);
        }

        PaymentMethod item = getItem(position);

        if (PayConstant.BALANCE.equals(item.paymentID)){
            holder.rightTip.setText(balanceShowText);
            holder.rightTip.setVisibility(View.VISIBLE);
        }else {
            holder.rightTip.setVisibility(View.GONE);
        }

        holder.payMethod.setText(item.title);
        ImageUtil.setImage(context, holder.icon, item.thumb);
        return view;
    }

    class BankListHolder {
        TextView payMethod;
        ImageView icon;
        TextView rightTip;
    }

}
