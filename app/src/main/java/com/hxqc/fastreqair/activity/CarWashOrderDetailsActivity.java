package com.hxqc.fastreqair.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CarWashOrderDetailsBean;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import hxqc.mall.R;

/**
 * liaoguilong
 * 2016年5月18日 11:20:39
 * 洗车订单详情
 */
public class CarWashOrderDetailsActivity extends NoBackActivity implements View.OnClickListener {

    public final static String ORDERID="orderID";
    private Toolbar mToolbar;
 //   private TextView mMenu;
    private TextView
            mOrderIDView,
            mPicView,
            mUserNameView,
            mPhotoView,
            mShopView,
            mOrderTimeView,
            mPaymentView,
            mTradeIDView,
            mOrderStatusView,
            mMoneyView;

    private RequestFailView mRequestFailView;
    private Button mSendComment;
    private CarWashOrderDetailsBean mCarWashOrderDetailsBean;
    private CarWashApiClient mCarWashApiClient;
    private String mOrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_order_details);
        mCarWashApiClient=new CarWashApiClient();
        initView();
      //  loadData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(true);
    }

    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_carwash_order_detail);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
       // mMenu = (TextView) findViewById(R.id.menu);
        mOrderID=getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID);
        mOrderIDView= (TextView) findViewById(R.id.carwash_order_detail_orderid);
        mPicView = (TextView) findViewById(R.id.carwash_order_detail_pic);
        mUserNameView = (TextView) findViewById(R.id.carwash_order_detail_username);
        mPhotoView = (TextView) findViewById(R.id.carwash_order_detail_photo);
        mShopView = (TextView) findViewById(R.id.carwash_order_detail_shop);
        mOrderTimeView = (TextView) findViewById(R.id.carwash_order_detail_ordertime);
        mPaymentView = (TextView) findViewById(R.id.carwash_order_detail_payment);
        mTradeIDView = (TextView) findViewById(R.id.carwash_order_detail_tradeID);
        mOrderStatusView = (TextView) findViewById(R.id.carwash_order_detail_orderstatus);
        mMoneyView = (TextView) findViewById(R.id.carwash_order_detail_money);
        mSendComment = (Button) findViewById(R.id.carwash_order_detail_send_comment);
        mRequestFailView = (RequestFailView) findViewById(R.id.carwash_order_detail_fail_view);
        mSendComment.setOnClickListener(this);
     //   mMenu.setOnClickListener(this);
    }

    private void showFailView() {
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setEmptyDescription("网络连接失败");
        mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(true);
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    /**
     * 无效的订单
     */
    private void noData() {
        mRequestFailView.setEmptyDescription("订单数据错误");
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    }


    public void loadData(boolean showAnim) {
        //调用接口
        mCarWashApiClient.getCarWashDetail(mOrderID, new LoadingAnimResponseHandler(CarWashOrderDetailsActivity.this, showAnim) {
            @Override
            public void onSuccess(String response) {
                mCarWashOrderDetailsBean = JSONUtils.fromJson(response, CarWashOrderDetailsBean.class);
                if (mCarWashOrderDetailsBean == null)
                    noData();
                else
                    bindData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showFailView();
            }
        });

    }

    /**
     * 绑定数据
     */
    private void bindData() {
        initVisible();
        mOrderIDView.setText(String.format("订单号：%s",mCarWashOrderDetailsBean.orderID));
        mPicView.setText(OtherUtil.amountFormat(mCarWashOrderDetailsBean.orderAmount,true));
        mShopView.setText(mCarWashOrderDetailsBean.shopName);
        mOrderTimeView.setText(mCarWashOrderDetailsBean.orderCreatTime);
        mPaymentView.setText(mCarWashOrderDetailsBean.paymentID);
        mTradeIDView.setText(mCarWashOrderDetailsBean.tradeID);
        mOrderStatusView.setText(mCarWashOrderDetailsBean.orderStatusText);
        mMoneyView.setText(String.format("已支付:%s", OtherUtil.amountFormat(mCarWashOrderDetailsBean.actualPayment,true)));

        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                mUserNameView.setText(meData.fullname); //待定
                mPhotoView.setText(meData.phoneNumber);//待定
            }

            @Override
            public void onFinish() {

            }
        }, true);

    }

    public void initVisible(){
          OtherUtil.setVisible(mSendComment,mCarWashOrderDetailsBean.orderStatus.equals(mCarWashOrderDetailsBean.ORDER_DDWC) && ! mCarWashOrderDetailsBean.getHasComment());
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.menu) {
//            //申请退款
//            new NormalDialog(this, "一旦退款成功，订单取消后将无法恢复。该订单已付金额将返还到银行卡或POS机线下退款") {
//                @Override
//                protected void doNext() {
//                    mCarWashApiClient.carWashRefund(mOrderID, new DialogResponseHandler(CarWashOrderDetailsActivity.this, getResources().getString(R.string.me_submitting)) {
//                        @Override
//                        public void onSuccess(String response) {
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//                                    ToastHelper.showGreenToast(getApplicationContext(), "退款成功");
//                                    new SharedPreferencesHelper(CarWashOrderDetailsActivity.this).setOrderChange(true);
//                                    CarWashOrderDetailsActivity.this.finish();
//                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                                }
//                            }, 2300);
//                        }
//                    });
//                }
//            }.show();
//        } else
        if (v.getId() == R.id.carwash_order_detail_send_comment) {
            //发表评论
            CarWashActivitySwitcher.CarWashSendComment(CarWashOrderDetailsActivity.this,mOrderID,mCarWashOrderDetailsBean.shopPhoto,mCarWashOrderDetailsBean.shopID,mCarWashOrderDetailsBean.shopName,mCarWashOrderDetailsBean.actualPayment);
        }
    }
}
