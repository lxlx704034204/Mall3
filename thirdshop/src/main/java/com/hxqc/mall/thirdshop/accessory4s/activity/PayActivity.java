package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.payment.activity.PayBaseActivity;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;
import com.hxqc.mall.thirdshop.accessory4s.model.SubmitOrderInfo4S;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.maintenance.utils.SharedPreferencesHelper;
import com.hxqc.widget.ListViewNoSlide;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 说明:预付订金
 *
 * @author: 吕飞
 * @since: 2016-02-29
 * Copyright:恒信汽车电子商务有限公司
 */
public class PayActivity extends PayBaseActivity {
    SubmitOrderInfo4S mSubmitOrderInfo4S;
    ArrayList<String> mOrderIDs = new ArrayList<>();//订单数据
    ListViewNoSlide mOrderListView;
    String mAmount;
    QuickAdapter<String> mOrderAdapter;

    @Override
    protected View getTileView() {
        mSubmitOrderInfo4S = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(ActivitySwitcherAccessory4S.SUBMIT_ORDER_INFO_4S);
        mAmount = mSubmitOrderInfo4S != null ? mSubmitOrderInfo4S.amount : "";
        Collections.addAll(mOrderIDs, mSubmitOrderInfo4S != null ? mSubmitOrderInfo4S.orderID.split(",") : new String[0]);
        View titleView = LayoutInflater.from(this).inflate(R.layout.view_accessory_pay_title_4s, null);
        mOrderListView = (ListViewNoSlide) titleView.findViewById(R.id.order_list);
        mOrderAdapter = new QuickAdapter<String>(this, R.layout.item_pay_order, mOrderIDs) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.order_id, "订单号：" + item);
            }
        };
        mOrderListView.setAdapter(mOrderAdapter);

        return titleView;
    }

    //获取支付方式列表
    protected void getPayList() {
        new Accessory4SApiClient().listPayment(getPayListResponseHandler(mAmount, "合计：" + OtherUtil.amountFormat(mAmount, true)));
    }

//    @Override
//    protected void exit() {
//        new BaseSharedPreferencesHelper(this).setOrderChange(true);
//        ActivitySwitchBase.toMain(this, 2);
//    }

    @Override
    public void balancePay(String pwd) {
        new Accessory4SApiClient().balance(mSubmitOrderInfo4S.orderID, mPaymentTypeChoiceView.getPaymentID(), pwd, getBalancePayResponseHandler());
    }

    @Override
    public void pay() {
        new Accessory4SApiClient().payment(mSubmitOrderInfo4S.orderID, mPaymentTypeChoiceView.getPaymentID(), getPayResponseHandler());
    }

    @Override
    public void toFinishPay() {
        ActivitySwitcherAccessory4S.toPayFinish(this);

    }

    @Override
    public void offlinePay() {
        new Accessory4SApiClient().payment(mSubmitOrderInfo4S.orderID, PayConstant.INSHOP, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                new SharedPreferencesHelper(PayActivity.this).setOrderChange(true);
                ActivitySwitcherAccessory4S.toShopPayFinish(PayActivity.this, mOrderIDs.get(0),mAmount);
                finish();
            }
        });
    }
}
