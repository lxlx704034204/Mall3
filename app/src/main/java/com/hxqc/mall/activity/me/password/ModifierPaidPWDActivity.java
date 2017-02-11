package com.hxqc.mall.activity.me.password;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.mall.core.views.materialedittext.pwd.MaterialPWDEditText;
import com.hxqc.mall.launch.view.CountdownButton;

import hxqc.mall.R;

public class ModifierPaidPWDActivity extends BasePWDInputActivity implements View.OnClickListener {

    MaterialPWDEditText mOriginPwdView;
    EditText mCodeView;
    CountdownButton mGetIdentifyingCodeView;
    Button mNextBtn;
    KeyboardView keyboardView;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_paid_pwd);
        //当前用户电话号码
        phoneNumber = UserInfoHelper.getInstance().getPhoneNumber(ModifierPaidPWDActivity.this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        initMVCV();

        mCodeView = (EditText) findViewById(R.id.real_code);
        mGetIdentifyingCodeView = (CountdownButton) findViewById(R.id.get_identifying_code);
        mNextBtn = (Button) findViewById(R.id.pwd_next);

        mGetIdentifyingCodeView.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        mOriginPwdView = (MaterialPWDEditText) findViewById(R.id.pay_pwd_origin);
        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        mOriginPwdView.setKeyboard(keyboardView);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pwd_next) {
            final String originPWD = mOriginPwdView.getText().toString();
            final String code = mCodeView.getText().toString();

            if (TextUtils.isEmpty(code)) {
                ToastHelper.showRedToast(ModifierPaidPWDActivity.this, "验证码不能为空！");
                return;
            }

            if (TextUtils.isEmpty(originPWD)) {
                ToastHelper.showRedToast(ModifierPaidPWDActivity.this, "原密码不能为空！");
                return;
            }

            new UserApiClient().changePayPWD(code, originPWD, new LoadingAnimResponseHandler(ModifierPaidPWDActivity.this, true) {
                @Override
                public void onSuccess(String response) {
                    ActivitySwitcher.resetModifyPaidPWD(code, originPWD, ModifierPaidPWDActivity.this);
                }

            });


        } else if (v.getId() == R.id.get_identifying_code) {
            //获取验证码
            mGetIdentifyingCodeView.realNameAuthenticationIdentifyingStart(phoneNumber, UserApiClient.CHANGE_PWD);

        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev != null) {

            if (ev.getY() < screenHeight - mOriginPwdView.getViewHeight()) {
                if (mOriginPwdView.isKeyboardShow())
                    mOriginPwdView.hideKeyboard();
            }

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        }
        return super.dispatchTouchEvent(ev);
    }

    // ----------------------语音验证码接口-------------start---------------------

    public void initMVCV() {
        mVCV = (VoiceCaptchaView) findViewById(R.id.tv_vcv);
        mVCV.setVoiceCaptchaListener(this);
    }

    @Override
    public void getVoiceCaptcha() {
        UserApiClient apiClient = new UserApiClient();
        apiClient.getCaptcha(phoneNumber, UserApiClient.CHANGE_PWD, UserApiClient.VOICE_CAPTCHA, mVCV.getVoiceCaptchaResponseHandler());
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }
    // ----------------------语音验证码接口-------------end---------------------

}
