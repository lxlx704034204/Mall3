package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.CatalogTipView;

import java.util.Iterator;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/12
 * FIXME
 * Todo  套餐确认
 */
public class AutoPackageVerify extends LinearLayout implements View.OnClickListener {
    TextView mTitleView;
    TextView mAmountView;
    LinearLayoutCompat mGoodsLayout;

    //    TextView mChooseView;// 选择按钮
    TextView mChangeView;// 修改按钮
    View mPackageLayout;//套餐内容

    public AutoPackageVerify(Context context) {
        super(context);
    }

    public AutoPackageVerify(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_auto_verify_package, this);
        mTitleView = (TextView) findViewById(R.id.package_title);
        mAmountView = (TextView) findViewById(R.id.package_amount);
        mGoodsLayout = (LinearLayoutCompat) findViewById(R.id.package_goods_detail);
        mChangeView = (TextView) findViewById(R.id.verify_package_change);
        mPackageLayout = findViewById(R.id.package_layout);
        this.setOnClickListener(this);
    }

    //    /**
//     * 是否显示 修改按钮
//     * @param isShow
//     */
    private void showPackageView(boolean isShow) {
        if (isShow) {
//            mChangeView.setVisibility(View.VISIBLE);
            mPackageLayout.setVisibility(View.VISIBLE);
            mGoodsLayout.setVisibility(View.VISIBLE);
        } else {
//            mChangeView.setVisibility(View.INVISIBLE);
            mPackageLayout.setVisibility(View.GONE);
            mGoodsLayout.setVisibility(View.GONE);
            mGoodsLayout.removeAllViews();
        }
    }

    /**
     * 是否显示用品
     *
     * @param isShow
     */
    public void showPackage(boolean isShow) {
        if (isShow) {
            this.setVisibility(View.VISIBLE);
            return;
        }
        this.setVisibility(View.GONE);
    }

    public void refreshPackageValue(AutoPackage autoPackage) {
        if (autoPackage == null) {
            showPackageView(false);
            return;
        }
        showPackageView(true);
        mTitleView.setText(autoPackage.title);
        mAmountView.setText(OtherUtil.stringToMoney(autoPackage.getAmount()));
        mGoodsLayout.removeAllViews();

        CatalogTipView catalogTipView;
        Iterator accessoryIterator = autoPackage.getChooseAccessory();
        if (accessoryIterator == null) return;
        while (accessoryIterator.hasNext()) {
            Accessory as = (Accessory) accessoryIterator.next();
            catalogTipView = new CatalogTipView(getContext());
            catalogTipView.setLeftTextView(as.title);
            catalogTipView.setRightTextView(String.format("%s x %d", OtherUtil.stringToMoney(as.price), as.count));
            mGoodsLayout.addView(catalogTipView);
        }
    }

    @Override
    public void onClick(View view) {
        ActivitySwitcher.toAutoPackageChoose(getContext(), -1);
    }
}
