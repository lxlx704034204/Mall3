package com.hxqc.mall.core.db.areacacheutil_new;

import android.content.Context;
import android.text.TextUtils;

import com.hxqc.mall.core.db.CoreDatabase;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.model.LocationArea;
import com.hxqc.mall.core.model.LocationAreaModel;
import com.hxqc.util.DebugLog;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年09月28日
 */
public class AreaCacheUtil {
    public static final int NEWMALL = 0;
    public static final int NEWENERGY = 1;
    public static final int THIRDSHOP = 2;
    public static final int MAINTENANCE = 3;
    public static final int ACCESSORY = 4;
    private final static String TAG = AreaCacheUtil.class.getSimpleName();
    private static AreaCacheUtil areaCacheUtil;


    private AreaCacheUtil(Context context) {

    }


    public static AreaCacheUtil getInstance(Context context) {
        if (areaCacheUtil == null) {
            synchronized (AreaCacheUtil.class) {
                if (areaCacheUtil == null) {
                    areaCacheUtil = new AreaCacheUtil(context);
                }
            }
        }
        return areaCacheUtil;
    }


    /**
     * 获取type类型的地区数据总共有多少条
     *
     * @param type
     * @return
     */
    private long getCount(int type) {
        long count = DbHelper.query(LocationCityBean.class, LocationCityBean_Table.dataType.eq(type)).size();

        SQLite.select().from(LocationCityBean.class).where(LocationCityBean_Table.dataType.eq(type)).queryList().size();

        DebugLog.e(TAG, type + " 类型数据总共有：" + count + " 条");

        return count;
    }


    /**
     * 删除type类型的所有地区数据
     *
     * @param type
     * @return
     */
    private boolean deleteOneType(int type) {
        DebugLog.e(TAG, SQLite.delete().from(LocationCityBean.class).where(LocationCityBean_Table.dataType.eq(type)).getQuery());

        long count = SQLite.delete().from(LocationCityBean.class).where(LocationCityBean_Table.dataType.eq(type)).count();

        DebugLog.e(TAG, type + " 类型已删除数据 ：" + count + " 条！");

        return count > 0;
    }


    private boolean save(int type, List<LocationCityBean> locationCityBeans) {
        boolean flag;
        DbHelper.saveTransactionFaster(CoreDatabase.class, locationCityBeans, LocationCityBean.class);
        flag = getCount(type) > 0;
        DebugLog.e(TAG, "保存方法执行 " + (flag ? "成功" : "失败"));
        return flag;
    }


    /**
     * 获取已存在的缓存数据
     *
     * @param type
     * @return
     */
    public ArrayList<LocationAreaModel> getCache(int type) {
        return assemblyData(type);
    }


    /**
     * 检查是否存在相关类型的地区缓存数据
     *
     * @param type 参见上方静态变量 NEWMALL、NEWENERGY等。
     * @return
     */
    public boolean isExistCache(int type) {
        if (getCount(type) <= 0) {
            DebugLog.e(TAG, "不存在 " + type + " 类型缓存数据 ");
            return false;
        }
        DebugLog.e(TAG, "存在 " + type + " 类型缓存数据 ");
        return true;
    }


    /**
     * 检查地区数据是否存在，若存在就检查是否需要更新（包含更新操作），若不存在就缓存数据
     *
     * @param type
     * @param locationCityBeans
     * @return
     */
    public boolean dataChangeAndUpdate(int type, List<LocationCityBean> locationCityBeans) {
        if (locationCityBeans == null || locationCityBeans.size() <= 0) {
            DebugLog.e(TAG, "传入的地区数据为空或为零，无法进行校验");
            return false;
        }
        if (isExistCache(type)) {
            if (getCount(type) != locationCityBeans.size()) {
                if (deleteOneType(type) && save(type, locationCityBeans)) {
                    DebugLog.e(TAG, type + " 类型数据update成功");
                    return true;
                } else {
                    DebugLog.e(TAG, type + " 类型数据update失败");
                    return false;
                }
            }
        } else {
            if (save(type, locationCityBeans)) {
                DebugLog.e(TAG, type + " 类型数据写入成功");
                return true;
            } else {
                DebugLog.e(TAG, type + " 类型数据写入失败");
                return false;
            }
        }
        return false;
    }


    /**
     * 将获取的数据可存入数据库的数据格式（包含自动补全该地区数据的类型等）
     *
     * @param type
     * @param locationAreaModels
     * @return
     */
    public List<LocationCityBean> transformData(int type, List<LocationAreaModel> locationAreaModels) {
        DebugLog.e(TAG, " this is transformData function");

        List<LocationCityBean> locationCityBeans = new ArrayList<>();

        if (locationAreaModels == null || locationAreaModels.size() <= 0) return locationCityBeans;

        for (int i = 0; i < locationAreaModels.size(); i++) {
            LocationAreaModel locationAreaModel = locationAreaModels.get(i);
            for (int j = 0; j < locationAreaModel.areaGroup.size(); j++) {
                LocationCityBean locationCityBean = new LocationCityBean(type, locationAreaModel.provinceInitial, locationAreaModel.provinceName, i, locationAreaModel.areaGroup.get(j).province);
                locationCityBeans.add(locationCityBean);
            }
        }
        return locationCityBeans;
    }


    /**
     * 检查地区列表中是否存在某个城市, true 存在，false 不存在
     *
     * @param type
     * @param cityName
     * @return
     */
    public boolean checkExistCity(int type, String cityName) {
        if (TextUtils.isEmpty(cityName)) throw new IllegalArgumentException("参数cityName不能为空！");
        ArrayList<LocationAreaModel> locationAreaModels = assemblyData(type);
        if (locationAreaModels == null || locationAreaModels.size() <= 0) {
            DebugLog.e(TAG, "检查数据失败");
            return false;
        }
        for (int i = 0; i < locationAreaModels.size(); i++) {
            if (locationAreaModels.get(i).areaGroup == null || locationAreaModels.get(i).areaGroup.size() <= 0)
                break;
            for (int j = 0; j < locationAreaModels.get(i).areaGroup.size(); j++) {
                if (cityName.equals(locationAreaModels.get(i).areaGroup.get(j).province))
                    return true;
            }
        }
        return false;
    }


    /**
     * 拼装返回数据集
     *
     * @param type
     * @return
     */
    public ArrayList<LocationAreaModel> assemblyData(int type) {
        List<LocationCityBean> locationCityBeans = DbHelper.query(LocationCityBean.class, LocationCityBean_Table.dataType.eq(type)); //取出全部数据
        if (locationCityBeans == null) {
            DebugLog.e(TAG, " initialize database failed");
            return null;
        } else if (locationCityBeans.size() == 0) {
            DebugLog.e(TAG, "Database size is 0");
            return null;
        }

//            GenericRawResults< String[] > genericRawResults = dao.queryRaw("select provinceInitial, count(*) from location_city where dataType = " + type + " group by provinceGroup");
//            int groupCount = Integer.valueOf((genericRawResults.getResults().size()));

        int groupCount = SQLite.select().from(LocationCityBean.class).where(LocationCityBean_Table.dataType.eq(type)).groupBy(LocationCityBean_Table.provinceGroup).queryList().size();

        DebugLog.i(TAG, "---------------------- groupCount is : " + groupCount);

        ArrayList<LocationAreaModel> locationAreaModels = new ArrayList<>();

        for (int i = 0; i < groupCount; i++) {
            LocationAreaModel locationAreaModel = new LocationAreaModel();

            for (LocationCityBean locationCityBean : locationCityBeans) {
                if (locationAreaModel.areaGroup == null)
                    locationAreaModel.areaGroup = new ArrayList<>();

                if (i == (locationCityBean.getProvinceGroup())) {

                    if (TextUtils.isEmpty(locationAreaModel.provinceInitial))
                        locationAreaModel.provinceInitial = locationCityBean.provinceInitial;
                    if (TextUtils.isEmpty(locationAreaModel.provinceName))
                        locationAreaModel.provinceName = locationCityBean.provinceName;

                    LocationArea locationArea = new LocationArea(locationCityBean.getProvince());

                    locationAreaModel.areaGroup.add(locationArea);
                }
            }

            locationAreaModels.add(locationAreaModel);
        }

        return locationAreaModels; //返回拼装后的数据
    }


    /**
     * 关闭数据资源的连接
     **/
    public void close() {
        if (areaCacheUtil != null) areaCacheUtil = null;
    }
}
