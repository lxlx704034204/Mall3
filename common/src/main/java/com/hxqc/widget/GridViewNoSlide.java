package com.hxqc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不滑动gridView
 *
 * author 胡俊杰
 */
public class GridViewNoSlide extends GridView {
    public GridViewNoSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewNoSlide(Context context) {
        super(context);
        setOverScrollMode(GridView.OVER_SCROLL_NEVER);
    }

    public GridViewNoSlide(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(GridView.OVER_SCROLL_NEVER);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
