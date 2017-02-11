package com.hxqc.mall.core.views.materialedittext.pwd;

import android.annotation.TargetApi;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.paymethodlibrary.view.input.KeyboardUtil;

/**
 * Author: wanghao
 * Date: 2016-04-01
 * FIXME   支付键盘
 * Todo
 */
public class MaterialPWDEditText extends MaterialEditText{

    KeyboardUtil keyboardUtil;
    KeyboardView kbv;
    int viewHeight = 0;

    public MaterialPWDEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setKeyboard(KeyboardView keyboardView) {
        this.kbv = keyboardView;
        setShowSoftInputOnFocus(false);
    }

    public void hideKeyboard() {
        if (keyboardUtil != null) {
            keyboardUtil.hideKeyboard();
            keyboardUtil = null;
        }
    }

    public boolean isKeyboardShow() {
        return keyboardUtil != null && keyboardUtil.isKeyboardShow();
    }

    public int getViewHeight(){
        return this.viewHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (kbv != null) {
            keyboardUtil = new KeyboardUtil(kbv, getContext(), MaterialPWDEditText.this);
            keyboardUtil.showKeyboard();
            this.viewHeight = kbv.getHeight();
        }

        return super.onTouchEvent(event);
    }

}
