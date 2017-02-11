package com.hxqc.mall.core.views.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Author:李烽
 * Date:2016-04-19
 * FIXME
 * Todo 底部
 */
public class MFooter extends VRecyclerViewFooter {
    private TextView mTextView;
    private RelativeLayout mContainerView;
    private ProgressWheel mProgressWheel;

    public MFooter(Context context) {
        this(context, null);
    }

    public MFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_v_recycler_view_footer, this);
        root_layout = findViewById(R.id.root_layout);
        mContainerView = (RelativeLayout) findViewById(R.id.footer_content);
        mProgressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        mTextView = (TextView) findViewById(R.id.footer_hint_textview);
    }

    @Override
    public int getViewHeight() {
        LayoutParams layoutParams = (LayoutParams) mContainerView.getLayoutParams();
        return layoutParams.height;
    }

    @Override
    protected View getRootLayout() {
        if (root_layout == null)
            root_layout = findViewById(R.id.root_layout);
        return root_layout;
    }

    @Override
    protected void onStateNoMore() {
        mProgressWheel.setVisibility(INVISIBLE);
        mTextView.setVisibility(VISIBLE);
        mTextView.setText("暂无更多");
    }

    @Override
    protected void onStateLoading() {
        mProgressWheel.setVisibility(VISIBLE);
        mTextView.setVisibility(INVISIBLE);
    }

    @Override
    protected void onStateReady() {
        mProgressWheel.setVisibility(INVISIBLE);
        mTextView.setVisibility(VISIBLE);
        mTextView.setText("松开加载");
    }

    @Override
    protected void onStateNormal() {
        mProgressWheel.setVisibility(INVISIBLE);
        mTextView.setVisibility(VISIBLE);
        mTextView.setText("上拉加载更多");
    }
}
