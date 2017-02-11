package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.widget.GridViewNoSlide;

import java.util.Set;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/10
 * FIXME
 * Todo  自定义套餐选择
 */
public class AutoPackageViewCustom extends AutoPackageView implements View.OnClickListener {
    GridViewNoSlide mGridView;
    PackageCustomAdapter mCustomAdapter;

    public AutoPackageViewCustom(Context context) {
        super(context);
    }

    public AutoPackageViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_auto_package_custom, this);
        mGridView = (GridViewNoSlide) findViewById(R.id.package_custom_goods_grid);
        mCustomAdapter = new PackageCustomAdapter();
        mGridView.setAdapter(mCustomAdapter);
        findViewById(R.id.package_clear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoPackage.clearTempChooseAccessory();
                setPriceAmount(OtherUtil.stringToMoney(mAutoPackage.getCustomTempToalAmount()));
                //清空
                if (mAutoPackageWidgetCheckChangeListener != null)
                    mAutoPackageWidgetCheckChangeListener.onCheckedChanged(mAutoPackage, false);
            }
        });
    }


    @Override
    void setAutoPackage(AutoPackage autoPackage, AutoPackage choosePackage,
                        AutoPackageWidgetCheckChangeListener mAutoPackageWidgetCheckChangeListener) {
        super.setAutoPackage(autoPackage, choosePackage, mAutoPackageWidgetCheckChangeListener);
        mCustomAdapter.notifyDataSetChanged();
    }

//价格
//    setPriceAmount(OtherUtil.stringToMoney(mAutoPackage.getTotalAmount()));

    @Override
    public void onClick(View v) {
        boolean isChoose = ((CheckBox) v).isChecked();
        Accessory accessory = (Accessory) v.getTag();
        int mCheckCount = mAutoPackage.setChooseAccessor(accessory, isChoose);
        if (mCheckCount == 1) {
            mAutoPackageWidgetCheckChangeListener.onCheckedChanged(mAutoPackage, true);
        }
        if (mCheckCount <= 0) {
            mAutoPackageWidgetCheckChangeListener.onCheckedChanged(mAutoPackage, false);
        }
        setPriceAmount(OtherUtil.stringToMoney(mAutoPackage.getCustomTempToalAmount()));
    }

    class PackageCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mAutoPackage == null) return 0;
            return mAutoPackage.accessory == null ? 0 : mAutoPackage.accessory.size();
        }

        @Override
        public Accessory getItem(int position) {
            return mAutoPackage.accessory.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new AutoPackageViewCustomGoods(getContext());
            }
            ((AutoPackageViewCustomGoods) convertView).setValue(getItem(position));
            boolean isChoose;
            Set< Accessory > sets = AutoPackage.getTempChooseAccessory();

            isChoose = sets != null && sets.contains(getItem(position));

            ((AutoPackageViewCustomGoods) convertView).setAccessoryChoose(isChoose);
            ((AutoPackageViewCustomGoods) convertView).setChooseClickListener(AutoPackageViewCustom.this);
            return convertView;
        }
    }
}
