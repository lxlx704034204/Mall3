package com.hxqc.mall.core.anim.easing;

import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public class Glider {

    public Glider() {
    }

    public static ValueAnimator glide(Skill skill, float duration, ValueAnimator animator) {
        return glide(skill, duration, animator, (BaseEasingMethod.EasingListener[])null);
    }

    public static ValueAnimator glide(Skill skill, float duration, ValueAnimator animator, BaseEasingMethod.EasingListener... listeners) {
        BaseEasingMethod t = skill.getMethod(duration);
        if(listeners != null) {
            t.addEasingListeners(listeners);
        }

        animator.setEvaluator(t);
        return animator;
    }

    public static PropertyValuesHolder glide(Skill skill, float duration, PropertyValuesHolder propertyValuesHolder) {
        propertyValuesHolder.setEvaluator(skill.getMethod(duration));
        return propertyValuesHolder;
    }

}
