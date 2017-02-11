package com.hxqc.autonews.view;

/**
 * Author:李烽
 * Date:2016-10-14
 * FIXME
 * Todo 加载更多数据回调
 */

public interface IMoreDataHandler<T>{
    void onMoreDataBack(T t);
    void onMoreDataNull(String msg);
}
