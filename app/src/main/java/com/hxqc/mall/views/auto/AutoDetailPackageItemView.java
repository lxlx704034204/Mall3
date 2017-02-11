package com.hxqc.mall.views.auto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AccessoryPhoto;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2015-10-18
 * FIXME
 * Todo 套餐单项
 */
public class AutoDetailPackageItemView extends RelativeLayout {
    private ImageView imgCheck;//选中标记
    //    private Accessory mAccessory = null;//数据
    private AutoPackage mAutoPackage = null;
    private LinearLayout container;//背景
    private ImageView[] imgs;
    private TextView nameTextView;
    private TextView priceTextView;

    public AutoDetailPackageItemView(Context context) {
        this(context, null);
        initContext(context);
    }

    public AutoDetailPackageItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoDetailPackageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void initContext(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_auto_item, this);
        nameTextView = (TextView) findViewById(R.id.item_auto_package_name);
        priceTextView = (TextView) findViewById(R.id.item_auto_package_price);
        ImageView img0 = (ImageView) findViewById(R.id.auto_item_img0);
        ImageView img1 = (ImageView) findViewById(R.id.auto_item_img1);
        ImageView img2 = (ImageView) findViewById(R.id.auto_item_img2);
        ImageView img3 = (ImageView) findViewById(R.id.auto_item_img3);
        container = (LinearLayout) findViewById(R.id.auto_item_container);
        imgCheck = (ImageView) findViewById(R.id.auto_item_choose_img);
        imgs = new ImageView[]{img0, img1, img2, img3};
    }

    private void initUI(boolean isChoose) {
        chooseChangeUI(isChoose);
        ArrayList< Accessory > accessory = mAutoPackage.accessory;
        nameTextView.setText(mAutoPackage.title);
        float amount = mAutoPackage.getAmount();
        if (amount <= 0) {
            priceTextView.setVisibility(GONE);
        } else {
            priceTextView.setVisibility(VISIBLE);
            priceTextView.setText(OtherUtil.stringToMoney(amount));
        }


        int size = accessory.size() >= 4 ? 4 : accessory.size();
        for (int i = 0; i < size; i++) {
            ArrayList< AccessoryPhoto > photos = accessory.get(i).photo;
            if (photos.size() > 0) {
                String thumb = photos.get(0).mini;
                ImageUtil.setImage(getContext(), imgs[i], thumb);
                imgs[i].setVisibility(View.VISIBLE);


            }
        }
    }

    private void chooseChangeUI(boolean isChoose) {
        if (isChoose) {
            imgCheck.setVisibility(View.VISIBLE);
            container.setBackgroundResource(R.drawable.bg_auto_item_view_checked);
        } else {
            imgCheck.setVisibility(View.INVISIBLE);
            container.setBackgroundResource(R.drawable.bg_auto_item_view_normal);
        }
    }

    /**
     * 添加数据
     */
    public void setAutoPackage(AutoPackage autoPackage, AutoPackage choosePackage) {
        this.mAutoPackage = autoPackage;
        initUI(choosePackage != null);
    }


}
