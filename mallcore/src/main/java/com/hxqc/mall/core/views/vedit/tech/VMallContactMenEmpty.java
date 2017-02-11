package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;

/**
 * Author:  wh
 * Date:  2016/11/4
 * FIXME
 * Todo  联系人不能为空
 */

public class VMallContactMenEmpty extends VMETValidator {

    public VMallContactMenEmpty() {
        super("请输入联系人姓名");
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty;
    }
}
