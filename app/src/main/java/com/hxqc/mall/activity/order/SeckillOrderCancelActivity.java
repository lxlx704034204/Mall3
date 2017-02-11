package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseCancelActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;

/**
 * liaoguilong
 * 2016年6月22日 15:49:55
 * 特价车取消订单
 */
public class SeckillOrderCancelActivity extends BaseCancelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void toCancel(String cancelText) {
        new ThirdPartShopClient().cancelSeckillOrder(getIntent().getStringExtra(ActivitySwitcher.ORDER_ID),cancelText, new LoadingAnimResponseHandler(SeckillOrderCancelActivity.this) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(SeckillOrderCancelActivity.this, "取消订单成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new SharedPreferencesHelper(SeckillOrderCancelActivity.this).setOrderChange(true);
                        SeckillOrderCancelActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }
}
