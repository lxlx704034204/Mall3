package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.OtherUtil;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/11
 * FIXME
 * Todo
 */
public abstract class AutoPackageView extends LinearLayout {
    AutoPackage mAutoPackage;
    TextView mComboTitleView;
    TextView mComboPriceView;//活动价
    AutoPackage mChoosePackage;

    public interface AutoPackageWidgetCheckChangeListener {
        void onCheckedChanged(AutoPackage autoPackage, boolean isChecked);
    }

    AutoPackageWidgetCheckChangeListener mAutoPackageWidgetCheckChangeListener;

    public AutoPackageView(Context context) {
        super(context);
        initView();
        mComboTitleView = (TextView) findViewById(R.id.tv_combo_title);
        mComboPriceView = (TextView) findViewById(R.id.tv_combo_price);

    }

    public AutoPackageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        mComboTitleView = (TextView) findViewById(R.id.tv_combo_title);
        mComboPriceView = (TextView) findViewById(R.id.tv_combo_price);

    }

    abstract void initView();

    void setAutoPackage(AutoPackage autoPackage, AutoPackage choosePackage,
                        AutoPackageWidgetCheckChangeListener mAutoPackageWidgetCheckChangeListener) {
        this.mAutoPackage = autoPackage;
        mComboTitleView.setText(mAutoPackage.title);
        this.mAutoPackageWidgetCheckChangeListener = mAutoPackageWidgetCheckChangeListener;
        this.mChoosePackage = autoPackage;
      switch (autoPackage.isCustomPackage()){
          case combo:
              setPriceAmount(OtherUtil.stringToMoney(autoPackage.getAmount()));
              break;
          case custom:
              setPriceAmount(OtherUtil.stringToMoney(autoPackage.getCustomTempToalAmount()));
              break;
      }


    }

    protected void setPriceAmount(String amount) {
        mComboPriceView.setText(amount);
    }


}
