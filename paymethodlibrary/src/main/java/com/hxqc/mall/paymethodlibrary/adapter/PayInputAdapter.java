package com.hxqc.mall.paymethodlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.paymethodlibrary.R;

/**
 * Author: wanghao
 * Date: 2016-03-17
 * FIXME
 * Todo
 */
public class PayInputAdapter extends BaseAdapter {

    private Context c;

    public PayInputAdapter(Context c) {
        this.c = c;
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public String getItem(int position) {

        if (position >= 0 && position <= 8) {
            return position + 1 + "";
        } else if (position == 9) {
            return "";
        } else if (position == 10) {
            return "0";
        } else if (position == 11) {
            return "backspace";
        }
        return "overLength";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_number_view, parent, false);

        TextView textview = (TextView) view.findViewById(R.id.item_number_view);

        if (position >= 0 && position <= 8) {
            String number = position+1+"";
            textview.setText(number);
        } else if (position == 9) {
            textview.setText("");
            textview.setBackgroundColor(c.getResources().getColor(R.color.bg_key_gray));
        } else if (position == 10) {
            textview.setText("0");
        } else if (position == 11) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_number_back_view, parent, false);
        }

        return view;
    }

}
