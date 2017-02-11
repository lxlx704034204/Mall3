package com.hxqc.mall.core.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;


/**
 * Author: wanghao
 * Date: 2015-10-12
 * FIXME
 * Todo
 */
public class LayoutAnimatorUtil {

    View view;//执行动画的view
    public static final int REDUCE = 1;
    public static final int INCREASE = 2;

    private long duration = 500;

    private int origin_h = 0;
    private int final_h = 0;
    private int type = 1;

    ViewGroup.LayoutParams params;
    ValueAnimator animator;
    AnimatorCallBack animatorCallBack;

    /**
     * 构造移动动画
     * @param view 执行的view
     * @param origin_h 原始长度
     * @param final_h 最终长度
     * @param t 变化类型
     */
    public LayoutAnimatorUtil(View view, int origin_h, int final_h, int t) {
        this.view = view;
        this.origin_h = origin_h;
        this.final_h = final_h;
        this.type = t;
        initAnimator();
    }

    public void startAnimator() {
        if (animator != null) {
            animator.start();
        }
    }

    public void setDuration(long duration) {
        if (animator!=null){
            animator.setDuration(duration);
        }
    }

    public interface AnimatorCallBack {
        void onAnimationEndCallBack(Animator animation);

        void onAnimationStartCallBack(Animator animation);
    }

    public void setAnimatorCallBack(AnimatorCallBack animatorCallBack) {
        this.animatorCallBack = animatorCallBack;
    }

    public void removeAnimatorCallBack() {
        if (this.animatorCallBack != null)
            this.animatorCallBack = null;
    }

    private void initAnimator() {

        final int y = Math.abs(final_h - origin_h);

        params = view.getLayoutParams();

        if (type == INCREASE) {
            animator = ValueAnimator.ofFloat(0, 1);
        } else {
            animator = ValueAnimator.ofFloat(1, 0);
        }

        animator.setDuration(duration);
//        animator.setInterpolator(new OvershootInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                /**
                 * animatedValue   0.0~~~~1.0
                 */

                params.height = (int) ((y) * animatedValue);
//                DebugLog.i("test_bug", "onAnimationUpdate" + params.height);
                view.setLayoutParams(params);
                view.setAlpha(animatedValue);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animatorCallBack != null)
                    animatorCallBack.onAnimationStartCallBack(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animatorCallBack != null)
                    animatorCallBack.onAnimationEndCallBack(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
