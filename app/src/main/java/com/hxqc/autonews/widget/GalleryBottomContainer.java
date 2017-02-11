package com.hxqc.autonews.widget;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Author:李烽
 * Date:2016-09-28
 * FIXME
 * Todo
 */
@Deprecated
public class GalleryBottomContainer extends RelativeLayout {
    private ViewDragHelper mDragHelper;

    public GalleryBottomContainer(Context context) {
        this(context, null);
    }

    public GalleryBottomContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryBottomContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
