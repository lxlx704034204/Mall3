package com.hxqc.mall.core.views;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月21日
 */
public class MyRecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
    OnRefreshHandler onRefreshHandler;

    public MyRecyclerViewOnScrollListener(OnRefreshHandler onRefreshHandler) {
        super();
        this.onRefreshHandler = onRefreshHandler;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && isScollBottom(recyclerView)) {
            if (onRefreshHandler != null && onRefreshHandler.hasMore()) {
                onRefreshHandler.onLoadMore();
            }
        }
    }

    private boolean isScollBottom(RecyclerView recyclerView) {
        return !isCanScollVertically(recyclerView);
    }

    private boolean isCanScollVertically(RecyclerView recyclerView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
        } else {
            return ViewCompat.canScrollVertically(recyclerView, 1);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }
}
