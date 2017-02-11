package com.hxqc.mall.launch.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.mall.launch.view.InputPassword;
import com.hxqc.mall.launch.view.InputPhoneNumber;

/**
 * 说明:登陆
 * <p/>
 * author: 吕飞
 * since: 2015-03-05
 * Copyright:恒信汽车电子商务有限公司
 * update author:liaoguilong
 * update time:2016年12月21日 09:25:43
 */
public class LoginActivity extends BaseAuthenticateActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    InputPhoneNumber mInputPhoneNumberView;//手机号
    InputPassword mInputPasswordView;//密码
    Button mLoginView;//登陆
    TextView mCodeLoginView;//验证码登陆
    TextView mRegisterView;//注册
    TextView mForgetPasswordView;//忘记密码


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ImageView welcome = (ImageView) findViewById(R.id.welcome);
//        ImageUtil.setImage(LoginActivity.this,welcome,R.drawable.bg_login_welcome);


        mInputPhoneNumberView = (InputPhoneNumber) findViewById(R.id.input_phone_number);
        mInputPasswordView = (InputPassword) findViewById(R.id.input_password);
        mLoginView = (Button) findViewById(R.id.login);
        mRegisterView = (TextView) findViewById(R.id.register);
        mForgetPasswordView = (TextView) findViewById(R.id.forget_password);
        mCodeLoginView = (TextView) findViewById(R.id.code_login);
        mLoginView.setOnClickListener(this);
        mRegisterView.setOnClickListener(this);
        mForgetPasswordView.setOnClickListener(this);
        mCodeLoginView.setOnClickListener(this);
        mInputPasswordView.getEditText().setOnEditorActionListener(this);
        mInputPhoneNumberView.mPhoneNumberView.setText(UserInfoHelper.getInstance().getPhoneNumber(LoginActivity.this));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getToken(LoginActivity.this))) {
            //已登录关闭页面
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login) {
            login();
        } else if (i == R.id.register) {
            ActivitySwitchAuthenticate.toRegister(this,getIntent().getIntExtra(ActivitySwitchAuthenticate
                    .ENTRANCE, mEntrance));
        } else if (i == R.id.forget_password) {
            ActivitySwitchAuthenticate.toForgetPassword(this, mInputPhoneNumberView.getPhoneNumber());
        } else if (i == R.id.code_login) {
            ActivitySwitchAuthenticate.toCodeLogin(this,mEntrance);
        }

    }

    //  登陆
    private void login() {
        if (canLogin()) {
            loginAction(mInputPhoneNumberView.getPhoneNumber(), mInputPasswordView.getPassword(), 1);
        }
    }


    //验证登录
    private boolean canLogin() {
        if (FormatCheck.checkPhoneNumber(mInputPhoneNumberView.getPhoneNumber(), this)
                != FormatCheck.CHECK_SUCCESS) {
            return false;
        } else if (TextUtils.isEmpty(mInputPasswordView.getPassword())) {
            ToastHelper.showYellowToast(this, R.string.me_password_hint);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            login();
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        UserInfoHelper.getInstance().loginCancel();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        UserInfoHelper.getInstance().loginCancel();
        super.onBackPressed();
    }
}


