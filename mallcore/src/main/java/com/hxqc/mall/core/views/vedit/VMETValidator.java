package com.hxqc.mall.core.views.vedit;

import android.support.annotation.NonNull;

/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo
 */

public abstract class VMETValidator {
    /**
     * 错误时显示的错误信息
     */
    private String errorMessage;

    public VMETValidator(@NonNull String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(@NonNull String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @NonNull
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * @param text   输入的信息
     * @param isEmpty   信息是否为空
     * @return True if valid, false if not
     */
    public abstract boolean isValid(@NonNull CharSequence text, boolean isEmpty);
}
