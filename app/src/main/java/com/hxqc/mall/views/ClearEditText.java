package com.hxqc.mall.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.views.materialedittext.MaterialEditText;

import hxqc.mall.R;

/**
 * 说明:带叉叉的输入框
 *
 * author: 吕飞
 * since: 2015-09-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class ClearEditText extends RelativeLayout {
    public MaterialEditText mEditTextView;
    ImageView mClearView;

    public ClearEditText(Context context) {
        super(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_clear_edittext, this);
        mEditTextView = (MaterialEditText) findViewById(R.id.edit_text);
        mClearView = (ImageView) findViewById(R.id.clear);
        mClearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextView.setText("");
            }
        });
        mEditTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(mEditTextView.getText())) {
                        mClearView.setVisibility(INVISIBLE);
                    } else {
                        mClearView.setVisibility(VISIBLE);
                    }
                } else {
                    mClearView.setVisibility(INVISIBLE);
                }
            }
        });
        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mEditTextView.getText()) && mEditTextView.hasFocus()) {
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
}
