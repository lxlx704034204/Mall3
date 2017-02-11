package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;


/**
 * Function:第三方店铺搜索框
 *
 * @author 袁秉勇
 * @since 2015年12月23日
 */
public class TopSearchView extends LinearLayout implements View.OnClickListener {
    Context mContext;
    TextView mChangeSearchTypeView;
    EditText mSearchContentView;
    LinearLayout mSearchButtonView;

    public TopSearchView(Context context) {
        this(context, null);
    }

    public TopSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.third_filter_top_search_layout, this);

        mChangeSearchTypeView = (TextView) findViewById(R.id.change_search_type);

        mSearchContentView = (EditText) findViewById(R.id.search_tip);

        mSearchButtonView = (LinearLayout) findViewById(R.id.search);

        mChangeSearchTypeView.setOnClickListener(this);
        mSearchButtonView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
