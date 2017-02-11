package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BaseItemChooseOnMapActivity;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.PullListView;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintenanceShopListOnMapAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintenanceViewPagerOnMapAdapter;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function: 地图上选店（保养模块）
 *
 * @author 袁秉勇
 * @since 2016年06月03日
 */
public class MaintenanceShopListOnMapActivity extends BaseItemChooseOnMapActivity implements MaintenanceViewPagerOnMapAdapter.CallBack {
    private final static String TAG = MaintenanceShopListOnMapActivity.class.getSimpleName();

    public static String TYPE = "type";
    public static String BRANDID = "brandID";
    public static String SERIESID = "seriesID";
    public static String AUTOMODELID = "autoModelID";
    public static String MYAUTOID = "myAutoID";
    public static String ITEMS = "items";
    public static String HASHMAP = "hashMap";

    private HashMap< String, String > hashMap = new HashMap<>();

    private MaintenanceClient maintenanceClient;

    private int shopType; // 0 为4S店，1 为快修店
    private String brandID;
    private String seriesID;
    private String autoModelID;
    private String myAutoID;
    private String items;

    private MaintenanceShopListOnMapAdapter shopListOnMapAdapter;

    private MaintenanceViewPagerOnMapAdapter viewPagerOnMapAdapter;

    private ArrayList< NewMaintenanceShop > maintenanceShops = new ArrayList<>();

    private ArrayList< NewMaintenanceShop > dataOnShow = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = "快修店";

        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initApi() {
        maintenanceClient = new MaintenanceClient();

        hashMap.put("latitude", new BaseSharedPreferencesHelper(this).getLatitudeBD());
        hashMap.put("longitude", new BaseSharedPreferencesHelper(this).getLongitudeBD());

        Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        hashMap.putAll((HashMap) bundle.getSerializable(HASHMAP));
        shopType = bundle.getInt(TYPE, 0);
        brandID = bundle.getString(BRANDID, "123");
        seriesID = bundle.getString(SERIESID, "101711793");
        autoModelID = bundle.getString(AUTOMODELID, "101637027");
        myAutoID = bundle.getString(MYAUTOID, "");
        items = bundle.getString(ITEMS, "{\"itemGroupID\":[\"hhs \"]}");
    }


    @Override
    protected void initAdapter() {
        shopListOnMapAdapter = new MaintenanceShopListOnMapAdapter(this, shopType == 0 ? false : true);
        mListView.setAdapter(shopListOnMapAdapter);

        viewPagerOnMapAdapter = new MaintenanceViewPagerOnMapAdapter(this, shopType == 0 ? false : true);
        viewPagerOnMapAdapter.setCallBack(this);
        mViewPager.setAdapter(viewPagerOnMapAdapter);
    }


    @Override
    protected void setAnchorPoint() {
        if (shopType == 1) { // 快修店
            mSlidingUpLayout.setBothAnchorPoint(OtherUtil.px2dp(305, getResources().getDisplayMetrics()), OtherUtil.px2dp(152.5f, getResources().getDisplayMetrics()));
        } else { // 4S店
            mSlidingUpLayout.setBothAnchorPoint(OtherUtil.px2dp(213, getResources().getDisplayMetrics()), OtherUtil.px2dp(106.5f, getResources().getDisplayMetrics()));
        }
    }


    @Override
    protected void refreshData(final boolean hasLoadingAnim) {
        if (mPtrFrameLayoutView.isRefreshing()) mPtrHelper.refreshComplete(mPtrFrameLayoutView);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1);
                shopListOnMapAdapter.setData(dataOnShow = getSectionData(maintenanceShops, mPage, PER_PAGE)); // 取出的分段数据
                viewPagerOnMapAdapter.setData(dataOnShow);
                addMarkersToMap(assemblyMarkerData());
                if (mPtrFrameLayoutView.isRefreshing()) mPtrHelper.refreshComplete(mPtrFrameLayoutView);
                if (hasLoadingAnim) mListView.setSelection(0);
//        mPtrHelper.setHasMore((mRecentMaxPage > mPage) || (mRecentMaxPage == mPage && dataOnShow.size() == PER_PAGE));
                mSlidingUpLayout.setIsLastPage(mPage <= mRecentMaxPage && dataOnShow.size() < PER_PAGE, mPage);
                setListViewIsAtFirstPage(mPage == DEFAULT_PATE);
                lastClickMarker = null;
            }
        }, 500);
    }


    private ArrayList< NewMaintenanceShop > getSectionData(ArrayList< NewMaintenanceShop > arrayList, int beginPage, int perPage) {
        ArrayList< NewMaintenanceShop > list = new ArrayList<>();
        int endIndex = perPage * beginPage > arrayList.size() ? arrayList.size() : perPage * beginPage;
        for (int i = (beginPage - 1) * perPage; i < endIndex; i++) {
            list.add(arrayList.get(i));
        }
        return list;
    }


    @Override
    protected void getData(boolean showLoadingAnim) {
//        if (mPtrHelper.isHasMore()) mPtrHelper.setHasMore(false);
        maintenanceClient.requireNewMaintenanceData(mPage, PER_PAGE, brandID, seriesID, autoModelID, myAutoID, items, shopType == 0 ? 10 : 20, hashMap, getLoadingAnimResponseHandler(true));
    }


    @Override
    protected void onSuccessResponse(String response) {
        ArrayList< NewMaintenanceShop > maintenanceShops = JSONUtils.fromJson(response, new TypeToken< ArrayList< NewMaintenanceShop > >() {
        });
//        if (maintenanceShops == null || maintenanceShops.size() <= 0) {
//            maintenanceShops = constructData();
//        }
        if (maintenanceShops != null) {
            if (maintenanceShops.size() > 0) {
                if (dataOnShow.size() > 0) {
                    dataOnShow.clear();
                }
                dataOnShow.addAll(maintenanceShops);

                this.maintenanceShops.addAll(maintenanceShops); // 当前返回的分段数据
                ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1); // 设置没有默认选中的position
                shopListOnMapAdapter.setData(maintenanceShops); // 设置Adapter中当前分段的数据
                viewPagerOnMapAdapter.setData(maintenanceShops);
                mListView.setSelection(0);
                lastClickMarker = null;

                addMarkersToMap(assemblyMarkerData());

//                mPtrHelper.setHasMore(maintenanceShops.size() >= PER_PAGE);
                mSlidingUpLayout.setIsLastPage(maintenanceShops.size() < PER_PAGE, mPage);
            } else {
                if (mPage == DEFAULT_PATE) {
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
//        ActivitySwitcherMaintenance.toFourSAndQuickConfirmOrder(this, dataOnShow.get(position));
    }


    @Override
    protected void setListViewIsAtFirstPage(boolean isAtFirstPage) {
        mSlidingUpLayout.setIsFirstPge(isAtFirstPage);
        mPtrFrameLayoutView.setPullToRefresh(!isAtFirstPage);
        mRecentMaxPage = mPage > mRecentMaxPage ? mPage : mRecentMaxPage;
        if (!mSlidingUpLayout.isLastPage()) mListView.changeFooterViewState(PullListView.LoadingState.LOADING_COMPLETE, mPage);
    }


    private ArrayList< NewMaintenanceShop > constructData() {
        ArrayList< NewMaintenanceShop > maintenanceShops = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NewMaintenanceShop newMaintenanceShop = new NewMaintenanceShop();
            newMaintenanceShop.shopTitle = "这是测试店铺 " + i;
            newMaintenanceShop.distance = i * 100;
            newMaintenanceShop.level = i + "级";
            newMaintenanceShop.evaluate = 4.39 + "";
            newMaintenanceShop.latitude = 30.5927504277 + i * 0.02;
            newMaintenanceShop.longitude = 114.2556213449 + i * 0.08;
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

            newMaintenanceShop.amount = i * 100;
            maintenanceShops.add(newMaintenanceShop);
        }
        return maintenanceShops;
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    public void callBack(int position) {
        clickItemToActivity(position);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
