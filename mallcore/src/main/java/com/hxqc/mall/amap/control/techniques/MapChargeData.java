package com.hxqc.mall.amap.control.techniques;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.amap.model.SelfServiceChargeStationModel;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.MapUtils;
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
public class MapChargeData extends BaseAMapDataHelper {


    public MapChargeData(Activity mA, AMap map) {
        super(mA, map);
    }

    @Override
    void initMarks(String pName, String cName, String rName, int errorCode) {

        DebugLog.i("chinese", "doSearchCharger ");
        aroundApiClient.getChargeStationInfoInner(pName, cName, rName, centerLocation.latitude + "", centerLocation.longitude + "", new LoadingAnimResponseHandler(mA, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<SelfServiceChargeStationModel> data = JSONUtils.fromJson(response, new TypeToken<ArrayList<SelfServiceChargeStationModel>>() {
                });
                DebugLog.i("chinese", "doSearchCharger onSuccess");
                if (data != null) {

                    destoryMarkers();

                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            markDatas.add(data.get(i));
                            LatLng latLng = MapUtils.bd_encrypt(data.get(i).tipLatitude(), data.get(i).tipLongitude());
                            addMarkersToMap(latLng,i);
                        }

                        if (callBack != null) {
                            callBack.moveCamera(13, false);
                        }

                        markerDataRequestSuccess();
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
        return renderChargerWindowView(i);
    }

    private View renderChargerWindowView(int i) {
        View view = LayoutInflater.from(mA).inflate(R.layout.view_around_map_window_charge, null);
        final SelfServiceChargeStationModel mode = (SelfServiceChargeStationModel) markDatas.get(i);
        TextView mTitle = (TextView) view.findViewById(R.id.title_around_map);
        TextView mAddress = (TextView) view.findViewById(R.id.address_around_map);

        TextView mStationType = (TextView) view.findViewById(R.id.charge_station_type);
        TextView mFee = (TextView) view.findViewById(R.id.charge_fee);
        TextView mAlternating = (TextView) view.findViewById(R.id.charge_available_alternating);
        TextView mDirect = (TextView) view.findViewById(R.id.charge_available_direct);

        ImageView mLogo = (ImageView) view.findViewById(R.id.charge_station_logo);

        DebugLog.w("apic", mode.providerUrl);
        ImageUtil.setImage(mA, mLogo, mode.providerUrl);
        if (TextUtils.isEmpty(mode.staType)) {
            mStationType.setText("电站类型：未知");
        } else {
            mStationType.setText(String.format("电站类型：%s", mode.staType));
        }

        mFee.setText(mode.price);
        mAlternating.setText(mode.acableNum);
        mDirect.setText(mode.dcableNum);

        mTitle.setText(mode.staName);
        mAddress.setText(String.format("地址：%s", mode.staAddress));
        view.findViewById(R.id.map_around_navi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng latLng = MapUtils.bd_encrypt(mode.tipLatitude(), mode.tipLongitude());

                NaviLatLng end = new NaviLatLng(latLng.latitude, latLng.longitude);

                if (callBack != null)
                    callBack.onNavigationClick(end);
            }
        });
        return view;
    }

}
