package com.hxqc.mall.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Function: 自动滚到指定位置的RecyclerView
 *
 * @author yby
 * @since 2015年10月21日
 */
public class CustomRecyclerView extends RecyclerView {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private int scrollType = 1;           //  默认为非平滑移动
    public static int SMOOTHSCROLL = 0;     //  平滑移动
    public static int NORMALSCROLL = 1;     //  非平滑移动
    private boolean move = false;
    private int mIndex = 0;
    private int gravity;                //  0为水平布局，1为竖直布局

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initConfig();
    }

    private void initConfig() {
        mRecyclerView = this;
        mRecyclerView.setOnScrollListener(new RecyclerViewListener());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        this.mLinearLayoutManager = (LinearLayoutManager) layout;
        gravity = mLinearLayoutManager.getOrientation();
        super.setLayoutManager(layout);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        super.setAdapter(adapter);
    }


    /**
     * 设置滑动的类型
     * @param scrollType    SMOOTHSCROLL为平滑滚动，NORMALSCROLL为非平滑移动
     */
    public void setScrollType(int scrollType) {
        this.scrollType = scrollType;
    }

    /**
     *  recycleView移动到指定的item位置
     * @param n     移动到的position
     */
    public void move(final int n) {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (n < 0 || n >= mAdapter.getItemCount()) {
                    Toast.makeText(getContext(), "超出范围了", Toast.LENGTH_SHORT).show();
                    return;
                }
                mIndex = n;
                mRecyclerView.stopScroll();
                if (scrollType == SMOOTHSCROLL) {
                    smoothMoveToPosition(n);
                } else if (scrollType == NORMALSCROLL) {
                    moveToPosition(n);
                }
            }
        });
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            this.smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            if (gravity == LinearLayout.VERTICAL) {
                int top = this.getChildAt(n - firstItem).getTop();
                this.smoothScrollBy(0, top);
            } else if (gravity == LinearLayout.HORIZONTAL) {
                int left = this.getChildAt(n - firstItem).getLeft();
                this.smoothScrollBy(left, 0);
            }
        } else {
            this.smoothScrollToPosition(n);
            move = true;
            scrollBy(1, 1);
        }
    }

    private void moveToPosition(int n) {
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        Log.d(getClass().getName(), "n " + n + " , firsItem " + firstItem + " , lastItem " + lastItem + " , count " + mLinearLayoutManager.getChildCount());
        if (n <= firstItem) {
            Log.d(getClass().getName(), "---------------> 1");
            this.scrollToPosition(n);
        } else if (n <= lastItem) {
            Log.d(getClass().getName(), "---------------> 2");
            if (gravity == LinearLayout.VERTICAL) {
                int top = this.getChildAt(n - firstItem).getTop();
                this.scrollBy(0, top);
            } else if (gravity == LinearLayout.HORIZONTAL) {
                int left = this.getChildAt(n - firstItem).getLeft();
                this.smoothScrollBy(left, 0);
            }
        } else {
            Log.d(getClass().getName(), "---------------> 3");
            this.scrollToPosition(n);
            move = true;
            scrollBy(1, 1);
        }
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE && scrollType == SMOOTHSCROLL) {
                move = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    if (gravity == LinearLayout.VERTICAL) {
                        int top = mRecyclerView.getChildAt(n).getTop();
                        mRecyclerView.smoothScrollBy(0, top);
                    } else if (gravity == LinearLayout.HORIZONTAL) {
                        int left = mRecyclerView.getChildAt(n).getLeft();
                        mRecyclerView.smoothScrollBy(left, 0);
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.d(getClass().getName(), "-----------------------> 2222222222222");
            super.onScrolled(recyclerView, dx, dy);
            if (move && scrollType == NORMALSCROLL) {
                Log.d(getClass().getName(), "222222222222222222222222");
                move = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                Log.d(getClass().getName(), "---------> " + n + " , first " + mLinearLayoutManager.findFirstVisibleItemPosition() + " , childCount " + mRecyclerView.getChildCount());
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    Log.d(getClass().getName(), "hello1");
                    if (gravity == LinearLayout.VERTICAL) {
                        int top = mRecyclerView.getChildAt(n).getTop();
                        Log.d(getClass().getName(), "------------> " + top);
                        mRecyclerView.scrollBy(0, top);
                    }  else if (gravity == LinearLayout.HORIZONTAL) {
                        int left = mRecyclerView.getChildAt(n).getLeft();
                        mRecyclerView.scrollBy(left, 0);
                    }
                } else {
                    Log.d(getClass().getName(), "-------------> first " + mLinearLayoutManager.findFirstCompletelyVisibleItemPosition());
                    int m = mIndex - mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (0 <= m && m < mRecyclerView.getChildCount()) {
                        Log.d(getClass().getName(), "hello2");
                        if (gravity == LinearLayout.VERTICAL) {
                            int top = mRecyclerView.getChildAt(m).getTop();
                            Log.d(getClass().getName(), "------------> " + top);
                            mRecyclerView.scrollBy(0, top);
                        }  else if (gravity == LinearLayout.HORIZONTAL) {
                            int left = mRecyclerView.getChildAt(m).getLeft();
                            mRecyclerView.scrollBy(left, 0);
                        }
                    }
                }
            }
        }
    }
}
