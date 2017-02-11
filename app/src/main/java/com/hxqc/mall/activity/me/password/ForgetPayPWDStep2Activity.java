package com.hxqc.mall.activity.me.password;

import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

/**
 * Author: wanghao
 * Date: 2016-03-28
 * FIXME  忘记密码第二步
 * Todo
 */
public class ForgetPayPWDStep2Activity extends ReSetPaidPWDActivity{


    private String captcha;
    private String realName;
    private String idNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        captcha = getIntent().getStringExtra("captcha");
        realName = getIntent().getStringExtra("realName");
        idNumber = getIntent().getStringExtra("IDNumber");
    }

    @Override
    void requestAndSetNewPWD() {

        String newPWD = mPwdView.getText().toString();

        new UserApiClient().forgetPayPWDReset(captcha, realName, idNumber, newPWD, new LoadingAnimResponseHandler(ForgetPayPWDStep2Activity.this,true) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(ForgetPayPWDStep2Activity.this, "支付密码修改成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (!ForgetPayPWDStep2Activity.this.isFinishing()){
                            ActivitySwitcher.toSettings(ForgetPayPWDStep2Activity.this);
                            ForgetPayPWDStep2Activity.this.finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                }, 2300);
            }

        });

    }

    @Override
    public void getVoiceCaptcha() {

    }

    @Override
    public String getPhoneNumber() {
        return null;
    }
}
