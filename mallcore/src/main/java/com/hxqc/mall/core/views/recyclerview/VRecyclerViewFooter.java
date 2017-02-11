package com.hxqc.mall.core.views.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Author:李烽
 * Date:2016-04-15
 * FIXME
 * Todo 上拉加载更多的footer
 */
public abstract class VRecyclerViewFooter extends LinearLayout {
    protected View root_layout;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_NO_MORE = 3;


    public abstract int getViewHeight();


    public VRecyclerViewFooter(Context context) {
        this(context, null);
    }

    public VRecyclerViewFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VRecyclerViewFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract View getRootLayout();


    public void setState(int state) {
        switch (state) {
            case STATE_NORMAL:
                onStateNormal();
                break;
            case STATE_READY:
                onStateReady();
                break;
            case STATE_LOADING:
                onStateLoading();
                break;
            case STATE_NO_MORE:
                onStateNoMore();
                break;
            default:
                break;
        }
    }

    protected abstract void onStateNoMore();

    protected abstract void onStateLoading();

    protected abstract void onStateReady();

    protected abstract void onStateNormal();

    public void setItemHeight(int height) {
        root_layout = getRootLayout();
        LayoutParams layoutParams = (LayoutParams) root_layout.getLayoutParams();
        layoutParams.height = height;
        root_layout.setLayoutParams(layoutParams);
    }

    public int getItemHeight() {
        root_layout = getRootLayout();
        LayoutParams layoutParams = (LayoutParams) root_layout.getLayoutParams();
        return layoutParams.height;
    }
}
