package com.hxqc.mall.auto.util;

/**
 * Author :liukechong
 * Date : 2016-02-18
 * FIXME
 * Todo 输入键盘工具
 */

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hxqc.mall.core.R;
@Deprecated
public class KeyboardUtil implements OnKeyboardActionListener {
    private static KeyboardUtil keyboardUtil;
    private Activity act;
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private EditText ed;

    public KeyboardUtil(Activity act) {
        this.act = act;
        k1 = new Keyboard(act, R.xml.symbols_city);
        k2 = new Keyboard(act, R.xml.symbols);
        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);
    }




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
        Editable editable = ed.getText();
        editable.replace(0, 1, text);
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
        if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成
            hideKeyboard();
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else {
            editable.insert(start, Character.toString((char) primaryCode));
        }
    }


    public void showKeyboard(int mode,EditText edit) {
        this.ed = edit;
        edit.setInputType(InputType.TYPE_NULL);
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

        if (mode == 1) {
            keyboardView.setKeyboard(k1);
        } else {
            keyboardView.setKeyboard(k2);
        }
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

}
