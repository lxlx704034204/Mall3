package com.hxqc.mall.core.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.views.dialog.NoCancelDialog;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 说明:语音验证
 *
 * @author: 吕飞
 * @since: 2016-12-14
 * Copyright:恒信汽车电子商务有限公司
 */

public class VoiceCaptchaView extends TextView {
    private static final int COUNTDOWN = 0;//倒计时的handler信息
    private static final int COUNTDOWN_TIME = 60;//验证码倒计时秒数
    String mString = "收不到短信？需要 语音验证码";
    VoiceCaptchaListener mVoiceCaptchaListener;
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

    public void setVoiceCaptchaListener(VoiceCaptchaListener voiceCaptchaListener) {
        this.mVoiceCaptchaListener = voiceCaptchaListener;
    }

    public VoiceCaptchaView(Context context) {
        super(context);
    }

    public VoiceCaptchaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(LinkMovementMethod.getInstance());
        setText(getText());
    }

    public SpannableStringBuilder getText() {
        SpannableStringBuilder style = new SpannableStringBuilder(mString);
        style.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (FormatCheck.checkPhoneNumber(mVoiceCaptchaListener.getPhoneNumber(), getContext()) == FormatCheck.CHECK_SUCCESS) {
                    mVoiceCaptchaListener.getVoiceCaptcha();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#2196f3"));
            }
        }, 9, mString.length(), 0);
        return style;
    }

    public interface VoiceCaptchaListener {
        void getVoiceCaptcha();

        String getPhoneNumber();
    }

    public LoadingAnimResponseHandler getVoiceCaptchaResponseHandler() {
        return new LoadingAnimResponseHandler(getContext()) {
            @Override
            public void onSuccess(String response) {
                new NoCancelDialog(getContext(), "提交成功", "正在拨打您的手机，请注意来电", "我知道了") {
                    @Override
                    protected void doNext() {

                    }
                }.show();
                countdownStart();
            }
        };
    }

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

    /**
     * 验证码倒计时显示
     */
    private void countdownShow() {
        time--;
        if (time > 0) {
            this.setText(mString + "(" + time + "s)");
        } else {
            this.setText(getText());
            time = COUNTDOWN_TIME;
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
    }
}
