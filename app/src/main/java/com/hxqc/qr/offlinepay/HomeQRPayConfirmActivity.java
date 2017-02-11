package com.hxqc.qr.offlinepay;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.model.recharge.RechargeRequest;
import com.hxqc.mall.qr.view.BalanceEditTextView;
import com.hxqc.mall.qr.model.OffLineWorkOrderQRModel;
import com.hxqc.qr.util.QRActivitySwitchApp;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * 首页扫码 支付线下付款  确认金额界面
 */
public class HomeQRPayConfirmActivity extends BackActivity {

    private OffLineWorkOrderQRModel model;
    private BalanceEditTextView mBalanceInputView;
    InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_qrpay_confirm);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        model = getIntent().getParcelableExtra(QRActivitySwitchApp.Scan_pay_tag);

        if (model == null) {
            initFailView();
        } else {
            initPayView();
        }
    }

    private void initFailView() {
        RequestFailView mFailView = (RequestFailView) findViewById(R.id.qr_pay_no_data);
        mFailView.showFailPageWithoutButton("获取数据失败");
    }

    private void initPayView() {
        TextView mOrderIDView = (TextView) findViewById(R.id.qr_pay_orderid);
        TextView mShopNameView = (TextView) findViewById(R.id.qr_pay_shop_name);
        TextView mTotalPayAmountView = (TextView) findViewById(R.id.tv_qr_pay_total);
        mBalanceInputView = (BalanceEditTextView) findViewById(R.id.betv_qr_input);

        mOrderIDView.setText(model.workOrderID);
        mShopNameView.setText(model.shopName);
        mTotalPayAmountView.setText(String.format("￥%s", model.amountPayable));
        mBalanceInputView.setHint(model.amountPayable);
    }

    /**
     * 点击确认订单
     */
    public void confirmOrder(View view) {
//        String payMoney = "0.01";
        String payMoney = mBalanceInputView.getPayMoney();
        String orderID = model.orderID;
        String workID = model.workOrderID;

        //提交时 对金额做判断 如果异常取可支付额度   如果大于
        try {
            double aDouble = Double.parseDouble(payMoney);
            if (aDouble > Double.parseDouble(model.amountPayable)) {
                payMoney = model.amountPayable;
            }
        } catch (Exception e) {
            payMoney = model.amountPayable;
        }

        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.charge_number = payMoney;
        rechargeRequest.orderID = orderID;
        rechargeRequest.workOrderID = workID;

        DebugLog.i("scan_code", payMoney + " --orderID--: " + orderID + " --workid--: " + workID);

        QRActivitySwitchApp.toQRRechargePayList(HomeQRPayConfirmActivity.this, rechargeRequest);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}
