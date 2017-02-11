package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;


/**
 * Author:胡俊杰
 * Date: 2015/12/1
 * FIXME
 * Todo  筛选条件
 */
public class FilterFactorView extends RelativeLayout {
    TextView mLabelView;
    TextView mTagView;
    int answerColor;//有结果颜色
    int unAnswerColor;//无结果颜色
    int labelColor;
    String labelString;


    public FilterFactorView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_filter_factor, this);
        mLabelView = (TextView) findViewById(R.id.filter_factor_label);
        mTagView = (TextView) findViewById(R.id.filter_factor_tag);
        answerColor = getContext().getResources().getColor(R.color.cursor_orange);
        unAnswerColor = getContext().getResources().getColor(R.color.straight_matter_and_secondary_text);
    }


    public FilterFactorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FilterFactorView);
        labelString = typedArray.getString(R.styleable.FilterFactorView_label);
        labelColor = typedArray.getColor(R.styleable.FilterFactorView_labelColor, getContext().getResources().getColor(R.color.cursor_orange));
        answerColor = typedArray.getResourceId(R.styleable.FilterFactorView_answerColor, getContext().getResources().getColor(R.color.cursor_orange));
        unAnswerColor = typedArray.getResourceId(R.styleable.FilterFactorView_unAnswerColor, getContext().getResources().getColor(R.color.straight_matter_and_secondary_text));
        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.item_filter_factor, this);
        mLabelView = (TextView) findViewById(R.id.filter_factor_label);
        mTagView = (TextView) findViewById(R.id.filter_factor_tag);
        mLabelView.setText(labelString);
        mLabelView.setTextColor(labelColor);
        mLabelView.getPaint().setFakeBoldText(true);
    }


    public void setLabelString(String labelString) {
        mLabelView.setText(labelString);
    }


    public void setTagString(String tagString, int color) {
        mTagView.setText(tagString);
        mTagView.setTextColor(color);
    }


    public void setTagString(String tagString, boolean hasResponse) {
        mTagView.setText(tagString);
        if (hasResponse) {
            mTagView.setTextColor(answerColor);
        } else {
            mTagView.setTextColor(unAnswerColor);
        }

    }
}
