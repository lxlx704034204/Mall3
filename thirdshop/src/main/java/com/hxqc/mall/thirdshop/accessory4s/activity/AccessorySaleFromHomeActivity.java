package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.fragment.AccessorySale4SFragment;
import com.hxqc.mall.thirdshop.accessory4s.views.FilterTipFromHomeView;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;

import java.util.LinkedList;

/**
 * 用品销售 首页版
 * Created by huangyi on 16/2/24.
 */
public class AccessorySaleFromHomeActivity extends NoBackActivity implements View.OnClickListener, FilterTipFromHomeView.OnFilterClickListener {

    /**
     * 品牌名, 车系名, 店铺id, 一级分类id, 二级分类id, 价格升降序(asc升序 desc降序)
     **/
    public String brand = "", series = "", shopID = "", class1stID = "", class2ndID = "", priceOrder = "";
    public BaseSharedPreferencesHelper helper;
    Toolbar mToolbar;
    TextView mCityView;
    FilterTipFromHomeView mTipView;
    AccessorySale4SFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_sale_from_home);

        helper = new BaseSharedPreferencesHelper(this);
        mToolbar = (Toolbar) findViewById(R.id.accessory_sale_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCityView = (TextView) findViewById(R.id.accessory_sale_city);
        mCityView.setOnClickListener(this);
        mTipView = (FilterTipFromHomeView) findViewById(R.id.accessory_sale_tip);
        mTipView.setOnFilterClickListener(this);
        mTipView.setShadeView(findViewById(R.id.accessory_sale_shade));
        mTipView.initData(AreaSiteUtil.getInstance(this).getSiteID(), "",
                helper.getLatitudeBD(), helper.getLongitudeBD());
        mFragment = new AccessorySale4SFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.accessory_sale_list, mFragment).commit();

        initLocationData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            String position = data.getStringExtra("position");
            if (!TextUtils.isEmpty(position)) {
                if (position.equals(mCityView.getText().toString())) {
                    return;
                } else {
                    mCityView.setText(position);
                    //好像没有改变数据
                    mTipView.initData(AreaSiteUtil.getInstance(this).getSiteID(), "",
                            helper.getLatitudeBD(), helper.getLongitudeBD());
                    mFragment.refreshPage();
                    mFragment.refreshData(true);
                }

                if (position.equals(helper.getCity())) {
                    showLocationIcon(true);
                } else {
                    showLocationIcon(false);
                }
            }
        }
    }

    private void initLocationData() {
        String city = helper.getCity();
        LinkedList<String> list = helper.getHistoryCity();
        if (list.isEmpty()) {
            showLocationIcon(false);
            mCityView.setText("全国");
            startSettingDialog(city);
        } else {
            String historyCity = list.getFirst();
            if (!TextUtils.isEmpty(historyCity)) {
                mCityView.setText(historyCity);
            } else {
                mCityView.setText("全国");
            }

            if (!city.equals(historyCity)) {
                showLocationIcon(false);
                startSettingDialog(city);
            } else {
                showLocationIcon(true);
            }
        }
    }

    public void showLocationIcon(boolean flag) {
        if (flag) {
            if ((mCityView.getCompoundDrawables())[0] == null)
                mCityView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.t_icon_button_location), null, null, null);
        } else {
            if ((mCityView.getCompoundDrawables())[0] != null)
                mCityView.setCompoundDrawables(null, null, null, null);
        }
    }

    private void startSettingDialog(final String city) {
        if (TextUtils.isEmpty(city) || helper.getPositionTranslate()) {
            return;
        } else {
            helper.setPositionTranslate(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示")
                .setMessage("您当前城市是【" + city + "】,需要切换吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLocationIcon(true);
                        mCityView.setText(city);
                        helper.setHistoryCity(city);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.accessory_sale_city) {
            ActivitySwitcherMaintenance.toPositionActivity(this, 1, mCityView.getText().toString());
        }
    }

    @Override
    public void onBrandClick(String brand, String series) {
        this.brand = brand;
        this.series = series;
        this.priceOrder = "";
        mFragment.refreshPage();
        mFragment.refreshData(true);
    }

    @Override
    public void onClassClick(String class1stID, String class2ndID) {
        this.class1stID = class1stID;
        this.class2ndID = class2ndID;
        this.priceOrder = "";
        mFragment.refreshPage();
        mFragment.refreshData(true);
    }

    @Override
    public void onStoreClick(String shopID) {
        this.shopID = shopID;
        this.priceOrder = "";
        mFragment.refreshPage();
        mFragment.refreshData(true);
    }

    @Override
    public void onPriceClick(String priceOrder) {
        this.priceOrder = priceOrder;
        mFragment.refreshPage();
        mFragment.refreshData(true);
    }

    public void clearCondition() {
        mTipView.clearCondition();
        this.brand = "";
        this.series = "";
        this.class1stID = "";
        this.class2ndID = "";
        this.shopID = "";
        this.priceOrder = "";
    }

}
