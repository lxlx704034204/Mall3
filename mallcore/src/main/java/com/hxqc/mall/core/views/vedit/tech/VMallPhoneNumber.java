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

public class VMallPhoneNumber extends VMETValidator {
    private Pattern pattern;

    public VMallPhoneNumber() {
        super("请输入正确的电话号码");
        String regexStr_phone = "^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$";
        pattern = Pattern.compile(regexStr_phone);
    }

    public VMallPhoneNumber(String errorMsg) {
        super(errorMsg);
        String regexStr_phone = "^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$";
        pattern = Pattern.compile(regexStr_phone);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty && pattern.matcher(text).matches();
    }
}
