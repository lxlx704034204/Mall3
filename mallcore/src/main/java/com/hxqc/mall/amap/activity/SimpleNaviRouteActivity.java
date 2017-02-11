//package com.hxqc.mall.amap.activity;
//
//import android.content.Intent;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.BitmapDescriptor;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.CameraPosition;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.navi.AMapNavi;
//import com.amap.api.navi.AMapNaviListener;
//import com.amap.api.navi.model.AMapNaviInfo;
//import com.amap.api.navi.model.AMapNaviLocation;
//import com.amap.api.navi.model.AMapNaviPath;
//import com.amap.api.navi.model.NaviInfo;
//import com.amap.api.navi.model.NaviLatLng;
//import com.amap.api.navi.view.RouteOverLay;
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.activity.BackActivity;
//import com.hxqc.mall.amap.control.TTSController;
//import com.hxqc.mall.amap.utils.AMapUtils;
//import com.hxqc.mall.core.R;
//import com.hxqc.mall.core.model.auto.PickupPointT;
//import com.hxqc.mall.core.model.order.OrderModel;
//import com.hxqc.mall.core.util.MapUtils;
//import com.hxqc.mall.core.util.ToastHelper;
//import com.hxqc.mall.core.views.ThirdFunctionButton;
//import com.hxqc.mall.core.views.dialog.ListDialog;
//import com.hxqc.mall.core.views.dialog.SubmitDialog;
//import com.hxqc.util.DebugLog;
//import com.hxqc.util.JSONUtils;
//
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * 高德地图导航 路线规划
// */
//@Deprecated
//public class SimpleNaviRouteActivity extends BackActivity implements
//        AMapNaviListener, AMapLocationListener, LocationSource, AMap.OnMapLoadedListener {
//    LatLng myLL;
//    OrderModel orderDetail;
//    PickupPointT pickupPoint;
//    //是否开始定位
//    boolean isReLoc = true;
//    // 地图和导航资源
//    private MapView mapView;
//    /*
//    --------------------------------------------------------------------------------------
//     */
//    private AMap mAMap;
//    private AMapNavi mAMapNavi;
//    /**
//     * 从第三方店铺首页进入的  tag
//     */
//    private int functionActiveState = 0;
//    private boolean isFromShop = false;
//    // 起点终点坐标
//    private NaviLatLng mNaviStart;
//    private NaviLatLng mNaviEnd;
//    // 起点终点列表
//    private ArrayList< NaviLatLng > mStartPoints = new ArrayList< NaviLatLng >();
//    private ArrayList< NaviLatLng > mEndPoints = new ArrayList< NaviLatLng >();
//    // 规划线路
//    private RouteOverLay mRouteOverLay;
//    // 是否驾车和是否计算成功的标志
//    private boolean mIsDriveMode = true;
//    private boolean mIsCalculateRouteSuccess = false;
//    //路径规划是否成功
//    private boolean isCalculateRouteSuccess = false;
//    private TextView mNavi;//商城导航
//    private TextView mCall;//商城打电话
//    private TextView mShop;//商城店铺
//    private TextView mDistance;//商城距离
//    private TextView mMyLoc;
//    //底部功能键
//    private LinearLayout mallBottomBarView;
//    private LinearLayout shopBottomBarView;
//    //第三方店铺
//    private ThirdFunctionButton mShopMyLoc;
//    private ThirdFunctionButton mShopCall;
//    private ThirdFunctionButton mShopNavi;
//
//    //TODO  路线规划失败重新规划
//    //是否获取到了导航信息
//    private boolean is_initData_success = false;
//    private SubmitDialog mProgressDialog;// 路径规划过程显示状态
//    private LocationManagerProxy mLocationManagerProxy;
//    /**
//     * 初始化路线描述信息和加载线路
//     */
//    //地图是否已加载
//    private boolean mIsMapLoaded = false;
//    //   定位回调
//    private OnLocationChangedListener mListener;
//    private LocationManagerProxy mAMapLocationManager;
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_simple_navi_route);
//        getSupportActionBar().setTitle("路线规划");
//
//        functionActiveState = getIntent().getIntExtra("map_opeator", 0);
//
//        PickupPointT shopLocation = getIntent().getParcelableExtra("shop_location");
///**
// * 添加店铺地址进入
// */
//        if (shopLocation == null) {
//            isFromShop = false;
//            orderDetail = JSONUtils.fromJson(getIntent().getStringExtra("order_detail"), new TypeToken< OrderModel >() {
//            });
//        } else {
//            isFromShop = true;
//            pickupPoint = shopLocation;
//            if (!TextUtils.isEmpty(pickupPoint.latitude) && !TextUtils.isEmpty(pickupPoint.longitude)) {
//                LatLng latLng = MapUtils.bd_decrypt(Double.parseDouble(pickupPoint.latitude), Double.parseDouble
//                        (pickupPoint.longitude));
//                mNaviEnd = new NaviLatLng(latLng.latitude, latLng.longitude);
//            }
//
//            is_initData_success = true;
//        }
//
//        if (orderDetail != null) {
//            pickupPoint = orderDetail.PickupPoint;
//            if (!TextUtils.isEmpty(pickupPoint.latitude) && !TextUtils.isEmpty(pickupPoint.longitude)) {
//                LatLng latLng = MapUtils.bd_decrypt(Double.parseDouble(pickupPoint.latitude), Double.parseDouble
//                        (pickupPoint.longitude));
//                mNaviEnd = new NaviLatLng(latLng.latitude, latLng.longitude);
//            }
//
//            is_initData_success = true;
//        }
//
//        TTSController ttsManager = TTSController.getInstance(this);// 初始化语音模块
//        ttsManager.init();
//        AMapNavi.getInstance(this).setAMapNaviListener(ttsManager);// 设置语音模块播报
//
//        mapView = (MapView) findViewById(R.id.simple_route_map);
//        mapView.onCreate(savedInstanceState);
//        showProgressDialog();
//        initView();
//
//        //我的位置----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//        mMyLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reLoc();
//            }
//        });
//        mShopMyLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reLoc();
//            }
//        });
//
//        //一键导航-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//        mNavi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickToNavi();
//            }
//        });
//        mShopNavi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickToNavi();
//            }
//        });
//
//        //拨打电话--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//        mCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shopCall();
//            }
//        });
//        mShopCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shopCall();
//            }
//        });
//
////        addMarkersToMap();
//    }
//
//    private void clickToNavi() {
//        if (isCalculateRouteSuccess) {
////                    startEmulatorNavi(true);
//            startGPSNavi(true);
//        } else {
//            if (is_initData_success) {
//                ToastHelper.showOrangeToast(SimpleNaviRouteActivity.this, "网络请求失败...");
////                        calculateDriveRoute();
//            } else {
//                ToastHelper.showOrangeToast(SimpleNaviRouteActivity.this, "获取数据失败...");
//            }
//        }
//    }
//
//    private void shopCall() {
//        String number;
//        if (is_initData_success) {
//            number = pickupPoint.tel;
//        } else {
//            ToastHelper.showOrangeToast(SimpleNaviRouteActivity.this, "获取数据失败");
//            return;
//        }
//
//        if (TextUtils.isEmpty(number)) {
//            ToastHelper.showOrangeToast(SimpleNaviRouteActivity.this, "获取店铺电话失败");
//            return;
//        }
//
//        if (number.contains(",")) {
//            final String[] numbers = number.split(",");
//            ListDialog dialog1 = new ListDialog(SimpleNaviRouteActivity.this, "点击拨号", numbers) {
//                @Override
//                public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
//                    openDial(numbers[position]);
//                }
//
//                @Override
//                protected void doNext(int position) {
//                }
//            };
//            dialog1.show();
//        } else {
//            openDial(number);
//        }
//    }
//
//    private void addMarkersToMap() {
//        LatLng latLng = MapUtils.bd_decrypt(30.553996, 114.211157);
//        ArrayList< BitmapDescriptor > giflist = new ArrayList< BitmapDescriptor >();
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//
//        MarkerOptions markerOption1 = new MarkerOptions().anchor(0.5f, 0.5f)
//                .position(latLng).title("成都市")
//                .snippet("成都市:30.679879, 104.064855").icons(giflist)
//                .perspective(true).draggable(false).period(50);
////        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
////        markerOptionlst.add(markerOption1);
////        aMap.addMarkers(markerOptionlst, true);
//        mAMap.addMarker(markerOption1);
//        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                marker.showInfoWindow();
//                return false;
//            }
//        });
//        mAMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                return null;
//            }
//        });
//    }
//
//    //重新定位---------------------------------------------------------------------------------------------------------------------
//    private void reLoc() {
//        DebugLog.i("amap", "reLoc");
//        if (mAMapLocationManager != null) {
//            DebugLog.i("amap", "reLoc -------->");
//            locationProxyActive();
//        } else {
//            mAMapLocationManager = LocationManagerProxy.getInstance(this);
//            locationProxyActive();
//        }
//    }
//
//    private void locationProxyActive() {
//        if (mAMapLocationManager != null) {
//            mAMapLocationManager.requestLocationData(
//                    LocationProviderProxy.AMapNetwork, -1, 10, this);
//            AMapLocation lastKnownLocation = mAMapLocationManager.getLastKnownLocation(LocationProviderProxy
//                    .AMapNetwork);
//            DebugLog.i("amap", "reLoc -------->" + lastKnownLocation.toString());
//            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15, 0, 30)), 600,
//                    null);
//        }
//    }
//
//    //---------------------------------------------------------------------------------------------------------------------------------------------
//    private void initData() {
//        calculateDriveRoute();
//        initNavi();
////        onResume();
//    }
//
//    //拨打电话
//    public void openDial(String str_num) {
//        if (str_num.contains("(")) {
//            str_num = str_num.replace("(", "");
//        }
//        if (str_num.contains(")")) {
//            str_num = str_num.replace(")", "");
//        }
//        Intent dialIntent = new Intent();
//        dialIntent.setAction(Intent.ACTION_DIAL);
//        dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        dialIntent.setData(Uri.parse("tel:" + str_num));
//        startActivity(dialIntent);
//    }
//
//    /**
//     * 初始化定位
//     */
//    private void init() {
//        // 初始化定位，只采用网络定位
//        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
//        mLocationManagerProxy.setGpsEnable(false);
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用destroy()方法
//        // 其中如果间隔时间为-1，则定位只定一次,
//        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
//        mLocationManagerProxy.requestLocationData(
//                LocationProviderProxy.AMapNetwork, 5 * 1000, 15, this);
//
//    }
//
//    // 初始化View
//    private void initView() {
//        mallBottomBarView = (LinearLayout) findViewById(R.id.mall_amap_bottom_bar);
//        shopBottomBarView = (LinearLayout) findViewById(R.id.shop_amap_bottom_bar);
//
//        if (isFromShop) {
//            mallBottomBarView.setVisibility(View.GONE);
//            shopBottomBarView.setVisibility(View.VISIBLE);
//        } else {
//            mallBottomBarView.setVisibility(View.VISIBLE);
//            shopBottomBarView.setVisibility(View.GONE);
//        }
//
////第三方店铺底部
//        mShopCall = (ThirdFunctionButton) findViewById(R.id.shop_amap_phone);
//        mShopMyLoc = (ThirdFunctionButton) findViewById(R.id.shop_my_loc);
//        mShopNavi = (ThirdFunctionButton) findViewById(R.id.shop_amap_navi);
//
////商城底部
//        mNavi = (TextView) findViewById(R.id.tv_amap_navi);
//        mCall = (TextView) findViewById(R.id.tv_amap_phone);
//        mShop = (TextView) findViewById(R.id.tv_amap_shop);
//        mDistance = (TextView) findViewById(R.id.tv_amap_distance);
//        mMyLoc = (TextView) findViewById(R.id.tv_my_loc);
//
//        if (is_initData_success) {
//            mShop.setText(pickupPoint.address);
//        } else {
//            mShop.setText("获取失败");
//        }
//        //增加起始点和终点
//        mStartPoints.clear();
//        mEndPoints.clear();
//        mStartPoints.add(mNaviStart);
//
//        if (mNaviEnd != null)
//            mEndPoints.add(mNaviEnd);
//        //--------------------------------------------------------------------------------------------
//        mAMapNavi = AMapNavi.getInstance(this);
//        mAMapNavi.setAMapNaviListener(this);
//
//        mAMap = mapView.getMap();
//        mAMap.setLocationSource(this);
//        mAMap.getUiSettings().setMyLocationButtonEnabled(false);
//        mAMap.setMyLocationEnabled(true);
//        mAMap.getUiSettings().setZoomControlsEnabled(false);
//        mAMap.getUiSettings().setScaleControlsEnabled(true);
//        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        mAMap.setOnMapLoadedListener(this);
//        mRouteOverLay = new RouteOverLay(mAMap, null);
//    }
//
//    //初始化导航
//    private void initNavi() {
//        mAMapNavi = AMapNavi.getInstance(this);
//        AMapNaviPath naviPath = mAMapNavi.getNaviPath();
//        if (naviPath == null) {
//            return;
//        }
//        dissmissProgressDialog();
//        // 获取路径规划线路，显示到地图上
//        mRouteOverLay.setRouteInfo(naviPath);
//        mRouteOverLay.addToMap();
//        if (mIsMapLoaded) {
//            mRouteOverLay.zoomToSpan();
//        }
//
//        //距离
//        double length = ((int) (naviPath.getAllLength() / (double) 1000 * 10))
//                / (double) 10;
//        // 不足分钟 按分钟计
//        int time = (naviPath.getAllTime() + 59) / 60;//耗时
//        int cost = naviPath.getTollCost();//花费
//
//        mDistance.setText(String.valueOf(length) + " km");
//    }
//
//    //计算驾车路线
//    private void calculateDriveRoute() {
//        DebugLog.d("amap", "calculateDriveRoute");
//        mStartPoints.clear();
//        mNaviStart = new NaviLatLng(myLL.latitude, myLL.longitude);
//        mStartPoints.add(mNaviStart);
//
//        if (mEndPoints.size() < 1) {
//            showToast("获取信息失败");
//            isCalculateRouteSuccess = false;
//        } else {
//            isCalculateRouteSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
//                    mEndPoints, null, AMapNavi.DrivingDefault);
//        }
//
//        if (!isCalculateRouteSuccess) {
//            showToast("路线计算失败..,请检查网络");
//        }
//        dissmissProgressDialog();
//    }
//
//    /**
//     * 显示进度框
//     */
//    private void showProgressDialog() {
//        if (mProgressDialog == null)
//            mProgressDialog = new SubmitDialog(SimpleNaviRouteActivity.this);
//        mProgressDialog.setText("路线规划中..");
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.setCancelable(true);
//            }
//        }, 5000);
//
//        mProgressDialog.show();
//    }
//
//    /**
//     * 隐藏进度框
//     */
//    private void dissmissProgressDialog() {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
//    }
//
//    //计算步行路线
//    private void calculateFootRoute() {
//        boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
//        if (!isSuccess) {
//            showToast("路线计算失败,检查参数情况");
//        }
//    }
//
//    private void showToast(String message) {
//        ToastHelper.showOrangeToast(SimpleNaviRouteActivity.this, message);
//    }
//
//    //模拟导航
//    private void startEmulatorNavi(boolean isDrive) {
//
//        if ((isDrive && mIsDriveMode && mIsCalculateRouteSuccess)
//                || (!isDrive && !mIsDriveMode && mIsCalculateRouteSuccess)) {
//            Intent emulatorIntent = new Intent(SimpleNaviRouteActivity.this,
//                    SimpleNaviActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(AMapUtils.ISEMULATOR, true);
//            bundle.putInt(AMapUtils.ACTIVITYINDEX, AMapUtils.SIMPLEROUTENAVI);
//            emulatorIntent.putExtras(bundle);
//            emulatorIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(emulatorIntent);
//
//        } else {
//            showToast("请先进行相对应的路径规划，再进行导航");
//        }
//    }
//
//    //GPS 导航
//    private void startGPSNavi(boolean isDrive) {
//        if ((isDrive && mIsDriveMode && mIsCalculateRouteSuccess)
//                || (!isDrive && !mIsDriveMode && mIsCalculateRouteSuccess)) {
//            Intent gpsIntent = new Intent(SimpleNaviRouteActivity.this,
//                    SimpleNaviActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(AMapUtils.ISEMULATOR, false);
//            bundle.putInt(AMapUtils.ACTIVITYINDEX, AMapUtils.SIMPLEROUTENAVI);
//            gpsIntent.putExtras(bundle);
//            gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(gpsIntent);
//        } else {
//            showToast("请先进行相对应的路径规划，再进行导航");
//        }
//    }
//
//    //--------------------导航监听回调事件-----------------------------
//    @Override
//    public void onArriveDestination() {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onArrivedWayPoint(int arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onCalculateRouteFailure(int arg0) {
//        showToast("路径规划出错,请检查网络连接");
//        mIsCalculateRouteSuccess = false;
//        dissmissProgressDialog();
//    }
//
//    @Override
//    public void onCalculateRouteSuccess() {
//
//        AMapNaviPath naviPath = mAMapNavi.getNaviPath();
//        if (naviPath == null) {
//            return;
//        }
//        // 获取路径规划线路，显示到地图上
//        mRouteOverLay.setRouteInfo(naviPath);
//        mRouteOverLay.addToMap();
//        mRouteOverLay.zoomToSpan();
//        double length = ((int) (naviPath.getAllLength() / (double) 1000 * 10))
//                / (double) 10;
//
//        mDistance.setText(String.valueOf(length) + " km");
//        mIsCalculateRouteSuccess = true;
//
//        if (isFromShop) {
//
//            DebugLog.i("test_home_map", "onCalculateRouteSuccess:" + functionActiveState);
//
//            if (functionActiveState == AMapUtils.FROM_THIRD_PART_SHOP_NAVI) {
//                clickToNavi();
//            } else if (functionActiveState == AMapUtils.FROM_THIRD_PART_SHOP_LOCATION) {
//                reLoc();
//            }
//
//            isFromShop = false;
//        }
////        dissmissProgressDialog();
//    }
//
//    //模拟导航结束
//    @Override
//    public void onEndEmulatorNavi() {
//        // TODO Auto-generated method stub
//
//    }
//
//    //    拿到导航信息
//    @Override
//    public void onGetNavigationText(int arg0, String arg1) {
//        // TODO Auto-generated method stub
//
//    }
//
//    //gps打开状态
//    @Override
//    public void onGpsOpenStatus(boolean arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    //初始化导航失败
//    @Override
//    public void onInitNaviFailure() {
//        // TODO Auto-generated method stub
//
//    }
//
//    //初始化导航成功
//    @Override
//    public void onInitNaviSuccess() {
//        // TODO Auto-generated method stub
//
//    }
//
//    //位置更新
//    @Override
//    public void onLocationChange(AMapNaviLocation arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    //    导航信息更新
//    @Override
//    public void onNaviInfoUpdated(AMapNaviInfo arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onReCalculateRouteForTrafficJam() {
//        // TODO Auto-generated method stub
//
//    }
//
//    //重新计算路线
//    @Override
//    public void onReCalculateRouteForYaw() {
//        // TODO Auto-generated method stub
//
//    }
//
////------------------生命周期重写函数---------------------------
//
//    //开始导航
//    @Override
//    public void onStartNavi(int arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    //交通状况 更新
//    @Override
//    public void onTrafficStatusUpdate() {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onNaviInfoUpdate(NaviInfo arg0) {
//
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//        init();
//        initNavi();
//        TTSController.getInstance(this).startSpeaking();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//        deactivate();
//        TTSController.getInstance(this).stopSpeaking();
//    }
////-------------------------------------------------------------------------------------------------
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//        //删除监听
//        destroyInstance();
//    }
//
//    //销毁实例   释放内存
//    private void destroyInstance() {
//        AMapNavi.getInstance(this).removeAMapNaviListener(this);
//        AMapNavi.getInstance(this).destroy();// 销毁导航
//        TTSController.getInstance(this).stopSpeaking();
//        TTSController.getInstance(this).destroy();
//    }
//
//    @Override
//    public void finish() {
//        destroyInstance();
//        super.finish();
//    }
//
//    //位置变化 定位
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//        if (mListener != null && amapLocation != null) {
//            if (amapLocation.getAMapException().getErrorCode() == 0) {
//                myLL = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
////                mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
////                        myLL, 15, 0, 30)), 1000, null);
//
//                if (isReLoc) {
//                    isReLoc = false;
//                    initData();
//                }
//
//                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//            } else {
//                DebugLog.e("AmapErr", "Location ERR:" + amapLocation.getAMapException().getErrorCode());
//            }
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//
//    //激活定位
//    @Override
//    public void activate(OnLocationChangedListener listener) {
//        mListener = listener;
//        if (mAMapLocationManager == null) {
//            mAMapLocationManager = LocationManagerProxy.getInstance(this);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用destroy()方法
//            // 其中如果间隔时间为-1，则定位只定一次
//            // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
//            mAMapLocationManager.requestLocationData(
//                    LocationProviderProxy.AMapNetwork, 5 * 1000, 10, this);
//        }
//    }
//
//    //注销定位
//    @Override
//    public void deactivate() {
//        mListener = null;
//        if (mAMapLocationManager != null) {
//            mAMapLocationManager.removeUpdates(this);
//            mAMapLocationManager.destroy();
//        }
//        mAMapLocationManager = null;
//    }
//
//    //地图加载回调
//    @Override
//    public void onMapLoaded() {
//        mIsMapLoaded = true;
//        if (mRouteOverLay != null) {
//            mRouteOverLay.zoomToSpan();
//        }
//    }
//}
