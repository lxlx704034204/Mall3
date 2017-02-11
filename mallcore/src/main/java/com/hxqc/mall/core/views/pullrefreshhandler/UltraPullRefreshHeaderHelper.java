package com.hxqc.mall.core.views.pullrefreshhandler;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.hxqc.mall.core.R;
import com.hxqc.util.LocalDisplay;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Author: 胡俊杰
 * Date: 2015-07-10
 * FIXME
 * Todo
 */
public class UltraPullRefreshHeaderHelper implements PtrHandler {
    PtrFrameLayout ptrFrameLayout;
    OnRefreshHandler onRefreshHandler;

    public UltraPullRefreshHeaderHelper(Context context, PtrFrameLayout refreshFrameLayout) {
        preparePullRefreshHeader(context, refreshFrameLayout);
    }
    public UltraPullRefreshHeaderHelper(Context context, PtrFrameLayout refreshFrameLayout,OnRefreshHandler onRefreshHandler) {
        setOnRefreshHandler(onRefreshHandler);
        preparePullRefreshHeader(context, refreshFrameLayout);
    }



    /**
     * 准备下拉刷新头
     */
    public void preparePullRefreshHeader(Context context, PtrFrameLayout refreshFrameLayout) {
        this.ptrFrameLayout = refreshFrameLayout;
        StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, LocalDisplay.dp2px(-100), 0, 0);
//        header.setScale(0.25f);
        header.initWithStringArray(R.array.hxqc_frame_head);
        header.setTextColor(Color.parseColor("#7d7d7d"));
        refreshFrameLayout.setDurationToCloseHeader(1000);
        refreshFrameLayout.setHeaderView(header);
        refreshFrameLayout.addPtrUIHandler(header);
        refreshFrameLayout.setPtrHandler(this);
        setBottomScrollListener();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        if (onRefreshHandler != null) {
            onRefreshHandler.onRefresh();
        }
    }

    /**
     * 关闭刷新
     */
    public void refreshComplete(final PtrFrameLayout ptrFrameLayout) {
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.refreshComplete();
            }
        }, 100);
    }


    /**
     * 添加滚动监听
     */
    void setBottomScrollListener() {
        View view = ptrFrameLayout.getContentView();
        if (view instanceof ListView) {
            ((ListView) view).setOnScrollListener(new ListViewOnScrollListener(this));
        } else if (view instanceof RecyclerView) {
            ((RecyclerView) view).setOnScrollListener(new RecyclerViewOnScrollListener(this));
        }
    }

    boolean hasMore = false;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setOnRefreshHandler(OnRefreshHandler onRefreshHandler) {
        this.onRefreshHandler = onRefreshHandler;
    }


    public void onFailure() {

    }
}
