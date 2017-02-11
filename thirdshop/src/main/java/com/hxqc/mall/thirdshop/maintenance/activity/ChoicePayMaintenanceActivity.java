package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.payment.activity.PayBaseActivity;
import com.hxqc.mall.payment.view.PaymentTypeChoice;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.order.CreateOrder;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.maintenance.utils.SharedPreferencesHelper;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

/**
 * @Author : 钟学东
 * @Since : 2016-03-16
 * FIXME
 * Todo 选择支付方式
 */
public class ChoicePayMaintenanceActivity extends PayBaseActivity implements PaymentTypeChoice.PaymentListener {

    String mAmount;
    private TextView mOrderNumView;
    private CreateOrder createOrder;
    private ActivitySwitcherThirdPartShop activitySwitcherThirdPartShop;
    private String shopID;
    private String flag;

    @Override
    protected View getTileView() {
        View titleView = LayoutInflater.from(this).inflate(R.layout.view_maintain_pay_title, null);

        mOrderNumView = (TextView) titleView.findViewById(R.id.order_number);
        activitySwitcherThirdPartShop = new ActivitySwitcherThirdPartShop();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
        createOrder = bundle.getParcelable("createOrder");
        shopID = bundle.getString("shopID");
        flag = bundle.getString("flag");
        mAmount = createOrder.amount+"";
        mOrderNumView.setText(createOrder.orderID);
        return titleView;
    }

    @Override
    protected void getPayList() {
        new MaintenanceClient().listPayment(getPayListResponseHandler(mAmount, "合计：" + OtherUtil.amountFormat(mAmount, true)));
    }

//    @Override
//    protected void exit() {
//        if(flag.equals("1")){
//            ActivitySwitcherMaintenance.toSmartMaintenanceWithFlag(this);
//        }else {
//            new BaseSharedPreferencesHelper(this).setOrderChange(true);
//            ActivitySwitchBase.toMain(this, 2);
//        }
//    }

    @Override
    public void balancePay(String pwd) {
        new MaintenanceClient().balance(createOrder.orderID, mPaymentTypeChoiceView.getPaymentID(),pwd,getBalancePayResponseHandler());
    }

    @Override
    public void pay() {
        new MaintenanceClient().pay(createOrder.orderID, mPaymentTypeChoiceView.getPaymentID(), mAmount, getPayResponseHandler());
    }

    @Override
    public void toFinishPay() {
        ActivitySwitcherMaintenance.toPayFinish(this);
    }

    @Override
    public void offlinePay() {
        new MaintenanceClient().pay(createOrder.orderID, PayConstant.INSHOP, mAmount, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                new SharedPreferencesHelper(ChoicePayMaintenanceActivity.this).setOrderChange(true);
                ActivitySwitcherMaintenance.toShopFinishPay(ChoicePayMaintenanceActivity.this,createOrder.orderID,"2");
            }
        });
    }


}
