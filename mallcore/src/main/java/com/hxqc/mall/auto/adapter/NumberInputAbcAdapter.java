package com.hxqc.mall.auto.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Author :liukechong
 * Date : 2016-02-29
 * FIXME
 * Todo
 */
public class NumberInputAbcAdapter extends BaseAdapter {
    String Abc = "QWERTYUIOASDFGHJKPZXCVBNML";
    @Override
    public int getCount() {
        return Abc.length();
    }

    @Override
    public Object getItem(int position) {
        return Abc.subSequence(position,position+1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_input, parent, false);
        }
        TextView textview = (TextView) convertView.findViewById(R.id.item_grid_view);
        textview.setText(Abc.subSequence(position,position+1));
        return convertView;
    }
}
