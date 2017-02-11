package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;
import com.hxqc.util.IdCardUtils;

/**
 * Author:  wh
 * Date:  2016/11/8
 * FIXME
 * Todo
 */

public class VMallID extends VMETValidator{

    public VMallID() {
        super("请输入正确的身份证号码");
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty && IdCardUtils.validateIdCard18(text.toString());
    }
}
