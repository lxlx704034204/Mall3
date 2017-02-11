//package com.hxqc.mall.thirdshop.activity;
//
//import android.os.Bundle;
//
//import com.hxqc.mall.activity.BackActivity;
//import com.hxqc.mall.core.util.ActivitySwitchBase;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
//import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
//import com.hxqc.mall.thirdshop.activity.auto.fragment.Filter4SBrandFragment;
//import com.hxqc.mall.thirdshop.activity.auto.fragment.Filter4SSeriesFragment;
//import com.hxqc.mall.thirdshop.model.Brand;
//import com.hxqc.mall.thirdshop.model.Series;
//import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
//
//import net.simonvt.menudrawer.MenuDrawer;
//import net.simonvt.menudrawer.OverlayDrawer;
//
///**
// * Author: HuJunJie
// * Date: 2016年1月13日
// * FIXME
// * Todo 新车品牌列表，实际4S店品牌
// */
//public class FourSBrandActivity extends BackActivity implements ControllerConstruct, Filter4SBrandFragment.FilterBrandFragmentCallBack
//        , Filter4SSeriesFragment.FilterSeriesFragmentCallBack {
//    OverlayDrawer mOverlayDrawer;
//    Filter4SBrandFragment filterBrandFragment;
//    Filter4SSeriesFragment filterSeriesFragment;
//    BaseFilterController baseFilterController;
//    public String mSiteID;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_4s_brand);
//        mSiteID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherThirdPartShop.SITE_ID);
//        initController();
//        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
//        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
//        mOverlayDrawer.setSidewardCloseMenu(false);
//        filterBrandFragment = (Filter4SBrandFragment) getSupportFragmentManager().findFragmentById(R.id.shop_brand);
//        filterBrandFragment.setShowNoLimit(false);
//        filterBrandFragment.setShowSideBar(true);
//        filterBrandFragment.setFilterBrandFragmentCallBack(this);
//
//        filterSeriesFragment = (Filter4SSeriesFragment) getSupportFragmentManager().findFragmentById(R.id.shop_series);
//        filterSeriesFragment.setShowNoLimit(false);
//        filterSeriesFragment.setFilterSeriesFragmentCallBack(this);
//    }
//
//    /**
//     * 显示车系
//     */
//    public void showSeries(Brand brand) {
//        if (!mOverlayDrawer.isMenuVisible()) mOverlayDrawer.openMenu();
//        if (brand != null)
//            filterSeriesFragment.getData(brand.brandName, mSiteID, true);
//    }
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        super.onCreateOptionsMenu(menu);
////        getMenuInflater().inflate(R.menu.menu_brand_filter, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        super.onOptionsItemSelected(item);
////        if (item.getItemId() == R.id.action_brand_filter) {
////            //按条件找车
////            String itemCategory = "10";//默认新车
////            if (getIntent().hasExtra(ActivitySwitchBase.KEY_DATA)) {
////                itemCategory = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AutoItem.ItemCategory);
////            }
////            ActivitySwitcher.toAutoFilter(this, itemCategory);
////        }
////        return false;
////    }
//
//    Brand brand;
//
//    @Override
//    public void onFilterBrandCallback(Brand brand) {
//        this.brand = brand;
//        showSeries(brand);
//    }
//
//    @Override
//    public void onFilterSeriesCallBack(Series series) {
//        if (series == null) return;
////        ActivitySwitcherThirdPartShop.toAutoModel(this, brand.brandName, series.seriesName, series);
////        ActivitySwitcher.toAutoList(this);
//        ActivitySwitcherThirdPartShop.toNewCarModelList(this, brand.brandName,mSiteID, series, true);
//    }
//
//
//    @Override
//    public void finish() {
//        super.finish();
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        destroyController();
//    }
//
//
//    @Override
//    public void initController() {
//        baseFilterController = BaseFilterController.getInstance();
//    }
//
//
//    @Override
//    public BaseFilterController getController() {
//        return baseFilterController;
//    }
//
//
//    @Override
//    public void destroyController() {
//        if (baseFilterController != null) baseFilterController.destroy();
//    }
//}
