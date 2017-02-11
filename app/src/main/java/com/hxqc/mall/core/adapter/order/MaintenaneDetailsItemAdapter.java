package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.order.MaintenanceOrderDetailBean;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/7.
 * 订单详情 --保养项目
 */
public class MaintenaneDetailsItemAdapter  extends BaseAdapter{
     private ArrayList<MaintenanceOrderDetailBean.MaintenanceItem> mMaintenanceItems;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MaintenaneDetailsItemAdapter(ArrayList<MaintenanceOrderDetailBean.MaintenanceItem> mMaintenanceItems, Context mContext) {
        this.mMaintenanceItems = mMaintenanceItems;
        this.mContext = mContext;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMaintenanceItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMaintenanceItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler mViewHoler;
        if(convertView==null)
        {
            convertView=mLayoutInflater.inflate(R.layout.activity_my_order_details_maintain_item,parent,false);
            mViewHoler=new ViewHoler(convertView);
            convertView.setTag(mViewHoler);
        }else
        {
            mViewHoler= (ViewHoler) convertView.getTag();
        }
        mViewHoler.mNameView.setText((position+1)+"."+mMaintenanceItems.get(position).name);
        mViewHoler.mAccessoriesView.setText(OtherUtil.amountFormat((mMaintenanceItems.get(position).amount-mMaintenanceItems.get(position).workCost),true));
        mViewHoler.mWorkCostView.setText(OtherUtil.amountFormat(mMaintenanceItems.get(position).workCost,true));
        mViewHoler.mProjectView.setText(OtherUtil.amountFormat(mMaintenanceItems.get(position).amount,true));
        mViewHoler.mGoodsView.setAdapter(new MaintenaneDetailsGoodsAdapter(mMaintenanceItems.get(position).goods,mContext));
        return convertView;
    }

    class ViewHoler {
        TextView mNameView;
        ListViewNoSlide mGoodsView;
        TextView mWorkCostView; //工时小计
        TextView mAccessoriesView; //配件小计
        TextView mProjectView; //本项小计

        public ViewHoler(View v) {
            mNameView= (TextView) v.findViewById(R.id.myorder_details_maintenanceItems_name);
            mGoodsView= (ListViewNoSlide) v.findViewById(R.id.myorder_details_maintenanceItems_goods);
            mWorkCostView= (TextView) v.findViewById(R.id.myorder_details_maintenanceItems_workCost_subtotal);
            mAccessoriesView= (TextView) v.findViewById(R.id.myorder_details_maintenanceItems_accessories_subtotal);
            mProjectView= (TextView) v.findViewById(R.id.myorder_details_maintenanceItems_project_subtotal);
        }
    }
}
