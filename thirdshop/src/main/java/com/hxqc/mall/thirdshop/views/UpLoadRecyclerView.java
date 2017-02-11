package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Author:李烽
 * Date:2016-05-06
 * FIXME
 * Todo 上拉加载更多的 RecyclerView
 */
public class UpLoadRecyclerView extends RecyclerView {

    public UpLoadRecyclerView(Context context) {
        this(context, null);
    }

    public UpLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.addOnScrollListener(new OnScrollListener() {
            boolean isSlidingToLast;
            LinearLayoutManager layoutManager;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //底部上拉
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    //当不滚动的时候
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                        int itemCount = layoutManager.getItemCount();
                        if (lastCompletelyVisibleItemPosition == (itemCount - 1) && isSlidingToLast) {
                            if (onLoadMoreCallBack != null)
                                onLoadMoreCallBack.onLoadMore();
                        }
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    Log.e("ClassCastException", "layoutManager 必须是LinearLayoutManager");
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
    }

    public void setOnLoadMoreCallBack(OnLoadMoreCallBack onLoadMoreCallBack) {
        this.onLoadMoreCallBack = onLoadMoreCallBack;
    }

    private OnLoadMoreCallBack onLoadMoreCallBack;

    public interface OnLoadMoreCallBack {

        void onLoadMore();

    }
}
