package com.hxqc.mall.core.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * 说明:提交内容对话框
 *
 * author: 吕飞
 * since: 2015-03-25
 * Copyright:恒信汽车电子商务有限公司
 */
public class SubmitDialog extends Dialog {
    public TextView mTextView;

    public SubmitDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_submit);
        setCancelable(false);
        mTextView = (TextView) findViewById(R.id.text);
    }

    public SubmitDialog(Context context,String text) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_submit);
        setCancelable(false);
        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText(text);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }
}
