package com.hxqc.autonews.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-10-17
 * FIXME
 * Todo 点击评论的横条
 */

public class EvaluationBar extends LinearLayout implements View.OnClickListener {

    private final TextView evaluationBarInput;
    private final ImageView evaluationBarMessage;
    private final TextView evaluationCount;
    private final ImageView evaluationBarShare;

    /**
     * 设置文本颜色（图集模式是灰色的背景字体要浅色）
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        evaluationBarInput.setTextColor(textColor);
    }

    private int textColor = Color.RED;
    private int bgColor = Color.WHITE;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        showCount();
    }

    /**
     * 显示当前资讯的已评论数
     */
    private void showCount() {
        if (count == 0) {
            evaluationCount.setVisibility(GONE);
        } else {
            evaluationCount.setText(count + "");
            evaluationCount.setVisibility(VISIBLE);
        }
    }

    private int count = 0;

    public EvaluationBar(Context context) {
        this(context, null);
    }

    public EvaluationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EvaluationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_evaluation_bar, this);
        evaluationBarInput = (TextView) findViewById(R.id.evaluation_bar_input);
        evaluationBarMessage = (ImageView) findViewById(R.id.evaluation_bar_message);
        evaluationCount = (TextView) findViewById(R.id.evaluation_count);
        evaluationBarShare = (ImageView) findViewById(R.id.evaluation_bar_share);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EvaluationBar);
        textColor = typedArray.getColor(R.styleable.EvaluationBar_text_color, Color.RED);
        bgColor = typedArray.getColor(R.styleable.EvaluationBar_bg_color, Color.WHITE);
        typedArray.recycle();
        evaluationBarInput.setTextColor(textColor);
        setBackgroundColor(bgColor);
        evaluationBarShare.setOnClickListener(this);
        evaluationBarMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.evaluation_bar_message) {
            //消息
            if (onMessageClickListener != null) {
                onMessageClickListener.onMessageClick();
            }
        } else if (v.getId() == R.id.evaluation_bar_share) {
            //分享
            if (onShareClickListener != null) {
                onShareClickListener.onShareClick();
            }
        }
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public void setOnMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    private OnShareClickListener onShareClickListener;
    private OnMessageClickListener onMessageClickListener;

    /**
     * 点击“分享”图标
     */
    public interface OnShareClickListener {
        void onShareClick();
    }

    /**
     * 点击message图标
     */
    public interface OnMessageClickListener {
        void onMessageClick();
    }
}
