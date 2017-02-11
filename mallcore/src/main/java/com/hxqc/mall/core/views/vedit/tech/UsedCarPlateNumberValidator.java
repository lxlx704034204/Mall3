package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;

import java.util.regex.Pattern;

/**
 * Author:吕飞
 * Date: 2016 - 12 - 20
 * FIXME
 * Todo 车牌验证 可以为空
 */

public class UsedCarPlateNumberValidator extends VMETValidator {

    private Pattern mPattern;

    public UsedCarPlateNumberValidator(@NonNull String errorMessage, @NonNull String regex) {
        super(errorMessage);
        mPattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return isEmpty || text.length() >= 5 && mPattern.matcher(text).matches();
    }
}
