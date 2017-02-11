package com.hxqc.mall.launch.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.hxqc.mall.core.R;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;

/**
 * 说明:密码输入栏
 *
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class InputPassword extends RelativeLayout {
    MaterialEditText mPasswordView;//密码
    ImageView mShowView;//控制是否显示密码
    ImageView mClearView;//控制清楚
    boolean mShowPassword = false;//是否显示密码
    ImageView mPasswordIconView;//密码图标
    public boolean mIsRegisterActivity;

    public InputPassword(Context context) {
        super(context);
    }

    public InputPassword(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_input_password, this);
        mPasswordView = (MaterialEditText) findViewById(R.id.password);
        mPasswordIconView = (ImageView) findViewById(R.id.password_icon);
        mShowView = (ImageView) findViewById(R.id.show);
        mClearView = (ImageView) findViewById(R.id.clear);
//        if (context instanceof LoginActivity) {
//            mShowView.setVisibility(GONE);
//        } else {
//
//        }
        mShowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowPassword = !mShowPassword;
                initPasswordStatus();
                mPasswordView.setSelection(mPasswordView.length());
            }
        });
        mClearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordView.setText("");
            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mPasswordView.getText()) && mPasswordView.hasFocus()) {
                    mClearView.setVisibility(VISIBLE);
                    mShowView.setVisibility(View.VISIBLE);
                    if (mIsRegisterActivity) {
                        mPasswordView.setFloatingLabelText(context.getResources().getString(R.string.me_password_hint_bt));
                    }
                } else {
                    if (mIsRegisterActivity) {
                        mPasswordView.setFloatingLabelText(context.getResources().getString(R.string.me_password_hint));
                    }
                    mClearView.setVisibility(INVISIBLE);
                    mShowView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPasswordView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mIsRegisterActivity && mPasswordView.length() > 0) {
                        mPasswordView.setFloatingLabelText(context.getResources().getString(R.string.me_password_hint_bt));
                    }
                    mPasswordIconView.setImageResource(R.drawable.ic_form_input_password_selected);
                    mShowView.setVisibility(View.VISIBLE);
                    mClearView.setVisibility(View.VISIBLE);
                } else {
                    if (mIsRegisterActivity && mPasswordView.length() == 0) {
                        mPasswordView.setFloatingLabelText(context.getResources().getString(R.string.me_password_hint));
                    }
                    mPasswordIconView.setImageResource(R.drawable.ic_form_input_password_normal);
                    mShowView.setVisibility(View.INVISIBLE);
                    mClearView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 显示密码状态：显示或者不显示
     */
    private void initPasswordStatus() {
        if (mShowPassword) {
            mShowView.setImageResource(R.drawable.eye_select);
            mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            mShowView.setImageResource(R.drawable.eye);
            mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void setHint(int resId) {
        mPasswordView.setHint(resId);
    }

    public String getPassword() {
        return mPasswordView.getText().toString().trim();
    }

    public MaterialEditText getEditText() {
        return mPasswordView;
    }
}
