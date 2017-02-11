package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.R;


/**
 * Author: HuJunJie
 * Date: 2015-04-01
 * FIXME
 * Todo
 *
 * @attr ref R.styleable#LineTranslateAnimView_lineCount
 * @attr ref R.styleable#LineTranslateAnimView_animDurations
 */
public class LineTranslateAnimView extends RelativeLayout {

    private static final int DEFAULT_LINES = 1;
    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 300;

    View v_line;
    int lineCount = 2;
    int lastIndex = 0;
    int mAnimationDuration = 200;

    public LineTranslateAnimView(Context context) {
        super(context);
    }

    public LineTranslateAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_line_translate_anim, this);
        v_line = findViewById(R.id.v_line);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LineTranslateAnimView);
        lineCount = typedArray.getInt(R.styleable.LineTranslateAnimView_lineCount, DEFAULT_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.LineTranslateAnimView_animDurations, DEFAULT_ANIM_DURATION);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initUnderline();
    }

    //初始化下划线
    private void initUnderline() {
        if(getWidth()!=0){
            LayoutParams layoutParams = (LayoutParams) v_line.getLayoutParams();
            layoutParams.width = getWidth() / lineCount;
            v_line.setLayoutParams(layoutParams);
            invalidate();
        }

    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;

    }

    //下划线动画
    public void startUnderlineAnim(int index) {
        TranslateAnimation animation = new TranslateAnimation(lastIndex * getWidth() / lineCount, index * getWidth() / lineCount, 0, 0);
//        int i = Math.abs(lastIndex - index);
//        i = i == 0 ? 1 : i;
//        int duration = mAnimationDuration*i;
        animation.setDuration(mAnimationDuration);
        animation.setFillAfter(true);
        v_line.startAnimation(animation);
        lastIndex = index;
    }
}
