package com.hxqc.mall.thirdshop.activity;

import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.payment.activity.PayBaseActivity;
import com.hxqc.mall.payment.api.PaymentClient;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

/**
 * 促销预付订金
 */
public class SalesDepositPayActivity extends PayBaseActivity {


    private PaymentClient apiClient;

    String orderID = "";
    String amount = "";
    String shopTel = "";

    @Override
    protected View getTileView() {
        orderID = getIntent().getStringExtra(ActivitySwitcherThirdPartShop.SHOP_ORDER_ID);
        amount = getIntent().getStringExtra(ActivitySwitcherThirdPartShop.DEPOSIT_AMOUNT);
        shopTel = getIntent().getStringExtra(ActivitySwitcherThirdPartShop.SHOP_TEL);
        DebugLog.w("4s_pay","getTileView:"+orderID+ " : "+amount+" : "+shopTel);
        apiClient = new PaymentClient();
        return initHead();
    }

    private View initHead() {
        View view = View.inflate(SalesDepositPayActivity.this,R.layout.view_deposit_head_t,null);
        TextView showOrderID = (TextView) view.findViewById(R.id.t_show_order_id);
        showOrderID.setText(String.format("订单号：%s", orderID));
        return view;
    }

    @Override
    protected void getPayList() {
        DebugLog.w("4s_pay","getTileView:"+orderID+ " : "+amount+" : "+shopTel);
//        apiClient.paymentList(getPayListResponseHandler(amount, String.format("支付订金：%s", amount)));
        apiClient.getPayment(orderID, getPayListResponseHandler(amount, String.format("支付订金：%s", OtherUtil.amountFormat(amount, false))));
    }

//    @Override
//    protected void exit() {
//        Intent intent = new Intent();
//        this.setResult(1, intent);
//
//    }


    @Override
    public void balancePay(String pwd) {
        apiClient.postBlancePay(orderID, mPaymentTypeChoiceView.getPaymentID(), pwd, getBalancePayResponseHandler());
    }

    @Override
    public void pay() {
        apiClient.postPay(orderID, mPaymentTypeChoiceView.getPaymentID(), amount, getPayResponseHandler());
    }

    @Override
    public void toFinishPay() {
        ActivitySwitcherThirdPartShop.finishPay(SalesDepositPayActivity.this);
    }

    @Override
    public void offlinePay() {

    }

}
