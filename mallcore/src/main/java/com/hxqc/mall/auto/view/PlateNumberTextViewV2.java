package com.hxqc.mall.auto.view;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

/**
 * Author :胡仲俊
 * Date : 2016-03-28
 * FIXME
 * Todo 车牌输入控件 默认字母输入框,setMode(0)城市输入框
 */
@Deprecated
public class PlateNumberTextViewV2 extends AutoCompleteTextView implements View.OnFocusChangeListener, KeyboardView.OnKeyboardActionListener {

    public static final String TAG = "PlateNumberTextViewV2";
    private Context mContext;

    public static final int MODE_CITY = 0;
    public static final int MODE_NUM = 1;


    public PlateNumberTextViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setInputType(InputType.TYPE_NULL);
        setOnFocusChangeListener(this);
    }

    //初始化keyboard
    private void initKeyboard(Activity act) {
        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(this);
    }

    private KeyboardView keyboardView;
    private Keyboard k1;// 城市键盘
    private Keyboard k2;// 字母键盘
    private int mode;

    //设置键盘模式
    public void setMode(Activity act, int i) {
        if (i == 0) {
            if (k1 == null) {
                k1 = new Keyboard(mContext, R.xml.licence_city);
            }
            initKeyboard(act);
            keyboardView.setKeyboard(k1);
            hideKeyboard();
            showKeyboard();
        } else if (i == 1) {
            if (k2 == null) {
                k2 = new Keyboard(mContext, R.xml.licence_num);
            }
            initKeyboard(act);
            keyboardView.setKeyboard(k2);
            hideKeyboard();
            showKeyboard();
        }
        mode = i;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        DebugLog.i(TAG, hasFocus + "");
        if (hasFocus) {
            if (null != keyboardView) {
//                hideDKeyboard();
                showKeyboard();
            }
        } else {
            DebugLog.i(TAG, "hide");
            if (null != keyboardView) {
                hideKeyboard();
            }
        }
    }

    public void hideDKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
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

    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = this.getText();
        int start = this.getSelectionStart();
        if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
            hideKeyboard();
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else {
            if (mode == 0) {
                if (start > 0) {
//                    DebugLog.i(TAG, "replace");
                    editable.replace(0, 1, Character.toString((char) primaryCode));
                } else if (start == 0) {
//                    DebugLog.i(TAG, "insert");
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            } else if (mode == 1) {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    }

    //显示键盘
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    //隐藏键盘
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

    //unicode转换
    public String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }


}
