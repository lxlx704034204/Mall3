package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.FourSSeries;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

/**
 * Author:李烽
 * Date:2016-05-06
 * FIXME
 * Todo 车型列表 item
 */
public class NewCarSeriesItem extends RelativeLayout {
    private ImageView autoImage;
    private TextView autoName;
    private TextView autoPriceRange;
    private TextView autoMsrp;

    public NewCarSeriesItem(Context context) {
        this(context, null);
    }

    public NewCarSeriesItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewCarSeriesItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_new_car_series, this);
        autoImage = (ImageView) findViewById(R.id.auto_image);
        autoName = (TextView) findViewById(R.id.auto_name);
        autoPriceRange = (TextView) findViewById(R.id.auto_price_range);
        autoMsrp = (TextView) findViewById(R.id.auto_msrp);
    }

    public void addData(final FourSSeries fourSSeries, final String siteID) {
        autoName.setText(String.format("%s系列车型", fourSSeries.seriesName));
        ImageUtil.setImage(getContext(), autoImage, fourSSeries.seriesThumb);
        autoPriceRange.setText(String.format("¥%s", OtherUtil.formatPriceRange(fourSSeries.priceRange)));
        autoMsrp.setText(String.format("¥%s", OtherUtil.formatPriceRange(fourSSeries.itemOrigPrice)));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toNewCarModelList2(getContext(),siteID,fourSSeries.brand, fourSSeries.getSeriesName());
            }
        });
    }
}
