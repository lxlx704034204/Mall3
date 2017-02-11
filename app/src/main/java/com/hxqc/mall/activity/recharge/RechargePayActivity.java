package com.hxqc.mall.activity.recharge;

import android.view.View;

import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * Author:李烽
 * Date:2016-09-06
 * FIXME
 * Todo 充值付款界面
 */
public class RechargePayActivity extends RechargePayBaseActivity {

    @Override
    public String getDialogTitle() {
        return null;
    }

    @Override
    public String getDialogContent() {
        return null;
    }

    @Override
    public View getChildHeadView() {
        return null;
    }

    @Override
    public void dialogDoNext() {
//                exit();
        ActivitySwitchBase.toMain(RechargePayActivity.this, 3);
        finish();
    }

}
