package com.hxqc.mall.auto.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * FIXME
 * Todo 通用车辆信息列表头
 */
public class CommonAutoInfoHeaderViewV2 extends LinearLayout {

//    private PlateNumberTextView mLicenseCityView;//城市
//    private PlateNumberTextView mLicenseNumView;//车牌号
    private PlateNumberEditText mPlateNumView;//车牌
    private TextView mPlateNumTitleView;//车牌标题
    private EditText mVINView;//VIN码
    private EditTextValidatorView mAutoTypeView;//车辆类型
    private TextView mMaintainView;//保养时间
    private TextView mDriveDistanceView;//公里数
    private TextView mQualityDateView;//质保时间
    private EditTextValidatorView mMileageView;//行驶公里数
    private LinearLayout mMaintainInfoView;//保养时间及公里数质保时间总条目
    private LinearLayout mMileageInfoView;//行驶公里数总条目
    private TextView mExplainContentView;

    public CommonAutoInfoHeaderViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_common_auto_info, this);
//        mLicenseCityView = (PlateNumberTextView) view.findViewById(R.id.commen_auto_info_license_city);
//        mLicenseNumView = (PlateNumberTextView) view.findViewById(R.id.commen_auto_info_license_number);
        mPlateNumTitleView = (TextView) findViewById(R.id.commen_auto_info_plate_number_title);
        mExplainContentView = (TextView) view.findViewById(R.id.common_explain_content);
        mPlateNumView = (PlateNumberEditText) view.findViewById(R.id.commen_auto_info_plate_number);
        mVINView = (EditText) view.findViewById(R.id.commen_auto_info_vin);
        mAutoTypeView = (EditTextValidatorView) view.findViewById(R.id.commen_auto_info_type);
        mMaintainView = (TextView) view.findViewById(R.id.commen_auto_info_maintain);
        mDriveDistanceView = (TextView) view.findViewById(R.id.commen_auto_info_driving_distance);
        mQualityDateView = (TextView) view.findViewById(R.id.commen_auto_info_date);
        mMileageView = (EditTextValidatorView) view.findViewById(R.id.commen_auto_info_mileage);
        mAutoTypeView.addValidator(new VMallDivNotNull("请选择车辆信息",""));
        mMileageView.addValidator(new VMallDivNotNull("请输入行驶里程",""));

        mMaintainInfoView = (LinearLayout) view.findViewById(R.id.commen_auto_info_maintain_info);
        mMileageInfoView = (LinearLayout) view.findViewById(R.id.commen_auto_info_mileage_info);

    }

//    public PlateNumberTextView getmLicenseCityView() {
//        return mLicenseCityView;
//    }
//
//    public PlateNumberTextView getmLicenseNumView() {
//        return mLicenseNumView;
//    }

    public TextView getmExplainContentView() {
        return mExplainContentView;
    }

    public void setExplainContentViewVisibility(boolean visibility) {
        if (visibility) {
            mExplainContentView.setVisibility(VISIBLE);
        } else {
            mExplainContentView.setVisibility(GONE);
        }

    }

    public void setPlateNumTitleDrawLeft(int id) {
        Drawable drawableLeft = getResources().getDrawable(id);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        mPlateNumTitleView.setCompoundDrawables(drawableLeft,null,null,null);
    }

    public PlateNumberEditText getmPlateNumView() {
        return mPlateNumView;
    }

    public EditText getmVINView() {
        return mVINView;
    }

    public EditTextValidatorView getmAutoTypeView() {
        return mAutoTypeView;
    }

    public TextView getmMaintainView() {
        return mMaintainView;
    }

    public TextView getmDriveDistanceView() {
        return mDriveDistanceView;
    }

    public TextView getmQualityDateView() {
        return mQualityDateView;
    }

    public EditTextValidatorView getmMileageView() {
        return mMileageView;
    }

    public LinearLayout getmMaintainInfoView() {
        return mMaintainInfoView;
    }

    public LinearLayout getmMileageInfoView() {
        return mMileageInfoView;
    }
}
