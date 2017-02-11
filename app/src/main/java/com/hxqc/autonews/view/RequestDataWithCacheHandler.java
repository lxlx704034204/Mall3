package com.hxqc.autonews.view;

/**
 * Author:李烽
 * Date:2016-10-14
 * FIXME
 * Todo 请求网络数据（带缓存）接口
 */

public interface RequestDataWithCacheHandler<T> extends ICacheDataView<T> {
    void onDataNull(String msg);

    void onDataResponse(T obj);
}
