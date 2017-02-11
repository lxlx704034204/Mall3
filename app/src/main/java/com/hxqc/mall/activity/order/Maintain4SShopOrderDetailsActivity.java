package com.hxqc.mall.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.adapter.order.Maintenane4SShopDetailsItemAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.model.order.Maintenance4SShopOrderDetailBean;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.mall.thirdshop.maintenance.model.order.CreateOrder;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.maintenance.views.FourSShopMaintenanceFirstChildV5;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;
import com.hxqc.xiaoneng.ChatManager;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;


/**
 * 2016年2月18日 10:02:32
 * 廖贵龙
 * 保养订单详情（4s）
 */
public class Maintain4SShopOrderDetailsActivity extends NoBackActivity implements View.OnClickListener{
    public static final String ORDERID = "orderID";
    private TextView
            mOrderID, //订单ID
            mUserFullname, //联系人姓名
            mUserPhoneNumber,//联系人电话
            mShopName, //店铺名称
            mAppointmentDate, //预约时间
            mMaintainDate, //维修时间
            mOrderCreatTime, //下单时间
//            mServiceAdviserName, //服务顾问
//            mMechanicName, //维修技师
            mPaymentType, // 支付方式
            mPrepayID, //交易单号
            mOrderStatus, //订单状态
            mOrderAmount,//订单总计
            mRefundStatus,//退款状态

            mScoreUnitPrice, // 积分 抵扣金额
            mScoreCount,//积分数
            mIntegral,//积分抵扣

            mCouponPrice, // 优惠卷 抵扣金额
            mCouponDescription,//优惠卷优惠说明
            mCoupon,//优惠卷抵扣
            mDiscount,//套餐折扣
//             mWorkCostAmount,//工时费总额
            mSumAmount, //订单总额
            mAmount, //应付金额
            mGoodSunAmount,//配件总额
            mWorkCostSunAmount,//工时费总额

            mWorkOrder,//工单号
            autoModel,////车辆信息
            mPlateNum,//车牌号
            accumulatingMileage,// 累计行驶里程
            firstCardTime;// 首次上牌时间

    private TextView mMenu1, mMenu2,mMenu3;// 取消订单，申请退款，//取消申请退款

    private Button mToPay;//支付按钮
    private   Button  mToSendComment;//,去评论
//    private   Button mVerifyComplete; //确认订单

    private LinearLayout mMaintenanceItems;// 保养项目

    private MaintenanceClient mMaintenanceClient; //API接口

    private Maintenance4SShopOrderDetailBean mMaintenanceOrderDetail; //保养订单详情数据
    private RequestFailView mRequestFailView;
    private Toolbar mToolbar;
    private RelativeLayout mContentView;
    private ScrollView mScrollView;

    private ImageView mQRCode;
    private TextView mCaptcha;
    private LinearLayout mScorelly,mScorelly2; //积分
    private LinearLayout mCouponlly,mCouponlly2; //优惠卷
    private LinearLayout mDiscountlly; //套餐折扣lly

    private TextView mMaintenanceItemsCountText;
    private TextView mCallService;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_4sshop_order_details);
        mMaintenanceClient = new MaintenanceClient();
        mSharedPreferencesHelper=new SharedPreferencesHelper(this);
        initView();
        loadData(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mSharedPreferencesHelper.getOrderChange())
            loadData(true);
    }

    private void initView() {
        mContentView= (RelativeLayout) findViewById(R.id.myorder_details_maintain_4s_content_rly);
        mScrollView= (ScrollView) findViewById(R.id.myorder_details_maintain_4s_scroll);
        OtherUtil.setVisible(mContentView, false);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_order_details);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mMenu1 = (TextView) findViewById(R.id.menu1); //取消订单
        mMenu2 = (TextView) findViewById(R.id.menu2); //申请退款
//        mMenu3 = (TextView) findViewById(R.id.menu3); //取消申请退款
        mToPay = (Button) findViewById(R.id.myorder_details_maintain_4s_topay);
        mToSendComment = (Button) findViewById(R.id.myorder_details_maintain_4s_send_comment);
        mOrderID = (TextView) findViewById(R.id.myorder_details_maintain_4s_orderID);
        mUserFullname = (TextView) findViewById(R.id.myorder_details_maintain_4s_userFullname);
        mUserPhoneNumber = (TextView) findViewById(R.id.myorder_details_maintain_4s_userPhoneNumber);
        mShopName = (TextView) findViewById(R.id.myorder_details_maintain_4s_shp_shopName);
        mAppointmentDate = (TextView) findViewById(R.id.myorder_details_maintain_4s_appointmentDate);
        mMaintainDate = (TextView) findViewById(R.id.myorder_details_maintain_4s_maintainDate);
//        mServiceAdviserName = (TextView) findViewById(R.id.myorder_details_maintain_4s_serviceAdviser_name);
//        mMechanicName = (TextView) findViewById(R.id.myorder_details_maintain_4s_mechanic_name);
        mPaymentType = (TextView) findViewById(R.id.myorder_details_maintain_4s_paymentType);
        mPrepayID = (TextView) findViewById(R.id.myorder_details_maintain_4s_prepayID);
        mOrderStatus = (TextView) findViewById(R.id.myorder_details_maintain_4s_orderStatus);
        mOrderAmount = (TextView) findViewById(R.id.myorder_details_maintain_4s_orderAmount);
        mOrderCreatTime = (TextView) findViewById(R.id.myorder_details_maintain_4s_orderCreatTime);
//        mVerifyComplete= (Button) findViewById(R.id.myorder_details_maintain_4s_verify_Complete);
        mMaintenanceItemsCountText = (TextView) findViewById(R.id.myorder_details_maintain_4s_maintenance_itemCountText);
        mMaintenanceItems = (LinearLayout) findViewById(R.id.myorder_details_maintain_4s_maintenanceItems);
        mRequestFailView = (RequestFailView) findViewById(R.id.myorder_details_maintain_4s_fail_view);
        autoModel= (TextView) findViewById(R.id.myorder_details_maintain_4s_autoModel);
        accumulatingMileage= (TextView) findViewById(R.id.myorder_details_maintain_4s_accumulatingMileage);
        firstCardTime= (TextView) findViewById(R.id.myorder_details_maintain_4s_firstCardTime);
        mWorkOrder = (TextView) findViewById(R.id.myorder_details_maintain_4s_workOrder);
        mRefundStatus= (TextView) findViewById(R.id.myorder_details_maintain_4s_refundStatus);

        mScoreCount= (TextView) findViewById(R.id.myorder_details_maintain_4s_score_count);
        mScoreUnitPrice= (TextView) findViewById(R.id.myorder_details_maintain_4s_unitPrice);
        mIntegral= (TextView) findViewById(R.id.myorder_details_maintain_4s_integral);

        mCouponDescription= (TextView) findViewById(R.id.myorder_details_maintain_4s_coupon_preferentialDescription);
        mCouponPrice= (TextView) findViewById(R.id.myorder_details_maintain_4s_coupon_price);
        mCoupon= (TextView) findViewById(R.id.myorder_details_maintain_4s_coupon);

        mSumAmount= (TextView) findViewById(R.id.myorder_details_maintain_4s_sunAmount);
        mAmount= (TextView) findViewById(R.id.myorder_details_maintain_4s_amount);

        mQRCode= (ImageView) findViewById(R.id.myorder_details_maintain_4s_QRCode);
        mCaptcha= (TextView) findViewById(R.id.myorder_details_maintain_4s_captcha);

        mScorelly= (LinearLayout) findViewById(R.id.myorder_details_maintain_4s_score_lly);
        mScorelly2=(LinearLayout) findViewById(R.id.myorder_details_maintain_4s_lly02);

        mCouponlly= (LinearLayout) findViewById(R.id.myorder_details_maintain_4s_coupon_lly);
        mCouponlly2=(LinearLayout) findViewById(R.id.myorder_details_maintain_4s_lly03);

        mDiscount= (TextView) findViewById(R.id.myorder_details_maintain_4s_discount);
        mDiscountlly= (LinearLayout) findViewById(R.id.myorder_details_maintain_4s_lly04);

        mCallService= (TextView) findViewById(R.id.call_service);

//        mWorkCostAmount= (TextView) findViewById(R.id.myorder_details_maintain_4s_maintenance_workCostAmount);

        mGoodSunAmount= (TextView) findViewById(R.id.myorder_details_maintain_4s_goodSunAmount);

        mWorkCostSunAmount= (TextView) findViewById(R.id.myorder_details_maintain_4s_workCostSunAmount);

        mPlateNum= (TextView) findViewById(R.id.myorder_details_maintain_4s_CarNumber);

        mMenu1.setOnClickListener(this);
        mMenu2.setOnClickListener(this);
//        mMenu3.setOnClickListener(this);
        mToPay.setOnClickListener(this);
        mToSendComment.setOnClickListener(this);
        mShopName.setOnClickListener(this);
//        mVerifyComplete.setOnClickListener(this);
        mWorkOrder.setOnClickListener(this);
        mCallService.setOnClickListener(this);
    }


    /**
     * 加载数据
     */
    private void loadData(boolean showAnim) {
        mMaintenanceClient.orderMaintenance4SShopDetail2(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID), new LoadingAnimResponseHandler(Maintain4SShopOrderDetailsActivity.this, showAnim) {

            @Override
            public void onSuccess(String response) {
                mMaintenanceOrderDetail = JSONUtils.fromJson(response, Maintenance4SShopOrderDetailBean.class);
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


    /**
     * 绑定数据
     */
    private void bindData() {
        initVisible();
        mOrderStatus.setText(mMaintenanceOrderDetail.orderStatusText);
        //  mOrderStatus.setTextColor(mMaintenanceOrderDetail.getStatusColor(this));
        mOrderID.setText(String.format("订单号:%s", mMaintenanceOrderDetail.orderID));
        mUserFullname.setText(mMaintenanceOrderDetail.userFullname);
        mUserPhoneNumber.setText(mMaintenanceOrderDetail.userPhoneNumber);
        mShopName.setText(mMaintenanceOrderDetail.shopPoint.shopName);
        mShopName.setTag(new PickupPointT(mMaintenanceOrderDetail.shopPoint.address, mMaintenanceOrderDetail.shopPoint.latitude , mMaintenanceOrderDetail.shopPoint.longitude, mMaintenanceOrderDetail.shopPoint.tel));
        mAppointmentDate.setText(mMaintenanceOrderDetail.appointmentDate);
        mMaintainDate.setText(mMaintenanceOrderDetail.maintainDate);
        mOrderCreatTime.setText(mMaintenanceOrderDetail.orderCreateTime);
//        mServiceAdviserName.setText(mMaintenanceOrderDetail.serviceAdviser.name);
//        mMechanicName.setText(mMaintenanceOrderDetail.mechanic.name);
        mPaymentType.setText(TextUtils.isEmpty(mMaintenanceOrderDetail.paymentIDText)?"":mMaintenanceOrderDetail.paymentIDText);
        mPrepayID.setText(mMaintenanceOrderDetail.prepayID);
        autoModel.setText(mMaintenanceOrderDetail.model);
        accumulatingMileage.setText(String.format("%.2f km",mMaintenanceOrderDetail.drivingDistance));
        firstCardTime.setText(mMaintenanceOrderDetail.registerTime);
        mRefundStatus.setText(mMaintenanceOrderDetail.refundStatusText);
        mWorkOrder.setText(mMaintenanceOrderDetail.workOrderID);
        mGoodSunAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.goodsAmount,true)); //配件总额
        mPlateNum.setText(mMaintenanceOrderDetail.plateNumber);//车牌号
//        mWorkCostAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.workCostAmount,true)); //工时总额

        mOrderAmount.setText(String.format("支付金额:%s", OtherUtil.amountFormat(mMaintenanceOrderDetail.orderAmount, true)));


        mScoreCount.setText(String.format("已使用积分%d分", (int)mMaintenanceOrderDetail.score.useCount));
        mScoreUnitPrice.setText(String.format("抵扣 %s", OtherUtil.amountFormat(mMaintenanceOrderDetail.score.usePrice, true)));
        mIntegral.setText(String.format("¥ -%.2f",mMaintenanceOrderDetail.score.usePrice)); //积分抵扣

        mCouponDescription.setText(mMaintenanceOrderDetail.coupon.preferentialDescription);
        mCouponPrice.setText(String.format("抵扣 %s", OtherUtil.amountFormat(mMaintenanceOrderDetail.coupon.price, true)));
        mCoupon.setText(String.format("¥ -%.2f", mMaintenanceOrderDetail.coupon.price)); //优惠卷抵扣

        mDiscount.setText(String.format("¥ -%.2f", mMaintenanceOrderDetail.discount)); //套餐折扣

        mSumAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.amount, true));
        mAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.orderAmount, true));//工时总额

        mWorkCostSunAmount.setText(OtherUtil.amountFormat(mMaintenanceOrderDetail.workCostAmount,true));
        if(!TextUtils.isEmpty(mMaintenanceOrderDetail.captcha))
            mCaptcha.setText(mMaintenanceOrderDetail.captcha.substring(0,4)+"   "+mMaintenanceOrderDetail.captcha.substring(4));

        if(mMaintenanceOrderDetail.maintenanceItems !=null) {
            mMaintenanceItemsCountText.setText(String.format("本次订单共%s个项目", mMaintenanceOrderDetail.maintenanceItems.size()));
           // mMaintenanceItems.setAdapter(new Maintenane4SShopDetailsItemAdapter(mMaintenanceOrderDetail.maintenanceItems, this));
            initMaintenanceItemsGroup(mMaintenanceOrderDetail.maintenanceItems,mMaintenanceItems);
        }
       // loadQRCode();

        mScrollView.smoothScrollTo(0, 0);
    }

    /**
     * 加载保养项目组
     * @param maintenanceItemGroup
     * @param mRecommendParentView
     */
    private void initMaintenanceItemsGroup(ArrayList<MaintenanceItemGroup> maintenanceItemGroup,LinearLayout mRecommendParentView){
        for (int i = 0 ; i < maintenanceItemGroup.size() ; i++){
            LayoutInflater.from(this).inflate(R.layout.item_four_s_shop_first_layer_v5,mRecommendParentView);
            FourSShopMaintenanceFirstChildV5 firstChild = (FourSShopMaintenanceFirstChildV5) mRecommendParentView.getChildAt(i);
            firstChild.initDate(maintenanceItemGroup.get(i),"2",i);
        }
    }

    /**
     * 是否显示条件判断
     */
    private void initVisible() {
        OtherUtil.setVisible(mContentView,true);
        OtherUtil.setVisible(mMenu1, mMaintenanceOrderDetail.isCancel());//取消条件判断
        OtherUtil.setVisible(mMenu2,mMaintenanceOrderDetail.isRefund()); //退款条件判断
        OtherUtil.setVisible(mToPay, mMaintenanceOrderDetail.orderStatus.equals(Maintenance4SShopOrderDetailBean.ORDER_WFK));
        OtherUtil.setVisible(mToSendComment, mMaintenanceOrderDetail.orderStatus.equals(Maintenance4SShopOrderDetailBean.ORDER_DDWC) && ! mMaintenanceOrderDetail.getHasComment());
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_shp_shopName_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.shopPoint.shopName));  //门店
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_orderCreatTime_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.orderCreateTime));  //下单时间
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_appointmentDate_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.appointmentDate));  //预约到店时间
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_maintainDate_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.maintainDate));  //实际到店时间
//        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_serviceAdviser_name_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.serviceAdviser.name));  //预约服务顾问
//        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_mechanic_name_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.mechanic.name));  //预约维修技师
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_paymentType_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.paymentID));  //支付方式
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_prepayID_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.prepayID));  //支付单号

        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_autoModel_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.model));  //车辆信息
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_accumulatingMileage_lly),mMaintenanceOrderDetail.drivingDistance !=0);  //累计行驶里程
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_firstCardTime_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.registerTime));  //首次上牌时间
//        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_QRCode_hint),TextUtils.isEmpty(mMaintenanceOrderDetail.captcha));
//        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_QRCode_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.captcha));
        // TODO: 2016/9/2 4s店保养订单没有二维码
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_QRCode_hint),false);
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_QRCode_lly),false);

        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_refundStatus_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.refundStatusText));
        OtherUtil.setVisible(findViewById(R.id.myorder_details_maintain_4s_workOrder_lly),!TextUtils.isEmpty(mMaintenanceOrderDetail.workOrderID));

        OtherUtil.setVisible(mScorelly, mMaintenanceOrderDetail.score.useCount != 0 && mMaintenanceOrderDetail.score.usePrice != 0);
        OtherUtil.setVisible(mScorelly2, mMaintenanceOrderDetail.score.useCount != 0 && mMaintenanceOrderDetail.score.usePrice != 0);
        OtherUtil.setVisible(mCouponlly, !TextUtils.isEmpty(mMaintenanceOrderDetail.coupon.preferentialDescription) && mMaintenanceOrderDetail.coupon.price != 0);
        OtherUtil.setVisible(mCouponlly2, !TextUtils.isEmpty(mMaintenanceOrderDetail.coupon.preferentialDescription) && mMaintenanceOrderDetail.coupon.price != 0);
        OtherUtil.setVisible(mDiscountlly, mMaintenanceOrderDetail.discount != 0);
    }

//    /**
//     *  生成二维码
//     */
//    private void loadQRCode(){
//        final int widthPix = getResources().getDimensionPixelOffset(com.hxqc.mall.thirdshop.R.dimen.qr_code_width);
//        final int heightPix=getResources().getDimensionPixelOffset(com.hxqc.mall.thirdshop.R.dimen.qr_code_height);
//        mQRCode.setImageBitmap(QRCodeUtil.createQRImage(mMaintenanceOrderDetail.captcha, widthPix, heightPix));
//    }

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.myorder_details_maintain_4s_workOrder) {
            UserApiClient mUserApiClient=new UserApiClient();
            String URL=mUserApiClient.getWorkOrderUrl(((TextView)v).getText().toString(),mMaintenanceOrderDetail.shopPoint.erpShopCode);
            ActivitySwitchBase.toH5Activity(v.getContext(), "工单详情", URL);
        }else if (v.getId() == R.id.myorder_details_maintain_4s_shp_shopName) { //去导航
            if (v.getTag() == null) return;
            PickupPointT msShopLocation = (PickupPointT) v.getTag();
            ActivitySwitcherThirdPartShop.toAMapNvai(v.getContext(), 0, msShopLocation);
        }
        else if (v.getId() == R.id.menu1) { //取消订单
            ActivitySwitcher.toOrderCancel(v.getContext(), getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID));
        }
         else if (v.getId() == R.id.menu2) { //申请退款
            if(mMaintenanceOrderDetail.getAlipayRefund())
            {
                ActivitySwitcher.toOrderRefund(v.getContext(), getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID));
            }else {
                ActivitySwitcher.toAlipayRefundInfo(v.getContext()); //支付宝退款提示页
            }
         }
        else if (v.getId() == R.id.myorder_details_maintain_4s_topay) {
            //去支付
            CreateOrder createOrder = new CreateOrder();
            createOrder.orderID = mMaintenanceOrderDetail.orderID;
            createOrder.amount=mMaintenanceOrderDetail.orderAmount;
            ActivitySwitcherMaintenance.toPayChoice(v.getContext(), createOrder, "", "3");
        }
        else if(v.getId()==R.id.myorder_details_maintain_4s_send_comment){ //去评论
            ActivitySwitcher.toOrderSendComment(v.getContext(), mMaintenanceOrderDetail.orderID, mMaintenanceOrderDetail.shopPhoto, mMaintenanceOrderDetail.shopPoint.id);
        }
//        else if(v.getId() ==R.id.myorder_details_maintain_4s_verify_Complete){ //确认订单
//            verifyCompleteOrder();
//        }
        else if(v.getId()==R.id.call_service) {
          ChatManager.getInstance().startChatWithNothing();  //小能
        }
    }

//    //确认订单
//    private void verifyCompleteOrder(){
//        new NormalDialog(this, "保养已结束，确定服务完成？") {
//            @Override
//            protected void doNext() {
//                mMaintenanceClient.verifyComplete(mMaintenanceOrderDetail.orderID, new LoadingAnimResponseHandler(Maintain4SShopOrderDetailsActivity.this,true) {
//                    @Override
//                    public void onSuccess(String response) {
//                        Message message=JSONUtils.fromJson(response, Message.class);
//                        ToastHelper.showGreenToast(Maintain4SShopOrderDetailsActivity.this, message.message);
//                        new SharedPreferencesHelper(Maintain4SShopOrderDetailsActivity.this).setOrderChange(true);
//                        finish();
//                    }
//                });
//            }
//        }.show();
//    }
}
