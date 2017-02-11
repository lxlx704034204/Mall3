package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;

import java.text.DecimalFormat;

/**
 * 说明:店铺用星级
 *
 * @author: 吕飞
 * @since: 2016-05-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShopRatingBar extends LinearLayout {
    RatingBar mStarView;
    TextView mScoreView;

    public ShopRatingBar(Context context) {
        super(context);
        initView();
    }

    public ShopRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_shop_rating_bar, this);
        mStarView = (RatingBar) findViewById(R.id.rating_star);
        mScoreView = (TextView) findViewById(R.id.rating_score);
    }

    public void setData(float score) {
        mScoreView.setText(new DecimalFormat("######0.00").format(score) + "分");
        float star = (float) ((int) (score / 0.5) * 0.5);
        mStarView.setRating(star);
    }
}
