package com.hxqc.mall.reactnative.nativeui.diyscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.MeasureSpecAssertions;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactClippingViewGroup;
import com.facebook.react.views.view.ReactClippingViewGroupHelper;
import com.hxqc.mall.reactnative.manager.HomeJumpManager;
import com.hxqc.util.DebugLog;

import javax.annotation.Nullable;

/**
 * Author: wanghao
 * Date: 2016-03-02
 * FIXME
 * Todo
 */
public class StretchScrollView extends ScrollView implements ReactClippingViewGroup {

    public String Tag_js = "js_scroll";

    private static final int MSG_REST_POSITION = 0x01;

    /**
     * The max scroll height.
     */
    private static final int MAX_SCROLL_HEIGHT = 800;
    /**
     * Damping, the smaller the greater the resistance
     */
    private static final float SCROLL_RATIO = 0.8f;

    private View mChildRootView;

    private float mTouchY;
    private boolean mTouchStop = false;

    private int mScrollY = 0;
    private int mScrollDy = 0;
    private float mFirstDownDy = 0;//第一次按下记下的位置
    private float tempYPoint = 0;//移动的y距离
    private boolean isFirstDown = true;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (MSG_REST_POSITION == msg.what) {

//                DebugLog.i("handle_js", "handleMessage  mScrollY: "+ mScrollY);

                if (mScrollY != 0 && mTouchStop) {
                    mScrollY -= mScrollDy;

                    if ((mScrollDy < 0 && mScrollY > 0) || (mScrollDy > 0 && mScrollY < 0)) {
                        mScrollY = 0;
                    }

                    mChildRootView.scrollTo(0, mScrollY);


                    // continue scroll after 20ms
                    mHandler.sendEmptyMessageDelayed(MSG_REST_POSITION, 20);
                }
            }
            return false;
        }
    });


    private boolean mRemoveClippedSubviews;
    private
    @Nullable
    Rect mClippingRect;
    private boolean mSendMomentumEvents;
    private boolean mDragging;
    private boolean mFlinging;
    private boolean mDoneFlinging;

    //记录当前 按下的y坐标
    private float mDownY = 0;


    public StretchScrollView(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (getChildCount() > 0)
            mChildRootView = getChildAt(0);

        MeasureSpecAssertions.assertExplicitMeasureSpec(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Call with the present values in order to re-layout if necessary
        scrollTo(getScrollX(), getScrollY());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mRemoveClippedSubviews) {
            updateClippingRect();
        }
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        DebugLog.d(Tag_js, "x: " + x + " y: " + y + " oldX: " + oldX + " oldY: " + oldY);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        doTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchY = ev.getY();
            DebugLog.i("test_sv", "mTouchY: " + mTouchY + " child Count: " + getChildCount());
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private void doTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mFirstDownDy = mDownY;
                DebugLog.e("test_sv", "doTouchEvent 正在移动 ACTION_DOWN: " + mDownY);
                break;

            case MotionEvent.ACTION_UP:
                isFirstDown = true;
//                DebugLog.e("test_sv", "doTouchEvent 正在移动 ACTION_UP: " + ev.getY());
//                onReceiveNativeEvent("show", 0);
                if (isEstimateCanClick(ev.getY())) {
                    HomeJumpManager.getInstance().touchUp();
                }

                mScrollY = mChildRootView.getScrollY();
                if (mScrollY != 0) {
                    mTouchStop = true;
                    mScrollDy = (int) (mScrollY / 10.0f);
                    mHandler.sendEmptyMessage(MSG_REST_POSITION);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
//                DebugLog.e("test_sv", "ACTION_MOVE  mScrollDy: " + mScrollDy);
//                verdifyIsUpOrDown(nowY);
                int deltaY = (int) (mTouchY - nowY);
                mTouchY = nowY;
                if (isNeedMove()) {
                    int offset = mChildRootView.getScrollY();
                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        mChildRootView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));
                        mTouchStop = false;
                    }
                }
                break;

            default:
                break;
        }
    }

    //判断点击是否可跳转
    private boolean isEstimateCanClick(float upY) {
        try {
            boolean isCanJump = false;
            float abs = Math.abs(upY - mDownY);
            isCanJump = abs < 20;
            return isCanJump;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNeedMove() {
        int viewHeight = mChildRootView.getMeasuredHeight();
        int scrollHeight = getHeight();
        int offset = viewHeight - scrollHeight;
        DebugLog.i("handle_js", "isNeedMove  offset: "+ offset);
        int scrollY = getScrollY();

        return scrollY == 0 || scrollY == offset;
    }

    @Override
    public void updateClippingRect() {
        if (!mRemoveClippedSubviews) {
            return;
        }

        Assertions.assertNotNull(mClippingRect);

        ReactClippingViewGroupHelper.calculateClippingRect(this, mClippingRect);
        View contentView = getChildAt(0);
        if (contentView instanceof ReactClippingViewGroup) {
            ((ReactClippingViewGroup) contentView).updateClippingRect();
        }

    }

    @Override
    public void getClippingRect(Rect outClippingRect) {
        outClippingRect.set(Assertions.assertNotNull(mClippingRect));
    }

    @Override
    public boolean getRemoveClippedSubviews() {
        return mRemoveClippedSubviews;
    }

    @Override
    public void setRemoveClippedSubviews(boolean removeClippedSubviews) {
        if (removeClippedSubviews && mClippingRect == null) {
            mClippingRect = new Rect();
        }
        mRemoveClippedSubviews = removeClippedSubviews;
        updateClippingRect();
    }

    /**
     * 判断是向上还是向下滑y
     */
    public void verdifyIsUpOrDown(float nowY) {
        double alpha = 1;
        float moveDY = 0;
        if (isFirstDown) {
            isFirstDown = false;
            tempYPoint = mFirstDownDy;
        } else {
            moveDY = nowY - mFirstDownDy;
            //下滑   隐藏
            if (nowY - tempYPoint > 0) {
                if (moveDY > 0) {
                    if (moveDY < 40) {
                        alpha = 1 - moveDY / 40;
                        onReceiveNativeEvent("hide", alpha);
                    }
                }
            }//上滑  显示
            else {


            }
            tempYPoint = nowY;
        }
    }

    public void onReceiveNativeEvent(String isHide, double yPoint) {
        DebugLog.i(Tag_js, "onReceiveNativeEvent  isHide : " + isHide + " yPoint: " + yPoint);
        WritableMap event = Arguments.createMap();
        event.putString("scrollMessage", isHide);
        event.putDouble("nativeScrollY", yPoint);
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "topChange",
                event);
    }
}
