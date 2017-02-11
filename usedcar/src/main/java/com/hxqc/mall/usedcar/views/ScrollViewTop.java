package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.hxqc.util.DisplayTools;

/**
 * 分页滚动第一页
 * Created by huangyi on 15/11/10.
 */
public class ScrollViewTop extends ScrollView {

    int mScreenHeight, mActionBarHeight, mBottomViewHeight;
    int t, startX, startY;

    int lastScrollY, height;
    OnScrollListener mOnScrollListener;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int scrollY = ScrollViewTop.this.getScrollY();
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollChange(changeColor(lastScrollY));
            }
        }
    };

    public ScrollViewTop(Context context) {
        this(context, null);
    }

    public ScrollViewTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    /** 初始化ActionBar的高度 ScrollViewContainer自动调用 **/
    public void initActionBarHeight(int px) {
        this.mActionBarHeight = px;
    }

    /** 初始化BottomView的高度 ScrollViewContainer自动调用 **/
    public void initBottomViewHeight(int px) {
        this.mBottomViewHeight = px;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }

    public void setOnScrollListener(Toolbar mToolbar, OnScrollListener scrollViewListener) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.hxqc.mall.core.R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.data;
        mToolbar.setBackgroundColor(color);
        mToolbar.getBackground().setAlpha(0);
        this.mOnScrollListener = scrollViewListener;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指按下的时候 获得滑动事件 也就是让ScrollViewContainer失去滑动事件
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                //并且记录X Y点值
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算差值
                int disX = (int) ev.getX() - startX;
                int disY = (int) ev.getY() - startY;
                if(Math.abs(disX) > Math.abs(disY)) {
                    //横着 传递给子view HorizontalScrollView
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mOnScrollListener != null) {
                    lastScrollY = this.getScrollY();
                    mOnScrollListener.onScrollChange(changeColor(lastScrollY));
                }

                //在滑动的时候获得当前值 并计算得到YS 用来判断是向上滑动还是向下滑动
                float Ys = ev.getY() - startY;
                //得到scrollview里面空间的高度
                int childHeight = this.getChildAt(0).getMeasuredHeight();
                //子控件高度减去scrollview向上滑动的距离
                int padding = childHeight - t;
                //Ys<0表示手指正在向上滑动 padding==mScreenHeight表示本scrollview已经滑动到了底部
//                if (Ys < 0 && padding == mScreenHeight - mActionBarHeight - mBottomViewHeight) {
                if (Ys < 0 && padding == mScreenHeight - mBottomViewHeight) {
                    //让ScrollViewContainer重新获得滑动事件
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);

                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /** 禁掉滚轮事件 **/
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //t表示本scrollview向上滑动的距离
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private float changeColor(int y) {
        float currentY = DisplayTools.dip2px(getContext(), y);
        float totalHeight = DisplayTools.dip2px(getContext(), height);
        float f;
        f = currentY / totalHeight;
        if (f >= 1) {
            f = 1;
        } else if (f < 0) {
            f = 0;
        }
        return f;
    }

    public interface OnScrollListener {
        void onScrollChange(float f1);
    }

}
