package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.Promotion;


/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 最新促销活动
 */
@Deprecated
public class PromotionsItem extends LinearLayout {
    private TextView title, date;
    private Promotion promotion;

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
        initData();
    }

    public PromotionsItem(Context context) {
        this(context, null);
    }

    public PromotionsItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromotionsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_item_promotions, this);
        title = (TextView) findViewById(R.id.promotions_item_title);
        date = (TextView) findViewById(R.id.promotions_item_date);
        initData();
    }

    private void initData() {
        if (null != promotion) {
            title.setText(promotion.title);
            date.setText(promotion.publishDate);
        }
    }
}
