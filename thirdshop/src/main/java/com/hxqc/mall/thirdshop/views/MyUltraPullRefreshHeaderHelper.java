package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hxqc.mall.core.views.pullrefreshhandler.ListViewOnScrollListener;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DebugLog;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月21日
 */
public class MyUltraPullRefreshHeaderHelper extends UltraPullRefreshHeaderHelper implements PtrHandler {
    private final static String TAG = MyUltraPullRefreshHeaderHelper.class.getSimpleName();
    PtrFrameLayout ptrFrameLayout;
    OnRefreshHandler onRefreshHandler;
    LinearLayout mTipView;
    MyRecyclerViewOnScrollListener myRecyclerViewOnScrollListener;
    int recyclerTop = 0;
    boolean hasMore = false;
    private Context mContext;

    public MyUltraPullRefreshHeaderHelper(Context context, PtrFrameLayout refreshFrameLayout, MyRecyclerViewOnScrollListener myRecyclerViewOnScrollListener) {
        super(context, refreshFrameLayout);
        DebugLog.e(TAG, ((MyCoordinatorLayout) refreshFrameLayout.findViewById(R.id.coordinatorlayout)).getChildAt(0).getBottom() + " ");
        this.myRecyclerViewOnScrollListener = myRecyclerViewOnScrollListener;
        setBottomScrollListener();
    }

    /** 设置可以下拉的条件 */
    public void setPullCondition(int pullCondition) {
        this.recyclerTop = pullCondition;
    }

    /**
     * 准备下拉刷新头
     */
    public void preparePullRefreshHeader(Context context, PtrFrameLayout refreshFrameLayout) {
        this.mContext = context;
        this.ptrFrameLayout = refreshFrameLayout;
        refreshFrameLayout.setDurationToCloseHeader(1000);
        refreshFrameLayout.setHeaderView(new TopSearchView(context));
        refreshFrameLayout.setPtrHandler(this);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
        View controlView = content.findViewById(R.id.recycler_view);
        if (mTipView == null) mTipView = (LinearLayout) content.findViewById(R.id.tip_view);
        if (mTipView.getVisibility() == View.VISIBLE) mTipView.setVisibility(View.GONE);
        return controlView.getTop() >= recyclerTop && PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, controlView, header);
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
        View view = ((ViewGroup)ptrFrameLayout.getContentView()).getChildAt(1);
        DebugLog.e(getClass().getName(), view.toString());
        if (view instanceof ListView) {
            ((ListView) view).setOnScrollListener(new ListViewOnScrollListener(this));
        } else if (view instanceof RecyclerView) {
            DebugLog.e(getClass().getName(), "Yes I do");
            ((RecyclerView) view).setOnScrollListener(this.myRecyclerViewOnScrollListener);
        }
    }

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
