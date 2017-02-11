package com.hxqc.mall.amap.control;

/**
 * Author: wanghao
 * Date: 2016-01-27
 * FIXME
 * Todo 地图操作
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.hxqc.mall.amap.activity.SimpleNaviActivity;
import com.hxqc.mall.amap.inter.AmapDataModel;
import com.hxqc.mall.amap.utils.AMapUtils;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.ListDialog;
import com.hxqc.mall.core.views.dialog.SubmitDialog;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: wanghao
 * Date: 2015-07-27
 * 地图相关管理
 * Todo
 */
public class AmapManager implements
        AMapNaviListener,
        AMap.OnMapLoadedListener,
        LocationSource,
        AMapLocationListener {

    public static AmapManager amapManager;

//    private int[] rPositions = new int[]{};
    LatLng myCurrentLocation;
    AmapDataModel adm;
    //定位-=================================================
    //是否开始定位
    boolean isReLoc = true;
    /**
     * 从第三方店铺首页进入的  tag
     */
    private int functionActiveState = 0;
    // 起点终点坐标-----------路径规划 导航需要
    private NaviLatLng mNaviStart;
    private NaviLatLng mNaviEnd;
    // 起点终点列表---------- 路径规划     导航需要
    private ArrayList< NaviLatLng > mStartPoints = new ArrayList<>();
    private ArrayList< NaviLatLng > mEndPoints = new ArrayList<>();
    private Activity mA;
    /**
     * views
     */
    private MapView mMapView;
    private TextView mDistanceView;
    private AMap mAMap;
    private AMapNavi mAMapNavi;
    // 规划线路
    private RouteOverLay mRouteOverLay;
    // 是否驾车和是否计算成功的标志
    private boolean mIsDriveMode = true;
    private boolean mIsCalculateRouteSuccess = false;
    //是否获取到了导航信息
    private boolean is_initData_success = false;
    private SubmitDialog mProgressDialog;// 路径规划过程显示状态
    private AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    //   定位回调
    private OnLocationChangedListener mListener;
    //地图是否已加载
    private boolean mIsMapLoaded = false;

    public AmapManager() {
    }

    public static AmapManager getInstance() {
        if (amapManager == null) {
            synchronized (AmapManager.class) {
                if (amapManager == null) {
                    amapManager = new AmapManager();
                }
            }
        }
        return amapManager;
    }

    public void initModelData(AmapDataModel amapDataModel) {
        this.adm = amapDataModel;
        if (adm != null) {
            if (!TextUtils.isEmpty(adm.tipLatitude() + "") && !TextUtils.isEmpty(adm.tipLongitude() + "")) {
                LatLng latLng = MapUtils.bd_decrypt(adm.tipLatitude(), adm.tipLongitude());
                mNaviEnd = new NaviLatLng(latLng.latitude, latLng.longitude);
            }
            is_initData_success = true;
        }

        //增加终点
        mEndPoints.clear();

        if (mNaviEnd != null)
            mEndPoints.add(mNaviEnd);

        startRequestLocation();
    }

    public void setFunctionActiveState(int functionActiveState) {
        this.functionActiveState = functionActiveState;
    }

    //-----地图设置
    private void setMapSetting() {
        /**
         * LOCATION_TYPE_LOCATE 表示只在第一次定位移动到地图中心点，值为1；
         * LOCATION_TYPE_MAP_FOLLOW 表示定位、移动到地图中心点并跟随，值为2;
         * LOCATION_TYPE_MAP_ROTATE 表示定位、移动到地图中心点，跟踪并根据方向旋转地图，值为3
         */
        mAMapNavi = AMapNavi.getInstance(mA);
        mAMapNavi.setAMapNaviListener(this);

        int myLocationType = AMap.LOCATION_TYPE_LOCATE;//
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        mAMap.setMyLocationType(myLocationType);
        mAMap.setOnMapLoadedListener(this);
        mAMap.setTrafficEnabled(true);

        mAMap.getUiSettings().setCompassEnabled(false);
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);
        mAMap.getUiSettings().setScaleControlsEnabled(true);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
    }

    //-----地图监听
    private void setListeners() {
        mAMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        mRouteOverLay = new RouteOverLay(mAMap, null);
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new SubmitDialog(mA);
        mProgressDialog.setText("路线规划中..");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mProgressDialog.isShowing())
                    mProgressDialog.setCancelable(true);
            }
        }, 5000);

        mProgressDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    //初始化导航
    private void initNavi() {
        mAMapNavi = AMapNavi.getInstance(mA);
        AMapNaviPath naviPath = mAMapNavi.getNaviPath();
        if (naviPath == null) {
            return;
        }
        // 获取路径规划线路，显示到地图上
        mRouteOverLay.setRouteInfo(naviPath);
        mRouteOverLay.addToMap();
        if (mIsMapLoaded) {
            mRouteOverLay.zoomToSpan();
        }

        //距离
        double length = ((int) (naviPath.getAllLength() / (double) 1000 * 10))
                / (double) 10;

        if (mDistanceView != null)
            mDistanceView.setText(String.valueOf(length) + " km");
    }

    /**
     * ----生命周期-------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public final void onCreate(Bundle savedInstanceState, Activity m, MapView mapView, TextView distance) {

        this.mMapView = mapView;
        this.mDistanceView = distance;
        this.mA = m;
        mMapView.onCreate(savedInstanceState);
        showProgressDialog();

        mAMap = mMapView.getMap();
        setMapSetting();
        setListeners();
//        TTSController ttsManager = TTSController.getInstance(mA.getApplicationContext());// 初始化语音模块
//        ttsManager.init();
//        mAMapNavi.addAMapNaviListener(ttsManager);
    }

    public final void onSaveInstanceState(Bundle savedInstanceState) {
        DebugLog.d("daohang", "onSaveInstanceState");
        mMapView.onSaveInstanceState(savedInstanceState);
    }

    public final void onResume() {
        DebugLog.d("daohang", "onResume");
        mMapView.onResume();
        if (mIsMapLoaded) {
            if (mRouteOverLay != null)
                mRouteOverLay.zoomToSpan();
        }
        startRequestLocation();
        initNavi();
//        TTSController.getInstance(mA.getApplicationContext()).startSpeaking();
    }

    public final void onPause() {
        DebugLog.d("daohang", "onPause");
        mMapView.onPause();
        deactivate();
//        TTSController.getInstance(mA.getApplicationContext()).stopSpeaking();
    }

    public final void onDestroy() {
        DebugLog.d("daohang", "onDestroy");
        if (mMapView != null)
            mMapView.onDestroy();
        destroyInstance();
    }

    //销毁实例   释放内存
    private void destroyInstance() {
//        AMapNavi.getInstance(mA).removeAMapNaviListener(this);
        AMapNavi.getInstance(mA).destroy();// 销毁导航
//        TTSController.getInstance(mA.getApplicationContext()).stopSpeaking();
//        TTSController.getInstance(mA.getApplicationContext()).destroy();

        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }

        if (amapManager != null)
            amapManager = null;
    }

    /**
     * ---------监听回调--------------------路径规划----导航----------------------------------------------------------------------------------------------
     */
    @Override
    public void onInitNaviFailure() {
        DebugLog.d("daohang", "onInitNaviFailure");
    }

    @Override
    public void onInitNaviSuccess() {
        DebugLog.d("daohang", "onInitNaviSuccess");

    }

    @Override
    public void onStartNavi(int i) {
        DebugLog.d("daohang", "onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {
        DebugLog.d("daohang", "onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        DebugLog.d("daohang", "onLocationChange");
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        DebugLog.d("daohang", "onGetNavigationText:- " + s);
    }

    @Override
    public void onEndEmulatorNavi() {
        DebugLog.d("daohang", "onEndEmulatorNavi");

    }

    @Override
    public void onArriveDestination() {
        DebugLog.d("daohang", "onArriveDestination");
    }

    @Override
    public void onCalculateRouteSuccess() {
        AMapNaviPath naviPath = mAMapNavi.getNaviPath();
        if (naviPath == null) {
            return;
        }
        // 获取路径规划线路，显示到地图上
        mRouteOverLay.setRouteInfo(naviPath);
        mRouteOverLay.addToMap();
        mRouteOverLay.zoomToSpan();
        double length = ((int) (naviPath.getAllLength() / (double) 1000 * 10))
                / (double) 10;

        if (mDistanceView != null) {
            mDistanceView.setText(String.valueOf(length) + " km");
        }

        mIsCalculateRouteSuccess = true;

        dissmissProgressDialog();

        if (functionActiveState == AMapUtils.FROM_THIRD_PART_SHOP_NAVI) {
            clickToNavi();
        } else if (functionActiveState == AMapUtils.FROM_THIRD_PART_SHOP_LOCATION) {
            reLoc();
        }
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        showToast("路径规划出错,请检查网络连接");
        mIsCalculateRouteSuccess = false;
        dissmissProgressDialog();
    }

    @Override
    public void onReCalculateRouteForYaw() {
        DebugLog.d("daohang", "onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        DebugLog.d("daohang", "onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        DebugLog.d("daohang", "onArrivedWayPoint");
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        DebugLog.d("daohang", "onGpsOpenStatus");
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        DebugLog.d("daohang", "onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        DebugLog.d("daohang", "onNaviInfoUpdate");
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

    /**
     * 初始化定位
     */
    private void startRequestLocation() {
        // 初始化定位，只采用网络定位
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mA);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }else {
            if (!mlocationClient.isStarted()){
                mlocationClient.startLocation();
            }
        }
    }

    private void animMoveToLL() {
        if (mlocationClient!=null){
            AMapLocation lastKnownLocation = mlocationClient.getLastKnownLocation();
            DebugLog.i("amap", "reLoc -------->" + lastKnownLocation.toString());
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(myCurrentLocation, 14, 0, 30)), 600, null);
        }else {
            startRequestLocation();
            animMoveToLL();
        }
    }

    /**
     * 定位资源回调操作
     * <p/>
     * 定位改变监听
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        startRequestLocation();
        DebugLog.d("daohang", "activate");
    }

    @Override
    public void deactivate() {
        DebugLog.d("daohang", "deactivate");
        mListener = null;
        if (mlocationClient != null) {
            if (mlocationClient.isStarted())
                mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onMapLoaded() {
        DebugLog.d("daohang", "onMapLoaded");
        mIsMapLoaded = true;
        if (mRouteOverLay != null) {
            mRouteOverLay.zoomToSpan();
        }
    }

    //GPS 导航
    private void startGPSNavi(boolean isDrive) {
        if ((isDrive && mIsDriveMode && mIsCalculateRouteSuccess)
                || (!isDrive && !mIsDriveMode && mIsCalculateRouteSuccess)) {
            Intent gpsIntent = new Intent(mA, SimpleNaviActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(AMapUtils.ISEMULATOR, false);
            bundle.putInt(AMapUtils.ACTIVITYINDEX, AMapUtils.SIMPLEROUTENAVI);
            gpsIntent.putExtras(bundle);
            gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            mA.startActivity(gpsIntent);
        } else {
            showToast("请先进行相对应的路径规划，再进行导航");
        }
    }

    /**
     * 放出的地图 操作-----------------------------------------------------------------------------------------------------------------------------------------
     */
    //----------------重新定位
    public void reLoc() {
        DebugLog.i("amap", "reLoc");
        if (myCurrentLocation != null) {
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(myCurrentLocation, 14, 0, 30)), 600, null);
        } else {
            animMoveToLL();
        }
    }

    public void clickToNavi() {
        if (mIsCalculateRouteSuccess) {
            startGPSNavi(true);
        } else {
            if (is_initData_success) {
                ToastHelper.showRedToast(mA, "网络请求失败...");
            } else {
                ToastHelper.showRedToast(mA, "获取数据失败...");
            }
        }
    }

    public void shopCall() {
        String number;
        if (is_initData_success) {
            number = adm.tipPhoneNumber();
        } else {
            ToastHelper.showRedToast(mA, "获取数据失败");
            return;
        }

        if (TextUtils.isEmpty(number)) {
            ToastHelper.showRedToast(mA, "获取店铺电话失败");
            return;
        }

        if (number.contains(",")) {
            final String[] numbers = number.split(",");
            ListDialog dialog1 = new ListDialog(mA, "点击拨号", numbers) {
                @Override
                public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                    openDial(numbers[position]);
                }

                @Override
                protected void doNext(int position) {
                }
            };
            dialog1.show();
        } else {
            openDial(number);
        }
    }

    //拨打电话
    public void openDial(String str_num) {
        if (str_num.contains("(")) {
            str_num = str_num.replace("(", "");
        }
        if (str_num.contains(")")) {
            str_num = str_num.replace(")", "");
        }
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialIntent.setData(Uri.parse("tel:" + str_num));
        mA.startActivity(dialIntent);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                myCurrentLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                DebugLog.d("daohang", "onLocationChanged");
                if (isReLoc) {
                    isReLoc = false;
                    initData();
                }
            } else {
                DebugLog.e("daohang", "Location ERR:" + amapLocation.getErrorCode());
            }
        }
    }

    private void initData() {
        calculateDriveRoute();
        initNavi();
    }

    //计算驾车路线
    private void calculateDriveRoute() {
        DebugLog.d("amap", "calculateDriveRoute");
        mStartPoints.clear();
        mNaviStart = new NaviLatLng(myCurrentLocation.latitude, myCurrentLocation.longitude);
        mStartPoints.add(mNaviStart);

        if (mEndPoints.size() < 1) {
            showToast("获取信息失败");
            mIsCalculateRouteSuccess = false;
        } else {
            mIsCalculateRouteSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
                    mEndPoints, null, AMapNavi.DrivingDefault);
        }

        if (!mIsCalculateRouteSuccess) {
            showToast("路线计算失败..,请检查网络");
        }
    }

    private void showToast(String message) {
        ToastHelper.showRedToast(mA, message);
    }
}
