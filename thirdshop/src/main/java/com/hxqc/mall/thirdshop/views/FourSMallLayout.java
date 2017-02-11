package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DebugLog;

/**
 * 说明:4s首页支持头部悬停的layout
 *
 * @author: 吕飞
 * @since: 2016-11-17
 * Copyright:恒信汽车电子商务有限公司
 */

public class FourSMallLayout extends LinearLayout implements NestedScrollingParent {
    OnNestScrollListener onNestScrollListener;
    FrameLayout mFragmentContainerView;
    LinearLayout mMallMenuView;
    LinearLayout mTopView;
    OverScroller mScroller;
    int mTopViewHeight;
    public FourSMallLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mFragmentContainerView.getLayoutParams();
        params.height = getMeasuredHeight() - mMallMenuView.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTopView.getMeasuredHeight() + mMallMenuView.getMeasuredHeight() + mFragmentContainerView.getMeasuredHeight());
    }

    public interface OnNestScrollListener {
        void onNestedPreScroll();

        void onNestedScroll();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = (LinearLayout) findViewById(R.id.top);
        mMallMenuView = (LinearLayout) findViewById(R.id.mall_menu);
        mFragmentContainerView = (FrameLayout) findViewById(R.id.fragment_container);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
        if (hiddenTop) {
            if (onNestScrollListener != null) {
                DebugLog.d(getClass().getSimpleName(), "onNestedScroll");
                onNestScrollListener.onNestedScroll();
            }
        }
        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= mTopViewHeight) return false;
        fling((int) velocityY);
        return true;
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (getScrollY() == 0) {
            if (onNestScrollListener != null) {
                onNestScrollListener.onNestedPreScroll();
            }
        }
        return true;
    }

    public void setOnNestScrollListener(OnNestScrollListener onNestScrollListener) {
        this.onNestScrollListener = onNestScrollListener;
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }
    @Override
    public void onStopNestedScroll(View target) {
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }
}
