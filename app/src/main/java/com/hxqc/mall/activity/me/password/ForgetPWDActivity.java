package com.hxqc.mall.activity.me.password;

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
import com.hxqc.mall.launch.view.CountdownButton;
import com.hxqc.util.IdCardUtils;

import hxqc.mall.R;

public class ForgetPWDActivity extends BasePWDInputActivity implements View.OnClickListener {

    EditText mRealNameView;
    EditText mIDView;
    EditText mCodeView;
    CountdownButton mGetIdentifyingCodeView;
    Button mNextStepBtn;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        //当前用户电话号码
        phoneNumber = UserInfoHelper.getInstance().getPhoneNumber(ForgetPWDActivity.this);

        initView();
    }

    private void initView() {
        initMVCV();
        mRealNameView = (EditText) findViewById(R.id.pay_pwd_name);
        mIDView = (EditText) findViewById(R.id.pay_pwd_id);
        mCodeView = (EditText) findViewById(R.id.real_code);
        mGetIdentifyingCodeView = (CountdownButton) findViewById(R.id.get_identifying_code);
        mNextStepBtn = (Button) findViewById(R.id.pwd_next);

        mGetIdentifyingCodeView.setOnClickListener(this);
        mNextStepBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pwd_next) {

            final String name = mRealNameView.getText().toString();
            final String id = mIDView.getText().toString();
            final String code = mCodeView.getText().toString();

            if (TextUtils.isEmpty(name)) {
                ToastHelper.showRedToast(ForgetPWDActivity.this, "真实姓名不能为空！");
                return;
            }

            if (TextUtils.isEmpty(id)) {
                ToastHelper.showRedToast(ForgetPWDActivity.this, "身份证不能为空！");
                return;
            }

            if (TextUtils.isEmpty(code)) {
                ToastHelper.showRedToast(ForgetPWDActivity.this, "验证码不能为空！");
                return;
            }

            if (!IdCardUtils.validateIdCard18(id)) {
                ToastHelper.showRedToast(ForgetPWDActivity.this, "身份证格式不正确！");
                return;
            }

            /**
             * TODO 验证待定
             */
            if (name.length() < 2) {
                ToastHelper.showYellowToast(ForgetPWDActivity.this, "请检查姓名格式");
                return;
            }

            new UserApiClient().verdifyUsernameForPayPWD(code, name, id, new LoadingAnimResponseHandler(ForgetPWDActivity.this, true) {
                @Override
                public void onSuccess(String response) {
                    /**
                     * 成功 跳转到第二步
                     */

                    ActivitySwitcher.resetForgetPaidPWD(code, name, id, ForgetPWDActivity.this);
                }

            });


        } else if (v.getId() == R.id.get_identifying_code) {
            //获取验证码
            mGetIdentifyingCodeView.realNameAuthenticationIdentifyingStart(phoneNumber, UserApiClient.FORGET_PWD);

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev != null) {
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
        apiClient.getCaptcha(phoneNumber, UserApiClient.FORGET_PWD, UserApiClient.VOICE_CAPTCHA, mVCV.getVoiceCaptchaResponseHandler());
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }
    // ----------------------语音验证码接口-------------end---------------------
}
