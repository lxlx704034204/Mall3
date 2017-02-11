package com.hxqc.mall.activity.auto;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.view.PlateNumberTextView;
import com.hxqc.mall.core.util.ToastHelper;

import hxqc.mall.R;

/**
 * liaoguilong
 * 整车质保查询
 */
public class QualityGuaranteeActivity extends BackActivity implements View.OnClickListener {

    private PlateNumberTextView mCityView, mNumView;
    private TextView mQualityGuaranteeDate;
    private Button mSelect;
    private LinearLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_guarantee_ativity);
        initView();
    }

    public void initView() {
        mCityView = (PlateNumberTextView) findViewById(R.id.commen_auto_info_license_city);
        mCityView.setMode(PlateNumberTextView.MODE_CITY);
        mNumView = (PlateNumberTextView) findViewById(R.id.commen_auto_info_license_number);
        mNumView.setMode(PlateNumberTextView.MODE_WORD);
        mQualityGuaranteeDate = (TextView) findViewById(R.id.ativity_quality_guarantee_date);
        mContentView = (LinearLayout) findViewById(R.id.ativity_quality_guarantee_content);
        mSelect = (Button) findViewById(R.id.ativity_quality_guarantee_but);
        mSelect.setOnClickListener(this);

    }

    public void select() {
        String plateNumber = mCityView.getText().toString() + mNumView.getText().toString();
        AutoInfoControl.getInstance().checkAutoInfo(this, "", plateNumber, new CallBackControl.CallBack<MyAuto>() {
            @Override
            public void onSuccess(MyAuto response) {
                mContentView.setVisibility(View.VISIBLE);
                if (response != null && !TextUtils.isEmpty(response.guaranteePeriod)) {
                    mQualityGuaranteeDate.setText(response.guaranteePeriod.toString());
                } else {
                    mQualityGuaranteeDate.setText("很抱歉，未能找到此车的相关信息");
                }
            }

            @Override
            public void onFailed(boolean offLine) {
                mContentView.setVisibility(View.VISIBLE);
                mQualityGuaranteeDate.setText("很抱歉，未能找到此车的相关信息");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mCityView.getText().toString())) {
            ToastHelper.showYellowToast(v.getContext(), "请选择地区别称!");
            return;
        }
        if (TextUtils.isEmpty(mNumView.getText().toString())) {
            ToastHelper.showYellowToast(v.getContext(), "请填写车牌号!");
            return;
        }
//        select();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AutoInfoControl.getInstance().killInstance();
    }
}
