//package com.hxqc.mall.thirdshop.maintenance.activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.hxqc.mall.activity.NoBackActivity;
//import com.hxqc.mall.auto.model.MyAuto;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.controler.UserInfoHelper;
//import com.hxqc.mall.core.model.auto.PickupPointT;
//import com.hxqc.mall.core.util.ActivitySwitchBase;
//import com.hxqc.mall.core.util.ImageUtil;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
//import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
//import com.hxqc.mall.thirdshop.maintenance.views.RecommendMaintainParent;
//import com.hxqc.mall.thirdshop.model.ShopInfo;
//import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
//import com.hxqc.util.DebugLog;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-04-19
// * FIXME
// * Todo  4s 快修店 报价页面
// */
//public class FourSAndQuickShopQuoteActivity extends NoBackActivity implements RecommendMaintainParent.onDeleteClick, RecommendMaintainParent.onAddGoodsCalculate {
//
//    protected Toolbar toolbar;
//    protected LinearLayout mServiceLayoutView;
//
//    private ScrollView mScrollView;
//
//    private ShopDetailsHeadView mFourSTitleView;
//    private RelativeLayout mCarTitleView;
//    private ImageView mCarLogoView;
//    private TextView mCarNameView;
//    private TextView mCarMileView;
//    private RelativeLayout mAddMaintainView;
//
//    private LinearLayout mParentView;
//    private TextView mGoodsAmountView; //配件总额
//    private TextView mWorkCostAmountView; //工时费
//
//    private TextView mPayableView; // 应付
//    private TextView mAmountView; // 实付
//    private TextView mTextTotalPayView;
//    private TextView mTotalPayView;
//    private Button mToMaintainView;
//
//    private RelativeLayout mMoneyView;
//
//    private LinearLayout linearLayoutParent; // 保养项目父布局
//    private LinearLayout mGrandParentView;
//    private RequestFailView mFailView;
//
//    private static int CODE = 1;
//    private static int RESULT_CONFRIM = 1;
//
//    private String flag;
//    private MyAuto myAuto;
//    private String shopID;
//    private NewMaintenanceShop newMaintenanceShop;
//    private ArrayList<MaintenanceItemN> maintenanceShowItems ;
//    private FourSAndQuickHelper fourSAndQuickHelper;
//    private ShopInfo shopInfo;
//
//    private boolean isRecommentShow = false ;
//    private boolean isOptionalShow = false;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_4s_quick_shop_quote);
//
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//        flag = fourSAndQuickHelper.getFlag();
//        myAuto = fourSAndQuickHelper.getMyAuto();
//        initView();
//        if(!flag.equals("3")) getDate();
//        initEvent();
//    }
//
//    private void initEvent() {
//        mAddMaintainView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ActivitySwitcherMaintenance.toFourSAndQuickMaintainAndRepair(FourSAndQuickShopQuoteActivity.this, CODE);
//            }
//        });
//
//        mToMaintainView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserInfoHelper.getInstance().loginAction(FourSAndQuickShopQuoteActivity.this, new UserInfoHelper.OnLoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        if(flag.equals("1")){
//                            ActivitySwitcherMaintenance.toFourSAndQuickConfirmOrder(FourSAndQuickShopQuoteActivity.this,null);
//                        }else {
//                            ActivitySwitcherMaintenance.toMaintenanceList(FourSAndQuickShopQuoteActivity.this,myAuto.brandID,myAuto.seriesID,myAuto.autoModelID,fourSAndQuickHelper.getItem(),1);
//                        }
//                    }
//                });
//            }
//        });
//
//        mServiceLayoutView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(FourSAndQuickShopQuoteActivity.this, R.style.MaterialDialog);
//                builder.setTitle("拨打电话").setMessage("400-1868-555").setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        Uri uri = Uri.parse("tel:" + "400-1868-555");
//                        intent.setData(uri);
//                        FourSAndQuickShopQuoteActivity.this.startActivity(intent);
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).create().show();
//            }
//        });
//    }
//
//    private void getDate() {
//        fourSAndQuickHelper.getItemsN(this, new FourSAndQuickHelper.ItemsNHandle() {
//
//            @Override
//            public void onSuccess(ArrayList<MaintenanceItemN> maintenanceShowItems) {
//                mGrandParentView.setVisibility(View.VISIBLE);
//                mFailView.setVisibility(View.GONE);
//                FourSAndQuickShopQuoteActivity.this.maintenanceShowItems = maintenanceShowItems;
//                initDate(maintenanceShowItems);
//                CalculateMoney(maintenanceShowItems);
//            }
//
//            @Override
//            public void onSuccessFor4S(ArrayList<MaintenanceItemN> maintenanceItems) {
//
//            }
//
//            @Override
//            public void onFailure() {
//                showFailView();
//            }
//        });
//    }
//
//
//    private void initDate(ArrayList<MaintenanceItemN> maintenanceItems) {
//        isRecommentShow = false;
//        isOptionalShow = false;
//        if (maintenanceItems.size() == 0) {
//            mMoneyView.setVisibility(View.GONE);
//            mTextTotalPayView.setVisibility(View.GONE);
//            mTotalPayView.setVisibility(View.GONE);
//            mToMaintainView.setEnabled(false);
//            mAddMaintainView.setVisibility(View.GONE);
//        } else {
//            mMoneyView.setVisibility(View.VISIBLE);
//            mTextTotalPayView.setVisibility(View.VISIBLE);
//            mTotalPayView.setVisibility(View.VISIBLE);
//            mToMaintainView.setBackgroundResource(R.drawable.btn_orange);
//            mToMaintainView.setEnabled(true);
//            mAddMaintainView.setVisibility(View.VISIBLE);
//
//            for (int m = 0; m < maintenanceItems.size(); m++) {
//                linearLayoutParent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_smart_maintain_first_layer, mParentView);
//                RecommendMaintainParent maintainParent = (RecommendMaintainParent) linearLayoutParent.getChildAt(m);
//                if(maintenanceItems.get(m).isPlatformRecommend ){
//                    if(!isRecommentShow){
//                        isRecommentShow = true;
//                        maintainParent.mTitleView.setVisibility(View.VISIBLE);
//                        maintainParent.mSecondTitleView.setVisibility(View.VISIBLE);
//                        maintainParent.mTitleView.setText("平台推荐的基础保养方案");
//                    }else {
//                        maintainParent.mTitleView.setVisibility(View.GONE);
//                        maintainParent.mSecondTitleView.setVisibility(View.GONE);
//                    }
//                }else {
//                    maintainParent.mSecondTitleView.setVisibility(View.GONE);
//                    if (!isOptionalShow){
//                        isOptionalShow = true;
//                        maintainParent.mTitleView.setVisibility(View.VISIBLE);
//                        maintainParent.mTitleView.setText("您自行选择的保养及维修方案 ");
//                    }else {
//                        maintainParent.mTitleView.setVisibility(View.GONE);
//                    }
//                }
//                maintainParent.setOnDeleteClick(this);
//                maintainParent.setOnAddGoodsCalculate(this);
//
//                maintainParent.initDate(maintenanceItems.get(m), m, shopID);
//            }
//        }
//    }
//
//    private void initView() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        mServiceLayoutView = (LinearLayout) findViewById(R.id.service);
//
//        mScrollView = (ScrollView) findViewById(R.id.scrollView);
//        mScrollView.smoothScrollTo(0, 20);
//
//        mFourSTitleView = (ShopDetailsHeadView) findViewById(R.id.four_s_title);
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
//
//        mPayableView = (TextView) findViewById(R.id.payable);
//        mAmountView = (TextView) findViewById(R.id.pay);
//        mTotalPayView = (TextView) findViewById(R.id.total_pay);
//        mToMaintainView = (Button) findViewById(R.id.to_maintain);
//        mToMaintainView.setText("去保养");
//
//
//        mTextTotalPayView = (TextView) findViewById(R.id.text_total_pay);
//        mMoneyView = (RelativeLayout) findViewById(R.id.money);
//
//        mGrandParentView = (LinearLayout) findViewById(R.id.grand_parent);
//        mFailView = (RequestFailView) findViewById(R.id.fail_view);
//
//        if(flag.equals("1")){ //常规保养的4s店流程
//            mFourSTitleView.setVisibility(View.VISIBLE);
//            mCarTitleView.setVisibility(View.GONE);
//            Intent intent = getIntent();
//            Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
//            newMaintenanceShop = bundle.getParcelable("newMaintenanceShop");
//            getSupportActionBar().setTitle(newMaintenanceShop.shopTitle);
//            initFourSTitle(newMaintenanceShop);
//
//            shopID = newMaintenanceShop.shopID;
//            fourSAndQuickHelper.setShopID(newMaintenanceShop.shopID);
//
//        }else if(flag.equals("2")){ //常规保养的快修店流程
//            mFourSTitleView.setVisibility(View.GONE);
//            mCarTitleView.setVisibility(View.VISIBLE);
//            getSupportActionBar().setTitle("快修店报价");
//            mCarNameView.setText(myAuto.getAutoModel());
//            DebugLog.i("TAG",myAuto.brandThumb);
//            ImageUtil.setImage(this, mCarLogoView, myAuto.brandThumb);
//            mCarMileView.setText(myAuto.drivingDistance);
//        }else if(flag.equals("3")){//4s店首页流程
//            mFourSTitleView.setVisibility(View.VISIBLE);
//            mCarTitleView.setVisibility(View.GONE);
//            Intent intent = getIntent();
//            Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
//            shopInfo = bundle.getParcelable("shopInfo");
//            getSupportActionBar().setTitle(shopInfo.shopTitle);
//
//            shopID = shopInfo.shopID;
//            fourSAndQuickHelper.setShopID(shopInfo.shopID);
//            mFourSTitleView.bindData(shopInfo);
//            mGrandParentView.setVisibility(View.VISIBLE);
//            mFailView.setVisibility(View.GONE);
//            FourSAndQuickShopQuoteActivity.this.maintenanceShowItems = fourSAndQuickHelper.getChooseMaintenanceItems();
//            initDate(maintenanceShowItems);
//            CalculateMoney(maintenanceShowItems);
//        }
//    }
//
//    //初始化4s店流程的title
//    private void initFourSTitle(NewMaintenanceShop newMaintenanceShop) {
//        ShopInfo shopInfo = new ShopInfo();
//        shopInfo.brand = newMaintenanceShop.brand;
//        shopInfo.brandID = newMaintenanceShop.brandID;
//        shopInfo.rescueTel = newMaintenanceShop.rescueTel;
//        shopInfo.shopID = newMaintenanceShop.shopID;
//        shopInfo.shopTel = newMaintenanceShop.shopTel;
//        shopInfo.shopLogoThumb = newMaintenanceShop.brandThumb;
//        shopInfo.shopTitle = newMaintenanceShop.shopTitle;
//        shopInfo.shopName = newMaintenanceShop.shopName;
//        shopInfo.shopInstruction = newMaintenanceShop.shopInstruction;
//        PickupPointT shopLocation = new PickupPointT();
//        shopLocation.latitude = String.valueOf(newMaintenanceShop.latitude);
//        shopLocation.longitude = String.valueOf(newMaintenanceShop.longitude);
//        shopLocation.distance = newMaintenanceShop.distance;
//        shopLocation.address = newMaintenanceShop.address;
//        shopInfo.setShopLocation(shopLocation);
//
//        mFourSTitleView.bindData(shopInfo);
//    }
//
//    //算钱
//    private void CalculateMoney(ArrayList<MaintenanceItemN> maintenanceShowItems){
//        fourSAndQuickHelper.CalculateMoney(maintenanceShowItems, new FourSAndQuickHelper.CalculateMoneyHandle() {
//            @Override
//            public void onSuccess(float goodsAmount, String workCostAmount, String amount) {
//                setMoney(goodsAmount,workCostAmount,amount);
//            }
//        });
//    }
//
//    private void setMoney(float goodsAmount, String workCostAmount, String amount){
//        mGoodsAmountView.setText(OtherUtil.amountFormat(goodsAmount, true));
//        mWorkCostAmountView.setText(OtherUtil.CalculateRangeMoney(workCostAmount,true));
//        mPayableView.setText(OtherUtil.CalculateRangeMoney(amount, true));
//        mAmountView.setText(OtherUtil.CalculateRangeMoney(amount, true));
//        mTotalPayView.setText(OtherUtil.CalculateRangeMoney(amount,true));
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_CONFRIM) {
//            if(linearLayoutParent != null) linearLayoutParent.removeAllViews();
//            fourSAndQuickHelper.ConfirmMaintenanceItems();
//            maintenanceShowItems = fourSAndQuickHelper.getMaintenanceShowItems();
//            CalculateMoney(maintenanceShowItems);
//            initDate(maintenanceShowItems);
//        }
//    }
//
//    @Override
//    public void deleteClick(int position) {
//        mParentView.removeViewAt(position);
//        fourSAndQuickHelper.deleteMaintenanceItem(maintenanceShowItems.get(position));
//        maintenanceShowItems.remove(position);
//        linearLayoutParent.removeAllViews();
//        if (maintenanceShowItems.size() == 0) {
//            mMoneyView.setVisibility(View.GONE);
//            mTextTotalPayView.setVisibility(View.GONE);
//            mTotalPayView.setVisibility(View.GONE);
//            mToMaintainView.setEnabled(false);
//        }else {
//            CalculateMoney(maintenanceShowItems);
//            initDate(maintenanceShowItems);
//        }
//    }
//
//    @Override
//    public void Complete(ArrayList<MaintenanceGoods> returnGoods, int position) {
//
//        maintenanceShowItems.get(position).goods.clear();
//        maintenanceShowItems.get(position).goods.addAll(returnGoods);
//        CalculateMoney(maintenanceShowItems);
////        RecommendMaintainParent maintainParent = (RecommendMaintainParent) linearLayoutParent.getChildAt(position);
////        maintainParent.initDate(maintenanceShowItems.get(position), position, shopID);
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
//    public void addGoodsCalculate(ArrayList<MaintenanceGoods> returnGoods , int position) {
//        maintenanceShowItems.get(position).goods.clear();
//        maintenanceShowItems.get(position).goods.addAll(returnGoods);
//        CalculateMoney(maintenanceShowItems);
//    }
//}
