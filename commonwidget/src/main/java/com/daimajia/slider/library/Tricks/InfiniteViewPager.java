package com.daimajia.slider.library.Tricks;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around effect. Should be used with an {@link
 * InfinitePagerAdapter}.
 */
public class InfiniteViewPager extends ViewPagerEx {
//    private ViewGroup parent;

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public void setNestParent(ViewGroup parent) {
//        this.parent = parent;
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (parent != null) {
//            switch (ev.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    parent.requestDisallowInterceptTouchEvent(true);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    break;
//                case MotionEvent.ACTION_UP:
//                    parent.requestDisallowInterceptTouchEvent(false);
//                    break;
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

}