package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BasePositionActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.control.TFilterController;
import com.hxqc.mall.thirdshop.model.AreaCategory;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.mall.thirdshop.views.adpter.ExpandAdapterForSpecialCar;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

/**
 * Function: 特价车分站地区选择Activity
 *
 * @author 袁秉勇
 * @since 2016年05月10日
 */
public class PositionActivityForSpecialCar extends BasePositionActivity {
    private final static String TAG = PositionActivityForSpecialCar.class.getSimpleName();
    ArrayList< AreaCategory > areaCategories = new ArrayList<>();
    private Context mContext;

    private AreaSiteUtil areaSiteUtil;


    @Override
    protected void initSharePreferenceHelper() {
        baseSharedPreferencesHelper = new SharedPreferencesHelper(this);

        setShowWholeCity(false);
        setShowWholeCityTitle(false);

        areaSiteUtil = AreaSiteUtil.getInstance(this);
    }


    @Override
    protected void getData() {
        if (baseSharedPreferencesHelper.getSpecialCarAreaHistoryFlag() && baseSharedPreferencesHelper.getLoadPosition()) {
            areaCategories = JSONUtils.fromJson(baseSharedPreferencesHelper.getSpecialCarHistoryData(), new TypeToken< ArrayList< AreaCategory > >() {
            });
            ExpandAdapterForSpecialCar myAdapter = new ExpandAdapterForSpecialCar(areaCategories == null ? new ArrayList< AreaCategory >() : areaCategories);
            location_expand_list_view.setAdapter(myAdapter);
        } else {
            ThirdPartShopClient apiClient = new ThirdPartShopClient();
            apiClient.requestSiteData(new DialogResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    baseSharedPreferencesHelper.setLoadPosition(true);

                    areaCategories = JSONUtils.fromJson(response, new TypeToken< ArrayList< AreaCategory > >() {
                    });
                    if (areaCategories == null || areaCategories.size() <= 0) {
                        if (baseSharedPreferencesHelper.getSpecialCarAreaHistoryFlag()) {
                            areaCategories = JSONUtils.fromJson(baseSharedPreferencesHelper.getSpecialCarHistoryData(), new TypeToken< ArrayList< AreaCategory > >() {
                            });
                        }
                        ExpandAdapterForSpecialCar myAdapter = new ExpandAdapterForSpecialCar(areaCategories == null ? new ArrayList< AreaCategory >() : areaCategories);
                        location_expand_list_view.setAdapter(myAdapter);
                        return;
                    }
                    baseSharedPreferencesHelper.setSpecialCarHistoryData(response);
                    baseSharedPreferencesHelper.setSpecialCarAreaHistoryFlag(true);
                    int i = 0;
                    sideTagMap = new ArrayMap<>();
                    for (AreaCategory locationAreaModel : areaCategories) {
                        sideTagMap.put(locationAreaModel.areaName, i++);
                    }

                    Set< String > strings = sideTagMap.keySet();
                    String[] strings1 = strings.toArray(new String[1]);
                    sidebar_position.setSideTag(strings1);
                    ExpandAdapterForSpecialCar myAdapter = new ExpandAdapterForSpecialCar(areaCategories);
                    location_expand_list_view.setAdapter(myAdapter);
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (baseSharedPreferencesHelper.getSpecialCarAreaHistoryFlag()) {
                        areaCategories = JSONUtils.fromJson(baseSharedPreferencesHelper.getSpecialCarHistoryData(), new TypeToken< ArrayList< AreaCategory > >() {
                        });
                        ExpandAdapterForSpecialCar myAdapter = new ExpandAdapterForSpecialCar(areaCategories == null ? new ArrayList< AreaCategory >() : areaCategories);
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

        AreaCategory child = (AreaCategory) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
//        String cityName = child.areaGroup.get(childPosition).city;

        /** 分站带回的是分站大地区名称 **/
        String siteGroupName = child.areaName;
        String siteGroupPinYin = child.pinyin;
        DebugLog.e(TAG, siteGroupPinYin);

        baseSharedPreferencesHelper.setSpecialCarAreaHistoryPinYing(siteGroupPinYin);
        ((SharedPreferencesHelper) baseSharedPreferencesHelper).setHistoryProvinceForSpecialCar(child.areaGroup.get(childPosition).province);
        ((SharedPreferencesHelper) baseSharedPreferencesHelper).setCityForSpecialCar(child.areaGroup.get(childPosition).city);

        finishSelfWithResult(siteGroupName);
        return true;
    }


    @Override
    protected void initFilterMap() {
        mFilterMap = TFilterController.getInstance().mFilterMap;
    }


    protected void setHistory(String cityName) {
        /** 判断点击是否来自于定位城市显示按钮上 **/
        if (clickFromPositionCity) {
            cityName = areaSiteUtil.getCityGroup(cityName); // 获得站点名称
        }
        String siteGroupPinYin = areaSiteUtil.getPinYin(cityName); // 获得站点对应的拼音
        baseSharedPreferencesHelper.setSpecialCarAreaHistoryPinYing(siteGroupPinYin); // 存入站点名称对应的拼音
        ((SharedPreferencesHelper) baseSharedPreferencesHelper).setHistoryCityForSpecialCar(cityName);
    }


    protected LinkedList< String > initHistoryCityList() {
        return ((SharedPreferencesHelper) baseSharedPreferencesHelper).getHistoryCityForSpecialCar();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (areaSiteUtil != null) areaSiteUtil.destroy();
    }
}
