package com.hxqc.mall.views.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.order.OrderModel;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;

import java.util.Arrays;

import hxqc.mall.R;

/**
 * 说明:订单底部（商城项目的精髓所在）
 * <p/>
 * author: 吕飞
 * since: 2015-06-03
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderBottom extends RelativeLayout {
    public static final String ONLINE_PAY = "0";
    public static final String OFFLINE_PAY = "1";
    public static final String BY_STAGES = "2";
    public static final String MY_ORDER = "0";//普通
    public static final String SPECIAL_OFFER = "1";//特卖
    OrderModel mOrderDetail;
    Context mContext;
    RadioGroup mPaymentTypeView;
    RadioButton mOnlinePayView;
    RadioButton mGoHomePayView;
    Button mOrderOperateView;//按钮
    TextView mPaidTextView;
    TextView mPaidView;
    int mPayFlag;
    ApiClient mApiClient;
    SharedPreferencesHelper mSharedPreferencesHelper;
    boolean mIsOrderDetailActivity;//是否是OrderDetailActivity

    public OrderBottom(Context context) {
        super(context);
    }

    public OrderBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_order_bottom, this);
        mPaidTextView = (TextView) findViewById(R.id.paid_text);
        mPaidView = (TextView) findViewById(R.id.paid);
        mPaymentTypeView = (RadioGroup) findViewById(R.id.payment_type);
        mOnlinePayView = (RadioButton) findViewById(R.id.online_pay);
        mGoHomePayView = (RadioButton) findViewById(R.id.go_home_pay);
        mOrderOperateView = (Button) findViewById(R.id.order_operate);
    }


    public void initBottom(Context context, OrderModel mOrderDetail, boolean mIsOrderDetailActivity) {
        this.mContext = context;
        this.mOrderDetail = mOrderDetail;
        this.mIsOrderDetailActivity = mIsOrderDetailActivity;
        mApiClient = new ApiClient();
        mSharedPreferencesHelper = new SharedPreferencesHelper(mContext);
        showBottom();
    }

    private void showBottom() {
        if (mOrderDetail.paymentType.equals("")) {
            mOrderDetail.mHasPaymentType = false;
            mPaymentTypeView.setVisibility(View.VISIBLE);
            mOrderOperateView.setText(R.string.me_to_pay);
            mOnlinePayView.setChecked(true);
            mOrderDetail.paymentType = ONLINE_PAY;
            mPaymentTypeView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.online_pay) {
                        mOrderDetail.paymentType = ONLINE_PAY;
                    } else if (checkedId == R.id.go_home_pay) {
                        mOrderDetail.paymentType = OFFLINE_PAY;
                    }
                }
            });
            mPaidTextView.setVisibility(View.GONE);
            mPaidView.setVisibility(View.GONE);
        } else {
            mOrderDetail.mHasPaymentType = true;
            mPaymentTypeView.setVisibility(View.GONE);
            if (
//                    !mIsOrderDetailActivity &&
                    mOrderDetail.orderStatusCode.equals("26") || mOrderDetail.getOrderStatus().equals("部分支付") ||
                            mOrderDetail.getOrderStatus().equals("已付订金") || mOrderDetail.getOrderStatus().equals("订单已确认")) {
                mPaidTextView.setVisibility(View.VISIBLE);
                mPaidView.setVisibility(View.VISIBLE);
                mPaidView.setText(OtherUtil.stringToMoney(mOrderDetail.orderPaid));
            } else {
                mPaidTextView.setVisibility(View.GONE);
                mPaidView.setVisibility(View.GONE);
            }
        }
        if (!hideButton(mOrderDetail)) {
            mOrderOperateView.setVisibility(VISIBLE);
            showButton();
        } else {
            mOrderOperateView.setVisibility(GONE);
        }
    }

    private void showButton() {
        if (mOrderDetail.orderStatusCode.equals("20") ||
                mOrderDetail.orderStatusCode.equals("10") ||
                mOrderDetail.orderStatusCode.equals("0") ||
                mOrderDetail.orderStatusCode.equals("30")) {
//            mOrderOperateView.setBackgroundResource(R.drawable.btn_orange);
//            mOrderOperateView.setTextColor(mContext.getResources().getColor(R.color.white));
//        配送方式未选择、已付款、已签合同，就要完善信息了
            if (mOrderDetail.expressType.equals("0") && !mOrderDetail.orderPaid.equals("0") && !mOrderDetail.userFullname.equals("")) {
                if (mOrderDetail.orderStatusCode.equals("30")) {
                    mOrderOperateView.setText(R.string.me_info);
                } else if (!mOrderDetail.paymentType.equals(ONLINE_PAY)) {
                    mOrderOperateView.setText(R.string.me_info);
                } else {
                    mOrderOperateView.setText(R.string.me_to_pay);
                }
            } else if (mOrderDetail.getOrderType().equals(SPECIAL_OFFER) && mOrderDetail.orderPaid.equals("0")) {
                mOrderOperateView.setText(R.string.me_to_pay);
            } else if (mOrderDetail.userFullname.equals("")) {
                mOrderOperateView.setText(R.string.me_info);
            } else {
                mOrderOperateView.setText(R.string.me_to_pay);
            }
//            进入付款
            mOrderOperateView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mOrderDetail.mHasPaymentType) {
                        mApiClient.setPaymentType(UserInfoHelper.getInstance().getToken(getContext()),
                                mOrderDetail.orderID, mOrderDetail.paymentType, new DialogResponseHandler(mContext, mContext.getResources().getString(R.string.me_submitting)) {
                            @Override
                            public void onSuccess(String response) {
                                mSharedPreferencesHelper.setOrderChange(true);
                                setPayFlag();
                            }
                        });
                    } else {
                        setPayFlag();
                    }
                }
            });
        } else if (mOrderDetail.orderStatusCode.equals("150") && mOrderDetail.canComment.equals("1")) {     // TODO 待根据服务器返回状态进行调整
//            mOrderOperateView.setBackgroundResource(R.drawable.btn_comment_in_order);
//            mOrderOperateView.setTextColor(mContext.getResources().
//                    getColor(R.color.text_color_orange));
            mOrderOperateView.setText("发表评论");
            mOrderOperateView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcher.toSendComment(mContext, mOrderDetail.itemID, mOrderDetail.orderID);
                }
            });
        }
    }

    static int anInt = 1;
    static int bAnInt = 2;

    //        跳转至支付界面（首先要设置FLAG）
    private void setPayFlag() {
//修改胡俊杰
        switch (mOrderDetail.getOrderType()) {
            case SPECIAL_OFFER:
                //特卖
                //  特卖 未签合同 线下 分期
                if (!mOrderDetail.paymentType.equals(ONLINE_PAY) && mOrderDetail.userFullname.equals("")) {
                    mPayFlag = PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE;
                    if (!mOrderDetail.orderPaid.equals("0")) {//已付订金
                        ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
                    } else {//未付订金
                        ActivitySwitcher.toDeposit(mContext, mPayFlag, mOrderDetail.orderID);
                    }
//                特卖 未签合同 线上
                } else if (mOrderDetail.paymentType.equals(ONLINE_PAY) && mOrderDetail.userFullname.equals("")) {
                    mPayFlag = PayConstant.PAY_ONLY_DEPOSIT_ONLINE;
                    if (!mOrderDetail.orderPaid.equals("0")) {//已付订金
                        ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
                    } else {//未付订金
                        ActivitySwitcher.toDeposit(mContext, mPayFlag, mOrderDetail.orderID);
                    }
//                特卖 只付了订金 已签合同
                } else if (!mOrderDetail.orderPaid.equals("0") && !mOrderDetail.userFullname.equals("") && mOrderDetail.expressType.equals("0")) {
                    mPayFlag = PayConstant.JUST_COMPLETE_INFO;
                    ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
//                已支付订金或者 部分 进入完善信息
                } else if (!mOrderDetail.orderPaid.equals("0") && mOrderDetail.expressType.equals("0")) {
                    if (mOrderDetail.orderStatusCode.equals("30")) {
                        toCompleteInfo();
                    } else {
                        toPayStep2();
                    }
                } else {
                    toPay();
                }
                break;
            case MY_ORDER:
                //普通
//                普通订单 分期线下 已签合同未付订金
                if (!mOrderDetail.paymentType.equals(ONLINE_PAY) && !mOrderDetail.userFullname.equals("") && mOrderDetail.orderPaid.equals("0")) {
                    mPayFlag = PayConstant.NORMAL_HAD_CONTRACT_UNDERLINE;
                    ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
//                普通订单 线上已签合同
                } else if (mOrderDetail.paymentType.equals(ONLINE_PAY) && !mOrderDetail.userFullname.equals("") && mOrderDetail.orderPaid.equals("0")) {
                    mPayFlag = PayConstant.NORMAL_HAD_CONTRACT_ONLINE;
                    ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);

                } else if (!mOrderDetail.orderPaid.equals("0") && mOrderDetail.expressType.equals("0")) {
                    if (mOrderDetail.orderStatusCode.equals("30")) {
                        toCompleteInfo();
                    } else if (!mOrderDetail.paymentType.equals(ONLINE_PAY)) {
                        toCompleteInfo();
                    } else {
                        toPayStep2();
                    }
                } else {
                    toPay();
                }
                break;
//            default:
//                if (!mOrderDetail.orderPaid.equals("0.00") && mOrderDetail.expressType.equals("0")) {
//                    mPayFlag = PayConstant.JUST_COMPLETE_INFO;
//                    ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
//                普通订单 分期线下 已签合同未付订金
//                } else {
//                mPayFlag = PayConstant.PAY_ONLY_ONLINE_PAID;
//                ActivitySwitcher.toPaySpareMoney(mContext, mPayFlag, mOrderDetail.orderID);
//                }
//                break;
        }


        //吕飞
////            特卖 未签合同 线下 分期
//        if (UserOrderActivity.sIsSpecialOffer && !mOrderDetail.paymentType.equals(ONLINE_PAY) && mOrderDetail.userFullname.equals("")) {
//            mPayFlag = PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE;
//            if (!mOrderDetail.orderPaid.equals("0.00")) {//已付订金
//                ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
//            } else {//未付订金
//                ActivitySwitcher.toDeposit(mContext, mPayFlag, mOrderDetail.orderID);
//            }
////                特卖 未签合同 线上
//        } else if (UserOrderActivity.sIsSpecialOffer && mOrderDetail.paymentType.equals(ONLINE_PAY) && mOrderDetail.userFullname.equals("")) {
//            mPayFlag = PayConstant.PAY_ONLY_DEPOSIT_ONLINE;
//            if (!mOrderDetail.orderPaid.equals("0.00")) {//已付订金
//                ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
//            } else {//未付订金
//                ActivitySwitcher.toDeposit(mContext, mPayFlag, mOrderDetail.orderID);
//            }
////                特卖 只付了订金 已签合同
//        } else if (UserOrderActivity.sIsSpecialOffer && !mOrderDetail.orderPaid.equals("0.00") && !mOrderDetail.userFullname.equals("") && mOrderDetail.expressType.equals("0")) {
//            mPayFlag = PayConstant.JUST_COMPLETE_INFO;
//            ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
////                已支付订金或者 部分 进入完善信息
//        } else if (!mOrderDetail.orderPaid.equals("0.00") && mOrderDetail.expressType.equals("0")) {
//            mPayFlag = PayConstant.JUST_COMPLETE_INFO;
//            ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
////                普通订单 分期线下 已签合同未付订金
//        } else if (!UserOrderActivity.sIsSpecialOffer && !mOrderDetail.paymentType.equals(ONLINE_PAY) && !mOrderDetail.userFullname.equals("") && mOrderDetail.orderPaid.equals("0.00")) {
//            mPayFlag = PayConstant.NORMAL_HAD_CONTRACT_UNDERLINE;
//            ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
////                普通订单 线上已签合同
//        } else if (!UserOrderActivity.sIsSpecialOffer && mOrderDetail.paymentType.equals(ONLINE_PAY) && !mOrderDetail.userFullname.equals("") && mOrderDetail.orderPaid.equals("0.00")) {
//            mPayFlag = PayConstant.NORMAL_HAD_CONTRACT_ONLINE;
//            ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
//        } else {
//            mPayFlag = PayConstant.PAY_ONLY_ONLINE_PAID;
//            ActivitySwitcher.toPaySpareMoney(mContext, mPayFlag, mOrderDetail.orderID);
//        }
    }

    private void toCompleteInfo() {
        mPayFlag = PayConstant.JUST_COMPLETE_INFO;
        ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
    }

    private void toPayStep2() {
        mPayFlag = PayConstant.uncomplete_info_pay_part;
        ActivitySwitcher.toPayMain(mContext, mPayFlag, mOrderDetail.orderID);
    }

    private void toPay() {
        mPayFlag = PayConstant.PAY_ONLY_ONLINE_PAID;
        ActivitySwitcher.toPaySpareMoney(mContext, mPayFlag, mOrderDetail.orderID);
    }

    //    是否隐藏底部
    public boolean hideBottom(OrderModel orderDetail) {
//        if (orderDetail.orderStatus.equals("订单完成") && orderDetail.canComment.equals("0")) {
//            return true;
//        }
        String[] showBottom = {"0", "10", "20", "25", "26"};
        switch (orderDetail.orderStatusCode) {
            case "30":
                return !orderDetail.expressType.equals("0");
            case "150":
                return orderDetail.hasComment.equals("1") || orderDetail.canComment.equals("0");
            default:
                return !(Arrays.asList(showBottom).contains(orderDetail.orderStatusCode));
        }
    }

    //    是否隐藏按钮
    public boolean showButton(OrderModel orderDetail) {
        if (orderDetail.orderStatusCode.equals("150")) {
            return true;
        }
        if (orderDetail.paymentType.equals(ONLINE_PAY)) {
            return orderDetail.orderStatusCode.equals("0") || orderDetail.orderStatusCode.equals("10") ||
                    (orderDetail.orderUnpaid.equals("0.00") && orderDetail.expressType.equals("0"));
        } else {
            return orderDetail.orderPaid.equals("0") || orderDetail.expressType.equals("0");
        }

    }

    public boolean hideButton(OrderModel orderDetail) {
        return !showButton(orderDetail);
    }
}
