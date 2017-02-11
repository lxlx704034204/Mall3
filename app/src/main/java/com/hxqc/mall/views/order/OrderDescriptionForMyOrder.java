package com.hxqc.mall.views.order;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.model.order.BaseOrder;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.ColorSquare;

import hxqc.mall.R;

/**
 * 说明:列表中订单的描述
 *
 * author: 吕飞
 * since: 2015-10-08
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderDescriptionForMyOrder extends RelativeLayout {
    TextView mPackagesView;
    BaseOrder mBaseOrder;
    ImageView mAutoImageView;
    TextView mAutoModelView;
    TextView mAutoPriceView;
    TextView mAutoNumView;
    TextView mInteriorTipText;
    RelativeLayout mDescriptionView;//整个展示区
    ColorSquare mAppearanceView;//车身颜色
    ColorSquare mInteriorView;//内饰颜色

    public OrderDescriptionForMyOrder(Context context) {
        super(context);
    }

    public OrderDescriptionForMyOrder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_order_description_for_my_order, this);
        mPackagesView = (TextView) findViewById(R.id.packages);
        mAutoImageView = (ImageView) findViewById(R.id.auto_image);
        mAutoModelView = (TextView) findViewById(R.id.auto_model);
        mAutoPriceView = (TextView) findViewById(R.id.auto_price);
        mAutoNumView = (TextView) findViewById(R.id.auto_num);
        mAppearanceView = (ColorSquare) findViewById(R.id.color_square_appearance);
        mInteriorView = (ColorSquare) findViewById(R.id.color_square_interior);
        mInteriorTipText = (TextView) findViewById(R.id.interior_tip_text);
        mDescriptionView = (RelativeLayout) findViewById(R.id.description);
    }

    public void initOrderDescription(BaseOrder mBaseOrder) {
        this.mBaseOrder = mBaseOrder;
        showOrderDescription();
    }

    private void showOrderDescription() {
        ImageUtil.setImage(getContext(), mAutoImageView, mBaseOrder.itemThumb);
        mAutoModelView.setText(mBaseOrder.getItemName());
        mAutoPriceView.setText(mBaseOrder.getItemPrice());
        mAppearanceView.setColors(mBaseOrder.getItemColor());
        if (mBaseOrder.getItemInterior().length != 0 && !TextUtils.isEmpty(mBaseOrder.getItemInterior()[0])) {
            mInteriorView.setColors(mBaseOrder.getItemInterior());
            mInteriorTipText.setVisibility(VISIBLE);
        } else {
            mInteriorTipText.setVisibility(GONE);
        }
        /**
         * @update  2015年10月30日 yuanbingyong
         * 不显示购买数量
         */
//        mAutoNumView.setText("×" + mBaseOrder.itemCount);
        if (mBaseOrder.packages != null && mBaseOrder.packages.size() > 0 && !mBaseOrder.getPackages().get(0).packageID.equals(AutoPackage.PACKAGE_CUSTOM)) {
            mPackagesView.setText(mBaseOrder.getPackages().get(0).title);
            mPackagesView.setVisibility(VISIBLE);
        } else {
            mPackagesView.setVisibility(GONE);
        }
    }
}
