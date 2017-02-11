package com.hxqc.mall.core.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.DebugLog;


/**
 * @Author : 钟学东
 * @Since : 2015-12-02
 * FIXME
 * Todo 监听y值的scrollview
 */
public class MonitorScrollView extends ScrollView {


    private int lastScrollY;
    private Context context;
    //颜色全变的高度
    private int height;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;


    public interface ScrollViewListener {
        void onScrollChange(float f1);

        //向下滑到一定程度
        void moveDown();

        //没有向下滑到一定程度
        void moveUp();
    }

    public interface onImageZoomListener{
        //图片放大
        void magnifyImage(int distance);
        //图片回弹
        void replyImage();
    }

    private ScrollViewListener scrollViewListener = null;

    @Deprecated
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public void setScrollViewListener(Toolbar mToolbar, ScrollViewListener scrollViewListener) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.data;
        mToolbar.setBackgroundColor(color);
        mToolbar.getBackground().setAlpha(0);
        this.scrollViewListener = scrollViewListener;
    }


    private onImageZoomListener ImageZoomListener = null;

    public void  setOnImageZoomListener(onImageZoomListener imageZoomListener){
        this.ImageZoomListener = imageZoomListener;
    }

    public MonitorScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public MonitorScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int scrollY = MonitorScrollView.this.getScrollY();

            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (scrollViewListener != null) {
                scrollViewListener.onScrollChange(changeColor(lastScrollY));
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
                if(ImageZoomListener != null){
                    // 手指离开后恢复图片
                    mScaling = false;
                    ImageZoomListener.replyImage();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (scrollViewListener != null) {
                    lastScrollY = this.getScrollY();
                    scrollViewListener.onScrollChange(changeColor(lastScrollY));
                }

                if (!mScaling) {
                    if (getScrollY() == 0) {
                        mFirstPosition =ev.getY();// 滚动到顶部时记录位置，否则正常返回
                    } else {
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                if (distance < 0) { // 如果当前位置比记录位置要小，正常返回
                    break;
                }
                if(ImageZoomListener != null){
                    DebugLog.i("Tag", " MonitorScrollView :  " +distance);
                    mScaling = true;
                    ImageZoomListener.magnifyImage(distance);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    //设置颜色全变的高度
    public void setHeight(int height){
        this.height = height;
    }

    private float changeColor(int y) {

        float currentY = DisplayTools.dip2px(context, y);
        float totalHeight = DisplayTools.dip2px(context, height);
        float f;
//        DebugLog.i("TAG","totalHeight :"+totalHeight);
//        DebugLog.i("TAG","currentY :"+currentY);

        f = currentY / totalHeight;

//        int r = 255;
//        int g = 0;
//        int b = 0;
        if (f >= 1) {
            f = 1;
        }else if(f < 0){
            f = 0 ;
        }
        DebugLog.i("TAG", "f :" + f);
        return f;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t > DisplayTools.getScreenHeight(getContext()) / 2) {
            scrollViewListener.moveDown();
        } else {
            scrollViewListener.moveUp();
        }
    }
}
