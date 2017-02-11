package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.control.AnnualVehicleControl;
import com.hxqc.aroundservice.control.ChangeLicenseControl;
import com.hxqc.aroundservice.control.IllegalQueryControl;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.BaseCancelActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.util.DebugLog;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 05
 * Des: 取消订单
 * FIXME
 * Todo
 */
public class CancelOrderDetailActivity extends BaseCancelActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private String imOrderID;
    private String imFlagFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
//            imOrderID = getIntent().getStringExtra("orderID");
//            imFlagFragment = getIntent().getIntExtra("flagFragment", -1);

            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            imOrderID = bundleExtra.getString("orderID");
            imFlagFragment = bundleExtra.getString("flagFragment", "empty");
        }
    }

    @Override
    protected void toCancel(String cancelText) {
        if (imFlagFragment.equals(OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL)) {
            DebugLog.i(TAG, "FRAGMENT_ILLEGAL_ORDER_DETAIL");
            IllegalQueryControl.getInstance().cancelIllegalOrder(this, imOrderID, cancelText, new CallBackControl.CallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    ToastHelper.showGreenToast(getApplicationContext(), "订单已取消");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new SharedPreferencesHelper(CancelOrderDetailActivity.this).setOrderChange(true);
                            ActivitySwitchAround.toOrderDetailActivity(CancelOrderDetailActivity.this);
                            CancelOrderDetailActivity.this.finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 2300);
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        } else if (imFlagFragment.equals(OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL)) {
            DebugLog.i(TAG, "FRAGMENT_VEHICLES_ORDER_DETAIL");
            AnnualVehicleControl.getInstance().cancelAnnualnspection(this, imOrderID, cancelText, new CallBackControl.CallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    ToastHelper.showGreenToast(getApplicationContext(), "订单已取消");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new SharedPreferencesHelper(CancelOrderDetailActivity.this).setOrderChange(true);
                            ActivitySwitchAround.toOrderDetailActivity(CancelOrderDetailActivity.this);
                            CancelOrderDetailActivity.this.finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 2300);
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        } else if (imFlagFragment.equals(OrderDetailContants.FRAGMENT_LICENSE_ORDER_DETAIL)) {
            DebugLog.i(TAG, "FRAGMENT_LICENSE_ORDER_DETAIL");
            ChangeLicenseControl.getInstance().cancelLicenceDetail(this, imOrderID, cancelText, new CallBackControl.CallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    ToastHelper.showGreenToast(getApplicationContext(), "订单已取消");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new SharedPreferencesHelper(CancelOrderDetailActivity.this).setOrderChange(true);
                            ActivitySwitchAround.toOrderDetailActivity(CancelOrderDetailActivity.this);
                            CancelOrderDetailActivity.this.finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 2300);
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        }
    }
}
