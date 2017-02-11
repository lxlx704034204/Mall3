package com.hxqc.newenergy.activity;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BasePositionActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.db.areacacheutil_new.AreaCacheUtil;
import com.hxqc.mall.core.model.LocationAreaModel;
import com.hxqc.mall.thirdshop.maintenance.adapter.ExpandAdapter;
import com.hxqc.newenergy.api.NewEnergyApiClient;
import com.hxqc.newenergy.control.FilterController;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

/**
 * Function: 新能源首界面定位Activity
 *
 * @author 袁秉勇
 * @since 2016年03月21日
 */
public class PositionActivity extends BasePositionActivity {
    private final static String TAG = PositionActivity.class.getSimpleName();
    ArrayList< LocationAreaModel > locationAreaModels = new ArrayList<>();
    private Context mContext;


    @Override
    protected void initSharePreferenceHelper() {
        baseSharedPreferencesHelper = new EVSharePreferencesHelper(this);
        setShowWholeCity(false);
    }


    @Override
    protected void getData() {
        if (areaCacheUtil.isExistCache(AreaCacheUtil.NEWENERGY) && baseSharedPreferencesHelper.getLoadPosition()) {
            locationAreaModels = areaCacheUtil.getCache(AreaCacheUtil.NEWENERGY);
            ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
            location_expand_list_view.setAdapter(myAdapter);
        } else {
            baseSharedPreferencesHelper.setLoadPosition(true);
            NewEnergyApiClient energyApiClient = new NewEnergyApiClient();
            energyApiClient.getAreaData(new DialogResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    locationAreaModels = JSONUtils.fromJson(response, new TypeToken< ArrayList< LocationAreaModel > >() {
                    });
                    if (locationAreaModels == null || locationAreaModels.size() <= 0) {
                        if (areaCacheUtil.isExistCache(AreaCacheUtil.NEWENERGY)) {
                            locationAreaModels = areaCacheUtil.getCache(AreaCacheUtil.NEWENERGY);
                        }
                        ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
                        location_expand_list_view.setAdapter(myAdapter);
                        return;
                    }
                    areaCacheUtil.dataChangeAndUpdate(AreaCacheUtil.NEWENERGY, areaCacheUtil.transformData(AreaCacheUtil.NEWENERGY, locationAreaModels));
                    int i = 0;
                    sideTagMap = new ArrayMap<>();
                    for (LocationAreaModel positionBean : locationAreaModels) {
//                    sideBarTagList.add(positionBean.provinceInitial);
                        sideTagMap.put(positionBean.provinceInitial, i++);
                    }

                    Set< String > strings = sideTagMap.keySet();
                    String[] strings1 = strings.toArray(new String[1]);
                    sidebar_position.setSideTag(strings1);
                    ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
                    location_expand_list_view.setAdapter(myAdapter);
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (areaCacheUtil.isExistCache(AreaCacheUtil.NEWENERGY)) {
                        locationAreaModels = areaCacheUtil.getCache(AreaCacheUtil.NEWENERGY);
                        ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
                        location_expand_list_view.setAdapter(myAdapter);
                    } else {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                }
            });
        }
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        DebugLog.d(TAG, "onChildClick: " + groupPosition + "----" + childPosition);
        //                intent.putExtra("position",locationAreaModels.get(groupPosition).areaGroup.get
        // (childPosition).province);
        LocationAreaModel child = (LocationAreaModel) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
        String cityName = child.areaGroup.get(childPosition).province;
//                TFilterController instance = TFilterController.getInstance();
//                instance.putValue("area",cityName );
////                instance.putValue("areaInitial", child.provinceInitial);
//                Intent intent = new Intent();
//                intent.putExtra("position", cityName);
//                setResult(1, intent);
//                finish();
        finishSelfWithResult(cityName);
        return true;
    }


    @Override
    protected void initFilterMap() {
        mFilterMap = FilterController.getInstance().mFilterMap;
    }

    protected LinkedList<String> initHistoryCityList() {
        return ((EVSharePreferencesHelper)baseSharedPreferencesHelper).getNewEnergyHistoryCity();
//        return new LinkedList<>();
    }

    protected void setHistory(String cityName) {
        ((EVSharePreferencesHelper)baseSharedPreferencesHelper).setNewenergyHistoryCity(cityName);
        super.setHistory(cityName);
    }
}
