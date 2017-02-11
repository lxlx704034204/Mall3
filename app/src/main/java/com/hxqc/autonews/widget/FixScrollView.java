package com.hxqc.autonews.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * Author:李烽
 * Date:2016-09-29
 * FIXME
 * Todo  Scrollview 嵌套 RecyclerView 及在Android 5.1版本滑动时 惯性消失问题
 */
@Deprecated
public class FixScrollView extends NestedScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    private ScrollViewListener scrollViewListener = null;

    public FixScrollView(Context context) {
        this(context, null);
    }

    public FixScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

//    public void setDisallowintercept(boolean disallowintercept) {
//        this.disallowintercept = disallowintercept;
//    }

//    private boolean disallowintercept = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewGroup parent = (ViewGroup) getParent();
        float scaleY = getScaleY();
        if (parent != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (scaleY == 0) {
                        parent.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    parent.requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop)
                    return true;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
