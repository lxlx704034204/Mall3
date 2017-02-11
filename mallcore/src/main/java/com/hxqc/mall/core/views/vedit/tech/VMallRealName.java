package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;

import java.util.regex.Pattern;

/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo 真实姓名验证
 */

public class VMallRealName  extends VMETValidator {
    private Pattern pattern;

    public VMallRealName() {
        super("请输入2-12个字，只能由中文组成");
        String telRegex = "^[\\u4e00-\\u9fa5]{2,12}";
        pattern = Pattern.compile(telRegex);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty && pattern.matcher(text).matches();
    }
}
