package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseCancelActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;

/**
 * liaoguilong
 * 2016年5月16日 15:38:27
 * 门店活动取消订单
 */
public class ThirdShopOrderCancelActivity extends BaseCancelActivity {

    public final static String ORDER_ID="orderID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void toCancel(String cancelText) {
        new ThirdPartShopClient().cancelOrder(getIntent().getStringExtra(ORDER_ID), cancelText, new DialogResponseHandler(ThirdShopOrderCancelActivity.this, "正在提交,请稍等") {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(getApplicationContext(), "订单已取消");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new BaseSharedPreferencesHelper(ThirdShopOrderCancelActivity.this).setOrderChange(true);
                        ThirdShopOrderCancelActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }
}
