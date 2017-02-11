package com.hxqc.mall.activity;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

import com.hxqc.mall.core.adapter.ExpandAdapter;
import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.db.area.TCity;
import com.hxqc.mall.core.db.area.TProvince;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.newenergy.bean.position.Province;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Function: 首页城市选择Activity
 *
 * @author 袁秉勇
 * @since 2016年04月22日
 */
public class PositionActivity extends BasePositionActivity {
    private final static String TAG = PositionActivity.class.getSimpleName();
    String[] areas = new String[]{"香港", "澳门", "台湾", "钓鱼岛"};
    ArrayList< Province > locationAreaModels = new ArrayList<>();
	private Context mContext;

    @Override
    protected void initSharePreferenceHelper() {
        baseSharedPreferencesHelper = new SharedPreferencesHelper(this);
        setShowWholeCity(false);
    }


    @Override
    protected void getData() {
        locationAreaModels = assembleAreaData();
        ExpandAdapter myAdapter = new ExpandAdapter(locationAreaModels);
        location_expand_list_view.setAdapter(myAdapter);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Province child = (Province) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
	    String cityName = child.areaGroup.get(childPosition).title;
	    finishSelfWithResult(cityName);
        return true;
    }


    @Override
    protected void initFilterMap() {

    }


    /** 拼装地区数据 **/
    private ArrayList< Province > assembleAreaData() {
	    ArrayList<TProvince> areaModels = AreaDBManager.getInstance(this.getApplicationContext()).getPList();
	    ArrayList< Province > provinces = new ArrayList<>();

        for (int i = 0; i < areaModels.size(); i++) {
            Province province = null;
            if (Arrays.asList(areas).contains(areaModels.get(i).title)) break; // 排除不需要的城市
	        province = new Province(areaModels.get(i).title, Integer.valueOf(areaModels.get(i).pid));

	        ArrayList<TCity> tCities = AreaDBManager.getInstance(this.getApplicationContext()).getCList(areaModels.get(i).pid + "");

	        province.areaGroup = tCities;
	        provinces.add(province);
        }
        return provinces;
    }
}
