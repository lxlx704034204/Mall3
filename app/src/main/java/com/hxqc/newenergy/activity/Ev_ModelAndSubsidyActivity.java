package com.hxqc.newenergy.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.db.area.TProvince;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.DropDownMenu;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.views.CustomRecyclerView;
import com.hxqc.newenergy.adapter.EV_ModelSubsidyAdapter;
import com.hxqc.newenergy.bean.FilterCondition;
import com.hxqc.newenergy.bean.FilterItem;
import com.hxqc.newenergy.bean.ModelAndSubsidy;
import com.hxqc.newenergy.bean.position.Province;
import com.hxqc.newenergy.control.FilterController;
import com.hxqc.newenergy.fragment.FilterAreaFragment;
import com.hxqc.newenergy.fragment.FilterBrandFragment;
import com.hxqc.newenergy.fragment.FilterMileageFragment;
import com.hxqc.newenergy.fragment.FilterPriceFragment;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function: 车型及补贴资料界面
 *
 * @author 袁秉勇
 * @since 2016年3月11日
 */

public class Ev_ModelAndSubsidyActivity extends ToolBarActivity implements
        OnRefreshHandler, FilterController.ModelAndSubsidyHandler, FilterController.FilterConditionHandler, FilterAreaFragment.FilterAreaCallBack, FilterBrandFragment.FilterBrandFragmentCallBack, FilterPriceFragment.FilterPriceCallBack, FilterMileageFragment.FilterMileageFragmentCallBack {
    private final static String TAG = Ev_ModelAndSubsidyActivity.class.getSimpleName();
    private static final int DEFAULT_PATE = 1;
    private final int PER_PAGE = 15;
	private Context mContext;
	private int mPage = DEFAULT_PATE;//当前页
	private boolean hasMore = false;

//    private NewEnergyApiClient newEnergyApiClient;

    private FilterController filterController;

    private DropDownMenu mDropDownMenu;

    private PtrFrameLayout mPtrFrameLayout;
    private UltraPullRefreshHeaderHelper mPtrHelper;
    private CustomRecyclerView mRecyclerView;
    private RequestFailView mRequestFailView;
    private EV_ModelSubsidyAdapter ev_modelSubsidyAdapter;

    private String[] headers = new String[]{"品牌", "价格", "续航", "地区"};
    private ArrayList< Object > popupView = new ArrayList< Object >() {
    };


    public CustomRecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        filterController = FilterController.getInstance();

        setContentView(R.layout.ev_activity_modelandsubsidy);
        toolbarInit();
        mDropDownMenu = (DropDownMenu) findViewById(R.id.drop_down_menu);

//        mContentView = (FrameLayout) findViewById(R.id.content_view);
//
//        if (mContentView.getParent() != null) {
//            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
//        }

        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.refresh_view);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayout, this);

        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recycler_view);
        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ev_modelSubsidyAdapter = new EV_ModelSubsidyAdapter(this);
        mRecyclerView.setAdapter(ev_modelSubsidyAdapter);

        filterController.mFilterMap.put("area", EVSharePreferencesHelper.getLastHistoryCity(mContext));
        getData();
        getFilterData();
    }


    private void getData() {
        if (mPtrFrameLayout.isRefreshing()) mPtrFrameLayout.refreshComplete();
        filterController.requestSubsidyData(this, mPage, PER_PAGE, filterController.mFilterMap, this);
    }


    private void getFilterData() {
        filterController.requestFilterData(this, this);
    }


    private void showNoData(boolean offLine) {
        if (offLine) {
            if (mPage == DEFAULT_PATE) {
                mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            } else {
                ToastHelper.showRedToast(this, "网络连接失败");
            }
        } else {
            if (mPage == DEFAULT_PATE) {
                mRequestFailView.setEmptyDescription("获取数据失败");
                mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
            } else {
                ToastHelper.showRedToast(this, "获取数据失败");
            }
        }
//        if (mPtrFrameLayout.getVisibility() == View.VISIBLE) mPtrFrameLayout.setVisibility(View.GONE);
        if (mRequestFailView.getVisibility() == View.GONE) mRequestFailView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean hasMore() {
        return hasMore;
    }


    @Override
    public void onRefresh() {
        mPage = DEFAULT_PATE;
        getData();
    }


    @Override
    public void onLoadMore() {
        ++mPage;
        getData();
    }


    @Override
    public void onBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void onSucceed(ArrayList< ModelAndSubsidy > modelAndSubsidies) {
        if (modelAndSubsidies == null) {
            showNoData(false);
            return;
        }

        hasMore = modelAndSubsidies.size() >= PER_PAGE;

        if (mPage == DEFAULT_PATE) {
            if (modelAndSubsidies.size() > 0) {
//                if (mPtrFrameLayout.getVisibility() == View.GONE) mPtrFrameLayout.setVisibility(View.VISIBLE);
                if (mRequestFailView.getVisibility() == View.VISIBLE) mRequestFailView.setVisibility(View.GONE);
                if (ev_modelSubsidyAdapter != null) ev_modelSubsidyAdapter.refreshData(modelAndSubsidies);
            } else {
                showNoData(false);
                return;
            }
        } else {
            ev_modelSubsidyAdapter.addData(modelAndSubsidies);
        }
    }


    @Override
    public void onFailed(boolean offLine) {
        if (offLine) {
            showNoData(true);
        } else {
            showNoData(false);
        }
    }


    @Override
    public void onGetFilterConditionSucceed(ArrayList< FilterCondition > filterConditions) {
        if (filterConditions == null || filterConditions.size() == 0) {
            onGetFilterConditionFailed(false);
            return;
        }
        initFragment(filterConditions);
    }


    private void initFragment(ArrayList< FilterCondition > filterConditions) {
        if (filterConditions.size() > 0) {
            FilterBrandFragment filterBrandFragment = FilterBrandFragment.newInstance(filterConditions.get(0).filterItem);
            filterBrandFragment.setFilterBrandFragmentCallBack(this);
            popupView.add(filterBrandFragment);
        } else {
            popupView.add(new Fragment());
        }

        if (filterConditions.size() > 1) {
            FilterPriceFragment filterPriceFragment = FilterPriceFragment.newInstance(filterConditions.get(1).filterItem);
            filterPriceFragment.setFilterPriceFragmentCallBack(this);
            popupView.add(filterPriceFragment);
        } else {
            popupView.add(new Fragment());
        }

        if (filterConditions.size() > 2) {
            FilterMileageFragment filterMileageFragment = FilterMileageFragment.newInstance(filterConditions.get(2).filterItem);
            filterMileageFragment.setFilterMileageCallBack(this);
            popupView.add(filterMileageFragment);
        } else {
            popupView.add(new Fragment());
        }

        FilterAreaFragment filterAreaFragment = FilterAreaFragment.newInstance(assembleAreaData());
        filterAreaFragment.setFilterAreaCallBack(this);
        popupView.add(filterAreaFragment);

        mDropDownMenu.setInflateFragment(true);

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupView);
        if (!TextUtils.isEmpty(EVSharePreferencesHelper.getLastHistoryCity(mContext))) {
            mDropDownMenu.setTabText(3, EVSharePreferencesHelper.getLastHistoryCity(mContext));
        }
    }


    /** 拼装地区数据 **/
    private ArrayList< Province > assembleAreaData() {
	    ArrayList<TProvince> areaModels = AreaDBManager.getInstance(this.getApplicationContext()).getPList();
	    ArrayList< Province > provinces = new ArrayList<>();
//        for (int i = 0; i < areaModels.size() + 1; i++) {
//            Province province = null;
//            if (i == 0) {
//                province = new Province("不限");
//            } else {
//                province = new Province(areaModels.get(i - 1).title);
//
//                ArrayList< AreaModel > areaModels1 = AreaDBManager.getInstance(this).getCList(areaModels.get(i - 1).siteID + "");
//
//                ArrayList< City > cities = new ArrayList<>();
//
//                for (int j = 0; j < areaModels1.size(); j++) {
//                    City city = new City(areaModels1.get(j).title);
//                    cities.add(city);
//                }
//
//                province.areaGroup = new ArrayList<>();
//                province.areaGroup.addAll(cities);
//            }
//            provinces.add(province);
//        }

	    //--------------
//        for (int i = 0; i < areaModels.size(); i++) {
//            Province province = null;
//            province = new Province(areaModels.get(i).title, areaModels.get(i).areaID);
//
//            ArrayList< AreaModel > areaModels1 = AreaDBManager.getInstance(this.getApplicationContext()).getCList(areaModels.get(i).areaID + "");
//
//            ArrayList< City > cities = new ArrayList<>();
//
//            for (int j = 0; j < areaModels1.size(); j++) {
//                City city = new City(areaModels1.get(j).title);
//                cities.add(city);
//            }
//
//            province.areaGroup = new ArrayList<>();
//            province.areaGroup.addAll(cities);
//            provinces.add(province);
//        }
        return provinces;
    }


    @Override
    public void onGetFilterConditionFailed(boolean offLine) {
//        ToastHelper.showRedToast(this, "抱歉，当前网络数据获取异常!");
    }


    @Override
    public void onFilterBrandCallback(FilterItem filterItem) {
        mDropDownMenu.closeMenu();
        if (null == filterItem) {
            mDropDownMenu.setTabText(0, "不限");
        } else {
            mDropDownMenu.setTabText(0, filterItem.label);
        }
        recombineFilterCondition("brand", filterItem);
    }


    @Override
    public void onFilterPriceCallBack(FilterItem filterItem) {
        mDropDownMenu.closeMenu();
        if (null == filterItem) {
            mDropDownMenu.setTabText(1, "不限");
        } else {
            mDropDownMenu.setTabText(1, filterItem.label);
        }
        recombineFilterCondition("priceLevel", filterItem);
    }


    @Override
    public void onFilterMileageCallBack(FilterItem filterItem) {
        mDropDownMenu.closeMenu();
        if (null == filterItem) {
            mDropDownMenu.setTabText(2, "不限");
        } else {
            mDropDownMenu.setTabText(2, filterItem.label);
        }
        recombineFilterCondition("batteryLife", filterItem);
    }


    @Override
    public void onFilterAreaCallBack(String cityName) {
        mDropDownMenu.closeMenu();
        if (!TextUtils.isEmpty(cityName)) {
            if (filterController.mFilterMap.containsKey("area")) {
                if (!filterController.mFilterMap.get("area").equals(cityName)) {
                    filterController.mFilterMap.put("area", cityName);
                    filterController.requestSubsidyData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                }
            } else {
                filterController.mFilterMap.put("area", cityName);
                filterController.requestSubsidyData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            }
            mDropDownMenu.setTabText(3, cityName);
        } else {
            if (filterController.mFilterMap.containsKey("area")) {
                filterController.mFilterMap.remove("area");
                filterController.requestSubsidyData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            }
            mDropDownMenu.setTabText(3, "不限");
        }
    }


    /** 重组请求数据 **/
    private void recombineFilterCondition(String conditionType, FilterItem filterItem) {
        if (filterItem == null) {
            if (filterController.mFilterMap.containsKey(conditionType)) {
                filterController.mFilterMap.remove(conditionType);
                filterController.requestSubsidyData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            }
        } else {
            if (!filterController.mFilterMap.containsKey(conditionType)) {
                filterController.mFilterMap.put(conditionType, filterItem.label);
                filterController.requestSubsidyData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            } else {
                if (!filterController.mFilterMap.get(conditionType).equals(filterItem.label)) {
                    filterController.mFilterMap.put(conditionType, filterItem.label);
                    filterController.requestSubsidyData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                }
            }
        }

        Iterator iterator = filterController.mFilterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            DebugLog.e(TAG, entry.getKey() + " : " + entry.getValue());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterController.destroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        closePopWindowMenu();
    }
}
