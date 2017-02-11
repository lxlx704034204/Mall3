package com.hxqc.mall.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Author:李烽
 * Date:2016-03-18
 * FIXME
 * Todo 标题内容组合
 */
public class MapItem extends LinearLayout {
    private TextView titleTextView;
    private String title;

    public MapItem(Context context) {
        this(context, null);
    }

    public MapItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_item_map, this);
        titleTextView = (TextView) findViewById(R.id.map_item_title);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
