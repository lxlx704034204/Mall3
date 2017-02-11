package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.order.MaintenanceBean;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/4.
 * 我的订单——保养项
 */
public class MaintenanceItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MaintenanceBean.MaintenanceProject> mMaintenanceProjects;
    private LayoutInflater mLayoutInflater;

    public MaintenanceItemAdapter(Context mContext, ArrayList<MaintenanceBean.MaintenanceProject> mMaintenanceProjects) {
        this.mContext = mContext;
        this.mMaintenanceProjects = mMaintenanceProjects;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMaintenanceProjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mMaintenanceProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView ==null)
        {
            convertView=mLayoutInflater.inflate(R.layout.activity_my_order_maintenance_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.mNameView= (TextView) convertView.findViewById(R.id.myorder_maintenane_item_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.mNameView.setText((position+1)+"."+mMaintenanceProjects.get(position).name);
        return convertView;
    }
    class ViewHolder{
          TextView mNameView;
    }

}
