package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;

import java.util.regex.Pattern;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 03
 * FIXME
 * Todo 车牌号校验规则
 */

public class VMallPlateNumber extends VMETValidator {

    private Pattern pattern;

    public VMallPlateNumber() {
        super("请输入正确的车牌号");
        String check = "^[A-Z]{1}[A-Z_0-9]{5}$";
        pattern = Pattern.compile(check);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty && pattern.matcher(text).matches();
    }
}
