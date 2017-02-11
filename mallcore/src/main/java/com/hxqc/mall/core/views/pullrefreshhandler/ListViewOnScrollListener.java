package com.hxqc.mall.core.views.pullrefreshhandler;

import android.widget.AbsListView;

import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;

/**
 * Author: 胡俊杰
 * Date: 2015-07-14
 * FIXME
 * Todo
 */
public class ListViewOnScrollListener implements AbsListView.OnScrollListener {
    private UltraPullRefreshHeaderHelper ultraPullRefreshHeaderHelper;

    public ListViewOnScrollListener(UltraPullRefreshHeaderHelper ultraPullRefreshHeaderHelper) {
        super();
        this.ultraPullRefreshHeaderHelper = ultraPullRefreshHeaderHelper;
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
            if (ultraPullRefreshHeaderHelper.onRefreshHandler != null && ultraPullRefreshHeaderHelper.isHasMore()) {
                ultraPullRefreshHeaderHelper.onRefreshHandler.onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
