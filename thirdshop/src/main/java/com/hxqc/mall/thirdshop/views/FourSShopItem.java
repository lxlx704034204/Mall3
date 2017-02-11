package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.FourSShop;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSShopItem extends LinearLayout {
    ImageView mShopImageView;
    ImageView mBrandImageView;
    TextView mShopNameView;
    TextView mShopBrandView;
    TextView mShopContent1View;
    TextView mShopContent2View;
    TextView mShopContent3View;

    public FourSShopItem(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_4s_shop_item, this);
        mShopImageView = (ImageView) findViewById(R.id.shop_image);
        mBrandImageView = (ImageView) findViewById(R.id.brand_image);
        mShopNameView = (TextView) findViewById(R.id.shop_name);
        mShopBrandView = (TextView) findViewById(R.id.shop_brand);
        mShopContent1View = (TextView) findViewById(R.id.shop_content_1);
        mShopContent2View = (TextView) findViewById(R.id.shop_content_2);
        mShopContent3View = (TextView) findViewById(R.id.shop_content_3);
    }

    public FourSShopItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void addData(final FourSShop fourSShop) {
        ImageUtil.setImage(getContext(), mShopImageView, fourSShop.shopPhoto);
        ImageUtil.setImageNormalSize(getContext(), mBrandImageView, fourSShop.brandThumb);
//        这里不能用：OtherUtil.setImage(getContext(), mBrandImageView, fourSShop.brandThumb);
        mShopNameView.setText(fourSShop.shopTitle);
        mShopBrandView.setText(fourSShop.manageBrand);
        try {
            mShopContent1View.setText(fourSShop.promotionList.get(0).title);
        } catch (IndexOutOfBoundsException e) {
            mShopContent1View.setText("");
        }
        try {
            mShopContent2View.setText(fourSShop.promotionList.get(1).title);
        } catch (IndexOutOfBoundsException e) {
            mShopContent2View.setText("");
        }
        try {
            mShopContent3View.setText(fourSShop.promotionList.get(2).title);
        } catch (IndexOutOfBoundsException e) {
            mShopContent3View.setText("");
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toShopHome(fourSShop.shopID, getContext());
            }
        });
    }
}
