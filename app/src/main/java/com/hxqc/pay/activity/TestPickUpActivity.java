package com.hxqc.pay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.hxqc.mall.activity.BackActivity;

import java.util.ArrayList;

import hxqc.mall.R;

public class TestPickUpActivity extends BackActivity implements AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        AMap.InfoWindowAdapter {


    private AMap aMap;
    private MapView mapView;
//    LatLng latLng = MapUtils.bd_decrypt(30.629824, 114.246613);
LatLng latLng = new LatLng(30.629824,114.246613);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pick_up);

        mapView = (MapView) findViewById(R.id.test_pickup_map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        aMap = mapView.getMap();
        setUpMap();
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);
//        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
//        aMap.stopAnimation();
        addMarkersToMap();// 往地图上添加marker
    }

    private void addMarkersToMap() {

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        giflist.add(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        giflist.add(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        MarkerOptions markerOption1 = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLng).title("成都市")
                .snippet("成都市:30.679879, 104.064855").icons(giflist)
                .draggable(false).period(50);
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<>();
        markerOptionlst.add(markerOption1);
//        aMap.addMarkers(markerOptionlst, true);
        aMap.addMarker(markerOption1);
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
    }




    @Override
    public void onMapLoaded() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        marker.showInfoWindow();

        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoContent = getLayoutInflater().inflate(R.layout.view_bg_map_pop, null);
        String title = marker.getTitle();
        TextView titleUi = ((TextView) infoContent.findViewById(R.id.tv_map_mark_));
        titleUi.setText(title);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(R.layout.view_bg_map_pop, null);
        String title = marker.getTitle();
        TextView titleUi = ((TextView) infoContent.findViewById(R.id.tv_map_mark_));
        titleUi.setText(title);
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
