package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.db.area.AreaModel;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-26
 * FIXME
 * Todo
 */
public class MyAreaChooseAdapter<T> extends BaseAdapter {


    ArrayList<T> areaModels;
    Context context;
    String chooseStr="";

    public MyAreaChooseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return areaModels != null ? areaModels.size() : 0;
    }

    public void setChooseColorChange(ArrayList<T> areaModels, String str) {
        this.areaModels = areaModels;
        this.chooseStr = str;
        notifyDataSetChanged();
    }

    @Override
    public AreaModel getItem(int position) {
        return (AreaModel) areaModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.item_list_choose, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_item_area);

        if (chooseStr.equals(getItem(position).getTitle())) {
            textView.setTextColor(context.getResources().getColor(R.color.cursor_orange));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
        }
        AreaModel areaModel = getItem(position);

        textView.setText(areaModel.getTitle());
        return view;
    }

    class ViewHolder {
        public TextView area;
    }


}
