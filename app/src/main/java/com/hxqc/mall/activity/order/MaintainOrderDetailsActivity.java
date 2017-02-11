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
import com.hxqc.mall.core.adapter.order.MaintenaneDetailsItemAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.MaintainFinishMessage;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.model.order.MaintenanceOrderDetailBean;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.qr.util.QRCodeUtil;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.order.CreateOrder;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;
import com.hxqc.xiaoneng.ChatManager;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;

/**
 * liaoguilong
 * 2016年2月18日 10:02:32
 * 订单详情（保养）
 */
public class MaintainOrderDetailsActivity extends NoBackActivity implements View.OnClickListener {

    public static final String ORDERID = "orderID";
    public static final String ORDERTYPE = "orderType";
//    public static final  int MAINTAINORDER_4SSHOP=0; //0和1   0 4s店
//    public static final  int MAINTAINORDER_FASTREPAIR=1; //   1 快修
    public int mFromType;
    private TextView
            mOrderID, //订单ID
            mUserFullname, //联系人姓名
            mUserPhoneNumber,//联系人电话
            mShopName, //店铺名称
      //    mShopAddress, //店铺地址
       //   mShopTel, //店铺电话
            mAppointmentDate, //预约时间
            mMaintainDate, //维修时间
            mOrderCreatTime, //下单时间
//            mServiceAdviserName, //服务顾问
//            mMechanicName, //维修技师
            mPaymentType, // 支付方式
            mPrepayID, //交易单号
            mOrderStatus, //订单状态
            mScoreCount, //积分 分数
            mScoreUnitPrice, // 积分 抵扣金额
            mGoodsAmount, //配件总额
            mDiscountPackage,//套餐折扣
            mIntegral,//积分抵扣
            mAmount, //总计
            mDiscount, //实付金额
            mWorkCostAmount,//工时总计
            mWorkOrder,
            mOrderAmount,//订单总计
            mRefundStatus,//退款状态
              autoModel,////车辆信息
              accumulatingMileage,// 累计行驶里程
              firstCardTime,// 首次上牌时间
              invoiceTitleText,// 发票抬头信息
              invoiceContentText;// 发票内容信息


    private LinearLayout mWorkOrderlly;
    private Toolbar mToolbar;
    private TextView mMenu1, mMenu2,mMenu3;// 取消订单，申请退款，//取消申请退款
    private Button mToPay,mToSendComment,mVerifyComplete; //支付按钮,去评论
    private ListViewNoSlide mMaintenanceItems;// 保养项目
    private LinearLayout mCouponlly;//优惠卷
    private TextView mPreferentialDescription;//优惠卷
    private TextView mCouponPrice;//优惠卷

    private MaintenanceClient mMaintenanceClient; //API接口

    private MaintenanceOrderDetailBean mMaintenanceOrderDetail; //保养订单详情数据

    private RequestFailView mRequestFailView;

    private ScrollView mScrollView;
    private LinearLayout mScorelly,mScorelly2;//积分的布局
    private ImageView mQRCode;
    private TextView mCaptcha;
    private TextView mQRhint;
    private LinearLayout mQRCodelly;
    private RelativeLayout mContentView;
    private LinearLayout mPackageDiscountlly;//套餐折扣布局
    private RelativeLayout mInvoiceInfoView;
    private TextView mCallService;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_maintain_details);
        mMaintenanceClient = new MaintenanceClient();
        mSharedPreferencesHelper=new SharedPreferencesHelper(this);
        initView();
        loadData(true);
    }

    private void initView() {
        mContentView= (RelativeLayout) findViewById(R.id.myorder_details_maintain_content_rly);
        OtherUtil.setVisible(mContentView, false);
        mScrollView= (ScrollView) findViewById(R.id.myorder_details_maintain_scroll);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_order_details);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
//        mFromType=getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(ORDERTYPE, MAINTAINORDER_FASTREPAIR);
        mMenu1 = (TextView) findViewById(R.id.menu1); //取消订单
//        mMenu2 = (TextView) findViewById(R.id.menu2); //申请退款
//        mMenu3 = (TextView) findViewById(R.id.menu3); //取消申请退款
        mToPay = (Button) findViewById(R.id.myorder_details_maintain_topay);
        mToSendComment = (Button) findViewById(R.id.myorder_details_maintain_send_comment);
        mOrderID = (TextView) findViewById(R.id.myorder_details_maintain_orderID);
        mUserFullname = (TextView) findViewById(R.id.myorder_details_maintain_userFullname);
        mUserPhoneNumber = (TextView) findViewById(R.id.myorder_details_maintain_userPhoneNumber);
        mShopName = (TextView) findViewById(R.id.myorder_details_maintain_shp_shopName);
//        mShopAddress = (TextView) findViewById(R.id.myorder_details_maintain_shp_address);
//        mShopTel = (TextView) findViewById(R.id.myorder_details_maintain_shp_tel);
        mAppointmentDate = (TextView) findViewById(R.id.myorder_details_maintain_appointmentDate);
        mMaintainDate = (TextView) findViewById(R.id.myorder_details_maintain_maintainDate);
//        mServiceAdviserName = (TextView) findViewById(R.id.myorder_details_maintain_serviceAdviser_name);
//        mMechanicName = (TextView) findViewById(R.id.myorder_details_maintain_mechanic_name);
        mPaymentType = (TextView) findViewById(R.id.myorder_details_maintain_paymentType);
        mPrepayID = (TextView) findViewById(R.id.myorder_details_maintain_prepayID);
        mOrderStatus = (TextView) findViewById(R.id.myorder_details_maintain_orderStatus);
        mScoreCount = (TextView) findViewById(R.id.myorder_details_maintain_score_count);
        mScoreUnitPrice = (TextView) findViewById(R.id.myorder_details_maintain_score_unitPrice);
        mGoodsAmount = (TextView) findViewById(R.id.myorder_details_maintain_goodsAmount);
        mAmount = (TextView) findViewById(R.id.myorder_details_maintain_amount);
        mDiscount = (TextView) findViewById(R.id.myorder_details_maintain_discount);
        mDiscountPackage = (TextView) findViewById(R.id.activity_my_order_maintain_discount_package);
        mIntegral = (TextView) findViewById(R.id.activity_my_order_maintain_integral);
        mOrderAmount = (TextView) findViewById(R.id.myorder_details_maintain_orderAmount);
        mWorkCostAmount = (TextView) findViewById(R.id.myorder_details_maintain_workCostAmount);
        mOrderCreatTime = (TextView) findViewById(R.id.myorder_details_maintain_orderCreatTime);
        mWorkOrder = (TextView) findViewById(R.id.myorder_details_maintain_workOrder);
        mWorkOrderlly = (LinearLayout) findViewById(R.id.myorder_details_maintain_workOrder_lly);
        mScorelly= (LinearLayout) findViewById(R.id.myorder_details_maintain_score_lly);
        mScorelly2=(LinearLayout) findViewById(R.id.activity_my_order_maintain_lly05);
        mVerifyComplete= (Button) findViewById(R.id.myorder_details_maintain_verify_Complete);
        mMaintenanceItems = (ListViewNoSlide) findViewById(R.id.myorder_details_maintain_maintenanceItems);

        mCouponlly= (LinearLayout) findViewById(R.id.myorder_details_maintain_coupon_lly);
        mPreferentialDescription= (TextView) findViewById(R.id.myorder_details_maintain_coupon_preferentialDescription);
        mCouponPrice= (TextView) findViewById(R.id.myorder_details_maintain_coupon_price);

        mRequestFailView = (RequestFailView) findViewById(R.id.myorder_details_maintain_fail_view);
        mQRCode= (ImageView) findViewById(R.id.myorder_details_maintain_QRCode);
        mCaptcha= (TextView) findViewById(R.id.myorder_details_maintain_captcha);

        mQRhint= (TextView) findViewById(R.id.myorder_details_maintain_QRCode_hint);
        mQRCodelly= (LinearLayout) findViewById(R.id.myorder_details_maintain_QRCode_lly);
        mPackageDiscountlly= (LinearLayout) findViewById(R.id.activity_my_order_maintain_lly04);
        autoModel= (TextView) findViewById(R.id.myorder_details_maintain_autoModel);
        accumulatingMileage= (TextView) findViewById(R.id.myorder_details_maintain_accumulatingMileage);
        firstCardTime= (TextView) findViewById(R.id.myorder_details_maintain_firstCardTime);
        mInvoiceInfoView= (RelativeLayout) findViewById(R.id.myorder_details_maintain_invoiceInfo_rly);
        invoiceContentText= (TextView) findViewById(R.id.myorder_details_maintain_invoiceContentText);
        invoiceTitleText= (TextView) findViewById(R.id.myorder_details_maintain_invoiceTitleText);
        mRefundStatus= (TextView) findViewById(R.id.myorder_details_maintain_refundStatus);
        mCallService= (TextView) findViewById(R.id.call_service);
        mWorkOrder.setOnClickListener(this);
        mMenu1.setOnClickListener(this);
//        mMenu2.setOnClickListener(this);
//        mMenu3.setOnClickListener(this);
        mToPay.setOnClickListener(this);
        mToSendComment.setOnClickListener(this);
        mShopName.setOnClickListener(this);
        mVerifyComplete.setOnClickListener(this);
        mInvoiceInfoView.setOnClickListener(this);
        mCallService.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSharedPreferencesHelper.getOrderChange())
            loadData(true);
    }

    /**
     * 加载数据
     */
    private void loadData(boolean showAnim) {
       // "60281458697443768643146"
        mMaintenanceClient.orderMaintenanceDetail(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID), new LoadingAnimResponseHandler(MaintainOrderDetailsActivity.this, showAnim) {

            @Override
            public void onSuccess(String response) {
                mMaintenanceOrderDetail = JSONUtils.fromJson(response, MaintenanceOrderDetailBean.class);
                if (mMaintenanceOrderDetail == null)
                    noData();
                else
                    bindData();
            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                showFailView();
            }
        });
    }

    private void showFailView() {
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setEmptyDescription("订单异常");
        mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(true);
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initVisible(){
        OtherUtil.setVisible(mContentView,true);
        OtherUtil.setVisible(mMenu1, mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.ORDER_WFK));
//        OtherUtil.setVisible(mMenu2, mFromType ==MAINTAINORDER_4SSHOP &&  mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.ORDER_YFK) && mMaintenanceOrderDetail.refundStatus.equals(MaintenanceOrderDetailBean.REFUND_DTK));
//        OtherUtil.setVisible(mMenu3, mFromType ==MAINTAINORDER_4SSHOP &&  mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.ORDER_YFK) && mMaintenanceOrderDetail.refundStatus.equals(MaintenanceOrderDetailBean.REFUND_TKZ));
        OtherUtil.setVisible(mToPay, mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.ORDER_WFK));
        OtherUtil.setVisible(mToSendComment, mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.ORDER_DDWC) && ! mMaintenanceOrderDetail.getHasComment());
        OtherUtil.setVisible(mVerifyComplete, mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.ORDER_DDFWWC));
        OtherUtil.setVisible(mScorelly, mMaintenanceOrderDetail.score.count != 0 && mMaintenanceOrderDetail.score.unitPrice != 0); //积分
        OtherUtil.setVisible(mScorelly2, mMaintenanceOrderDetail.score.count != 0 && mMaintenanceOrderDetail.score.unitPrice != 0);//积分
        OtherUtil.setVisible(mCouponlly, !TextUtils.isEmpty(mMaintenanceOrderDetail.coupon.preferentialDescription) && mMaintenanceOrderDetail.coupon.price != 0);//优惠卷
        OtherUtil.setVisible(mWorkOrderlly,!TextUtils.isEmpty(mMaintenanceOrderDetail.workOrderID)); //工单
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_shp_shopName_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.shopPoint.shopName));  //门店
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_orderCreatTime_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.orderCreateTime));  //下单时间
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_appointmentDate_lly),false);  //预约到店时间
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_maintainDate_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.maintainDate));  //实际到店时间
//        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_serviceAdviser_name_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.serviceAdviser.name));  //预约服务顾问
//        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_mechanic_name_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.mechanic.name));  //预约维修技师
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_paymentType_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.paymentType));  //支付方式
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_prepayID_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.prepayID));  //支付单号

        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_autoModel_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.model));  //车辆信息
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_accumulatingMileage_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.drivingDistance));  //累计行驶里程
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_firstCardTime_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.registerTime));  //首次上牌时间
//        OtherUtil.setVisible(mCaptcha,mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.ORDER_DDWC));
        OtherUtil.setVisible(mQRhint,TextUtils.isEmpty(mMaintenanceOrderDetail.captcha));
        OtherUtil.setVisible(mQRCodelly,!TextUtils.isEmpty(mMaintenanceOrderDetail.captcha));
        OtherUtil.setVisible(mPackageDiscountlly,(mMaintenanceOrderDetail.amount - mMaintenanceOrderDetail.discount)!=0);
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_refundStatus_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.refundStatusText));
        if(mMaintenanceOrderDetail.orderInvoice == null || mMaintenanceOrderDetail.orderInvoice.getInvoiceContent().equals("-1")) //如果是不开发票，隐藏抬头,和内容
        {
            OtherUtil.setVisible(invoiceTitleText,false);
            OtherUtil.setVisible(invoiceContentText,false);
            OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_invoiceinfo_image),false);
        }else{
             OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_invoiceText),false);
        }
    }

    /**
     * 绑定数据
     */
    private void bindData() {
        initVisible();
        mOrderStatus.setText(mMaintenanceOrderDetail.getStatusText());
      //  mOrderStatus.setTextColor(mMaintenanceOrderDetail.getStatusColor(this));
        mOrderID.setText(String.format("订单号:%s", mMaintenanceOrderDetail.orderID));
        mUserFullname.setText(mMaintenanceOrderDetail.userFullname);
        mUserPhoneNumber.setText(mMaintenanceOrderDetail.userPhoneNumber);
        mShopName.setText(mMaintenanceOrderDetail.shopPoint.shopName);
        mShopName.setTag(new PickupPointT(mMaintenanceOrderDetail.shopPoint.address,mMaintenanceOrderDetail.shopPoint.latitude+"",mMaintenanceOrderDetail.shopPoint.longitude+"",mMaintenanceOrderDetail.shopPoint.tel));
//        mShopAddress.setText(mMaintenanceOrderDetail.shopPoint.address);
//        mShopTel.setText(mMaintenanceOrderDetail.shopPoint.tel);
        mAppointmentDate.setText(mMaintenanceOrderDetail.appointmentDate);
        mMaintainDate.setText(mMaintenanceOrderDetail.maintainDate);
        mOrderCreatTime.setText(mMaintenanceOrderDetail.orderCreateTime);
//        mServiceAdviserName.setText(mMaintenanceOrderDetail.serviceAdviser.name);
//        mMechanicName.setText(mMaintenanceOrderDetail.mechanic.name);
        mPaymentType.setText(mMaintenanceOrderDetail.paymentType);
        mPrepayID.setText(mMaintenanceOrderDetail.prepayID);
        mWorkOrder.setText(mMaintenanceOrderDetail.workOrderID);
        autoModel.setText(mMaintenanceOrderDetail.model);
        accumulatingMileage.setText(mMaintenanceOrderDetail.drivingDistance);
        firstCardTime.setText(mMaintenanceOrderDetail.registerTime);
        mRefundStatus.setText(mMaintenanceOrderDetail.refundStatusText);
        if(mMaintenanceOrderDetail.orderInvoice != null) {
            invoiceTitleText.setText(String.format("发票抬头:", mMaintenanceOrderDetail.orderInvoice.invoiceTitleText));
            invoiceContentText.setText(String.format("发票内容:", mMaintenanceOrderDetail.orderInvoice.invoiceContentText));
        }

        if(!TextUtils.isEmpty(mMaintenanceOrderDetail.captcha))
            mCaptcha.setText(mMaintenanceOrderDetail.captcha.substring(0,4)+"   "+mMaintenanceOrderDetail.captcha.substring(4));

        mScoreCount.setText(String.format("已使用的%d积分", mMaintenanceOrderDetail.score.count));
        mScoreUnitPrice.setText(String.format("抵扣 %s", OtherUtil.amountFormat(mMaintenanceOrderDetail.score.unitPrice * mMaintenanceOrderDetail.score.count, true)));
        mGoodsAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.goodsAmount, true));
        mAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.amount, true));//应付总额
        mDiscount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.orderAmount, true));//实付金额
        mIntegral.setText(String.format("¥ -%.2f", mMaintenanceOrderDetail.score.unitPrice)); //积分抵扣
         mDiscountPackage.setText(String.format("¥ -%.2f", mMaintenanceOrderDetail.amount - mMaintenanceOrderDetail.discount)); //套餐折扣
//        if(mMaintenanceOrderDetail.orderStatus.equals(MaintenanceOrderDetailBean.WAIT)) //待付款
         mOrderAmount.setText(String.format("支付金额:%s", OtherUtil.amountFormat(mMaintenanceOrderDetail.orderAmount, true)));
//        else
//         mOrderAmount.setText(String.format("已付金额:%s", OtherUtil.amountFormat(mMaintenanceOrderDetail.orderPaid, true)));
        mWorkCostAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.workCostAmount, true));
        mMaintenanceItems.setAdapter(new MaintenaneDetailsItemAdapter(mMaintenanceOrderDetail.maintenanceItems, this));
        mPreferentialDescription.setText(mMaintenanceOrderDetail.coupon.preferentialDescription);
        mCouponPrice.setText(String.format("抵扣 %s", OtherUtil.amountFormat(mMaintenanceOrderDetail.coupon.price, true)));
        loadQRCode();
        mScrollView.smoothScrollTo(0,0);
    }

    /**
     *  生成二维码
     */
    private void loadQRCode(){
        final int widthPix=getResources().getDimensionPixelOffset(com.hxqc.mall.thirdshop.R.dimen.qr_code_width);
        final int heightPix=getResources().getDimensionPixelOffset(com.hxqc.mall.thirdshop.R.dimen.qr_code_height);
        mQRCode.setImageBitmap(QRCodeUtil.createQRImage(mMaintenanceOrderDetail.captcha, widthPix, heightPix));
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
        if (v.getId() == R.id.myorder_details_maintain_workOrder) {
            UserApiClient mUserApiClient=new UserApiClient();
            String URL = mUserApiClient.getWorkOrderUrl(mMaintenanceOrderDetail.workOrderID, mMaintenanceOrderDetail.erpCode);
            ActivitySwitchBase.toH5Activity(v.getContext(), "查看工单", URL);
        }
        else if (v.getId() == R.id.myorder_details_maintain_shp_shopName) { //去导航
            if (v.getTag() == null) return;
            PickupPointT msShopLocation = (PickupPointT) v.getTag();
            ActivitySwitcherThirdPartShop.toAMapNvai(v.getContext(), 0, msShopLocation);
        //    ActivitySwitcherThirdPartShop.toShopDetails(this, mMaintenanceOrderDetail.shopPoint.id);
        }
        else if (v.getId() == R.id.menu1) { //取消订单
            ActivitySwitcher.toOrderCancel(v.getContext(), getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID));
        }
//        else if (v.getId() == R.id.menu2) { //申请退款
//            ActivitySwitcher.toOrderRefund(v.getContext(),getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID));
//        } else if (v.getId() == R.id.menu3) { //取消申请退款
//            new NormalDialog(this, "取消申请退款后将不能再申请退款，您确定取消吗？") {
//                @Override
//                protected void doNext() {
//                    mMaintenanceClient.cancaelRefundOrder(mMaintenanceOrderDetail.orderID, new DialogResponseHandler(MaintainOrderDetailsActivity.this, getResources().getString(R.string.me_submitting)) {
//                        @Override
//                        public void onSuccess(String response) {
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//                                    ToastHelper.showGreenToast(getApplicationContext(), "退款已取消");
//                                    new SharedPreferencesHelper(MaintainOrderDetailsActivity.this).setOrderChange(true);
//                                    MaintainOrderDetailsActivity.this.finish();
//                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                                }
//                            }, 2300);
//                        }
//                    });
//                }
//            }.show();
//        }
        else if (v.getId() == R.id.myorder_details_maintain_topay) {
            //去支付
            CreateOrder createOrder = new CreateOrder();
            createOrder.orderID = mMaintenanceOrderDetail.orderID;
            createOrder.amount=mMaintenanceOrderDetail.orderAmount;
            ActivitySwitcherMaintenance.toPayChoice(v.getContext(), createOrder,"","3");
        }else if(v.getId()==R.id.myorder_details_maintain_send_comment){ //去评论
             ActivitySwitcher.toOrderSendComment(v.getContext(), mMaintenanceOrderDetail.orderID, mMaintenanceOrderDetail.shopPhoto,mMaintenanceOrderDetail.shopPoint.id);
        }else if(v.getId() ==R.id.myorder_details_maintain_verify_Complete){
            verifyCompleteOrder();
        }else if(v.getId()==R.id.myorder_details_maintain_invoiceInfo_rly)
        {
//             if(mMaintenanceOrderDetail.invoice==null || mMaintenanceOrderDetail.invoice.getInvoiceContent().equals("-1"))
//                 return; //不开发票 无发票详情
            //跳发票详情
            ActivitySwitcher.toInVoiceInfo(MaintainOrderDetailsActivity.this,mMaintenanceOrderDetail.orderInvoice);
        }else if(v.getId()==R.id.call_service)
        {
             //小能
            ChatManager.getInstance().startChatWithNothing();
        }
    }



    //确认订单
    private void verifyCompleteOrder(){
       new NormalDialog(this, "保养已结束，确定服务完成？") {
                @Override
                protected void doNext() {
                    mMaintenanceClient.verifyComplete(mMaintenanceOrderDetail.orderID, new LoadingAnimResponseHandler(MaintainOrderDetailsActivity.this,true) {
                        @Override
                        public void onSuccess(String response) {
                            MaintainFinishMessage message=JSONUtils.fromJson(response, MaintainFinishMessage.class);
                            ToastHelper.showGreenToast(MaintainOrderDetailsActivity.this,message.message);
                            new SharedPreferencesHelper(MaintainOrderDetailsActivity.this).setOrderChange(true);
                            finish();
                        }
                    });
                }
            }.show();
    }
}
