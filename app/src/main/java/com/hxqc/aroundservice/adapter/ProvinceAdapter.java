package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.aroundservice.model.Province;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo
 */
public class ProvinceAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Province> mProvinces;

    public ProvinceAdapter(Context context, ArrayList<Province> provinces) {
        this.mContext = context;
        this.mProvinces = provinces;
    }

    public ArrayList<Province> getmProvinces() {
        return mProvinces;
    }

    @Override
    public int getCount() {
        return mProvinces != null ? mProvinces.size() : 0;
    }

    public void notifyData(ArrayList<Province> provinces) {
        this.mProvinces = provinces;
        notifyDataSetChanged();
    }

    @Override
    public String getItem(int position) {
        return mProvinces.get(position).province;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_list_choose, null);
        TextView textView = (TextView) view.findViewById(com.hxqc.mall.core.R.id.tv_item_area);
        textView.setText(mProvinces.get(position).province);
        return view;
    }

    class ViewHolder {
        public TextView area;
    }

}
