package com.hxqc.mall.core.views.dialog;

import android.content.Context;
import android.view.View;

/**
 * 说明:没有取消的对话框
 *
 * @author: 吕飞
 * @since: 2015-11-04
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class NoCancelDialog extends NormalDialog {
    public NoCancelDialog(Context context) {
        super(context);
        mCancelView.setVisibility(View.GONE);
    }

    public NoCancelDialog(Context context, String title, String content) {
        super(context, title, content);
        mCancelView.setVisibility(View.GONE);
    }

    public NoCancelDialog(Context context, String title, String content, String okString) {
        super(context, title, content);
        mOkView.setText(okString);
        mCancelView.setVisibility(View.GONE);
    }

    public NoCancelDialog(Context context, String content) {
        super(context, content);
        mCancelView.setVisibility(View.GONE);
    }

    public NoCancelDialog(Context context, String title, View contentView) {
        super(context, title, contentView);
        mCancelView.setVisibility(View.GONE);
    }

    public void setTitleText(String titleText) {
        mTitleView.setText(titleText);
    }
}
