//package com.hxqc.mall.activity.order;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.text.TextUtils;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.activity.AppBackActivity;
//import com.hxqc.mall.core.adapter.OrderPackageAdapter;
//import com.hxqc.mall.core.api.DialogResponseHandler;
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.api.RequestFailViewUtil;
//import com.hxqc.mall.core.controler.UserInfoHelper;
//import com.hxqc.mall.core.model.CancelReason;
//import com.hxqc.mall.core.model.order.OrderModel;
//import com.hxqc.mall.core.util.ActivitySwitchBase;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
//import com.hxqc.mall.core.views.Order.OrderDescription;
//import com.hxqc.mall.views.ExpressType;
//import com.hxqc.mall.views.dialog.CancelOrderDialog;
//import com.hxqc.mall.views.order.OrderBottom;
//import com.hxqc.mall.views.order.OrderItem;
//import com.hxqc.pay.util.Switchhelper;
//import com.hxqc.util.DebugLog;
//import com.hxqc.util.JSONUtils;
//import com.hxqc.widget.ListViewNoSlide;
//
//import cz.msebera.android.httpclient.Header;
//
//import java.util.ArrayList;
//
//import hxqc.mall.R;
//
///**
// * 说明:订单详情
// * <p/>
// * author: 吕飞
// * since: 2015-04-07
// * Copyright:恒信汽车电子商务有限公司
// * <p/>
// * <p/>
// * 订单状态	状态值	订单状态(页面显示)	按钮文字	按钮状态	取消订单	申请退款	完善贷款资料
// * 订单已关闭  -30
// * 管理员取消订单  -20	用户取消
// * 用户取消	-10	用户取消
// * 系统取消	-1	系统取消
// * 部分支付 , 订单超时		订单超时
// * 支付完成 , 订单超时		订单超时
// * 等待付款	0	待付款	去付款	启用	启用
// * 部分支付	10	部分支付	去付款	启用		启用	启用（支付方式为分期付款）
// * 部分支付，上门收款	20	部分支付				启用
// * 上门收款受理中	25	部分支付				启用
// * 分期办理受理中	26	部分支付				启用	启用
// * 支付完成	30	支付完成				启用
// * 送货中	40	送货中
// * 申请退货	45	申请退货
// * 退货中	50	退货中
// * 完成退货	55	退货完成
// * 申请退款	60	申请退款
// * 退款已受理	70	申请退款
// * 退款已审核	80	申请退款
// * 退款进行中	90	申请退款
// * 退款完成	100	退款完成
// * 申请换货	120	申请换货
// * 换货中	130	申请换货
// * 完成换货	140	换货完成
// * 订单完成	150	订单完成	发表评论	启用
// * 客户拒签	160	订单完成
// */
//public class OrderDetailActivity extends AppBackActivity implements View.OnClickListener {
//    private static String TAG = OrderDetailActivity.class.getSimpleName();
//    RelativeLayout mBottomView;
//    OrderBottom mOrderBottomView;//底部
//    RelativeLayout mShadowView;//阴影
//    ScrollView mScrollView;
//    TextView mOrderIdView;//订单号
//    TextView mCallServiceView;//呼叫客服
//    OrderDescription mOrderDescriptionView;//订单描述
//    LinearLayout mExtraTaxView;
//    OrderItem mPrePurchaseTaxView;
//    OrderItem mPreCarTaxView;
//    OrderItem mTrafficInsuranceView;
//    OrderItem mServiceFeeView;//服务费
//    TextView mConsigneeView;//收货人
//    TextView mPhoneNumberView;//电话
//    TextView mAddressView;//地址
//    RelativeLayout mLoanInformationView;//贷款资料
//    TextView mLoanInformationUploadNumView;//贷款资料上传张数
//    OrderItem mExpressType1View;//送货方式
//    ExpressType mExpressType2View;//送货方式（含自提点）
//    OrderItem mPayTypeView;//付款方式
//    RelativeLayout mExpressView;//订单追踪
//    TextView mOrderStatusView;//订单状态
//    TextView mTimeView;//更新时间
//    OrderItem mItemPriceView;//商品总额
//    //        OrderItem mExpressFeeView;//运费
//    OrderItem mBuyInsurance;//办理保险
//    //    OrderItem mInsuranceFeeView;//保险
////    OrderItem mTaxFeeView;//税费
//    TextView mAmountView;//总计
//    TextView mPaidView;//已付
//    TextView mNeedPayView;//需付款
//    OrderModel mOrderDetail;//订单详情
//    String mOrderID;//订单号
//    int mMenuFlag;//右上显示代号
//    RequestFailView mBlankView;
//    Menu mMenu;
//    String mResponse;//详情json
//    ListViewNoSlide mPackageForDetailView;//套餐
//    RelativeLayout mBuyerInfoView;//购车人信息
//    View mBuyerLineView;//购车人信息分割线
//    boolean unDoAsyncGetData = true;
////    int lastScrollPos = 0;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.blank_background);
//        //手势关闭
////        SlidrConfig config = new SlidrConfig.Builder()
////                .primaryColor(getResources().getColor(R.color.primary))
////                .sensitivity(1f).build();
////        Slidr.attach(this, config);
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        mOrderID = bundle.getString(ActivitySwitcher.ORDER_ID);
//
//        String phoneNumber = UserInfoHelper.getInstance().getPhoneNumber(getApplicationContext());
////        ChatManager.getInstance().userLogin(getApplicationContext(),phoneNumber, phoneNumber, 0);
//    }
//
//
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putInt("scrollY", mScrollView.getScrollY());
//    }
//
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && unDoAsyncGetData) {
//            getData();
//            unDoAsyncGetData = false;
//        }
//    }
//
//
//    //    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
////            mLoanInformationUploadNumView.setText("4 张");
////        }
////    }
//
//    private void initView() {
//        setContentView(R.layout.activity_order_detail);
//        mScrollView = (ScrollView) findViewById(R.id.scrollView);
//        mExtraTaxView = (LinearLayout) findViewById(R.id.extra_tax);
//        mPrePurchaseTaxView = (OrderItem) findViewById(R.id.pre_purchase_tax);
//        mPreCarTaxView = (OrderItem) findViewById(R.id.pre_car_tax);
//        mTrafficInsuranceView = (OrderItem) findViewById(R.id.traffic_insurance);
//        mBuyerInfoView = (RelativeLayout) findViewById(R.id.buyer_info);
//        mBuyerLineView = findViewById(R.id.buyer_line);
//        mLoanInformationView = (RelativeLayout) findViewById(R.id.loan_information);
//        mLoanInformationUploadNumView = (TextView) findViewById(R.id.loan_information_upload_num);
//        mPackageForDetailView = (ListViewNoSlide) findViewById(R.id.packages);
//
//        mBottomView = (RelativeLayout) findViewById(R.id.bottom_view);
//        mOrderBottomView = (OrderBottom) findViewById(R.id.order_bottom);
//        mShadowView = (RelativeLayout) findViewById(R.id.shadow);
//        mExpressView = (RelativeLayout) findViewById(R.id.express);
//        mOrderStatusView = (TextView) findViewById(R.id.order_status);
//        mTimeView = (TextView) findViewById(R.id.time);
//        mOrderIdView = (TextView) findViewById(R.id.order_id);
//        mCallServiceView = (TextView) findViewById(R.id.call_service);
//        mOrderDescriptionView = (OrderDescription) findViewById(R.id.order_description);
//        mConsigneeView = (TextView) findViewById(R.id.consignee);
//        mPhoneNumberView = (TextView) findViewById(R.id.phone_number);
//        mAddressView = (TextView) findViewById(R.id.address);
//        mExpressType1View = (OrderItem) findViewById(R.id.express_type1);
//        mExpressType2View = (ExpressType) findViewById(R.id.express_type2);
//        mPayTypeView = (OrderItem) findViewById(R.id.pay_type);
//        mItemPriceView = (OrderItem) findViewById(R.id.item_price);
//        mBuyInsurance = (OrderItem) findViewById(R.id.buy_insurance);
////        mExpressFeeView = (OrderItem) findViewById(R.id.express_fee);
////        mInsuranceFeeView = (OrderItem) findViewById(R.id.insurance_fee);
////        mTaxFeeView = (OrderItem) findViewById(R.id.tax_fee);
//        mServiceFeeView = (OrderItem) findViewById(R.id.service_fee);
//        mAmountView = (TextView) findViewById(R.id.amount);
//        mPaidView = (TextView) findViewById(R.id.pay);
//        mNeedPayView = (TextView) findViewById(R.id.need_pay);
//
//        mCallServiceView.setOnClickListener(this);
//
//        mPrePurchaseTaxView.mLeftTextView.setText(R.string.pre_purchase_tax);
//        mPreCarTaxView.mLeftTextView.setText(R.string.pre_car_tax);
//        mTrafficInsuranceView.mLeftTextView.setText(R.string.traffic_insurance);
//        mServiceFeeView.mLeftTextView.setText(R.string.me_service_fee);
//
//        // 如果付款方式是分期付款，就显示贷款资料视图
//        if (Float.valueOf(mOrderDetail.orderPaid) > 0 && mOrderDetail.paymentType.equals("2")) {
//            if (!mOrderDetail.orderStatusCode.equals("10") && !mOrderDetail.orderStatusCode.equals("26") &&
//                    !mOrderDetail.orderStatusCode.equals("27")) {
//                mLoanInformationUploadNumView.setText("");
//            } else {
//                if (Float.valueOf(mOrderDetail.installmentInfo) < 5) {
//                    mLoanInformationUploadNumView.setTextColor(getResources().getColor(R.color.text_color_orange));
//                    mLoanInformationUploadNumView.setText("上传贷款资料");
//                } else if (Float.valueOf(mOrderDetail.installmentInfo) >= 5) {
//                    mLoanInformationUploadNumView.setTextColor(getResources().getColor(R.color.text_gray));
//                    mLoanInformationUploadNumView.setText("修改贷款资料");
//                }
//            }
//            mLoanInformationView.setVisibility(View.VISIBLE);
//            mLoanInformationView.setOnClickListener(this);
//        } else {
//            mLoanInformationView.setVisibility(View.GONE);
//        }
//
//        if (mOrderDetail.expressType.equals("1")) {
//            mExpressType1View.setVisibility(View.GONE);
//            mExpressType2View.setVisibility(View.VISIBLE);
//            mExpressType2View.setPickupPoint(mOrderDetail.PickupPoint.address, this);
//            mExpressType2View.setOnClickListener(this);
//        } else {
//            mExpressType1View.setVisibility(View.VISIBLE);
//            mExpressType2View.setVisibility(View.GONE);
//            mExpressType1View.mLeftTextView.setText(R.string.me_express_type);
//        }
//        mPayTypeView.mLeftTextView.setText(R.string.me_pay_type);
//        mItemPriceView.mLeftTextView.setText(R.string.me_item_price);
////        mExpressFeeView.mLeftTextView.setText(R.string.me_express_fee);
////        mInsuranceFeeView.mLeftTextView.setText(R.string.me_insurance_fee);
////        mTaxFeeView.mLeftTextView.setText(R.string.me_tax_fee);
//        mBuyInsurance.mLeftTextView.setText(R.string.me_buy_insurance);
//
//        fillData();
//    }
//
//    private void getData() {
//        mApiClient.getOrderDetail(UserInfoHelper.getInstance().getToken(OrderDetailActivity.this), mOrderID, new
//                LoadingAnimResponseHandler(this) {
//                    @Override
//                    public void onSuccess(String response) {
//                        mResponse = response;
//                        mOrderDetail = JSONUtils.fromJson(response, OrderModel.class);
//                        if (mOrderDetail != null) {
//                            initView();
//                            initMenu();
//                        } else {
//                            showEmptyOrFailView();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
//                            throwable) {
//                        showEmptyOrFailView();
//                    }
//                });
//    }
//
//    private void fillData() {
//        if (Float.valueOf(mOrderDetail.orderServiceFee) > 0) {
//            mPrePurchaseTaxView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.getOrderPurchaseTax()));
//            mPrePurchaseTaxView.mLeftTextView.setTextColor(getResources().getColor(R.color.text_color_subheading));
//            mPreCarTaxView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.getOrderTravelTax()));
//            mPreCarTaxView.mLeftTextView.setTextColor(getResources().getColor(R.color.text_color_subheading));
//            mTrafficInsuranceView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.orderInsuranceFee));
//            mTrafficInsuranceView.mLeftTextView.setTextColor(getResources().getColor(R.color.text_color_subheading));
//            mServiceFeeView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.orderServiceFee));
//            mServiceFeeView.mRightTextView.setTextColor(getResources().getColor(R.color.text_color_red));
//            mExtraTaxView.setVisibility(View.VISIBLE);
//        } else {
//            mExtraTaxView.setVisibility(View.GONE);
//        }
//        if (mOrderDetail.packages != null && mOrderDetail.packages.size() > 0) {
//            mPackageForDetailView.setAdapter(new OrderPackageAdapter(mOrderDetail.getPackages(), this));
//        } else {
//            mPackageForDetailView.setVisibility(View.GONE);
//        }
//        mOrderIdView.setText(String.format("%s%s", getResources().getString(R.string.me_order_id), mOrderDetail
//                .orderID));
//        mOrderDescriptionView.initOrderDescription(this, mOrderDetail, false);
////        if (mOrderDetail.orderType.equals(AutoItem.AUTO_COMMON)) {
//        mOrderDescriptionView.setOnClickListener(this);
////        }
////        mConsigneeView.setText(getResources().getString(R.string.me_consignee) + mOrderDetail.userFullname);
//        if (TextUtils.isEmpty(mOrderDetail.userFullname)) {
//            mBuyerLineView.setVisibility(View.GONE);
//            mBuyerInfoView.setVisibility(View.GONE);
//        } else {
//            mConsigneeView.setText(mOrderDetail.userFullname);
//            mPhoneNumberView.setText(mOrderDetail.userPhoneNumber);
//            mAddressView.setText(mOrderDetail.getUserAddress());
//        }
//        mExpressType1View.mRightTextView.setText(mOrderDetail.getExpressType(this));
//        mPayTypeView.mRightTextView.setText(mOrderDetail.getPaymentType(this));
//        mItemPriceView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.itemPrice));
////        mExpressFeeView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.orderExpressFee));
////        mInsuranceFeeView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.orderInsuranceFee));
////        mTaxFeeView.mRightTextView.setText(OtherUtil.stringToMoney(mOrderDetail.orderTaxFee));
//
//        if (mOrderDetail.insurance.equals("0")) {
//            mBuyInsurance.mRightTextView.setText("否");
//        } else if (mOrderDetail.insurance.equals("1")) {
//            mBuyInsurance.mRightTextView.setText("是");
//        }
//
//        mAmountView.setText(OtherUtil.stringToMoney(mOrderDetail.orderAmount));
//        mPaidView.setText(OtherUtil.stringToMoney(mOrderDetail.orderPaid));
//        mExpressView.setOnClickListener(this);
//        mNeedPayView.setText(OtherUtil.stringToMoney(mOrderDetail.orderUnpaid));
//        mOrderStatusView.setText(mOrderDetail.orderStatus);
//        mTimeView.setText(mOrderDetail.orderStatusTime);
//        if (mOrderBottomView.hideBottom(mOrderDetail)) {
//            mBottomView.setVisibility(View.GONE);
//            mOrderBottomView.setVisibility(View.GONE);
//            mShadowView.setVisibility(View.GONE);
//        } else {
//            mBottomView.setVisibility(View.VISIBLE);
//            mOrderBottomView.setVisibility(View.VISIBLE);
//            mShadowView.setVisibility(View.VISIBLE);
//            mOrderBottomView.initBottom(this, mOrderDetail, true);
//        }
//
////        mScrollView.post(new Runnable() {
////            @Override
////            public void run() {
////                mScrollView.scrollTo(0, lastScrollPos);
////                lastScrollPos = 0;
////            }
////        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        this.mMenu = menu;
//        return true;
//    }
//
//    //显示右上菜单
//    private void initMenu() {
//        mMenuFlag = getDetailMenu(mOrderDetail);
//        if (mMenu != null) {
//            mMenu.clear();
//        }
//        if (mMenuFlag != ActivitySwitchBase.NO_MENU) {
//            getMenuInflater().inflate(R.menu.menu_order_detail, mMenu);
//            if (mMenuFlag == ActivitySwitchBase.REFUND) {
//                mMenu.getItem(0).setTitle(R.string.title_activity_me_refund);
//            }
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.action_order_cancel && mOrderDetail != null) {
//            if (mMenuFlag == ActivitySwitchBase.CANCEL) {
//                mApiClient.getCancelReason(UserInfoHelper.getInstance().getToken(OrderDetailActivity.this), new
//                        DialogResponseHandler(this, getResources().getString(R.string.me_submitting)) {
//                            @Override
//                            public void onSuccess(String response) {
//                                ArrayList< CancelReason > mCancelReasons = JSONUtils.fromJson(response, new TypeToken<
//                                        ArrayList< CancelReason > >() {
//                                });
//                                if (mCancelReasons != null && mCancelReasons.size() > 0) {
//                                    CancelOrderDialog mCancelOrderDialog = new CancelOrderDialog(OrderDetailActivity.this, mApiClient, mCancelReasons, mOrderID, new CancelOrderDialog.CancelOrderListener() {
//                                        @Override
//                                        public void cancelOrder() {
//                                            finish();
//                                        }
//                                    });
//                                    mCancelOrderDialog.show();
//                                }
//
//                            }
//                        });
//            } else {
//                ActivitySwitcher.applicationRefund(mOrderDetail.orderID, this);
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        switch (i) {
//            case R.id.call_service:
//                if (mOrderDetail != null) {
////                    ChatManager.getInstance().startChatWithOrder( mOrderDetail.orderID,
////                            UserInfoHelper.getInstance().getUser(this).getFullName(false),
////                           "订单",
////                            "订单");
//                }
//
//                break;
//
//            case R.id.loan_information:
//                /**
//                 * 10.18王颢... 调用pay包里面的页面
//                 */
//                DebugLog.i("test_upload", "orderdetail: " + mOrderDetail.orderID);
////                lastScrollPos = mScrollView.getScrollY();
//                Switchhelper.toUploadInfo(mOrderDetail.orderStatusCode, mOrderDetail.orderID, this);
//                break;
//
//            case R.id.express:
////                lastScrollPos = mScrollView.getScrollY();
//                ActivitySwitcher.toExpress(this, mOrderDetail.orderID);
//                break;
//
//            case R.id.express_type2:
////                lastScrollPos = mScrollView.getScrollY();
//                ActivitySwitcher.toAMapNvai(this, mResponse);
//                break;
//
//            case R.id.order_description:
////                lastScrollPos = mScrollView.getScrollY();
//                DebugLog.e(TAG, mOrderDetail.promotionStatus + " " + " status");
//                /** 用于判断该促销车辆是否下架，如果下架就将订单类型变为普通订单，跳转到普通车辆详情页面 */
//                if (mOrderDetail.orderType.equals("1") && !mOrderDetail.getPromotionStatus()) {
//                    mOrderDetail.orderType = "0";
//                }
//                if ("0".equals(mOrderDetail.orderType)) {
//                    ActivitySwitcher.toAutoItemDetail(this, mOrderDetail.orderType, mOrderDetail.itemID, mOrderDetail
//                            .itemName);
//                } else if ("1".equals(mOrderDetail.orderType)) {
//                    ActivitySwitcher.toAutoItemDetail(this, mOrderDetail.orderType, mOrderDetail.promotionID,
//                            mOrderDetail.itemName);
//                }
//                break;
//        }
//    }
//
//    //    详情右上显示代号
//    public int getDetailMenu(OrderModel orderDetail) {
//        if (orderDetail.orderStatusCode.equals("0")) {
//            return ActivitySwitchBase.CANCEL;
//        } else if (orderDetail.orderStatusCode.equals("26") || orderDetail.getOrderStatus().equals("部分支付") ||
//                orderDetail.getOrderStatus().equals("已付订金") || orderDetail.getOrderStatus().equals("订单已确认") ||
//                orderDetail.getOrderStatus().equals("支付完成")) {
//            return ActivitySwitchBase.REFUND;
//        } else {
//            return ActivitySwitchBase.NO_MENU;
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mOrderDetail != null) {
//            getData();
//        }
//    }
//
//    public void showEmptyOrFailView() {
//        mBlankView = (RequestFailView) new RequestFailViewUtil().getFailView(OrderDetailActivity.this);
//        mBlankView.setVisibility(View.VISIBLE);
//        mBlankView.setEmptyDescription("数据获取失败");
//        mBlankView.setRequestViewType(RequestFailView.RequestViewType.empty);
//        mBlankView.setEmptyButtonClick("重新加载", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBlankView.setVisibility(View.GONE);
//                getData();
//            }
//        });
//        setContentView(mBlankView);
//    }
//}
