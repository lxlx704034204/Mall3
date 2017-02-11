package com.hxqc.mall.usedcar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.MonitorScrollView;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.ValuationArgument;
import com.hxqc.mall.usedcar.model.ValuationResult;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.views.FuturePriceView;
import com.hxqc.mall.usedcar.views.ValuationPriceDetailView;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 说明:估价结果
 *
 * @author: 吕飞
 * @since: 2016-05-11
 * Copyright:恒信汽车电子商务有限公司
 */
public class ValuationResultActivity extends NoBackActivity implements View.OnClickListener, MonitorScrollView.ScrollViewListener {
    Toolbar mToolbar;
    CircleImageView mBrandLogoView;
    TextView mCarNameView;
    TextView cityRangeAgeView;
    TextView mValuationPriceView;
    ValuationPriceDetailView mValuationPriceDetailView;
    FuturePriceView mFuturePriceView;
    FuturePriceView mFutureSalvageView;
    TextView mBuyCarView;
    TextView mSellCarView;
    ValuationArgument mValuationArgument;
    ValuationResult mValuationResult;
    MonitorScrollView mScrollView;
    LinearLayout mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValuationArgument = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(UsedCarActivitySwitcher.VALUATION_ARGUMENT);
        setContentView(R.layout.activity_valuation_result);
        initView();
        getPrice();
        fillData();
    }

    private void fillData() {
        mCarNameView.setText(mValuationArgument.title);
        cityRangeAgeView.setText(mValuationArgument.city + "\n"  + OtherUtil.formatRange(mValuationArgument.range) + "万公里/"+ mValuationArgument.licensing_date.substring(0, 4));
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.title_activity_valuation_result);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        mScrollView = (MonitorScrollView) findViewById(R.id.scroll_view);
        mResultView = (LinearLayout) findViewById(R.id.result);
        mBrandLogoView = (CircleImageView) findViewById(R.id.brand_logo);
        mCarNameView = (TextView) findViewById(R.id.car_name);
        cityRangeAgeView = (TextView) findViewById(R.id.city_range_age);
        mValuationPriceView = (TextView) findViewById(R.id.valuation_price);
        mValuationPriceDetailView = (ValuationPriceDetailView) findViewById(R.id.valuation_price_detail);
        mFuturePriceView = (FuturePriceView) findViewById(R.id.future_price);
        mFutureSalvageView = (FuturePriceView) findViewById(R.id.future_salvage);
        mBuyCarView = (TextView) findViewById(R.id.buy_car);
        mSellCarView = (TextView) findViewById(R.id.sell_car);
        mScrollView.setHeight(DisplayTools.dip2px(this, 214));
        mScrollView.setScrollViewListener(mToolbar,this);
        mBuyCarView.setOnClickListener(this);
        mSellCarView.setOnClickListener(this);
    }

    private void getPrice() {
        new UsedCarApiClient().getValuation(mValuationArgument.brandId, mValuationArgument.seriesId, mValuationArgument.modelId, mValuationArgument.city, OtherUtil.formatDate(mValuationArgument.licensing_date), mValuationArgument.range, mValuationArgument.valuationModelId,
                new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onSuccess(String response) {
                        mValuationResult = JSONUtils.fromJson(response, ValuationResult.class);
                        if (mValuationResult != null) {
                            setData();
                        }
                    }
                });
    }

    private void setData() {
        mResultView.setVisibility(View.VISIBLE);
        com.hxqc.mall.core.util.ImageUtil.setImageSquare(this, mBrandLogoView, mValuationResult.brand_logo);
        mValuationPriceView.setText(com.hxqc.mall.core.util.OtherUtil.amountFormat(mValuationResult.estimate_price, true) + "万");
        String[] a1 = mValuationResult.buy_price.split(",");
        String[] s1 = new String[]{
                a1[0] + "万", a1[1] + "万", a1[2] + "万", a1[3] + "万"
        };
        String[] a2 = mValuationResult.sell_price.split(",");
        mValuationArgument.new_car_price = a2[3];
        String[] s2 = new String[]{
                a2[0] + "万", a2[1] + "万", a2[2] + "万", a2[3] + "万"
        };
        mValuationPriceDetailView.setValuationPriceDetail(s1, s2);
        String[] b1 = mValuationResult.future_price.split(",");
        float[] f1 = new float[]{
                Float.parseFloat(b1[0]), Float.parseFloat(b1[1]), Float.parseFloat(b1[2]), Float.parseFloat(b1[3]), Float.parseFloat(b1[4])
        };
        mFuturePriceView.setChartData(f1, Float.parseFloat(a2[3]));
        String[] b2 = mValuationResult.future_salvage.split(",");
        float[] f2 = new float[]{
                Float.parseFloat(b2[0]), Float.parseFloat(b2[1]), Float.parseFloat(b2[2]), Float.parseFloat(b2[3]), Float.parseFloat(b2[4])
        };
        mFutureSalvageView.setChartData(f2, 100f);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buy_car) {
            UsedCarActivitySwitcher.toBuyCarFromHome(this);
        } else if (i == R.id.sell_car) {
            if (mValuationResult.match.equals("0")) {
                UsedCarActivitySwitcher.toNewSellCar(this);
            } else {
                UsedCarActivitySwitcher.toNewSellCar(this, mValuationArgument);
            }
        }
    }

    @Override
    public void onScrollChange(float f1) {
        mToolbar.getBackground().setAlpha((int) (f1 * 255));
    }

    @Override
    public void moveDown() {

    }

    @Override
    public void moveUp() {

    }

}
