package com.hxqc.mall.auto.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.FocusEditBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.core.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 01
 * Des: 保养添加车辆与修改车辆
 * FIXME
 * Todo
 */
public class MaintainEditAutoActivity extends FocusEditBackActivity {

    private static final String TAG = AutoInfoContants.LOG_J;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_auto);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        AutoTypeControl.getInstance().killInstance();
        AutoHelper.getInstance().killInstance(this);
        finish();
        super.onDestroy();
    }

}
