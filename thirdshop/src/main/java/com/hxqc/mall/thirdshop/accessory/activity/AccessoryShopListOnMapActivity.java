package com.hxqc.mall.thirdshop.accessory.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BaseItemChooseOnMapActivity;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.PullListView;
import com.hxqc.mall.thirdshop.accessory.adapter.AccessoryShopListOnMapAdapter;
import com.hxqc.mall.thirdshop.accessory.adapter.AccessoryViewPagerAdapter;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.fragment.AccessoryShopListFragment;
import com.hxqc.mall.thirdshop.accessory.model.PickupShop;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Function: 地图上选店（用品模块）
 *
 * @author 袁秉勇
 * @since 2016年06月06日
 */
public class AccessoryShopListOnMapActivity extends BaseItemChooseOnMapActivity {
    private final static String TAG = AccessoryShopListOnMapActivity.class.getSimpleName();
    private Context mContext;

    private AccessoryApiClient accessoryApiClient;

    private AccessoryShopListOnMapAdapter shopListOnMapAdapter;

    private AccessoryViewPagerAdapter accessoryViewPagerAdapter;

    private ArrayList< PickupShop > pickupShops = new ArrayList<>();

    private ArrayList< PickupShop > dataOnShow = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = "用品店";

        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initApi() {
        accessoryApiClient = new AccessoryApiClient();
    }


    @Override
    protected void initAdapter() {
        shopListOnMapAdapter = new AccessoryShopListOnMapAdapter(this);
        mListView.setAdapter(shopListOnMapAdapter);

        accessoryViewPagerAdapter = new AccessoryViewPagerAdapter(this);
        mViewPager.setAdapter(accessoryViewPagerAdapter);
    }


    @Override
    protected void setAnchorPoint() {
        mSlidingUpLayout.setBothAnchorPoint(OtherUtil.px2dp(332, getResources().getDisplayMetrics()), OtherUtil.px2dp(161, getResources().getDisplayMetrics()));
    }


    @Override
    protected void refreshData(final boolean hasLoadingAnim) {
        if (mPtrFrameLayoutView.isRefreshing()) mPtrHelper.refreshComplete(mPtrFrameLayoutView);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1);
                shopListOnMapAdapter.setData(dataOnShow = getSectionData(pickupShops, mPage, PER_PAGE)); // 取出的分段数据
                accessoryViewPagerAdapter.setData(dataOnShow);
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


    private ArrayList< PickupShop > getSectionData(ArrayList< PickupShop > arrayList, int beginPage, int perPage) {
        ArrayList< PickupShop > list = new ArrayList<>();
        int endIndex = perPage * beginPage > arrayList.size() ? arrayList.size() : perPage * beginPage;
        for (int i = (beginPage - 1) * perPage; i < endIndex; i++) {
            list.add(arrayList.get(i));
        }
        return list;
    }


    @Override
    protected void getData(boolean showLoadingAnim) {
//        if (mPtrHelper.isHasMore()) mPtrHelper.setHasMore(false);
        accessoryApiClient.getShopList(mPage, PER_PAGE, new BaseSharedPreferencesHelper(this).getLatitudeBD(), new BaseSharedPreferencesHelper(this).getLongitudeBD(), getLoadingAnimResponseHandler(true));
    }


    @Override
    protected void onSuccessResponse(String response) {
        ArrayList< PickupShop > pickupShops = JSONUtils.fromJson(response, new TypeToken< ArrayList< PickupShop > >() {
        });
        if (pickupShops == null || pickupShops.size() <= 0) {
            pickupShops = constructData();
        }

        if (pickupShops != null && pickupShops.size() > 0) {
            if (dataOnShow.size() > 0) {
                dataOnShow.clear();
            }
            dataOnShow.addAll(pickupShops);
            this.pickupShops.addAll(pickupShops); // 当前返回的分段数据

            ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1); // 设置没有默认选中的position
            shopListOnMapAdapter.setData(pickupShops); // 设置Adapter中当前分段的数据
            accessoryViewPagerAdapter.setData(dataOnShow);
            mListView.setSelection(0);
            lastClickMarker = null;

            addMarkersToMap(assemblyMarkerData());

//            mPtrHelper.setHasMore(pickupShops.size() >= PER_PAGE);
            mSlidingUpLayout.setIsLastPage(pickupShops.size() < PER_PAGE, mPage);
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
            LatLng latLng = MapUtils.bd_encrypt(dataOnShow.get(i).latitude, dataOnShow.get(i).longitude);
            latLngs.add(latLng);
        }
        return latLngs;
    }


    @Override
    protected void clickItemToActivity(int position) {
        Intent intent = new Intent();
        intent.putExtra(AccessoryShopListFragment.CHOOSE_SHOP_ID, dataOnShow.get(position).shopID);
        intent.putExtra(AccessoryShopListFragment.CHOOSE_SHOP_NAME, dataOnShow.get(position).shopTitle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    protected void setListViewIsAtFirstPage(boolean isAtFirstPage) {
        mSlidingUpLayout.setIsFirstPge(isAtFirstPage);
        mPtrFrameLayoutView.setPullToRefresh(!isAtFirstPage);
        mRecentMaxPage = mPage > mRecentMaxPage ? mPage : mRecentMaxPage;
        if (!mSlidingUpLayout.isLastPage()) mListView.changeFooterViewState(PullListView.LoadingState.LOADING_COMPLETE, mPage);
    }


    private ArrayList< PickupShop > constructData() {
        ArrayList< PickupShop > pickupShops = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PickupShop PickupShop = new PickupShop();
            PickupShop.shopTitle = "这是测试店铺 " + i;
            PickupShop.distance = i * 100;
            PickupShop.latitude = (float) (30.5927504277 + i * 0.02);
            PickupShop.longitude = (float) (114.2556213449 + i * 0.08);
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

            pickupShops.add(PickupShop);
        }
        return pickupShops;
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
