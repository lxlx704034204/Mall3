package com.hxqc.mall.views.auto;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.LayoutAnimatorUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.CatalogTipView;

import hxqc.mall.R;


/**
 * Author:胡俊杰
 * Date: 2015/10/24
 * FIXME
 * Todo  包牌服务
 */
public class AutoVerifyLicenseService extends LinearLayout {
    CatalogTipView mPurchaseTaxView;//购置税
    CatalogTipView mTravelTaxView;//车船费
    CatalogTipView mInsuranceView;//强制险
    CatalogTipView mLicenseCostView;//上牌费
    CatalogTipView mSimplenessView;//裸车价
    View mLicenseViewGroup;//牌照费用详情
    LayoutAnimatorUtil animatorUtil;
    RadioButton mLicenseView;//办理
    RadioButton mUnLicenseView;//不办理
    View mSimpleLayout;//选择裸车layout
    View mLicenseLayout;//选择裸车包牌layout
    private RadioGroup.OnCheckedChangeListener mLicenseServiceListener;
    /**
     * 牌照办理
     */
    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                if (mLicenseServiceListener != null) {
                    mLicenseServiceListener.onCheckedChanged(null, compoundButton.getId());
                }
                switch (compoundButton.getId()) {
                    case R.id.transaction_simpleness:
                        mLicenseView.setChecked(false);
                        animatorUtil = new LayoutAnimatorUtil(mLicenseViewGroup, mSimplenessView.getHeight() * 4, 0, LayoutAnimatorUtil.REDUCE);
                        animatorUtil.setAnimatorCallBack(new LayoutAnimatorUtil.AnimatorCallBack() {
                            @Override
                            public void onAnimationEndCallBack(Animator animation) {
                                mLicenseViewGroup.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationStartCallBack(Animator animation) {
                            }
                        });
                        animatorUtil.startAnimator();
                        break;
                    case R.id.transaction_license:
                        mUnLicenseView.setChecked(false);
                        mLicenseViewGroup.setVisibility(View.VISIBLE);
//                    DebugLog.i("test_bug", "mSimplenessView.getHeight()__INCREASE:" + mSimplenessView.getHeight());
                        animatorUtil = new LayoutAnimatorUtil(mLicenseViewGroup, 0, mSimplenessView.getHeight() * 4, LayoutAnimatorUtil.INCREASE);
                        animatorUtil.setAnimatorCallBack(new LayoutAnimatorUtil.AnimatorCallBack() {
                            @Override
                            public void onAnimationEndCallBack(Animator animation) {
                                mLicenseViewGroup.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationStartCallBack(Animator animation) {

                            }
                        });
                        animatorUtil.startAnimator();
                        break;
                }
            }

        }
    };

    public AutoVerifyLicenseService(Context context) {
        super(context);
    }

    public AutoVerifyLicenseService(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_auto_verify_pai_service, this);
        mPurchaseTaxView = (CatalogTipView) findViewById(R.id.purchase_tax);
        mTravelTaxView = (CatalogTipView) findViewById(R.id.vehicle_tax);
        mInsuranceView = (CatalogTipView) findViewById(R.id.force_insurance);
        mLicenseCostView = (CatalogTipView) findViewById(R.id.license_cost);
        mSimplenessView = (CatalogTipView) findViewById(R.id.simpleness);
//        mLicensePlateView = (RadioGroup) findViewById(R.id.license_plate_radiogroup);
//        mLicensePlateView.setOnCheckedChangeListener(new LicenseChange());
        mLicenseViewGroup = findViewById(R.id.license_view_group);
        //初始化时只显示裸车价
        ViewGroup.LayoutParams layoutParams = mLicenseViewGroup.getLayoutParams();
        layoutParams.height = 1;
        mLicenseViewGroup.setLayoutParams(layoutParams);
        mLicenseViewGroup.setAlpha(0);

        mUnLicenseView = (RadioButton) findViewById(R.id.transaction_simpleness);
        mLicenseView = (RadioButton) findViewById(R.id.transaction_license);

        mUnLicenseView.setOnCheckedChangeListener(checkedChangeListener);
        mLicenseView.setOnCheckedChangeListener(checkedChangeListener);

        mSimpleLayout = findViewById(R.id.simple_layout);
        mLicenseLayout = findViewById(R.id.license_layout);
    }

    public void setLicenseServiceListener(RadioGroup.OnCheckedChangeListener licenseServiceListener) {
        this.mLicenseServiceListener = licenseServiceListener;
    }

    public void setAutoDetail(AutoDetail autoDetail) {


        mSimplenessView.setLeftTextView("裸车价", OtherUtil.stringToMoney(autoDetail.getItemPrice()));
        mSimplenessView.getRightTextView().setTextColor(getResources().getColor(R.color.text_color_red));
        mPurchaseTaxView.setLeftTextView(getContext().getString(R.string.pre_purchase_tax), OtherUtil.stringToMoney(autoDetail.getFare().getPurchaseTax()));//购置税
        mTravelTaxView.setLeftTextView(getContext().getString(R.string.pre_car_tax), OtherUtil.stringToMoney(autoDetail.getFare().getTravelTax()));//车船费
        mInsuranceView.setLeftTextView(getContext().getString(R.string.traffic_insurance), OtherUtil.stringToMoney(autoDetail.getFare().getInsurance()));//强制险
        mLicenseCostView.setLeftTextView(getContext().getString(R.string.me_service_fee), OtherUtil.stringToMoney(autoDetail.getFare().getPlateTax()));//上牌费
        if (autoDetail.getFare().getPlateTax() != 0) {
            //上牌服务费不为0显示红色，新能源电动车免费上牌
            mLicenseCostView.getRightTextView().setTextColor(getResources().getColor(R.color.text_color_red));
        }


        //新能源
        switch (autoDetail.getItemCategory()) {
            case AutoItem.CATEGORY_AUTOMOBILE:
                break;
            case AutoItem.CATEGORY_NEW_ENERGY:
                mSimpleLayout.setVisibility(View.GONE);
                mLicenseView.setChecked(true);
                break;
            default:
                break;
        }
    }


}
