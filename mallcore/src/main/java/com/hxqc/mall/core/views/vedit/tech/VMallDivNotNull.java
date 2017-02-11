package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.hxqc.mall.core.views.vedit.VMETValidator;

import java.util.regex.Pattern;

/**
 * Author:  wh
 * Date:  2016/11/4
 * FIXME
 * Todo
 */

public class VMallDivNotNull extends VMETValidator {

    private String regStr;

    public VMallDivNotNull(@NonNull String errorMessage, @NonNull String regex) {
        super(TextUtils.isEmpty(errorMessage) ? "当前项不能为空" : errorMessage);
        this.regStr = regex;
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        if (!TextUtils.isEmpty(regStr)) {
            Pattern pattern = Pattern.compile(regStr);
            boolean matches = pattern.matcher(text).matches();
            Log.i("Validator", "内容是否验证 正确： " + matches);
        }
        return !isEmpty;
    }
}
