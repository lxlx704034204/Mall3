package com.hxqc.aroundservice.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.hxqc.aroundservice.adapter.PositionListAdapter;
import com.hxqc.aroundservice.model.CityList;
import com.hxqc.aroundservice.util.AroundServiceSharePreferencesHelper;
import com.hxqc.mall.activity.BasePositionActivity;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 周边服务module中的城市定位Activity
 *
 * @author 袁秉勇
 * @since 2016年04月13日
 */
public class PositionActivity extends BasePositionActivity {
    public final static String DATA = "data";
    private final static String TAG = PositionActivity.class.getSimpleName();
    private Context mContext;
    private ListView mListView;
    private ArrayList<CityList> mCityLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (null == (mCityLists = getIntent().getParcelableArrayListExtra(DATA))) {
            mCityLists = null;
        }

        super.onCreate(savedInstanceState);

    }


    @Override
    protected void initSharePreferenceHelper() {
        baseSharedPreferencesHelper = new AroundServiceSharePreferencesHelper(this);
        setShowWholeCity(false);
        setShowWholeCityTitle(true);
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_around_services_position);
    }


    @Override
    protected void initListData() {
        header = LayoutInflater.from(this).inflate(R.layout.activity_position_header, null);
        mListView = (ListView) findViewById(R.id.location_list_view);
        mListView.addHeaderView(header);
        mListView.setDivider(getResources().getDrawable(R.color.divider));
    }


    @Override
    protected void getData() {
        if (null != mCityLists) {
            mListView.setAdapter(new PositionListAdapter(this, mCityLists));
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                    if (position == 0) return;
                    CityList cityList = mCityLists.get(position - 1);
                    finishSelfWithResult(cityList.cityname, cityList.cityid);
                }
            });
        } else {
            mListView.setAdapter(new PositionListAdapter(this, new ArrayList< CityList >()));
        }
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }


    @Override
    protected void initFilterMap() {

    }

    protected void setHistory(String cityName) {
        baseSharedPreferencesHelper.setHistoryCity(cityName);
    }
}
