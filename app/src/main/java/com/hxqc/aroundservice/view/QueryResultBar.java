package com.hxqc.aroundservice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 12
 * FIXME
 * Todo
 */
@Deprecated
public class QueryResultBar extends LinearLayout {

    private final TextView mIllegalView;
    private final TextView mMoneyView;
    private final TextView mPointView;

    public QueryResultBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_query_result, this);
        mIllegalView = (TextView) findViewById(R.id.query_result_illegal);
        mMoneyView = (TextView) findViewById(R.id.query_result_money);
        mPointView = (TextView) findViewById(R.id.query_result_point);
    }

    public void setText(CharSequence illegal,CharSequence money,CharSequence point) {
        mIllegalView.setText(illegal);
        mMoneyView.setText(money);
        mPointView.setText(point);
    }
}
