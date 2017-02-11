package com.hxqc.mall.core.views.pullrefreshhandler;


/**
 * Author: 胡俊杰
 * Date: 2015-07-14
 * FIXME
 * Todo
 */
public interface OnRefreshHandler {
    boolean hasMore();

    void onRefresh();

    void onLoadMore();

}
