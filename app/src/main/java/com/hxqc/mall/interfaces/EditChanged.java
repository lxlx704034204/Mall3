package com.hxqc.mall.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Author: HuJunJie
 * Date: 2015-03-31
 * FIXME
 * Todo 输入监听
 */
public abstract class EditChanged implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onEditChange(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public abstract void onEditChange(CharSequence s, int start, int before, int count);
}
