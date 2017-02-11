package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.order.AccessoryBean;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/3.
 * 我的订单--用品--商品项
 */
public class AcessoryItemAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<AccessoryBean.AccessoryItem> mAccessoryItems;
    private LayoutInflater mLayoutInflater;

    public AcessoryItemAdapter(Context mContext, ArrayList<AccessoryBean.AccessoryItem> mAccessoryItems) {
        this.mContext = mContext;
        this.mAccessoryItems = mAccessoryItems;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mAccessoryItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mAccessoryItems.get(position);
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
            v=mLayoutInflater.inflate(R.layout.ativity_my_order_acessory_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.mImageView= (ImageView) v.findViewById(R.id.myorder_aessory_item_imge);
            viewHolder.mNameView= (TextView) v.findViewById(R.id.myorder_aessory_item_name);
            viewHolder.mPicView= (TextView) v.findViewById(R.id.myorder_aessory_item_pic);
            viewHolder.mProductNum= (TextView) v.findViewById(R.id.myorder_aessory_item_productNum);
            viewHolder.mRefundStatusView= (TextView) v.findViewById(R.id.myorder_aessory_item_refund_status);
            viewHolder.mRefundNumView= (TextView) v.findViewById(R.id.myorder_aessory_item_refund_num);
            viewHolder.mRefundllyView= (LinearLayout) v.findViewById(R.id.myorder_aessory_item_refund_lly);
            v.setTag(viewHolder);
        }else
        {
            viewHolder= (ViewHolder) v.getTag();
        }
        viewHolder.mNameView.setText(mAccessoryItems.get(position).name);
        viewHolder.mPicView.setText(OtherUtil.amountFormat(mAccessoryItems.get(position).price, true));
        viewHolder.mProductNum.setText(String.format("商品数量：%s", mAccessoryItems.get(position).productNum));
        if(!TextUtils.isEmpty(mAccessoryItems.get(position).refundStatus) && !TextUtils.isEmpty(mAccessoryItems.get(position).refundStatusText) &&  !TextUtils.isEmpty(mAccessoryItems.get(position).refundCount)) {
            viewHolder.mRefundNumView.setText(String.format("退款数量：%s", mAccessoryItems.get(position).refundCount));
            viewHolder.mRefundStatusView.setText(mAccessoryItems.get(position).refundStatusText);
            OtherUtil.setVisible(viewHolder.mRefundllyView,true);
        }else {
            OtherUtil.setVisible(viewHolder.mRefundllyView,false);
        }
        ImageUtil.setImage(mContext, viewHolder.mImageView, mAccessoryItems.get(position).smallPhoto);

        return v;
    }

    class  ViewHolder{
          ImageView  mImageView;
          TextView  mNameView;
          TextView  mPicView;
         TextView mProductNum;
        TextView mRefundStatusView;
        TextView mRefundNumView;
        LinearLayout mRefundllyView;
    }
}
