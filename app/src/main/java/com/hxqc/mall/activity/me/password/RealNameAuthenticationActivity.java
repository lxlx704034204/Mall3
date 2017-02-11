package com.hxqc.mall.activity.me.password;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.core.views.materialedittext.pwd.MaterialPWDEditText;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.ValidatorTech;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.mall.launch.view.CountdownButton;
import com.hxqc.mall.paymethodlibrary.view.input.PayPwdViewManager;

import hxqc.mall.R;

public class RealNameAuthenticationActivity extends BasePWDInputActivity implements View.OnClickListener {


    UserApiClient apiClient;


    EditTextValidatorView mRealNameView;
    EditTextValidatorView mRealIDView;
    EditTextValidatorView mIdentifyCodeView;
    MaterialPWDEditText mPwdView;
    MaterialPWDEditText mPwdConfirmView;
    KeyboardView keyboardView;

    TextView mPwdNotifyView;
    TextView mPwdConfirmNotifyView;
    Button mSaveView;
    CountdownButton mGetIdentifyingCodeView;

    PayPwdViewManager manager;
    PayPwdViewManager managerConfirm;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_authentication);
        apiClient = new UserApiClient();
        manager = new PayPwdViewManager(RealNameAuthenticationActivity.this);
        managerConfirm = new PayPwdViewManager(RealNameAuthenticationActivity.this);

        //当前用户电话号码
        phoneNumber = UserInfoHelper.getInstance().getPhoneNumber(RealNameAuthenticationActivity.this);
        initMVCV();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        manager.onDestroy();
    }

    private void initView() {
        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);

        mRealNameView = (EditTextValidatorView) findViewById(R.id.real_name);
        mRealNameView.addValidator(ValidatorTech.RealName);

        mRealIDView = (EditTextValidatorView) findViewById(R.id.real_id);
        mRealIDView.addValidator(ValidatorTech.ID);

        mIdentifyCodeView = (EditTextValidatorView) findViewById(R.id.real_code);
        mIdentifyCodeView.addValidator(new VMallDivNotNull("请输入验证码", ""));

        //设置密码
        mPwdView = (MaterialPWDEditText) findViewById(R.id.real_pwd);
        mPwdNotifyView = (TextView) findViewById(R.id.real_notify_pwd);
        //确认密码
        mPwdConfirmView = (MaterialPWDEditText) findViewById(R.id.real_pwd_confirm);
        mPwdConfirmNotifyView = (TextView) findViewById(R.id.real_notify_pwd_confirm);

        mGetIdentifyingCodeView = (CountdownButton) findViewById(R.id.get_identifying_code);
        mGetIdentifyingCodeView.setOnClickListener(this);
        //保存
        mSaveView = (Button) findViewById(R.id.real_save_btn);
        mSaveView.setOnClickListener(this);

        setInputPwd();
    }

    private void setInputPwd() {
        mPwdView.setKeyboard(keyboardView);
        mPwdConfirmView.setKeyboard(keyboardView);

        mPwdView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setPWDNotify(false);
                    setPWDConfirmNotify(false);
                } else {
                    if (TextUtils.isEmpty(mPwdView.getText().toString())) {
                        setPWDNotify(true);
                    } else {
                        if (mPwdView.getText().toString().length() != 6) {
                            setPWDNotify(true);
                        } else {
                            setPWDNotify(false);
                        }
                    }
                }
            }
        });

        mPwdConfirmView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setPWDConfirmNotify(false);
                }
            }
        });

        mPwdConfirmView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(mPwdConfirmView.getText().toString())) {
                    setPWDConfirmNotify(true);
                } else {
                    if ((mPwdConfirmView.getText().toString()).equals(mPwdView.getText().toString())) {
                        setPWDConfirmNotify(false);
                    } else {
                        setPWDConfirmNotify(true);
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.real_save_btn) {

            String name = mRealNameView.getText().toString();
            String id = mRealIDView.getText().toString();
            String code = mIdentifyCodeView.getText().toString();
            String pwd1 = mPwdView.getText().toString();
            String pwd2 = mPwdConfirmView.getText().toString();

            VWholeEditManager manager = new VWholeEditManager(RealNameAuthenticationActivity.this);
            manager.autoAddVViews();

            if (!manager.validate()) {
                return;
            }

            if (TextUtils.isEmpty(pwd1)) {
                mPwdView.requestFocus();
                ToastHelper.showYellowToast(RealNameAuthenticationActivity.this, "请输入6位支付密码");
                return;
            }

            if (pwd1.length() != 6) {
                mPwdView.requestFocus();
                ToastHelper.showYellowToast(RealNameAuthenticationActivity.this, "请输入6位支付密码");
                return;
            }

            if (TextUtils.isEmpty(pwd2)) {
                mPwdConfirmView.requestFocus();
                ToastHelper.showYellowToast(RealNameAuthenticationActivity.this, "确认密码与支付密码不一致");
                return;
            }

            //验证密码是否相同
            if (pwd1.equals(pwd2)) {
                apiClient.setPaidPassword(code, name, id, pwd1, new LoadingAnimResponseHandler(RealNameAuthenticationActivity.this, true) {
                    @Override
                    public void onSuccess(String response) {
                        UserInfoHelper.getInstance().refreshUserInfo(RealNameAuthenticationActivity.this,
                                new UserInfoHelper.UserInfoAction() {
                                    @Override
                                    public void showUserInfo(User meData) {

                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                }, false);
                        NormalDialog dialog = new NormalDialog(RealNameAuthenticationActivity.this, "支付密码设置成功") {
                            @Override
                            protected void doNext() {
                                RealNameAuthenticationActivity.this.finish();
                            }
                        };
                        dialog.mCancelView.setVisibility(View.INVISIBLE);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
            } else {
                setPWDConfirmNotify(true);
                ToastHelper.showYellowToast(RealNameAuthenticationActivity.this, "确认密码与支付密码不一致");
            }

        } else if (v.getId() == R.id.get_identifying_code) {
            //获取验证码
            mGetIdentifyingCodeView.realNameAuthenticationIdentifyingStart(phoneNumber, UserApiClient.SET_PWD);
        }

    }

    private void setPWDNotify(boolean isShow) {
        if (isShow) {
            mPwdNotifyView.setVisibility(View.VISIBLE);
            mPwdView.setIsOutSideValidSuccess(false);
        } else {
            mPwdView.setIsOutSideValidSuccess(true);
            mPwdConfirmView.setText("");
            mPwdNotifyView.setVisibility(View.GONE);
        }
    }

    private void setPWDConfirmNotify(boolean isShow) {
        if (isShow) {
            mPwdConfirmView.setIsOutSideValidSuccess(false);
            mPwdConfirmNotifyView.setVisibility(View.VISIBLE);
        } else {
            mPwdConfirmView.setIsOutSideValidSuccess(true);
            mPwdConfirmNotifyView.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean dispatchTouchEvent(@Nullable MotionEvent ev) {
        if (ev != null) {
            if (mPwdView.isKeyboardShow()) {
                if (ev.getY() < screenHeight - mPwdView.getViewHeight()) {
                    mPwdView.hideKeyboard();
                }
            }

            if (mPwdConfirmView.isKeyboardShow()) {
                if (ev.getY() < screenHeight - mPwdConfirmView.getViewHeight()) {
                    mPwdConfirmView.hideKeyboard();
                }
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
        apiClient.getCaptcha(phoneNumber, UserApiClient.SET_PWD, UserApiClient.VOICE_CAPTCHA, mVCV.getVoiceCaptchaResponseHandler());
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }
    // ----------------------语音验证码接口-------------end---------------------
}
