package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseRefundActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;

import hxqc.mall.R;

/**
 * liaoguilong
 * 2016年5月16日 15:38:15
 * 保养退款
 */
public class MaintainOrderRefundActivity extends BaseRefundActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void toRefund(String refundText) {
        new MaintenanceClient().refundOrder(getIntent().getStringExtra(ActivitySwitcher.ORDER_ID), refundText, new DialogResponseHandler(MaintainOrderRefundActivity.this, getResources().getString(R.string.me_submitting)) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(MaintainOrderRefundActivity.this, "申请退款成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new SharedPreferencesHelper(MaintainOrderRefundActivity.this).setOrderChange(true);
                        MaintainOrderRefundActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }

}
