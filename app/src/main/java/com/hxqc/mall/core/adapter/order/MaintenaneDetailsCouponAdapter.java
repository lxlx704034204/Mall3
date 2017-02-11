package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.order.MaintenanceOrderDetailBean;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/7.
 * 优惠卷adapter
 */
public class MaintenaneDetailsCouponAdapter extends BaseAdapter {
     private ArrayList<MaintenanceOrderDetailBean.MaintenaneCoupon> mMaintenaneCoupons;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MaintenaneDetailsCouponAdapter(ArrayList<MaintenanceOrderDetailBean.MaintenaneCoupon> mMaintenaneCoupons, Context mContext) {
        this.mMaintenaneCoupons = mMaintenaneCoupons;
        this.mContext = mContext;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMaintenaneCoupons.size();
    }

    @Override
    public Object getItem(int position) {
        return mMaintenaneCoupons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if(convertView ==null)
        {
            convertView=mLayoutInflater.inflate(R.layout.activity_my_order_maintain_details_coupon_item,parent,false);
            mViewHolder=new ViewHolder();
            mViewHolder.mPreferentialDescriptionView= (TextView) convertView.findViewById(R.id.myorder_details_maintenance_coupon_preferentialDescription);
            mViewHolder.mPriceView= (TextView) convertView.findViewById(R.id.myorder_details_maintenance_coupon_price);
            mViewHolder.mLine=convertView.findViewById(R.id.myorder_details_maintenance_coupon_line);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        if(position==0)
            mViewHolder.mLine.setVisibility(View.GONE);
        else
            mViewHolder.mLine.setVisibility(View.VISIBLE);

        mViewHolder.mPreferentialDescriptionView.setText(mMaintenaneCoupons.get(position).preferentialDescription);
        mViewHolder.mPriceView.setText(String.format("抵扣 %s", OtherUtil.amountFormat(mMaintenaneCoupons.get(position).price,true)));
        return convertView;
    }

    class ViewHolder{
        TextView mPreferentialDescriptionView;
        TextView mPriceView;
        View mLine;
    }
}
