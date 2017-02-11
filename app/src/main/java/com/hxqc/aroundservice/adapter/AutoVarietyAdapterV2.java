package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.aroundservice.model.AutoSpeciesType;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo
 */
public class AutoVarietyAdapterV2 extends BaseAdapter {

    private Context mContext;
    private ArrayList<AutoSpeciesType> mAutoSpeciesTypes;

    public AutoVarietyAdapterV2(Context context,ArrayList<AutoSpeciesType> autoSpeciesTypes) {
        this.mContext = context;
        this.mAutoSpeciesTypes = autoSpeciesTypes;
    }

    public ArrayList<AutoSpeciesType> getmAutoSpeciesTypes() {
        return mAutoSpeciesTypes;
    }

    @Override
    public int getCount() {
        return mAutoSpeciesTypes != null ? mAutoSpeciesTypes.size() : 0;
    }

    public void notifyData(ArrayList<AutoSpeciesType> autoSpeciesTypes) {
        this.mAutoSpeciesTypes = autoSpeciesTypes;
        notifyDataSetChanged();
    }

    @Override
    public String getItem(int position) {
        return mAutoSpeciesTypes.get(position).car;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_list_choose, null);
        TextView textView = (TextView) view.findViewById(com.hxqc.mall.core.R.id.tv_item_area);
        textView.setText(mAutoSpeciesTypes.get(position).car);
        return view;
    }

    class ViewHolder {
        public TextView area;
    }

}
