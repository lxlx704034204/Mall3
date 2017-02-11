package com.hxqc.mall.thirdshop.accessory.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryOrderDetailBean;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.util.JSONUtils;

/**
 * 说明:确认订单
 *
 * @author: 吕飞
 * @since: 2016-06-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class AccessoryShopPayFinishActivity extends NoBackActivity {
    String mOrderID;
    TextView mMoneyView;
    TextView mNameView;
    TextView mPhoneView;
    TextView mShopView;
    Button mCompleteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_shop_pay_finish);
        mOrderID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory.ORDER_ID);
        mMoneyView = (TextView) findViewById(R.id.money);
        mNameView = (TextView) findViewById(R.id.name);
        mPhoneView = (TextView) findViewById(R.id.phone);
        mShopView = (TextView) findViewById(R.id.shop);
        mCompleteView = (Button) findViewById(R.id.complete);
        getData();
        mCompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(AccessoryShopPayFinishActivity.this, 2);
            }
        });
    }

    private void getData() {
        new AccessoryApiClient().getAccessoryOrderDetail(mOrderID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                AccessoryOrderDetailBean orderDetail = JSONUtils.fromJson(response, AccessoryOrderDetailBean.class);
                mMoneyView.setText("金额：" + OtherUtil.amountFormat(orderDetail.orderAmount, true));
                mNameView.setText(orderDetail.contactName);
                mPhoneView.setText(orderDetail.contactPhone);
                mShopView.setText(orderDetail.shopTitle);
            }
        });
    }


}
