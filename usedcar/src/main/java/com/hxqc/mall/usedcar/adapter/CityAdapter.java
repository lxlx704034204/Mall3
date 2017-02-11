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
import com.hxqc.mall.usedcar.model.CityModel;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2015-09-24
 * FIXME
 * Todo
 */
public class CityAdapter extends BaseAdapter {

    Context context;
    MaterialEditText mCityView;
    ArrayList<CityModel> areaModels_c;//
    LayoutInflater mLayoutInflater ;

    public CityAdapter(Context context , ArrayList<CityModel> areaModels_c, MaterialEditText mCityView){

        this.context = context;
        this.mCityView = mCityView;
        this.areaModels_c = areaModels_c;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return areaModels_c == null ? 0 : areaModels_c.size();
    }

    @Override
    public CityModel getItem(int position) {
        return areaModels_c.get(position);
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

        area.setText(areaModels_c.get(position).c_name);

        if (mCityView.getText().toString().trim().equals(getItem(position).c_name)) {
            area.setTextColor(context.getResources().getColor(R.color.text_color_orange));
        } else {
            area.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
        }

        return convertView;
    }
}
