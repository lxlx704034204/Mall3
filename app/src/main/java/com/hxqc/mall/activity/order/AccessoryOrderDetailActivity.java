package com.hxqc.mall.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.adapter.order.AcessoryRefundItemAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.qr.util.QRCodeUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryOrderDetailBean;
import com.hxqc.mall.thirdshop.accessory.model.SubmitOrderInfo;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.mall.thirdshop.accessory.views.ShopInOrder;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;
import com.hxqc.xiaoneng.ChatManager;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 用品订单详情
 */
public class AccessoryOrderDetailActivity extends NoBackActivity implements View.OnClickListener{

    private TextView mOrderIDView,
            mUserFullname,//姓名
            mUserPhoneNumber,//电话
            mShopName,//门店
            mPaymentType,//支付方式
            mPrepayID,//交易单号
            mOrderStatus,//订单状态
            mAmount,//支付金额
            mOrderCreatTime,//下单时间
            mOrderAmount,//支付金额
            invoiceTitleText,// 发票抬头信息
            invoiceContentText;// 发票内容信息
    private ShopInOrder mShopInOrder;
    private ListViewNoSlide mRefundList;
    TextView mMenu1; //取消订单，申请退款，取消申请退款
    private ScrollView mScrollView;
    private Toolbar mToolbar;
    private RequestFailView mRequestFailView;
    private String mOrderID;
    private AccessoryOrderDetailBean mAccessoryOrderDetailBean;
    private ImageView mQRCode;
    private LinearLayout mRefundlly,mQRcodelly,mPrepayIDlly,mPaymentTypelly;
    private Button mToPay;
 //   private Button mSendcomment;
    private TextView mCaptcha;
    private TextView mQRhint;
    private RelativeLayout mContentView;
    private Button mVerifyComplete;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private RelativeLayout mInvoiceInfoView;
    private TextView mCallService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_order_detail);
        mOrderID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory.ORDER_ID);
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        initView();
        loadData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSharedPreferencesHelper.getOrderChange())
         loadData(true);
    }

    private void loadData(boolean showAnim) {
        new AccessoryApiClient().getAccessoryOrderDetail(mOrderID, new LoadingAnimResponseHandler(this,showAnim) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showFailView();
            }

            @Override
            public void onSuccess(String response) {
                mAccessoryOrderDetailBean = JSONUtils.fromJson(response, AccessoryOrderDetailBean.class);
                if (mAccessoryOrderDetailBean == null)
                    noData();
                else
                    bindData();
            }
        });

    }

    /**
     * 绑定数据
     */
    private void bindData() {
        initVisible();
        mOrderIDView.setText(String.format("订单号：%s", mAccessoryOrderDetailBean.orderID));
        mUserFullname.setText(mAccessoryOrderDetailBean.contactName);
        mUserPhoneNumber.setText(mAccessoryOrderDetailBean.contactPhone);
        mShopName.setText(mAccessoryOrderDetailBean.shopTitle);
        mOrderStatus.setText(mAccessoryOrderDetailBean.orderStatusText);
        mPaymentType.setText(TextUtils.isEmpty(mAccessoryOrderDetailBean.paymentIDText)?"":mAccessoryOrderDetailBean.paymentIDText);
        mPrepayID.setText(mAccessoryOrderDetailBean.transactionNumber);
//        mOrderStatus.setTextColor(mAccessoryOrderDetailBean.getStatusColor(this));
        mAmount.setText(OtherUtil.amountFormat(mAccessoryOrderDetailBean.orderAmount, true));
        mOrderCreatTime.setText(mAccessoryOrderDetailBean.orderCreateTime);
        mOrderAmount.setText(String.format("支付金额:%s", OtherUtil.amountFormat(mAccessoryOrderDetailBean.orderAmount, true)));
        mShopInOrder.fillData(mAccessoryOrderDetailBean.itemList, true);
        if(!TextUtils.isEmpty(mAccessoryOrderDetailBean.captcha))
        mCaptcha.setText(mAccessoryOrderDetailBean.captcha.substring(0,4)+"   "+mAccessoryOrderDetailBean.captcha.substring(4));
        mRefundList.setAdapter(new AcessoryRefundItemAdapter(this, mAccessoryOrderDetailBean.refund));

        if(mAccessoryOrderDetailBean.invoice !=null) {
            invoiceTitleText.setText(String.format("发票抬头:", mAccessoryOrderDetailBean.invoice.invoiceTitleText));
            invoiceContentText.setText(String.format("发票内容:", mAccessoryOrderDetailBean.invoice.invoiceContentText));
        }
        loadQRCode();
    }

    /**
     * 处理显示隐藏
     */
    private void initVisible() {
        OtherUtil.setVisible(mContentView,true);
        OtherUtil.setVisible(mPrepayIDlly,!TextUtils.isEmpty(mAccessoryOrderDetailBean.transactionNumber));
        OtherUtil.setVisible(mRefundlly, mAccessoryOrderDetailBean.refund.size() > 0);
        OtherUtil.setVisible(mMenu1, mAccessoryOrderDetailBean.orderStatus.equals(mAccessoryOrderDetailBean.ORDER_DFK)); //取消订单（待付款状态）
        OtherUtil.setVisible(mToPay,mAccessoryOrderDetailBean.orderStatus.equals(mAccessoryOrderDetailBean.ORDER_DFK));
        OtherUtil.setVisible(mQRcodelly, !TextUtils.isEmpty(mAccessoryOrderDetailBean.captcha));
      //  OtherUtil.setVisible(mSendcomment,mAccessoryOrderDetailBean.orderStatus.equals(mAccessoryOrderDetailBean.ORDER_DDWC)); //去评论（完成状态）
        OtherUtil.setVisible(mQRhint, TextUtils.isEmpty(mAccessoryOrderDetailBean.captcha));
        OtherUtil.setVisible(mPaymentTypelly,!TextUtils.isEmpty(mAccessoryOrderDetailBean.paymentID));
        OtherUtil.setVisible(mVerifyComplete,mAccessoryOrderDetailBean.orderStatus.equals(mAccessoryOrderDetailBean.ORDER_DDFWWC)); //确认收货
        if(mAccessoryOrderDetailBean.invoice == null || mAccessoryOrderDetailBean.invoice.getInvoiceContent().equals("-1")) //如果是不开发票，隐藏抬头,和内容
        {
            OtherUtil.setVisible(invoiceTitleText,false);
            OtherUtil.setVisible(invoiceContentText,false);
            OtherUtil.setVisible(findViewById(R.id.myorder_details_accessory_invoiceinfo_image),false);
        }else{
            OtherUtil.setVisible(findViewById(R.id.myorder_details_accessory_invoiceText),false);
        }
    }

    /**
     *  生成二维码
     */
    private void loadQRCode(){
         int widthPix=getResources().getDimensionPixelOffset(com.hxqc.mall.thirdshop.R.dimen.qr_code_width);
         int heightPix=getResources().getDimensionPixelOffset(com.hxqc.mall.thirdshop.R.dimen.qr_code_height);
        mQRCode.setImageBitmap(QRCodeUtil.createQRImage(mAccessoryOrderDetailBean.captcha,widthPix,heightPix));
    }

    /**
     * 网络异常
     */
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

    private void initView() {
        mContentView= (RelativeLayout) findViewById(R.id.myorder_details_accessory_content_rly);
        OtherUtil.setVisible(mContentView,false);
        mScrollView= (ScrollView) findViewById(R.id.myorder_details_accessory_scroll);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_order_details);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mRequestFailView= (RequestFailView) findViewById(R.id.myorder_details_accessory_fail_view);
        mMenu1= (TextView) findViewById(R.id.menu1); //取消订单
        mOrderIDView= (TextView) findViewById(R.id.myorder_details_accessory_orderID);
        mUserFullname= (TextView) findViewById(R.id.myorder_details_accessory_userFullname);
        mUserPhoneNumber= (TextView) findViewById(R.id.myorder_details_accessory_userPhoneNumber);
        mShopName= (TextView) findViewById(R.id.myorder_details_accessory_shopName);
        mPaymentType= (TextView) findViewById(R.id.myorder_details_accessory_paymentType);
        mPrepayID= (TextView) findViewById(R.id.myorder_details_accessory_prepayID);
        mOrderStatus= (TextView) findViewById(R.id.myorder_details_accessory_orderStatus);
        mAmount= (TextView) findViewById(R.id.myorder_details_accessory_amount);
        mOrderCreatTime= (TextView) findViewById(R.id.myorder_details_accessory_orderCreatTime);
        mShopInOrder= (ShopInOrder) findViewById(R.id.myorder_details_accessory_order);
        mRefundList= (ListViewNoSlide) findViewById(R.id.myorder_details_accessory_refund);
        mQRCode= (ImageView) findViewById(R.id.myorder_details_accessory_QRCode);
        mRefundlly= (LinearLayout) findViewById(R.id.myorder_details_accessory_refund_lly);
        mOrderAmount= (TextView) findViewById(R.id.myorder_details_accessory_orderAmount);
        mToPay= (Button) findViewById(R.id.myorder_details_accessory_topay);
        mVerifyComplete= (Button) findViewById(R.id.myorder_details_accessory_verify_Complete);
//        mSendcomment= (Button) findViewById(R.id.myorder_details_accessory_send_comment);
        mCaptcha= (TextView) findViewById(R.id.myorder_details_accessory_captcha);
        mQRcodelly= (LinearLayout) findViewById(R.id.myorder_details_accessory_QRCode_lly);
        mQRhint= (TextView) findViewById(R.id.myorder_details_accessory_QRCode_hint);
        mPrepayIDlly= (LinearLayout) findViewById(R.id.myorder_details_accessory_prepayID_lly);
        mPaymentTypelly= (LinearLayout) findViewById(R.id.myorder_details_accessory_paymentType_lly);
        mInvoiceInfoView= (RelativeLayout) findViewById(R.id.myorder_details_accessory_invoiceInfo_rly);
        invoiceTitleText= (TextView) findViewById(R.id.myorder_details_accessory_invoiceContentText);
        invoiceContentText= (TextView) findViewById(R.id.myorder_details_accessory_invoiceTitleText);
        mCallService= (TextView) findViewById(R.id.call_service);
        mCallService.setOnClickListener(this);
        mMenu1.setOnClickListener(this);
        mToPay.setOnClickListener(this);
//       mSendcomment.setOnClickListener(this);
        mShopName.setOnClickListener(this);
        mVerifyComplete.setOnClickListener(this);
        mInvoiceInfoView.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.myorder_details_accessory_shopName:
                ActivitySwitcherAccessory.toAMapNvai(this, mAccessoryOrderDetailBean.shopLocation);
               break;
           case R.id.menu1: //取消订单
                ActivitySwitcher.toAccessoryOrderCancel(this,mAccessoryOrderDetailBean.orderID);
//               new NormalDialog(this, "订单取消后将无法恢复，您确定取消吗？") {
//                   @Override
//                   protected void doNext() {
//                       new AccessoryApiClient().cancelAccessoryOrder(mOrderID,"", new LoadingAnimResponseHandler(AccessoryOrderDetailActivity.this) {
//                           @Override
//                           public void onSuccess(String response) {
//                               new BaseSharedPreferencesHelper(AccessoryOrderDetailActivity.this).setOrderChange(true);
//                               finish();
//                           }
//                       });
//                   }
//               }.show();
               break;
           case R.id.myorder_details_accessory_topay: // 去支付
               ArrayList<SubmitOrderInfo> submitOrderInfos = new ArrayList<>();
               SubmitOrderInfo submitOrderInfo = new SubmitOrderInfo(mAccessoryOrderDetailBean.orderID, mAccessoryOrderDetailBean.shopLocation.name);
               submitOrderInfos.add(submitOrderInfo);
               ActivitySwitcherAccessory.toPaySubscription(AccessoryOrderDetailActivity.this, submitOrderInfos, mAccessoryOrderDetailBean.orderAmount);
               break;
//           case R.id.myorder_details_accessory_send_comment: //去评论
//              ActivitySwitcher.toOrderSendComment(v.getContext(), mAccessoryOrderDetailBean.orderID, mAccessoryOrderDetailBean.shopPhoto, mAccessoryOrderDetailBean.shopID);
//               break;
           case R.id.myorder_details_accessory_verify_Complete: //确认收货
               new NormalDialog(this, "商品已收到，确认收货？") {
                   @Override
                   protected void doNext() {
                       new AccessoryApiClient().confirmReceived(mOrderID, new LoadingAnimResponseHandler(AccessoryOrderDetailActivity.this) {
                           @Override
                           public void onSuccess(String response) {
                               new BaseSharedPreferencesHelper(AccessoryOrderDetailActivity.this).setOrderChange(true);
                               finish();
                           }
                       });
                   }
               }.show();
               break;
           case R.id.myorder_details_accessory_invoiceInfo_rly:
//              if(mAccessoryOrderDetailBean.invoice==null || mAccessoryOrderDetailBean.invoice.getInvoiceContent().equals("-1"))
//                 return; //不开发票 无发票详情
               //跳发票详情
               ActivitySwitcher.toInVoiceInfo(AccessoryOrderDetailActivity.this,mAccessoryOrderDetailBean.invoice);
               break;
           case R.id.call_service:
               ChatManager.getInstance().startChatWithNothing();
            //小能
           break;
       }

    }
}
