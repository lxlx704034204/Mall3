package com.hxqc.mall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.util.ImageUtil;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-06-17
 * FIXME
 * Todo
 */
@Deprecated
public class HotBrandChildView extends LinearLayout {
    ImageView mPriceView;
    TextView mNameView;

    public HotBrandChildView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_brand, this);
        mPriceView = (ImageView) findViewById(R.id.brand_thumb);
        mNameView = (TextView) findViewById(R.id.brand_name);
    }

    public HotBrandChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_brand, this);
        mPriceView = (ImageView) findViewById(R.id.brand_thumb);
        mNameView = (TextView) findViewById(R.id.brand_name);
    }

    public HotBrandChildView setBrand(Brand brand) {
        ImageUtil.setImage(getContext(), mPriceView, brand.brandThumb);
        mNameView.setText(brand.brandName);
        return this;
    }

    public ImageView getmPriceView() {
        return mPriceView;
    }

    public TextView getmNameView() {
        return mNameView;
    }
}
