package com.hxqc.mall.launch.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.launch.api.ApiClientAuthenticate;

import cz.msebera.android.httpclient.Header;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 说明:倒计时按钮
 * <p/>
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class CountdownButton extends TextView {
    private static final int COUNTDOWN = 0;//倒计时的handler信息
    private static final int COUNTDOWN_TIME = 60;//验证码倒计时秒数
    public boolean mIsRegisterActivity;//是否是RegisterActivity
    public boolean mIsInCountdownTime = false;//是否在倒计时当中
    int time = COUNTDOWN_TIME;//倒计时显示的秒数
    Timer mTimer;//验证码计时
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == COUNTDOWN) {
                countdownShow();
            }
            return false;
        }
    });

    public CountdownButton(Context context) {
        super(context);
    }

    public CountdownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 验证码倒计时显示
     */
    private void countdownShow() {
        time--;
        if (time > 0) {
            mIsInCountdownTime = true;
            this.setTextColor(getResources().getColor(R.color.title_and_main_text));
            this.setClickable(false);
            this.setText(time + getResources().getString(R.string.me_reget_identifying_code));
        } else {
            this.setTextColor(getResources().getColor(R.color.text_blue));
            this.setClickable(true);
            this.setText(R.string.me_get_identifying_code_again);
            time = COUNTDOWN_TIME;
            if (mTimer != null) {
                mIsInCountdownTime = false;
                mTimer.cancel();
                mTimer = null;
            }
        }
    }

    public void identifyingStart(String mPhoneNumber) {
        if (mIsRegisterActivity) {
            new ApiClientAuthenticate().canRegister(mPhoneNumber, new DialogResponseHandler(getContext(), getContext().getResources().getString(R.string.me_submitting_launch)) {
                @Override
                public void onSuccess(String response) {
                    countdownStart();
                }
            });
        } else {
            new ApiClientAuthenticate().isRegistered(mPhoneNumber, new DialogResponseHandler(getContext(), getContext().getResources().getString(R.string.me_submitting_launch)) {
                @Override
                public void onSuccess(String response) {
                    countdownStart();
                }
            });
        }
    }

    public void identifyingStart2(String mPhoneNumber) {
        new ApiClientAuthenticate().authenticate(mPhoneNumber, new DialogResponseHandler(getContext(), getContext().getResources().getString(R.string.me_submitting_launch)) {
            @Override
            public void onSuccess(String response) {
                countdownStart();
            }
        });
    }

    //    未知注册与否
    public void identifyingStart3(String mPhoneNumber,int userType) {
        new ApiClientAuthenticate().getUsedCarCaptcha(mPhoneNumber,userType, new DialogResponseHandler(getContext(), getContext().getResources().getString(R.string.me_submitting_launch)) {
            @Override
            public void onSuccess(String response) {
                countdownStart();
            }
        });
    }

    /**
     * 实名认证验证码
     *
     * @param mPhoneNumber 手机号
     */
    public void realNameAuthenticationIdentifyingStart(String mPhoneNumber, String type) {
        UserApiClient apiClient = new UserApiClient();
        apiClient.getCaptcha(mPhoneNumber, type, new DialogResponseHandler(getContext(), getContext().getResources().getString(R.string.me_submitting_launch)) {
            @Override
            public void onSuccess(String response) {
                countdownStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mIsInCountdownTime = false;
            }
        });
    }

    /**
     * 验证码倒计时开始
     */
    private void countdownStart() {
        if (mTimer != null) {
            return;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(COUNTDOWN);

            }
        }, 0, 1000);
    }
}
