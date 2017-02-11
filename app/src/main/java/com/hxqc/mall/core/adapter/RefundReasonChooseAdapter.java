package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.RefundReason;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-26
 * FIXME
 * Todo
 */
public class RefundReasonChooseAdapter extends BaseAdapter {

    TextView textView;
    ArrayList<RefundReason > areaModels;
    Context context;

    public RefundReasonChooseAdapter(ArrayList<RefundReason> areaModels, Context context, TextView textView) {
        this.areaModels = areaModels;
        this.context = context;
        this.textView = textView;
    }

    @Override
    public int getCount() {
        return areaModels == null ? 0 : areaModels.size();
    }

    @Override
    public RefundReason getItem(int position) {
        return areaModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHeadHolder holder;
//        View view;
//        if (convertView != null) {
//            view = convertView;
//            holder = (ViewHeadHolder) view.getTag();
//
//        } else {
//
//            holder = new ViewHeadHolder();
//            view = View.inflate(context, R.layout.item_list_choose, null);
//            holder.reason = (TextView) view.findViewById(R.id.tv_item_area);
//            view.setTag(holder);
//        }
//
//
//
//        if (textView.getText().toString().trim().equals(getItem(position).reasonContent)) {
//            holder.reason.setTextColor(context.getResources().getColor(R.color.button_pay_orange));
//        } else {
//            holder.reason.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
//        }
//
//        holder.reason.setText(getItem(position).reasonContent);
        View view = View.inflate(context, R.layout.item_list_choose, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_item_area);

        if (textView.getText().toString().trim().equals(getItem(position).reasonContent)) {
            textView.setTextColor(context.getResources().getColor(R.color.cursor_orange));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
        }

        textView.setText(getItem(position).reasonContent);
        return view;
    }

//    class ViewHeadHolder {
//        public TextView reason;
//    }


}
