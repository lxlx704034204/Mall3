package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.views.dialog.PackageInfoDialog;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/10
 * FIXME
 * Todo  自定义套餐商品
 */
public class AutoPackageViewCustomGoods extends RelativeLayout {
    CheckBox mGoodsCheckView;
    TextView mTitleView;
    TextView mAmountView;
    ImageView mImageView;


    public AutoPackageViewCustomGoods(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_package_goods_custom, this);
        mGoodsCheckView = (CheckBox) findViewById(R.id.package_goods_check);
        mTitleView = (TextView) findViewById(R.id.tv_p_title);
        mAmountView = (TextView) findViewById(R.id.tv_p_amount);
        mImageView = (ImageView) findViewById(R.id.iv_p_img);
//        mGoodsCheckView.setOnClickListener(this);
    }

    public AutoPackageViewCustomGoods(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_package_goods_custom, this);
        mGoodsCheckView = (CheckBox) findViewById(R.id.package_goods_check);
//        mGoodsCheckView.setOnClickListener(this);
    }


    Accessory acc;

    public void setValue(Accessory accessory) {
        this.acc = accessory;
        mTitleView.setText(accessory.title);
        mAmountView.setText(String.format("%s x %d",
                OtherUtil.stringToMoney(accessory.price), accessory.count));
        if (accessory.photo != null && accessory.photo.size() > 0) {
            ImageUtil.setImage(getContext(), mImageView, accessory.photo.get(0).thumb);
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new PackageInfoDialog(getContext(), acc).show();
            }
        });
        mGoodsCheckView.setTag(acc);
    }

    public void setAccessoryChoose(boolean isChoose) {
        mGoodsCheckView.setChecked(isChoose);
    }

    public void setChooseClickListener(OnClickListener onClickListener) {
        mGoodsCheckView.setOnClickListener(onClickListener);
    }

}
