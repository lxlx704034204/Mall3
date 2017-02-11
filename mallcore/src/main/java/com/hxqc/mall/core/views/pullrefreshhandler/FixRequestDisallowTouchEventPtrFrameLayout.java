package com.hxqc.mall.core.views.pullrefreshhandler;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hxqc.util.DebugLog;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author:李烽
 * Date:2016-09-27
 * FIXME
 * Todo  PtrFrameLayout 在实现dispatchTouchEvent方法时，
 * 没有考虑到FLAG_DISALLOW_INTERCEPT的因素，导致requestDisallowInterceptTouchEvent方法不起作用
 * 所以对其进行改造
 */

public class FixRequestDisallowTouchEventPtrFrameLayout extends PtrFrameLayout {
    private boolean disallowInterceptTouchEvent = false;

    public FixRequestDisallowTouchEventPtrFrameLayout(Context context) {
        super(context);
    }

    public FixRequestDisallowTouchEventPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixRequestDisallowTouchEventPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (mStatus == PTR_STATUS_INIT) {
            DebugLog.i(getClass().getSimpleName(), "改变disallowInterceptTouchEvent：" + disallowIntercept);
            disallowInterceptTouchEvent = disallowIntercept;
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (disallowInterceptTouchEvent) {
            DebugLog.i(getClass().getSimpleName(), "让父容器分发事件了");
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }
}
