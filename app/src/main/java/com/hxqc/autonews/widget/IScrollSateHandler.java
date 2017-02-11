package com.hxqc.autonews.widget;

/**
 * Author:李烽
 * Date:2016-10-13
 * FIXME
 * Todo  滚动状态监控，主要为获取汽车资讯顶部滑动显示和底部下拉刷新的冲突
 */

public interface IScrollSateHandler {
    void onNestedPreScroll();

    void onNestedScroll();
}
