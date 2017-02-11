package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.activity.auto.control.FilterControllerForSpecialCar;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterBrandFragment;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterSeriesFragment1;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.TCarSeriesModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Author: HuJunJie
 * Date: 2016年1月13日
 * FIXME
 * Todo 新车品牌列表，实际4S店品牌
 */
public class ShopBrandActivity extends BaseSiteChooseActivity implements ControllerConstruct, FilterBrandFragment.FilterBrandFragmentCallBack, FilterSeriesFragment1.FilterSeriesFragmentCallBack, View.OnClickListener {
    public static final String FINISH = "finish_activity";
    public static final String FROM_4S_HOME = "from_4S_home";
    public static final String FROM_CAR_COMPARE = "from_car_compare";
    public static final String FROM_CHOOSE_CAR_FRAGMENT = "form_choose_car_fragment";

    OverlayDrawer mOverlayDrawer;
    FilterBrandFragment filterBrandFragment;
    FilterSeriesFragment1 filterSeriesFragment;
    BaseFilterController baseFilterController;
    private String seriesName;

    private boolean fromCarCompare;  //来自车型对比 by zf


    @Override
    void onResultCallBack(Bundle bundle) {
        baseFilterController.mFilterMap.put("siteID", bundle.getString(AREAID));
        if (mOverlayDrawer.isMenuVisible()) mOverlayDrawer.closeMenu(false);
        if (filterBrandFragment != null) {
            filterBrandFragment.getData();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_brand);
        EventBus.getDefault().register(this);
        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mChangeCityView.setOnClickListener(this);
        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            fromCarCompare = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(FROM_CAR_COMPARE, false);
            if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(FROM_4S_HOME, false))
                mChangeCityView.setVisibility(View.GONE);
        }
        //-----------顺序不要变-----------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("新车销售");
        if (getIntent().getBooleanExtra(FROM_CHOOSE_CAR_FRAGMENT, false) || fromCarCompare) {
            mChangeCityView.setVisibility(View.GONE);
            toolbar.setTitle("选择品牌");
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //-----------顺序不要变-----------

        initController();
        initLocationData();
        baseFilterController.mFilterMap.put("siteID", cityGroupID);

        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(false);
        filterBrandFragment = (FilterBrandFragment) getSupportFragmentManager().findFragmentById(R.id.shop_brand);
        filterBrandFragment.setShowNoLimit(false);
        filterBrandFragment.setShowSideBar(true);
        filterBrandFragment.setFilterBrandFragmentCallBack(this);

        filterSeriesFragment = (FilterSeriesFragment1) getSupportFragmentManager().findFragmentById(R.id.shop_series);
        filterSeriesFragment.setShowNoLimit(false);
        filterSeriesFragment.setFilterSeriesFragmentCallBack(this);
        filterSeriesFragment.setFromNewCar(true);
        filterSeriesFragment.setShowAllBrandFilter(fromCarCompare);
    }


    /**
     * 显示车系
     */
    public void showSeries(Brand brand) {
        if (!mOverlayDrawer.isMenuVisible()) mOverlayDrawer.openMenu();
        if (brand != null) filterSeriesFragment.getData(brand.brandName, true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_brand_filter, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.action_brand_filter) {
//            //按条件找车
//            String itemCategory = "10";//默认新车
//            if (getIntent().hasExtra(ActivitySwitchBase.KEY_DATA)) {
//                itemCategory = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AutoItem.ItemCategory);
//            }
//            ActivitySwitcher.toAutoFilter(this, itemCategory);
//        }
//        return false;
//    }

    Brand brand;


    @Override
    public void onFilterBrandCallback(Brand brand) {
        this.brand = brand;
        showSeries(brand);
        EventBus.getDefault().post(brand.brandName); // 改变 条件选车 界面品牌的文字内容
    }


    @Override
    public void onFilterSeriesCallBack(Series series) {
        this.seriesName = series.seriesName;
/*        if (series == null) return;
//        ActivitySwitcherThirdPartShop.toAutoModel(this, brand.brandName, series.seriesName, series);
        ActivitySwitcherThirdPartShop.toNewCarModelList2(this, areaSiteUtil.getSiteID(), brand.brandName, series.seriesName);
//        ActivitySwitcher.toAutoList(this);*/
    }


    /**
     * 车系筛选列表点击回调
     * {@link FilterSeriesFragment1#onChildClick}
     */
    @Subscribe
    public void onBrandNameCallBack(TCarSeriesModel tCarSeriesModels) {
        if (tCarSeriesModels == null) return;
        if (fromCarCompare) {
            //跳转车型筛选列表
            ActivitySwitcherThirdPartShop.toFilterCarModel(this, brand.brandName, seriesName);
            return;
        }
        //tCarSeriesModels.brandName 厂家名
        //brand.brandName 品牌
        ActivitySwitcherThirdPartShop.toNewCarModelList2(this, areaSiteUtil.getSiteID(), brand.brandName, seriesName);

    }

    @Subscribe
    public void onFinish(String msg) {
        if (msg.equals(FINISH)) {
            finish();
        }
    }


    @Override
    public void finish() {
        super.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyController();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void initController() {
        if (!fromCarCompare) {
            baseFilterController = FilterControllerForSpecialCar.getInstance();
        } else baseFilterController = BaseFilterController.getInstance();
    }


    @Override
    public BaseFilterController getController() {
        return baseFilterController;
    }


    @Override
    public void destroyController() {
        if (baseFilterController != null) baseFilterController.destroy();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_city) {
            ActivitySwitcherThirdPartShop.toSpecialCarChoosePositon(this, 1, ((TextView) v).getText().toString());
        }
    }


    @Override
    public void onBackPressed() {
        if (mOverlayDrawer.isMenuVisible())
            mOverlayDrawer.closeMenu();
        else
            finish();
    }
}
