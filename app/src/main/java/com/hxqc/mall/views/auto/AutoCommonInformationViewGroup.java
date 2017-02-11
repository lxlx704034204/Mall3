package com.hxqc.mall.views.auto;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.views.dialog.PriceCutDialog;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-10
 * FIXME
 * Todo  普通车辆 头部基本信息
 */
public class AutoCommonInformationViewGroup extends LinearLayout implements View.OnClickListener {
    TextView mPriceView;//现价
    TextView mFallView;//降价
    TextView mSalesView;//销量
    TextView mInventoryView;//库存
    TextView mOriginalPriceView;//原价
    TextView mSubscriptionView;//订金

    public AutoCommonInformationViewGroup(Context context) {
        super(context);
    }

    public AutoCommonInformationViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_auto_detail_common_information, this);
        mPriceView = (TextView) findViewById(R.id.auto_detail_price);
        mFallView = (TextView) findViewById(R.id.auto_detail_fall);
        mSalesView = (TextView) findViewById(R.id.auto_detail_sales);
        mInventoryView = (TextView) findViewById(R.id.auto_detail_inventory);
        mOriginalPriceView = (TextView) findViewById(R.id.auto_detail_original_price);
        mOriginalPriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        findViewById(R.id.auto_detail_price_cut_notification).setOnClickListener(this);
        mSubscriptionView = (TextView) findViewById(R.id.auto_detail_subscription);
    }

    AutoDetail autoDetail;

    public void setInformation(AutoDetail autoDetail) {
        mPriceView.setText(String.format("¥%s", autoDetail.getItemPriceU()));
        mFallView.setText(autoDetail.getItemFallU());
        mSalesView.setText(String.format("%s辆", autoDetail.getItemSales()));
        mInventoryView.setText(Integer.valueOf(autoDetail.getInventory()) > 0 ? "有货" : "缺货");
        mOriginalPriceView.setText( autoDetail.getItemOriginalPrice());
        mSubscriptionView.setText(String.format("订金: %s", OtherUtil.amountFormat(autoDetail.getSubscription())));
        this.autoDetail = autoDetail;
    }

    /**
     * 降价通知
     */
    public void clickPriceCutNotification() {
        new PriceCutDialog(getContext(), autoDetail).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auto_detail_price_cut_notification:
                //降价通知
                clickPriceCutNotification();
                break;
        }
    }
}
