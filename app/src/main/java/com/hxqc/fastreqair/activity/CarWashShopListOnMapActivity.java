package com.hxqc.fastreqair.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.google.gson.reflect.TypeToken;
import com.hxqc.fastreqair.adapter.CarWashShopListOnMapAdapter;
import com.hxqc.fastreqair.adapter.CarWashViewpagerAdapter;
import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CarWashShopListBean;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.activity.BaseItemChooseOnMapActivity;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.PullListView;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function: 地图上选店（洗车模块）
 *
 * @author 袁秉勇
 * @since 2016年06月04日
 */
public class CarWashShopListOnMapActivity extends BaseItemChooseOnMapActivity {
    private final static String TAG = CarWashShopListOnMapActivity.class.getSimpleName();

    public final static String DATA = "sort";
    private CarWashApiClient carWashApiClient;

    private HashMap< String, String > hashMap = new HashMap<>();

    private CarWashShopListOnMapAdapter shopListOnMapAdapter;

    private CarWashViewpagerAdapter carWashViewpagerAdapter;

    private ArrayList< CarWashShopListBean > carWashShopListBeans = new ArrayList<>();

    private ArrayList< CarWashShopListBean > dataOnShow = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = "洗车店";
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initApi() {
        carWashApiClient = new CarWashApiClient();
        hashMap.putAll((HashMap) getIntent().getSerializableExtra(DATA));
        hashMap.put("latitude", TextUtils.isEmpty(new BaseSharedPreferencesHelper(this).getLatitudeBD()) ? "0" : new BaseSharedPreferencesHelper(this).getLatitudeBD());
        hashMap.put("longitude", TextUtils.isEmpty(new BaseSharedPreferencesHelper(this).getLongitudeBD()) ? "0" : new BaseSharedPreferencesHelper(this).getLongitudeBD());
    }


    @Override
    protected void initAdapter() {
        shopListOnMapAdapter = new CarWashShopListOnMapAdapter(this);
        mListView.setAdapter(shopListOnMapAdapter);

        carWashViewpagerAdapter = new CarWashViewpagerAdapter(this);
        mViewPager.setAdapter(carWashViewpagerAdapter);
    }


    @Override
    protected void setAnchorPoint() {
        mSlidingUpLayout.setBothAnchorPoint(OtherUtil.px2dp(336, getResources().getDisplayMetrics()), OtherUtil.px2dp(168, getResources().getDisplayMetrics()));
    }


    @Override
    protected void refreshData(final boolean hasLoadingAnim) {
        if (mPtrFrameLayoutView.isRefreshing()) mPtrHelper.refreshComplete(mPtrFrameLayoutView);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1);
                shopListOnMapAdapter.setData(dataOnShow = getSectionData(carWashShopListBeans, mPage, PER_PAGE)); // 取出的分段数据
                carWashViewpagerAdapter.setData(dataOnShow);
                addMarkersToMap(assemblyMarkerData());
                if (hasLoadingAnim) mListView.setSelection(0);
//        mPtrHelper.setHasMore((mRecentMaxPage > mPage) || (mRecentMaxPage == mPage && dataOnShow.size() == PER_PAGE));
                mSlidingUpLayout.setIsLastPage(mPage <= mRecentMaxPage && dataOnShow.size() < PER_PAGE, mPage);
                setListViewIsAtFirstPage(mPage == DEFAULT_PATE);
                lastClickMarker = null;
            }
        }, 500);
    }


    private ArrayList< CarWashShopListBean > getSectionData(ArrayList< CarWashShopListBean > arrayList, int beginPage, int perPage) {
        ArrayList< CarWashShopListBean > list = new ArrayList<>();
        int endIndex = perPage * beginPage > arrayList.size() ? arrayList.size() : perPage * beginPage;
        for (int i = (beginPage - 1) * perPage; i < endIndex; i++) {
            list.add(arrayList.get(i));
        }
        return list;
    }


    @Override
    protected void getData(boolean showLoadingAnim) {
//        if (mPtrHelper.isHasMore()) mPtrHelper.setHasMore(false);
        carWashApiClient.getCarWashShopListData(mPage, PER_PAGE, hashMap, getLoadingAnimResponseHandler(true));
    }


    @Override
    protected void onSuccessResponse(String response) {
        ArrayList< CarWashShopListBean > carWashShopListBeans = JSONUtils.fromJson(response, new TypeToken< ArrayList< CarWashShopListBean > >() {
        });
//        if (carWashShopListBeans == null || carWashShopListBeans.size() <= 0) {
//            carWashShopListBeans = constructData();
//        }
        if (carWashShopListBeans != null) {
            if (carWashShopListBeans.size() > 0) {
                if (dataOnShow.size() > 0) {
                    dataOnShow.clear();
                }
                dataOnShow.addAll(carWashShopListBeans);

                this.carWashShopListBeans.addAll(carWashShopListBeans); // 当前返回的分段数据
                ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1); // 设置没有默认选中的position
                shopListOnMapAdapter.setData(carWashShopListBeans); // 设置Adapter中当前分段的数据
                carWashViewpagerAdapter.setData(carWashShopListBeans);
                mListView.setSelection(0);
                lastClickMarker = null;

                addMarkersToMap(assemblyMarkerData());

//                mPtrHelper.setHasMore(carWashShopListBeans.size() >= PER_PAGE);
                mSlidingUpLayout.setIsLastPage(carWashShopListBeans.size() < PER_PAGE, mPage);
            } else {
                if (mPage == DEFAULT_PATE) {
                    clearMarkers();
                    showNoData("未找到合适店铺");
                }
                mPage = mPage > 1 ? --mPage : mPage;
            }
        } else {
            if (mPage == DEFAULT_PATE) {
                showNoData("未找到合适店铺");
            }
            mPage = mPage > 1 ? --mPage : mPage;
        }
        setListViewIsAtFirstPage(mPage == DEFAULT_PATE);
    }


    @Override
    protected ArrayList< LatLng > assemblyMarkerData() {
        if (latLngs != null && latLngs.size() > 0) latLngs.clear();
        for (int i = 0; i < dataOnShow.size(); i++) {
//            LatLng latLng = new LatLng(dataOnShow.get(i).latitude, dataOnShow.get(i).longitude);
            LatLng latLng = new LatLng(dataOnShow.get(i).latitude, dataOnShow.get(i).longitude);
            latLngs.add(latLng);
        }
        return latLngs;
    }


//    @Override
//    protected LatLngBounds getLatLngBounds() {
//        LatLngBounds.Builder builder = LatLngBounds.builder();
//        for (int i = 0; i < assemblyMarkerData().size(); i++) {
//            builder.include(assemblyMarkerData().get(i));
//        }
//        return builder.build();
//    }


    @Override
    protected void clickItemToActivity(int position) {
        CarWashActivitySwitcher.toWashCarShop(this, dataOnShow.get(position).shopID);
    }


    @Override
    protected void setListViewIsAtFirstPage(boolean isAtFirstPage) {
        mSlidingUpLayout.setIsFirstPge(isAtFirstPage);
        mPtrFrameLayoutView.setPullToRefresh(!isAtFirstPage);
        mRecentMaxPage = mPage > mRecentMaxPage ? mPage : mRecentMaxPage;
        if (!mSlidingUpLayout.isLastPage()) mListView.changeFooterViewState(PullListView.LoadingState.LOADING_COMPLETE, mPage);
    }


    private ArrayList< CarWashShopListBean > constructData() {
        ArrayList< CarWashShopListBean > carWashShopListBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CarWashShopListBean carWashShopListBean = new CarWashShopListBean();
            carWashShopListBean.shopTitle = "这是测试店铺 " + i;
            carWashShopListBean.distance = i * 100;
            carWashShopListBean.evaluate = 4.39 + "";
            carWashShopListBean.latitude = 30.5927504277 + i * 0.02;
            carWashShopListBean.longitude = 114.2556213449 + i * 0.08;
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.ALIPAY = true;
//            }
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.WEIXIN = true;
//            }
//
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.offline = true;
//            }
//
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.BALANCE = true;
//            }

            carWashShopListBeans.add(carWashShopListBean);
        }
        return carWashShopListBeans;
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
