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
import com.hxqc.mall.thirdshop.model.FourSModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

/**
 * Author:李烽
 * Date:2016-05-06
 * FIXME
 * Todo 车型列表 item
 */
public class NewCarModelItem extends RelativeLayout {
    private ImageView autoImage;
    private TextView autoName;
    private TextView autoPriceRange;
    private TextView autoMsrp;

    public NewCarModelItem(Context context) {
        this(context, null);
    }

    public NewCarModelItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewCarModelItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_new_car_series, this);
        autoImage = (ImageView) findViewById(R.id.auto_image);
        autoName = (TextView) findViewById(R.id.auto_name);
        autoPriceRange = (TextView) findViewById(R.id.auto_price_range);
        autoMsrp = (TextView) findViewById(R.id.auto_msrp);
    }

    public void addData(final FourSModel fourSModel, final String areaID, final String series) {
        autoName.setText(fourSModel.modelName);
        ImageUtil.setImage(getContext(), autoImage, fourSModel.modelThumb);
        autoPriceRange.setText("￥" + OtherUtil.formatPriceRange(fourSModel.priceRange));
        autoMsrp.setText("￥" + OtherUtil.formatPriceRange(fourSModel.itemOrigPrice));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toShopInfoOfNewCarList(getContext(), areaID, series, fourSModel.modelName);
            }
        });
    }
}
