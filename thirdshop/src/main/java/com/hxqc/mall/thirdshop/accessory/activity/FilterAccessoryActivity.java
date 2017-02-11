package com.hxqc.mall.thirdshop.accessory.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.hxqc.mall.activity.BaseFilterActivity;
import com.hxqc.mall.thirdshop.accessory.adapter.AccessoryPriceAdapter;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.control.FilterController;
import com.hxqc.mall.thirdshop.accessory.fragment.Filter.FilterAccessoryCoreFragment;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryInfo;
import com.hxqc.mall.thirdshop.accessory.model.ProductSeries;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;

import java.util.ArrayList;

/**
 * Function: 用品首页
 *
 * @author 袁秉勇
 * @since 2016年02月22日
 */
public class FilterAccessoryActivity extends BaseFilterActivity implements FilterController.AccessoryInfoHandler, FilterAccessoryCoreFragment.FilterAccessoryCallBack {
    private final static String TAG = FilterAccessoryActivity.class.getSimpleName();
    private Context mContext;

    AccessoryPriceAdapter accessoryPriceAdapter;
    FilterController filterController;
    AccessoryApiClient accessoryApiClient;
    private String recentBrandID; // 用于从报价页面回退时，如果用户在报价页面点击选择过品牌，则自动刷新筛选列表


    @Override
    protected void initHashMap() {
        filterController = FilterController.getInstance();
//        this.mFilterMap = filterController.mFilterMap;
    }


    @Override
    protected void setTitle() {
        toolbar.setTitle("用品销售");
    }


    @Override
    protected void initApiClient() {
        accessoryApiClient = new AccessoryApiClient();
    }


    @Override
    protected void initAdapter() {
        accessoryPriceAdapter = new AccessoryPriceAdapter() {
            @Override
            public void onBindViewHolder(final Holder holder, final int position) {
                super.onBindViewHolder(holder, position);
                holder.contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterController.mFilterMap.put("productSeriesID", accessoryPriceAdapter.list.get(position).productSeries.productSeriesID);
                        echo(filterController.mFilterMap);
                        if (filterController.getBrand() == null) {
                            recentBrandID = null;
                        } else {
                            recentBrandID = filterController.getBrand().brandID;
                        }
                        ActivitySwitcherAccessory.toAccessoryPriceActivity(FilterAccessoryActivity.this,
                                accessoryPriceAdapter.list.get(position).productSeries.smallPhoto, accessoryPriceAdapter.list.get(position).productSeries.name,
                                filterController.getBrand(), accessoryPriceAdapter.list.get(position).productBrandID, accessoryPriceAdapter.list.get(position).productBrandName);
                    }
                });
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        mChangeCityView.setVisibility(View.GONE);
        filterController.mFilterMap.remove("productSeriesID");

        if (filterController.getBrand() == null) {
            if (!TextUtils.isEmpty(recentBrandID)) {
                filterController.mFilterMap.remove("brandID");
                filterController.mFilterMap.remove("seriesID");
                baseFilterCoreFragment.mBrandView.setTagString("不限", false);
                baseFilterCoreFragment.mSeriesView.setTagString("不限", false);
                baseFilterCoreFragment.setListener(false);
                filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            }
        } else if (TextUtils.isEmpty(recentBrandID) || !recentBrandID.equals(filterController.getBrand().brandID)) {
            filterController.mFilterMap.remove("seriesID");
            baseFilterCoreFragment.mBrandView.setTagString(filterController.getBrand().brandName, true);
            baseFilterCoreFragment.mSeriesView.setTagString("不限", false);
            baseFilterCoreFragment.setListener(true);
            filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
        }
    }


    @Override
    protected void initFragment() {
        baseFilterCoreFragment = new FilterAccessoryCoreFragment();
        baseFilterCoreFragment.setCallBack(this);
        ((FilterAccessoryCoreFragment) baseFilterCoreFragment).setFilterAccessoryCallBack(this);
    }


    @Override
    protected void initFragmentView() {
//        if (baseFilterCoreFragment != null) baseFilterCoreFragment.mAutoModelView.setVisibility(View.GONE);
    }


    @Override
    protected void initData() {
        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
    }


    @Override
    protected void refreshData() {
        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
    }


    @Override
    public void onLoadMore() {
        ++mPage;
        filterController.getAccessoryPriceData(this, mPage, PER_PAGE, filterController.mFilterMap, this);
    }


    @Override
    protected void toPositionChoose() {
        ActivitySwitcherAccessory.toPositionActivity(this, 1, mChangeCityView.getText().toString());
    }


    @Override
    protected void clearAdapterData() {
        if (adapter instanceof AccessoryPriceAdapter) {
            ((AccessoryPriceAdapter) adapter).list.clear();
        }
    }


    @Override
    protected void showDistanceInAdapter(boolean flag) {

    }


    @Override
    public void baseFilterCoreFragmentCallBack(String key, String value) {
        if (!TextUtils.isEmpty(value)) filterController.putValue(key, value);
        switch (key) {
            // 搜店铺
            case "brandID":
                echo(filterController.mFilterMap);
                if (TextUtils.isEmpty(value)) {
                    if (filterController.getBrand() != null) filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                } else {
                    if (filterController.getBrand() == null) {
                        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                    } else if (filterController.getBrand() != null && !value.equals(filterController.getBrand().brandID)) {
                        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                    }
                }
                recentBrandID = value;
                break;

            // 搜店铺
            case "seriesID":
                echo(filterController.mFilterMap);
                if (TextUtils.isEmpty(value)) {
                    if (filterController.getSeries() != null) {
                        filterController.mFilterMap.remove("seriesID");
                        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                    }
                } else {
                    if (filterController.getSeries() == null) {
                        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                    } else if (filterController.getSeries() != null && !value.equals(filterController.getSeries().seriesID)) {
                        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                    }
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterController.destroy();
    }


    private ArrayList< AccessoryInfo > constructData() {
        ArrayList< AccessoryInfo > accessoryInfoArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AccessoryInfo accessoryInfo = new AccessoryInfo();
            accessoryInfo.productBrandName = "这是第 " + i + " 个ProductBrandName";
            accessoryInfo.productBrandID = i + "";

            ProductSeries productSeries = new ProductSeries();
            productSeries.priceRange = (i * 100) + " — " + (i * 200);
            productSeries.smallPhoto = "null";
            productSeries.name = "这是第 " + i + " 个Name";
            productSeries.productSeriesID = i + "";

            accessoryInfo.productSeries = productSeries;
            accessoryInfoArrayList.add(accessoryInfo);
        }

        return accessoryInfoArrayList;
    }


    @Override
    public void onGetInfoSucceed(ArrayList< AccessoryInfo > accessoryInfos) {
        if (accessoryInfos == null) {
            if (mPage == DEFAULT_PATE) {
                FoldJudgment(true);
            }
            return;
//            accessoryInfos = constructData();
        }

        hasMore = accessoryInfos.size() >= PER_PAGE;

        if (mPage == DEFAULT_PATE) {
            accessoryPriceAdapter.list.clear();
            if (accessoryInfos.size() > 0) {
                mRecyclerView.setAdapter(accessoryPriceAdapter);
            } else {
                FoldJudgment(true);
                return;
            }
        }
        accessoryPriceAdapter.addData(accessoryInfos);
        FoldJudgment(false);
    }

    protected void initLocationData() {}


    @Override
    public void onGetInfoFailed() {
        FoldJudgment(true);
    }


    @Override
    public void onFilterAccessoryCoreFragmentCallBack() {
        filterController.getAccessoryPriceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
    }
}
