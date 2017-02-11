package com.hxqc.mall.core.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Author:李烽
 * Date:2016-03-17
 * FIXME
 * Todo
 */
public abstract class TextCompleteListener implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        onCompleteText(s);
    }

    public abstract void onCompleteText(Editable s);
}
