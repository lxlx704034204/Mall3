package com.hxqc.qr.offlinepay;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.recharge.RechargePayBaseActivity;
import com.hxqc.mall.auto.util.ScreenUtil;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.payment.control.PaymentControl;
import com.hxqc.qr.util.QRActivitySwitchApp;
import com.hxqc.util.OtherUtil;

import hxqc.mall.R;

/**
 * Author:  wh
 * Date:  2016/11/16
 * FIXME
 * Todo
 */

public class HomeQRPayChargeActivity extends RechargePayBaseActivity {

    @Override
    public String getDialogTitle() {
        return null;
    }

    @Override
    public String getDialogContent() {
        return "离开后付款信息将不保存，请尽快完成支付";
    }

    @Override
    public View getChildHeadView() {
        TextView textView = new TextView(this);
        textView.setText(String.format("工单号：%s", rechargeRequest.workOrderID));
        textView.setTextColor(getResources().getColor(R.color.green_txt));
        textView.setTextSize(20);
        int padding = ScreenUtil.dip2px(this, 16);
        textView.setPadding(padding, padding, padding, padding);
        textView.setBackgroundColor(Color.parseColor("#efefef"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        return textView;
    }

    @Override
    protected void getPayList() {
        PaymentControl.getInstance().getPayment(this, rechargeRequest.orderID,
                getPayListResponseHandler(rechargeRequest.charge_number, "支付金额：" + OtherUtil.amountFormat(rechargeRequest.charge_number, true)));
    }

    @Override
    public void pay() {
        PaymentControl.getInstance().postPay(rechargeRequest.orderID, mPaymentTypeChoiceView.getPaymentID(),
                rechargeRequest.charge_number, getPayResponseHandler());
    }

    @Override
    public void toFinishPay() {
        QRActivitySwitchApp.toQRRechargePayFinish(HomeQRPayChargeActivity.this);
    }

    @Override
    public void dialogDoNext() {
        ActivitySwitchBase.toMain(HomeQRPayChargeActivity.this, 0);
        finish();
    }


}
