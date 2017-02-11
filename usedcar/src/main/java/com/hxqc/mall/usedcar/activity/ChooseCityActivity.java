package com.hxqc.mall.usedcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;

import com.amap.api.location.AMapLocation;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.controler.AMapLocationControl;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.ChooseCityAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.City;
import com.hxqc.mall.usedcar.model.CityGroup;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.mall.usedcar.views.SideBar;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;


/**
 * 说明:选择城市
 *
 * @author: 吕飞
 * @since: 2015-08-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseCityActivity extends BackActivity implements AMapLocationControl.onCoreLocationListener, ExpandableListView.OnChildClickListener, SideBar.OnTouchingLetterChangedListener {
    public static final String CHOSEN_CITY = "chosen_city";
    protected PinnedHeaderExpandableListView mCityListView;//城市列表
    String mCurrentCity;//当前城市名
    ArrayList<CityGroup> cityGroups;//城市数据组
    ChooseCityAdapter mChooseCityAdapter;//城市适配器
    SideBar mSideBar;//侧边栏
    UsedCarSPHelper mUsedCarSPHelper;
    AMapLocationControl locationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        mUsedCarSPHelper = new UsedCarSPHelper(this);
//        cityGroups = JSONUtils.fromJson(mUsedCarSPHelper.getCityList(), new TypeToken<ArrayList<CityGroup>>() {
//        });
//        if (cityGroups != null && cityGroups.size() > 0) {
//            initList();
//        } else {
            getCity();
//        }

    }

    private void getCity() {
        new UsedCarApiClient().getCities(new BaseMallJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                cityGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<CityGroup>>() {
                });
                if (cityGroups != null) {
                    CityGroup current = new CityGroup();
                    current.groupTag = "当前位置";
                    current.group = new ArrayList<>();
                    current.group.add(new City("正在获取当前位置"));
                    cityGroups.add(0, current);
//                    String cityList = JSONUtils.toJson(cityGroups);
//                    new UsedCarSPHelper(ChooseCityActivity.this).saveCityList(cityList);
                    initList();
                }
            }
        });
    }

    private void initList() {
        mCurrentCity = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(UsedCarActivitySwitcher.CURRENT_CITY);
        mCityListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandable_list);
        mSideBar = (SideBar) findViewById(R.id.side_bar);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mCityListView.setOnChildClickListener(this);
        mCityListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        }, false);

        showList();
        if (TextUtils.isEmpty(mCurrentCity)) {
            initLoc();
        } else {
            cityGroups.get(0).group.clear();
            cityGroups.get(0).group.add(new City(mCurrentCity));
            mChooseCityAdapter.notifyDataSetInvalidated();
        }
    }

    //列表展示
    private void showList() {
        mChooseCityAdapter = new ChooseCityAdapter(this, cityGroups);
        mCityListView.setAdapter(mChooseCityAdapter);
        mCityListView.setOnHeaderUpdateListener(mChooseCityAdapter);
        OtherUtil.openAllChild(mChooseCityAdapter, mCityListView);
    }

    /**
     * 初始化定位
     */
    private void initLoc() {
        locationControl = AMapLocationControl.getInstance().setUpLocation(getApplicationContext());
        locationControl.setCoreLocationListener(this);
        locationControl.startLocation();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (!cityGroups.get(groupPosition).group.get(childPosition).city_name.equals(getResources().getString(R.string.loc_fail)) && !cityGroups.get(groupPosition).group.get(childPosition).city_name.equals(getResources().getString(R.string.locing))) {
            mUsedCarSPHelper.saveCity(cityGroups.get(groupPosition).group.get(childPosition).city_name);
            Intent intent = new Intent();
            intent.putExtra(CHOSEN_CITY, cityGroups.get(groupPosition).group.get(childPosition).city_name);
            setResult(RESULT_OK, intent);
        }
        finish();
        return false;
    }

    @Override
    public void onTouchingLetterChanged(String s, StringBuffer s1) {
        int position = 0;
        for (int i = 0; i < cityGroups.size(); i++) {
            if (cityGroups.get(i).groupTag.charAt(0) == (s.charAt(0))) {
                mCityListView.setSelection(position);
                break;
            } else {
                position = position + cityGroups.get(i).group.size() + 1;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationControl != null) {
            locationControl.stopLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationControl != null) {
            locationControl.unregistListener();
        }
    }

    @Override
    public void onLocationChange(AMapLocation aMapLocation) {
        cityGroups.get(0).group.clear();
        if (!TextUtils.isEmpty(aMapLocation.getCity())) {
            cityGroups.get(0).group.add(new City(aMapLocation.getCity()));
        } else {
            cityGroups.get(0).group.add(new City(getResources().getString(R.string.loc_fail)));
        }
        mChooseCityAdapter.notifyDataSetInvalidated();
    }
}
