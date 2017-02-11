package com.hxqc.mall.usedcar.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.fragment.ChooseFragment;
import com.hxqc.mall.usedcar.model.ValuationArgument;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.mall.usedcar.views.UsedCarDatePicker;
import com.hxqc.util.DisplayTools;

import java.util.Calendar;

/**
 * 说明:车辆估价
 *
 * @author: 吕飞
 * @since: 2015-08-07
 * Copyright:恒信汽车电子商务有限公司
 */
public class AutoValuationActivity extends NoBackActivity implements ChooseFragment.ChooseValuationListener, View.OnClickListener {
    DrawerLayout mDrawerLayoutView;//侧滑菜单
    UsedCarSPHelper mUsedCarSPHelper;
    FrameLayout mRightView;//右边浮层
    ChooseFragment mChooseFragment;//浮层fragment
    MaterialEditText mBrandModelView;//选车栏
    MaterialEditText mLicensingDateView;//上牌日期栏
    MaterialEditText mRangeView;//里程栏
    MaterialEditText mCityView;//选车源所在地
    Button mStartView;//开始估价按钮
    int mMonth;//存储上牌月份
    int mYear;//存储上牌年份
    int mDay;//存储上牌日期
    String mBrandId;//储存品牌id
    String mSeriesId;//储存车系id
    String mModelId;//储存车型id
    String mValuationModelId;//储存估价车型id
    Toolbar mToolbar;//顶部toolbar，因为侧滑浮层要覆盖顶部，所以不能用actionbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_valuation);
        mUsedCarSPHelper = new UsedCarSPHelper(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.title_activity_auto_valuation);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        mBrandModelView = (MaterialEditText) findViewById(R.id.brand_model);
        mLicensingDateView = (MaterialEditText) findViewById(R.id.licensing_date);
        mRangeView = (MaterialEditText) findViewById(R.id.range);
        mCityView = (MaterialEditText) findViewById(R.id.city);
        mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightView = (FrameLayout) findViewById(R.id.right);
        mStartView = (Button) findViewById(R.id.start);
        mChooseFragment = new ChooseFragment();
        mChooseFragment.setType(ChooseFragment.VALUATION);
        mChooseFragment.setValuationListener(this);
        mBrandModelView.setOnClickListener(this);
        mLicensingDateView.setOnClickListener(this);
        mCityView.setOnClickListener(this);
        mStartView.setOnClickListener(this);
        initRight();
        initDate();
        OtherUtil.setDrawerMode(mDrawerLayoutView);
        if (!TextUtils.isEmpty(mUsedCarSPHelper.getCity())) {
            mCityView.setText(mUsedCarSPHelper.getCity());
        }
        OtherUtil.control2Decimal(mRangeView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mCityView.setText(data.getStringExtra(ChooseCityActivity.CHOSEN_CITY));
        }
    }

    //    浮层初始化
    @SuppressLint("RtlHardcoded")
    private void initRight() {
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 4 / 5, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.right, mChooseFragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.brand_model) {
            com.hxqc.mall.core.util.OtherUtil.closeSoftKeyBoard(this, v);
            mDrawerLayoutView.openDrawer(Gravity.RIGHT);

        } else if (i == R.id.licensing_date) {
            showDatePiker();

        } else if (i == R.id.city) {
            UsedCarActivitySwitcher.toChooseCityForResult(this, mUsedCarSPHelper.getCity());
        } else if (i == R.id.start) {
            if (canStart()) {
                UsedCarActivitySwitcher.toValuationResult(this, new ValuationArgument(mBrandId, mCityView.getText().toString().trim(), mLicensingDateView.getText().toString().trim(), mModelId, mRangeView.getText().toString().trim(), mSeriesId, mBrandModelView.getText().toString().trim(), mValuationModelId));
            }

        }
    }

    //日期选择器
    private void showDatePiker() {
        new UsedCarDatePicker(this, mYear, mMonth, mDay, UsedCarDatePicker.FIRST_ON_CARD) {
            @Override
            protected void doNext(int year, int monthOfYear, int dayOfMonth, String month, String day) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                mLicensingDateView.setText(mYear + "-" + month + "-" + day);
            }
        };
    }

    //判断是否能都进行估价
    private boolean canStart() {
        if (TextUtils.isEmpty(mBrandModelView.getText().toString().trim())) {
            ToastHelper.showYellowToast(this, getResources().getString(R.string.hint_no_brand_model));
            return false;
        } else if (TextUtils.isEmpty(mLicensingDateView.getText().toString().trim())) {
            ToastHelper.showYellowToast(this, getResources().getString(R.string.hint_no_licensing_date));
            return false;
        } else if (TextUtils.isEmpty(mRangeView.getText().toString().trim())) {
            ToastHelper.showYellowToast(this, getResources().getString(R.string.hint_no_range));
            return false;
        } else if (Float.parseFloat(mRangeView.getText().toString().trim()) >= 50) {
            ToastHelper.showYellowToast(this, getResources().getString(R.string.hint_range_too_large));
            return false;
        } else if (TextUtils.isEmpty(mCityView.getText().toString().trim())) {
            ToastHelper.showYellowToast(this, getResources().getString(R.string.hint_no_city));
            return false;
        }
        return true;
    }

    //初始化日期
    private void initDate() {
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mMonth = Calendar.getInstance().get(Calendar.MONTH);
        mYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayoutView.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayoutView.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void completeValuationChoose(String brand, String series, String model, String brandId, String seriesId, String modelId, String valuationModelId) {
        if (!brand.equals("")) {
            mBrandModelView.setText(String.format("%s %s", series, model));
            mBrandId = brandId;
            mSeriesId = seriesId;
            mModelId = modelId;
            mValuationModelId = valuationModelId;
        }
        mDrawerLayoutView.closeDrawer(Gravity.RIGHT);
        initRight();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        instance.finish();
//    }
}
