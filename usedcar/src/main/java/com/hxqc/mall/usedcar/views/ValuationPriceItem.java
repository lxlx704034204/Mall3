package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-11
 * Copyright:恒信汽车电子商务有限公司
 */
public class ValuationPriceItem extends LinearLayout {
    TextView mItemTitleView;
    String mItemTitle;
    TextView mItemPriceView;

    public ValuationPriceItem(Context context) {
        super(context);
        initView();
    }

    public ValuationPriceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ValuationPriceItem);
        mItemTitle = typedArray.getString(R.styleable.ValuationPriceItem_valuationPriceItemTitle);
        typedArray.recycle();
        mItemTitleView.setText(mItemTitle);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_valuation_price_item, this);
        mItemTitleView = (TextView) findViewById(R.id.item_title);
        mItemPriceView = (TextView) findViewById(R.id.item_price);
    }

    public void setPrice(String price) {
        mItemPriceView.setText(price);
    }
}
