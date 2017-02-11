package com.hxqc.aroundservice.activity;

import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.payment.activity.PayFinishActivity;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 28
 * Des: 周边支付完成页面
 * FIXME
 * Todo
 */

public class CircumPayFinishActivity extends PayFinishActivity {

    @Override
    protected void finishPay() {
        ActivitySwitcher.toMain(this,2);
    }
}
