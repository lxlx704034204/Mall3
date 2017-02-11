package com.hxqc.mall.core.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.ScreenUtil;


/**
 * 默认样式标题栏
 */
public class CustomToolBar extends Toolbar implements OnClickListener {

    private String title;
    private Context context;
    private View v;
    private ImageView leftImg, rightImg, rightImg2;
    private TextView mRightTv, mTitleTv;
    private TextView mLeftTv;
    private OnClickListener goBackListener;
    private int height;

    public CustomToolBar(Context context) {
        this(context, null);
    }

    public CustomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.view_topbar_def, null);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar);
        title = a.getString(R.styleable.CustomToolBar_toolbarTitle);
        a.recycle();
        height = ScreenUtil.dip2px(context, 55);
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height);
        mParams.gravity = Gravity.CENTER;
        addView(v, mParams);
        setContentInsetsAbsolute(0, 0);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).setSupportActionBar(this);
        }
        intiView();
    }

    public CustomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void intiView() {
        mTitleTv = (TextView) v.findViewById(R.id.topbar_title);
        leftImg = (ImageView) v.findViewById(R.id.topbar_left_img);
        if (!TextUtils.isEmpty(title))
            setTitle(title);
        else if (!TextUtils.isEmpty(getTitle().toString()))
            setTitle(getTitle().toString());


        leftImg.setOnClickListener(this);
    }

    public void setTitle(String str) {

        mTitleTv.setText(str);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.topbar_left_img) {
            if (goBackListener != null)
                goBackListener.onClick(v);
            else
                ((Activity) context).finish();
        }
    }

    public void setOnGoBackListener(OnClickListener goBackListener) {
        this.goBackListener = goBackListener;
    }

    public void setRightTitle(String str, OnClickListener listener) {
        mRightTv = (TextView) v.findViewById(R.id.topbar_right_tv);
        mRightTv.setText(str);
        mRightTv.setOnClickListener(listener);
    }


    public void setLeftText(String str, final OnClickListener listener) {
        mLeftTv = (TextView) v.findViewById(R.id.topbar_left_tv);
        mLeftTv.setText(str);
        mLeftTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftTv.setText("");
                if (listener != null)
                    listener.onClick(v);
            }
        });
    }

    public void showLeftImg(boolean b) {
        leftImg.setVisibility(b ? VISIBLE : GONE);
    }

}
