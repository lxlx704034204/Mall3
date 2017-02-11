package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.BackActivity;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 29
 * FIXME
 * Todo 违章查缴及代办页面
 */
@Deprecated
public class IllegalQueryAndAgencyActivity extends BackActivity {

    private LinearLayout mIllegalQueryView;
    private LinearLayout mVehicleInspectionView;
    private LinearLayout mLicenseInspectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_illegal_and_agency);
        initView();

        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        mIllegalQueryView.setOnClickListener(clickIllegalQueryListener);
        mVehicleInspectionView.setOnClickListener(clickVehicleInspectionListener);
        mLicenseInspectionView.setOnClickListener(clickLicenseInspectionListener);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mIllegalQueryView = (LinearLayout) findViewById(R.id.illegal_query_btn);
        mVehicleInspectionView = (LinearLayout) findViewById(R.id.vehicle_inspection_btn);
        mLicenseInspectionView = (LinearLayout) findViewById(R.id.license_inspection_btn);
    }

    /**
     * 违章查缴按钮监听
     */
    private View.OnClickListener clickIllegalQueryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAround.toIllegalQueryActivity(IllegalQueryAndAgencyActivity.this);
//            ActivitySwitchAround.toOrderDetailActivity(IllegalQueryAndAgencyActivity.this,"123456", OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL);
//            ActivitySwitchAround.toIllegalProcessingSuccessActivity(IllegalQueryAndAgencyActivity.this);
        }
    };

    /**
     * 车辆年审按钮监听
     */
    private View.OnClickListener clickVehicleInspectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAround.toVehicleInspectionActivity(IllegalQueryAndAgencyActivity.this);
        }
    };

    /**
     * 驾照年审按钮监听
     */
    private View.OnClickListener clickLicenseInspectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAround.toDriversLicenseChangeActivity(IllegalQueryAndAgencyActivity.this);
        }
    };
}
