package com.hxqc.mall.launch.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.mall.launch.api.ApiClientAuthenticate;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.mall.launch.view.InputIdentifyingCodeNoButton;
import com.hxqc.mall.launch.view.InputPassword;
import com.hxqc.mall.launch.view.InputPhoneVerificationCode;

/**
 * 说明:注册
 * <p/>
 * author: 吕飞
 * since: 2015-03-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class RegisterActivity extends BaseAuthenticateActivity implements View.OnClickListener {
    InputPhoneVerificationCode mInputPhoneNumberView;//手机号
    InputPassword mInputPasswordView;//密码
    InputIdentifyingCodeNoButton mInputIdentifyingCodeView;//验证码
    CheckBox mAgreeView;//同意
    LinearLayout mRegisteredLoginView;//登陆
    Button mRegisterView;//注册
    TextView mRegisterAgreementView;//注册协议
    int entrance;//注册入口
    VoiceCaptchaView mVoiceCaptchaView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.primary))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
        Bundle bundle = getIntent().getExtras();
        entrance = bundle.getInt(ActivitySwitchAuthenticate.ENTRANCE, ActivitySwitchBase.ENTRANCE_SHOP);
        mInputPhoneNumberView = (InputPhoneVerificationCode) findViewById(R.id.input_phone_number);
        mInputPasswordView = (InputPassword) findViewById(R.id.input_password);
        mInputIdentifyingCodeView = (InputIdentifyingCodeNoButton) findViewById(R.id.input_identifying_code);
        mAgreeView = (CheckBox) findViewById(R.id.agree);
        mRegisteredLoginView = (LinearLayout) findViewById(R.id.registered_login);
        mRegisterView = (Button) findViewById(R.id.register);
        mRegisterAgreementView = (TextView) findViewById(R.id.register_agreement);
        mInputPhoneNumberView.getCountdownButton().mIsRegisterActivity = true;
        mInputPasswordView.mIsRegisterActivity = true;
        mRegisteredLoginView.setOnClickListener(this);
        mInputPhoneNumberView.getCountdownButton().setOnClickListener(this);
        mRegisterView.setOnClickListener(this);
        mRegisterAgreementView.setOnClickListener(this);
        if(!TextUtils.isEmpty(UserInfoHelper.getInstance().getUserPhotoNum()))
        mInputPhoneNumberView.mPhoneNumberView.setText(UserInfoHelper.getInstance().getUserPhotoNum());
        mVoiceCaptchaView= (VoiceCaptchaView) findViewById(R.id.voice_captcha);
        mVoiceCaptchaView.setVoiceCaptchaListener(new VoiceCaptchaView.VoiceCaptchaListener() {
            @Override
            public void getVoiceCaptcha() {
                new ApiClientAuthenticate().canRegister2(mInputPhoneNumberView.getPhoneNumber(),mVoiceCaptchaView.getVoiceCaptchaResponseHandler());
            }

            @Override
            public String getPhoneNumber() {
                return mInputPhoneNumberView.getPhoneNumber();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.registered_login) {
            ActivitySwitchAuthenticate.toCodeLogin(this,  entrance);
        } else if (i == R.id.register) {
            if (canRegister()) {
                final String phoneNumber = mInputPhoneNumberView.getPhoneNumber();
                final String pw = mInputPasswordView.getPassword();
                new ApiClientAuthenticate().register(phoneNumber, pw,
                        mInputIdentifyingCodeView.getIdentifyingCode(), entrance,
                        new DialogResponseHandler(this, getResources().getString(R.string.me_submitting_launch)) {
                            @Override
                            public void onSuccess(String response) {
                                UserInfoHelper.getInstance().loginAction(RegisterActivity.this, phoneNumber, pw, 1);

                            }
                        });
            }
        } else if (i == R.id.get_identifying_code) {
            if (FormatCheck.checkPhoneNumber(mInputPhoneNumberView.getPhoneNumber(), this) == FormatCheck.CHECK_SUCCESS) {
                mInputPhoneNumberView.getCountdownButton().identifyingStart(mInputPhoneNumberView.getPhoneNumber());
            }
        } else if (i == R.id.register_agreement) {
            ActivitySwitchAuthenticate.toRegisterAgreement(RegisterActivity.this,
                    ApiClientAuthenticate.getRegisterAgreement());

        }

    }

    private boolean canRegister() {
        return (FormatCheck.checkPhoneNumber(mInputPhoneNumberView.getPhoneNumber(), this) == FormatCheck.CHECK_SUCCESS) && (FormatCheck.checkPassword(mInputPasswordView.getPassword(), this, true) < FormatCheck.NO_PASSWORD) && (FormatCheck.checkIdentifyingCode(mInputIdentifyingCodeView.getIdentifyingCode(), this) == FormatCheck.CHECK_SUCCESS) && checkAgreement();
    }

    private boolean checkAgreement() {
        if (mAgreeView.isChecked()) {
            return true;
        } else {
            ToastHelper.showYellowToast(this, R.string.me_please_agreement);
            return false;
        }
    }
}
