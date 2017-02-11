package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.hxqc.mall.core.views.LineTranslateAnimView;
import com.hxqc.mall.usedcar.R;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-11
 * Copyright:恒信汽车电子商务有限公司
 */
public class ValuationPriceDetailView extends LinearLayout implements RadioGroup.OnCheckedChangeListener {
    RadioGroup mPriceRadioGroupView;
    LineTranslateAnimView mLineAnimView;
    ValuationPriceItem mPrice1View;
    ValuationPriceItem mPrice2View;
    ValuationPriceItem mPrice3View;
    ValuationPriceItem mPrice4View;
    String[] mBuyPrice;
    String[] mSellPrice;

    public ValuationPriceDetailView(Context context) {
        super(context);
        initView();
    }

    public ValuationPriceDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_valuation_price_detail, this);
        mPriceRadioGroupView = (RadioGroup) findViewById(R.id.price_radio_group);
        mPriceRadioGroupView.setOnCheckedChangeListener(this);
        mLineAnimView = (LineTranslateAnimView) findViewById(R.id.line_anim);
        mPrice1View = (ValuationPriceItem) findViewById(R.id.price_1);
        mPrice2View = (ValuationPriceItem) findViewById(R.id.price_2);
        mPrice3View = (ValuationPriceItem) findViewById(R.id.price_3);
        mPrice4View = (ValuationPriceItem) findViewById(R.id.price_4);
    }

    public void setValuationPriceDetail(String[] buyPrice, String[] sellPrice) {
        mBuyPrice = buyPrice;
        mSellPrice = sellPrice;
        setBuyPrice();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.price_radio_buy) {
            setBuyPrice();
            mLineAnimView.startUnderlineAnim(0);
        }else {
            setSellPrice();
            mLineAnimView.startUnderlineAnim(1);
        }
    }

    private void setSellPrice() {
        mPrice1View.setPrice(mSellPrice[0]);
        mPrice2View.setPrice(mSellPrice[1]);
        mPrice3View.setPrice(mSellPrice[2]);
        mPrice4View.setPrice(mSellPrice[3]);
    }

    private void setBuyPrice() {
        mPrice1View.setPrice(mBuyPrice[0]);
        mPrice2View.setPrice(mBuyPrice[1]);
        mPrice3View.setPrice(mBuyPrice[2]);
        mPrice4View.setPrice(mBuyPrice[3]);
    }
}
