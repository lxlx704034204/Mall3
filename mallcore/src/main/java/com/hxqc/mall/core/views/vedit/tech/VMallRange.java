package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;

/**
 * Author:  吕飞
 * Date:  2016/12/12
 * FIXME
 * Todo 里程验证
 */

public class VMallRange extends VMETValidator {
    public VMallRange() {
        super("请提供50万公里及以下的车辆");
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty && Float.parseFloat(text.toString()) < 50;
    }
}
