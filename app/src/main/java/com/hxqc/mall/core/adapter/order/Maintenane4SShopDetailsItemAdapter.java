package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.order.Maintenance4SShopOrderDetailBean;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/7.
 * 订单详情 --保养项目
 */
public class Maintenane4SShopDetailsItemAdapter extends BaseAdapter{
     private ArrayList<Maintenance4SShopOrderDetailBean.MaintenanceItem> mMaintenanceItems;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public Maintenane4SShopDetailsItemAdapter(ArrayList<Maintenance4SShopOrderDetailBean.MaintenanceItem> mMaintenanceItems, Context mContext) {
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
            convertView=mLayoutInflater.inflate(R.layout.activity_my_order_details_maintain_4sshop_item,parent,false);
            mViewHoler=new ViewHoler(convertView);
            convertView.setTag(mViewHoler);
        }else
        {
            mViewHoler= (ViewHoler) convertView.getTag();
        }
        mViewHoler.mNameView.setText((position+1)+"."+mMaintenanceItems.get(position).name);
        String goodsName="";
        for (int i = 0; i < mMaintenanceItems.get(position).goods.size(); i++) {
            goodsName += mMaintenanceItems.get(position).goods.get(i).name;
            if(i !=mMaintenanceItems.get(position).goods.size()-1)
              goodsName += "\n";
        }
        mViewHoler.mGoodsView.setText(goodsName);
        if (mMaintenanceItems.get(position).workCost < 0.01)
        {
            mViewHoler.mWorkTimeAmountView.setText("免费"); //工时费
        }else {
            mViewHoler.mWorkTimeAmountView.setText(OtherUtil.amountFormat(mMaintenanceItems.get(position).workCost,true)); //工时费
        }
        mViewHoler.mAmountView.setText(OtherUtil.amountFormat(mMaintenanceItems.get(position).goodsAmount, true)); //材料费
        OtherUtil.setVisible(mViewHoler.mGoodsAmountlly,mMaintenanceItems.get(position).goodsAmount >= 0.01);
        OtherUtil.setVisible(mViewHoler.mGoodsView,mMaintenanceItems.get(position).goodsAmount >= 0.01);
        return convertView;
    }

    class ViewHoler {
        TextView mNameView;
        TextView mGoodsView;
        TextView mAmountView;
        TextView mWorkTimeAmountView;
        LinearLayout mGoodsAmountlly;

        public ViewHoler(View v) {
            mNameView= (TextView) v.findViewById(R.id.myorder_details_maintenance4sShopItems_name);
            mGoodsView= (TextView) v.findViewById(R.id.myorder_details_maintenance4sShopItems_goods_name);
            mAmountView= (TextView) v.findViewById(R.id.myorder_details_maintenance4sShopItems_amount);
            mWorkTimeAmountView= (TextView) v.findViewById(R.id.myorder_details_maintenance4sShopItems_timeamount);
            mGoodsAmountlly= (LinearLayout) v.findViewById(R.id.myorder_details_maintenance4sShopItems_goodsAmount_lly);
        }
    }
}
