package com.hxqc.mall.thirdshop.accessory.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.fragment.AccessorySaleFragment;
import com.hxqc.mall.thirdshop.accessory.views.FilterTipView;

/**
 * 用品销售 旧版本
 * Created by huangyi on 16/2/24.
 */
public class AccessorySaleActivity extends BackActivity implements FilterTipView.OnFilterClickListener {

    public String class1stID; //一级分类id
    public String class2ndID; //二级分类id
    public String brandID; //品牌id
    public String seriesID; //车系id
    public String priceOrder; //价格升降序 asc升序 desc降序
    FilterTipView mTipView;
    AccessorySaleFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_sale);

        mTipView = (FilterTipView) findViewById(R.id.accessory_sale_tip);
        mTipView.setOnFilterClickListener(this);
        mTipView.setShadeView(findViewById(R.id.accessory_sale_shade));

        mFragment = new AccessorySaleFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.accessory_sale_list, mFragment).commit();
    }

    @Override
    public void onBrandClick(String brandID, String seriesID) {
        this.brandID = brandID;
        this.seriesID = seriesID;
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
    public void onPriceClick(String priceOrder) {
        this.priceOrder = priceOrder;
        mFragment.refreshPage();
        mFragment.refreshData(true);
    }

    public void clearCondition() {
        mTipView.clearCondition();
        this.brandID = "";
        this.seriesID = "";
        this.class1stID = "";
        this.class2ndID = "";
        this.priceOrder = "";
    }

}
