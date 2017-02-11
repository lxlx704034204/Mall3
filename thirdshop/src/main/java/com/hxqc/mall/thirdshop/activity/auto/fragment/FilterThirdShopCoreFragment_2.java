package com.hxqc.mall.thirdshop.activity.auto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.FilterFactorView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.fragment.Filter.FilterAreaFragment;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.CarType;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.util.DebugLog;


/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月17日
 */
public class FilterThirdShopCoreFragment_2 extends FunctionFragment implements View.OnClickListener, FilterBrandFragment.FilterBrandFragmentCallBack, FilterTypeFragment.FilterTypeFragmentCallBack, FilterSeriesFragment1.FilterSeriesFragmentCallBack, FilterAreaFragment.OnAreaChooseInteractionListener, FilterAreaFragment.FilterAreaFragmentCallBack {
    public FilterFactorView mBrandView;//品牌
    public FilterFactorView mSeriesView;//车系
    public FilterFactorView mAutoModelView;//车型
//    public FilterFactorView mCityView;//城市

    FilterMenuListener filterMenuListener;

    public FilterBrandFragment filterBrandFragment;
    public FilterSeriesFragment1 filterSeriesFragment1;
    public FilterTypeFragment filterTypeFragment;
    FilterAreaFragment filterAreaFragment;

//    FilterThirdShopCoreFragment_2 filterThirdShopCoreFragment_2;

//    public Brand brand;

    FilterThirdShopCoreFragmentCallBack filterThirdShopCoreFragmentCallBack;

    BaseFilterController baseFilterController;

    View.OnClickListener SeriesViewOnClickListener;
    View.OnClickListener ModeViewOnclickListener;

    ThirdPartShopClient ThirdPartShopClient;


    public void setFilterThirdShopCoreFragmentCallBack(FilterThirdShopCoreFragmentCallBack filterThirdShopCoreFragmentCallBack) {
        this.filterThirdShopCoreFragmentCallBack = filterThirdShopCoreFragmentCallBack;
    }


    public void setFilterMenuListener(FilterMenuListener filterMenuListener) {
        this.filterMenuListener = filterMenuListener;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterMenuListener) {
            filterMenuListener = (FilterMenuListener) context;
        } else if (filterMenuListener == null) {
            DebugLog.e("Error", "be careful filterMenuListener is null!!!");
        }

        if (getActivity() instanceof ControllerConstruct) {
            baseFilterController = ((ControllerConstruct) getActivity()).getController();
        } else {
            baseFilterController = BaseFilterController.getInstance();
        }

        ThirdPartShopClient = new ThirdPartShopClient();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.t_fragment_filter_core, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBrandView = (FilterFactorView) view.findViewById(R.id.filter_third_brand);
        mSeriesView = (FilterFactorView) view.findViewById(R.id.filter_third_series);
        mAutoModelView = (FilterFactorView) view.findViewById(R.id.filter_third_auto_model);
//        mCityView = (FilterFactorView) view.findViewById(R.id.filter_third_city);

        SeriesViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showRedToast(getContext(), "请先选择品牌");
            }
        };

        ModeViewOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showRedToast(getContext(), "请先选择车系");
            }
        };

//        mCityView.setOnClickListener(this);
        mBrandView.setOnClickListener(this);
        mSeriesView.setOnClickListener(SeriesViewOnClickListener);
        mAutoModelView.setOnClickListener(ModeViewOnclickListener);


//        mSeriesView.setEnabled(false);
//        mAutoModelView.setEnabled(false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (baseFilterController.getBrand() != null && baseFilterController.getSeries() != null && baseFilterController.getCarType() != null) {
            initData(baseFilterController.getBrand().brandName, baseFilterController.getSeries().seriesName, baseFilterController.getCarType().modelName);
        }
    }


    /**
     * 将来自商城车辆条件的数据，填入到对应的筛选view中
     *
     * @param brandName  品牌名称
     * @param seriesName 车系名称
     * @param modelName  车型名称
     */
    public void initData(String brandName, String seriesName, String modelName) {

        mBrandView.setTagString(brandName, true);
        mSeriesView.setTagString(VerifyString(seriesName), true);
        mAutoModelView.setTagString(modelName, true);

        mSeriesView.setOnClickListener(this);
        mAutoModelView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.filter_third_brand) {
            if (filterBrandFragment == null) {
                filterBrandFragment = new FilterBrandFragment();
                filterBrandFragment.setFilterBrandFragmentCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterBrandFragment);

        } else if (i == R.id.filter_third_series) {
            if (filterSeriesFragment1 == null) {
                filterSeriesFragment1 = FilterSeriesFragment1.newInstance(baseFilterController.getBrand());
                filterSeriesFragment1.setShowNoLimit(false);
                filterSeriesFragment1.setFilterSeriesFragmentCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterSeriesFragment1);

        } else if (i == R.id.filter_third_auto_model) {
            if (filterTypeFragment == null) {
                filterTypeFragment = FilterTypeFragment.newInstace(baseFilterController.getBrand(), baseFilterController.getSeries());
                filterTypeFragment.setFilterTypeFragmentCallBack(this);
            }
            filterMenuListener.showFilterFactor(filterTypeFragment);

        }
//        else if (i == R.id.filter_third_city) {
//            if (filterAreaFragment == null) {
//                filterAreaFragment = FilterAreaFragment.newInstance(1);
//                filterAreaFragment.setmListener(this);
//                filterAreaFragment.setFilterAreaFragmentCallBack(this);
//            }
//            filterMenuListener.showFilterFactor(filterAreaFragment);
//
//        } else if (i == R.id.submit_filter) {
//            int type = baseFilterController.mFilterMap.get("type").equals(baseFilterController.TFilerTypeShop) ? 1 : 2;
//            ActivitySwitcherThirdPartShop.toSearchThirdShop(mContext, type, JSONUtils.toJson(baseFilterController.mFilterMap));
//
//        }
    }


    /**
     * 当进行搜索时，利用该方法清空历史筛选条件
     */
    public void onSearchClearFilter() {
        onBrandTipChanged(true);
    }


    /**
     * 品牌筛选条件改变时和点击搜索时，改变筛选条件的公共代码块
     */
    public void onBrandTipChanged(boolean doingSearch) {
        baseFilterController.mFilterMap.remove("brand");
        baseFilterController.mFilterMap.remove("serie");
        baseFilterController.mFilterMap.remove("model");
        mBrandView.setTagString("不限", false);
        mSeriesView.setTagString("不限", false);
        mAutoModelView.setTagString("不限", false);
        mSeriesView.setOnClickListener(SeriesViewOnClickListener);
        mAutoModelView.setOnClickListener(ModeViewOnclickListener);
        if (!doingSearch) filterThirdShopCoreFragmentCallBack.onCallBack("brand", null);
        baseFilterController.setBrand(null);
        baseFilterController.setSeries(null);
        baseFilterController.setCarType(null);
    }


    @Override
    public void onFilterSeriesCallBack(Series series) {
        if (series == null) {
            baseFilterController.mFilterMap.remove("serie");
            baseFilterController.mFilterMap.remove("model");
            mSeriesView.setTagString("不限", false);
            mAutoModelView.setTagString("不限", false);
            mAutoModelView.setOnClickListener(ModeViewOnclickListener);
//            mAutoModelView.setEnabled(false);
            filterThirdShopCoreFragmentCallBack.onCallBack("serie", null);
            baseFilterController.setSeries(null);
        } else {
            if (series.seriesName == null) {
                ToastHelper.showRedToast(getContext(), "车系名称不存在");
                filterMenuListener.closeFilterFactor();
                return;
            }
            if (null != baseFilterController.getSeries() && !series.seriesName.equals(baseFilterController.getSeries().seriesName)) {
                baseFilterController.mFilterMap.remove("model");
                mAutoModelView.setTagString("不限", false);
                if (filterTypeFragment != null) filterTypeFragment.getData(baseFilterController.getBrand().brandName, series.seriesName, true);
            }
            mAutoModelView.setOnClickListener(this);
//            mAutoModelView.setEnabled(true);
            mSeriesView.setTagString(VerifyString(series.seriesName), true);
            filterThirdShopCoreFragmentCallBack.onCallBack("serie", series.seriesName);
            baseFilterController.setSeries(series);
        }
        filterMenuListener.closeFilterFactor();
    }


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
    public void onFilterTypeCallBack(CarType carType) {
        if (carType == null) {
            baseFilterController.mFilterMap.remove("model");
            mAutoModelView.setTagString("不限", false);
            filterThirdShopCoreFragmentCallBack.onCallBack("model", null);
            baseFilterController.setCarType(null);
        } else {
            mAutoModelView.setTagString(carType.modelName, true);
            filterThirdShopCoreFragmentCallBack.onCallBack("model", carType.modelName);
            baseFilterController.setCarType(carType);
        }
        filterMenuListener.closeFilterFactor();
    }


    @Override
    public void onFilterAreaCallBack() {
        if (baseFilterController.mFilterMap.containsKey("area")) baseFilterController.mFilterMap.remove("area");
//        mCityView.setTagString("不限", false);
        filterThirdShopCoreFragmentCallBack.onCallBack("area", null);
        filterMenuListener.closeFilterFactor();
    }


    @Override
    public void OnAreaChooseInteraction(String provinces, String provinceInitial, String city) {
        if (!TextUtils.isEmpty(city)) {
//            mCityView.setTagString(city, true);
            filterThirdShopCoreFragmentCallBack.onCallBack("area", city);
        } else {
            if (baseFilterController.mFilterMap.containsKey("area")) baseFilterController.mFilterMap.remove("area");
//            mCityView.setTagString("不限", false);
            filterThirdShopCoreFragmentCallBack.onCallBack("area", null);
        }
        filterMenuListener.closeFilterFactor();
    }


    @Override
    public void onFilterBrandCallback(Brand brand) {
//        if (filterThirdShopCoreFragment_2 == null) {
//            if (getActivity().getSupportFragmentManager().findFragmentById(R.id.third_filter_fragment) == this) {
//                filterThirdShopCoreFragment_2 = (FilterThirdShopCoreFragment_2) getActivity()
// .getSupportFragmentManager().findFragmentById(R.id.tip_fragment);
//            } else {
//                filterThirdShopCoreFragment_2 = (FilterThirdShopCoreFragment_2) getActivity()
// .getSupportFragmentManager().findFragmentById(R.id.third_filter_fragment);
//            }
//        }
        if (brand == null) {
//            baseFilterController.mFilterMap.remove("brand");
//            baseFilterController.mFilterMap.remove("serie");
//            baseFilterController.mFilterMap.remove("model");
//            mBrandView.setTagString("不限", false);
//            mSeriesView.setTagString("不限", false);
//            mAutoModelView.setTagString("不限", false);
//            mSeriesView.setOnClickListener(SeriesViewOnClickListener);
//            mAutoModelView.setOnClickListener(ModeViewOnclickListener);
//            baseFilterController.setBrand(null);
////            filterThirdShopCoreFragment_2.mBrandView.setTagString("不限", false);
////            filterThirdShopCoreFragment_2.mSeriesView.setTagString("不限", false);
////            filterThirdShopCoreFragment_2.mAutoModelView.setTagString("不限", false);
////            filterThirdShopCoreFragment_2.mSeriesView.setEnabled(false);
////            filterThirdShopCoreFragment_2.mAutoModelView.setEnabled(false);
            onBrandTipChanged(false);
        } else {
            if (brand.brandName == null) {
                ToastHelper.showRedToast(getContext(), "品牌名称不存在");
                filterMenuListener.closeFilterFactor();
                return;
            }
            if (null != baseFilterController.getBrand() && !brand.brandName.equals(baseFilterController.getBrand().brandName)) {
                baseFilterController.mFilterMap.remove("serie");
                baseFilterController.mFilterMap.remove("model");
                mSeriesView.setTagString("不限", false);
                mAutoModelView.setTagString("不限", false);
                mAutoModelView.setOnClickListener(ModeViewOnclickListener);
//                mAutoModelView.setEnabled(false);
                if (filterSeriesFragment1 != null) filterSeriesFragment1.getData(brand.brandName, true);
            }
            mBrandView.setTagString(brand.brandName, true);
            mSeriesView.setOnClickListener(this);
//            mSeriesView.setEnabled(true);
//            filterThirdShopCoreFragment_2.brand = brand;
//            filterThirdShopCoreFragment_2.mBrandView.setTagString(brand.brandName, true);
//            filterThirdShopCoreFragment_2.mSeriesView.setEnabled(true);
            filterThirdShopCoreFragmentCallBack.onCallBack("brand", brand.brandName);
            baseFilterController.setBrand(brand);
        }
        filterMenuListener.closeFilterFactor();
    }


    public interface FilterMenuListener {
        void showFilterFactor(Fragment fragment);

        void closeFilterFactor();
    }


    @Override
    public String fragmentDescription() {
        return "第三方店铺搜索Fragment";
    }


    public interface FilterThirdShopCoreFragmentCallBack {
        void onCallBack(String key, String value);
    }
}
