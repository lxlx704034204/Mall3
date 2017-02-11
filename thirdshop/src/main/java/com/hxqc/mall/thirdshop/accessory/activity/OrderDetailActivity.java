//package com.hxqc.mall.thirdshop.accessory.activity;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.util.ActivitySwitchBase;
//import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.core.views.dialog.NormalDialog;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.accessory.model.OrderDetail;
//import com.hxqc.mall.thirdshop.accessory.model.SubmitOrderInfo;
//import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
//import com.hxqc.mall.thirdshop.accessory.views.OrderDetailItem;
//import com.hxqc.mall.thirdshop.accessory.views.ShopInOrder;
//import com.hxqc.util.JSONUtils;
//
//import cz.msebera.android.httpclient.Header;
//
//import java.util.ArrayList;
//
///**
// * 说明:用品订单详情
// *
// * @author: 吕飞
// * @since: 2016-02-25
// * Copyright:恒信汽车电子商务有限公司
// */
//@Deprecated
//public class OrderDetailActivity extends AccessoryNoBackActivity implements View.OnClickListener {
//    String mOrderID;//订单id
//    Toolbar mToolbar;
//    TextView mMenuView;//右上角菜单
//    RequestFailView mFailView;
//    TextView mOrderIDView;//订单号
//    ShopInOrder mShopInOrderView;//订单商品内容
//    LinearLayout mShopAddressView;//门店item
//    TextView mShopAddressTextView;//门店地址
//    OrderDetailItem mContactInfoView;//联系人信息
//    OrderDetailItem mOrderStatusView; //订单状态
//    OrderDetailItem mPaymentView;//支付方式
//    OrderDetailItem mTransactionNumberView;//交易单号
//    OrderDetailItem mOrderTimeView;//下单时间
//    OrderDetailItem mRefundStatusView;//退款状态
//    Button mDeleteView;//删除按钮
//    Button mToPayView;//支付按钮
//    TextView mSubscriptionView;//订金
//    OrderDetail mOrderDetail;//订单详情数据
////    public static OrderDetailActivity sInstance = null;
//    public static boolean sNeedRefresh = false;
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (sNeedRefresh) {
//            getData();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mOrderID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory.ORDER_ID);
//        setContentView(R.layout.activity_accessory_order_detail);
//        initView();
//        getData();
//    }
//
//    private void getData() {
//        sNeedRefresh = false;
//        mAccessoryApiClient.getAccessoryOrderDetail(mOrderID, new LoadingAnimResponseHandler(this) {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                showFailView();
//            }
//
//            @Override
//            public void onSuccess(String response) {
//                mOrderDetail = JSONUtils.fromJson(response, OrderDetail.class);
//                fillData();
//            }
//        });
//    }
//
//    private void fillData() {
//        mFailView.setVisibility(View.GONE);
//        initMenu();
//        mOrderIDView.setText(String.format("订单号：%s", mOrderDetail.orderID));
//        mShopInOrderView.fillData(mOrderDetail.itemList, true);
//        mShopAddressTextView.setText(mOrderDetail.shopInfo.shopLocation.name);
//        mContactInfoView.setRightText(mOrderDetail.contactName, mOrderDetail.contactPhone);
//        mOrderStatusView.setRightText(mOrderDetail.getOrderStatus());
//        mOrderTimeView.setRightText(mOrderDetail.orderCreateTime);
////        mPayTimeView.setRightText(mOrderDetail.orderPaymentTime);
////        OtherUtil.setVisible(mPayTimeView, !mOrderDetail.orderStatusCode.equals(OrderDetail.WAIT));
//        if (!TextUtils.isEmpty(mOrderDetail.refundStatus)) {
//            mRefundStatusView.setRightText(mOrderDetail.getRefundStatus());
//        } else {
//            mRefundStatusView.setVisibility(View.GONE);
//        }
//        mSubscriptionView.setText(mOrderDetail.getSubscription());
//        OtherUtil.setVisible(mToPayView, mOrderDetail.orderStatusCode.equals(OrderDetail.WAIT));
//        OtherUtil.setVisible(mDeleteView, mOrderDetail.orderStatusCode.equals(OrderDetail.CANCELED) || mOrderDetail.orderStatusCode.equals(OrderDetail.CLOSED));
//    }
//
//    private void initMenu() {
//        mMenuView.setVisibility(View.VISIBLE);
//        if (mOrderDetail.orderStatusCode.equals(OrderDetail.WAIT)) {
//            mMenuView.setText("取消订单");
//        } else if (TextUtils.isEmpty(mOrderDetail.refundStatus)&&mOrderDetail.orderStatusCode.equals(OrderDetail.PAID)) {
//            mMenuView.setText("申请退款");
//        } else if (mOrderDetail.refundStatus.equals(OrderDetail.UNREFUND)) {
//            mMenuView.setText("取消申请退款");
//        } else {
//            mMenuView.setVisibility(View.GONE);
//        }
//    }
//
//    private void showFailView() {
//        mFailView.setVisibility(View.VISIBLE);
//        mFailView.setEmptyDescription("网络连接失败");
//        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getData();
//            }
//        });
//        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
//    }
//
//    private void initView() {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle(R.string.accessory_title_activity_order_detail);
//        mToolbar.setTitleTextColor(Color.WHITE);
//        mToolbar.setNavigationIcon(R.drawable.ic_back);
//        setSupportActionBar(mToolbar);
//        mMenuView = (TextView) findViewById(R.id.menu);
//        mFailView = (RequestFailView) findViewById(R.id.fail_view);
//        mOrderIDView = (TextView) findViewById(R.id.order_id);
//        mShopInOrderView = (ShopInOrder) findViewById(R.id.shop_in_order);
//        mShopAddressView = (LinearLayout) findViewById(R.id.shop_address);
//        mShopAddressTextView= (TextView) findViewById(R.id.order_shop_address_text);//1
//        mContactInfoView = (OrderDetailItem) findViewById(R.id.order_contact_info);//1
//        mOrderStatusView = (OrderDetailItem) findViewById(R.id.order_status);
//        mPaymentView = (OrderDetailItem) findViewById(R.id.order_payment); //1
//        mOrderTimeView = (OrderDetailItem) findViewById(R.id.order_time);
//        mTransactionNumberView = (OrderDetailItem) findViewById(R.id.order_transaction_number); //1
//        mRefundStatusView = (OrderDetailItem) findViewById(R.id.order_refund_status); //1
//        mDeleteView = (Button) findViewById(R.id.delete);
//        mToPayView = (Button) findViewById(R.id.order_to_pay); //1
//        mSubscriptionView = (TextView) findViewById(R.id.subscription);
//        mShopAddressView.setOnClickListener(this);
//        mDeleteView.setOnClickListener(this);
//        mToPayView.setOnClickListener(this);
//        mMenuView.setOnClickListener(this);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return super.onSupportNavigateUp();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.shop_address) {
//            ActivitySwitcherAccessory.toAMapNvai(this, mOrderDetail.shopInfo.shopLocation);
//        } else if (i == R.id.menu) {
//            if (mMenuView.getText().equals("取消订单")) {
//                new NormalDialog(this, "订单取消后将无法恢复，您确定取消吗？") {
//                    @Override
//                    protected void doNext() {
//                        mAccessoryApiClient.cancelAccessoryOrder(mOrderID,"", new LoadingAnimResponseHandler(OrderDetailActivity.this) {
//                            @Override
//                            public void onSuccess(String response) {
//                                new BaseSharedPreferencesHelper(OrderDetailActivity.this).setOrderChange(true);
//                                finish();
//                            }
//                        });
//                    }
//                }.show();
//            } else if (mMenuView.getText().equals("申请退款")) {
//                ActivitySwitcherAccessory.toRefund(this, mOrderID);
//            } else if (mMenuView.getText().equals("取消申请退款")) {
//                new NormalDialog(this, "您确定要取消申请退款吗？") {
//                    @Override
//                    protected void doNext() {
//                        mAccessoryApiClient.cancelRefund(mOrderID, new LoadingAnimResponseHandler(OrderDetailActivity.this) {
//                            @Override
//                            public void onSuccess(String response) {
//                                new BaseSharedPreferencesHelper(OrderDetailActivity.this).setOrderChange(true);
//                                finish();
//                            }
//                        });
//                    }
//                }.show();
//            }
//        } else if (i == R.id.delete) {
//            new NormalDialog(this, "订单删除后将无法恢复，您确定删除吗？") {
//                @Override
//                protected void doNext() {
//                    mAccessoryApiClient.deleteAccessoryOrder(mOrderID, new LoadingAnimResponseHandler(OrderDetailActivity.this) {
//                        @Override
//                        public void onSuccess(String response) {
//                            new BaseSharedPreferencesHelper(OrderDetailActivity.this).setOrderChange(true);
//                            finish();
//                        }
//                    });
//                }
//            }.show();
//        } else if (i == R.id.order_to_pay) {
//            ArrayList<SubmitOrderInfo> submitOrderInfos = new ArrayList<>();
//            SubmitOrderInfo submitOrderInfo = new SubmitOrderInfo(mOrderDetail.orderID, mOrderDetail.shopInfo.shopLocation.name);
//            submitOrderInfos.add(submitOrderInfo);
//            ActivitySwitcherAccessory.toPaySubscription(OrderDetailActivity.this, submitOrderInfos, mOrderDetail.orderAmount);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        sInstance = null;
//    }
//}
