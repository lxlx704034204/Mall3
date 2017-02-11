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
 * 说明:验证码输入栏，没有获取验证码按钮
 *
 * author: 吕飞
 * since: 2015-03-12
 * Copyright:恒信汽车电子商务有限公司
 */
public class InputIdentifyingCodeNoButton extends RelativeLayout {
    public MaterialEditText mIdentifyingCodeView;//验证码
    ImageView mIdentifyingCodeIconView;//验证码图标
    ImageView mClearView;//删除图标

    public InputIdentifyingCodeNoButton(Context context) {
        super(context);
    }

    public InputIdentifyingCodeNoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_input_identifying_code_no_button, this);
        mClearView = (ImageView) findViewById(R.id.clear);
        mIdentifyingCodeView = (MaterialEditText) findViewById(R.id.identifying_code);
        mIdentifyingCodeIconView = (ImageView) findViewById(R.id.identifying_code_icon);
        mIdentifyingCodeView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(mIdentifyingCodeView.getText())) {
                        mClearView.setVisibility(INVISIBLE);
                    } else {
                        mClearView.setVisibility(VISIBLE);
                    }
                    mIdentifyingCodeIconView.setImageResource(R.drawable.ic_form_input_code_selected);
                } else {
                    mIdentifyingCodeIconView.setImageResource(R.drawable.ic_form_input_code_normal);
                }
            }
        });
        mClearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIdentifyingCodeView.setText("");
            }
        });
        mIdentifyingCodeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mIdentifyingCodeView.getText()) && mIdentifyingCodeView.hasFocus()) {
                    mClearView.setVisibility(VISIBLE);
                } else {
                    mClearView.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String getIdentifyingCode() {
        return mIdentifyingCodeView.getText().toString();
    }


    public void setIdentifyingCodeViewType(int inputtype){
         mIdentifyingCodeView.setInputType(inputtype);
    }
}
