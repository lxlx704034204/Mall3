package com.hxqc.mall.paymethodlibrary.view.input;


import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.hxqc.mall.paymethodlibrary.R;


/**
 * Author: wanghao
 * Date: 2016-03-15
 * FIXME  支付键盘 控制类
 * Todo
 */
public class KeyboardUtil {
    private KeyboardView keyboardView;
    private EditText ed;

    public KeyboardUtil(KeyboardView kbView, Context context, EditText editText) {
        this.keyboardView = kbView;
        this.ed = editText;
        Keyboard symbols = new Keyboard(context, R.xml.balance_keyboard);

        keyboardView.setKeyboard(symbols);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == 1001) {

            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };


    // 显示键盘
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public boolean isKeyboardShow() {
        return keyboardView.getVisibility() == View.VISIBLE;
    }

    // 隐藏键盘
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }


}
