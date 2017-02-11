package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.payment.activity.PayBaseActivity;
import com.hxqc.mall.payment.control.PaymentControl;
import com.hxqc.mall.payment.model.PaymentMethod;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;


/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 25
 * Des: 周边支付方式
 * FIXME
 * Todo
 */
public class PeripheralServicesPayActivity extends PayBaseActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private String imMoney;//金额
    private String imOrderID;//订单号
    private String imPaymentID;
    private int imTypeChoose;//类型选择
    private String imExemption;

    @Override
    protected View getTileView() {
        if (getIntent() != null) {
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            imMoney = bundleExtra.getString("money");
            imOrderID = bundleExtra.getString("orderID");
            imPaymentID = bundleExtra.getString("paymentID");
            imTypeChoose = bundleExtra.getInt("typeChoose", -1);
            imExemption = bundleExtra.getString("exemption", "-1");
            DebugLog.i(TAG, imMoney + "----" + imOrderID + "----" + imPaymentID + "----" + imExemption);
//            imMoney = getIntent().getStringExtra("money");
//            imOrderID = getIntent().getStringExtra("orderID");
//            imPaymentID = getIntent().getStringExtra("paymentID");
//            imMoney = "1";
        }
        View view = LayoutInflater.from(this).inflate(R.layout.activity_illegal_payment_head, null);
        TextView mOrderIDView = (TextView) view.findViewById(R.id.payment_head_order_id);
//        mOrderIDView.setText("订单号:  " + imOrderID + "金额:" + imMoney);
        mOrderIDView.setText("订单号:  " + imOrderID);

        /*if (imTypeChoose == OrderDetailContants.PAY_PROMPT) {
            showPayPrompt();
        }*/
        return view;
    }

    /**
     * 获取支付列表
     */
    @Override
    protected void getPayList() {
        PaymentControl.getInstance().getPayment(this, imOrderID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                ArrayList<PaymentMethod> paymentMethods = JSONUtils.fromJson(response, new TypeToken<ArrayList<PaymentMethod>>() {
                });
                if (paymentMethods != null && paymentMethods.size() > 0) {
                    showPayList(paymentMethods, imMoney, "支付金额：" + OtherUtil.amountFormat(imMoney, true));
                    DebugLog.i(TAG, "flagActivity: " + imTypeChoose);
                    if (imTypeChoose == OrderDetailContants.FLAG_ACTIVITY_LICENSE) {
                        showPayPrompt("");
                    } else if (imTypeChoose == OrderDetailContants.FLAG_ACTIVITY_VEHICLES) {
                        try {
                            if (imExemption.equals("0")) {
                                String content = "        " + getString(R.string.vehicles_out_date);
                                showPayPrompt(content);
                            } else if (imExemption.equals("1")) {
                                String content = "        " + getString(R.string.vehicles_in_date);
                                showPayPrompt(content);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            DebugLog.e(TAG, e.toString());
                        }
                    }
                } else {
                    mRequestFailView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mRequestFailView.setVisibility(View.VISIBLE);
            }
        });
    }

//    @Override
//    protected void exit() {
//        new BaseSharedPreferencesHelper(this).setOrderChange(true);
//        ActivitySwitchBase.toMain(this, 2);
//    }

    @Override
    public void balancePay(String pwd) {
        PaymentControl.getInstance().postBlancePay(imOrderID, mPaymentTypeChoiceView.getPaymentID(), pwd, getBalancePayResponseHandler());
    }

    /**
     * 去支付
     */
    @Override
    public void pay() {

        PaymentControl.getInstance().postPay(imOrderID, mPaymentTypeChoiceView.getPaymentID(),
                imMoney, getPayResponseHandler());
    }

    /**
     * 完成支付操作·
     */
    @Override
    public void toFinishPay() {
        if (imTypeChoose == OrderDetailContants.ILLEGAL_AND_COMMISSION || imTypeChoose == OrderDetailContants.FLAG_ACTIVITY_LICENSE || imTypeChoose == OrderDetailContants.FLAG_ACTIVITY_VEHICLES) {
            ActivitySwitchAround.toPayFinishActivity(this);
        } else if (imTypeChoose == OrderDetailContants.CAR_WASH) {
            CarWashActivitySwitcher.toWashCarPayFinish(this, imOrderID);
        }
    }

    @Override
    public void offlinePay() {

    }

    private AlertDialog alertDialog;

    /**
     * 支付前提示弹窗
     */
    private void showPayPrompt(String content) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_show_pay_prompt, null);
        TextView toPayView = (TextView) view.findViewById(R.id.pay_prompt_to_pay);
        TextView toContentView = (TextView) view.findViewById(R.id.pay_prompt_to_content);
        if (!TextUtils.isEmpty(content)) {
            toContentView.setText(content);
        }
        toPayView.setOnClickListener(toPayClickListener);
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this, R.style.MMaterialDialog)
                    .setView(view)
                    .show();
        } else {
            alertDialog.show();
        }
    }

    private View.OnClickListener toPayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        showExitDialog();
        return false;
    }

    private void showExitDialog() {
        new NormalDialog(this, "确认要离开付款页面？", "") {
            @Override
            protected void doNext() {
                new BaseSharedPreferencesHelper(PeripheralServicesPayActivity.this).setOrderChange(true);
                ActivitySwitchBase.toMain(PeripheralServicesPayActivity.this, 2);
                finish();
            }
        }.show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}
