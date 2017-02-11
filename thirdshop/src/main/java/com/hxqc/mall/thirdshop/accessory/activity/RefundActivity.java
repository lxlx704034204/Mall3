package com.hxqc.mall.thirdshop.accessory.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BaseRefundActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;

/**
 * 说明:用品退款
 *
 * @author: 吕飞
 * @since: 2016-03-29
 * Copyright:恒信汽车电子商务有限公司
 */
public class RefundActivity extends BaseRefundActivity {
    String mOrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory.ORDER_ID);
    }

    @Override
    protected void toRefund(String refundText) {
        new AccessoryApiClient().refund(mOrderID, refundText, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {

                new BaseSharedPreferencesHelper(RefundActivity.this).setOrderChange(true);
                finish();
            }
        });
    }
}
