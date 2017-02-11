package com.hxqc.mall.amap.control.techniques;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hxqc.mall.amap.api.AroundApiClient;
import com.hxqc.mall.amap.control.MapMarkOperateCallBack;
import com.hxqc.mall.amap.control.MapMarkerType;
import com.hxqc.mall.amap.control.OnMarkersRequestListener;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.LoadingDialog;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author:  wh
 * Date:  2016/4/21
 * FIXME
 * Todo  地图 数据请求
 */
public abstract class BaseAMapDataHelper implements GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter,
        AMap.OnMapClickListener {

    MapMarkOperateCallBack callBack;

    OnMarkersRequestListener markersRequestListener;

    public LoadingDialog loadingDialog;

    private Marker currentShowMarker;//当前弹出info window 的marker标记

    PoiSearch.Query query;// Poi查询条件类
    PoiSearch poiSearch;

    ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
    ArrayList<MarkerOptions> markerOptionlst = new ArrayList<>();
    ArrayList<Object> markDatas = new ArrayList<>();

    AroundApiClient aroundApiClient;
    LatLng centerLocation;
    Activity mA;
    AMap mAMap;

    /**
     * 开启请求loading
     */
    public void showLoadingDialog() {
        if (mA != null) {
            if (!(mA.isFinishing())) {
                loadingDialog = new LoadingDialog(mA);
                loadingDialog.setCancelable(true);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
            }
        }
    }

    /**
     * 关闭请求loading
     */
    public void dissmissLoadingDialog() {
        if (!(mA.isFinishing()) && loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    public BaseAMapDataHelper(Activity mA, AMap map) {
        this.mA = mA;
        this.mAMap = map;
        aroundApiClient = new AroundApiClient();
        setListener();
    }

    //设置 mark操作回调
    public void setCallBack(MapMarkOperateCallBack callBack) {
        this.callBack = callBack;
    }

    //设置mark请求回调
    public void setMarkersRequestListener(OnMarkersRequestListener markersRequestListener) {
        unregistMarkersRequestListener();
        this.markersRequestListener = markersRequestListener;
    }

    //注销mark请求回调
    public void unregistMarkersRequestListener() {
        if (markersRequestListener != null)
            markersRequestListener = null;
    }

    //marker数据请求成功
    void markerDataRequestSuccess() {
        if (markersRequestListener != null)
            markersRequestListener.markerDataRSuccess();
    }

    //marker数据请求失败
    void markerDataRequestFail() {
        if (markersRequestListener != null)
            markersRequestListener.markerDataRFail();
    }

    void setListener() {
        mAMap.setOnMapClickListener(this);//设置地图点击事件
        mAMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        mAMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        mAMap.setInfoWindowAdapter(this);// 设置自
    }

    public void showToast(String message) {
        ToastHelper.showRedToast(mA, message);
    }

    /**
     * 清除mark 及数据
     */
    public void destoryMarkers() {

        if (mAMap != null) {
            mAMap.clear(true);
        }

        if (giflist != null) {
            giflist.clear();
        }

        if (markerOptionlst != null) {
            markerOptionlst.clear();
        }

        if (markDatas != null) {
            markDatas.clear();
        }

        destroyPoiSearchInstance();
    }

    private void destroyPoiSearchInstance() {
        if (query != null) {
            query = null;
        }
        if (poiSearch != null) {
            poiSearch = null;
        }
    }

    /**
     * 逆地理位置编码
     */
    public void searchForName(LatLng l) {
        DebugLog.w("Map_Tag", "searchForName 1");
        if (l != null) {

            showLoadingDialog();

            DebugLog.w("Map_Tag", "searchForName 2");
            this.centerLocation = l;
            LatLonPoint latLonPoint = new LatLonPoint(l.latitude, l.longitude);
            GeocodeSearch geocoderSearch = new GeocodeSearch(mA);
            geocoderSearch.setOnGeocodeSearchListener(this);
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
        } else {
            DebugLog.w("Map_Tag", "searchForName LatLng is null");
        }
    }

    /**
     * 逆地理位置编码回调
     *
     * @param regeocodeResult 逆地理位置结果
     * @param i               返回码
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        dissmissLoadingDialog();

        DebugLog.w("Map_Tag", "onRegeocodeSearched ");
        String cityName = regeocodeResult.getRegeocodeAddress().getCity();
        String pName = regeocodeResult.getRegeocodeAddress().getProvince();
        String r = regeocodeResult.getRegeocodeAddress().getDistrict();
        if (currentShowMarker != null && currentShowMarker.isInfoWindowShown())
            currentShowMarker.hideInfoWindow();

        if (TextUtils.isEmpty(pName)) {
            markerDataRequestFail();
        } else {
            if (TextUtils.isEmpty(cityName)) {
                cityName = pName;
            }
            initMarks(pName, cityName, r, MapMarkerType.GeoCodeSuccess);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * mark操作
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return createInfoWindow(Integer.parseInt(marker.getSnippet()));
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentShowMarker = marker;
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentShowMarker != null && currentShowMarker.isInfoWindowShown()) {
            currentShowMarker.hideInfoWindow();
        }
    }

    /**
     * 初始化marker view
     */
    public BitmapDescriptor initMarkerView() {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.around_map_mark);
        giflist.add(bitmapDescriptor);
        return bitmapDescriptor;
    }

    /**
     * 在地图上添加  marker
     */
    public void addMarkersToMap(LatLng latLng, int i) {
        DebugLog.i("chinese_m", "addMarkersToMap : " + i + " latLng: " + latLng.toString());
        MarkerOptions markerOption = new MarkerOptions().snippet(i + "").title("Marker")
                .anchor(0.5f, 0.5f).position(latLng)
//                .setInfoWindowOffset(0, (int) (10 * screenDensity))
                .icon(initMarkerView()).draggable(false);
        markerOptionlst.add(markerOption);
        mAMap.addMarker(markerOption);
    }

    abstract void initMarks(String pName, String cName, String rName, int errorCode);

    abstract View createInfoWindow(int i);
}
