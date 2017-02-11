package com.hxqc.newenergy.view;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.LayoutAnimatorUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.newenergy.bean.EVNewWenergySubsidyDatailBean;
import com.hxqc.newenergy.control.EV_CreateViewFromHTMLTools;
import com.hxqc.newenergy.util.ActivitySwitcherEV;

import hxqc.mall.R;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/26.
 * Copyright:恒信汽车电子商务有限公司
 */
public class ModelsandsubsidiesDatailView extends FrameLayout implements View.OnClickListener {

    LayoutAnimatorUtil animatorUtil;
    EVNewWenergySubsidyDatailBean data;

    private TextView mCarName_TextView = null;//车辆名称
    private TextView mCity_TextView = null;  //销售城市
    private TextView mOrigPrice_TextView = null;// 厂商指导价
    private TextView mSubsidyCountry_TextView = null;//国家补贴
    private TextView mSubsidyManufacturer_TextView = null;//厂家补贴
    private TextView mSubsidyLocal_TextView = null;//地方补贴
    private TextView mSubsidyDealer_TextView = null;//经销商补贴
    private TextView mSubsidyTotal_TextView = null;//补贴总额
    private TextView mPriceView = null;//实际售价
    private TextView mBatteryLife_TextView = null;//电池续航
    private TextView mHasPurchaseTax_TextView = null;//购置税
    private TextView mArea_TextView = null;//地区
    private TextView mModelType_TextView = null;//车辆类型
    private TextView mModelSize_TextView = null;//车型尺寸
    private TextView mpPwer_TextView = null;//功率
    private TextView mTorque_TextView = null;//扭矩
    private TextView mFastChargingTime_TextView = null;//快充时间
    private TextView mSlowChargingTime_TextView = null;//慢充时间
    private TextView mMaximumSpeed_TextView = null;//最高速度
    private TextView mBatteryType_TextView = null;//电池类型
    private TextView mBatteryCapacity_TextView = null;//电池容量
    private TextView mChargingInHome_TextView = null;//是否可以在家充电
    private TextView mGuaranteePeriod_TextView = null;//整车质保
    private TextView mMaintenancePeriod_TextView = null;//保养周期

    boolean isFirstIn = true;

    /**
     * 补贴
     */
    private RelativeLayout mAllowanceClickView;
    private LinearLayout mAllowanceContainerView;
    private ImageView mAllowanceArrowView;
    private boolean isAllowanceOpen = true;
    private int allowanceHeight = 0;

    /**
     * 政策
     */
    private RelativeLayout mPolicyClickView;
    private LinearLayout mPolicyContainerView = null;//地方政策
    private ImageView mPolicyArrowView;
    private boolean isPolicyOpen = true;
    private int policyHeight = 0;

    /**
     * 充电
     */
    private RelativeLayout mChargeClickView;
    private LinearLayout mChargeContainerView;
    private ImageView mChargeArrow;
    private boolean isChargeOpen = true;
    private int chargeHeight = 0;


    public ModelsandsubsidiesDatailView(Context context) {
        super(context);
        inIt();

    }

    public ModelsandsubsidiesDatailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }

    public ModelsandsubsidiesDatailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inIt();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModelsandsubsidiesDatailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inIt();
    }


    public void inIt() {
        inflate(getContext(), R.layout.newenergy_ev_modelsandsubsidiesdatail_view, this);

        /**
         * 补贴
         */
        mAllowanceClickView = (RelativeLayout) findViewById(R.id.allowance_label_layout);
        mAllowanceContainerView = (LinearLayout) findViewById(R.id.container_allowance);
        mAllowanceArrowView = (ImageView) findViewById(R.id.allowance_arrow);

        /**
         * 政策
         */
        mPolicyClickView = (RelativeLayout) findViewById(R.id.policy_click_view);
        mPolicyContainerView = (LinearLayout) findViewById(R.id.localPolicy_text);
        mPolicyArrowView = (ImageView) findViewById(R.id.policy_arrow_view);

        /**
         * 充电
         */
        mChargeClickView = (RelativeLayout) findViewById(R.id.charge_click_view);
        mChargeContainerView = (LinearLayout) findViewById(R.id.container_charge_time);
        mChargeArrow = (ImageView) findViewById(R.id.charge_arrow_view);


        mCarName_TextView = (TextView) findViewById(R.id.carname_text);
        mOrigPrice_TextView = (TextView) findViewById(R.id.origPrice_text);
        mSubsidyCountry_TextView = (TextView) findViewById(R.id.subsidyCountry_text);
        mSubsidyManufacturer_TextView = (TextView) findViewById(R.id.subsidyManufacturer_text);
        mSubsidyLocal_TextView = (TextView) findViewById(R.id.subsidyLocal_text);
        mSubsidyDealer_TextView = (TextView) findViewById(R.id.subsidyDealer_text);
        mSubsidyTotal_TextView = (TextView) findViewById(R.id.subsidyTotal_text);
        mBatteryType_TextView = (TextView) findViewById(R.id.batteryType_text);
        mBatteryLife_TextView = (TextView) findViewById(R.id.batteryLife_textf);
        mHasPurchaseTax_TextView = (TextView) findViewById(R.id.hasPurchaseTax_text);
        mArea_TextView = (TextView) findViewById(R.id.city_text);
        mpPwer_TextView = (TextView) findViewById(R.id.power_text);
        mFastChargingTime_TextView = (TextView) findViewById(R.id.fastChargingTime_text);
        mSlowChargingTime_TextView = (TextView) findViewById(R.id.slowChargingTime_text);
        mBatteryCapacity_TextView = (TextView) findViewById(R.id.batteryCapacity_text);
        mChargingInHome_TextView = (TextView) findViewById(R.id.chargingInHome_text);
        mGuaranteePeriod_TextView = (TextView) findViewById(R.id.guaranteePeriod_text);
        mMaintenancePeriod_TextView = (TextView) findViewById(R.id.maintenancePeriod_text);
        mPriceView = (TextView) findViewById(R.id.price_text);

    }


    public void setData(final EVNewWenergySubsidyDatailBean mNewWenergy_Ev_Subsidy_DatailBean) {

        if (mNewWenergy_Ev_Subsidy_DatailBean != null) {

            this.data = mNewWenergy_Ev_Subsidy_DatailBean;

            mCarName_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.autoName);
            mOrigPrice_TextView.setText(OtherUtil.amountFormat(mNewWenergy_Ev_Subsidy_DatailBean.origPrice));
            mSubsidyCountry_TextView.setText(OtherUtil.amountFormat(mNewWenergy_Ev_Subsidy_DatailBean.subsidyCountry));
            mSubsidyManufacturer_TextView.setText(OtherUtil.amountFormat(mNewWenergy_Ev_Subsidy_DatailBean.subsidyManufacturer));
            mSubsidyLocal_TextView.setText(OtherUtil.amountFormat(mNewWenergy_Ev_Subsidy_DatailBean.subsidyLocal));
            mSubsidyDealer_TextView.setText(OtherUtil.amountFormat(mNewWenergy_Ev_Subsidy_DatailBean.subsidyDealer));
            mSubsidyTotal_TextView.setText(OtherUtil.amountFormat(mNewWenergy_Ev_Subsidy_DatailBean.subsidyTotal));
            mBatteryType_TextView.setText(String.valueOf(mNewWenergy_Ev_Subsidy_DatailBean.batteryType));
            mHasPurchaseTax_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.hasPurchaseTax);
            mArea_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.area);

            /**
             * 生成html 政策列表
             */
            String s = Html.fromHtml(mNewWenergy_Ev_Subsidy_DatailBean.localPolicy).toString();
            new EV_CreateViewFromHTMLTools(s, mPolicyContainerView);

            mpPwer_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.power);
            mFastChargingTime_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.fastChargingTime);
            mSlowChargingTime_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.slowChargingTime);
            mBatteryCapacity_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.batteryCapacity);
            mChargingInHome_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.chargingInHome);
            mGuaranteePeriod_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.guaranteePeriod);
            mMaintenancePeriod_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.maintenancePeriod);
            mBatteryLife_TextView.setText(mNewWenergy_Ev_Subsidy_DatailBean.batteryLife);
            mPriceView.setText(OtherUtil.amountFormat(mNewWenergy_Ev_Subsidy_DatailBean.price));

            /**
             * 注册点击监听
             */
            setUpListeners();
        }


    }

    private void setUpListeners() {
        mBatteryType_TextView.setOnClickListener(this);
        mAllowanceClickView.setOnClickListener(this);
        mPolicyClickView.setOnClickListener(this);
        mChargeClickView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (isFirstIn){
            /**
             * 获取原始高度
             */
            allowanceHeight = mAllowanceContainerView.getHeight();
            policyHeight = mPolicyContainerView.getHeight();
            chargeHeight = mChargeContainerView.getHeight();
            isFirstIn = false;
        }

        if (v.getId() == R.id.batteryType_text) {
            if (data != null && !data.batteryURL.equals("")) {
                /**
                 * TODO 跳转 web activity
                 */
                ActivitySwitcherEV.toSingleWikiPage(getContext(),data.batteryURL,data.batteryType);
            }
        } else if (v.getId() == R.id.allowance_label_layout) {

            if (isAllowanceOpen) {
                animatorClose(mAllowanceContainerView, allowanceHeight);
                arrowClose(mAllowanceArrowView);
                isAllowanceOpen = false;
            } else {
                animatorOpen(mAllowanceContainerView, allowanceHeight);
                arrowOpen(mAllowanceArrowView);
                isAllowanceOpen = true;
            }

        }else if (v.getId() == R.id.policy_click_view){

            if (isPolicyOpen){
                animatorClose(mPolicyContainerView,policyHeight);
                arrowClose(mPolicyArrowView);
                isPolicyOpen = false;
            }else {
                animatorOpen(mPolicyContainerView,policyHeight);
                arrowOpen(mPolicyArrowView);
                isPolicyOpen = true;
            }

        }else if (v.getId() == R.id.charge_click_view){

            if (isChargeOpen){
                animatorClose(mChargeContainerView,chargeHeight);
                arrowClose(mChargeArrow);
                isChargeOpen = false;
            }else{
                animatorOpen(mChargeContainerView,chargeHeight);
                arrowOpen(mChargeArrow);
                isChargeOpen = true;
            }

        }
    }

    private void animatorOpen(final View containerView, int height) {
        animatorUtil = new LayoutAnimatorUtil(containerView, 0, height, LayoutAnimatorUtil.INCREASE);
        animatorUtil.setAnimatorCallBack(new LayoutAnimatorUtil.AnimatorCallBack() {
            @Override
            public void onAnimationEndCallBack(Animator animation) {
                containerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationStartCallBack(Animator animation) {
            }
        });
        animatorUtil.setDuration(100);
        animatorUtil.startAnimator();
    }

    private void animatorClose(final View containerView, int height) {
        animatorUtil = new LayoutAnimatorUtil(containerView, height, 0, LayoutAnimatorUtil.REDUCE);
        animatorUtil.setAnimatorCallBack(new LayoutAnimatorUtil.AnimatorCallBack() {
            @Override
            public void onAnimationEndCallBack(Animator animation) {
                containerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStartCallBack(Animator animation) {
            }
        });
        animatorUtil.startAnimator();
    }

    private void arrowOpen(View view){
        RotateAnimation rotateAnimator = new RotateAnimation(180, 0,view.getMeasuredWidth() / 2,view.getMeasuredHeight() / 2);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
        rotateAnimator.setFillAfter(true);
        rotateAnimator.setDuration(100);
        view.startAnimation(rotateAnimator);
    }

    private void arrowClose(View view){
        RotateAnimation rotateAnimator = new RotateAnimation(0, 180,view.getMeasuredWidth() / 2,view.getMeasuredHeight() / 2);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
        rotateAnimator.setFillAfter(true);
        rotateAnimator.setDuration(100);
        view.startAnimation(rotateAnimator);
    }


}
