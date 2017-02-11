package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.ExpressDetail;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:物流数据
 *
 * author: 吕飞
 * since: 2015-06-02
 * Copyright:恒信汽车电子商务有限公司
 */
public class ExpressAdapter extends RecyclerView.Adapter {
    ArrayList<ExpressDetail > mExpressDetails;
    Context mContext;

    public ExpressAdapter(ArrayList<ExpressDetail> mExpressDetails, Context mContext) {
        this.mExpressDetails = mExpressDetails;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_express, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpressDetail mExpressDetail = mExpressDetails.get(position);
        if (position == 0) {
            ((ViewHolder) holder).mCircleView.setImageResource(R.drawable.ic_timeline_dot_red);
            ((ViewHolder) holder).mExpressStatusView.setTextColor(mContext.getResources().getColor(R.color.title_and_main_text));
        } else {
            ((ViewHolder) holder).mCircleView.setImageResource(R.drawable.ic_timeline_dot);
            ((ViewHolder) holder).mExpressStatusView.setTextColor(mContext.getResources().getColor(R.color.straight_matter_and_secondary_text));
        }
        ((ViewHolder) holder).mTimeView.setText(mExpressDetail.orderStatusTime);
        ((ViewHolder) holder).mExpressStatusView.setText(mExpressDetail.orderStatus);
        int[] mBackground = {mContext.getResources().getColor(R.color.window_color), mContext.getResources().getColor(R.color.express_background
        )};
        ((ViewHolder) holder).mItemExpressView.setBackgroundColor(mBackground[position % 2]);
    }

    @Override
    public int getItemCount() {
        return mExpressDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTimeView;//时间
        TextView mExpressStatusView;//物流状态
        ImageView mCircleView;//红圈圈
        RelativeLayout mItemExpressView;//整个item

        public ViewHolder(View v) {
            super(v);
            mCircleView = (ImageView) v.findViewById(R.id.circle);
            mTimeView = (TextView) v.findViewById(R.id.time);
            mExpressStatusView = (TextView) v.findViewById(R.id.express_status);
            mItemExpressView = (RelativeLayout) v.findViewById(R.id.item_express);
        }
    }
}
