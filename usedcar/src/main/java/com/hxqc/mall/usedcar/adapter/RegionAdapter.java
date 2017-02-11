package com.hxqc.mall.usedcar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.RegionModel;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2015-09-24
 * FIXME
 * Todo
 */
public class RegionAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    private MaterialEditText mRegionView;
    private ArrayList<RegionModel> areaModels;

    public RegionAdapter(Context context, ArrayList<RegionModel> areaModels_s, MaterialEditText mRegionView){
        this.context = context;
        this.mRegionView = mRegionView;
        this.areaModels = areaModels_s;
        mLayoutInflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return areaModels == null ? 0 : areaModels.size();
    }

    @Override
    public RegionModel getItem(int position) {
        return areaModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            convertView =mLayoutInflater.inflate(R.layout.item_choose_list,null);
        }

        TextView area = (TextView) convertView.findViewById(R.id.tv_item_area);

        area.setText(areaModels.get(position).r_name);

        if (mRegionView.getText().toString().trim().equals(getItem(position).r_name)) {
            area.setTextColor(context.getResources().getColor(R.color.text_color_orange));
        } else {
            area.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
        }

        return convertView;
    }
}
