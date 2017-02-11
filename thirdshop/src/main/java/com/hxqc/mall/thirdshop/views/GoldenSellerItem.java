package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.GoldenSeller;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 金牌销售itemview
 */
public class GoldenSellerItem extends LinearLayout {
    private ImageView sellerIcon;
    private TextView sellerName, sellerPhoneNumber, sellerJobTitle;
    private LinearLayout sellerContainer;

    public void setGoldenSeller(GoldenSeller goldenSeller) {
        this.goldenSeller = goldenSeller;
        initData();
    }

    private GoldenSeller goldenSeller;

    public GoldenSellerItem(Context context) {
        this(context, null);
    }

    public GoldenSellerItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoldenSellerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_item_view_golden_sellers, this);
        sellerIcon = (ImageView) findViewById(R.id.seller_icon);
        sellerName = (TextView) findViewById(R.id.seller_name);
        sellerPhoneNumber = (TextView) findViewById(R.id.seller_phone_number);
        sellerJobTitle = (TextView) findViewById(R.id.seller_job_title);
        sellerContainer = (LinearLayout) findViewById(R.id.seller_container);
        initData();
    }

    public void setFullPadding() {
        int dimensionPixelSize_6 = getContext().getResources().getDimensionPixelSize(R.dimen.promotion_item_padding_6);
        int dimensionPixelSize_16 = getContext().getResources().getDimensionPixelSize(R.dimen.item_padding_16);
        sellerContainer.setPadding(dimensionPixelSize_16, dimensionPixelSize_6, dimensionPixelSize_16, dimensionPixelSize_6);
    }

    private void initData() {
        if (null != goldenSeller) {
            sellerJobTitle.setText(goldenSeller.sellerTitle);
            sellerName.setText(goldenSeller.sellerName);
            sellerPhoneNumber.setText(goldenSeller.sellerTel);
            ImageUtil.setImage(getContext(), sellerIcon, goldenSeller.sellerPhoto);
        }
    }
}
