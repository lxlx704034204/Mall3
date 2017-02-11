package com.hxqc.mall.auto.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.R;
import com.hxqc.util.DisplayTools;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 21
 * Des: 文字控件
 * FIXME
 * Todo
 */

public class TextRelativeLayout extends RelativeLayout {

    private static final String TAG = AutoInfoContants.LOG_J;
    private CharSequence leftText;
    private int leftTextColor;
    private int leftTextSize;
    private int leftDrawableLeft;//头左侧图片
    private CharSequence rightText;
    private int rightTextColor;
    private int rightTextSize;
    private int leftBrackground;
    private int rightMarginLeft;
    private int rightMarginTop;
    private TextView mRightView;
    private TextView mLeftView;
    private int leftEms;
    private boolean rightAlignParentRight;

    public TextRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextRelativeLayout);

        leftText = typedArray.getText(R.styleable.TextRelativeLayout_trl_left_text);
        leftTextColor = typedArray.getColor(R.styleable.TextRelativeLayout_trl_left_textColor, getResources().getColor(R.color.text_color_title));
        leftTextSize = typedArray.getDimensionPixelSize(R.styleable.TextRelativeLayout_trl_left_textSize, 14);
        leftBrackground = typedArray.getResourceId(R.styleable.TextRelativeLayout_trl_left_background, 0);
        leftDrawableLeft = typedArray.getResourceId(R.styleable.TextRelativeLayout_trl_left_drawleft, 0);
        leftEms = typedArray.getInt(R.styleable.TextRelativeLayout_trl_left_ems, 0);

        rightText = typedArray.getText(R.styleable.TextRelativeLayout_trl_right_text);
        rightTextColor = typedArray.getColor(R.styleable.TextRelativeLayout_trl_right_textColor, getResources().getColor(R.color.text_color_title));
        rightTextSize = typedArray.getDimensionPixelSize(R.styleable.TextRelativeLayout_trl_right_textSize, 14);
        rightMarginLeft = typedArray.getDimensionPixelSize(R.styleable.TextRelativeLayout_trl_right_marginLeft, 0);
        rightMarginTop = typedArray.getDimensionPixelSize(R.styleable.TextRelativeLayout_trl_right_marginTop, 0);
        rightAlignParentRight = typedArray.getBoolean(R.styleable.TextRelativeLayout_trl_right_alignParentRight, false);
        typedArray.recycle();

        mLeftView = new TextView(context);
        mLeftView.setText(leftText);
        mLeftView.setId(R.id.left_text);
        if (leftEms > 0) {
            mLeftView.setEms(leftEms);
        }
        mLeftView.setTextColor(leftTextColor);
        mLeftView.setTextSize(DisplayTools.px2sp(context, leftTextSize));
        mLeftView.setBackgroundResource(leftBrackground);

        if (leftDrawableLeft > 0) {
            Drawable drawableLeft = getResources().getDrawable(leftDrawableLeft);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            mLeftView.setCompoundDrawables(drawableLeft, null, null, null);
        }

        mRightView = new TextView(context);
        mRightView.setText(rightText);
        mRightView.setTextColor(rightTextColor);
        mRightView.setTextSize(DisplayTools.px2sp(context, rightTextSize));

        RelativeLayout.LayoutParams rightlayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (rightAlignParentRight) {
            rightlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }

        rightlayoutParams.addRule(RelativeLayout.RIGHT_OF, mLeftView.getId());
        rightlayoutParams.setMargins(rightMarginLeft, rightMarginTop, 0, 0);
        mRightView.setLayoutParams(rightlayoutParams);
        mRightView.setSingleLine(true);

        addView(mLeftView);
        addView(mRightView, rightlayoutParams);

    }

}
