package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;

/**
 * 说明:二手车备注信息
 *
 * @author: 吕飞
 * @since: 2016-05-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class RemarkView extends LinearLayout {
    protected String mTitle;
    protected TextView mTitleView;
    protected EditText mRemarkView;
    protected TextView mCountView;

    public RemarkView(Context context) {
        super(context);
        initView();
    }

    public RemarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RemarkView);
        mTitle = typedArray.getString(R.styleable.RemarkView_remarkTitle);
        typedArray.recycle();
        initView();
        mTitleView.setText(mTitle);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_remark, this);
        mRemarkView = (EditText) findViewById(R.id.remark);
        mTitleView = (TextView) findViewById(R.id.title);
        mCountView = (TextView) findViewById(R.id.count);
        mRemarkView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCountView.setText(charSequence.length()+"/500");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public EditText getRemarkView() {
        return mRemarkView;
    }
    public void setRemarkText(String remarkText) {
        mRemarkView.setText(remarkText);
    }
}
