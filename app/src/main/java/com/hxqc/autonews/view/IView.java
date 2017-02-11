package com.hxqc.autonews.view;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 数据获取视图回调
 */
public interface IView<T> {
    void onDataBack(T data);

    void onDataNull(String message);
}
