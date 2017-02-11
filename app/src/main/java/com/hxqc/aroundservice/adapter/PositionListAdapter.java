package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.aroundservice.model.CityList;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年04月13日
 */
public class PositionListAdapter extends BaseAdapter {
    private final static String TAG = PositionListAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< CityList > mCityLists;


    public PositionListAdapter(Context mContext, ArrayList< CityList > cityLists) {
        this.mContext = mContext;
        this.mCityLists = cityLists;
    }


    @Override
    public int getCount() {
        return mCityLists.size();
    }


    @Override
    public CityList getItem(int position) {
        return mCityLists.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_choose, null);
            viewHolder = new ViewHolder();
            viewHolder.mCityView = (TextView) convertView.findViewById(R.id.tv_item_area);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mCityView.setText(getItem(position).cityname);
        return convertView;
    }


    class ViewHolder {
        TextView mCityView;
    }
}
