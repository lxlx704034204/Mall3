package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseRefundActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;

import hxqc.mall.R;

/**
 * liaoguilong
 * 2016年5月16日 15:38:15
 * 特价车退款
 */
public class SeckillOrderRefundActivity extends BaseRefundActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void toRefund(String refundText) {
        new ThirdPartShopClient().refundSeckillOrder(getIntent().getStringExtra(ActivitySwitcher.ORDER_ID), refundText, new DialogResponseHandler(SeckillOrderRefundActivity.this, getResources().getString(R.string.me_submitting)) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(SeckillOrderRefundActivity.this, "退款申请成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new SharedPreferencesHelper(SeckillOrderRefundActivity.this).setOrderChange(true);
                        SeckillOrderRefundActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }

}
