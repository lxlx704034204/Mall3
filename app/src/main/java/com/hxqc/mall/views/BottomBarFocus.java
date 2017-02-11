package com.hxqc.mall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import hxqc.mall.R;


/**
 * Author: HuJunJie
 * Date: 2015-04-14
 * FIXME
 * Todo
 */
public class BottomBarFocus extends LinearLayout {
    CheckBox mCheckBox;

    public BottomBarFocus(Context context) {
        super(context);
    }

    public BottomBarFocus(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_bottom_bar_focus, this);
        mCheckBox = (CheckBox) findViewById(R.id.focus_check);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public boolean getCheckStatus() {
        return mCheckBox.isChecked();
    }

    public void setCheckStatus(boolean isFocus) {
        mCheckBox.setChecked(isFocus);
        if (isFocus) {
            mCheckBox.setText("已关注");
        } else {
            mCheckBox.setText("关注");
        }
    }
}
