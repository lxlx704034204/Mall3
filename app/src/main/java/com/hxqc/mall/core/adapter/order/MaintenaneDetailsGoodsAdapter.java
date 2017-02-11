package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.order.MaintenanceOrderDetailBean;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/7.
 * 保养订单详情--保养项目--配件
 */
public class MaintenaneDetailsGoodsAdapter extends BaseAdapter {
    private ArrayList<MaintenanceOrderDetailBean.MaintenanceItem.MaintenanceItemGoods> mMaintenanceItemGoodses;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MaintenaneDetailsGoodsAdapter(ArrayList<MaintenanceOrderDetailBean.MaintenanceItem.MaintenanceItemGoods> mMaintenanceItemGoodses, Context mContext) {
        this.mMaintenanceItemGoodses = mMaintenanceItemGoodses;
        this.mContext = mContext;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMaintenanceItemGoodses.size();
    }

    @Override
    public Object getItem(int position) {
        return mMaintenanceItemGoodses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
         if(convertView==null)
         {
             convertView=mLayoutInflater.inflate(R.layout.activity_my_order_details_maintain_good_item,parent,false);
             mViewHolder=new ViewHolder();
             mViewHolder.mNameView= (TextView) convertView.findViewById(R.id.myorder_details_maintenance_goods_name);
             mViewHolder.mPriceView= (TextView) convertView.findViewById(R.id.myorder_details_maintenance_goods_price);
             mViewHolder.mThumbView= (ImageView) convertView.findViewById(R.id.myorder_details_maintenance_goods_thumb);
             mViewHolder.mCountView= (TextView) convertView.findViewById(R.id.myorder_details_maintenance_goods_count);
             convertView.setTag(mViewHolder);
         }else {
             mViewHolder= (ViewHolder) convertView.getTag();
         }
        mViewHolder.mNameView.setText(mMaintenanceItemGoodses.get(position).name);
        mViewHolder.mPriceView.setText(String.format("配件价格：%s",OtherUtil.amountFormat(mMaintenanceItemGoodses.get(position).price,true)));
        mViewHolder.mCountView.setText(String.format("数量：%s",mMaintenanceItemGoodses.get(position).count));
        if (!TextUtils.isEmpty(mMaintenanceItemGoodses.get(position).thumb))
            ImageUtil.setImage(mContext, mViewHolder.mThumbView, mMaintenanceItemGoodses.get(position).thumb);
        return convertView;
    }

    class ViewHolder{
        ImageView mThumbView;
        TextView mNameView;
        TextView mPriceView;
        TextView mCountView;
    }
}
