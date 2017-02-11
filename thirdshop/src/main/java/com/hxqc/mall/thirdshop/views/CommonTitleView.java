package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;


/**
 * Author:李烽
 * Date:2015-11-30
 * FIXME
 * Todo 店铺首页的几个列表的title
 */
public class CommonTitleView extends LinearLayout implements View.OnClickListener {
    private TextView titleTextVeiw;

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        invalidate();
    }

    private String titleText = "";

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        invalidate();
    }

    private boolean hasMore = true;//是否是可以点击查看更多全部

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private CommonTitleView.OnClickListener onClickListener;

    public CommonTitleView(Context context) {
        this(context, null);
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_common_title_view, this);
        titleTextVeiw = (TextView) findViewById(R.id.titleTextView);
        TextView rightText = (TextView) findViewById(R.id.right_text);
        ImageView rightIcon = (ImageView) findViewById(R.id.right_icon);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleView);
        titleText = typedArray.getString(R.styleable.CommonTitleView_title_text);
        hasMore = typedArray.getBoolean(R.styleable.CommonTitleView_hasMore, true);

        titleTextVeiw.setText(titleText);
        if (hasMore) {
            rightIcon.setVisibility(VISIBLE);
            rightText.setVisibility(VISIBLE);
        } else {
            rightIcon.setVisibility(INVISIBLE);
            rightText.setVisibility(INVISIBLE);
        }

//        rightIcon.setOnClickListener(this);
        rightText.setOnClickListener(this);
        findViewById(R.id.right_icon_container).setOnClickListener(this);
        typedArray.recycle();
    }

    public interface OnClickListener {
        void onClick(View view);
    }


    @Override
    public void onClick(View v) {
        if (null != onClickListener)
            onClickListener.onClick(this);
    }
}
