package com.hxqc.mall.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.model.order.RepairOrderDetailsBean;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.JSONUtils;
import com.hxqc.xiaoneng.ChatManager;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;

/**
 * liaoguilong
 * 2016年2月22日 11:35:09
 * 订单详情（预约修车）
 */
public class RepairOrderDetailsActivity extends NoBackActivity implements View.OnClickListener {
    public static final String ORDERID = "orderID";
    private TextView
            mOrderIDView, //订单ID
            mServiceTypeView, //服务类型
            mAppintmentDateView,//预约时间
            mNameView, //联系人
            mPhoneView,//联系人手机号
            mAutoModelView,//车型名称
            mShopNameView,//店铺名称
//            mServiceAdviserNameView,//服务顾问名称
//            mMechanicNameView;//维修技师名称
            mRemarks,//备注
            mOrderStatusView;//

    private TextView mWorkOrder;////工单
   // private ImageView mShopPoint; //店铺

    private RequestFailView mRequestFailView;
    private MaintenanceClient mMaintenanceClient; //接口API

    private RepairOrderDetailsBean mRepairOrderDetailsBean; //预约修车订单数据
    private Toolbar mToolbar;
    private TextView mMenu;

    private LinearLayout mWorkOrderIDlly,mRemarkslly;
    private ScrollView mScrollView;
    private TextView mCallService;
    private RelativeLayout mToSendCommentrly;
    private Button mToSendComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_car_order_details);
        mMaintenanceClient = new MaintenanceClient();
        initView();
        loadData(true);
    }


    private void initView() {
        mScrollView= (ScrollView) findViewById(R.id.activity_myorder_details_repair_scrollview);
        OtherUtil.setVisible(mScrollView,false);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_repair_order_details);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);

        mMenu = (TextView) findViewById(R.id.menu);
        mMenu.setOnClickListener(this);
        mOrderIDView = (TextView) findViewById(R.id.activity_myorder_details_repair_orderID);
        mServiceTypeView = (TextView) findViewById(R.id.activity_myorder_details_repair_serviceType);
        mAppintmentDateView = (TextView) findViewById(R.id.activity_myorder_details_repair_appintmentDate);
        mNameView = (TextView) findViewById(R.id.activity_myorder_details_repair_name);
        mPhoneView = (TextView) findViewById(R.id.activity_myorder_details_repair_phone);
        mAutoModelView = (TextView) findViewById(R.id.activity_myorder_details_repair_autoModel);
        mShopNameView = (TextView) findViewById(R.id.activity_myorder_details_repair_shopPoint_shopName);
//        mServiceAdviserNameView = (TextView) findViewById(R.id.activity_myorder_details_repair_serviceAdviserName);
//        mMechanicNameView = (TextView) findViewById(R.id.activity_myorder_details_repair_mechanicName);
        mOrderStatusView= (TextView) findViewById(R.id.activity_myorder_details_repair_OrderStatus);
//        mShopPoint = (ImageView) findViewById(R.id.activity_myorder_details_repair_shopPoint);
        mToSendCommentrly= (RelativeLayout) findViewById(R.id.activity_myorder_details_repair_send_comment_rly);
        mToSendComment= (Button) findViewById(R.id.activity_myorder_details_repair_send_comment);
        mRemarks= (TextView) findViewById(R.id.activity_myorder_details_repair_remarks);
        mRemarkslly= (LinearLayout) findViewById(R.id.activity_myorder_details_repair_remarks_lly);
        mShopNameView.setOnClickListener(this);

        mWorkOrder = (TextView) findViewById(R.id.activity_myorder_details_repair_workOrderID);
        mWorkOrderIDlly= (LinearLayout) findViewById(R.id.activity_myorder_details_repair_workorderID_lly);
        mWorkOrder.setOnClickListener(this);

        mRequestFailView = (RequestFailView) findViewById(R.id.activity_myorder_details_repair_fail_view);

        mCallService= (TextView) findViewById(R.id.call_service);
        mCallService.setOnClickListener(this);
        mToSendComment.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mRepairOrderDetailsBean !=null)
            loadData(true);
    }

    private void loadData(boolean showAnim) {
        mMaintenanceClient.orderRepairDetail(UserInfoHelper.getInstance().getToken(this), getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ORDERID), new LoadingAnimResponseHandler(RepairOrderDetailsActivity.this, showAnim) {

            @Override
            public void onSuccess(String response) {
                mRepairOrderDetailsBean = JSONUtils.fromJson(response, RepairOrderDetailsBean.class);
                if (mRepairOrderDetailsBean == null)
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
        mRequestFailView.setEmptyDescription("数据加载失败");
        mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(true);
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }


    private void bindData() {
        OtherUtil.setVisible(mMenu, mRepairOrderDetailsBean.isCancel());
        OtherUtil.setVisible(mScrollView,true);
        OtherUtil.setVisible(mWorkOrderIDlly, !mRepairOrderDetailsBean.workOrderID.equals(""));
        OtherUtil.setVisible(mToSendCommentrly,mRepairOrderDetailsBean.orderStatus.equals(RepairOrderDetailsBean.ORDER_YWC) && !mRepairOrderDetailsBean.getHasComment()); //已完成，并且没有评价
        OtherUtil.setVisible(mRemarkslly,mRepairOrderDetailsBean.serviceType.equals("其他"));
//       OtherUtil.setVisible(findViewById(R.id.activity_myorder_details_repair_serviceAdviserName_lly),! TextUtils.isEmpty(mRepairOrderDetailsBean.serviceAdviserName));
//        OtherUtil.setVisible(findViewById(R.id.activity_myorder_details_repair_mechanicName_lly), !TextUtils.isEmpty(mRepairOrderDetailsBean.mechanicName));
        mOrderIDView.setText(String.format("订单号:%s", mRepairOrderDetailsBean.orderId));
        mServiceTypeView.setText(mRepairOrderDetailsBean.serviceType);
        mAppintmentDateView.setText(mRepairOrderDetailsBean.appintmentDate);
        mNameView.setText(mRepairOrderDetailsBean.name);
        mPhoneView.setText(mRepairOrderDetailsBean.phone);
        mAutoModelView.setText(String.format("%s  %s",mRepairOrderDetailsBean.plateNumber,mRepairOrderDetailsBean.autoModel));
        mShopNameView.setText(mRepairOrderDetailsBean.shopPoint.shopName);
        mShopNameView.setTag(new PickupPointT(mRepairOrderDetailsBean.shopPoint.address, mRepairOrderDetailsBean.shopPoint.latitude , mRepairOrderDetailsBean.shopPoint.longitude, mRepairOrderDetailsBean.shopPoint.tel));
//        mServiceAdviserNameView.setText(mRepairOrderDetailsBean.serviceAdviserName);
//        mMechanicNameView.setText(mRepairOrderDetailsBean.mechanicName);
        mWorkOrder.setText(mRepairOrderDetailsBean.workOrderID);
        mOrderStatusView.setText(mRepairOrderDetailsBean.orderStatusText);
        mRemarks.setText(mRepairOrderDetailsBean.remark);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 无效的订单
     */
    private void noData() {
//        OtherUtil.setVisible(mMenu, mRepairOrderDetailsBean != null);
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
        if (v.getId() == R.id.activity_myorder_details_repair_workOrderID) {
            UserApiClient mUserApiClient=new UserApiClient();
            String URL=mUserApiClient.getWorkOrderUrl(((TextView)v).getText().toString(),mRepairOrderDetailsBean.shopPoint.erpShopCode);
            ActivitySwitchBase.toH5Activity(v.getContext(), "工单详情", URL);
        } else if (v.getId() == R.id.activity_myorder_details_repair_shopPoint_shopName) {
            if (v.getTag() == null) return;
            PickupPointT msShopLocation = (PickupPointT) v.getTag();
            ActivitySwitcherThirdPartShop.toAMapNvai(v.getContext(), 0, msShopLocation);
        }else if(v.getId()==R.id.call_service) {
            ChatManager.getInstance().startChatWithNothing();    //小能
        }
        else if (v.getId() == R.id.menu) { //取消订单
            ActivitySwitcher.toRepairOrderCancel(v.getContext(),mRepairOrderDetailsBean.orderId);
        }else if(v.getId() ==R.id.activity_myorder_details_repair_send_comment){ //发表评价
            ActivitySwitcher.toOrderSendComment(v.getContext(), mRepairOrderDetailsBean.orderId, mRepairOrderDetailsBean.shopPhoto, mRepairOrderDetailsBean.shopPoint.id);
        }
    }
}
