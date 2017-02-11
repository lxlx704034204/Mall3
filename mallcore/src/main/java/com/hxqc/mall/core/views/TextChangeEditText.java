package com.hxqc.mall.core.views;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Author:  wh
 * Date:  2016/9/2
 * FIXME
 * Todo
 */
public class TextChangeEditText extends EditText implements TextWatcher{

    boolean isCanActive = true;
    OnCanActiveByTextChangeListener onCanActiveByTextChangeListener;
    int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

    public TextChangeEditText(Context context) {
        super(context);
        init();
    }

    public TextChangeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextChangeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setInputType(inputType);
        addTextChangedListener(this);
    }

    public void setOnCanActiveByTextChangeListener(OnCanActiveByTextChangeListener onCanActiveByTextChangeListener) {
        this.onCanActiveByTextChangeListener = onCanActiveByTextChangeListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isCanActive){
            Log.e("et_test", "afterTextChanged: 不触发");
            isCanActive = true;
            return;
        }

        if (onCanActiveByTextChangeListener!=null){
            Log.e("et_test", "afterTextChanged: 触发" + s);
            onCanActiveByTextChangeListener.onActive(s);
        }
    }

    public void setEditInfoText(String text){
        isCanActive = false;
        setText(text);
    }


    public interface OnCanActiveByTextChangeListener{
        public void onActive(Editable s);
    }

}
