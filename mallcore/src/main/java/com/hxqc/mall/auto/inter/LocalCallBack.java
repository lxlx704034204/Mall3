package com.hxqc.mall.auto.inter;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 23
 * FIXME
 * Todo
 */
public interface LocalCallBack<T> {

    void onSuccess(T response);

    void onFailed(T response);
}
