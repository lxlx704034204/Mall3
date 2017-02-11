
package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.auto.adapter.BaseRecyclerAdapter;
import com.hxqc.mall.payment.util.PaymentActivitySwitch;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 23
 * FIXME
 * Todo 我的违章订单
 */
public class MyQueryOrderAdapter extends BaseRecyclerAdapter<IllegalOrderDetail> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public MyQueryOrderAdapter(Context context, ArrayList<IllegalOrderDetail> mDatas) {
        super(mDatas);
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_my_illegal_order, parent, false);
        ChildViewHolder viewHolder = new ChildViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, IllegalOrderDetail data) {
        ChildViewHolder childViewHolder = (ChildViewHolder) viewHolder;
        childViewHolder.mInfoView.setOnClickListener(clickInfoListener);
        childViewHolder.mPayView.setOnClickListener(clickPayListener);
    }

    private View.OnClickListener clickPayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PaymentActivitySwitch.toAroundPaymentActivity(mContext,"","", OrderDetailContants.ILLEGAL_AND_COMMISSION,"-1");
        }
    };

    private View.OnClickListener clickInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAround.toOrderDetailActivity(mContext,"", "empty");
        }
    };

    class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView mNumberView;
        private TextView mStateView;
        private ImageView mBGView;
        private TextView mPlateNumView;
        private TextView mServiceView;
        private Button mPayView;
        private TextView mMoneyView;
        private LinearLayout mInfoView;

        public ChildViewHolder(View itemView) {
            super(itemView);
            mInfoView = (LinearLayout) itemView.findViewById(R.id.illegal_order_info);
            mNumberView = (TextView) itemView.findViewById(R.id.illegal_order_number);
            mStateView = (TextView) itemView.findViewById(R.id.illegal_order_state);
            mBGView = (ImageView) itemView.findViewById(R.id.illegal_order_bg);
            mPlateNumView = (TextView) itemView.findViewById(R.id.illegal_order_plate_number);
            mServiceView = (TextView) itemView.findViewById(R.id.illegal_order_service);
            mMoneyView = (TextView) itemView.findViewById(R.id.illegal_order_money);
            mPayView = (Button) itemView.findViewById(R.id.illegal_order_pay);
        }
    }
}
