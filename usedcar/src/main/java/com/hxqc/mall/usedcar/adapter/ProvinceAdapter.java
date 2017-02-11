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
import com.hxqc.mall.usedcar.model.AreaModel;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2015-09-24
 * FIXME
 * Todo
 */
public class ProvinceAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mLayoutInflater ;
    ArrayList<AreaModel> areaModels_p;
    MaterialEditText mProvinceView;

    public ProvinceAdapter(Context context, ArrayList<AreaModel> areaModels_p, MaterialEditText mProvinceView){
        this.context = context;
        this.areaModels_p = areaModels_p;
        mLayoutInflater = LayoutInflater.from(context);
        this.mProvinceView = mProvinceView;
    }

    @Override
    public int getCount() {
        return areaModels_p == null ? 0 : areaModels_p.size();
    }

    @Override
    public AreaModel getItem(int position) {
        return areaModels_p.get(position);
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
        area.setText(areaModels_p.get(position).p_name);
        if (mProvinceView.getText().toString().trim().equals(getItem(position).p_name)) {
            area.setTextColor(context.getResources().getColor(R.color.text_color_orange));
        } else {
            area.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
        }

        return convertView;
    }
}
