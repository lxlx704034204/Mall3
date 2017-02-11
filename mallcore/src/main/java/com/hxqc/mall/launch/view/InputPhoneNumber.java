package com.hxqc.mall.launch.view;

import android.content.Context;
import android.text.Editable;
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
 * 说明:手机号输入栏
 *
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class InputPhoneNumber extends RelativeLayout {
    public MaterialEditText mPhoneNumberView;//手机号
    ImageView mClearView;//清空手机号栏
    ImageView mPhoneNumberIconView;//手机号图标

    public InputPhoneNumber(Context context) {
        super(context);
    }

    public InputPhoneNumber(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_input_phone_number, this);
        mPhoneNumberView = (MaterialEditText) findViewById(R.id.phone_number);
        mPhoneNumberIconView = (ImageView) findViewById(R.id.phone_number_icon);
        mClearView = (ImageView) findViewById(R.id.clear);
//        if (context instanceof LoginActivity) {
//            mClearView.setVisibility(GONE);
//        } else {
//
//        }
        mClearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumberView.setText("");
            }
        });
        mPhoneNumberView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(mPhoneNumberView.getText())) {
                        mClearView.setVisibility(INVISIBLE);
                    } else {
//                        if (!(context instanceof LoginActivity)) {
                            mClearView.setVisibility(VISIBLE);
//                        }
                    }
                    mPhoneNumberIconView.setImageResource(R.drawable.ic_form_input_phone_selected);
                } else {
                    mClearView.setVisibility(INVISIBLE);
                    mPhoneNumberIconView.setImageResource(R.drawable.ic_form_input_phone_normal);
                }
            }
        });
        mPhoneNumberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mPhoneNumberView.getText()) && mPhoneNumberView.hasFocus()) {
//                    if (!(context instanceof LoginActivity)) {
                        mClearView.setVisibility(VISIBLE);
//                    }
                } else {
                    mClearView.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String getPhoneNumber() {
        return mPhoneNumberView.getText().toString().trim();
    }
}
