package com.hxqc.mall.thirdshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.adapter.FilterAutoAdapter;
import com.hxqc.mall.auto.model.Filter;
import com.hxqc.mall.auto.model.FilterGroup;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.mall.thirdshop.fragment.FilterAutoBrandFragment;
import com.hxqc.mall.thirdshop.fragment.FilterAutoSerieFragment;
import com.hxqc.mall.thirdshop.fragment.FilterContentFragment;
import com.hxqc.mall.thirdshop.interfaces.FilterAction;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.util.JSONUtils;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 筛选特卖车界面(有不定项)
 */
public class FilterSeckillIndefiniteActivity extends BackActivity implements AdapterView.OnItemClickListener,
        FilterAction {
    static final String BRAND_FRAGMENT = "BrandFragment";
    static final String SERIES_FRAGMENT = "SeriesFragment";
    static final String FILTER_CONTENT_FRAGMENT = "filterContentFragment";
    public static final String SITEID = "siteID";
    OverlayDrawer mOverlayDrawer;
    ListView mFilterFactorView;
    ArrayList<FilterGroup> mFilterGroups = new ArrayList<>();
    FilterAutoAdapter mAdapter;
    ThirdPartShopClient client;
    FilterContentFragment filterContentFragment;
    FilterAutoBrandFragment mMainAutoBrandFragment;//品牌
    FilterAutoSerieFragment mMainAutoSeriesFragment;//品牌
    HashMap<String, String> mFilterMap = new HashMap<>();
    Button mResponseView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_auto);
//        itemCategory = getIntent().getStringExtra(AutoItem.ItemCategory);//类型

        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mOverlayDrawer.setSidewardCloseMenu(false);

        mFilterFactorView = (ListView) findViewById(R.id.filter_factor);
        mFilterFactorView.setOnItemClickListener(this);
        client = new ThirdPartShopClient();
        client.filterArgumentSeckill(new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                createListView(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                createListView("");
            }
        });
        FilterGroup filterGroup = new FilterGroup("品牌");
        FilterGroup filterGroup2 = new FilterGroup("车系");
        mFilterGroups.add(0, filterGroup);
        mFilterGroups.add(1, filterGroup2);

        String siteID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(SITEID);

        mMainAutoBrandFragment = FilterAutoBrandFragment.newInstance(siteID);
        mMainAutoBrandFragment.setmFilterGroup(filterGroup);

        mMainAutoSeriesFragment = FilterAutoSerieFragment.newInstance(siteID);
        mMainAutoSeriesFragment.setmFilterGroup(filterGroup2);


        filterContentFragment = new FilterContentFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.mdMenu, mMainAutoBrandFragment, BRAND_FRAGMENT).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mdMenu, mMainAutoSeriesFragment, SERIES_FRAGMENT).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mdMenu, filterContentFragment, FILTER_CONTENT_FRAGMENT).commit();

        mResponseView = (Button) findViewById(R.id.filter_response);
    }

    protected void createListView(String response) {
        ArrayList<FilterGroup> filterGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<FilterGroup>>() {
        });
        if (filterGroups != null)
            this.mFilterGroups.addAll(filterGroups);
        if (mAdapter == null) {
            mAdapter = new FilterAutoAdapter(this, mFilterGroups);
            mFilterFactorView.setAdapter(mAdapter);
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (position == 0) {
            ft.hide(filterContentFragment).
                    hide(mMainAutoSeriesFragment).
                    show(mMainAutoBrandFragment).commit();
            openMenu();
        } else if (position == 1) {
            if (!TextUtils.isEmpty(mFilterMap.get(FilterResultKey.BRAND_KEY))) {
                ft.hide(filterContentFragment).
                        hide(mMainAutoBrandFragment).
                        show(mMainAutoSeriesFragment).commit();
                openMenu();
            } else {
                ToastHelper.showRedToast(this, "请先选择品牌");
            }
        } else {
            ft.hide(mMainAutoBrandFragment)
                    .hide(mMainAutoSeriesFragment)
                    .show(filterContentFragment).commit();
            filterContentFragment.notifyFilterFactor(this, mFilterGroups.get(position));
            openMenu();
        }

    }

    void openMenu() {
        if (!mOverlayDrawer.isMenuVisible()) {
            mOverlayDrawer.openMenu();
        }
    }

    @Override
    public void filterListener(int position, Filter filter, FilterGroup filterGroup) {
        if (filter.filterKey.equals(FilterResultKey.BRAND_KEY)) {
            mFilterGroups.get(1).setDefaultFilter(null);
            mFilterMap.put(FilterResultKey.SERIES_KEY, "");
        }
        if (position == 0) {
            if (mFilterMap.containsKey(filter.filterKey)) {
                mFilterMap.remove(filter.filterKey);
            }
            filter = null;
        } else {
            mFilterMap.put(filter.filterKey, filter.filterValue);
            if (filter.filterKey.equals(FilterResultKey.BRAND_KEY)) {
                mMainAutoSeriesFragment.upBrand(((Brand) filter).brandName);
            }
        }

        filterGroup.setDefaultFilter(filter);
        mAdapter.notifyDataSetChanged();
        mOverlayDrawer.closeMenu();
        if (!mFilterMap.isEmpty()) {
            mResponseView.setEnabled(true);
            mResponseView.setText("查看");
        } else {
            mResponseView.setEnabled(false);
            mResponseView.setText("请选择筛选条件");
        }
    }


    public void toAutoList(View view) {
        if (mFilterMap.keySet().size() <= 0) {
            ToastHelper.showYellowToast(this, "请选择筛选条件.");
            return;
        }
//        mFilterMap.put(AutoItem.ItemCategory, itemCategory);
        //回去列表页面
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FilterResultKey.KEY_VALUE, mFilterMap);
        intent.putExtra(FilterResultKey.PARAMS, bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mOverlayDrawer.isMenuVisible())
            mOverlayDrawer.closeMenu();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().beginTransaction().remove(mMainAutoSeriesFragment);
        getSupportFragmentManager().beginTransaction().remove(mMainAutoBrandFragment);
        mMainAutoSeriesFragment = null;
        mMainAutoSeriesFragment = null;
    }
}
