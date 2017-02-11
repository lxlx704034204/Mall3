package com.hxqc.mall.core.db.area;

import android.content.Context;

import com.hxqc.mall.core.db.DbHelper;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-11-13
 * FIXME
 * Todo 全国表
 */
public class AreaDBManager {
    private static AreaDBManager ourInstance;
	Context context;

    private AreaDBManager(Context context) {
        this.context = context;
    }

    public static AreaDBManager getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (AreaDBManager.class) {
                if (ourInstance == null) {
                    ourInstance = new AreaDBManager(context);
                }
            }
        }
        return ourInstance;
    }

    public void destroy() {
        ourInstance = null;
    }


    /**
     * 根据名称获取省级id
     */
    public TProvince searchPIDByTitle(String title) {
	    return DbHelper.queryEntity(TProvince.class, TProvince_Table.title.eq(title));
    }

	/**
	 * 根据名称获取市级id
	 */
	public TCity searchCIDByTitle(String title) {
		return DbHelper.queryEntity(TCity.class, TCity_Table.title.eq(title));
	}

	/**
	 * 根据名称获取区级id
	 */
	public TDistrict searchDIDByTitle(String title) {
		return DbHelper.queryEntity(TDistrict.class, TDistrict_Table.title.eq(title));
	}

	/**
	 * 根据城市名称模糊查询该城市的id即省id
	 */
	public int[] searchPIDAndCIDByTitle(String title) {
		int[] id = new int[2];

		TCity city = DbHelper.queryEntity(TCity.class, TCity_Table.title.like(title + "%"));
		id[0] = Integer.valueOf(city.pid);
		id[1] = Integer.valueOf(city.cid);
        return id;
    }


	/**
	 * 获取省级列表
	 */
	public ArrayList<TProvince> getPList() {
		ArrayList<TProvince> tProvinces = (ArrayList<TProvince>) DbHelper.queryA(TProvince.class);
		return tProvinces;
	}

	/**
	 * 获取对应 市级列表
	 */
	public ArrayList<TCity> getCList(String condition_pid) {
		ArrayList<TCity> tCities = (ArrayList<TCity>) DbHelper.query(TCity.class, TCity_Table.pid.eq(condition_pid));
		return tCities;
	}

	/**
	 * 获取对应 区级列表
	 */
	public ArrayList<TDistrict> getDList(String condition_cid) {
		ArrayList<TDistrict> tDistricts = (ArrayList<TDistrict>) DbHelper.query(TDistrict.class, TDistrict_Table.cid.eq(condition_cid));
		return tDistricts;
	}

    /**
     * 获取对应 市级列表
     */
    public ArrayList<TCity> getCListByName(String provinceName) {
	    TProvince tProvince = DbHelper.queryEntity(TProvince.class, TProvince_Table.title.like(provinceName + "%"));

	    ArrayList<TCity> tCities = (ArrayList<TCity>) DbHelper.query(TCity.class, TCity_Table.pid.eq(tProvince.pid));
	    return tCities;
    }
    /**
     * 获取对应 区级列表
     */
    public ArrayList<TDistrict> getDListByName(String cityName) {
	    TCity tCity = DbHelper.queryEntity(TCity.class, TCity_Table.title.like(cityName + "%"));

	    ArrayList<TDistrict> tDistricts = (ArrayList<TDistrict>) DbHelper.query(TDistrict.class, TDistrict_Table.cid.eq(tCity.cid));
	    return tDistricts;
    }
}
