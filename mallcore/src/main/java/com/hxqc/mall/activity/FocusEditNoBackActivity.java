package com.hxqc.mall.activity;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hxqc.mall.auto.view.PlateNumberTextView;
import com.hxqc.util.DebugLog;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 14
 * FIXME
 * Todo 屏幕触摸空白出的键盘操作
 */
public class FocusEditNoBackActivity extends NoBackActivity {

    private static final String TAG = "FocusEditBackActivity";

    //空白处隐藏系统键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (v != null && (v instanceof PlateNumberTextView)) {
                DebugLog.i(TAG, "PlateNumberTextView111");
                int[] leftTop = {0, 0};
                //获取输入框当前的location位置
                v.getLocationInWindow(leftTop);
                int left = leftTop[0];
                int top = leftTop[1];
                int bottom = top + v.getHeight();
                int right = left + v.getWidth();
                if (ev.getX() > left && ev.getX() < right
                        && ev.getY() > top && ev.getY() < bottom) {
                    // 点击EditText的事件，忽略它。
                    DebugLog.i(TAG, "isPN");
                } else {
                    DebugLog.i(TAG, "noPN");
                }
            }


            if (isShouldHideInput(v, ev)) {
                if (v instanceof PlateNumberTextView) {
                    DebugLog.i(TAG, "PlateNumberTextView");
//                    v.setFocusable(true);
//                    v.setFocusableInTouchMode(true);
//                    v.requestFocus();
                    ((PlateNumberTextView) v).dismissPopup();
                } else {
                    DebugLog.i(TAG, "EditText");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            DebugLog.i(TAG, "isEditText");
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选
        return false;
    }
}
