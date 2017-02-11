//package com.hxqc.mall.thirdshop.fragment.Filter;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.hxqc.mall.core.fragment.FunctionFragment;
//import com.hxqc.mall.core.util.ToastHelper;
//import com.hxqc.mall.core.views.FilterFactorView;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.control.TFilterController;
//import com.hxqc.mall.thirdshop.model.Brand;
//import com.hxqc.mall.thirdshop.model.CarType;
//import com.hxqc.mall.thirdshop.model.Series;
//import com.hxqc.util.JSONUtils;
//import com.loopj.android.http.JsonHttpResponseHandler;
//
//import cz.msebera.android.httpclient.Header;
//import org.json.JSONObject;
//
//
///**
// * Author:胡俊杰
// * Date: 2015/12/1
// * FIXME
// * Todo 第三方店铺筛选
// */
//@Deprecated
//public class FilterThirdShopCoreFragment extends FunctionFragment implements View.OnClickListener,
//        FilterBrandFragmentCallBack, FilterTypeFragment.FilterTypeFragmentCallBack, FilterSeriesFragment.FilterSeriesFragmentCallBack, FilterAreaFragment.OnAreaChooseInteractionListener, FilterAreaFragment.FilterAreaFragmentCallBack {
//    FilterFactorView mBrandView;//品牌
//    FilterFactorView mSeriesView;//车系
//    FilterFactorView mAutoModelView;//车型
//    FilterFactorView mCityView;//城市
//
//    FilterMenuListener filterMenuListener;
//
//    FilterBrandFragment filterBrandFragment;
//    FilterSeriesFragment filterSeriesFragment;
//    FilterTypeFragment filterTypeFragment;
//    FilterAreaFragment filterAreaFragment;
//
//    Brand brand;
//    Series series;
//
//    Button mSubmitFilterView;
//
//    FilterThirdShopCoreFragmentCallBack filterThirdShopCoreFragmentCallBack;
//
//    TFilterController tFilterController;
//
//
//    public void setFilterThirdShopCoreFragmentCallBack(FilterThirdShopCoreFragmentCallBack filterThirdShopCoreFragmentCallBack) {
//        this.filterThirdShopCoreFragmentCallBack = filterThirdShopCoreFragmentCallBack;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        filterMenuListener = (FilterMenuListener) context;
//        tFilterController = TFilterController.getInstance();
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.t_fragment_filter_core, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mBrandView = (FilterFactorView) view.findViewById(R.id.filter_third_brand);
//        mSeriesView = (FilterFactorView) view.findViewById(R.id.filter_third_series);
//        mAutoModelView = (FilterFactorView) view.findViewById(R.id.filter_third_auto_model);
//        mCityView = (FilterFactorView) view.findViewById(R.id.filter_third_city);
//        mSubmitFilterView = (Button) view.findViewById(R.id.submit_filter);
//        mCityView.setOnClickListener(this);
//        mBrandView.setOnClickListener(this);
//        mSeriesView.setOnClickListener(this);
//        mAutoModelView.setOnClickListener(this);
//        mSubmitFilterView.setOnClickListener(this);
//
//        mSeriesView.setEnabled(false);
//        mAutoModelView.setEnabled(false);
//    }
//
//    @Override
//    public void onClick(View view) {
//        int i = view.getId();
//        if (i == R.id.filter_third_brand) {
//            if (filterBrandFragment == null) {
//                filterBrandFragment = new FilterBrandFragment();
//                filterBrandFragment.setFilterBrandFragmentCallBack(this);
//            }
//            filterMenuListener.showFilterFactor(filterBrandFragment);
//
//        } else if (i == R.id.filter_third_series) {
//            if (filterSeriesFragment == null) {
//                filterSeriesFragment = FilterSeriesFragment.newInstance(brand);
//                filterSeriesFragment.setFilterSeriesFragmentCallBack(this);
//            }
//            filterMenuListener.showFilterFactor(filterSeriesFragment);
//
//        } else if (i == R.id.filter_third_auto_model) {
//            if (filterTypeFragment == null) {
//                filterTypeFragment = FilterTypeFragment.newInstace(brand, series);
//                filterTypeFragment.setFilterTypeFragmentCallBack(this);
//            }
//            filterMenuListener.showFilterFactor(filterTypeFragment);
//
//        } else if (i == R.id.filter_third_city) {
//            if (filterAreaFragment == null) {
//                filterAreaFragment = FilterAreaFragment.newInstance(1);
//                filterAreaFragment.setmListener(this);
//                filterAreaFragment.setFilterAreaFragmentCallBack(this);
//            }
//            filterMenuListener.showFilterFactor(filterAreaFragment);
//
//        } else if (i == R.id.submit_filter) {
//            int type = tFilterController.mFilterMap.get("type").equals(TFilterController.TFilerTypeShop) ? 1 : 2;
//            ActivitySwitcherThirdPartShop.toSearchThirdShop(mContext, type, JSONUtils.toJson(tFilterController
//                    .mFilterMap));
//
//        }
//    }
//
//    @Override
//    public void onFilterBrandCallback(Brand brand) {
//        if (brand == null) {
//            tFilterController.mFilterMap.remove("brand");
//            tFilterController.mFilterMap.remove("serie");
//            tFilterController.mFilterMap.remove("model");
//            mBrandView.setTagString("不限", false);
//            mSeriesView.setTagString("不限", false);
//            mAutoModelView.setTagString("不限", false);
//            mSeriesView.setEnabled(false);
//            mAutoModelView.setEnabled(false);
//        } else {
//            if (brand.brandName == null) {
//                ToastHelper.showOrangeToast(getContext(), "品牌名称不存在");
//                filterMenuListener.closeFilterFactor();
//                return;
//            }
//            if (this.brand != null && !this.brand.brandName.equals(brand.brandName)) {
//                tFilterController.mFilterMap.remove("serie");
//                tFilterController.mFilterMap.remove("model");
//                mSeriesView.setTagString("不限", false);
//                mAutoModelView.setTagString("不限", false);
//                mAutoModelView.setEnabled(false);
//                if (filterSeriesFragment != null) filterSeriesFragment.getData(brand.brandName, false);
//            }
//            this.brand = brand;
//            mBrandView.setTagString(brand.brandName, true);
//            mSeriesView.setEnabled(true);
//            filterThirdShopCoreFragmentCallBack.onCallBack("brand", brand.brandName);
//        }
//        refreshDataCount();
//        filterMenuListener.closeFilterFactor();
//    }
//
//    @Override
//    public void onFilterSeriesCallBack(Series series) {
//        if (series == null) {
//            tFilterController.mFilterMap.remove("serie");
//            tFilterController.mFilterMap.remove("model");
//            mSeriesView.setTagString("不限", false);
//            mAutoModelView.setTagString("不限", false);
//            mAutoModelView.setEnabled(false);
//        } else {
//            if (series.seriesName == null) {
//                ToastHelper.showOrangeToast(getContext(), "车系名称不存在");
//                filterMenuListener.closeFilterFactor();
//                return;
//            }
//            if (this.series != null && !this.series.seriesName.equals(series.seriesName)) {
//                tFilterController.mFilterMap.remove("model");
//                mAutoModelView.setTagString("不限", false);
//                if (filterTypeFragment != null) filterTypeFragment.getData(brand.brandName, series.seriesName, false);
//            }
//            this.series = series;
//            mAutoModelView.setEnabled(true);
//            mSeriesView.setTagString(series.seriesName, true);
//            filterThirdShopCoreFragmentCallBack.onCallBack("serie", series.seriesName);
//        }
//        filterMenuListener.closeFilterFactor();
//        refreshDataCount();
//    }
//
//    @Override
//    public void onFilterTypeCallBack(CarType carType) {
//        if (carType == null) {
//            tFilterController.mFilterMap.remove("model");
//            mAutoModelView.setTagString("不限", false);
//        } else {
//            mAutoModelView.setTagString(carType.modelName, true);
//            filterThirdShopCoreFragmentCallBack.onCallBack("model", carType.modelName);
//        }
//        filterMenuListener.closeFilterFactor();
//        refreshDataCount();
//    }
//
//    @Override
//    public void onFilterAreaCallBack() {
//        if (tFilterController.mFilterMap.containsKey("area")) tFilterController.mFilterMap.remove("area");
//        mCityView.setTagString("不限", false);
//        filterMenuListener.closeFilterFactor();
//        refreshDataCount();
//    }
//
//    @Override
//    public void OnAreaChooseInteraction(String provinces, String provinceInitial, String city) {
//        if (!TextUtils.isEmpty(city)) {
//            mCityView.setTagString(city, true);
//            filterThirdShopCoreFragmentCallBack.onCallBack("area", city);
//        } else {
//            if (tFilterController.mFilterMap.containsKey("area")) tFilterController.mFilterMap.remove("area");
//            mCityView.setTagString("不限", false);
//        }
//        refreshDataCount();
//        filterMenuListener.closeFilterFactor();
//    }
//
//    public interface FilterMenuListener {
//        void showFilterFactor(Fragment fragment);
//
//        void closeFilterFactor();
//    }
//
//    /**
//     * 刷新当前筛选车辆的条数
//     */
//    public void refreshDataCount() {
//        ThirdPartShopClient.getFilterCarCount(tFilterController.mFilterMap, new JsonHttpResponseHandler() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                mSubmitFilterView.setEnabled(false);
//                mSubmitFilterView.setText("正在查询中...");
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                try {
//                    if (response.has("count")) {
//                        String count = response.getString("count");
//                        changeResponseViewState(Integer.valueOf(count));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    changeResponseViewState(0);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                changeResponseViewState(0);
//            }
//        });
//    }
//
//    @Override
//    public String fragmentDescription() {
//        return "第三方店铺搜索Fragment";
//    }
//
//    /**
//     * 切换商铺筛选
//     */
//    public void changeShop() {
//        mSeriesView.setVisibility(View.GONE);
//        mAutoModelView.setVisibility(View.GONE);
//    }
//
//    public void changeBrandFilter() {
//        mSeriesView.setVisibility(View.VISIBLE);
//        mAutoModelView.setVisibility(View.VISIBLE);
//    }
//
//    public interface FilterThirdShopCoreFragmentCallBack {
//        void onCallBack(String key, String value);
//    }
//
//    /**
//     * 修改车型按钮
//     *
//     * @param count
//     */
//    void changeResponseViewState(int count) {
//        if (tFilterController.mFilterMap.get(TFilterController.TypeKey).equals(TFilterController.TFilerTypeShop)) {
//            if (count <= 0) {
//                mSubmitFilterView.setText("未找到合适店铺");
//            } else {
//                mSubmitFilterView.setText(String.format("找到%d个店铺", count));
//            }
//        } else {
//            if (count <= 0) {
//                mSubmitFilterView.setText("未找到合适车辆");
//            } else {
//                mSubmitFilterView.setText(String.format("找到%d个车型", count));
//            }
//        }
//        if (count <= 0) {
//            mSubmitFilterView.setEnabled(false);
//        } else {
//            mSubmitFilterView.setEnabled(true);
//        }
//    }
//
//    /**
//     * 当切换车型和店铺的选项卡时，清空用户的所选数据
//     */
//    public void clearFilterData() {
//        mBrandView.setTagString("不限", false);
//        mSeriesView.setTagString("不限", false);
//        mSeriesView.setEnabled(false);
//        mAutoModelView.setTagString("不限", false);
//        mAutoModelView.setEnabled(false);
//        mCityView.setTagString("不限", false);
//        mSubmitFilterView.setText("确定");
//        mSubmitFilterView.setEnabled(true);
//    }
//}
