package com.hxqc.mall.auto.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
public class CommonTwoTextView extends LinearLayout {

    private static final String TAG = "CommonTwoTextView";
    private CharSequence firstText;
    private int firstTextColor;
    private int firstTextSize;
    private int firstDrawableLeft;//头左侧图片
    private CharSequence twoText;
    private int twoTextColor;
    private int twoTextSize;
    private int firstBrackground;
    private int twoMarginLeft;
    private int twoMarginTop;
    private TextView mTwoView;
    private TextView mFirstView;
    private int firstEms;

    public CommonTwoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommenTwoTextView);
        firstText = typedArray.getText(R.styleable.CommenTwoTextView_auto_info_first_text);
        firstTextColor = typedArray.getColor(R.styleable.CommenTwoTextView_auto_info_first_textColor, getResources().getColor(R.color.text_color_title));
        firstTextSize = typedArray.getDimensionPixelSize(R.styleable.CommenTwoTextView_auto_info_first_textSize, 14);
        firstBrackground = typedArray.getResourceId(R.styleable.CommenTwoTextView_auto_info_first_background, 0);
        firstDrawableLeft = typedArray.getResourceId(R.styleable.CommenTwoTextView_auto_info_first_drawleft, R.drawable.bg_transparent);
        firstEms = typedArray.getInt(R.styleable.CommenTwoTextView_auto_info_first_ems, 0);

        twoText = typedArray.getText(R.styleable.CommenTwoTextView_auto_info_two_text);
        twoTextColor = typedArray.getColor(R.styleable.CommenTwoTextView_auto_info_two_textColor, getResources().getColor(R.color.text_color_title));
        twoTextSize = typedArray.getDimensionPixelSize(R.styleable.CommenTwoTextView_auto_info_two_textSize, 14);
        twoMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommenTwoTextView_auto_info_two_marginLeft, 0);
        twoMarginTop = typedArray.getDimensionPixelSize(R.styleable.CommenTwoTextView_auto_info_two_marginTop, 0);
        typedArray.recycle();

        mFirstView = new TextView(context);
        mFirstView.setText(firstText);
        if (firstEms > 0) {
            mFirstView.setEms(firstEms);
        }
        mFirstView.setTextColor(firstTextColor);
        mFirstView.setTextSize(DisplayTools.px2sp(context, firstTextSize));
        mFirstView.setBackgroundResource(firstBrackground);
       /* Drawable drawableLeft = getResources().getDrawable(firstDrawableLeft);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        mFirstView.setCompoundDrawables(drawableLeft, null, null, null);*/
        addView(mFirstView);

        mTwoView = new TextView(context);
        mTwoView.setText(twoText);
        mTwoView.setTextColor(twoTextColor);
        mTwoView.setTextSize(DisplayTools.px2sp(context, twoTextSize));
        LayoutParams twoParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        twoParams.setMargins(twoMarginLeft, twoMarginTop, 0, 0);
        mTwoView.setLayoutParams(twoParams);
        mTwoView.setSingleLine(true);
        addView(mTwoView);

    }

    public void setFirstDrawableLeft(int id) {
        Drawable drawableLeft = getResources().getDrawable(id);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        mFirstView.setCompoundDrawables(drawableLeft, null, null, null);
    }

    public void setFirstText(CharSequence text) {
        mFirstView.setText(text);
    }

    public void setFirstTextColor(int color) {
        mFirstView.setTextColor(color);
    }

    public void setTwoText(CharSequence text) {
        mTwoView.setText(text);
    }

    public void setTwoTextColor(int color) {
        mTwoView.setTextColor(color);
    }

    public String getTwoText() {
        return mTwoView.getText().toString();
    }

    public void setFirstTextEms(int ems) {
        mFirstView.setEms(ems);
    }
}
