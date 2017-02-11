package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;

import java.util.regex.Pattern;

/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo
 */

public class VMallEmail extends VMETValidator {

    private Pattern pattern;

    public VMallEmail() {
        super("请输入正确的邮箱");
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        pattern = Pattern.compile(check);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty && pattern.matcher(text).matches();
    }
}
