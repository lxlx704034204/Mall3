package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BasePositionActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.db.areacacheutil_new.AreaCacheUtil;
import com.hxqc.mall.core.model.LocationAreaModel;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.control.TFilterController;
import com.hxqc.mall.thirdshop.views.adpter.ExpandAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

/**
 * Function: 城市选择Activity（定位）
 *
 * @author 袁秉勇
 * @since 2016年02月18日
 */
public class PositionActivity extends BasePositionActivity {
    private final static String TAG = PositionActivity.class.getSimpleName();
    ArrayList< LocationAreaModel > locationAreaModels = new ArrayList<>();
    private Context mContext;

    @Override
    protected void getData() {
        if (areaCacheUtil.isExistCache(AreaCacheUtil.THIRDSHOP) && baseSharedPreferencesHelper.getLoadPosition()) {
            locationAreaModels = areaCacheUtil.getCache(AreaCacheUtil.THIRDSHOP);
            ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
            location_expand_list_view.setAdapter(myAdapter);
        } else {
            baseSharedPreferencesHelper.setLoadPosition(true);
            ThirdPartShopClient apiClient = new ThirdPartShopClient();
            apiClient.requestFilterArea(new DialogResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    locationAreaModels = JSONUtils.fromJson(response, new TypeToken< ArrayList< LocationAreaModel > >() {
                    });
                    if (locationAreaModels == null || locationAreaModels.size() <= 0) {
                        if (areaCacheUtil.isExistCache(AreaCacheUtil.THIRDSHOP)) {
                            locationAreaModels = areaCacheUtil.getCache(AreaCacheUtil.THIRDSHOP);
                        }
                        ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
                        location_expand_list_view.setAdapter(myAdapter);
                        return;
                    }
//                    saveCache(locationAreaModels, "position");
                    areaCacheUtil.dataChangeAndUpdate(AreaCacheUtil.THIRDSHOP, areaCacheUtil.transformData(AreaCacheUtil.THIRDSHOP, locationAreaModels));
                    int i = 0;
                    sideTagMap = new ArrayMap<>();
                    for (LocationAreaModel locationAreaModel : locationAreaModels) {
//                    sideBarTagList.add(locationAreaModel.provinceInitial);
                        sideTagMap.put(locationAreaModel.provinceInitial, i++);
                    }

                    Set< String > strings = sideTagMap.keySet();
                    String[] strings1 = strings.toArray(new String[1]);
                    sidebar_position.setSideTag(strings1);
                    ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
                    location_expand_list_view.setAdapter(myAdapter);
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (areaCacheUtil.isExistCache(AreaCacheUtil.THIRDSHOP)) {
                        locationAreaModels = areaCacheUtil.getCache(AreaCacheUtil.THIRDSHOP);
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
        mFilterMap = TFilterController.getInstance().mFilterMap;
    }
}
