package com.hxqc.mall.views.auto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.util.OtherUtil;

import hxqc.mall.R;


/**
 * Author: HuJunJie
 * Date: 2015-04-10
 * FIXME
 * Todo
 */
public class AutoPromotionInformationViewGroup extends LinearLayout {
    TextView mPriceView;//价格
    TextView mFallView;//降价
    TextView mSalesView;//订金
    TextView mInventoryView;//库存
    ImageView mSellOutView;

    View mInventoryLayout;

    public AutoPromotionInformationViewGroup(Context context) {
        super(context);
    }

    public AutoPromotionInformationViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_auto_promotion_detail_information, this);
        mPriceView = (TextView) findViewById(R.id.auto_detail_price);
        mFallView = (TextView) findViewById(R.id.auto_detail_fall);
        mSalesView = (TextView) findViewById(R.id.auto_detail_sales);
        mInventoryView = (TextView) findViewById(R.id.auto_detail_inventory);
        mSellOutView = (ImageView) findViewById(R.id.sellout_view);
        mInventoryLayout = findViewById(R.id.inventory_layout);
    }

    public void setInformation(AutoDetail autoDetail) {
        mPriceView.setText(String.format("¥%s", autoDetail.getItemPriceU()));
        mFallView.setText(autoDetail.getItemFallU());
        mSalesView.setText(String.format("¥%s", OtherUtil.amountFormat(autoDetail.getSubscription())));

        //剩余
        String store = autoDetail.getInventory();
        if (Integer.valueOf(store) <= 0) {
            mInventoryLayout.setVisibility(View.GONE);
            mSellOutView.setVisibility(View.VISIBLE);
        } else {
            mInventoryView.setText(String.format("%s辆",store));
        }
    }
}
