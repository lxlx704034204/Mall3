package com.hxqc.mall.amap.control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.hxqc.mall.amap.activity.SimpleNaviActivity;
import com.hxqc.mall.amap.control.techniques.BaseAMapDataHelper;
import com.hxqc.mall.amap.control.techniques.MapChargeData;
import com.hxqc.mall.amap.control.techniques.MapGasData;
import com.hxqc.mall.amap.control.techniques.MapParkData;
import com.hxqc.mall.amap.utils.AMapUtils;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.SubmitDialog;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import java.util.ArrayList;


/**
 * Author: wanghao
 * Date: 2016-04-07
 * FIXME
 * Todo   地图相关功能
 */
public class AMapAroundManager implements AMapNaviListener, OnMapLoadedListener, LocationSource, AMapLocationListener, AMap.OnCameraChangeListener {

    public static AMapAroundManager aMapManager;
    private SubmitDialog mProgressDialog;// 路径规划过程显示状态
    private Activity mA;
    private MapView mMapView;
    private AMap mAMap;
    private AMapNavi mAMapNavi;

    // 起点终点列表---------- 路径规划     导航需要=====================
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<>();
    //===============导航起点 终点 参数列表==========================

    LatLng myCurrentLocation;//司机当前位置记录
    LatLng centerPosition;//当前中心点坐标
    float screenDensity;//当前屏幕密度
    OnLocationInitSuccessListener onLocationInitSuccessListener;//定位成功接口
    OnDataRequestListener onDataRequestListener;//请求数据成功与否回调
    boolean isInFirst = true;//是否是初次进入界面
    boolean isFirstScale = true;//是否是第一次缩放地图

    private boolean is_init_location_success = false; //是否定位成功
    private double MAXLIMITDISTANCE = 800;//最大限制 移动距离

    /**
     * ====定位==========start================
     */
    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;//   地图默认 定位回调
//    private int currentShowMarkStatus = 0;  //当前激活显示的marker类型

    /**
     * 当前显示的mark   数据
     */
    BaseAMapDataHelper currentMarkData;

    public AMapAroundManager() {
    }

    public static AMapAroundManager getInstance() {
        if (aMapManager == null) {
            synchronized (AMapAroundManager.class) {
                if (aMapManager == null) {
                    aMapManager = new AMapAroundManager();
                }
            }
        }
        return aMapManager;
    }

    public void setOnLocationInitSuccessListener(OnLocationInitSuccessListener onLocationInitSuccessListener) {
        this.onLocationInitSuccessListener = onLocationInitSuccessListener;
    }

    public void setOnDataRequestListener(OnDataRequestListener onDataRequestListener) {
        this.onDataRequestListener = onDataRequestListener;
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog(String text, boolean cancelable) {
        if (mProgressDialog == null)
            mProgressDialog = new SubmitDialog(mA);
        mProgressDialog.setText(text);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(cancelable);

        if (!mProgressDialog.isShowing())
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

    /**
     * 实现的地图接口=======================================================
     */

    //-----地图设置
    private void setMapSetting() {
        /**
         * LOCATION_TYPE_LOCATE 表示只在第一次定位移动到地图中心点，值为1；
         * LOCATION_TYPE_MAP_FOLLOW 表示定位、移动到地图中心点并跟随，值为2;
         * LOCATION_TYPE_MAP_ROTATE 表示定位、移动到地图中心点，跟踪并根据方向旋转地图，值为3
         */
        int myLocationType = AMap.LOCATION_TYPE_LOCATE;//
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        mAMap.setMyLocationType(myLocationType);
        mAMap.setOnMapLoadedListener(this);
        mAMap.setOnCameraChangeListener(this);


        mAMap.getUiSettings().setCompassEnabled(false);
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);
        mAMap.getUiSettings().setScaleControlsEnabled(true);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.getUiSettings().setTiltGesturesEnabled(false);
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    //-----地图监听
    private void setListeners() {
        mAMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
    }

    private void showToast(String message) {
        ToastHelper.showRedToast(mA, message);
    }

    /**
     * navigation==================================================================
     */
    /**
     * 开始 gps导航----------
     */
    private void startGPSNavi() {
        DebugLog.e("initNaviPoint", "startGPSNavi");
        Intent gpsIntent = new Intent(mA, SimpleNaviActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(AMapUtils.ISEMULATOR, false);
        bundle.putInt(AMapUtils.ACTIVITYINDEX, AMapUtils.SIMPLEROUTENAVI);
        gpsIntent.putExtras(bundle);
        gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mA.startActivity(gpsIntent);
    }

    /**
     * 初始化 导航和路径 -----------
     */
    private void initNaviAndRoute() {
        DebugLog.e("initNaviPoint", "initNaviAndRoute");
        if (mAMapNavi != null) {
            mAMapNavi.removeAMapNaviListener(this);
            mAMapNavi.destroy();
        }

        mAMapNavi = AMapNavi.getInstance(mA);
        mAMapNavi.addAMapNaviListener(this);

//        TTSController ttsManager = TTSController.getInstance(mA.getApplicationContext());// 初始化语音模块
//        ttsManager.init();
//        mAMapNavi.addAMapNaviListener(ttsManager);// 设置语音模块播报
    }

    /**
     * ---开始计算规划路径---------------------
     */
    private void startCalculateRoute() {
        boolean b = mAMapNavi.calculateDriveRoute(mStartPoints, mEndPoints, null, PathPlanningStrategy.DRIVING_DEFAULT);
        DebugLog.e("initNaviPoint", "initNaviAndRoute calculateDriveRoute: " + b);
    }

    /**
     * ----生命周期-------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public final void onCreate(Bundle savedInstanceState, Activity m, MapView mapView) {
        this.mMapView = mapView;
        this.mA = m;

        screenDensity = ScreenUtil.getScreenDensity(mA);
        mMapView.onCreate(savedInstanceState);
        showProgressDialog("初始化定位", true);
        mAMap = mMapView.getMap();
        initMap();
    }

    private void initMap() {
        setMapSetting();
        setListeners();
    }

    public final void onSaveInstanceState(Bundle savedInstanceState) {
        mMapView.onSaveInstanceState(savedInstanceState);
    }

    public final void onResume() {
        mMapView.onResume();
        startRequestLocation();
//        TTSController.getInstance(mA.getApplicationContext()).startSpeaking();
    }

    public final void onPause() {
        mMapView.onPause();
        deactivate();
//        TTSController.getInstance(mA.getApplicationContext()).stopSpeaking();
    }

    public final void onDestroy() {
        destroyInstance();
    }

    //销毁实例   释放内存
    private void destroyInstance() {
        mListener = null;

        clearMarks();

        if (mMapView != null)
            mMapView.onDestroy();

        deactivate();

        if (mAMapNavi != null) {
            mAMapNavi.removeAMapNaviListener(this);
            mAMapNavi.destroy();
        }

//        TTSController.getInstance(mA.getApplicationContext()).stopSpeaking();
//        TTSController.getInstance(mA.getApplicationContext()).destroy();

        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }

        if (aMapManager != null)
            aMapManager = null;
    }

    /**
     * 初始化定位
     */
    private void startRequestLocation() {
        // 初始化定位，只采用网络定位
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mA.getApplicationContext());
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
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
        } else {
            if (!mlocationClient.isStarted()) {
                mlocationClient.startLocation();
            }
        }

        DebugLog.i("daohang", "startRequestLocation: " + mlocationClient.isStarted());
    }

    private void animMoveToLL() {
        if (mlocationClient != null) {
            AMapLocation lastKnownLocation = mlocationClient.getLastKnownLocation();
            DebugLog.i("amap", "reLoc -------->" + lastKnownLocation.toString());
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(myCurrentLocation, 14, 0, 30)), 600, null);
        } else {
            startRequestLocation();
            animMoveToLL();
        }
    }

    /**
     * location=========================================================
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        startRequestLocation();
    }

    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            if (mlocationClient.isStarted())
                mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        DebugLog.i("daohang", "onLocationChanged");
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                myCurrentLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                DebugLog.d("daohang", "onLocationChanged:" + myCurrentLocation.toString());
                dissmissProgressDialog();

                is_init_location_success = true;

                if (isInFirst) {
                    isInFirst = false;
                    centerPosition = myCurrentLocation;
                    if (onLocationInitSuccessListener != null) {
                        onLocationInitSuccessListener.onLocationChangeSuccess();
                    }
                }


            } else {
                DebugLog.e("daohang", "Location ERR:" + amapLocation.getErrorCode());
            }
        }
    }

    /**
     * Navigation==========================================================
     */
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        DebugLog.e("initNaviPoint", "onLocationChange");
    }

    //导航初始化失败
    @Override
    public void onInitNaviFailure() {
        DebugLog.e("initNaviPoint", "onInitNaviFailure");
        showToast("初始化导航失败");
    }

    //导航初始化成功
    @Override
    public void onInitNaviSuccess() {
        DebugLog.e("initNaviPoint", "onInitNaviSuccess");
        /**
         * 初始化导航成功计算路径
         */
        startCalculateRoute();
    }

    @Override
    public void onStartNavi(int i) {
        DebugLog.e("initNaviPoint", "onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {
        DebugLog.e("initNaviPoint", "onEndEmulatorNavi");
    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {
        DebugLog.e("initNaviPoint", "onCalculateRouteSuccess");
//        isCalculateRouteSuccess = true;
        startGPSNavi();
        dissmissProgressDialog();
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        DebugLog.e("initNaviPoint", "onCalculateRouteFailure");
        showToast("路径规划出错,请检查网络连接");
//        isCalculateRouteSuccess = false;
        dissmissProgressDialog();
    }

    @Override
    public void onReCalculateRouteForYaw() {
        DebugLog.e("initNaviPoint", "onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        DebugLog.e("initNaviPoint", "onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        DebugLog.e("initNaviPoint", "onArrivedWayPoint");
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        DebugLog.e("initNaviPoint", "onGpsOpenStatus: " + b);
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        DebugLog.e("initNaviPoint", "onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        DebugLog.e("initNaviPoint", "onNaviInfoUpdate");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        DebugLog.e("initNaviPoint", "OnUpdateTrafficFacility");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        DebugLog.e("initNaviPoint", "OnUpdateTrafficFacility");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        DebugLog.e("initNaviPoint", "showCross");
    }


    /**
     * end======implement  interface End==============
     */

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
    public void onMapLoaded() {
        DebugLog.d("daohang", "onMapLoaded");
    }

    /**
     * 监听地图缩放移动 位置
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng temp_ll = null;

        if (mAMap != null) {
            Point p = mAMap.getProjection().toScreenLocation(cameraPosition.target);
            temp_ll = mAMap.getProjection().fromScreenLocation(new Point(p.x, p.y));
        }

        if (temp_ll != null && centerPosition != null) {
            DebugLog.d("Camera", "onCameraChangeFinish: " + cameraPosition.toString());
            DebugLog.i("Camera", "onCameraChangeFinish:temp_ll " + temp_ll.toString());
            DebugLog.i("Camera", "onCameraChangeFinish:centerPosition" + centerPosition.toString());
            DebugLog.i("Camera", "onCameraChangeFinish getDistance: " + getDistance(centerPosition, temp_ll));
            if (getDistance(centerPosition, temp_ll) > MAXLIMITDISTANCE) {
                centerPosition = temp_ll;

                if (is_init_location_success) {
                    if (currentMarkData != null) {
                        currentMarkData.searchForName(centerPosition);
                    }
                }
            }
        }


    }

    //计算距离
    private double getDistance(LatLng ml1, LatLng tl2) {
        return com.amap.api.maps.AMapUtils.calculateLineDistance(ml1, tl2);
    }

    /**
     * 放出方法 ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    //当前屏幕地图中心点坐标
    public LatLng getCenterPosition() {
        if (centerPosition == null) {
            CameraPosition cameraPosition = mAMap.getCameraPosition();
            if (mAMap != null) {
                Point p = mAMap.getProjection().toScreenLocation(cameraPosition.target);
                centerPosition = mAMap.getProjection().fromScreenLocation(new Point(p.x, p.y));
            }
        }
        return centerPosition;
    }

    private void addMarkListener() {
        if (currentMarkData != null) {

            currentMarkData.setCallBack(new MapMarkOperateCallBack() {
                @Override
                public void onNavigationClick(NaviLatLng end) {
                    startNaviPoint(end);
                }

                @Override
                public void moveCamera(int scaleGrade, boolean isReloc) {
                    moveCameraToThat(scaleGrade, isReloc);
                }
            });

            currentMarkData.setMarkersRequestListener(new OnMarkersRequestListener() {
                @Override
                public void markerDataRSuccess() {
                    if (onDataRequestListener!=null)
                        onDataRequestListener.requestSuccess();
                }

                @Override
                public void markerDataRFail() {
                    if (onDataRequestListener!=null)
                        onDataRequestListener.requestFail();
                }
            });
        }
    }

    private void clearMarks() {
        if (currentMarkData != null) {
            currentMarkData.destoryMarkers();
            currentMarkData = null;
        }
    }

    //搜索加油站显示
    public void showGasStation(boolean isShow) {
        DebugLog.w("Map_Tag", "showGasStation : " + isShow);
        if (is_init_location_success) {
            clearMarks();
            if (isShow) {
                currentMarkData = new MapGasData(mA, mAMap);
                addMarkListener();
                currentMarkData.searchForName(centerPosition);
            }else {
                if (onDataRequestListener!=null)
                    onDataRequestListener.requestSuccess();
            }
        }
    }

    //搜索停车场
    public void searchAroundPark(boolean isShow) {
        DebugLog.w("Map_Tag", "searchAroundPark : " + isShow);
        if (is_init_location_success) {
            clearMarks();
            if (isShow) {
                currentMarkData = new MapParkData(mA, mAMap);
                addMarkListener();
                currentMarkData.searchForName(centerPosition);
            }else {
                if (onDataRequestListener!=null)
                    onDataRequestListener.requestSuccess();
            }
        }
    }

    //搜索充电站
    public void searchAroundCharger(boolean isShow) {
        DebugLog.w("Map_Tag", "searchAroundCharger : " + isShow);
        if (is_init_location_success) {
            clearMarks();
            if (isShow) {
                currentMarkData = new MapChargeData(mA, mAMap);
                addMarkListener();
                currentMarkData.searchForName(centerPosition);
            }else {
                if (onDataRequestListener!=null)
                    onDataRequestListener.requestSuccess();
            }
        }
    }

    //开启路况
    public void openTraffic() {
        if (mAMap != null) {
            mAMap.setTrafficEnabled(true);
        }
    }

    //关闭路况
    public void closeTraffic() {
        if (mAMap != null) {
            mAMap.setTrafficEnabled(false);
        }
    }

    //设置缩小
    public void reduceMapView() {
        changeCamera(CameraUpdateFactory.zoomOut(), null);
    }

    //设置放大
    public void increaseMapView() {
        changeCamera(CameraUpdateFactory.zoomIn(), null);
    }

    /**
     * 缩放地图
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        if (mAMap != null)
            mAMap.animateCamera(update, 200, callback);
    }

    /**
     * 移动镜头
     */
    private boolean moveCameraToThat(int num, boolean isReloc) {
        if (centerPosition != null && myCurrentLocation != null && mAMap != null) {
            if (isReloc) {
                mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(myCurrentLocation, num, 0, 30)), 600, null);
            } else {
                if (isFirstScale) {
                    isFirstScale = false;
                    mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(centerPosition, num, 0, 30)), 600, null);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    //----------------重新定位
    public void reLoc() {
        DebugLog.i("amap", "reLoc");
        if (!moveCameraToThat(14, true)) {
            animMoveToLL();
        }
    }

    /**
     * 调用开始导航
     *
     * @param endLocation 目的地坐标
     */
    public void startNaviPoint(NaviLatLng endLocation) {

        showProgressDialog("路线初始化", true);
        NaviLatLng mNaviStart = new NaviLatLng(myCurrentLocation.latitude, myCurrentLocation.longitude);

        DebugLog.e("initNaviPoint", "initNaviPoint");
        DebugLog.e("initNaviPoint", "initNaviPoint start:" + mNaviStart.toString());

        mStartPoints.clear();
        mStartPoints.add(mNaviStart);
        mEndPoints.clear();
        if (endLocation == null) {
            dissmissProgressDialog();
            showToast("目的地有误");
        } else {
            DebugLog.e("initNaviPoint", "initNaviPoint end:" + endLocation.toString());
            mEndPoints.add(endLocation);
            initNaviAndRoute();
        }
    }

    /**
     * 定位成功回调接口
     */
    public interface OnLocationInitSuccessListener {
        void onLocationChangeSuccess();
    }

    public interface OnDataRequestListener{
        void requestSuccess();
        void requestFail();
    }

}
