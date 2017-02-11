package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseRefundActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;

/**
 * liaoguilong
 * 2016年5月16日 15:38:15
 * 门店订单详情申请退款
 */
public class ThirdShopOrderRefundActivity extends BaseRefundActivity {
    public final static String ORDER_ID="orderID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void toRefund(String refundText) {
        new ThirdPartShopClient().refundOrder(getIntent().getStringExtra(ORDER_ID), refundText, new DialogResponseHandler(ThirdShopOrderRefundActivity.this, "正在提交,请稍等") {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(getApplicationContext(), "申请成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new BaseSharedPreferencesHelper(ThirdShopOrderRefundActivity.this).setOrderChange(true);
                        ThirdShopOrderRefundActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }

}
