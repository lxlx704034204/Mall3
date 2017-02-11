package com.hxqc.autonews.view;

/**
 * Author:李烽
 * Date:2016-10-13
 * FIXME
 * Todo 缓存数据回调
 */

public interface ICacheDataView<T> {

    void onCacheDataBack(T t);

    void onCacheDataNull();

}
