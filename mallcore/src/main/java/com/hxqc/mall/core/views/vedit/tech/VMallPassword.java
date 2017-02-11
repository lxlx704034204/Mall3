package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.core.views.vedit.VMETValidator;


/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo
 */

public class VMallPassword extends VMETValidator {

    public VMallPassword(@NonNull String errorMessage) {
        super(errorMessage);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return false;
    }
}
