package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.CarDetail;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.util.DisplayTools;

/**
 * 车辆详情页 数据View(车辆大图 + 车型 + 价格)
 * Created by huangyi on 16/6/20.
 */
public class CarDetailTopView extends LinearLayout {

    ImageView mPhotoView, mPriceFlagView;
    TextView mCarNoView, mCountView, mCarView, mCarPriceView, mNewCarPriceView, mPurchasePriceView, mAllPriceView, mSavePriceView;
    RelativeLayout mPriceParentView;

    public CarDetailTopView(Context context) {
        this(context, null);
    }

    public CarDetailTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarDetailTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View root = LayoutInflater.from(context).inflate(R.layout.view_car_detail_top, this);
        mPhotoView = (ImageView) root.findViewById(R.id.top_photo);
        ViewGroup.LayoutParams params = mPhotoView.getLayoutParams();
        params.height = (int) (DisplayTools.getScreenWidth(context) / 1.75);
        mPhotoView.setLayoutParams(params);
        mCarNoView = (TextView) root.findViewById(R.id.top_car_no);
        mCountView = (TextView) root.findViewById(R.id.top_count);
        mCarView = (TextView) root.findViewById(R.id.top_car);
        mCarPriceView = (TextView) root.findViewById(R.id.top_car_price);
        mPriceFlagView = (ImageView) root.findViewById(R.id.top_car_price_flag);
        mPriceParentView = (RelativeLayout) root.findViewById(R.id.top_car_price_parent);
        mNewCarPriceView = (TextView) root.findViewById(R.id.top_new_car_price);
        mPurchasePriceView = (TextView) root.findViewById(R.id.top_purchase_price);
        mAllPriceView = (TextView) root.findViewById(R.id.top_new_car_all_price);
        mSavePriceView = (TextView) root.findViewById(R.id.top_save_price);
    }

    public void initData(final CarDetail detail) {
        ImageUtil.setImageSquare(getContext(), mPhotoView, detail.cover);
        mCarNoView.setText(String.format("车源编号: %s", detail.car_source_no));
        mCountView.setText(String.format("%d张", detail.image.size()));
        mCarView.setText(detail.car_name);
        mCarPriceView.setText(detail.getCarPrice());

        if (TextUtils.isEmpty(detail.new_car_price) || 0 == Float.valueOf(detail.new_car_price)) {
            mPriceFlagView.setVisibility(GONE);
            mPriceParentView.setVisibility(GONE);
        } else {
            mPriceFlagView.setVisibility(VISIBLE);
            mPriceParentView.setVisibility(VISIBLE);
            mNewCarPriceView.setText(detail.getNewCarPrice());
            mPurchasePriceView.setText(detail.getPurchasePrice());
            mAllPriceView.setText(detail.getAllPrice());
            mSavePriceView.setText(detail.getSavePrice());
        }
        mPhotoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != detail.image && detail.image.size() != 0) {
                    UsedCarActivitySwitcher.toImage(getContext(), detail.image);
                }
            }
        });
    }

    public void updatePrice(CarDetail detail) {
        mCarPriceView.setText(detail.getCarPrice());
        mSavePriceView.setText(detail.getSavePrice());
    }

}
