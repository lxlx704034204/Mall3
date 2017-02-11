package com.hxqc.mall.thirdshop.accessory4s.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductInfo4S;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.widget.SquareImageView;

/**
 * 说明:订单中的商品
 *
 * @author: 吕飞
 * @since: 2016-02-22
 * Copyright:恒信汽车电子商务有限公司
 */
public class ProductInOrder4S extends LinearLayout {
    RelativeLayout mHeadView;//套餐头部
    TextView mPackagePriceView;//套餐价格
    TextView mPackageNumView;//套餐数量
    RelativeLayout mProductInfoView;//商品信息
    SquareImageView mPhotoView;//商品图片
    TextView mNameView;//商品名字
    TextView mProductNumView;//商品数目
    TextView mProductPriceView;//商品价格
    View mBottomBlankView;//底部空白
    View mLineView;//底部线
    boolean mToProductDetail;
    ProductInfo4S mProductInfo;

    public ProductInOrder4S(Context context) {
        super(context);
    }

    public ProductInOrder4S(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_product_in_order, this);
        mHeadView = (RelativeLayout) findViewById(R.id.head);
        mPackagePriceView = (TextView) findViewById(R.id.package_price);
        mPackageNumView = (TextView) findViewById(R.id.package_num);
        mProductInfoView = (RelativeLayout) findViewById(R.id.product_info);
        mPhotoView = (SquareImageView) findViewById(R.id.photo);
        mNameView = (TextView) findViewById(R.id.name);
        mProductNumView = (TextView) findViewById(R.id.product_num);
        mProductPriceView = (TextView) findViewById(R.id.product_price);
        mBottomBlankView = findViewById(R.id.bottom_blank);
        mLineView = findViewById(R.id.line);
    }

    public void fillData(ProductInfo4S productInfo, boolean toProductDetail) {
        mProductInfo = productInfo;
        mToProductDetail = toProductDetail;
        initView();
    }

    private void initView() {
        OtherUtil.setVisible(mLineView, mProductInfo.isLastInPackage || !mProductInfo.isInPackage);
        OtherUtil.setVisible(mBottomBlankView, !mProductInfo.isInPackage || mProductInfo.isLastInPackage);
        OtherUtil.setVisible(mHeadView, mProductInfo.isFirstInPackage);
        mPackageNumView.setText(mProductInfo.getPackageNum());
        ImageUtil.setImageSquare(getContext(), mPhotoView, mProductInfo.smallPhoto);
        mNameView.setText(mProductInfo.name);
        mProductNumView.setText(mProductInfo.getProductNum());
        try {
            mPackagePriceView.setText(OtherUtil.amountFormat(mProductInfo.comboPrice, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProductPriceView.setText(OtherUtil.amountFormat(mProductInfo.price, true));
        if (mToProductDetail) {
            mProductInfoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcherAccessory4S.toProductDetail(getContext(), mProductInfo.productID, "");
                }
            });
        }
    }
}
