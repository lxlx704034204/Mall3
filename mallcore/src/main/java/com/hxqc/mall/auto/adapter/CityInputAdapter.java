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
public class CityInputAdapter extends BaseAdapter {
//    private String city = "辽吉黑冀晋陕鲁皖苏浙豫鄂湘赣台闽滇琼川黔粤甘青渝沪津京宁蒙藏新贵港澳";
    private String city = "京沪浙苏粤鄂晋冀豫川渝辽吉黑皖鲁湘赣闽陕甘宁蒙津贵云桂琼青新藏港澳";
//    private String city = "京津冀晋蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新港澳台";
    public CityInputAdapter(){

    }

    @Override
    public int getCount() {

        return city.length();
    }

    @Override
    public CharSequence getItem(int position) {
        return city.subSequence(position,position+1);
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
        textview.setText(city.subSequence(position,position+1));
        return convertView;
    }
}
