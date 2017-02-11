package com.hxqc.mall.launch.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.views.AgreementView;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.mall.launch.api.ApiClientAuthenticate;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.mall.launch.view.InputIdentifyingCodeNoButton;
import com.hxqc.mall.launch.view.InputPhoneVerificationCode;

/**
 * liaogulong
 *  验证码登录
 *  2016年3月11日
 */
public class CodeLoginActivity extends BaseAuthenticateActivity implements View.OnClickListener{
    InputPhoneVerificationCode mInputPhoneNumberView;//手机号 和 验证码
    InputIdentifyingCodeNoButton mInputIdentifyingCodeView;//验证码
    Button mLoginView;//登陆
    TextView mAccountPasswordLoginView;// 账户密码登录
    String mToWhere;//控制登录时转向的界面
    AgreementView mAgreementView;
    VoiceCaptchaView mVoiceCaptchaView;
    int mTab;
    int mEntrance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_login);
//        ImageView welcome = (ImageView) findViewById(R.id.welcome);
//        ImageUtil.setImage(CodeLoginActivity.this,welcome,);


        mToWhere = getIntent().getStringExtra(ActivitySwitchBase.TO_WHERE);
        mTab = getIntent().getIntExtra(ActivitySwitchBase.TAB, 2);
        mEntrance=getIntent().getIntExtra(ActivitySwitchAuthenticate.ENTRANCE,ActivitySwitchBase.ENTRANCE_SHOP);

        mInputPhoneNumberView = (InputPhoneVerificationCode) findViewById(R.id.input_phone_number);
        mInputIdentifyingCodeView= (InputIdentifyingCodeNoButton) findViewById(R.id.input_identifying_code);
        mLoginView = (Button) findViewById(R.id.login);
        mAccountPasswordLoginView = (TextView) findViewById(R.id.accountpassword_login);
        mLoginView.setOnClickListener(this);
        mAccountPasswordLoginView.setOnClickListener(this);
        mInputPhoneNumberView.getCountdownButton().setOnClickListener(this);
        mInputPhoneNumberView.setGetIdentifyingCodeViewText("获取验证码");
        mInputIdentifyingCodeView.setIdentifyingCodeViewType(InputType.TYPE_CLASS_TEXT);
        mInputPhoneNumberView.mPhoneNumberView.setText(UserInfoHelper.getInstance().getPhoneNumber(CodeLoginActivity.this));
        mAgreementView= (AgreementView) findViewById(R.id.xieyi);
        mAgreementView.setAgreementClickListener(new AgreementView.AgreementClickListener() {
            @Override
            public void onClick() {
                    //跳转用户协议H5
                ActivitySwitchAuthenticate.toRegisterAgreement(CodeLoginActivity.this, ApiClientAuthenticate.getRegisterAgreement());
            }
        });
        mVoiceCaptchaView= (VoiceCaptchaView) findViewById(R.id.voice_captcha);
        mVoiceCaptchaView.setVoiceCaptchaListener(new VoiceCaptchaView.VoiceCaptchaListener() {
            @Override
            public void getVoiceCaptcha() {
                new ApiClientAuthenticate().authenticate20(mInputPhoneNumberView.getPhoneNumber(),mVoiceCaptchaView.getVoiceCaptchaResponseHandler());
            }

            @Override
            public String getPhoneNumber() {
                return mInputPhoneNumberView.getPhoneNumber();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getToken(CodeLoginActivity.this))) {
            //已登录关闭页面
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login){
            login();
        }else if (i == R.id.accountpassword_login){
            //帐号密码登录
            ActivitySwitchAuthenticate.toLogin(CodeLoginActivity.this,mEntrance);
        }else if(i==R.id.get_identifying_code){
            //获取验证码
            if (FormatCheck.checkPhoneNumber(mInputPhoneNumberView.getPhoneNumber(), v.getContext()) == FormatCheck.CHECK_SUCCESS) {
//                if (!(mInputPhoneNumberView.getPhoneNumber().equals(UserInfoHelper.getInstance().getPhoneNumber(v.getContext())))) {
//                    ToastHelper.showYellowToast(v.getContext(), R.string.me_phone_number_error);
//                } else {
                mInputPhoneNumberView.getCountdownButton().identifyingStart2(mInputPhoneNumberView.getPhoneNumber());
//                }
            }
        }
    }

    //  登陆
    private void login() {
        if (canLogin()) {
            loginAction(mInputPhoneNumberView.getPhoneNumber(),
                    mInputIdentifyingCodeView.getIdentifyingCode(),2);

        }
    }

    //验证登录
    private boolean canLogin() {
        return FormatCheck.checkPhoneNumber(mInputPhoneNumberView.getPhoneNumber(), CodeLoginActivity.this)
                == FormatCheck.CHECK_SUCCESS && FormatCheck.
                checkIdentifyingCode(mInputIdentifyingCodeView.getIdentifyingCode(), CodeLoginActivity.this)
                == FormatCheck.CHECK_SUCCESS;
    }

}


