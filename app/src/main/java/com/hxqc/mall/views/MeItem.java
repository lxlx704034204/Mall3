package com.hxqc.mall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hxqc.mall.R;


/**
 * 说明:个人中心item(含设置界面部分item)
 * <p/>
 * author: 吕飞
 * since: 2015-03-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class MeItem extends RelativeLayout {
    TextView mLeftTextView;
    ImageView mLeftIconView;
    TextView mRightTextView;
    ImageView mRightIconView;

    String mLabelString;
    int mTagDrawable;
    int mIconDrawable;

    public MeItem(Context context) {
        super(context);
    }

    public MeItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CellTab);
        mLabelString = typedArray.getString(R.styleable.CellTab_cellLabel);
        mIconDrawable = typedArray.getResourceId(R.styleable.CellTab_cellIconDrawable, 0);
        mTagDrawable = typedArray.getResourceId(R.styleable.CellTab_cellTagDrawable, R.drawable.ic_list_arrow);
        typedArray.recycle();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_item_me, this);
        mLeftTextView = (TextView) findViewById(R.id.left_text);
        mRightTextView = (TextView) findViewById(R.id.right_text);
        mLeftIconView = (ImageView) findViewById(R.id.left_icon);
        mRightIconView = (ImageView) findViewById(R.id.right_icon);

        if (isClickable()) {
            mRightIconView.setVisibility(View.VISIBLE);
        } else {
            mRightIconView.setVisibility(View.GONE);
        }
        mLeftTextView.setText(mLabelString);
        mLeftIconView.setImageResource(mIconDrawable);

    }


    public void setLeftText(int resId) {
        mLeftTextView.setText(resId);
    }

    public void setLeftIcon(int resId) {
        mLeftIconView.setImageResource(resId);
    }

    public void setRightText(String text) {
        mRightTextView.setText(text);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mRightIconView.setVisibility(View.VISIBLE);
    }
}
