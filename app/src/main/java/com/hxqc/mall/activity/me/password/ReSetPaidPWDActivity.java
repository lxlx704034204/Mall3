package com.hxqc.mall.activity.me.password;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.pwd.MaterialPWDEditText;

import hxqc.mall.R;

public abstract class ReSetPaidPWDActivity extends BasePWDInputActivity implements OnClickListener {


    public MaterialPWDEditText mPwdView;
    public MaterialPWDEditText mPwdConfirmView;
    KeyboardView keyboardView;

    public Button mConfirmBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_paid_pwd);

        mPwdView = (MaterialPWDEditText) findViewById(R.id.input_new_pwd);
        mPwdConfirmView = (MaterialPWDEditText) findViewById(R.id.confirm_new_pwd);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        mConfirmBtn = (Button) findViewById(R.id.set_new_pwd);

        mPwdView.setKeyboard(keyboardView);
        mPwdConfirmView.setKeyboard(keyboardView);

        setUpViews();
    }

    private void setUpViews() {

        mConfirmBtn.setOnClickListener(this);

        mPwdView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPwdConfirmView.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        String pwd1 = mPwdView.getText().toString();
        String pwd2 = mPwdConfirmView.getText().toString();

        if (TextUtils.isEmpty(pwd1)) {
            ToastHelper.showRedToast(ReSetPaidPWDActivity.this, "支付密码不能为空！");
            return;
        }

        if (TextUtils.isEmpty(pwd2)) {
            ToastHelper.showRedToast(ReSetPaidPWDActivity.this, "确认密码不能为空！");
            return;
        }

        //验证密码是否相同
        if (pwd1.equals(pwd2)) {
            requestAndSetNewPWD();
        } else {
            ToastHelper.showRedToast(ReSetPaidPWDActivity.this, "两次输入密码不一致！");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev != null) {

            if (mPwdView.isKeyboardShow()){
                if (ev.getY() < screenHeight - mPwdView.getViewHeight()) {
                    mPwdView.hideKeyboard();
                }
            }

            if (mPwdConfirmView.isKeyboardShow()){
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

    abstract void requestAndSetNewPWD();
}
