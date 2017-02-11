package com.hxqc.newenergy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2016/5/25
 * FIXME
 * Todo
 */
public class EvHomeModuleView extends LinearLayout {
    LinearLayout mContentView;
    TextView mTitleView;

    public EvHomeModuleView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_ev_home_module_layout, this);
        mContentView = (LinearLayout) findViewById(R.id.ev_home_module_content_layout);
        mTitleView = (TextView) findViewById(R.id.ev_home_module_title);
    }

    public EvHomeModuleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EvHomeModuleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
//        super.setOnClickListener(l);
        findViewById(R.id.ev_home_module_more).setVisibility(View.VISIBLE);
        findViewById(R.id.ev_home_module_more).setOnClickListener(l);
    }

    public LinearLayout getContentView() {
        return mContentView;
    }

    public void setModuleTitle(String title) {
        mTitleView.setText(title);
    }

}
