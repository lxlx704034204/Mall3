package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.ScreenUtil;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 店铺地址
 */
public class ShopAddressView extends LinearLayout implements View.OnClickListener {
    private ImageView addressPhoto;
    private String photoUrl = "";


    public void addData(String photoUrl) {
        this.photoUrl = photoUrl;
        initData();

    }

    public ShopAddressView(Context context) {
        this(context, null);
    }

    public ShopAddressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShopAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_shop_address, this);
        addressPhoto = (ImageView) findViewById(R.id.shop_address_photo);
        ViewGroup.LayoutParams params = addressPhoto.getLayoutParams();
        int screenWidth = ScreenUtil.getScreenWidth(context);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.item_padding_16);
        int width = screenWidth - 2 * dimensionPixelSize;
        int height = width * 9 / 16;
        params.width = width;
        params.height = height;
        addressPhoto.setLayoutParams(params);
        addressPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        initData();
        findViewById(R.id.shop_address_location).setOnClickListener(this);
        findViewById(R.id.shop_address_navigation).setOnClickListener(this);
        addressPhoto.setOnClickListener(this);
    }

    private void initData() {
        try {
            ImageUtil.setImage(getContext(), addressPhoto, photoUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.shop_address_location) {
            if (null != onClickListener)
                onClickListener.onLocaClick();

        } else if (i == R.id.shop_address_navigation) {
            if (null != onClickListener)
                onClickListener.onNaviClick();

        } else if (i == R.id.shop_address_photo) {
            if (null != onClickListener)
                onClickListener.onMapClick();

        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onLocaClick();

        void onNaviClick();

        void onMapClick();
    }
}
