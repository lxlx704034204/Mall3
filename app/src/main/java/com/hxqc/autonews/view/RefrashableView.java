package com.hxqc.autonews.view;


/**
 * Author:李烽
 * Date:2016-09-29
 * FIXME
 * Todo 下来刷新视图回调
 */

public interface RefrashableView<T> extends IView<T> {
    void onMoreDataResponse(T data);
}
