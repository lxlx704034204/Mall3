package com.hxqc.pay.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.api.RequestFailViewUtil;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.views.dialog.SubmitDialog;
import com.hxqc.pay.adapter.PickPositionAdapter;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.model.PickupPointResponse;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import hxqc.mall.R;


public class PickupPlaceActivity extends BackActivity implements PickPositionAdapter.PositionChange, OnMarkerClickListener,
        OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener, AMapLocationListener, LocationSource,
        InfoWindowAdapter {


    ListView positionList;
    RequestFailView mNoDataNotice;
    PickPositionAdapter mPickAdapter;
    PayApiClient apiClient;
    PickupPointResponse pickupPointResponse;
    ArrayList< PickupPointT > data;
    String pid = "";
    String cid = "";
    String province = "";
    String itemID = "";
    String orderID = "";
    // 省市等级
    int level = 0;
    LatLng myLL;
    //是否开始定位
    boolean isReLoc = true;
    //重新定位
    ImageButton mReloc;
    //添加覆盖物
    ArrayList< BitmapDescriptor > giflist = new ArrayList< BitmapDescriptor >();
    //3d map 用
    ArrayList< MarkerOptions > markerOptionlst = new ArrayList< MarkerOptions >();
    //Marker集合
    ArrayList< Marker > markers = new ArrayList<>();
    //排序规则
    Comparator< PickupPointT > comparator = new Comparator< PickupPointT >() {
        @Override
        public int compare(PickupPointT lhs, PickupPointT rhs) {
            if ((lhs.distance - rhs.distance) > 0) {
                return 1;
            } else if ((lhs.distance - rhs.distance) == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    };
    private AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private AMap aMap;
    private MapView mapView;
    //   定位回调
    private OnLocationChangedListener mListener;
    private SubmitDialog mProgressDialog;

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new SubmitDialog(PickupPlaceActivity.this);
        mProgressDialog.setText("信息获取中..");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_place);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("自提点");
        showProgressDialog();
        positionList = (ListView) findViewById(R.id.lv_nearby_position);
        mReloc = (ImageButton) findViewById(R.id.ib_re_loc);

        if (getIntent().hasExtra(ActivitySwitchBase.KEY_DATA)) {
            //车辆详情 点击查看 自提点信息
            data = getIntent().getParcelableArrayListExtra(ActivitySwitchBase.KEY_DATA);
        } else {
            //请求
            orderID = getIntent().getStringExtra("orderID");
            itemID = getIntent().getStringExtra("itemID");
            pid = getIntent().getStringExtra("pid");
            cid = getIntent().getStringExtra("cid");
            province = getIntent().getStringExtra("province");
        }


        mReloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO    重新定位
//                Intent intent  = new Intent(PickupPlaceActivity.this,TestPickUpActivity.class) ;
//                startActivity(intent);
                reLoc();
            }
        });


        mapView = (MapView) findViewById(R.id.pickup_map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        initMap();
    }

    //初始化地图=========================================================================
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        apiClient = new PayApiClient();
        mNoDataNotice = (RequestFailView) findViewById(R.id.pkpp_fail_notice_view);
        //定位
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMapLoadedListener(this);


    }

    //设置地图监听
    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
    }

    //3d maps 一次加载多项marker
//    private void setMarkers() {
//        aMap.addMarkers(markerOptionlst, true);
//    }

    //定位================================================================================

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(int i) {
        PickupPointT point = data.get(i);
        View markView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_pickup_point_mark_for_map, null);
        TextView num = (TextView) markView.findViewById(R.id.tv_mappin_num_map);
        num.setText(String.format("%d", i + 1));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(markView);
        giflist.add(bitmapDescriptor);
        LatLng latLng = MapUtils.bd_decrypt_2d(point.getLatitude(), point.getLongitude());

        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLng).title(point.name).icon(bitmapDescriptor)
                .draggable(false);
        markerOptionlst.add(markerOption);
        Marker marker = aMap.addMarker(markerOption);
        markers.add(marker);
    }

    private void reLoc() {
        DebugLog.i("amap", "reLoc");
        if (mlocationClient != null) {
            DebugLog.i("amap", "reLoc -------->");
            locationProxyActive();
        } else {
            mlocationClient = new AMapLocationClient(PickupPlaceActivity.this);
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

            locationProxyActive();
        }
    }

    private void locationProxyActive() {
            AMapLocation lastKnownLocation = mlocationClient.getLastKnownLocation();
            DebugLog.i("amap", "reLoc -------->" + lastKnownLocation.toString());
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 12, 0, 30)), 1000, null);
    }

    //功能业务===============================================================================
    //拿到自提点信息以及更改
    private void changePointData() {


        if (data != null) {
            initMapAndPoint("");
        } else {

            apiClient.getPickupPoint(orderID,itemID, cid, pid, new BaseMallJsonHttpResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    DebugLog.e("test_map", response + "===");
                    mNoDataNotice.setVisibility(View.GONE);
                    positionList.setVisibility(View.VISIBLE);
                    initMapAndPoint(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    mNoDataNotice.setVisibility(View.VISIBLE);
                    mNoDataNotice.setRequestViewType(RequestFailView.RequestViewType.fail);
                    positionList.setVisibility(View.GONE);
//                , "获取数据失败"
                    mNoDataNotice.addView(new RequestFailViewUtil().getFailView(PickupPlaceActivity.this));
                }
            });

        }

    }

    // 初始化地图  以及自提点 isrefresh 判断是否继续刷新显示
    private void initMapAndPoint(String response) {
        DebugLog.i("distance", "---------" + response);
        DebugLog.e("pickup", "zoom  -->" + myLL.toString() + "-=-");

        if (data == null) {
            pickupPointResponse = JSONUtils.fromJson(response, new TypeToken< PickupPointResponse >() {
            });
            data = pickupPointResponse.pointList;

            level = pickupPointResponse.getLevel();
            DebugLog.i("distance", "-=-=  " + level + "---------" + response);
            DebugLog.i("map_p", "-=-=  1");
        }

        if (data == null) {
            DebugLog.i("map_p", "-=-=  2");
            mNoDataNotice.setVisibility(View.VISIBLE);
            mNoDataNotice.setRequestViewType(RequestFailView.RequestViewType.empty);
            positionList.setVisibility(View.GONE);


//            if (!TextUtils.isEmpty(province)) {
//                DebugLog.i("map_p", "-=-=  3");
//                mNoDataNotice.addView(mRequestFailViewUtil.getEmptyView(PickupPlaceActivity.this, province + "暂无自提点"));
//            } else {
//                DebugLog.i("map_p", "-=-=  4");
//                mNoDataNotice.addView(mRequestFailViewUtil.getEmptyView(PickupPlaceActivity.this, "暂无数据"));
//            }

        } else {

            if (myLL.latitude == 0d) {
                mNoDataNotice.setVisibility(View.VISIBLE);
                mNoDataNotice.setRequestViewType(RequestFailView.RequestViewType.fail);
                positionList.setVisibility(View.GONE);

//                mNoDataNotice.addView(mRequestFailViewUtil.getEmptyView(PickupPlaceActivity.this, "计算失败无法获取位置信息请重新定位"));
            } else {
                mNoDataNotice.setVisibility(View.GONE);
                positionList.setVisibility(View.VISIBLE);
                DebugLog.i("map_p", "-=-=  6");
                fillPointData();
            }

        }
        dissmissProgressDialog();
    }

    //计算距离
    private double getDistance(LatLng ml1, LatLng tl2) {
        return AMapUtils.calculateLineDistance(ml1, MapUtils.bd_decrypt_2d(tl2.latitude, tl2.longitude));
    }

    //数据排序
    private void sortPointDataByDistance() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).distance = getDistance(myLL, new LatLng(data.get(i).getLatitude(), data.get(i).getLongitude()));
        }
        Collections.sort(data, comparator);
    }

    private void fillPointData() {
        mNoDataNotice.setVisibility(View.GONE);
        positionList.setVisibility(View.VISIBLE);
        DebugLog.i("distance", myLL.toString());
        sortPointDataByDistance();
        DebugLog.e("test_map", data.toString());
        mPickAdapter = new PickPositionAdapter(data, PickupPlaceActivity.this);
        positionList.setAdapter(mPickAdapter);
        mPickAdapter.setPositionChange(this);
//        if (aMap != null) {
//            aMap.clear();
//        }
        for (int i = 0; i < data.size(); i++) {
            try {
                addMarkersToMap(i);// 往地图上添加marker
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        setMarkers();
    }

    //更改地图显示配置 状态
    private void mapStatusChange(LatLng latLng1) {
        for (int i = 0; i < markers.size(); i++) {
            LatLng position = markers.get(i).getPosition();
            if (position.latitude == latLng1.latitude && position.longitude == latLng1.longitude) {
                markers.get(i).showInfoWindow();
            }
        }

        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng1, 15, 0, 30)), 1000, null);
    }

    //选择换地图切换的  位置
//    @Override
//    public void ChangeMapPosition(LatLng LL) {
//        mapStatusChange(MapUtils.bd_decrypt_2d(LL.latitude, LL.longitude));
//    }

    //生命周期重写==========================================================================
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mlocationClient != null) {
            if (mlocationClient.isStarted())
                mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
    }
    //地图标记 接口回调========================================================================

    //自定义窗口adapter
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.view_bg_map_pop, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    //自定义 info window
    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(R.layout.view_bg_map_pop, null);
        render(marker, infoContent);
        return null;
    }

    //window 点击事件
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.isInfoWindowShown())
            marker.hideInfoWindow();
    }

    //操作修改info window显示内容
    public void render(Marker marker, View view) {
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.tv_map_mark_));
        titleUi.setText(title);
    }

    //-------------------------------------------------------------------
    @Override
    public void onMapLoaded() {

    }

    //marker覆盖物点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        int p = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).name.equals(marker.getTitle())) {
                p = i;
            }
        }
        mapStatusChange(MapUtils.bd_decrypt_2d(data.get(p).getLatitude(), data.get(p).getLongitude()));
        positionList.setSelection(p);
        marker.showInfoWindow();
        return true;
    }

    //拖动
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

//定位 接口==============================================================================

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (isReLoc) {
            if (mListener != null && amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    isReLoc = false;
                    myLL = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    DebugLog.e("amap", "Location ERR:animateCamera");
                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            myLL, 12, 0, 30)), 1000, null);
                    changePointData();
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                } else {
                    DebugLog.e("AmapErr", "Location ERR:" + amapLocation.getErrorCode());
                }
            }
        }
    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(PickupPlaceActivity.this);
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
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void ChangeMapPosition(com.amap.api.maps.model.LatLng LL) {
        mapStatusChange(MapUtils.bd_decrypt_2d(LL.latitude, LL.longitude));
    }
}
