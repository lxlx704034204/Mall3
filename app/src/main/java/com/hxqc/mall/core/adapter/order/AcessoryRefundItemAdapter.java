package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryOrderDetailBean;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * liaogulong
 * Created by CPR113 on 2016/6/7.
 * 用品订单详情-退款信息项
 */
public class AcessoryRefundItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<AccessoryOrderDetailBean.RefundInfo> mRefundInfos;

    public AcessoryRefundItemAdapter(Context context,ArrayList<AccessoryOrderDetailBean.RefundInfo> refundInfos) {
        this.mContext = context;
        this.mRefundInfos=refundInfos;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRefundInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mRefundInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder viewHolder;
        if(v==null)
        {
            v=mLayoutInflater.inflate(R.layout.ativity_my_order_acessory_refund_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.mDotLineView=  v.findViewById(R.id.myorder_aessory_refund_item_dotLine);
            viewHolder.mRefundAmountView= (TextView) v.findViewById(R.id.myorder_aessory_refund_item_refundAmount);
            viewHolder.mRefundStatusView= (TextView) v.findViewById(R.id.myorder_aessory_refund_item_refundStatus);
            viewHolder.mRefundNumberView= (TextView) v.findViewById(R.id.myorder_aessory_refund_item_refundNumber);
            viewHolder.mRefundTimeView= (TextView) v.findViewById(R.id.myorder_aessory_refund_item_refundTime);
            viewHolder.mRefundDescriptionView= (TextView) v.findViewById(R.id.myorder_aessory_refund_item_refundDescription);
            v.setTag(viewHolder);
        }else
        {
            viewHolder= (ViewHolder) v.getTag();
        }
        if(position==0)
            viewHolder.mDotLineView.setVisibility(View.GONE);
         else
            viewHolder.mDotLineView.setVisibility(View.VISIBLE);

        viewHolder.mRefundAmountView.setText(OtherUtil.amountFormat(mRefundInfos.get(position).refundAmount,true));
        viewHolder.mRefundStatusView.setText(mRefundInfos.get(position).refundStatusText);
        viewHolder.mRefundDescriptionView.setText(mRefundInfos.get(position).refundDescription);
        viewHolder.mRefundTimeView.setText(mRefundInfos.get(position).refundTime);
        viewHolder.mRefundNumberView.setText(mRefundInfos.get(position).refundNumber);
        return v;
    }

    class  ViewHolder{
        View mDotLineView;
        TextView mRefundAmountView;//退款金额 number
        TextView  mRefundTimeView;//退款时间 y-m-d h:m:s string
        TextView  mRefundStatusView;//退款状态
        TextView mRefundNumberView;//退款单号 string
        TextView mRefundDescriptionView;//退款原因描述 string
    }
}
