package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.activity.BaseCancelActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;

import hxqc.mall.R;

/**
 * liaoguilong
 * 2016年5月16日 15:38:27
 * 修车预约取消订单
 */
public class RepairOrderCancelActivity extends BaseCancelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void toCancel(String cancelText) {
        new MaintenanceClient().reservationMaintainCancel(getIntent().getStringExtra(ActivitySwitcher.ORDER_ID),cancelText, new DialogResponseHandler(RepairOrderCancelActivity.this, getResources().getString(R.string.me_submitting)) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(RepairOrderCancelActivity.this, "取消订单成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new SharedPreferencesHelper(RepairOrderCancelActivity.this).setOrderChange(true);
                        RepairOrderCancelActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }
}
