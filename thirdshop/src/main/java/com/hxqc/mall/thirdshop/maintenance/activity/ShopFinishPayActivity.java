package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.order.MaintainOrderDetail;
import com.hxqc.util.JSONUtils;

/**
 * @Author : 钟学东
 * @Since : 2016-04-28
 * FIXME
 * Todo
 */
public class ShopFinishPayActivity extends NoBackActivity {

    private TextView mMoneyView;
    private TextView mNameView;
    private TextView mPhoneView;
    private TextView mShopView;
    private TextView mPayWayView;
    private RelativeLayout mRlMaintainTimeView;
    private TextView mMaintainTimeView;
//    private RelativeLayout mRlServiceView;
//    private TextView mServiceView;
//    private RelativeLayout mRlMechanicView;
//    private TextView mMechanicView;
    private Button mFinishView;

    private String orderID;
    private String flag;

    private MaintenanceClient maintenanceClient;
    private MaintainOrderDetail maintainOrderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_finish_pay);
        maintenanceClient = new MaintenanceClient();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
        orderID = bundle.getString("orderID");
        flag = bundle.getString("flag");
        initView();
        getDate();
        initEvent();

    }

    private void initEvent() {
        mFinishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(ShopFinishPayActivity.this, 2);
            }
        });
    }

    private void getDate() {
        maintenanceClient.orderMaintenanceDetail(orderID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                maintainOrderDetail = JSONUtils.fromJson(response, new TypeToken< MaintainOrderDetail >() {
                });
                if(maintainOrderDetail != null){
                    initDate(maintainOrderDetail);
                }
            }
        });
    }

    private void initDate(MaintainOrderDetail maintainOrderDetail) {
        mMoneyView.setText(OtherUtil.amountFormat(maintainOrderDetail.orderAmount, true));
        mNameView.setText(maintainOrderDetail.userFullname);
        mPhoneView.setText(maintainOrderDetail.userPhoneNumber);
        mShopView.setText(maintainOrderDetail.shopPoint.shopName);
        if(flag.equals("1")){
            mPayWayView.setText("优惠抵扣");
        }else {
            mPayWayView.setText("到店支付");
        }
        if (maintainOrderDetail.shopType == 10) { //10 4S店 20快修店
            mMaintainTimeView.setText(maintainOrderDetail.appointmentDate);
//            mServiceView.setText(maintainOrderDetail.serviceAdviser.name);
//            mMechanicView.setText(maintainOrderDetail.mechanic.name);
        } else {
            mRlMaintainTimeView.setVisibility(View.GONE);
//            mRlServiceView.setVisibility(View.GONE);
//            mRlMechanicView.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mMoneyView = (TextView) findViewById(R.id.money);
        mNameView = (TextView) findViewById(R.id.name);
        mPhoneView = (TextView) findViewById(R.id.phone);
        mShopView = (TextView) findViewById(R.id.shop);
        mPayWayView = (TextView) findViewById(R.id.pay_way);
        mRlMaintainTimeView = (RelativeLayout) findViewById(R.id.rl_maintain_time);
        mMaintainTimeView = (TextView) findViewById(R.id.maintain_time);
//        mRlServiceView = (RelativeLayout) findViewById(R.id.rl_serve);
//        mServiceView = (TextView) findViewById(R.id.serve);
//        mRlMechanicView = (RelativeLayout) findViewById(R.id.rl_mechanic);
//        mMechanicView = (TextView) findViewById(R.id.mechanic);
        mFinishView = (Button) findViewById(R.id.reserve_success_finish_btn);
    }

    @Override
    public void onBackPressed() {

    }
}
