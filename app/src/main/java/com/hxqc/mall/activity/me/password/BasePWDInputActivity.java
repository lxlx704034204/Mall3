package com.hxqc.mall.activity.me.password;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.util.ScreenUtil;

public abstract class BasePWDInputActivity extends BackActivity implements VoiceCaptchaView.VoiceCaptchaListener{

    VoiceCaptchaView mVCV;
    InputMethodManager inputMethodManager;
    int screenHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        screenHeight = ScreenUtil.getScreenHeight(this);
    }

}
