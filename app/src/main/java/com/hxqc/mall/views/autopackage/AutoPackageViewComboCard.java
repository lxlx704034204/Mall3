package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.OtherUtil;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-10-09
 * FIXME
 * Todo
 */
public class AutoPackageViewComboCard extends AutoPackageView implements View.OnClickListener {


    RelativeLayout clickView;
    CheckBox mGoGoGoView;
    TextView mComboOldAmountView;//原价
    RecyclerView mItemsRecycleView;
    AutoPackageViewComboCardHolderAdapter adapter;

    public AutoPackageViewComboCard(Context context) {
        super(context);
        mComboOldAmountView = (TextView) findViewById(R.id.tv_combo_old_amount);
        mComboOldAmountView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public AutoPackageViewComboCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mComboOldAmountView = (TextView) findViewById(R.id.tv_combo_old_amount);
        mComboOldAmountView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_auto_package_card, this);
        clickView = (RelativeLayout) findViewById(R.id.ll_click_item);
        mGoGoGoView = (CheckBox) findViewById(R.id.v_gogogogo);
        mGoGoGoView.setOnClickListener(this);
        clickView.setOnClickListener(this);

        mItemsRecycleView = (RecyclerView) findViewById(R.id.rlv_item_combo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mItemsRecycleView.setLayoutManager(layoutManager);
        mItemsRecycleView.setHasFixedSize(true);
        mItemsRecycleView.setItemAnimator(new DefaultItemAnimator());
//        mItemsRecycleView.addItemDecoration(new DividerItemDecoration(
//                getContext(), DividerItemDecoration.HORIZONTAL_LIST));
        adapter = new AutoPackageViewComboCardHolderAdapter();
        mItemsRecycleView.setAdapter(adapter);
    }

    @Override
    void setAutoPackage(AutoPackage autoPackage, AutoPackage choosePackage, AutoPackageWidgetCheckChangeListener mAutoPackageWidgetCheckChangeListener) {
        super.setAutoPackage(autoPackage, choosePackage, mAutoPackageWidgetCheckChangeListener);
        adapter.setAccessories(autoPackage.accessory);
        mComboOldAmountView.setText(String.format("总价:%s", OtherUtil.stringToMoney(mAutoPackage.getTotalAmount())));
        mGoGoGoView.setChecked(choosePackage != null);
    }

    @Override
    public void onClick(View v) {
        if (mAutoPackageWidgetCheckChangeListener == null) return;
        if (v.getId() == R.id.ll_click_item ) {
            mGoGoGoView.setChecked(!mGoGoGoView.isChecked());
        }
        mAutoPackageWidgetCheckChangeListener.onCheckedChanged(mAutoPackage, mGoGoGoView.isChecked());
    }

}
