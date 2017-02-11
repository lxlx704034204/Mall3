package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;

/**
 * Author: wanghao
 * Date: 2015-12-01
 * FIXME 促销详情 价格变化表 块
 * Todo
 */
public class SalesDetailPricePieceView extends RelativeLayout {

    public TextView mLabelView;
    public TextView mValueView;
    public ImageView mFunctionImg;

    public SalesDetailPricePieceView(Context context) {
        super(context);
        initView();
    }

    public SalesDetailPricePieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.t_piece_sales_detail_price_view, this);
        mLabelView = (TextView) findViewById(R.id.tv_s_detail_price_piece_label);
        mValueView = (TextView) findViewById(R.id.tv_s_detail_price_piece_value);
        mFunctionImg = (ImageView) findViewById(R.id.iv_s_detail_price_piece_img);
    }

    public void setValues(String label, String value, boolean isShowImg, int res) {
        mLabelView.setText(label);
        mValueView.setText(value);
        if (isShowImg) {
            mFunctionImg.setVisibility(VISIBLE);
            ImageUtil.setImage(getContext(), mFunctionImg, res);
        } else {
            mFunctionImg.setVisibility(GONE);
        }
    }

    public void setValueColor(int color) {
        mValueView.setTextColor(this.getResources().getColor(color));
    }
}
