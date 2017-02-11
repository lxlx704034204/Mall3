package com.hxqc.mall.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.model.order.BaseOrder;
import com.hxqc.mall.core.model.order.SeckillOrderDetailBean;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.Order.OrderDescription;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DateUtil;
import com.hxqc.util.JSONUtils;
import com.hxqc.xiaoneng.ChatManager;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;

/**
 * 廖贵龙
 * 2016年5月16日 15:37:54
 * 特价车订单详情
 */
public class SeckillOrderDetailActivity extends NoBackActivity implements View.OnClickListener{
    public static final String ORDERID = "orderID";
    private TextView mOrderID ,//订单ID
            mUsername, //联系
            mPhoneNumber, //联系人电话
            mShopTitle,//商家名称
            mOrderStatus, //订单状态
            mOrderCreateTime, //下单时间
            mMethod, //购车方式
            mInsurance, //保险
            mDecorate, //店内装饰
            mOrderAmount,//订单总计
            mOrderPaid,//已付款金额
            mOrderUnpaid,//待付款金额
            mCaptcha,//验证码
            mpaymentIDText,//付款方式
            mMoney;
    private OrderDescription mOrderDescription;
    private LinearLayout mOrderPaidlly,mOrderUnpaidlly,mCaptchally;
    private Button mTopay;
    private Toolbar mToolbar;
    private TextView mMenu,mMenu1; //取消订单，申请退款
    private RequestFailView mRequestFailView;
    private SeckillOrderDetailBean mSeckillOrderDetailBean;
    private RelativeLayout mContentView;
    private TextView mCallService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seckill_order_detail);
        initView();
        loadData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(new BaseSharedPreferencesHelper(this).getOrderChange())
            loadData(true);
    }

    public void initView(){
        mContentView= (RelativeLayout) findViewById(R.id.seckill_order_detail_content_rly);
        OtherUtil.setVisible(mContentView,false);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_seckill_order_detail);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mMenu= (TextView) findViewById(R.id.menu);
        mMenu1= (TextView) findViewById(R.id.menu1);
        mOrderID= (TextView) findViewById(R.id.seckill_order_detail_orderid);
        mUsername= (TextView) findViewById(R.id.seckill_order_detail_username);
        mPhoneNumber= (TextView) findViewById(R.id.seckill_order_detail_phoneNumber);
        mShopTitle= (TextView) findViewById(R.id.seckill_order_detail_shopInfo_shopTitle);
        mOrderStatus= (TextView) findViewById(R.id.seckill_order_detail_orderStatus);
        mOrderCreateTime= (TextView) findViewById(R.id.seckill_order_detail_orderCreateTime);
        mMethod= (TextView) findViewById(R.id.seckill_order_detail_method);
        mInsurance= (TextView) findViewById(R.id.seckill_order_detail_insurance);
        mDecorate= (TextView) findViewById(R.id.seckill_order_detail_decorate);
        mOrderAmount= (TextView) findViewById(R.id.seckill_order_detail_orderAmount);
        mOrderPaid= (TextView) findViewById(R.id.seckill_order_detail_orderPaid);
        mOrderUnpaid= (TextView) findViewById(R.id.seckill_order_detail_orderUnpaid);
        mMoney= (TextView) findViewById(R.id.seckill_order_detail_money);
        mOrderDescription= (OrderDescription) findViewById(R.id.seckill_order_detail_description);
        mOrderPaidlly= (LinearLayout) findViewById(R.id.seckill_order_detail_orderPaid_lly);
        mOrderUnpaidlly= (LinearLayout) findViewById(R.id.seckill_order_detail_orderUnpaid_lly);
        mTopay= (Button) findViewById(R.id.seckill_order_detail_topay);
        mRequestFailView = (RequestFailView) findViewById(R.id.seckill_order_detail_fail_view);
        mCaptcha = (TextView) findViewById(R.id.seckill_order_detail_captcha);
        mCaptchally = (LinearLayout) findViewById(R.id.seckill_order_detail_captcha_lly);
        mCallService= (TextView) findViewById(R.id.call_service);
        mpaymentIDText= (TextView) findViewById(R.id.seckill_order_detail_paymentIDText);
        mCallService.setOnClickListener(this);
        mTopay.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mMenu1.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
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
        mRequestFailView.setEmptyDescription("订单数据不存在");
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.menu)
        {
//            new NormalDialog(this, "订单取消后将无法恢复，您确定取消吗？") {
//                @Override
//                protected void doNext() {
//                    new ThirdPartShopClient().cancelSeckillOrder(mSeckillOrderDetailBean.orderID,"", new LoadingAnimResponseHandler(SeckillOrderDetailActivity.this) {
//                        @Override
//                        public void onSuccess(String response) {
//                            new BaseSharedPreferencesHelper(SeckillOrderDetailActivity.this).setOrderChange(true);
//                            finish();
//                        }
//                    });
//                }
//            }.show();
            //取消订单
            ActivitySwitcher.toSeckillOrderCancel(SeckillOrderDetailActivity.this,mSeckillOrderDetailBean.orderID);
        }else if(v.getId()==R.id.menu1){
            //申请退款
            if(mSeckillOrderDetailBean.getAlipayRefund())
            {
                ActivitySwitcher.toSeckillOrderRefund(SeckillOrderDetailActivity.this, mSeckillOrderDetailBean.orderID);
            }else {
                ActivitySwitcher.toAlipayRefundInfo(SeckillOrderDetailActivity.this); //支付宝退款详情页
            }
        }else if(v.getId()==R.id.seckill_order_detail_topay){
            //去支付
            ActivitySwitcherThirdPartShop.toPayDeposit(mSeckillOrderDetailBean.subscription, mSeckillOrderDetailBean.orderID, mSeckillOrderDetailBean.shopInfo.shopTel, SeckillOrderDetailActivity.this);
        }else if(v.getId()==R.id.call_service)
        {
            //小能
            ChatManager.getInstance().startChatWithNothing();
        }

    }

    public void loadData(boolean showAnim){
        new ThirdPartShopClient().getSeckillOrderDetail(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID), new LoadingAnimResponseHandler(SeckillOrderDetailActivity.this,showAnim) {
            @Override
            public void onSuccess(String response) {
                //绑定数据
                mSeckillOrderDetailBean= JSONUtils.fromJson(response, SeckillOrderDetailBean.class);
                if(mSeckillOrderDetailBean !=null)
                    bindData();
                else
                    noData();
            }
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                showFailView();
            }
        });

    }
    public void bindData(){
        initVisible();
        mOrderID.setText(String.format("订单号:%s", mSeckillOrderDetailBean.orderID));
        mUsername.setText(mSeckillOrderDetailBean.username);
        mPhoneNumber.setText(mSeckillOrderDetailBean.phoneNumber);
        mShopTitle.setText(mSeckillOrderDetailBean.shopInfo.shopTitle);
         mOrderStatus.setText(mSeckillOrderDetailBean.getOrderStatus());
       // mOrderStatus.setTextColor(mSeckillOrderDetailBean.getStatusColor(SeckillOrderDetailActivity.this));
        mOrderCreateTime.setText(mSeckillOrderDetailBean.orderCreateTime);
        mMethod.setText(mSeckillOrderDetailBean.method);
        mInsurance.setText(mSeckillOrderDetailBean.isInsurance());
        mDecorate.setText(mSeckillOrderDetailBean.decorate);
        mOrderAmount.setText(OtherUtil.amountFormat(mSeckillOrderDetailBean.orderAmount,true));
        mOrderPaid.setText(OtherUtil.amountFormat(mSeckillOrderDetailBean.orderPaid,true));
        mOrderUnpaid.setText(OtherUtil.amountFormat(mSeckillOrderDetailBean.orderUnpaid, true));
        mCaptcha.setText(mSeckillOrderDetailBean.captcha);
        if(mSeckillOrderDetailBean.orderStatusCode.equals(mSeckillOrderDetailBean.ORDER_YFK)) //已付款
            mMoney.setText(String.format("已支付:%s",OtherUtil.amountFormat(mSeckillOrderDetailBean.subscription,false)));
        else
            mMoney.setText(String.format("支付订金:%s", OtherUtil.amountFormat(mSeckillOrderDetailBean.subscription, false)));
        BaseOrder mBaseOrder=new BaseOrder();
        mBaseOrder.itemColor=mSeckillOrderDetailBean.appearance;
        mBaseOrder.itemInterior=mSeckillOrderDetailBean.interior;
        mBaseOrder.itemPrice=mSeckillOrderDetailBean.itemPrice;
        mBaseOrder.itemName=mSeckillOrderDetailBean.itemName;
        mBaseOrder.itemThumb=mSeckillOrderDetailBean.itemThumb;
        mOrderDescription.initOrderDescription(this, mBaseOrder,null);
        mpaymentIDText.setText(mSeckillOrderDetailBean.paymentIDText);
    }

    public void initVisible(){
        OtherUtil.setVisible(mContentView,true);
        OtherUtil.setVisible(mMenu,mSeckillOrderDetailBean.orderStatusCode.equals(mSeckillOrderDetailBean.ORDER_WFK)); //待付款
        OtherUtil.setVisible(mMenu1,mSeckillOrderDetailBean.orderStatusCode.equals(mSeckillOrderDetailBean.ORDER_YFK) && mSeckillOrderDetailBean.getRefund()); //已付款 并且可退款
        OtherUtil.setVisible(mOrderPaidlly,mSeckillOrderDetailBean.orderStatusCode.equals(mSeckillOrderDetailBean.ORDER_YFK)); //已付款
        OtherUtil.setVisible(mOrderUnpaidlly,mSeckillOrderDetailBean.orderStatusCode.equals(mSeckillOrderDetailBean.ORDER_YFK)); //已付款
        OtherUtil.setVisible(mTopay,mSeckillOrderDetailBean.orderStatusCode.equals(mSeckillOrderDetailBean.ORDER_WFK)); //待付款
        OtherUtil.setVisible(mCaptchally,!TextUtils.isEmpty(mSeckillOrderDetailBean.captcha)); //验证码
    }

//    /**
//     * 满足3个月退款时间以内
//     * @param orderCreateTime
//     * @return
//     */
//    public boolean isInsideRefundTime(String orderCreateTime){
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -3);
//        if(DateUtil.str2Date(orderCreateTime).after(calendar.getTime()))
//            return true;
//        return false;
//    }
}
