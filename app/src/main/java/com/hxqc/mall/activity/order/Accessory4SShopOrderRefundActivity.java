package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseRefundActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;

/**
 * liaoguilong
 * 2016年6月22日 15:49:55
 * 用品申请退款
 */
public class Accessory4SShopOrderRefundActivity extends BaseRefundActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void toRefund(String refundText) {
        new Accessory4SApiClient().refund(getIntent().getStringExtra(ActivitySwitcher.ORDER_ID) ,refundText, new LoadingAnimResponseHandler(Accessory4SShopOrderRefundActivity.this) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(Accessory4SShopOrderRefundActivity.this, "申请退款成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new SharedPreferencesHelper(Accessory4SShopOrderRefundActivity.this).setOrderChange(true);
                        Accessory4SShopOrderRefundActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }
}
