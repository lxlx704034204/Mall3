package com.hxqc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * 不滑动Listview
 *
 * author 胡俊杰
 */
public class ListViewNoSlide extends ListView {
    public ListViewNoSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewNoSlide(Context context) {
        super(context);
        setOverScrollMode(GridView.OVER_SCROLL_NEVER);
    }

    public ListViewNoSlide(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(GridView.OVER_SCROLL_NEVER);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
