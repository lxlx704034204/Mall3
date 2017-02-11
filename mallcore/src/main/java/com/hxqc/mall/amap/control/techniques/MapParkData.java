package com.hxqc.mall.amap.control.techniques;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.amap.model.SelfServiceParkModel;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.Error;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;


import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * Author:  wh
 * Date:  2016/4/26
 * FIXME
 * Todo
 */
public class MapParkData extends BaseAMapDataHelper implements PoiSearch.OnPoiSearchListener {

    private boolean isParkInner = false;//是否是内部停车场信息

    public MapParkData(Activity mA, AMap map) {
        super(mA, map);
    }

    @Override
    void initMarks(String pName, String cName, String rName, int errorCode) {
        if (TextUtils.isEmpty(cName)) {
            isParkInner = false;
            doSearchPark();
        } else {
//            if (cName.contains("武汉")) {
//                isParkInner = true;
//                doSearchParkInner();
//            } else {
                isParkInner = false;
                doSearchPark();
//            }
        }
    }

    @Override
    View createInfoWindow(int i) {
        return renderParkWindowView(i);
    }

    /**
     * 搜索内部详细停车场
     */
    private void doSearchParkInner() {
        DebugLog.i("chinese", "doSearchParkInner");
        aroundApiClient.getParkInfoInner(centerLocation.latitude + "", centerLocation.longitude + "", new LoadingAnimResponseHandler(mA, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<SelfServiceParkModel> data = JSONUtils.fromJson(response, new TypeToken<ArrayList<SelfServiceParkModel>>() {
                });
                DebugLog.i("chinese", "doSearchParkInner onSuccess");
                if (data != null && data.size() > 0) {

                    DebugLog.i("chinese", "doSearchParkInner markers onSuccess destoryMarkers");
                    destoryMarkers();

                    for (int i = 0; i < data.size(); i++) {
                        SelfServiceParkModel item = data.get(i);
                        markDatas.add(item);
                        LatLng latLng = new LatLng(item.tipLatitude(), item.tipLongitude());
                        addMarkersToMap(latLng, i);
                    }

                    if (callBack != null) {
                        callBack.moveCamera(15, false);
                    }

                    markerDataRequestSuccess();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                markerDataRequestFail();
            }

            @Override
            public void onOtherFailure(int statusCode, Header[] headers, String responseString, Error error) {
                markerDataRequestFail();
            }

        });

    }

    /**
     * 搜索高的自带停车场
     */
    private void doSearchPark() {
        int currentPage = 0;
        String keyWord = "停车场";
        query = new PoiSearch.Query(keyWord, "", "全国");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        LatLonPoint lp = new LatLonPoint(centerLocation.latitude, centerLocation.longitude);//
        poiSearch = new PoiSearch(mA, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 3000, true));//
        // 设置搜索区域为以lp点为圆心，其周围3000米范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    /**
     * =============================================地图  poi  搜索   回调==================================================================
     */
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        DebugLog.w("map_search", "onPoiSearched");
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    ArrayList<PoiItem> data = result.getPois();
                    if (data != null && data.size() > 0) {

                        destoryMarkers();

                        for (int i = 0; i < data.size(); i++) {
                            PoiItem poiItem = data.get(i);
                            markDatas.add(poiItem);
                            LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                            addMarkersToMap(latLng, i);
                        }

                        if (callBack != null) {
                            callBack.moveCamera(17, false);
                        }

                        markerDataRequestSuccess();
                    } else {
                        markerDataRequestFail();
                    }
                } else {
                    markerDataRequestFail();
                }
            } else {
                markerDataRequestFail();
            }
        } else {
            markerDataRequestFail();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public  String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    private View renderParkWindowView(int i) {

        View view = null;
        if (isParkInner) {
            view = LayoutInflater.from(mA).inflate(R.layout.view_around_map_inner_park, null);
        } else {
            view = LayoutInflater.from(mA).inflate(R.layout.view_around_map_window, null);
        }

        TextView mTitle = (TextView) view.findViewById(R.id.title_around_map);
        TextView mAddress = (TextView) view.findViewById(R.id.address_around_map);

        if (isParkInner) {
            final SelfServiceParkModel model = (SelfServiceParkModel) markDatas.get(i);
            TextView mParkType = (TextView) view.findViewById(R.id.park_station_type);
            TextView mParkFee = (TextView) view.findViewById(R.id.park_fee);
            TextView mParkSpaces = (TextView) view.findViewById(R.id.park_spaces);

            mTitle.setText(model.name);
            mAddress.setText(model.address);

            if (TextUtils.isEmpty(model.capacityDesc)) {
                mParkType.setVisibility(View.GONE);
            } else {
                if (model.capacityDesc.contains("泊位")) {
                    mParkType.setVisibility(View.GONE);
                } else {
                    mParkType.setVisibility(View.VISIBLE);
                    mParkType.setText(String.format("停车场类型：%s", model.capacityDesc));
                }
            }

            mParkFee.setText(ToSBC(model.feedesc));
            mParkSpaces.setText(model.capacityNum + "");

            view.findViewById(R.id.map_around_navi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NaviLatLng end = new NaviLatLng(model.tipLatitude(), model.tipLongitude());
                    if (callBack != null)
                        callBack.onNavigationClick(end);
                }
            });
        } else {
            final PoiItem poiItem = (PoiItem) markDatas.get(i);
            LinearLayout viewGas1 = (LinearLayout) view.findViewById(R.id.gas_ll_1);
            LinearLayout viewGas2 = (LinearLayout) view.findViewById(R.id.gas_ll_2);
            View gasLine = view.findViewById(R.id.around_map_line);

            viewGas1.setVisibility(View.GONE);
            viewGas2.setVisibility(View.GONE);
            gasLine.setVisibility(View.GONE);

            mTitle.setText(poiItem.getTitle());
            mAddress.setText(String.format("地址：%s", poiItem.getSnippet()));

            view.findViewById(R.id.map_around_navi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLonPoint latLonPoint = poiItem.getLatLonPoint();

                    DebugLog.w("mark_navi", "renderParkWindowView: " + latLonPoint.toString());

                    NaviLatLng end = new NaviLatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());

                    if (callBack != null)
                        callBack.onNavigationClick(end);
                }
            });
        }

        return view;
    }
}
