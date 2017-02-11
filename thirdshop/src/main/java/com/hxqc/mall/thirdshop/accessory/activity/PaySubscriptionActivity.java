package com.hxqc.mall.thirdshop.accessory.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.payment.activity.PayBaseActivity;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.SubmitOrderInfo;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.mall.thirdshop.maintenance.utils.SharedPreferencesHelper;

import java.util.ArrayList;

/**
 * 说明:预付订金
 *
 * @author: 吕飞
 * @since: 2016-02-29
 * Copyright:恒信汽车电子商务有限公司
 */
public class PaySubscriptionActivity extends PayBaseActivity {
    ArrayList<SubmitOrderInfo> mSubmitOrderInfos = new ArrayList<>();//上个界面带来的订单信息
    //    ListViewNoSlide mOrderListView;//订单信息
    ArrayList<String> mOrderIDs = new ArrayList<>();//订单数据
    //    QuickAdapter<SubmitOrderInfo> mOrderAdapter;//订单列表适配器
    String mOrderID;
    String mAmount;
    TextView mOrderIDView;

    @Override
    protected View getTileView() {
        mSubmitOrderInfos = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(ActivitySwitcherAccessory.SUBMIT_ORDER_INFO);
        mAmount = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory.AMOUNT);
        mOrderID = mSubmitOrderInfos.get(0).orderID;
//        for (int i = 0; i < mSubmitOrderInfos.size(); i++) {
//            mOrderIDs.add(mSubmitOrderInfos.get(i).orderID);
//        }
        View titleView = LayoutInflater.from(this).inflate(R.layout.view_accessory_pay_title_2, null);
        mOrderIDView = (TextView) titleView.findViewById(R.id.order_id);
        mOrderIDView.setText("订单号：" + mOrderID);
//        mOrderListView = (ListViewNoSlide) titleView.findViewById(R.id.order_list);
//        mOrderAdapter = new QuickAdapter<SubmitOrderInfo>(this, R.layout.item_pay_subscription_order, mSubmitOrderInfos) {
//            @Override
//            protected void convert(BaseAdapterHelper helper, SubmitOrderInfo item) {
//                helper.setText(R.id.shop_name, item.shopName);
//                helper.setText(R.id.order_id, "订单号：" + item.orderID);
//            }
//        };
//        mOrderListView.setAdapter(mOrderAdapter);

        return titleView;
    }

    //获取支付方式列表
    protected void getPayList() {
        new AccessoryApiClient().getPaymentList(getPayListResponseHandler(mAmount, "合计：" + OtherUtil.amountFormat(mAmount, true)));
    }
    @Override
    public void balancePay(String pwd) {
        new AccessoryApiClient().balancePay(mOrderID, mPaymentTypeChoiceView.getPaymentID(), pwd, getBalancePayResponseHandler());
    }

    @Override
    public void pay() {
        new AccessoryApiClient().accessoryPayment(mOrderID, mPaymentTypeChoiceView.getPaymentID(), getPayResponseHandler());
    }

    @Override
    public void toFinishPay() {
        ActivitySwitcherAccessory.toPayFinish(this);

    }

    @Override
    public void offlinePay() {
        new AccessoryApiClient().accessoryPayment(mOrderID, PayConstant.INSHOP, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                new SharedPreferencesHelper(PaySubscriptionActivity.this).setOrderChange(true);

                ActivitySwitcherAccessory.toShopPayFinish(PaySubscriptionActivity.this, mOrderID);
                finish();
            }
        });
    }
}
