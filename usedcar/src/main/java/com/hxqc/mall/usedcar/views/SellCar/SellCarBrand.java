package com.hxqc.mall.usedcar.views.SellCar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.mall.usedcar.R;


/**
 * @Author : 吕飞
 * @Since : 2016-12-14
 * FIXME
 * Todo
 */
public class SellCarBrand extends RelativeLayout implements View.OnClickListener {
    EditTextValidatorView mBrandView;
    ImageView mAddBrandView;
    OnSellCarBrandClickListener mOnSellCarBrandClickListener;

    public SellCarBrand(Context context) {
        super(context);
    }

    public SellCarBrand(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_sell_car_brand, this);
        initView();
        initEvent();
    }
    public EditTextValidatorView getBrandView() {
        return mBrandView;
    }

    public void setOnSellCarBrandClickListener(OnSellCarBrandClickListener onSellCarBrandClickListener) {
        this.mOnSellCarBrandClickListener = onSellCarBrandClickListener;
    }

    private void initEvent() {
        mAddBrandView.setOnClickListener(this);
        mBrandView.setOnClickListener(this);
    }

    private void initView() {
        mAddBrandView = (ImageView) findViewById(R.id.add_brand);
        mBrandView = (EditTextValidatorView) findViewById(R.id.brand);
        mBrandView.setSingleLine(false);
        mBrandView.setMaxLines(2);
        mBrandView.addValidator(new VMallDivNotNull("请选择品牌车型", ""));
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if(i==R.id.add_brand){
            mOnSellCarBrandClickListener.addBrand();
        }else if(i==R.id.brand){
            mOnSellCarBrandClickListener.chooseBrand();
        }
    }

    public String getBrandString() {
        return mBrandView.getText().toString();
    }

    public void setBrandString(String text) {
        mBrandView.setText(text);
    }

    public interface OnSellCarBrandClickListener {
        void addBrand();
        void chooseBrand();
    }
}
