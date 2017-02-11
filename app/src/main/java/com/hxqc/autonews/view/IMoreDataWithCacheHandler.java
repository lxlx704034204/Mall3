package com.hxqc.autonews.view;

/**
 * Author:李烽
 * Date:2016-10-14
 * FIXME
 * Todo 加载更多数据回调（带缓存查询）
 */

public interface IMoreDataWithCacheHandler <T> extends IMoreDataHandler<T>{
    void onMoreCacheDataBack(T t);
    void onMoreCacheDataNull(String msg);
}
