package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hxqc.util.DisplayTools;

/**
 * 分页滚动
 * Created by huangyi on 15/11/10.
 */
public class ScrollViewContainer extends ScrollView {
    float mDensity;
    int mScreenHeight; //屏幕的高度
    int mStatusBarHeight; //屏幕最顶端状态栏的高度

    ScrollViewTop mTopView;
    int mActionBarHeight;
    int mBottomViewHeight;
    int mRadioGroupHeight;

    boolean isMeasured;
    boolean isTopView = true;

    public ScrollViewContainer(Context context) {
        this(context, null);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mStatusBarHeight = DisplayTools.getStatusBarHeight();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (! isMeasured) {
            //得到里面的控件
            LinearLayout parent = (LinearLayout) getChildAt(0);
            mTopView = (ScrollViewTop) parent.getChildAt(0);
            LinearLayout mBottomView = (LinearLayout) parent.getChildAt(1);

            mTopView.initActionBarHeight(mActionBarHeight);
            mTopView.initBottomViewHeight(mBottomViewHeight);
//            mTopView.getLayoutParams().height = mScreenHeight - mActionBarHeight - mBottomViewHeight; //TopView重置高
            mTopView.getLayoutParams().height = mScreenHeight - mBottomViewHeight; //TopView重置高
            mBottomView.getLayoutParams().height = mScreenHeight - mStatusBarHeight - mActionBarHeight - mBottomViewHeight; //BottomView重置高
            mBottomView.getChildAt(0).getLayoutParams().height = mScreenHeight - mStatusBarHeight - mActionBarHeight - mRadioGroupHeight - mBottomViewHeight; //webview parent重置高
            isMeasured = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
        }
    }

    /** 初始化ActionBar的高度 如果有ActionBar必须调用 **/
    public void initActionBarHeight(int dp) {
        if(dp > 0)
            this.mActionBarHeight = (int) (dp * mDensity + 0.5);
    }

    /** 初始化RadioGroup的高度 如果有RadioGroup必须调用 **/
    public void initRadioGroupHeight(int dp) {
        if(dp > 0)
            this.mRadioGroupHeight = (int) (dp * mDensity + 0.5);
    }

    /** 初始化BottomView的高度 如果有BottomView必须调用 **/
    public void initBottomViewHeight(int dp) {
        if(dp > 0)
            this.mBottomViewHeight = (int) (dp * mDensity + 0.5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollY = getScrollY();
                int minScroll = mScreenHeight / 3; //最小滑动距离 394
                if (isTopView) { //在顶部
                    if (scrollY <= minScroll - mStatusBarHeight - mActionBarHeight - mBottomViewHeight) {
                        //回到顶部
                        this.smoothScrollTo(0, 0);
                    } else {
                        //滑到底部
                        this.smoothScrollTo(0, mScreenHeight);
                        this.setFocusable(false);
                        isTopView = false;
                    }
                } else { //在底部
                    int scrollpadding = mScreenHeight - scrollY; //scrollpadding 未滑动时初始值 mActionBarHeight + mBottomViewHeight
                    if (scrollpadding >= minScroll) {
                        this.smoothScrollTo(0, 0);
                        isTopView = true;
                    } else {
                        this.smoothScrollTo(0, mScreenHeight);
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /** TopView显示 **/
    public void toTop() {
        if(isTopView) {
            mTopView.smoothScrollTo(0, 0);
        }else {
            this.smoothScrollTo(0, 0);
            mTopView.smoothScrollTo(0, 0);
            isTopView = true;
        }
    }

}