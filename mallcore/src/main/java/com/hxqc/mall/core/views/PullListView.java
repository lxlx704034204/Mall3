package com.hxqc.mall.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年10月19日
 */
public class PullListView extends ListView implements AbsListView.OnScrollListener {
    private final static String TAG = PullListView.class.getSimpleName();
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private View mFootView;

    private TextView mTextView;

    private ImageView mImageView;

    private Scroller mScroller;

    private float SCROLL_RATIO = 0.4f;

    float lastY = 0f;
    float deltaY = 0f;
    float totalY = 0f;
    boolean isFirstMove = true;
    boolean isPullToBottom = false;
    int height = 0;

    int currentPage = 1;

    private LoadingState LastLoadingState;

    private LoadingDataCallBack loadingDataCallBack;

    public static boolean ISLOADING = false;

    private final int FLIP_ANIMATION_DURATION = 150;

    private Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    public enum LoadingState {LOADING, LOADING_COMPLETE, LOADING_NO_MORE_DATA, LOADING_HIDDEN, PULL_TO_REFRESH, RELEASE_TO_REFRESH}


    public void setLoadingDataCallBack(LoadingDataCallBack loadingDataCallBack) {
        this.loadingDataCallBack = loadingDataCallBack;
    }


    public PullListView(Context context) {
        this(context, null);
        DebugLog.e(TAG, "one param");
    }


    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        DebugLog.e(TAG, "two params");
    }


    public PullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DebugLog.e(TAG, "three params");
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        mScroller = new Scroller(mContext);
        initFooterView();
        setOnScrollListener(this);
//        initPullListView();
    }


    public void initFooterView() {
        height = mContext.getResources().getDisplayMetrics().heightPixels;
        mFootView = mLayoutInflater.inflate(R.layout.layout_listview_load_more, null);
        mTextView = (TextView) mFootView.findViewById(R.id.tv_loadMore);
        mImageView = (ImageView) mFootView.findViewById(R.id.img_loadMore);
        this.addFooterView(mFootView);
        changeFooterViewState(LoadingState.LOADING_HIDDEN, 1);

        mRotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
        mResetRotateAnimation.setFillAfter(true);

        circleRotateAnimation = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
        circleRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
    }


    boolean isReachBottom = false;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        DebugLog.e(TAG, " ...");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isReachBottom = isListViewReachBottomEdge(this);

                DebugLog.d(TAG, " Action_down " + ev.getY() + "         " + isReachBottom);

                lastY = ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                DebugLog.d(TAG, " Action_move :  getY() : " + ev.getY() + "  totalY : " + totalY);

                deltaY = ev.getY() - lastY;
                totalY += deltaY;
                lastY = ev.getY();

                if (isFirstMove && deltaY < 0) {
                    // 根据用户的初始滑动方向决定是上拉操作还是下拉操作（一个全事件只判断一次）
                    isPullToBottom = true;
                }

                if (isFirstMove && deltaY != 0) isFirstMove = false;

                DebugLog.d(TAG, "deltaY : " + deltaY + "  isPullToBottom : " + isPullToBottom + "  isReachBottom : " + isReachBottom);

                if (isReachBottom && isPullToBottom && !ISLOADING) {
                    DebugLog.e(TAG, " 33333333333333");
                    // ListView到达底部，且继续向上滑动
                    if (totalY < 0) {
                        if (Math.abs(deltaY) > Math.abs(totalY) && getScrollY() > 0) {
                            scrollTo(0, 0);
                            DebugLog.e(TAG, "11111111111        totalY : " + totalY);
                        } else {
                            scrollBy(0, (int) (-deltaY * SCROLL_RATIO));
                            DebugLog.e(TAG, "22222222222         totalY : " + totalY);
                        }
                    }

                    if (totalY <= -150) {
                        changeFooterViewState(LoadingState.RELEASE_TO_REFRESH, 0);
                    } else {
                        changeFooterViewState(LoadingState.PULL_TO_REFRESH, currentPage);
                    }
                    return true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                DebugLog.d(TAG, "Action_cancel");
            case MotionEvent.ACTION_UP:
                DebugLog.d(TAG, " Action_up : " + ev.getY() + " getScrollX : " + getScrollX() + "  getScrollY : " + getScrollY());

                lastY = 0f;
                deltaY = 0f;
                isFirstMove = true;

                if (isReachBottom && isPullToBottom && !ISLOADING) {
                    if (totalY < -150) {
                        scrollTo(0, 0);
                        changeFooterViewState(LoadingState.LOADING, 0);
                        loadingDataCallBack.loadMore();
                    } else {
                        mScroller.startScroll(getScrollX(), getScrollY(), 0, -getScrollY(), (int) ((1 - Math.abs(totalY) / height) * 300));
                        invalidate();
                    }
                    isReachBottom = false;
                    isPullToBottom = false;
                    totalY = 0f;
                    return true;
                }

                totalY = 0f;
                isReachBottom = false;
                isPullToBottom = false;

                break;
        }

        boolean result = true;

        try {
            result = super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            DebugLog.e(TAG, " ------- computeScroll --------- " + " getCurrX : " + mScroller.getCurrX() + "  getCurrY : " + mScroller.getCurrY());

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            invalidate();
        }
        super.computeScroll();
    }


    public boolean isListViewReachBottomEdge(ListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }


    @Override
    public ListAdapter getAdapter() {
        ListAdapter adapter = super.getAdapter();
        return adapter instanceof HeaderViewListAdapter ? ((HeaderViewListAdapter) adapter).getWrappedAdapter() : adapter;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        DebugLog.e(TAG, " --------------- onScrollStateChanged ---------------");
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        DebugLog.e(TAG, " --------------- onScroll ----------------------");
    }


    public interface LoadingDataCallBack {
        void loadMore();
    }

    private Animation mRotateAnimation, mResetRotateAnimation;

    private AnimationSet circleRotateAnimation;


    public void changeFooterViewState(LoadingState loadingState, int page) {
//        if (LastLoadingState == loadingState) {
//            return;
//        }

        if (mImageView.getAnimation() != null && mImageView.getAnimation() == circleRotateAnimation) {
            mImageView.clearAnimation();
        }

        if (loadingState == LoadingState.LOADING) {
            ISLOADING = true;
        } else {
            ISLOADING = false;
        }

        if (loadingState != LoadingState.LOADING_HIDDEN && mFootView.getVisibility() == GONE) mFootView.setVisibility(VISIBLE);

        switch (loadingState) {
            // ListView进入放手即可加载数据状态
            case RELEASE_TO_REFRESH:
                if (LastLoadingState == LoadingState.RELEASE_TO_REFRESH) return;
                DebugLog.e(TAG, "loading_prepare...");
                mImageView.startAnimation(mRotateAnimation);
                mTextView.setText("松开加载下一页");
                break;

            // ListView处于上拉加载还未到能刷新的状态
            case PULL_TO_REFRESH:
                if (LastLoadingState == LoadingState.PULL_TO_REFRESH) return;
                if (LastLoadingState == LoadingState.RELEASE_TO_REFRESH) {
                    mImageView.startAnimation(mResetRotateAnimation);
                    mTextView.setText(String.format("当前第%d页 , 上拉加载下一页", currentPage));
                }
                break;

            // ListView加载数据中
            case LOADING:
                DebugLog.e(TAG, "loading...");

                mImageView.setImageResource(R.drawable.progress_drawable);
                mImageView.startAnimation(circleRotateAnimation);
                mTextView.setText("加载中...");
                break;

            // ListView数据在完成时状态，不同于LOADING_NO_MORE_DATA，这是表示还有下一页数据可以加载
            case LOADING_COMPLETE:
                DebugLog.e(TAG, "loading_complete...");
                mImageView.setImageResource(R.drawable.pull_refresh_icon);
                currentPage = page;
                mTextView.setText(String.format("当前第%d页 , 上拉加载下一页", page));
                break;

            // 数据全部加载完，理论上最后页分页数据时状态
            case LOADING_NO_MORE_DATA:
                DebugLog.e(TAG, "loading_no_more_data...");
                mImageView.setImageResource(R.drawable.pull_refresh_icon);
                mTextView.setText(String.format("当前第%d页 , 数据全部加载完毕", page));
                break;

            // 当ListVie的Adapter中还没有数据时，不显示FooterView的一种状态
            case LOADING_HIDDEN:
                DebugLog.e(TAG, "loading_hidden...");
                mFootView.setVisibility(GONE);
                break;
        }

        LastLoadingState = loadingState;
    }
}
