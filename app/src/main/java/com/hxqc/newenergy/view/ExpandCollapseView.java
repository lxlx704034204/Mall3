package com.hxqc.newenergy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * Function: 包含动画的展开、收起View
 *
 * @author 袁秉勇
 * @since 2016年3月11日
 */
public class ExpandCollapseView extends RelativeLayout {
    private static final String TAG = ExpandCollapseView.class.getSimpleName();

    private static final int DURATION = 400;

    private float DEGREES = 180;

    /** 可展开的组group中的相关控件 **/
    protected RelativeLayout titleContentView;

    protected ImageView rightIcon;

    protected LinearLayout contentLayout;

    private ValueAnimator animator;
    private RotateAnimation rotateAnimator;


    public ImageView getRightIcon() {
        return rightIcon;
    }


    public ExpandCollapseView(Context context) {
        super(context);
        init();
    }


    public ExpandCollapseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public ExpandCollapseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.expandable_view_v20, this);

        titleContentView = (RelativeLayout) findViewById(R.id.title_content_view);


        rightIcon = (ImageView) findViewById(R.id.expandable_view_right_icon);

        contentLayout = (LinearLayout) findViewById(R.id.expandable_view_content_layout);

        contentLayout.setVisibility(GONE);

        titleContentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doClick();
            }
        });

        DebugLog.e(getClass().getName(), "excute function xxxxxxxxxx " + System.currentTimeMillis());

        titleContentView.post(new Runnable() {
            @Override
            public void run() {
                DebugLog.e(TAG, " titleContentView this is excute time" + System.currentTimeMillis());
                contentLayout.setPadding(contentLayout.getPaddingLeft(), titleContentView.getMeasuredHeight(), contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
            }
        });

        DebugLog.e(getClass().getName(), "excute function xxxxxxxx" + System.currentTimeMillis());

        contentLayout.post(new Runnable() {
            @Override
            public void run() {
                DebugLog.e(TAG, " contentLayout this is excute time " + System.currentTimeMillis());
            }
        });

        this.post(new Runnable() {
            @Override
            public void run() {
                DebugLog.e(TAG, " ExpandCollapseView this is excute time " + System.currentTimeMillis());
            }
        });

        if (getHandler() == null) {
            DebugLog.e(TAG, "initView Handler is NULL");
        } else {
            DebugLog.e(TAG, "initView Handler is not NULL");
        }
    }


    @Override
    protected void onFinishInflate() {
        DebugLog.e(TAG, "this is inflate");
        super.onFinishInflate();
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        DebugLog.e(TAG, "this is windowFocusChange ");
        super.onWindowFocusChanged(hasWindowFocus);
    }


    @Override
    protected void onAttachedToWindow() {
        DebugLog.e(TAG, " this is onAttachToWindow ");


        if (getHandler() == null) {
            DebugLog.e(TAG, "initView Handler is NULL");
        } else {
            DebugLog.e(TAG, "initView Handler is not NULL");
        }

        super.onAttachedToWindow();

        if (getHandler() == null) {
            DebugLog.e(TAG, "onAttachedToWindow Handler is NULL");
        } else {
            DebugLog.e(TAG, "onAttachedToWindow Handler is not NULL");
        }

//        DebugLog.e(getClass().getName(), "onAttachToWindow excute function xxxxxxxxxx " + System.currentTimeMillis());
//        titleContentView.post(new Runnable() {
//            @Override
//            public void run() {
//                DebugLog.e(TAG, " onAttachedToWindow titleContentView this is excute time ========" + System.currentTimeMillis());
//                contentLayout.setPadding(contentLayout.getPaddingLeft(), titleContentView.getMeasuredHeight(), contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
//            }
//        });
//
//        DebugLog.e(getClass().getName(), "onAttachToWindow excute function xxxxxxxxxx " + System.currentTimeMillis());
    }


    public void doClick() {
        if (contentLayout.isShown()) {
            collapse();
        } else {
            expand();
        }
    }


    public void addContentView(View view) {
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }


    public void addTitleView(View view) {
        for (int i = 0; i < titleContentView.getChildCount(); i++) {
            if (titleContentView.getChildAt(i) == rightIcon) break;
            titleContentView.removeView(titleContentView.getChildAt(i));
        }
        titleContentView.addView(view);
    }


    /**
     * Expand animation to display the discoverable content.
     */
    public void expand() {
        //set Visible
        contentLayout.setVisibility(View.VISIBLE);
//        int x = rightIcon.getMeasuredWidth() / 2;
//        int y = rightIcon.getMeasuredHeight() / 2;
//
//        rotateAnimator = new RotateAnimation(0f, DEGREES, x, y);
//        rotateAnimator.setInterpolator(new LinearInterpolator());
//        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
//        rotateAnimator.setFillAfter(true);
//        rotateAnimator.setDuration(DURATION);
//        rotateAnimator.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                rightIcon.setImageResource(R.drawable.ph_jiantou01);
//            }
//
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        rightIcon.startAnimation(rotateAnimator);

        final int widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        contentLayout.measure(widthSpec, heightSpec);

        animator = slideAnimator(titleContentView.getMeasuredHeight(), contentLayout.getMeasuredHeight());

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rightIcon.setImageResource(R.drawable.ph_jiantou01);
            }
        });
        animator.start();
    }


    /**
     * Collapse animation to hide the discoverable content.
     */
    public void collapse() {
        int finalHeight = contentLayout.getHeight();

//        int x = rightIcon.getMeasuredWidth() / 2;
//        int y = rightIcon.getMeasuredHeight() / 2;
//
//        rotateAnimator = new RotateAnimation(0f, -DEGREES, x, y);
//        rotateAnimator.setInterpolator(new LinearInterpolator());
//        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
//        rotateAnimator.setFillAfter(true);
//        rotateAnimator.setDuration(DURATION);
//        rotateAnimator.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                rightIcon.setImageResource(R.drawable.ph_jiantou03);
//            }
//
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//            }
//
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        rightIcon.startAnimation(rotateAnimator);

        ValueAnimator mAnimator = slideAnimator(finalHeight, titleContentView.getMeasuredHeight());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                contentLayout.setVisibility(View.GONE);
                rightIcon.setImageResource(R.drawable.ph_jiantou02);
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(DURATION);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
                layoutParams.height = value;

                contentLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DebugLog.e(TAG, "this is onMeasure ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        DebugLog.e(TAG, "this is onLayout ");
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    public void draw(Canvas canvas) {
        DebugLog.e(TAG, " this is draw ");
        super.draw(canvas);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        DebugLog.e(TAG, " this is onDraw ");
        super.onDraw(canvas);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        DebugLog.e(TAG, " this is dispatchDraw ");
        super.dispatchDraw(canvas);
    }
}
