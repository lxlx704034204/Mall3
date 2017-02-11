package com.hxqc.newenergy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.newenergy.bean.PromotionAuto;

import hxqc.mall.R;

/**
 * 说明: 新能源特卖view
 * author: 何玉
 * since: 2016/3/31.
 * Copyright:恒信汽车电子商务有限公司
 */
public class DealsView extends FrameLayout implements View.OnClickListener {

    private ImageView mCarIconView;
    private ImageView mStateImageView;
    private TextView mCareNameView;
    private TextView mCarPriceView;
    private TextView mCarDecreaseView;
    private TextView mCarStockView;
    private TextView mStateView;
    private TimeTextView mTime;
    private Thread mThread;

    public DealsView(Context context) {
        super(context);
        init();
    }

    public DealsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DealsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DealsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.item_ev_newwenergydeals_listview_adapter, this);
        mCarIconView = (ImageView) findViewById(R.id.car_ico);
        mStateImageView = (ImageView) findViewById(R.id.state);
        mCareNameView = (TextView) findViewById(R.id.car_name);
        mCarPriceView = (TextView) findViewById(R.id.car_price);
        mCarDecreaseView = (TextView) findViewById(R.id.car_decrease);
        mCarStockView = (TextView) findViewById(R.id.car_stock);
        mStateView = (TextView) findViewById(R.id.state_text);
        mTime = (TimeTextView) findViewById(R.id.car_time);

    }

    PromotionAuto mPromotionAuto;

    public View setData(@Nullable PromotionAuto mPromotionAuto) {
        if (mPromotionAuto == null) return this;
        this.mPromotionAuto = mPromotionAuto;
        ImageUtil.setImage(getContext(), mCarIconView, mPromotionAuto.itemPic);
        mCareNameView.setText(mPromotionAuto.itemName);
        mCarPriceView.setText(OtherUtil.amountFormat(mPromotionAuto.itemPrice, true));
        mCarDecreaseView.setText(OtherUtil.amountFormat(mPromotionAuto.decrease, true));
        mCarStockView.setText(String.format("%s辆", mPromotionAuto.stock));

        if (!mTime.isRun()) {
            mTime.setDate(mStateImageView, mTime, mStateView, mPromotionAuto.startTime,
                    mPromotionAuto.serverTime, mPromotionAuto.endTime);
        }
        setOnClickListener(this);
        return this;
    }

    public void stopTimer() {
        mTime.setRun(false);
    }
    
    @Override
    public void onClick(View v) {
        ActivitySwitcher.toAutoItemDetail(getContext(), "1", mPromotionAuto.itemID, "车辆详情");
    }
}
