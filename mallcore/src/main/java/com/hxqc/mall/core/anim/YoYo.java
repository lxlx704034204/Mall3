package com.hxqc.mall.core.anim;

import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * 动画
 */
public class YoYo {

    private static final long DURATION = BaseViewAnimator.DURATION;
    private static final long NO_DELAY = 0;

    private BaseViewAnimator animator;
    private long duration;
    private long delay;
    private Interpolator interpolator;
    private List<Animator.AnimatorListener> callbacks;
    private View target;

    private YoYo(AnimationComposer animationComposer) {
        animator = animationComposer.animator;
        duration = animationComposer.duration;
        delay = animationComposer.delay;
        interpolator = animationComposer.interpolator;
        callbacks = animationComposer.callbacks;
        target = animationComposer.target;
    }

    public static AnimationComposer with(Techniques techniques) {
        return new AnimationComposer(techniques);
    }

    public static AnimationComposer with(BaseViewAnimator animator) {
        return new AnimationComposer(animator);
    }

    private BaseViewAnimator play() {
        animator.setTarget(target);
        animator.setDuration(duration)
                .setInterpolator(interpolator)
                .setStartDelay(delay);

        if (callbacks.size() > 0) {
            for (Animator.AnimatorListener callback : callbacks) {
                animator.addAnimatorListener(callback);
            }
        }

        animator.animate();
        return animator;
    }

    public interface AnimatorCallback {
        void call(Animator animator);
    }

    private static class EmptyAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation){}
        @Override
        public void onAnimationEnd(Animator animation){}
        @Override
        public void onAnimationCancel(Animator animation){}
        @Override
        public void onAnimationRepeat(Animator animation){}
    }

    public static final class AnimationComposer {

        private List<Animator.AnimatorListener> callbacks = new ArrayList<Animator.AnimatorListener>();

        private BaseViewAnimator animator;
        private long duration = DURATION;
        private long delay = NO_DELAY;
        private Interpolator interpolator;
        private View target;

        private AnimationComposer(Techniques techniques) {
            this.animator = techniques.getAnimator();
        }

        private AnimationComposer(BaseViewAnimator animator) {
            this.animator = animator;
        }

        public AnimationComposer duration(long duration) {
            this.duration = duration;
            return this;
        }

        public AnimationComposer delay(long delay) {
            this.delay = delay;
            return this;
        }

        public AnimationComposer interpolate(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }


        public AnimationComposer withListener(Animator.AnimatorListener listener) {
            callbacks.add(listener);
            return this;
        }

        public AnimationComposer onStart(final AnimatorCallback callback) {
            callbacks.add(new EmptyAnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) { callback.call(animation); }
            });
            return this;
        }

        public AnimationComposer onEnd(final AnimatorCallback callback) {
            callbacks.add(new EmptyAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) { callback.call(animation); }
            });
            return this;
        }

        public AnimationComposer onCancel(final AnimatorCallback callback) {
            callbacks.add(new EmptyAnimatorListener() {
                @Override
                public void onAnimationCancel(Animator animation) { callback.call(animation); }
            });
            return this;
        }

        public AnimationComposer onRepeat(final AnimatorCallback callback) {
            callbacks.add(new EmptyAnimatorListener() {
                @Override
                public void onAnimationRepeat(Animator animation) { callback.call(animation); }
            });
            return this;
        }

        public YoYoString playOn(View target) {
            this.target = target;
            return new YoYoString(new YoYo(this).play(), this.target);
        }

    }

    /**
     * YoYo string, you can use this string to control your YoYo.
     */
    public static final class YoYoString {

        private BaseViewAnimator animator;
        private View target;

        private YoYoString(BaseViewAnimator animator, View target){
            this.target = target;
            this.animator = animator;
        }

        public boolean isStarted(){
            return animator.isStarted();
        }

        public boolean isRunning(){
            return animator.isRunning();
        }

        public void stop(boolean reset){
            animator.cancel();

            if(reset)
                animator.reset(target);
        }

    }

}
