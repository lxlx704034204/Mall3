package com.hxqc.aroundservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.adapter.AroundServiceMapListAdapter;
import com.hxqc.aroundservice.adapter.AroundServiceViewPagerAdapter;
import com.hxqc.aroundservice.adapter.MapListForGasStationAdapter;
import com.hxqc.aroundservice.adapter.ViewPagerForGasStationAdapter;
import com.hxqc.aroundservice.api.AroundServiceApiClient;
import com.hxqc.mall.activity.BaseItemChooseOnMapActivity;
import com.hxqc.mall.amap.activity.SimpleNaviActivity;
import com.hxqc.mall.amap.model.AroundGasModel;
import com.hxqc.mall.amap.model.GasStationModel;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.NetWorkUtils;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.PullListView;
import com.hxqc.mall.core.views.SlidingUpView.SlidingUpLayout;
import com.hxqc.mall.core.views.dialog.SubmitDialog;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hxqc.mall.R;

import static com.hxqc.mall.amap.utils.AMapUtils.ACTIVITYINDEX;
import static com.hxqc.mall.amap.utils.AMapUtils.ISEMULATOR;
import static com.hxqc.mall.amap.utils.AMapUtils.SIMPLEROUTENAVI;

/**
 * Function: 周边服务（停车场、充电站、加油站)
 *
 * @author 袁秉勇
 * @since 2016年06月21日
 */
public class AroundServiceListOnMapActivity extends BaseItemChooseOnMapActivity implements BaseMapListAdapter.ClickCallBack, AMapNaviListener {
    private final static String TAG = AroundServiceListOnMapActivity.class.getSimpleName();
    private Context mContext;

    public final static String TYPE = "type";
    private final static int GASSTATION = 1;
    private final static int PARK = 2;
    private final static int ROTE = 3;
    private final static int CHARGE = 4;
    private int type = 3; // 1 加油站， 2 停车场， 3 路况， 4 充电站

    private AroundServiceApiClient aroundServiceApiClient;

    private AroundServiceMapListAdapter aroundServiceMapListAdapter;
    private AroundServiceViewPagerAdapter aroundServiceViewPagerAdapter;

    private MapListForGasStationAdapter mapListForGasStationAdapter;
    private ViewPagerForGasStationAdapter viewPagerForGasStationAdapter;

    private ArrayList< PoiItem > poiItems = new ArrayList<>();
    private ArrayList< PoiItem > dataOnShow = new ArrayList<>();

    private ArrayList< GasStationModel > gasStationModels = new ArrayList<>();
    private ArrayList< GasStationModel > mapGasDataOnShow = new ArrayList<>();

    private int lastCheckID = -1;

    private SubmitDialog mProgressDialog;// 路径规划过程显示状态

    private boolean mIsCalculateRouteSuccess = false;

    private AMapNavi mAMapNavi;
    private NaviLatLng mNaviStart;
    private NaviLatLng mNaviEnd;
    // 起点终点列表---------- 路径规划     导航需要
    private ArrayList< NaviLatLng > mStartPoints = new ArrayList<>();
    private ArrayList< NaviLatLng > mEndPoints = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(TYPE, 0);

        switch (type) {
            case GASSTATION:
                title = "加油站";
                /**
                 * modifyTime ： 2017年1月6日
                 * modifyDescribe：加油站数据从高德Api获取
                 */
//                requestGasData = true;

                dataFormGD = true;
                showSearchView = true;
                showFilterContailerView = true;
                break;

            case PARK:
                title = "停车场";
                dataFormGD = true;
                showSearchView = true;
                showFilterContailerView = true;
                break;

            case CHARGE:
                title = "充电站";
                dataFormGD = true;
                showSearchView = true;
                showFilterContailerView = true;
                break;

            case ROTE:
                typeIsRote = true;
                break;
        }

        showToolView = true;
        super.onCreate(savedInstanceState);

        switch (type) {
            case PARK:
                mRadioGroup.check(R.id.around_map_park);
                break;

            case GASSTATION:
                mRadioGroup.check(R.id.around_map_gas);
                break;

            case CHARGE:
                mRadioGroup.check(R.id.around_map_charge);
                break;

            case ROTE:
                ((CheckBox) findViewById(R.id.around_map_road)).setChecked(true);
                break;
        }
    }


    @Override
    protected void initApi() {
        if (!dataFormGD) {
            aroundServiceApiClient = new AroundServiceApiClient();
        }
    }


    @Override
    protected void initAdapter() {
        if (dataFormGD) {
            aroundServiceMapListAdapter = new AroundServiceMapListAdapter(this);
            aroundServiceMapListAdapter.setClickCallBack(this);
            mListView.setAdapter(aroundServiceMapListAdapter);

            aroundServiceViewPagerAdapter = new AroundServiceViewPagerAdapter(this);
            aroundServiceViewPagerAdapter.setClickCallBack(this);
            mViewPager.setAdapter(aroundServiceViewPagerAdapter);
        } else {
            mapListForGasStationAdapter = new MapListForGasStationAdapter(this);
            mapListForGasStationAdapter.setClickCallBack(this);
            mListView.setAdapter(mapListForGasStationAdapter);

            viewPagerForGasStationAdapter = new ViewPagerForGasStationAdapter(this);
            viewPagerForGasStationAdapter.setClickCallBack(this);
            mViewPager.setAdapter(viewPagerForGasStationAdapter);
        }
    }


    @Override
    protected void setAnchorPoint() {
        if (dataFormGD) {
            mSlidingUpLayout.setBothAnchorPoint(OtherUtil.px2dp(242, getResources().getDisplayMetrics()), OtherUtil.px2dp(121, getResources().getDisplayMetrics()));
        } else {
            mSlidingUpLayout.setBothAnchorPoint(OtherUtil.px2dp(242, getResources().getDisplayMetrics()), OtherUtil.px2dp(121, getResources().getDisplayMetrics()));
        }
    }


    @Override
    protected void refreshData(final boolean hasLoadingAnim) {
        if (mPtrFrameLayoutView.isRefreshing()) mPtrHelper.refreshComplete(mPtrFrameLayoutView);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1);
                if (dataFormGD) {
                    aroundServiceMapListAdapter.setData(dataOnShow = getSectionData(poiItems, mPage, PER_PAGE)); // 取出的分段数据
                    aroundServiceViewPagerAdapter.setData(dataOnShow);
                } else {
                    mapListForGasStationAdapter.setData(mapGasDataOnShow = getGasSectionData(gasStationModels, mPage, PER_PAGE));
                    viewPagerForGasStationAdapter.setData(mapGasDataOnShow);
                }
                addMarkersToMap(assemblyMarkerData());
                if (hasLoadingAnim) mListView.setSelection(0);
//                mPtrHelper.setHasMore(((mRecentMaxPage > mPage) || (mRecentMaxPage == mPage && dataOnShow.size() == PER_PAGE)) & canLoadData);
                mSlidingUpLayout.setIsLastPage(mPage <= mRecentMaxPage && (dataFormGD ? dataOnShow.size() < PER_PAGE : mapGasDataOnShow.size() < PER_PAGE), mPage);
                setListViewIsAtFirstPage(mPage == DEFAULT_PATE);
                lastClickMarker = null;
            }
        }, 500);
    }


    private ArrayList< PoiItem > getSectionData(ArrayList< PoiItem > arrayList, int beginPage, int perPage) {
        ArrayList< PoiItem > list = new ArrayList<>();
        int endIndex = perPage * beginPage > arrayList.size() ? arrayList.size() : perPage * beginPage;
        for (int i = (beginPage - 1) * perPage; i < endIndex; i++) {
            list.add(arrayList.get(i));
        }
        return list;
    }


    private ArrayList< GasStationModel > getGasSectionData(ArrayList< GasStationModel > arrayList, int beginPage, int perPage) {
        ArrayList< GasStationModel > list = new ArrayList<>();
        int endIndex = perPage * beginPage > arrayList.size() ? arrayList.size() : perPage * beginPage;
        for (int i = (beginPage - 1) * perPage; i < endIndex; i++) {
            list.add(arrayList.get(i));
        }
        return list;
    }


    @Override
    protected void getData(boolean showLoadingAnim) {
        if (typeIsRote) return;
        Log.e(TAG, "( " + mAMapLocation.getLatitude() + " , " + mAMapLocation.getLongitude() + " )");

        if (dataFormGD) {
            if (clearAllDataFlag) {
                clearAllDataFlag = false;
                poiItems.clear();
            }

            showLoadingDialog();

            query = new PoiSearch.Query(userFilterInput + title, "公共设施|交通设施服务|汽车服务", areaCode);
            DebugLog.e(TAG, "搜索条件 : " + query.getQueryString() + " " + areaCode);
            // keyWord表示搜索字符串，
            //第二个参数表示POI搜索类型，二者选填其一，
            //POI搜索类型共分为以下20种：汽车服务|汽车销售|
            //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
            //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
            //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
            //cityCode表示POI搜索区域的编码，是必须设置参数
            query.setPageSize(PER_PAGE); // 设置每页最多返回多少条poiitem
            query.setPageNum(((mPage - 1) >= 0) ? (mPage - 1) : 0); //设置查询页码
            PoiSearch poiSearch = new PoiSearch(this, query); // 初始化poiSearch对象    武汉市中心坐标 : 30.5927504277,114.3052418195
//            if (areaCode == defAreaCode) {
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mAMapLocation.getLatitude(), mAMapLocation.getLongitude()), 100000, true)); // 设置周边搜索的中心点以及区域
//            }
            poiSearch.setOnPoiSearchListener(this); // 设置回调数据的监听器
            if (NetWorkUtils.isNetWorkUseable(this)) {
                poiSearch.searchPOIAsyn(); // 开始搜索
            } else {
                ToastHelper.showRedToast(this, "当前网络尚未连接，请先开启网络!");
                mListView.changeFooterViewState(PullListView.LoadingState.LOADING_COMPLETE, --mPage);
                dismissLoadingDialog();
            }
        } else {
            DebugLog.e(TAG, "从第三方接口获取加油站数据");
            aroundServiceApiClient.getGasStationInfoInner(latLng.latitude + "", latLng.longitude + "", 3000, mPage, getLoadingAnimResponseHandler(true));
        }
    }


    @Override
    protected void onSuccessResponse(String response) {
        AroundGasModel aroundGasModel = JSONUtils.fromJson(response, new TypeToken< AroundGasModel >() {
        });
        ArrayList< GasStationModel > gasStationModels;
        if (aroundGasModel != null && aroundGasModel.result != null && aroundGasModel.result.data != null && aroundGasModel.result.data.size() > 0) {
            gasStationModels = aroundGasModel.result.data;

            if (mapGasDataOnShow.size() > 0) {
                mapGasDataOnShow.clear();
            }
            mapGasDataOnShow.addAll(gasStationModels);
            this.gasStationModels.addAll(gasStationModels); // 当前返回的分段数据

            ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1); // 设置没有默认选中的position
            mapListForGasStationAdapter.setData(gasStationModels); // 设置Adapter中当前分段的数据
            viewPagerForGasStationAdapter.setData(gasStationModels);
            mListView.setSelection(0);
            lastClickMarker = null;

            addMarkersToMap(assemblyMarkerData());

//            mPtrHelper.setHasMore(gasStationModels.size() >= PER_PAGE);
            mSlidingUpLayout.setIsLastPage(gasStationModels.size() < PER_PAGE, mPage);
            if (mRequestFailView.isShown()) mRequestFailView.setVisibility(View.GONE);
        } else {
            if (mPage == DEFAULT_PATE) {
                clearMarkers();
                showNoData("未找到合适加油站");
            }
            mPage = mPage > 1 ? --mPage : mPage;
        }

        setListViewIsAtFirstPage(mPage == DEFAULT_PATE);
    }


    @Override
    protected ArrayList< LatLng > assemblyMarkerData() {
        LatLng latLng;
        if (latLngs != null && latLngs.size() > 0) latLngs.clear();
        for (int i = 0; i < (dataFormGD ? dataOnShow.size() : mapGasDataOnShow.size()); i++) {
            if (dataFormGD) {
                latLng = new LatLng(dataOnShow.get(i).getLatLonPoint().getLatitude(), dataOnShow.get(i).getLatLonPoint().getLongitude());
            } else {
                latLng = new LatLng(mapGasDataOnShow.get(i).getLatitude(), mapGasDataOnShow.get(i).getLongitude());
            }
            latLngs.add(latLng);
        }
        return latLngs;
    }


    @Override
    protected void clickItemToActivity(int position) {
//        ToastHelper.showGreenToast(this, "点击跳转");
    }


    @Override
    protected void setListViewIsAtFirstPage(boolean isAtFirstPage) {
        mSlidingUpLayout.setIsFirstPge(isAtFirstPage);
        mPtrFrameLayoutView.setPullToRefresh(!isAtFirstPage);
        mRecentMaxPage = mPage > mRecentMaxPage ? mPage : mRecentMaxPage;
        if (!mSlidingUpLayout.isLastPage()) mListView.changeFooterViewState(PullListView.LoadingState.LOADING_COMPLETE, mPage);
    }


    @Override
    public void onPoiSearched(final PoiResult poiResult, int i) {
        dismissLoadingDialog();
        DebugLog.e(TAG, " ------->>>>>>>>>>>>" + poiResult.getQuery().getQueryString());

        if (poiResult != null && poiResult.getPois() != null && poiResult.getPois().size() > 0) {
            for (int j = 0; j < poiResult.getPois().size(); j++) {
//                Log.e(TAG, poiResult.getPois().get(j).getDistance() + "");
                poiResult.getPois().get(j).setDistance((int) AMapUtils.calculateLineDistance(new LatLng(poiResult.getPois().get(j).getLatLonPoint().getLatitude(), poiResult.getPois().get(j).getLatLonPoint().getLongitude()), new LatLng(mAMapLocation.getLatitude(), mAMapLocation.getLongitude())));
            }

            poiItems.addAll(poiResult.getPois());
            if (dataOnShow.size() > 0) {
                dataOnShow.clear();
            }
            dataOnShow.addAll(poiResult.getPois());

            ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(-1); // 设置没有默认选中的position
            /** 此处用Handler将数据更新放到UI Thread，是为了防止出现ListView的 “The content of the adapter has changed but ListView did not receive a notification.
             * Make sure the content of your adapter is not modified from a background thread, but only from the UI thread”问题 **/
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    aroundServiceMapListAdapter.setData(poiResult.getPois()); // 设置Adapter中当前分段的数据
                    aroundServiceViewPagerAdapter.setData(poiResult.getPois());
                    mListView.setSelection(0);
                }
            });


            addMarkersToMap(assemblyMarkerData());

            lastClickMarker = null;
//            mPtrHelper.setHasMore(poiResult.getPois().size() >= PER_PAGE);
            mSlidingUpLayout.setIsLastPage(poiResult.getPois().size() < PER_PAGE, mPage);
            if (mRequestFailView.isShown()) mRequestFailView.setVisibility(View.GONE);
        } else {
            if (mPage == DEFAULT_PATE) {
                clearMarkers();
                showNoData("未找到合适" + title);
            }
            mPage = mPage > 1 ? --mPage : mPage;
        }

        setListViewIsAtFirstPage(mPage == DEFAULT_PATE);
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (lastCheckID == -1) {
            lastCheckID = checkedId;
            if (!typeIsRote) return;
        }

        if (lastCheckID == checkedId && !typeIsRote) {
            return;
        } else {
            lastCheckID = checkedId;
        }

        mPage = mRecentMaxPage = DEFAULT_PATE;

        /**
         * modifyTime ： 2017年1月6日
         * modifyDescribe：加油站数据从高德Api获取
         */
//        if (checkedId == R.id.around_map_gas) {
//            poiItems.clear();
//            dataOnShow.clear();
//
//            dataFormGD = false;
//            title = "加油站";
//            mTitleView.setText(title);
//            mFilterContentView.setVisibility(View.GONE);
//            mSearchView.setVisibility(View.INVISIBLE);
//            mBackView.setVisibility(View.VISIBLE);
//
//            if (aroundServiceApiClient == null) aroundServiceApiClient = new AroundServiceApiClient();
//        } else

        if (checkedId == R.id.around_map_charge || checkedId == R.id.around_map_park || checkedId == R.id.around_map_gas) {
            gasStationModels.clear();
            mapGasDataOnShow.clear();

            poiItems.clear();
            dataOnShow.clear();

            dataFormGD = true;
            areaCode = defAreaCode;
            title = (checkedId == R.id.around_map_park) ? "停车场" : (checkedId == R.id.around_map_charge) ? "充电站" : "加油站";
            mTitleView.setText(title);
            mFilterContentView.setVisibility(View.VISIBLE);
            mSearchView.setVisibility(View.VISIBLE);
            mBackView.setVisibility(View.GONE);
            if (mAMapLocation != null && (districtItems == null || districtItems.size() <= 0)) getFilterData();
            if (mapListFilterAdapter != null) {
                mapListFilterAdapter.setLastClickPos(-1);
                mapListFilterAdapter.notifyDataSetChanged();
            }
            mFilterTextView.setText("全城");
        }

        mListView.changeFooterViewState(PullListView.LoadingState.LOADING_HIDDEN, 1);
        initAdapter();
        typeIsRote = typeIsRote == true ? !typeIsRote : typeIsRote;
        setAnchorPoint();
        getData(true);

        if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.HIDDEN) {
//            mSlidingUpLayout.setReFirstSize(true);
            mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.ANCHORED);
        }
    }


    @Override
    public void clickCallBack(PickupPointT pointT) {
        showProgressDialog();

        if (mAMapNavi == null) {
            mAMapNavi = AMapNavi.getInstance(this);
            mAMapNavi.addAMapNaviListener(this);
        }

        if (!TextUtils.isEmpty(pointT.tipLatitude() + "") && !TextUtils.isEmpty(pointT.tipLongitude() + "")) {
            mEndPoints.clear();
            mNaviEnd = new NaviLatLng(pointT.getLatitude(), pointT.getLongitude());
            if (mAMapNavi != null) mEndPoints.add(mNaviEnd);
        }

        calculateDriveRoute();
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) mProgressDialog = new SubmitDialog(this);
        mProgressDialog.setText("路线规划中..");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mProgressDialog.isShowing()) mProgressDialog.setCancelable(true);
            }
        }, 5000);

        mProgressDialog.show();
    }


    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    //计算驾车路线
    private void calculateDriveRoute() {
        DebugLog.d("amap", "calculateDriveRoute");
        mStartPoints.clear();
        if (mNaviStart == null) {
            mNaviStart = new NaviLatLng(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
            mStartPoints.add(mNaviStart);
        }

        if (mEndPoints.size() < 1) {
            mIsCalculateRouteSuccess = false;
            ToastHelper.showGreenToast(this, "获取导航终点信息失败");
        } else {
            mIsCalculateRouteSuccess = mAMapNavi.calculateDriveRoute(mStartPoints, mEndPoints, null, AMapNavi.DrivingDefault);
        }

        if (!mIsCalculateRouteSuccess) {
            ToastHelper.showGreenToast(this, "导航信息获取信息失败 0x1");
        }
    }


    @Override
    public void onInitNaviFailure() {

    }


    @Override
    public void onInitNaviSuccess() {

    }


    @Override
    public void onStartNavi(int i) {

    }


    @Override
    public void onTrafficStatusUpdate() {

    }


    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }


    @Override
    public void onGetNavigationText(int i, String s) {

    }


    @Override
    public void onEndEmulatorNavi() {

    }


    @Override
    public void onArriveDestination() {

    }


    @Override
    public void onCalculateRouteSuccess() {
        dismissProgressDialog();

        if (mIsCalculateRouteSuccess) {
            Intent gpsIntent = new Intent(this, SimpleNaviActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(ISEMULATOR, false);
            bundle.putInt(ACTIVITYINDEX, SIMPLEROUTENAVI);
            gpsIntent.putExtras(bundle);
            gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(gpsIntent);
        } else {
            ToastHelper.showGreenToast(this, "导航信息获取信息失败 0x2");
        }
    }


    @Override
    public void onCalculateRouteFailure(int i) {
        dismissProgressDialog();
    }


    @Override
    public void onReCalculateRouteForYaw() {

    }


    @Override
    public void onReCalculateRouteForTrafficJam() {

    }


    @Override
    public void onArrivedWayPoint(int i) {

    }


    @Override
    public void onGpsOpenStatus(boolean b) {

    }


    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }


    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }


    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }


    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }


    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }


    @Override
    public void hideCross() {

    }


    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }


    @Override
    public void hideLaneInfo() {

    }


    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }


    @Override
    public void notifyParallelRoad(int i) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAMapNavi != null) mAMapNavi.removeAMapNaviListener(this);
    }
}
