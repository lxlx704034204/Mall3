package com.hxqc.mall.auto.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DisplayTools;

/**
 * Author:胡仲俊
 * Date: 2016 - 09 - 20
 * FIXME
 * Todo
 */
public class CommonRelativeTextView extends RelativeLayout {

    private TextView mFirstTextView;
    private TextView mTwoTextView;
    private CharSequence firstText;
    private int firstTextColor;
    private int firstTextSize;
    private int firstTextEms;
    private CharSequence twoText;
    private boolean twoTextSingleLine;
    private int twoTextColor;
    private int twoTextSize;
    private int firstBrackground;
    private int twoMarginLeft;
    private final boolean twoAlignParentRight;

    public CommonRelativeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonRelativeTextView);
        firstText = typedArray.getText(R.styleable.CommonRelativeTextView_layout_first_text);
        firstTextColor = typedArray.getColor(R.styleable.CommonRelativeTextView_layout_first_textColor, getResources().getColor(R.color.text_color_subheading));
        firstTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonRelativeTextView_layout_first_textSize, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        firstBrackground = typedArray.getResourceId(R.styleable.CommonRelativeTextView_layout_first_background, 0);
        firstTextEms = typedArray.getInteger(R.styleable.CommonRelativeTextView_layout_first_ems, 0);

        twoText = typedArray.getText(R.styleable.CommonRelativeTextView_layout_two_text);
        twoTextSingleLine = typedArray.getBoolean(R.styleable.CommonRelativeTextView_layout_two_text_singleline, false);
        twoTextColor = typedArray.getColor(R.styleable.CommonRelativeTextView_layout_two_textColor, getResources().getColor(R.color.text_color_subheading));
        twoTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonRelativeTextView_layout_two_textSize, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        twoMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonRelativeTextView_layout_two_marginLeft, 0);
        twoAlignParentRight = typedArray.getBoolean(R.styleable.CommonRelativeTextView_layout_two_alignParentRight, false);
        typedArray.recycle();

        mFirstTextView = new TextView(context);
        mFirstTextView.setText(firstText);
        if(firstTextEms>0) {
            mFirstTextView.setEms(firstTextEms);
        }
        mFirstTextView.setTextColor(firstTextColor);
        mFirstTextView.setTextSize(DisplayTools.px2sp(context, firstTextSize));
        mFirstTextView.setBackgroundResource(firstBrackground);
        addView(mFirstTextView);

        mTwoTextView = new TextView(context);
        mTwoTextView.setText(twoText);
        mTwoTextView.setTextColor(twoTextColor);
        mTwoTextView.setTextSize(DisplayTools.px2sp(context, twoTextSize));
        if (twoAlignParentRight) {
            RelativeLayout.LayoutParams twolayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            twolayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mTwoTextView.setLayoutParams(twolayoutParams);
        }
        addView(mTwoTextView);

    }

    public void setTwoText(CharSequence text) {
        mTwoTextView.setText(text);
    }

}
