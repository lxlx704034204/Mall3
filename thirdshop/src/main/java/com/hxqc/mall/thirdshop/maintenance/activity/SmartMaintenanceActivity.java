//package com.hxqc.mall.thirdshop.maintenance.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.RotateAnimation;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.hxqc.mall.activity.BackActivity;
//import com.hxqc.mall.auto.model.MyAuto;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.controler.UserInfoHelper;
//import com.hxqc.mall.core.util.ActivitySwitchBase;
//import com.hxqc.mall.core.util.ImageUtil;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.control.MaintenanceHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.CheckPackage;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UploadItem;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UploadObject;
//import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
//import com.hxqc.mall.thirdshop.maintenance.views.RecommendMaintainParent;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-02-16
// * FIXME
// * Todo 智能保养的activity
// */
//public class SmartMaintenanceActivity extends BackActivity implements RecommendMaintainParent.onDeleteClick {
//
//    private ScrollView mScrollView;
//    private RelativeLayout mCarTitleView;
//    private ImageView mCarLogoView;
//    private TextView mCarNameView;
//    private TextView mCarMileView;
//    private RelativeLayout mAddMaintainView;
//
//    private LinearLayout mParentView;
//    private TextView mGoodsAmountView;
//    private TextView mWorkCostAmountView;
//    private TextView mDiscountView;
//    private TextView mPayableView;
//    private TextView mAmountView;
//    private TextView mTextTotalPayView;
//    private TextView mTotalPayView;
//    private Button mToMaintainView;
//    private TextView mTitleView;
//    private LinearLayout mRlPackageDiscountView;
//    private View mPackageDiscountLineView;
//
//    private RelativeLayout mCalculateView;
//    private ImageView mLoadingView;
//    private LinearLayout mMoneyView;
//
//    private RelativeLayout mRefreshView;
//    private Button mFailToSeeView;
//
//    private LinearLayout linearLayoutParent;
//    private LinearLayout mGrandParentView;
//    private RequestFailView mFailView;
//
//    private ArrayList<MaintenanceItem> maintenanceItems;
//
//    private String flag;
//    private MyAuto myAuto;
//    private String shopID;
//    private String packageID;
//    private String autoModelID;
//
//    private String title;
//
//
//    private static int CODE = 1;
//
//    private boolean isPackage;
//
//    private MaintenanceHelper maintenanceHelper;
//    private static int RESULT_CONFRIM = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_smart_maintenance);
//        maintenanceHelper = MaintenanceHelper.getInstance();
//        final Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if(bundle !=null){
//            flag = bundle.getString("flag");
//            shopID = bundle.getString("shopID");
//            packageID = bundle.getString("packageID");
//            myAuto = bundle.getParcelable("myAuto");
//        }
//        //活动
//        title = intent.getStringExtra("title");
//
//
//        if(myAuto!= null){
//            autoModelID = myAuto.autoModelID;
//        }else {
//            autoModelID = "";
//        }
//        initView();
//        getDate();
//        initEvent();
//    }
//
//    private void getDate() {
//        maintenanceHelper.getRecommendProgram(this, shopID, packageID, isPackage,autoModelID,new MaintenanceHelper.recommendProgramHandle() {
//            @Override
//            public void onSuccess(ArrayList<MaintenanceItem> maintenanceItems, UploadObject uploadObject, ArrayList<UploadItem> packageItmes) {
//                SmartMaintenanceActivity.this.maintenanceItems = maintenanceItems;
//                mGrandParentView.setVisibility(View.VISIBLE);
//                mFailView.setVisibility(View.GONE);
//                initDate(maintenanceItems);
//                checkPackage();
//            }
//
//            @Override
//            public void onFailure() {
//                showFailView();
//            }
//        });
//    }
//
//    private void initEvent() {
//        mAddMaintainView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ActivitySwitcherMaintenance.toMaintainAndRepairForResult(SmartMaintenanceActivity.this, CODE);
//            }
//        });
//
//        mToMaintainView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                UserInfoHelper.getInstance().loginAction(SmartMaintenanceActivity.this,  ActivitySwitchBase.ENTRANCE_ACCESSORYMAINTENANE, new UserInfoHelper.OnLoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        ActivitySwitcherMaintenance.toConfirmOrder(SmartMaintenanceActivity.this);
//                    }
//                });
//            }
//        });
//
//        mFailToSeeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkPackage();
//            }
//        });
//
//    }
//
//
//    private void initDate(ArrayList<MaintenanceItem> maintenanceItems) {
//        if (maintenanceItems.size() == 0) {
//            mMoneyView.setVisibility(View.GONE);
//            mTextTotalPayView.setVisibility(View.GONE);
//            mTotalPayView.setVisibility(View.GONE);
//            mToMaintainView.setEnabled(false);
//
//        } else {
//            mMoneyView.setVisibility(View.VISIBLE);
//            mTextTotalPayView.setVisibility(View.VISIBLE);
//            mTotalPayView.setVisibility(View.VISIBLE);
//            mToMaintainView.setBackgroundResource(R.drawable.btn_orange);
//            mToMaintainView.setEnabled(true);
//            ArrayList<MaintenanceItem> tempItems = new ArrayList<>();
//            tempItems = maintenanceItems;
//            for (int m = 0; m < tempItems.size(); m++) {
//                linearLayoutParent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_smart_maintain_first_layer, mParentView);
//                RecommendMaintainParent maintainParent = (RecommendMaintainParent) linearLayoutParent.getChildAt(m);
//                maintainParent.setOnDeleteClick(this);
//                maintainParent.initDate(tempItems.get(m), m, shopID);
//            }
//        }
//    }
//
//    private void initView() {
//        mScrollView = (ScrollView) findViewById(R.id.scrollView);
//        mScrollView.smoothScrollTo(0, 20);
//        mCarTitleView = (RelativeLayout) findViewById(R.id.rl_car_title);
//        mCarLogoView = (ImageView) findViewById(R.id.car_logo);
//        mCarNameView = (TextView) findViewById(R.id.car_name);
//        mCarMileView = (TextView) findViewById(R.id.car_mile);
//        mAddMaintainView = (RelativeLayout) findViewById(R.id.add_maintain);
//
//        mParentView = (LinearLayout) findViewById(R.id.ll_parent);
//
//        mGoodsAmountView = (TextView) findViewById(R.id.accessory);
//        mWorkCostAmountView = (TextView) findViewById(R.id.labor_hour);
//        mDiscountView = (TextView) findViewById(R.id.package_discount);
//        mPayableView = (TextView) findViewById(R.id.payable);
//        mAmountView = (TextView) findViewById(R.id.pay);
//        mTotalPayView = (TextView) findViewById(R.id.total_pay);
//        mToMaintainView = (Button) findViewById(R.id.to_maintain);
//        mToMaintainView.setText("去保养");
//        mTitleView = (TextView) findViewById(R.id.maintenance_title);
//        mRlPackageDiscountView = (LinearLayout) findViewById(R.id.rl_package_discount);
//        mPackageDiscountLineView = findViewById(R.id.line2);
//
//        mTextTotalPayView = (TextView) findViewById(R.id.text_total_pay);
//        mCalculateView = (RelativeLayout) findViewById(R.id.calculate);
//        mLoadingView = (ImageView) findViewById(R.id.loading);
//        mMoneyView = (LinearLayout) findViewById(R.id.money);
//
//        mRefreshView = (RelativeLayout) findViewById(R.id.refresh);
//        mFailToSeeView = (Button) findViewById(R.id.fail_to_see);
//
//        mGrandParentView = (LinearLayout) findViewById(R.id.grand_parent);
//        mFailView = (RequestFailView) findViewById(R.id.fail_view);
//
//        //判断是不是套餐
//        if (flag.equals("2")) {
//            mTitleView.setText("其他优惠套餐");
//            isPackage = true;
//        } else if (flag.equals("1")) {
//            mTitleView.setText("推荐保养方案");
//            isPackage = false;
//        } else if (flag.equals("3")) {
//            mTitleView.setText("基本套餐");
//            isPackage = false;
//        } else if (flag.equals("4")) { //活动
//            mTitleView.setText(title);
//            isPackage = true;
//            mAddMaintainView.setVisibility(View.GONE);
//        }
//
//        if (myAuto == null) {
//            mCarTitleView.setVisibility(View.GONE);
//            mAddMaintainView.setVisibility(View.GONE);
//        } else {
//            mCarNameView.setText(myAuto.autoModel);
//            mCarMileView.setText(myAuto.drivingDistance + "");
//            maintenanceHelper.setAutoModelID(myAuto.autoModelID);
//            ImageUtil.setImage(this, mCarLogoView, myAuto.brandThumb);
//        }
//    }
//
//
//    //检查套餐接口
//    private void checkPackage() {
//        maintenanceHelper.checkPackage(this, new MaintenanceHelper.checkPackageHandle() {
//            @Override
//            public void onStart() {
//                mMoneyView.setVisibility(View.GONE);
//                mCalculateView.setVisibility(View.VISIBLE);
//                mRefreshView.setVisibility(View.GONE);
//                mTextTotalPayView.setText("计算中....");
//                mTotalPayView.setVisibility(View.GONE);
//                Animation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                animation.setInterpolator(new LinearInterpolator());
//                animation.setFillAfter(true);
//                animation.setDuration(8000);
//                mLoadingView.setAnimation(animation);
//            }
//
//            @Override
//            public void onSuccess(CheckPackage checkPackage) {
//                mGoodsAmountView.setText(OtherUtil.amountFormat(checkPackage.goodsAmount, true));
//                mWorkCostAmountView.setText(OtherUtil.amountFormat(checkPackage.workCostAmount, true));
//                mPayableView.setText(OtherUtil.amountFormat(checkPackage.amount, true));
//                mDiscountView.setText(OtherUtil.amountFormat((checkPackage.discount - checkPackage.amount), true));
//
//                mAmountView.setText(OtherUtil.amountFormat(checkPackage.discount, true));
//                mTotalPayView.setText(OtherUtil.amountFormat(checkPackage.discount, true));
//
//                if ((checkPackage.amount - checkPackage.discount) == 0) {
//                    mRlPackageDiscountView.setVisibility(View.GONE);
//                    mPackageDiscountLineView.setVisibility(View.GONE);
//                }
//
//                mMoneyView.setVisibility(View.VISIBLE);
//                mCalculateView.setVisibility(View.GONE);
//                mRefreshView.setVisibility(View.GONE);
//                mTextTotalPayView.setText("实付：");
//                mTotalPayView.setVisibility(View.VISIBLE);
//                mToMaintainView.setEnabled(true);
//            }
//
//            @Override
//            public void onFailure() {
//                mMoneyView.setVisibility(View.GONE);
//                mCalculateView.setVisibility(View.GONE);
//                mRefreshView.setVisibility(View.VISIBLE);
//                mTextTotalPayView.setVisibility(View.GONE);
//                mTotalPayView.setVisibility(View.GONE);
//                mToMaintainView.setEnabled(false);
//            }
//        });
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_CONFRIM) {
////            maintenanceItems.clear();
//            linearLayoutParent.removeAllViews();
//            maintenanceHelper.ConfirmMaintenanceItems();
//            maintenanceItems = maintenanceHelper.getMaintainItems();
//            ConfirmUploadObject(maintenanceItems);
//            initDate(maintenanceItems);
//        }
//    }
//
//    @Override
//    public void deleteClick(int position) {
//        mParentView.removeViewAt(position);
//        maintenanceItems.remove(position);
//        linearLayoutParent.removeAllViews();
//        ConfirmUploadObject(maintenanceItems);
//        initDate(maintenanceItems);
//    }
//
//
//    private void ConfirmUploadObject(ArrayList<MaintenanceItem> maintenanceItems){
//        maintenanceHelper.ConfirmUploadObject(maintenanceItems, new MaintenanceHelper.ConfirmUploadObjectHandle() {
//            @Override
//            public void onSuccess() {
//                checkPackage();
//            }
//        });
//    }
//
//    /**
//     * @param maintenanceGoodses 用于返回选中的goodsID 和 重排的商品以便带到添加维修保养页面中做比较
//     * @param position           item 的 position
//     */
//    @Override
//    public void Complete(ArrayList<MaintenanceGoods> maintenanceGoodses, int position) {
//
//        maintenanceItems.get(position).goods.clear();
//        maintenanceItems.get(position).goods.addAll(maintenanceGoodses);
//        ConfirmUploadObject(maintenanceItems);
//
//    }
//
//    //错误界面的显示
//    private void showFailView() {
//        mGrandParentView.setVisibility(View.GONE);
//        mFailView.setVisibility(View.VISIBLE);
//        mFailView.setEmptyDescription("网络连接失败");
//        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDate();
//            }
//        });
//        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        maintenanceHelper.destroy();
//    }
//}
