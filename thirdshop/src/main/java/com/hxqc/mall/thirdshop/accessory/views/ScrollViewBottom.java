package com.hxqc.mall.thirdshop.accessory.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 分页滚动第二页
 * Created by huangyi on 15/11/10.
 */
public class ScrollViewBottom extends WebView {
    public float oldY;
    private int t;

    public ScrollViewBottom(Context context) {
        super(context);
    }

    public ScrollViewBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true); //ScrollViewContainer不拦截
                oldY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float Ys = Y - oldY;
                if (Ys > 0 && t == 0) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false); //ScrollViewContainer拦截
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true); //ScrollViewContainer不拦截
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 禁掉滚轮事件
     **/
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
