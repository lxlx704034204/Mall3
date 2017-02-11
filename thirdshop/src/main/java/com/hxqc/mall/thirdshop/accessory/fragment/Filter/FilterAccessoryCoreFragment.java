package com.hxqc.mall.thirdshop.accessory.fragment.Filter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.fragment.BaseFilterCoreFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.control.FilterController;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryBigCategory;
import com.hxqc.mall.thirdshop.accessory.model.Brand;
import com.hxqc.mall.thirdshop.accessory.model.Series;
import com.hxqc.util.DebugLog;

/**
 * Function: 用品销售界面
 *
 * @author 袁秉勇
 * @since 2016年02月16日
 */
public class FilterAccessoryCoreFragment extends BaseFilterCoreFragment implements FilterBrandFragment.FilterBrandFragmentCallBack, FilterSeriesFragment.FilterSeriesFragmentCallBack, FilterCategoryFragment.CallBack {
    private final static String TAG = FilterAccessoryCoreFragment.class.getSimpleName();
    private Context mContext;

    public FilterBrandFragment filterBrandFragment;
    public FilterSeriesFragment filterSeriesFragment;
    public FilterCategoryFragment filterCategoryFragment;
    FilterController filterController;
    AccessoryApiClient accessoryApiClient;
    private FilterAccessoryCallBack filterAccessoryCallBack;


    public void setFilterAccessoryCallBack(FilterAccessoryCallBack filterAccessoryCallBack) {
        this.filterAccessoryCallBack = filterAccessoryCallBack;
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_filter_core_accessory, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        filterController = FilterController.getInstance();

        accessoryApiClient = new AccessoryApiClient();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.filter_brand) {
            if (filterBrandFragment == null) {
                filterBrandFragment = new FilterBrandFragment();
                filterBrandFragment.setShowSideBar(true);
                filterBrandFragment.setFilterBrandFragmentCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterBrandFragment);

        } else if (i == R.id.filter_series) {
            if (filterSeriesFragment == null) {
                filterSeriesFragment = FilterSeriesFragment.newInstance(filterController.getBrand());
                filterSeriesFragment.setFilterSeriesFragmentCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterSeriesFragment);

        } else if (i == R.id.filter_goods_category) {
            if (filterCategoryFragment == null) {
                filterCategoryFragment = new FilterCategoryFragment();
                filterCategoryFragment.setCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterCategoryFragment);

        }
    }


    @Override
    public void onFilterBrandCallback(Brand brand) {
        filterMenuListener.closeFilterFactor();

        if (brand == null) {
            filterController.mFilterMap.remove("brandID");
            filterController.mFilterMap.remove("seriesID");
            mBrandView.setTagString("不限", false);
            mSeriesView.setTagString("不限", false);
            mSeriesView.setOnClickListener(seriesViewOnClickListener);
            callBack.baseFilterCoreFragmentCallBack("brandID", null);
            filterController.setBrand(null);
        } else {
            if (TextUtils.isEmpty(brand.brandID)) {
                ToastHelper.showRedToast(getContext(), "品牌不存在");
                mSeriesView.setOnClickListener(seriesViewOnClickListener);
                filterMenuListener.closeFilterFactor();
                return;
            }
            if (null != filterController.getBrand() && !brand.brandID.equals(filterController.getBrand().brandID)) {
                filterController.mFilterMap.remove("seriesID");
                mSeriesView.setTagString("不限", false);
                if (filterSeriesFragment != null) filterSeriesFragment.getData(brand.brandName, brand.brandID, true);
            }
            mBrandView.setTagString(brand.brandName, true);
            mSeriesView.setOnClickListener(this);
            callBack.baseFilterCoreFragmentCallBack("brandID", brand.brandID);
            filterController.setBrand(brand);
        }
    }


    @Override
    public void onFilterSeriesCallBack(Series series) {
        filterMenuListener.closeFilterFactor();

        if (series == null) {
            mSeriesView.setTagString("不限", false);
            callBack.baseFilterCoreFragmentCallBack("seriesID", null);
            filterController.setSeries(null);
        } else {
            if (TextUtils.isEmpty(series.seriesID)) {
                ToastHelper.showRedToast(getContext(), "车系名称不存在");
                filterMenuListener.closeFilterFactor();
                return;
            }
            if (null != filterController.getSeries() && !series.seriesID.equals(filterController.getSeries().seriesID)) {
            }
            mSeriesView.setTagString(VerifyString(series.seriesName), true);
            callBack.baseFilterCoreFragmentCallBack("seriesID", series.seriesID);
            filterController.setSeries(series);
        }
    }


    @Override
    public void onFilterCategoryCallBack(AccessoryBigCategory accessoryBigCategory) {
        filterMenuListener.closeFilterFactor();

        if (accessoryBigCategory != null) {
            if (filterController.getAccessoryBigCategory() == null ||
                    (accessoryBigCategory.class1stID.equals(filterController.getAccessoryBigCategory().class1stID) && !accessoryBigCategory.class2nd.get(0).class2ndID.equals(filterController.getAccessoryBigCategory().class2nd.get(0).class2ndID)) ||
                    !accessoryBigCategory.class1stID.equals(filterController.getAccessoryBigCategory().class1stID)) {

                DebugLog.e(TAG, accessoryBigCategory.class1stID + " ----> " + accessoryBigCategory.class2nd.get(0).class2ndID);

                filterController.mFilterMap.put("class1stID", accessoryBigCategory.class1stID);
                filterController.mFilterMap.put("class2ndID", accessoryBigCategory.class2nd.get(0).class2ndID);

                filterController.setAccessoryBigCategory(accessoryBigCategory);

                mGoodsCategoryView.setTagString(accessoryBigCategory.class1stName + " " + accessoryBigCategory.class2nd.get(0).class2ndName, true);

                filterAccessoryCallBack.onFilterAccessoryCoreFragmentCallBack();
            }
        } else {
            if (filterController.getAccessoryBigCategory() != null) {
                filterController.mFilterMap.remove("class1stID");
                filterController.mFilterMap.remove("class2ndID");

                filterController.setAccessoryBigCategory(null);

                mGoodsCategoryView.setTagString("不限", false);
                filterAccessoryCallBack.onFilterAccessoryCoreFragmentCallBack();
            }
        }
    }


    /** 修改车系名称显示规则 **/
    public String VerifyString(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.length() > 1 && "系".equals(str.substring(str.length() - 1, str.length()))) {
                return str + "列车型";
            } else if (str.length() > 2 && "系列".equals(str.substring(str.length() - 2, str.length()))) {
                return str + "车型";
            }

            return str + "系列车型";
        }
        return "未知车系车型";
    }


    @Override
    public String fragmentDescription() {
        return "维修筛选店铺fragment";
    }


    public interface FilterAccessoryCallBack {
        void onFilterAccessoryCoreFragmentCallBack();
    }
}
