package com.hxqc.newenergy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.newenergy.bean.EVNewenergyAutoSample;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2016/5/25
 * FIXME
 * Todo
 */
public class RecommendView extends LinearLayout {
    private ImageView mCarIco;
    private TextView mCarName;
    private TextView mCarPrice;
    private TextView mTotalPrice;
    private TextView mBatteryLife;

    public RecommendView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_ev_newwenergyrecommended_listview_adapter, this);
        mCarIco = (ImageView) findViewById(R.id.car_ico);
        mCarName = (TextView) findViewById(R.id.car_name);
        mCarPrice = (TextView) findViewById(R.id.car_price);
        mTotalPrice = (TextView) findViewById(R.id.car_TotalPrice);
        mBatteryLife = (TextView) findViewById(R.id.car_batteryLife);
    }

    public RecommendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_ev_newwenergyrecommended_listview_adapter, this);
        mCarIco = (ImageView) findViewById(R.id.car_ico);
        mCarName = (TextView) findViewById(R.id.car_name);
        mCarPrice = (TextView) findViewById(R.id.car_price);
        mTotalPrice = (TextView) findViewById(R.id.car_TotalPrice);
        mBatteryLife = (TextView) findViewById(R.id.car_batteryLife);
    }

    public RecommendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(final EVNewenergyAutoSample autoSample) {
        ImageUtil.setImage(getContext(), mCarIco, autoSample.itemThumb);
        mCarName.setText(autoSample.itemName);
        mCarPrice.setText(OtherUtil.amountFormat(autoSample.itemPrice, true));
        mTotalPrice.setText(OtherUtil.amountFormat(autoSample.itemTotalPrice, true));
        mBatteryLife.setText(String.format("%s公里", autoSample.batteryLife));


        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAutoItemDetail(getContext(), "0", autoSample.itemID, "车辆详情");
            }
        });
    }
}
