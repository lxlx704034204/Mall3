package com.hxqc.mall.core.views.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Author:李烽
 * Date:2016-04-15
 * FIXME
 * Todo 带上拉加载更多的RecyclerView
 */
public class VRecyclerView extends RecyclerView {

    private static final String TAG = "VRecyclerView";
    private static final int PULL_LOAD_MORE_DELTA = 300;
    // 记录按下点的y坐标
    // 用来回滚
    private VRecyclerViewFooter mFooter;
    private boolean readyLoadingMore = false;
    private boolean enableLoadingMore = true;
    private boolean isLoadingMore = false;

    private Scroller mScroller;

    public VRecyclerView(Context context) {
        this(context, null);
    }

    public VRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(getContext());
    }


    public VRecyclerViewFooter getFooter() {
        try {
            mFooter = ((VRecyclerViewAdapter) getAdapter()).getVRecyclerViewFooter();
        } catch (Exception e) {
            Log.e("TypeErrorException", "Footer must be VRecyclerViewFooter");
        }
        return mFooter;
    }


    private float lastY = 0;
    float height = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mFooter = getFooter();
        if (!isLoadingMore && mFooter != null) {
            try {
                LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        mFooter.show();
                        height = 0;
                        if (enableLoadingMore)
                            mFooter.setState(VRecyclerViewFooter.STATE_NORMAL);
                        lastY = e.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float y = lastY - e.getRawY();//确定方向的值大于0是down
                        lastY = e.getRawY();
                        int visible = layoutManager.getChildCount();
                        int total = layoutManager.getItemCount();
                        int past = layoutManager.findFirstCompletelyVisibleItemPosition();
                        if ((visible + past) > total && y > 0) {
                            height = height + y;
                            mFooter.setItemHeight((int) height);
                            Log.d(TAG, "height:" + height + "mFooter.getItemHeight:" + mFooter.getItemHeight());
                            if (mFooter.getItemHeight() >= PULL_LOAD_MORE_DELTA && enableLoadingMore) {
                                mFooter.setState(VRecyclerViewFooter.STATE_READY);
                                readyLoadingMore = true;
                            }
                        } else if ((visible + past) > total && y < 0) {
                            height = height + y;
                            mFooter.setItemHeight((int) height);
                            Log.d(TAG, "height:" + height + "mFooter.getItemHeight:" + mFooter.getItemHeight());
                            if (mFooter.getItemHeight() <= PULL_LOAD_MORE_DELTA && enableLoadingMore) {
                                mFooter.setState(VRecyclerViewFooter.STATE_NORMAL);
                                readyLoadingMore = false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (readyLoadingMore) {
                            int dimensionPixelOffset = mFooter.getViewHeight();
                            float h = height - dimensionPixelOffset;
                            Log.d(TAG, "h:" + h);
                            mScroller.startScroll(0, 0, 0, -(int) h, 1000);
                            int visible1 = layoutManager.getChildCount();
                            int total1 = layoutManager.getItemCount();
                            int past1 = layoutManager.findFirstCompletelyVisibleItemPosition();
                            if ((visible1 + past1) > total1 && enableLoadingMore) {
                                loadMore();
                            }
                        } else {
                            mScroller.startScroll(0, 0, 0, -(int) height, 1000);
                            invalidate();
                        }

                        break;
                }
//                Log.e(TAG, mFooter.getState() + "");
            } catch (Exception ex) {
                Log.e("TypeErrorException", "layoutManager must be LinearLayoutManager");
            }
        }
        return super.onTouchEvent(e);
    }


    public void loadAllDataOk() {
        mFooter = getFooter();
        enableLoadingMore = false;
//        mFooter.show();
        mFooter.setState(VRecyclerViewFooter.STATE_NO_MORE);
    }
public void refreshDataState(){
    mFooter = getFooter();
    enableLoadingMore = true;
    mFooter.setState(VRecyclerViewFooter.STATE_NORMAL);

}
    @Override
    public void computeScroll() {
        mFooter = getFooter();
        if (mScroller.computeScrollOffset()) {
            mFooter.setItemHeight((int) (height + mScroller.getCurrY()));
            Log.d("TAG", "true" + mScroller.getCurrY() + "/mFooter:" + mFooter.getItemHeight());
            postInvalidate();
        } else if (!mScroller.computeScrollOffset()) {
            Log.d("TAG", "false" + mScroller.getCurrY() + "/mFooter:" + mFooter.getItemHeight());
        }
    }

    /**
     * 进入加载状态
     */
    private void loadMore() {
        mFooter = getFooter();
        if (vRecyclerViewLoader != null)
            vRecyclerViewLoader.loadMore();
        mFooter.setState(VRecyclerViewFooter.STATE_LOADING);
        readyLoadingMore = false;
        isLoadingMore = true;
    }

    public void loadComplete() {
        mFooter = getFooter();
        mFooter.setState(VRecyclerViewFooter.STATE_NORMAL);
        if (isLoadingMore) {
            isLoadingMore = false;
        }
    }

    private VRecyclerViewLoader vRecyclerViewLoader;

    public void addVRecyclerViewLoader(VRecyclerViewLoader vRecyclerViewLoader) {
        this.vRecyclerViewLoader = vRecyclerViewLoader;
    }

    public interface VRecyclerViewLoader {
        void loadMore();
    }

}
