package com.hxqc.mall.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Function: 类似书籍目录提示的View
 *
 * author yby
 * since 2015年10月12日
 */
public class CatalogTipView extends RelativeLayout {
    private TextView mLeftTextView;
    private TextView mRightTextView;

    public CatalogTipView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_catalogtip, this);
        mLeftTextView = (TextView) findViewById(R.id.left_text_view);
        mRightTextView = (TextView) findViewById(R.id.right_text_view);

    }

    public CatalogTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_catalogtip, this);
        mLeftTextView = (TextView) findViewById(R.id.left_text_view);
        mRightTextView = (TextView) findViewById(R.id.right_text_view);
    }

    public TextView getLeftTextView() {
        return mLeftTextView;
    }

    public TextView getRightTextView() {
        return mRightTextView;
    }

    public void setLeftTextView(String str) {
        mLeftTextView.setText(str);
    }

    public void setLeftTextView(String str, int size) {
        mLeftTextView.setText(str);
        mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) size);
    }

    public void setLeftText(String str, int size, int colorRes) {
        mLeftTextView.setText(str);
        mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) size);
        mLeftTextView.setTextColor(colorRes);
    }

    public void setRightTextView(String str) {
        mRightTextView.setText(str);
    }

    public void setRightTextView(String str, int size) {
        mRightTextView.setText(str);
        mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) size);
    }

    public void setRightTextView(String str, int size, int colorRes) {
        mRightTextView.setText(str);
        mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) size);
        mRightTextView.setTextColor(colorRes);
    }

    public void setLeftTextView(String leftString, String rightString) {
        mLeftTextView.setText(leftString);
        mRightTextView.setText(rightString);
    }

    public void setRightTextView(TextView mRightTextView) {

    }
}
