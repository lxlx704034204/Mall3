package com.hxqc.mall.thirdshop.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.api.RequestFailViewUtil;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.ThirdOrderModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.T_CancelOrderDialog;
import com.hxqc.mall.thirdshop.views.ThirdOrderBottom;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Function:第三方店铺订单详情页
 *
 * @author 袁秉勇
 * @since 2015年12月01日
 */
public class ThirdShopOrderDetailActivity extends NoBackActivity implements T_CancelOrderDialog.CancelOrderListener,View.OnClickListener {
    ThirdPartShopClient mThirdApiClient;
    String mOrderID;
    ThirdOrderModel mThirdOrderModel;
    TextView mMerchantNameView;//商户名称
    TextView mConsigneeView;//收货人
    TextView mConsigneeViewTelView;//收货人电话
    TextView mOrderStatusView;//订单状态
    TextView mOrderStatusRefundView;//订单状态
    TextView mOrderTimeView;//订单时间
    TextView mOrderIDView;
    ImageView mThirdOrderImageView;
    TextView mThirdOrderFavorableNameView;
    TextView mThirdOrderFavorableTimeView;
    TextView mCaptchaView;
    RelativeLayout mBottomView;
    ThirdOrderBottom mThirdOrderBottom;
    TextView mPaymentIDText;
    private Toolbar mToolbar;
    private TextView mMenu1,mMenu2;
    private LinearLayout mRefundlly;
    private RelativeLayout   mCaptcharlyView;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.blank_background);

        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null)
        {
            mOrderID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("orderID");
        }

        mThirdApiClient = new ThirdPartShopClient();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        mThirdApiClient.getThirdOrderDetail(mOrderID, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                mThirdOrderModel = JSONUtils.fromJson(response, ThirdOrderModel.class);
                if (mThirdOrderModel != null) {
                    initView();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                try {
                    mError = JSONUtils.fromJson(responseString, com.hxqc.mall.core.model.Error.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showEmptyOrErrorView(mError);
            }
        });
    }


    public void initView() {
        setContentView(R.layout.t_third_shop_order_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_me_order_detail);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mMenu1 = (TextView) findViewById(R.id.menu1); //取消订单
        mMenu2 = (TextView) findViewById(R.id.menu2); //申请退款
        mMenu1.setOnClickListener(this);
        mMenu2.setOnClickListener(this);
        mThirdOrderImageView = (ImageView) findViewById(R.id.third_order_item_image);
        mThirdOrderFavorableNameView = (TextView) findViewById(R.id.third_order_item_favorable_name);
        mThirdOrderFavorableTimeView = (TextView) findViewById(R.id.third_order_item_favorable_time);
        ImageUtil.setImage(this, mThirdOrderImageView, mThirdOrderModel.promotion.thumb);
        mThirdOrderFavorableNameView.setText(mThirdOrderModel.promotion.title);
        mThirdOrderFavorableTimeView.setText(mThirdOrderModel.promotion.startDate + " 至 " + mThirdOrderModel.promotion.endDate);

        OtherUtil.setVisible(mMenu1, mThirdOrderModel.orderStatusCode == 0);
        OtherUtil.setVisible(mMenu2, mThirdOrderModel.getRefund() && mThirdOrderModel.orderStatusCode == 10);
        mMerchantNameView = (TextView) findViewById(R.id.merchant_name);
        mMerchantNameView.setText(mThirdOrderModel.shopTitle);

        mConsigneeView = (TextView) findViewById(R.id.consignee);
        mConsigneeView.setText(mThirdOrderModel.fullname);

        mConsigneeViewTelView = (TextView) findViewById(R.id.consignee_tel);
        mConsigneeViewTelView.setText(mThirdOrderModel.mobile);

        mOrderStatusView = (TextView) findViewById(R.id.order_status);
        mOrderStatusRefundView = (TextView) findViewById(R.id.order_status_refund);

        mOrderTimeView = (TextView) findViewById(R.id.order_time);
        mOrderTimeView.setText(mThirdOrderModel.orderCreateTime);

        mOrderIDView = (TextView) findViewById(R.id.order_id);

        mOrderIDView.setText("订单号：" + mThirdOrderModel.orderID);

        mCaptchaView= (TextView) findViewById(R.id.order_captcha);
        mCaptchaView.setText(mThirdOrderModel.captcha);

        mPaymentIDText= (TextView) findViewById(R.id.order_paymentid_text);
        mPaymentIDText.setText(mThirdOrderModel.paymentIDText);

        mCaptcharlyView= (RelativeLayout) findViewById(R.id.order_captcha_rly);
        OtherUtil.setVisible(mCaptcharlyView, !TextUtils.isEmpty(mThirdOrderModel.captcha));

        mBottomView = (RelativeLayout) findViewById(R.id.bottom_view);
        mThirdOrderBottom = (ThirdOrderBottom) findViewById(R.id.order_bottom);
        mOrderStatusView.setText(mThirdOrderModel.orderStatus);
        mOrderStatusRefundView.setText(TextUtils.isEmpty(mThirdOrderModel.refundStatusText)? "" : mThirdOrderModel.refundStatusText);
        mRefundlly= (LinearLayout) findViewById(R.id.order_status_refund_lly);
        OtherUtil.setVisible(mRefundlly,!TextUtils.isEmpty(mThirdOrderModel.refundStatus) && !TextUtils.isEmpty(mThirdOrderModel.refundStatusText) && !mThirdOrderModel.refundStatus.equals("10"));

        if (mThirdOrderModel.orderStatusCode == 0) {
//            if (OtherUtil.date2TimeStamp(mThirdOrderModel.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") < OtherUtil.date2TimeStamp(mThirdOrderModel.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30".equals(mThirdOrderModel.promotion.status) || !mThirdOrderModel.promotion.getPaymentAvailable()) {
            if (OtherUtil.date2TimeStamp(mThirdOrderModel.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") < OtherUtil.date2TimeStamp(mThirdOrderModel.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30".equals(mThirdOrderModel.promotion.status)) {
//                mOrderStatusView.setText("已关闭");
                mBottomView.setVisibility(View.GONE);
                return;
            }
//            mOrderStatusView.setText("等待付款");
        } else if (mThirdOrderModel.orderStatusCode == 10 || mThirdOrderModel.orderStatusCode == 20) {
//            mOrderStatusView.setText("已付订金");
        } else if (mThirdOrderModel.orderStatusCode == -40) {
//            mOrderStatusView.setText("已关闭");
            mBottomView.setVisibility(View.GONE);
            return;
        } else {
            mBottomView.setVisibility(View.GONE);
            return;
        }
        mThirdOrderBottom.initBottom(mThirdOrderModel);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 获取数据为空或出现错误时，显示提示页面
     */
    public void showEmptyOrErrorView(com.hxqc.mall.core.model.Error mError) {
        final RequestFailView requestFailView = (RequestFailView) new RequestFailViewUtil().getFailView(ThirdShopOrderDetailActivity.this);
        if (mError != null && "车辆不存在".equals(mError.message) && mError.code == 400) {
            requestFailView.setEmptyDescription("数据不存在");
            requestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        } else {
            requestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestFailView.setVisibility(View.GONE);
                    getData();
                }
            });
            requestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        }
        requestFailView.setVisibility(View.VISIBLE);
        this.addContentView(requestFailView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void cancelOrder() {
        finish();
    }

    /**
     * 跳转到活动详情页
     *
     * @param view
     */
    public void toFavorableActiveDetail(View view) {
        ActivitySwitcherThirdPartShop.toSalesItemDetail(mThirdOrderModel.promotion.promotionID, this);
    }

    public void CallService(View view) {
        com.hxqc.mall.core.util.OtherUtil.callHXService(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.menu1)
        {
            //取消订单

            ActivitySwitcherThirdPartShop.toThirShopOrderCancel(mThirdOrderModel.orderID,v.getContext());

        }else if(v.getId()==R.id.menu2)
        {
            //申请退款
            if(mThirdOrderModel.getAlipayRefund())
            {
                ActivitySwitcherThirdPartShop.toThirShopOrderRefund(mThirdOrderModel.orderID,v.getContext());
            }else{
                ActivitySwitcherThirdPartShop.toAlipayRefundInfo(v.getContext());
            }
        }
    }
}
