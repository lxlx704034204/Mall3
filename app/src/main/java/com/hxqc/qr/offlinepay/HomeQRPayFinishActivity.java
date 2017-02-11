package com.hxqc.qr.offlinepay;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.payment.activity.PayFinishActivity;

/**
 * Author:  wh
 * Date:  2016/11/16
 * FIXME
 * Todo
 */

public class HomeQRPayFinishActivity extends PayFinishActivity {

    @Override
    protected void pressButtonFinish() {
        ActivitySwitchBase.toMain(this, 0);
        finish();
    }

    @Override
    protected void finishPay() {

    }
}
