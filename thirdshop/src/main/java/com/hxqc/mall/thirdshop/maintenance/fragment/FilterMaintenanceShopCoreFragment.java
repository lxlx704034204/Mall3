package com.hxqc.mall.thirdshop.maintenance.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.fragment.BaseFilterCoreFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.control.FilterController;
import com.hxqc.mall.thirdshop.maintenance.fragment.Filter.FilterBrandFragment;
import com.hxqc.mall.thirdshop.maintenance.fragment.Filter.FilterSeriesFragment;
import com.hxqc.mall.thirdshop.maintenance.model.Brand;
import com.hxqc.mall.thirdshop.maintenance.model.Series;

/**
 * Function:常规保养界面
 *
 * @author 袁秉勇
 * @since 2016年02月16日
 */
public class FilterMaintenanceShopCoreFragment extends BaseFilterCoreFragment implements FilterBrandFragment.FilterBrandFragmentCallBack, FilterSeriesFragment.FilterSeriesFragmentCallBack {
    private final static String TAG = FilterMaintenanceShopCoreFragment.class.getSimpleName();
    private Context mContext;

    public FilterBrandFragment filterBrandFragment;
    public FilterSeriesFragment filterSeriesFragment;
    FilterController filterController;
    MaintenanceClient maintenanceClient;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_filter_core_maintenance, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        filterController = FilterController.getInstance();

        maintenanceClient = new MaintenanceClient();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.filter_brand) {
            if (filterBrandFragment == null) {
                filterBrandFragment = new FilterBrandFragment();
                filterBrandFragment.setFilterBrandFragmentCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterBrandFragment);

        } else if (i == R.id.filter_series) {
            if (filterSeriesFragment == null) {
                filterSeriesFragment = FilterSeriesFragment.newInstance(filterController.getBrand());
                filterSeriesFragment.setFilterSeriesFragmentCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterSeriesFragment);
        }
    }


    @Override
    public void onFilterBrandCallback(Brand brand) {
        filterMenuListener.closeFilterFactor();

        if (brand == null) {
            filterController.mFilterMap.remove("brand");
            filterController.mFilterMap.remove("series");
            mBrandView.setTagString("不限", false);
            mSeriesView.setTagString("不限", false);
            mSeriesView.setOnClickListener(seriesViewOnClickListener);
            callBack.baseFilterCoreFragmentCallBack("brand", null);
            filterController.setBrand(null);
        } else {
            if (brand.brandName == null) {
                ToastHelper.showRedToast(getContext(), "品牌名称不存在");
                filterMenuListener.closeFilterFactor();
                return;
            }
            if (null != filterController.getBrand() && !brand.brandName.equals(filterController.getBrand().brandName)) {
                filterController.mFilterMap.remove("series");
                mSeriesView.setTagString("不限", false);
                if (filterSeriesFragment != null) filterSeriesFragment.getData(brand.brandName, true);
            }
            mBrandView.setTagString(brand.brandName, true);
            mSeriesView.setOnClickListener(this);
            callBack.baseFilterCoreFragmentCallBack("brand", brand.brandName);
            filterController.setBrand(brand);
        }
    }


    @Override
    public void onFilterSeriesCallBack(Series series) {
        filterMenuListener.closeFilterFactor();

        if (series == null) {
            filterController.mFilterMap.remove("series");
            mSeriesView.setTagString("不限", false);
            callBack.baseFilterCoreFragmentCallBack("series", null);
            filterController.setSeries(null);
        } else {
            if (series.seriesName == null) {
                ToastHelper.showRedToast(getContext(), "车系名称不存在");
                filterMenuListener.closeFilterFactor();
                return;
            }
            mSeriesView.setTagString(VerifyString(series.seriesName), true);
            callBack.baseFilterCoreFragmentCallBack("series", series.seriesName);
            filterController.setSeries(series);
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
}
