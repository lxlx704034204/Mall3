package com.hxqc.mall.auto.inter;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 16
 * FIXME
 * Todo
 */
public interface CallBack<T> {

    void onSuccess(T response);

    void onFailed(boolean offLine);
}
