package com.hxqc.mall.core.anim.easing;

import com.nineoldandroids.animation.TypeEvaluator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public abstract class BaseEasingMethod implements TypeEvaluator<Number>{

    protected float mDuration;
    private ArrayList< EasingListener > mListeners = new ArrayList<  >();

    public void addEasingListener(BaseEasingMethod.EasingListener l) {
        this.mListeners.add(l);
    }

    public void addEasingListeners(BaseEasingMethod.EasingListener... ls) {
        int len$ = ls.length;

        this.mListeners.addAll(Arrays.asList(ls).subList(0, len$));

    }

    public void removeEasingListener(BaseEasingMethod.EasingListener l) {
        this.mListeners.remove(l);
    }

    public void clearEasingListeners() {
        this.mListeners.clear();
    }

    public BaseEasingMethod(float duration) {
        this.mDuration = duration;
    }

    public void setDuration(float duration) {
        this.mDuration = duration;
    }

    public final Float evaluate(float fraction, Number startValue, Number endValue) {
        float t = this.mDuration * fraction;
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        float d = this.mDuration;
        float result = this.calculate(t, b, c, d);

        for (EasingListener l : this.mListeners) {
            l.on(t, result, b, c, d);
        }

        return result;
    }

    public abstract Float calculate(float var1, float var2, float var3, float var4);

    public interface EasingListener {
        void on(float var1, float var2, float var3, float var4, float var5);
    }

}
