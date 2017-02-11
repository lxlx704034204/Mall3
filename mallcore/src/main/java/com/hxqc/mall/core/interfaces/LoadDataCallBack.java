package com.hxqc.mall.core.interfaces;

/**
 * Author:李烽
 * Date:2016-03-17
 * FIXME
 * Todo
 */
public interface LoadDataCallBack<T> {
    void onDataNull(String message);

    void onDataGot(T obj);
}
