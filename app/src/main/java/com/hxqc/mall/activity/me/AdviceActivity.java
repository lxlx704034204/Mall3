package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;

import hxqc.mall.R;

/**
 * 说明:意见反馈
 * <p/>
 * author: 吕飞
 * since: 2015-03-23
 * Copyright:恒信汽车电子商务有限公司
 */
public class AdviceActivity extends AppBackActivity implements View.OnClickListener, TextWatcher {
    public static final int NO_LABEL = 0;
    MaterialEditText mPhoneNumberView;
    MaterialEditText mAdviceView;//意见输入框
    TextView mAdviceViewNum;//意见反馈字数
    ImageView mClearView;//清除键
    Button mSubmitView;//提交
    CheckBox mFlowView;//流量问题
    CheckBox mUiView;//界面建议
    CheckBox mFunctionView;//功能建议
    CheckBox mOperateView;//操作建议
    CheckBox mNewNeedView;//新需求
    CheckBox mOtherView;//其他问题
    int mLabelResult = NO_LABEL;//标签选择结果
    String mLabelResultString;//标签选择结果二进制文本
    String mPhoneNumber;//提交的电话号码
    boolean mUseSavedNumber;//是否使用保存的号码

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.primary))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
        mPhoneNumberView = (MaterialEditText) findViewById(R.id.phone_number);
        mAdviceView = (MaterialEditText) findViewById(R.id.advice);
        mAdviceViewNum = (TextView) findViewById(R.id.advice_num);
        mClearView = (ImageView) findViewById(R.id.clear);
        mSubmitView = (Button) findViewById(R.id.submit);
        mFlowView = (CheckBox) findViewById(R.id.flow);
        mUiView = (CheckBox) findViewById(R.id.ui);
        mFunctionView = (CheckBox) findViewById(R.id.function);
        mOperateView = (CheckBox) findViewById(R.id.operate);
        mNewNeedView = (CheckBox) findViewById(R.id.new_need);
        mOtherView = (CheckBox) findViewById(R.id.other);
        init();
    }

    private void init() {
        showPhoneNumber();
        mPhoneNumberView.addTextChangedListener(this);
        mAdviceView.addTextChangedListener(this);
        mSubmitView.setOnClickListener(this);
        mClearView.setOnClickListener(this);
        mPhoneNumberView.setSelection(mPhoneNumberView.getText().toString().trim().length());
    }

    private void showPhoneNumber() {
        mPhoneNumber = UserInfoHelper.getInstance().getPhoneNumber(AdviceActivity.this);
        if (UserInfoHelper.getInstance().isLogin(this) && (!TextUtils.isEmpty(mPhoneNumber))) {
            mPhoneNumberView.setText(String.format("%s****%s",
                    mPhoneNumber.substring(0, 3), mPhoneNumber.substring(7)));
            mUseSavedNumber = true;
        }
        mPhoneNumberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mUseSavedNumber) {
                    mUseSavedNumber = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.clear:
                mPhoneNumberView.setText("");
                break;
            case R.id.submit:
                mLabelResultString = OtherUtil.boolean2Int(mOtherView.isChecked()) + "" + OtherUtil.boolean2Int(mNewNeedView.isChecked()) + OtherUtil.boolean2Int(mOperateView.isChecked()) + OtherUtil.boolean2Int(mFunctionView.isChecked()) + OtherUtil.boolean2Int(mUiView.isChecked()) + OtherUtil.boolean2Int(mFlowView.isChecked());
                mLabelResult = Integer.parseInt(mLabelResultString, 2);
                if (canSubmit()) {
                    String mSendNumber;
                    if (mUseSavedNumber) {
                        mSendNumber = mPhoneNumber;
                    } else {
                        mSendNumber = mPhoneNumberView.getText().toString().trim();
                    }
                    mApiClient.sendAdvice(mSendNumber, mAdviceView.getText().toString(), mLabelResult, new DialogResponseHandler(this, getResources().getString(R.string.me_advice_submitting)) {
                        @Override
                        public void onSuccess(String response) {
                            ToastHelper.showGreenToast(AdviceActivity.this, R.string.me_advice_success);
                            finish();
                        }
                    });
                }
                break;
        }
    }

    //验证是否能够提交
    private boolean canSubmit() {
        if (mUseSavedNumber) {
            return checkAdvice() && getLabel();
        } else {
            return FormatCheck.checkPhoneNumber(mPhoneNumberView.getText().toString(), this) == FormatCheck.CHECK_SUCCESS && checkAdvice() && getLabel();
        }
    }

    //验证输入的意见
    private boolean checkAdvice() {
        if (!TextUtils.isEmpty(mAdviceView.getText().toString().trim())) {
            return true;
        } else {
            ToastHelper.showYellowToast(this, R.string.me_advice_hint);
            return false;
        }
    }

    //        获取意见类型标签
    private boolean getLabel() {
        if (mLabelResult == NO_LABEL) {
            ToastHelper.showYellowToast(this, R.string.me_advice_no_label);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        showLength(s);
    }

    private void showLength(CharSequence s) {
        mAdviceViewNum.setText(s.length() + "/200");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean dispatchTouchEvent(@Nullable MotionEvent ev) {

        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

}
