package com.hxqc.mall.views.auto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.hxqc.mall.control.AutoItemDataControl;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/11/14
 * FIXME
 * Todo  确认购买，保险
 */
public class AutoVerifyInsurance extends RelativeLayout {
    AutoItemDataControl mAutoDataControl;

    public AutoVerifyInsurance(Context context) {
        super(context);
    }

    public AutoVerifyInsurance(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_auto_verify_insurance, this);
        ((CheckBox) findViewById(R.id.insurance_checkbox)).setOnCheckedChangeListener(new InsuranceCheckListener());
        mAutoDataControl = AutoItemDataControl.getInstance();
        findViewById(R.id.insurance_calculator).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转保险计算器
                ActivitySwitcher.toInsuranceCounter(getContext(), mAutoDataControl.getAutoDetail().getAutoBaseInformation().getItemID());
            }
        });
    }

    /**
     * 保险办理
     */
    class InsuranceCheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mAutoDataControl.mOrderPayRequest.setInsurance(isChecked);
        }
    }
}
