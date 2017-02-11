package com.hxqc.fastreqair.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CarWashOrderDetailsBean;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.JSONUtils;

import hxqc.mall.R;

/**
 * @Author : 钟学东
 * @Since : 2016-06-16
 * FIXME
 * Todo 洗车支付完成界面
 */
public class WashCarPayFinishActivity  extends NoBackActivity{

    private String orderID;

    private RelativeLayout mRlShopView;
    private TextView mShopView;
    private RelativeLayout mRlPayView;
    private TextView mPayView;
    private RelativeLayout mRlMoneyView;
    private TextView mMoneyView;
    private RelativeLayout mRlTimeView;
    private TextView mTimeView;

    private Button mFinishView;

    private CarWashApiClient carWashApiClient;
    private CarWashOrderDetailsBean carWashOrderDetailsBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_car_pay_finish);
        carWashApiClient = new CarWashApiClient();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
        orderID = bundle.getString("orderID");

        initView();
        getDate();
        initEvnt();
    }

    private void initEvnt() {
        mFinishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(WashCarPayFinishActivity.this, 2);
            }
        });
    }

    private void getDate() {
        carWashApiClient.getCarWashDetail(orderID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                carWashOrderDetailsBean = JSONUtils.fromJson(response,new TypeToken<CarWashOrderDetailsBean>(){});
                initDate(carWashOrderDetailsBean);
            }
        });
    }

    private void initDate(CarWashOrderDetailsBean carWashOrderDetailsBean) {
        if(TextUtils.isEmpty(carWashOrderDetailsBean.shopName)){
            mRlShopView.setVisibility(View.GONE);
        }else {
            mShopView.setText(carWashOrderDetailsBean.shopName);
        }

        if(TextUtils.isEmpty(carWashOrderDetailsBean.paymentID)){
            mRlPayView.setVisibility(View.GONE);
        }else {
            mPayView.setText(carWashOrderDetailsBean.paymentID);
        }

        if (TextUtils.isEmpty(carWashOrderDetailsBean.actualPayment)){
            mRlMoneyView.setVisibility(View.GONE);
        }else {
            mMoneyView.setText(OtherUtil.amountFormat(carWashOrderDetailsBean.actualPayment,true));
        }

        if(TextUtils.isEmpty(carWashOrderDetailsBean.orderCreatTime)){
            mRlTimeView.setVisibility(View.GONE);
        }else {
            mTimeView.setText(carWashOrderDetailsBean.orderCreatTime);
        }
    }

    private void initView() {
        mRlShopView = (RelativeLayout) findViewById(R.id.rl_shop);
        mShopView = (TextView) findViewById(R.id.shop);
        mRlPayView = (RelativeLayout) findViewById(R.id.rl_pay);
        mPayView = (TextView) findViewById(R.id.pay);
        mRlMoneyView = (RelativeLayout) findViewById(R.id.rl_money);
        mMoneyView = (TextView) findViewById(R.id.money);
        mRlTimeView = (RelativeLayout) findViewById(R.id.rl_time);
        mTimeView = (TextView) findViewById(R.id.time);
        mFinishView = (Button) findViewById(com.hxqc.mall.thirdshop.R.id.reserve_success_finish_btn);
    }


}
