package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.auto.model.Record;
import com.hxqc.mall.thirdshop.maintenance.views.MaintenanceDetailView;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-25
 * FIXME
 * Todo 保养记录列表适配器
 */
public class MaintenanceRecordListAdapter extends BaseAdapter {
    private ArrayList<Record> mData;
    private Context mContext;

    public MaintenanceRecordListAdapter(ArrayList<Record> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = new MaintenanceDetailView(mContext);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        holder.detailView = (MaintenanceDetailView) convertView;
        holder.detailView.addData(mData.get(position).maintenancDate, mData.get(position).amount,
                mData.get(position).distance,
                mData.get(position).maintenanceItem, mData.get(position).shopName);
        return convertView;
    }

    class ViewHolder {
        private MaintenanceDetailView detailView;
    }
}
