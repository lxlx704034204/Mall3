package com.hxqc.mall.auto.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DisplayTools;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * FIXME
 * Todo 带*文字
 */
public class CommenTwoTextView extends LinearLayout {

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
    private TextView mTwoView;
    private TextView mFirstView;

    public CommenTwoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommenTwoTextView);
        firstText = typedArray.getText(R.styleable.CommenTwoTextView_auto_info_first_text);
        firstTextColor = typedArray.getColor(R.styleable.CommenTwoTextView_auto_info_first_textColor, getResources().getColor(R.color.text_color_subheading));
        firstTextSize = typedArray.getDimensionPixelSize(R.styleable.CommenTwoTextView_auto_info_first_textSize, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        firstBrackground = typedArray.getResourceId(R.styleable.CommenTwoTextView_auto_info_first_background, 0);
        firstTextEms = typedArray.getInteger(R.styleable.CommenTwoTextView_auto_info_first_ems, 6);


        twoText = typedArray.getText(R.styleable.CommenTwoTextView_auto_info_two_text);
        twoTextSingleLine = typedArray.getBoolean(R.styleable.CommenTwoTextView_auto_info_two_text_singleline,false);
        twoTextColor = typedArray.getColor(R.styleable.CommenTwoTextView_auto_info_two_textColor, getResources().getColor(R.color.text_color_title));
        twoTextSize = typedArray.getDimensionPixelSize(R.styleable.CommenTwoTextView_auto_info_two_textSize, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        twoMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommenTwoTextView_auto_info_two_marginLeft, 0);
        typedArray.recycle();

        mFirstView = new TextView(context);
        mFirstView.setText(firstText);
        mFirstView.setEms(firstTextEms);
        mFirstView.setTextColor(firstTextColor);
        mFirstView.setTextSize(DisplayTools.px2sp(context, firstTextSize));
        mFirstView.setBackgroundResource(firstBrackground);
        addView(mFirstView);

        mTwoView = new TextView(context);
        mTwoView.setText(twoText);
        mTwoView.setTextColor(twoTextColor);
        mTwoView.setTextSize(DisplayTools.px2sp(context, twoTextSize));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(twoMarginLeft,0,0,0);
        mTwoView.setLayoutParams(layoutParams);
        if(twoTextSingleLine) {
            mTwoView.setSingleLine(true);
        }
        mTwoView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        addView(mTwoView);
    }

    public void setTwoText(CharSequence text) {
        mTwoView.setText(text);
    }
}
