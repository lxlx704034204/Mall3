package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 最新促销活动
 */
public class PromotionsNormalItem extends LinearLayout {
    private ImageView pic;
    private TextView title, date;
    private SalesPModel promotion;

    public void setPromotion(SalesPModel promotion) {
        this.promotion = promotion;
        initData();
    }

    public PromotionsNormalItem(Context context) {
        this(context, null);
    }

    public PromotionsNormalItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromotionsNormalItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_item_first_promotions, this);
        pic = (ImageView) findViewById(R.id.promotions_item_pic);
        title = (TextView) findViewById(R.id.promotions_item_title);
        date = (TextView) findViewById(R.id.promotions_item_date);
        initData();
    }

    private void initData() {
        if (null != promotion) {
            ImageUtil.setImage(getContext(), pic, promotion.thumb);
            title.setText(promotion.title);
            date.setText(promotion.publishDate);
        }

    }
}
