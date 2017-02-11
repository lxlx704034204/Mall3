package com.hxqc.mall.core.interfaces;

/**
 * Author:李烽
 * Date:2016-10-12
 * FIXME
 * Todo
 */

public interface CacheDataListener<T> extends LoadDataCallBack<T> {
    void onCacheDataBack(T obj);

    void onCacheDataNull();
}
