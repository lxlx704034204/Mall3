package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DebugLog;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function:自定义CoordinateLayout
 *
 * @author 袁秉勇
 * @since 2015年12月25日
 */
public class MyCoordinatorLayout extends CoordinatorLayout {
    private final static String TAG = MyCoordinatorLayout.class.getSimpleName();
    private boolean interceptMove = false;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private int appBarlayout_right;
    private int appBarlayout_bottom;
    private int recyclerView_top;
    private int recyclerView_right;
    private int recyclerView_bottom;
    private LinearLayout mTipView;
    private boolean relayout = false;

    public MyCoordinatorLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.post(new Runnable() {
            @Override
            public void run() {
                appBarLayout = (AppBarLayout) MyCoordinatorLayout.this.getChildAt(0);
                recyclerView = (RecyclerView) MyCoordinatorLayout.this.getChildAt(1);
                mTipView = (LinearLayout) findViewById(R.id.tip_view);
                relayout = true;

                appBarlayout_right = appBarLayout.getRight();
                appBarlayout_bottom = appBarLayout.getBottom();

                recyclerView_top = recyclerView.getTop();
                recyclerView_right = recyclerView.getRight();
                recyclerView_bottom = recyclerView.getBottom();

                DebugLog.e(TAG, appBarlayout_right + " " + appBarlayout_bottom + " " + recyclerView_top + " " + recyclerView_right + " " + recyclerView_bottom);
                DebugLog.e(TAG, " ---------------> this is post function <-------------------");
            }
        });
    }

    /**
     * 设置是否阻止滑动
     *
     * @param interceptMove
     */
    public void setInterceptMove(boolean interceptMove) {
        this.interceptMove = interceptMove;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (((PtrFrameLayout) this.getParent()).isRefreshing()) {
            ((PtrFrameLayout) this.getParent()).refreshComplete();
        }

        if (interceptMove) {
            if (this.isPointInChildBounds(((ViewGroup) this.getChildAt(0)).getChildAt(0), (int) ev.getRawX(), (int) ev.getY())) {
                return super.onInterceptTouchEvent(ev);
            } else {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (relayout && recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() <= 0 && appBarLayout.getBottom() == 0) {
            if (mTipView.getVisibility() == View.VISIBLE) mTipView.setVisibility(GONE);
            DebugLog.e(TAG, " 自定义布局  ---------" + appBarlayout_right + " " + appBarlayout_bottom + "  " + recyclerView_top + "  " + recyclerView_bottom);
            appBarLayout.layout(0, 0, appBarlayout_right, appBarlayout_bottom);
            recyclerView.layout(0, recyclerView_top, recyclerView_right, recyclerView_bottom);
            DebugLog.d(TAG, " ---------------- " + appBarLayout.getBottom());
        }
    }

    @Override
    public void onLayoutChild(View child, int layoutDirection) {
        DebugLog.e(TAG, " onLayoutChild ---------- ");
        super.onLayoutChild(child, layoutDirection);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    public void onDraw(Canvas c) {
        DebugLog.e(TAG, " onDraw");
        DebugLog.e(TAG, this.getChildAt(0).getTop() + "　" + this.getChildAt(0).getBottom() + " \n" + this.getChildAt(1).getTop() + " " + this.getChildAt(1).getBottom());
        super.onDraw(c);
    }
}
