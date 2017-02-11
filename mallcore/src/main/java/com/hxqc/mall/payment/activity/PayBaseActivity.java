package com.hxqc.mall.payment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.payment.model.PaymentMethod;
import com.hxqc.mall.payment.view.PaymentTypeChoice;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayCallBackTag;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 说明:支付基础界面，使用参看PaySubscriptionActivity
 *
 * @author: 吕飞
 * @since: 2016-03-21
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class PayBaseActivity extends BackActivity implements PaymentTypeChoice.PaymentListener {
    protected LinearLayout mRootView;//跟布局
    protected PaymentTypeChoice mPaymentTypeChoiceView;//支付方式选择
    protected RequestFailView mRequestFailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_pay_base);
        mRootView = (LinearLayout) findViewById(R.id.root);
        mPaymentTypeChoiceView = (PaymentTypeChoice) findViewById(R.id.payment_type_choice);
        mRequestFailView = (RequestFailView) findViewById(R.id.fail_view);
        mRequestFailView.setFailButtonClick("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayList();
            }
        });
        mRequestFailView.setRequestType(RequestFailView.RequestViewType.fail);
        mPaymentTypeChoiceView.setPaymentListener(this);
        mRootView.addView(getTileView(), 0);
        getPayList();
    }

    protected abstract View getTileView();

    //销毁时，注销EventBus
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //接收返回结果显示
    @Subscribe
    public void onEventMainThread(EventGetSuccessModel event) {
//        DebugLog.i("pay_test",event.toString());
        if (event.getPay_status() == PayCallBackTag.PAY_SUCCESS) {
            mPaymentTypeChoiceView.finishPay();
        } else if (event.getPay_status() == PayCallBackTag.PAY_FAIL) {
            ToastHelper.showRedToast(this, "支付失败");
        } else if (event.getPay_status() == PayCallBackTag.PAY_CANCEL) {
            ToastHelper.showRedToast(this, "交易取消");
        } else if (event.getPay_status() == PayCallBackTag.PAY_EXCEPTION) {
            ToastHelper.showRedToast(this, "数据异常");
        } else if (event.getPay_status() == PayCallBackTag.PAY_PROGRESSING) {
            ToastHelper.showYellowToast(this, "支付结果确认中");
        }
    }

    //调用支付方式列表接口
    protected abstract void getPayList();


    public void showPayList(ArrayList<PaymentMethod> paymentMethods, String amount, String amountText) {
        mRequestFailView.setVisibility(View.GONE);
        mRootView.setVisibility(View.VISIBLE);
        mPaymentTypeChoiceView.fillData(paymentMethods, amount);
        mPaymentTypeChoiceView.setAmountText(amountText);
    }

    /**
     * 支付列表请求
     *
     * @param amount     支付金额
     * @param amountText 支付金额显示文字
     */
    protected LoadingAnimResponseHandler getPayListResponseHandler(final String amount, final String amountText) {
        return new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                ArrayList<PaymentMethod> paymentMethods = JSONUtils.fromJson(response, new TypeToken<ArrayList<PaymentMethod>>() {
                });
                if (paymentMethods != null && paymentMethods.size() > 0) {
                    showPayList(paymentMethods, amount, amountText);
                } else {
                    mRequestFailView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mRequestFailView.setVisibility(View.VISIBLE);
            }
        };
    }

    //支付
    protected LoadingAnimResponseHandler getPayResponseHandler() {
        return new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                mPaymentTypeChoiceView.toPay(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastHelper.showRedToast(PayBaseActivity.this, "支付失败，请稍后重试");
            }
        };
    }

    //余额支付
    protected LoadingAnimResponseHandler getBalancePayResponseHandler() {
        return new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                mPaymentTypeChoiceView.finishPay();
            }
        };
    }

    @Override
    public boolean onSupportNavigateUp() {
        showExitDialog();
        return false;
    }

    private void showExitDialog() {
        new NormalDialog(this, "确认要离开付款页面？", "下单后24小时订单将被取消，请尽快完成支付") {
            @Override
            protected void doNext() {
                new BaseSharedPreferencesHelper(PayBaseActivity.this).setOrderChange(true);
                ActivitySwitchBase.toMain(PayBaseActivity.this, 2);
                finish();
            }
        }.show();
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }

}
