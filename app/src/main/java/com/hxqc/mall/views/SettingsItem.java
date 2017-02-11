package com.hxqc.mall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hxqc.mall.R;


/**
 * 说明:设置界面部分item
 *
 * author: 吕飞
 * since: 2015-03-10
 * Copyright:恒信汽车电子商务有限公司
 */
public class SettingsItem extends RelativeLayout {
    public TextView mRightTextView;
    public ProgressBar mProgressBarView;
    TextView mLeftTextView;
    ImageView mLeftIconView;

    public SettingsItem(Context context) {
        super(context);
    }

    public SettingsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_item_settings, this);
        mLeftTextView = (TextView) findViewById(R.id.left_text);
        mLeftIconView = (ImageView) findViewById(R.id.left_icon);
        mRightTextView = (TextView) findViewById(R.id.right_text);
        mProgressBarView = (ProgressBar) findViewById(R.id.progress_bar);
    }

    public void setLeftText(int resId) {
        mLeftTextView.setText(resId);
    }

    public void setLeftIcon(int resId) {
        mLeftIconView.setImageResource(resId);
    }

    public void setRightText(String text, int resId) {
        mRightTextView.setText(text);
        mRightTextView.setTextColor(getResources().getColor(resId));
    }
}
