package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.aroundservice.model.City;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo
 */
public class CityAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<City> mCities;

    public CityAdapter(Context context, ArrayList<City> mCities) {
        this.mContext = context;
        this.mCities = mCities;
    }

    public ArrayList<City> getmCities() {
        return mCities;
    }

    @Override
    public int getCount() {
        return mCities != null ? mCities.size() : 0;
    }

    public void notifyData( ArrayList<City> mCities) {
        this.mCities = mCities;
        notifyDataSetChanged();
    }

    @Override
    public String getItem(int position) {
        return mCities.get(position).city_name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_list_choose, null);
        TextView textView = (TextView) view.findViewById(com.hxqc.mall.core.R.id.tv_item_area);
        textView.setText(mCities.get(position).city_name);
        return view;
    }

    class ViewHolder {
        public TextView area;
    }

}
