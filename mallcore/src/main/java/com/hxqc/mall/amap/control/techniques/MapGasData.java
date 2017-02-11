package com.hxqc.mall.amap.control.techniques;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.amap.model.AroundGasModel;
import com.hxqc.mall.amap.model.GasStationModel;
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
public class MapGasData extends BaseAMapDataHelper {

    public MapGasData(Activity mA, AMap map) {
        super(mA, map);
    }

    @Override
    void initMarks(String pName, String cName, String rName, int errorCode) {

        DebugLog.i("chinese", "MapGasData initMarks");
        String lat = centerLocation.latitude + "";
        String lon = centerLocation.longitude + "";
        aroundApiClient.getGasStationInfoInner(lon, lat, "1000", "1", new LoadingAnimResponseHandler(mA, true) {
            @Override
            public void onSuccess(String response) {
                DebugLog.i("chinese", "MapGasData onSuccess");
                AroundGasModel aroundGasModel = JSONUtils.fromJson(response, new TypeToken<AroundGasModel>() {
                });

                if (aroundGasModel != null && aroundGasModel.result != null) {
                    ArrayList<GasStationModel> data = aroundGasModel.result.data;
                    if (data != null) {

                        destoryMarkers();

                        if (data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                markDatas.add(data.get(i));
                                LatLng latLng = new LatLng(data.get(i).getLatitude(), data.get(i).getLongitude());
                                addMarkersToMap(latLng,i);
                            }
                            if (callBack != null) {
                                callBack.moveCamera(15, false);
                            }
                            markerDataRequestSuccess();
                        }
                    }else {
                        markerDataRequestFail();
                    }
                }else {
                    markerDataRequestFail();
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

    @Override
    View createInfoWindow(int i) {
        return renderGasWindowView(i);
    }


    private View renderGasWindowView(int i) {
        View view = LayoutInflater.from(mA).inflate(R.layout.view_around_map_window, null);


        final GasStationModel item = (GasStationModel) markDatas.get(i);
        TextView mTitle = (TextView) view.findViewById(R.id.title_around_map);
        TextView mAddress = (TextView) view.findViewById(R.id.address_around_map);

        TextView mE90 = (TextView) view.findViewById(R.id.gas_around_map_90);
        TextView mE93 = (TextView) view.findViewById(R.id.gas_around_map_93);
        TextView mE97 = (TextView) view.findViewById(R.id.gas_around_map_97);
        TextView mE0 = (TextView) view.findViewById(R.id.gas_around_map_0);

        mE90.setText(item.price.E90);
        mE93.setText(item.price.E93);
        mE97.setText(item.price.E97);
        mE0.setText(item.price.E0);

        mTitle.setText(item.name);
        mAddress.setText(String.format("地址：%s", item.address));
        view.findViewById(R.id.map_around_navi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());

                DebugLog.w("mark_navi", "renderGasWindowView: " + latLng.toString());

                NaviLatLng end = new NaviLatLng(latLng.latitude, latLng.longitude);

                if (callBack != null)
                    callBack.onNavigationClick(end);
            }
        });

        return view;
    }
}
