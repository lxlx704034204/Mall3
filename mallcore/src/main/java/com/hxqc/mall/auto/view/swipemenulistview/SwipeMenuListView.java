package com.hxqc.mall.auto.view.swipemenulistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hxqc.util.DebugLog;

import java.util.List;

/**
 * @author baoyz
 * @date 2014-8-18
 */
public class SwipeMenuListView extends ListView implements AbsListView.OnScrollListener, SwipeMenuLayout.onCloseListener {

    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;

    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = -1;
    private int mDirection = 1;//swipe from right to left by default

    private int MAX_Y = 5;
    private int MAX_X = 3;
    private float mDownX;
    private float mDownY;
    private int mTouchState;
    private int mTouchPosition;
    private SwipeMenuLayout mTouchView;
    private OnSwipeListener mOnSwipeListener;

    private SwipeMenuCreator mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnMenuStateChangeListener mOnMenuStateChangeListener;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;
    private int OpenPosition = 0;
    private ListAdapter mSwipeMenuAdapter;
    private int ActionDownPos = 0;
    private List<Integer> disallowOpenPos;
    private float firstX, firstY;

    public SwipeMenuListView(Context context) {
        super(context);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnScrollListener(this);
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        mSwipeMenuAdapter = new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) {
                if (mMenuCreator != null) {
                    mMenuCreator.create(menu);
                }
            }

            @Override
            public void onItemClick(final SwipeMenuView view, final SwipeMenu menu,
                                    final int index) {
                if (mTouchView != null) {
                    mTouchView.smoothCloseMenu();
                    if (mOnMenuStateChangeListener != null) {
                        mOnMenuStateChangeListener.onMenuClose(OpenPosition);
                    }
                }

                mTouchView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean flag = false;
                        if (mOnMenuItemClickListener != null) {
                            flag = mOnMenuItemClickListener.onMenuItemClick(
                                    view.getPosition(), menu, index);
                        }
                      /*  if (mTouchView != null && !flag) {
                            mTouchView.smoothCloseMenu();
                            mOnMenuStateChangeListener.onMenuClose(OpenPosition);
                        }*/
                    }
                }, SwipeMenuLayout.ANIM_DURING);

            }
        };
        super.setAdapter(mSwipeMenuAdapter);
    }


    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }


    private void slideClash(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            firstX = ev.getX();
            firstY = ev.getY();
            ((View) getParent()).setEnabled(false);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float lastX = ev.getX();
            float lastY = ev.getY();
            int degre = (int) ((Math.atan(Math.abs(firstY - lastY) / Math.abs(firstX - lastX))) * (180 / Math.PI));
            if (lastY - firstY > 0 && degre > 30) {
                ((View) getParent()).setEnabled(true);
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            ((View) getParent()).setEnabled(true);
        }
    }


    boolean handled;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //  slideClash(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            DebugLog.e("ACTION_DOWN", "ACTION_DOWN: ");
            firstX = ev.getX();
            firstY = ev.getY();
            ((View) getParent()).setEnabled(false);
            mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            ActionDownPos = mTouchPosition - getHeaderViewsCount();
            if (mTouchView != null && mTouchView.isOpen() && !inRangeOfView(mTouchView.getMenuView(), ev)) {
                mTouchView.smoothCloseMenu();
                if (mOnMenuStateChangeListener != null) {
                    mOnMenuStateChangeListener.onMenuClose(OpenPosition);
                }
                handled = true;
                return true;
            }
           /* if (ActionDownPos < 0) {
                handled = true;
                return true;
            }*/
     /*       View view = getChildAt(ActionDownPos >= 0 ? mTouchPosition - getFirstVisiblePosition() :
                    OpenPosition + getHeaderViewsCount() - getFirstVisiblePosition());
            //只在空的时候赋值 以免每次触摸都赋值，会有多个open状态
            if (view instanceof SwipeMenuLayout) {
                //如果有打开了 就拦截.
                if (mTouchView != null && mTouchView.isOpen() && !inRangeOfView(mTouchView.getMenuView(), ev)) {
                    mTouchView.smoothCloseMenu(); //不用点击另外的item 直接滑动即可关闭上一个item
                    if (mOnMenuStateChangeListener != null) {
                        mOnMenuStateChangeListener.onMenuClose(OpenPosition);
                    }
                    handled = true;
                    return true;
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.setOnCloseListener(this);
            }*/

        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            DebugLog.e("ACTION_MOVE", "ACTION_MOVE: " + handled);
            if (mTouchView != null) {
                if (handled)
                    return true;

                if (mTouchView.isSlide()) {
                    ((View) getParent()).setEnabled(false);
                } else {
                    float lastX = ev.getX();
                    float lastY = ev.getY();
                    int degre = (int) ((Math.atan(Math.abs(firstY - lastY) / Math.abs(firstX - lastX))) * (180 / Math.PI));
                    if (lastY - firstY > 0 && degre > 30) {
                        ((View) getParent()).setEnabled(true);
                    }
                }

            }

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            DebugLog.e("ACTION_UP", "ACTION_UP: " + handled);
            if (mTouchView != null) {
                if (handled) {
                    handled = false;
                    return true;
                }
            }
            ((View) getParent()).setEnabled(true);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //在拦截处处理，在滑动设置了点击事件的地方也能swip，点击时又不能影响原来的点击事件
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                boolean handled = super.onInterceptTouchEvent(ev);
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                ActionDownPos = mTouchPosition - getHeaderViewsCount();

                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                //只在空的时候赋值 以免每次触摸都赋值，会有多个open状态
                if (view instanceof SwipeMenuLayout) {
                    //如果有打开了 就拦截.
                    if (mTouchView != null && mTouchView.isOpen() && !inRangeOfView(mTouchView.getMenuView(), ev)) {
                        mTouchView.smoothCloseMenu(); //不用点击另外的item 直接滑动即可关闭上一个item
                        if (mOnMenuStateChangeListener != null) {
                            mOnMenuStateChangeListener.onMenuClose(OpenPosition);
                        }
                        if (disallowOpenCtrl())
                            return false;
                        return true;
                    }
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                    mTouchView.setOnCloseListener(this);

                    if (disallowOpenCtrl())
                        return false;
                }

                //如果摸在另外个view
                if (mTouchView != null && mTouchView.isOpen() && view != mTouchView) {
                    handled = true;
                }

                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                return handled;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (Math.abs(dy) > MAX_Y || Math.abs(dx) > MAX_X) {
                    //每次拦截的down都把触摸状态设置成了TOUCH_STATE_NONE 只有返回true才会走onTouchEvent 所以写在这里就够了
                    if (mTouchState == TOUCH_STATE_NONE) {
                        if (Math.abs(dy) > MAX_Y) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > MAX_X) {
                            mTouchState = TOUCH_STATE_X;
                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                            }
                        }
                    }
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
            return super.onTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;

                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

                if (mTouchPosition == oldPos && mTouchView != null
                        && mTouchView.isOpen()) {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);
                    return true;
                }

                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    // return super.onTouchEvent(ev);
                    // try to cancel the touch event
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    onTouchEvent(cancelEvent);
                    if (mOnMenuStateChangeListener != null) {
                        mOnMenuStateChangeListener.onMenuClose(OpenPosition);
                    }
                    return true;
                }
                if (view instanceof SwipeMenuLayout) {
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }
                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //有些可能有header,要减去header再判断
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY()) - getHeaderViewsCount();
                //如果滑动了一下没完全展现，就收回去，这时候mTouchView已经赋值，再滑动另外一个不可以swip的view
                //会导致mTouchView swip 。 所以要用位置判断是否滑动的是一个view
                if (!mTouchView.getSwipEnable() || mTouchPosition != mTouchView.getPosition()) {
                    break;
                }
                if (disallowOpenCtrl())
                    return super.onTouchEvent(ev);

                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                    getSelector().setState(new int[]{0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (mTouchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) {
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (disallowOpenCtrl())
                    return super.onTouchEvent(ev);

                if (ActionDownPos < 0) {
                    return super.onTouchEvent(ev);
                }

                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        boolean isBeforeOpen = mTouchView.isOpen();
                        mTouchView.onSwipe(ev);
                        boolean isAfterOpen = mTouchView.isOpen();
                        if (isBeforeOpen != isAfterOpen && mOnMenuStateChangeListener != null) {
                            if (isAfterOpen && ActionDownPos >= 0) {
                                this.OpenPosition = ActionDownPos;
                                mOnMenuStateChangeListener.onMenuOpen(ActionDownPos);
                            } else {
                                mOnMenuStateChangeListener.onMenuClose(OpenPosition);
                            }
                        }
                        if (!isAfterOpen) {
                            mTouchPosition = -1;
                            mTouchView = null;
                        }
                    }
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean disallowOpenCtrl() {
        if (disallowOpenPos != null && disallowOpenPos.size() > 0) {
            for (int a : disallowOpenPos) {
                if (ActionDownPos == a) {
                    DebugLog.e("拦截", "拦截");
                    return true;
                }
            }
        }
        return false;
    }

    public void smoothOpenMenu(int position) {
        this.OpenPosition = position;
        position += getHeaderViewsCount();
        if (position >= getFirstVisiblePosition()
                && position <= getLastVisiblePosition()) {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.setSwipeDirection(mDirection);
                mTouchView.smoothOpenMenu();
            }
        }
    }

    public void smoothCloseMenu() {
        if (mTouchView != null && mTouchView.isOpen()) {
            mTouchView.smoothCloseMenu();
            if (mOnMenuStateChangeListener != null) {
                mOnMenuStateChangeListener.onMenuClose(OpenPosition);
            }
        }

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        this.mMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener onMenuStateChangeListener) {
        mOnMenuStateChangeListener = onMenuStateChangeListener;
    }


    public void disallowOpen(List<Integer> disallowOpenPos) {
        this.disallowOpenPos = disallowOpenPos;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }


    @Override
    public void onClose() {
        /*if (mOnMenuStateChangeListener != null) {
            mOnMenuStateChangeListener.onMenuClose(OpenPosition);
        }*/
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(int position, SwipeMenu menu, int index);
    }

    public interface OnSwipeListener {
        void onSwipeStart(int position);

        void onSwipeEnd(int position);
    }

    public interface OnMenuStateChangeListener {
        void onMenuOpen(int position);

        void onMenuClose(int position);
    }

    public void setSwipeDirection(int direction) {
        mDirection = direction;
    }

    /**
     * 判断点击事件是否在某个view内
     *
     * @param view
     * @param ev
     * @return
     */
    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }


}