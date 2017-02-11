package com.hxqc.mall.control;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.hxqc.mall.core.model.BootAnimBean;

import java.util.ArrayList;


/**
 * Author: wanghao
 * Date: 2015-04-28
 * FIXME
 * 启始动画
 */
public class BootAnimControl {

    public final static int SHOOT_LEFT = 1;
    public final static int SHOOT_RIGHT = 2;

    protected static BootAnimControl animInstance;
    public int duringTime_bg = 2000;
    public int duringTime_item = 400;
    ArrayList<BootAnimBean> blocks;
    private int width;

    protected BootAnimControl() {
    }

    public static BootAnimControl getInstance() {
        if (animInstance == null) {
            synchronized (BootAnimControl.class) {
                if (animInstance == null) {
                    animInstance = new BootAnimControl();
                }
            }
        }
        return animInstance;
    }

    public void onDestroy() {
        animInstance = null;
    }
    /**
     * 背景透明消失
     *
     * @return
     */
    public void alphaBGAnim(ImageView bg, ArrayList<BootAnimBean> animBeans,int w) {
        this.blocks = animBeans;
        this.width = w;
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(duringTime_bg);
        bg.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                for (BootAnimBean bean : blocks) {
                    interpolatorAnim(bean);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 左右动画
     */
    public void interpolatorAnim(final BootAnimBean bean) {

        TranslateAnimation animation = null;
        if (bean.shoot_direction == SHOOT_LEFT) {
            animation = new TranslateAnimation(-width, 0f, 1f, 1f);
        } else if (bean.shoot_direction == SHOOT_RIGHT) {
            animation = new TranslateAnimation(width, 0f, 1f, 1f);
        }
        if (animation != null) {
            animation.setFillAfter(true);
            animation.setDuration(duringTime_item);
            animation.setInterpolator(new OvershootInterpolator());
            bean.itemView.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    bean.itemView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

}
