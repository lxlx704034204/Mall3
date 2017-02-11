package com.hxqc.mall.thirdshop.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.utils.OnScrollListenerHelper;

/**
 * Created by zhaofan
 */
public class AppearanceToolbar extends LinearLayout implements View.OnClickListener {

    private Context context;
    private TextView title;
    private ImageView goBack2;
    private ImageView mToHome2;
    private OnClickListener mOnHomeClick;

    public AppearanceToolbar(Context context) {
        this(context, null);
    }

    public AppearanceToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_appearancetoolbar, this);
        init();
    }

    private void init() {
        findViewById(R.id.goback).setOnClickListener(this);
        findViewById(R.id.to_home).setOnClickListener(this);
        goBack2 = (ImageView) findViewById(R.id.goback2);
        mToHome2 = (ImageView) findViewById(R.id.to_home2);
        title = (TextView) findViewById(R.id.title);
        title.setAlpha(0);
    }

    /**
     * @param hight 设置为0时  按第一个item的高度 设置透明度
     * @param lv
     */
    public void setAlphaAppearance(int hight, ListView lv) {
        OnScrollListenerHelper mOnScrollListenerHelper = new OnScrollListenerHelper(lv);
        mOnScrollListenerHelper.setOnToolbarAppearListener(hight, new OnScrollListenerHelper.OnToolbarAppearListener() {
            @Override
            public void OnToolbarAppear(int alpha) {
                title.setAlpha(alpha / 225f);
                goBack2.setVisibility(alpha > 0 ? GONE : VISIBLE);
                mToHome2.setVisibility(alpha > 0 ? GONE : VISIBLE);
                //  goBack2.setAlpha(1 - alpha / 225f);
            }
        });
    }


    public void setOnHomeClick(OnClickListener mOnHomeClick) {
        this.mOnHomeClick = mOnHomeClick;
    }


    public void setTitle(String mTitle) {
        title.setText(mTitle);
    }

    public TextView getTitle() {
        return title;
    }

    public void showToolbar() {
        title.setAlpha(1);
        goBack2.setVisibility(GONE);
        mToHome2.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.goback) {
            ((Activity) context).finish();
        } else if (i == R.id.to_home) {
            //  ActivitySwitchBase.toMain(AskLeastMoneyActivity2.this, 0);
            if (mOnHomeClick != null)
                mOnHomeClick.onClick(v);
        }
    }
}