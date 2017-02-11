package com.hxqc.mall.thirdshop.maintenance.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;

import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;

import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;

import com.hxqc.mall.thirdshop.maintenance.views.FourSShopMaintenanceFirstChildV5;
import com.hxqc.mall.thirdshop.maintenance.views.MaintainCheckDialog;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.hxqc.util.DebugLog;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-08-02
 * FIXME
 * Todo 保养第四版4s店页面
 */
public class FourSShopMaintenanceActivity extends NoBackActivity implements FourSAndQuickHelper.CalculateMoneyFor4SHandle, FourSShopMaintenanceFirstChildV5.BothGroupListener {
    private FourSAndQuickHelper fourSAndQuickHelper;

    protected Toolbar toolbar;
//    protected LinearLayout mServiceLayoutView;
    private ShopDetailsHeadView mFourSTitleView; //4s店头部
    private NewMaintenanceShop newMaintenanceShop;

    private RequestFailView mFailView;  //联网失败View
    private LinearLayout mGrandParentView;

    private LinearLayout mRecommendParentTitleView;
    private LinearLayout mOptionalParentTitleView;
    private LinearLayout mRecommendParentView;
    private LinearLayout mOptionalParentView;
    private TextView mRecommendTitleView;
    private TextView mOptionalTitleView;
    private ImageView mArrowView;
    private TextView mTotalPayView; //总金额
    private Button mToMaintainView;
    private LinearLayout mRlWorkCostView;
    private TextView mWorkCostView; //工时费
    private TextView mGoodsCostView; //材料费
    private TextView mOrderCostView; //订单总额
    private LinearLayout mMaintainCheckView;
    private View view1; //虚线一
    private View view2; //虚线二
    private View view3;//虚线三
    private View view4;//虚线四
    private TextView textView1; // 套餐折扣 text
    private TextView mPackageDiscountView;//套餐折扣
    private TextView mShouldPayView; // 实付总额

    private ArrayList<MaintenanceItemGroup> recommentItemsFor4S;
    private ArrayList<MaintenanceItemGroup> optionalItemsFor4S;

    private int AnimeTime = 300;
    private boolean isDown = false;
    private ValueAnimator animator;

    private String TAG = "TAG"; //log

    private ShopDetailsController shopDetailsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4s_shop_maintenance);
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
        shopDetailsController = ShopDetailsController.getInstance();
        initView();

        if(fourSAndQuickHelper.getFlag().equals("3")){
            initDate(fourSAndQuickHelper.getRecommentItemsFor4S(),fourSAndQuickHelper.getOptionalItemsFor4S());
        }else {
            getDate();
        }

        initEvent();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mFourSTitleView !=null)
            mFourSTitleView.cleanTabCheck();
    }

    private void getDate() {
        fourSAndQuickHelper.getItemsNFor4S(this, new FourSAndQuickHelper.ItemsNHandleFor4S() {

            @Override
            public void onSuccess(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, ArrayList<MaintenanceItemGroup> optionalItemsFor4S) {
                mGrandParentView.setVisibility(View.VISIBLE);
                mFailView.setVisibility(View.GONE);
                initDate(recommentItemsFor4S,optionalItemsFor4S);
            }

            @Override
            public void onSuccessFor4SHome(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, int optionSize) {

            }


            @Override
            public void onFailure() {
                showFailView();
            }
        });
    }

    @hugo.weaving.DebugLog
    private void initDate(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, ArrayList<MaintenanceItemGroup> optionalItemsFor4S) {
        DebugLog.i("Time", "initDate 1:" + System.currentTimeMillis());
        this.recommentItemsFor4S = recommentItemsFor4S;
        this.optionalItemsFor4S = optionalItemsFor4S;

        mRecommendParentView.removeAllViews();
        if(recommentItemsFor4S.size() == 0){
            mRecommendParentTitleView.setVisibility(View.GONE);
        }else {
            mRecommendTitleView.setText(String.format("您已选择的%s项保养项目",recommentItemsFor4S.size()));
            for (int i = 0 ; i < recommentItemsFor4S.size() ; i++){
                LayoutInflater.from(this).inflate(R.layout.item_four_s_shop_first_layer_v5,mRecommendParentView);
                initRecommendChild(i);
            }
        }

        mOptionalParentView.removeAllViews();
        if(optionalItemsFor4S.size() == 0){
            mOptionalParentTitleView.setVisibility(View.GONE);
        }else {
            mOptionalTitleView.setText(String.format("可自行选择其他%s项保养项目",optionalItemsFor4S.size()));
            for (int j = 0 ; j < optionalItemsFor4S.size() ; j++){

                LayoutInflater.from(this).inflate(R.layout.item_four_s_shop_first_layer_v5,mOptionalParentView);
                initOptionalChild(j);
            }
        }
        if(recommentItemsFor4S.size() == 0  && optionalItemsFor4S.size() == 0){
            mRlWorkCostView.setVisibility(View.GONE);
        }else {
            mRlWorkCostView.setVisibility(View.VISIBLE);
        }

        initButtonState();
        fourSAndQuickHelper.CalculateMoneyFor4S(recommentItemsFor4S, optionalItemsFor4S,this);
        DebugLog.i("Time", "initDate 2:" + System.currentTimeMillis());
    }

    private void CalculateMoneyForOptional(MaintenanceItemGroup itemGroup, int position) {
        optionalItemsFor4S.remove(position);
        optionalItemsFor4S.add(position,itemGroup);

        fourSAndQuickHelper.setOptionalItemsFor4S(optionalItemsFor4S);
        fourSAndQuickHelper.CalculateMoneyFor4S(recommentItemsFor4S,optionalItemsFor4S,this);
        initButtonState();
    }



    private void CalculateMoneyForRecomment(MaintenanceItemGroup itemGroup, int position) {
        recommentItemsFor4S.remove(position);
        recommentItemsFor4S.add(position,itemGroup);

        fourSAndQuickHelper.setRecommentItemsFor4S(recommentItemsFor4S);
        fourSAndQuickHelper.CalculateMoneyFor4S(recommentItemsFor4S,optionalItemsFor4S,this);
        initButtonState();
    }

    //顺便计算工时费
    private void initButtonState() {
        boolean isAllNoCheck = true; //如果所有的项目都没勾选则不能点击

        for(MaintenanceItemGroup itemGroup : recommentItemsFor4S){
            if(itemGroup.isCheck){
                isAllNoCheck = false;
            }
        }

        for(MaintenanceItemGroup itemGroup : optionalItemsFor4S){
            if(itemGroup.isCheck){
                isAllNoCheck = false;
            }
        }
        if(isAllNoCheck){
            mToMaintainView.setEnabled(false);
        }else {
            mToMaintainView.setEnabled(true);
        }
    }

    private void initEvent() {
//        mServiceLayoutView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(FourSShopMaintenanceActivity.this, R.style.MaterialDialog);
//                builder.setTitle("拨打电话").setMessage("400-1868-555").setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        Uri uri = Uri.parse("tel:" + "400-1868-555");
//                        intent.setData(uri);
//                        FourSShopMaintenanceActivity.this.startActivity(intent);
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).create().show();
//            }
//        });

        mArrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDown){
                    isDown = false;
                    mOptionalParentView.setVisibility(View.VISIBLE);

                    animator = slideAnimator(0,getTargetHeight(mOptionalParentView),mOptionalParentView);
                    DebugLog.i("TAG","mOptionalParentView.getMeasuredHeight()   " + mOptionalParentView.getMeasuredHeight() );
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mArrowView.setEnabled(false);
                            mOptionalParentView.setVisibility(View.VISIBLE);

                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mArrowView.setEnabled(true);
                            initDate(recommentItemsFor4S,optionalItemsFor4S);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();
                    mArrowView.setImageResource(R.drawable.maintain_arrow_up);
                }else {
                    isDown = true;

                    animator = slideAnimator(getTargetHeight(mOptionalParentView), 0 ,mOptionalParentView);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mArrowView.setEnabled(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mArrowView.setEnabled(true);
                            mOptionalParentView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();

                    mArrowView.setImageResource(R.drawable.maintain_arrow_down);
                }
            }
        });

        mToMaintainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoHelper.getInstance().loginAction(FourSShopMaintenanceActivity.this, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcherMaintenance.toFourSAndQuickConfirmOrder(FourSShopMaintenanceActivity.this,null);
                    }
                });
            }
        });

        mMaintainCheckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaintainCheckDialog dialog = new MaintainCheckDialog(FourSShopMaintenanceActivity.this, R.style.FullWidthDialog,new MaintenanceClient().getMaintainCheckURL());
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
                dialog.show();
            }
        });
    }

    //测量view的高宽
    private int getTargetHeight(View v) {
        try {
            Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
                    int.class);
            m.setAccessible(true);
            m.invoke(v, View.MeasureSpec.makeMeasureSpec(
                    ((View) v.getParent()).getMeasuredWidth(),
                    View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED));
        } catch (Exception e) {

        }
        return v.getMeasuredHeight();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFourSTitleView = (ShopDetailsHeadView) findViewById(R.id.four_s_title);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
        newMaintenanceShop = bundle.getParcelable("newMaintenanceShop");
        fourSAndQuickHelper.setShopID(newMaintenanceShop.shopID);
        shopDetailsController.setShopID(fourSAndQuickHelper.getShopID());
        shopDetailsController.requestThirdPartShop(this, new ShopDetailsController.ThirdPartShopHandler() {
            @Override
            public void onSucceed(ThirdPartShop thirdPartShop) {
                mFourSTitleView.bindData(thirdPartShop.getShopInfo());
            }

            @Override
            public void onFailed(boolean offLine) {

            }
        });

        getSupportActionBar().setTitle(newMaintenanceShop.shopTitle);

//        mServiceLayoutView = (LinearLayout) findViewById(R.id.service);
        mFailView = (RequestFailView) findViewById(R.id.fail_view);
        mGrandParentView = (LinearLayout) findViewById(R.id.grand_parent);

        mRecommendParentTitleView = (LinearLayout) findViewById(R.id.recommend_parent_title);
        mOptionalParentTitleView = (LinearLayout) findViewById(R.id.optional_parent_title);
        mRecommendParentView = (LinearLayout) findViewById(R.id.recommend_parent);
        mOptionalParentView = (LinearLayout) findViewById(R.id.optional_parent);
        mRecommendTitleView = (TextView) findViewById(R.id.recommend_title);
        mOptionalTitleView = (TextView) findViewById(R.id.optional_title);
        mArrowView = (ImageView) findViewById(R.id.iv_arrow);
        mTotalPayView = (TextView) findViewById(R.id.total_pay);
        mToMaintainView = (Button) findViewById(R.id.to_maintain);
        mRlWorkCostView = (LinearLayout) findViewById(R.id.rl_work_cost);
        mWorkCostView = (TextView) findViewById(R.id.work_cost);
        mGoodsCostView = (TextView) findViewById(R.id.goods_cost);
        mOrderCostView = (TextView) findViewById(R.id.order_cost);
        mMaintainCheckView = (LinearLayout) findViewById(R.id.maintain_check);
        view1 = findViewById(R.id.view1);
        view1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view2 = findViewById(R.id.view2);
        view2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view3 = findViewById(R.id.view3);
        view3.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view4 = findViewById(R.id.view4);
        view4.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        textView1 = (TextView) findViewById(R.id.text_1);
        mPackageDiscountView = (TextView) findViewById(R.id.package_discount);
        mShouldPayView = (TextView) findViewById(R.id.should_pay);
    }


    private ValueAnimator slideAnimator(int start, int end , final LinearLayout contentLayout) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(AnimeTime);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
                layoutParams.height = value;

                contentLayout.setLayoutParams(layoutParams);
                contentLayout.invalidate();
                contentLayout.requestLayout();
            }
        });
        return animator;
    }

    //错误界面的显示
    private void showFailView() {
        mGrandParentView.setVisibility(View.GONE);
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setEmptyDescription("网络连接失败");
        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    @Override
    public void onSuccess(float amount , float workCost , float goodsCost , float oldPrice) {
        mTotalPayView.setText(OtherUtil.amountFormat(amount,true));
        mWorkCostView.setText(OtherUtil.amountFormat(workCost,true));
        mGoodsCostView.setText(OtherUtil.amountFormat(goodsCost,true));
        mOrderCostView.setText(OtherUtil.amountFormat(oldPrice,true));
        mShouldPayView.setText(OtherUtil.amountFormat(amount,true));
        if((oldPrice - amount) == 0){
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            mPackageDiscountView.setVisibility(View.GONE);
        }else {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            mPackageDiscountView.setVisibility(View.VISIBLE);
            mPackageDiscountView.setText(OtherUtil.amountFormat(-(oldPrice - amount),true));
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        shopDetailsController.onDestroy();
    }

    @Override
    public void bothGroup() {
        //记录 互斥的推荐项目
        ArrayList<Integer> mutexRecommends = fourSAndQuickHelper.getMutexRecommend();
        if(mutexRecommends.size() != 0){
            for(int mutexRecommend : mutexRecommends){
                initRecommendChild(mutexRecommend);
            }
        }
        DebugLog.i(TAG,"mutexRecommends :" + mutexRecommends.size());
        //记录 互斥的自选项目
         ArrayList<Integer> mutexOptionals = fourSAndQuickHelper.getMutexOptional();
        if(mutexOptionals.size() !=0){
            for(int mutexOptional : mutexOptionals){
                initOptionalChild(mutexOptional);
            }
        }
        DebugLog.i(TAG,"mutexOptionals :" + mutexOptionals.size());
        //记录 同时选中的推荐项目
         ArrayList<Integer> bothRecommends = fourSAndQuickHelper.getBothRecommend();
         if(bothRecommends.size() != 0){
            for(int bothRecommend : bothRecommends){
                initRecommendChild(bothRecommend);
            }
        }
        DebugLog.i(TAG,"bothRecommends :" + bothRecommends.size());
         //记录 同时选中的自选项目
         ArrayList<Integer> bothOptionals = fourSAndQuickHelper.getBothOptional();
        if(bothOptionals.size() !=0){
            for(int bothOptional : bothOptionals){
                initOptionalChild(bothOptional);
            }
        }
        DebugLog.i(TAG,"bothOptionals :" + bothOptionals.size());
        initButtonState();
        fourSAndQuickHelper.CalculateMoneyFor4S(recommentItemsFor4S,optionalItemsFor4S,this);
    }

    private void  initRecommendChild(int childPosition){
        FourSShopMaintenanceFirstChildV5 firstChild = (FourSShopMaintenanceFirstChildV5) mRecommendParentView.getChildAt(childPosition);
        firstChild.initDate(recommentItemsFor4S.get(childPosition),"1",childPosition);
        firstChild.setCaulateMoenyListener(new FourSShopMaintenanceFirstChildV5.CalculateMoneyListener() {
            @Override
            public void CalculateMoney(MaintenanceItemGroup itemGroup, int position) {
                CalculateMoneyForRecomment(itemGroup,position);
            }
        });
        firstChild.setBothGroupListener(this);
    }

    private void  initOptionalChild(int childPosition){
        FourSShopMaintenanceFirstChildV5 firstChild = (FourSShopMaintenanceFirstChildV5) mOptionalParentView.getChildAt(childPosition);

        firstChild.initDate(optionalItemsFor4S.get(childPosition),"1",childPosition);
        firstChild.setCaulateMoenyListener(new FourSShopMaintenanceFirstChildV5.CalculateMoneyListener() {
            @Override
            public void CalculateMoney(MaintenanceItemGroup itemGroup, int position) {
                CalculateMoneyForOptional(itemGroup,position);
            }
        });
        firstChild.setBothGroupListener(this);
    }
}
