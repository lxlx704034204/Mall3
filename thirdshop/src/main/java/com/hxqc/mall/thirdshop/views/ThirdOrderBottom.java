package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ThirdOrderModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;


/**
 * Function:第三方订单详情页底部的状态栏
 *
 * @author 袁秉勇
 * @since 2015年12月01日
 */
public class ThirdOrderBottom extends RelativeLayout {
    Context mContext;
    View rootView;
    Button mOrderOperateView;//按钮
    TextView mPaidTextView;
    TextView mPaidView;
    TextView mOrderPriceView;

    public ThirdOrderBottom(Context context) {
        super(context);
    }

    public ThirdOrderBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.t_view_order_bottom, this);
        mOrderPriceView = (TextView) findViewById(R.id.order_price);
        mPaidTextView = (TextView) findViewById(R.id.paid_text);
        mPaidView = (TextView) findViewById(R.id.paid);
        mOrderOperateView = (Button) findViewById(R.id.order_operate);
    }

    public void initBottom(final ThirdOrderModel thirdOrderModel) {
        if (thirdOrderModel.orderStatusCode == 10 || thirdOrderModel.orderStatusCode == 20) {
            mPaidView.setVisibility(VISIBLE);
            mPaidTextView.setVisibility(VISIBLE);
            mPaidView.setText(OtherUtil.amountFormat(thirdOrderModel.amount, false));
        } else if (thirdOrderModel.orderStatusCode == 0) {
//            if (OtherUtil.date2TimeStamp(thirdOrderModel.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") < OtherUtil.date2TimeStamp(thirdOrderModel.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30".equals(thirdOrderModel.promotion.status) || !thirdOrderModel.promotion.getPaymentAvailable()) {
            if (OtherUtil.date2TimeStamp(thirdOrderModel.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") < OtherUtil.date2TimeStamp(thirdOrderModel.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30".equals(thirdOrderModel.promotion.status)) {
                return;
            }
            mOrderPriceView.setVisibility(VISIBLE);
            mOrderPriceView.setText("订金：" + OtherUtil.amountFormat(thirdOrderModel.amount, true));
            mOrderOperateView.setVisibility(VISIBLE);
            mOrderOperateView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcherThirdPartShop.toPayDeposit(thirdOrderModel.subscription, thirdOrderModel.orderID, thirdOrderModel.shopTel, mContext);
                }
            });
        }
    }
}
