package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseCancelActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;

/**
 * liaoguilong
 * 2016年6月22日 15:49:55
 * 用品取消订单
 */
public class Accessory4SShopOrderCancelActivity extends BaseCancelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void toCancel(String cancelText) {
        new Accessory4SApiClient().cancelOrder(cancelText,getIntent().getStringExtra(ActivitySwitcher.ORDER_ID) , new LoadingAnimResponseHandler(Accessory4SShopOrderCancelActivity.this) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(Accessory4SShopOrderCancelActivity.this, "订单已取消");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new SharedPreferencesHelper(Accessory4SShopOrderCancelActivity.this).setOrderChange(true);
                        Accessory4SShopOrderCancelActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }
}
