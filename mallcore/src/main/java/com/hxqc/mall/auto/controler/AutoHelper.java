package com.hxqc.mall.auto.controler;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.AutoModelGroup;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.core.util.utils.disklurcache.DiskLruCacheHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 20
 * FIXME
 * Todo
 */
public class AutoHelper {

    public static final String SHARED_PREFERENCES_NAME = "com.hxqc.hxqcmall.myautoinfo";//存储文件夹
    public static final String AUTO_INFO = "com.hxqc.hxqcmall.auto_info_json";
    //    public static final String ALL_AUTO = "com.hxqc.hxqcmall.all_auto_json";
    public static final String AUTO_LOCAL_INFO = "local_auto_info_json";//缓存网络车辆
    //    public static final String AUTO_LOCAL_INFO = UserInfoHelper.getInstance().getPhoneNumber(SampleApplicationContext.application);//缓存网络车辆
//    public static final String AUTO_DETAIL_INFO = "detail_auto_info_json";//添加车辆
    public static final String AUTO_DETAIL_INFO = AUTO_LOCAL_INFO;//添加车辆
    public static final String RESERVE_MAINTAIN_AUTO_INFO = "reserve_maintain_auto_info";
    public static final String SWITCH_AUTO = "switch_auto_json";//换车车辆
    public static final String JSON_BRAND = "json_brand";//品牌数据
    public static final String JSON_SERIES = "json_series";//车系数据
    public static final String JSON_MODEL = "json_model";//车型数据
    private static final String TAG = "AutoHelper";
    private static AutoHelper ourInstance;
    private SharedPreferences sharedPreferences;
    private DiskLruCacheHelper mDiskLruCacheHelper;

    public static AutoHelper getInstance() {
        if (ourInstance == null) {
            synchronized (AutoHelper.class) {
                if (ourInstance == null) {
                    ourInstance = new AutoHelper();
                }
            }
        }
        return ourInstance;
    }

    public void killInstance(Context context) {

        AutoInfoControl.getInstance().killInstance();
        AutoTypeControl.getInstance().killInstance();
//        AutoDao.getInstance().killInstance(context);
        if (ourInstance != null) {
            ourInstance = null;
        }

    }

    /**
     * 获取品牌名
     *
     * @param shopBrandGroups
     * @return
     */
    public String getBrand(ArrayList<BrandGroup> shopBrandGroups) {
        String brands = "";
        for (BrandGroup brandGroup : shopBrandGroups) {
            for (Brand brand : brandGroup.group) {
                brands = brands + "  " + brand.brandName;
            }
        }
        return brands;
    }

    /**
     * 得到所有品牌
     *
     * @param brandGroups
     * @return
     */
    public ArrayList<Brand> getAllBrands(ArrayList<BrandGroup> brandGroups) {
        ArrayList<Brand> brands = new ArrayList<Brand>();
        if (brandGroups != null && !brandGroups.isEmpty()) {
            for (int i = 0; i < brandGroups.size(); i++) {
                for (int j = 0; j < brandGroups.get(i).group.size(); j++) {
                    brands.add(brandGroups.get(i).group.get(j));
                }
            }
            return brands;
        }
        return brands;
    }

    /**
     * @param brands
     * @return
     */
    public ArrayList<Brand> distinctArrayList(ArrayList<Brand> brands) {
        ArrayList<Brand> newBrands = new ArrayList<>();
        for (int i = 0; i < brands.size(); i++) {
            if (i == 0) {
                newBrands.add(brands.get(i));
            } else if (i > 0 && i < brands.size() - 1) {
                if (!brands.get(i).brandID.equals(brands.get(i + 1).brandID)) {
                    newBrands.add(brands.get(i));
                }
            } else if (i == brands.size() - 1) {
                for (int j = 0; j < brands.size(); j++) {
                    if (!brands.get(i).brandID.equals(brands.get(j).brandID)) {
                        newBrands.add(brands.get(i));
                    }
                }
            }
        }
        return newBrands;
    }

    /**
     * 匹配本地数据车辆
     *
     * @param context
     * @param shopID
     * @param localCallBack
     */
    public void checkLocationMyAuto(final Context context, String shopID, @NonNull final CallBackControl.LocalCallBack<MyAuto> localCallBack) {

        AutoTypeControl.getInstance().requestBrand(context, shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
            MyAuto myAuto = null;
            ArrayList<MyAuto> myAutos = null;

            @Override
            public void onSuccess(ArrayList<BrandGroup> brandGroups) {
                ArrayList<Brand> allBrands = getAllBrands(brandGroups);
                ArrayList<MyAuto> autoDataLocal = getAutoLocal(context, AUTO_LOCAL_INFO);
                DebugLog.i(TAG, "autoDataLocal:" + autoDataLocal.size());
                if (myAutos == null) {
                    myAutos = new ArrayList<MyAuto>();
                }
                if (autoDataLocal != null && !autoDataLocal.isEmpty()) {
                    DebugLog.i(TAG, "检索Json");
                    for (int i = 0; i < allBrands.size(); i++) {
                        for (int j = 0; j < autoDataLocal.size(); j++) {
                            if (autoDataLocal.get(j).brand.equals(allBrands.get(i).brandName)) {
                                myAutos.add(autoDataLocal.get(j));
                            }
                        }
                    }
                    if (!myAutos.isEmpty()) {
                        localCallBack.onSuccess(myAutos.get(0));
                    } else {
                        localCallBack.onFailed(null);
                    }
                } else {
                    localCallBack.onFailed(null);
                }
          /*      else {
                    DebugLog.i(TAG, "检索数据库");
                    for (int i = 0; i < allBrands.size(); i++) {
                        myAuto = AutoDao.getInstance().queryByBrandIDForFirst(context, allBrands.get(i).brandID);
                    }
                    if (myAuto != null) {
                        checkMyAutoHandler.onCheckLocationSucceed(myAuto);
                    } else {
                        checkMyAutoHandler.onCheckLocationFailed(null);
                    }
                }*/
            }

            @Override
            public void onFailed(boolean offLine) {
                localCallBack.onFailed(null);
            }
        });
    }

    /**
     * 匹配网络数据车辆
     *
     * @param context
     * @param shopID
     * @param localCallBack
     */
    public void checkNetWorkMyAuto(final Context context, String shopID, @NonNull final CallBackControl.LocalCallBack<MyAuto> localCallBack) {
        AutoTypeControl.getInstance().requestBrand(context, shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
            MyAuto myAuto = null;

            @Override
            public void onSuccess(ArrayList<BrandGroup> brandGroups) {
                final ArrayList<Brand> allBrands = getAllBrands(brandGroups);
                AutoInfoControl.getInstance().requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
                    @Override
                    public void onSuccess(ArrayList<MyAuto> response) {
                        if (response != null && !response.isEmpty()) {
                            for (int i = 0; i < response.size(); i++) {
                                for (int j = 0; j < allBrands.size(); j++) {
                                    if (response.get(i).brand.equals(allBrands.get(j).brandName)) {
                                        myAuto = response.get(i);
                                        break;
                                    }
                                }
                                if (myAuto != null) {
                                    break;
                                }
                            }
                            if (myAuto != null) {
                                localCallBack.onSuccess(myAuto);
                            } else {
                                localCallBack.onFailed(null);
                            }
                        } else {
                            localCallBack.onFailed(null);
                        }
                    }

                    @Override
                    public void onFailed(boolean offLine) {
                        localCallBack.onFailed(null);
                    }
                });
                        /*new AutoInfoControl.AutoInfoHandler() {
                    @Override
                    public void onAutoInfoSucceed(ArrayList<MyAuto> myAutos) {
                        for (int i = 0; i < myAutos.size(); i++) {
                            for (int j = 0; j < allBrands.size(); j++) {
                                if (myAutos.get(i).brandID.equals(allBrands.get(j).brandID)) {
                                    myAuto = myAutos.get(i);
                                }
                            }
                        }
                        if (myAuto != null) {
                            localCallBack.onSuccess(myAuto);
                        } else {
                            localCallBack.onFailed(null);
                        }
                    }

                    @Override
                    public void onAutoInfoFailed(boolean offLine) {
                        localCallBack.onFailed(null);
                    }
                });*/
            }

            @Override
            public void onFailed(boolean offLine) {

            }
        });
    }

    public SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 获取车辆信息-json
     *
     * @param context
     * @return
     */
    public synchronized String getAutoJsonDataLocal(Context context, String localUrl) {
        return getSharedPreferences(context).getString(localUrl, null);
    }

    /**
     * 保存车辆信息-json
     *
     * @param context
     * @param autoInfoJson
     * @return
     */
    public boolean saveAutoLocal(Context context, String autoInfoJson, String localUrl) {
        getSharedPreferences(context).edit().putString(localUrl, autoInfoJson).apply();
        ArrayList<MyAuto> autoDataLocal = getAutoLocal(context, localUrl);
        return autoDataLocal != null && autoDataLocal.size() > 0;
    }

    /**
     * 获取车辆-json
     *
     * @param context
     * @return
     */
    public synchronized ArrayList<MyAuto> getAutoLocal(Context context, String localUrl) {
        DebugLog.i(TAG, "localUrl: " + localUrl);
        String autoJsonData = getAutoJsonDataLocal(context, localUrl);
        DebugLog.i(TAG, autoJsonData);
        if (TextUtils.isEmpty(autoJsonData)) {
            return new ArrayList<>();
        }
        return JSONUtils.fromJson(autoJsonData, new TypeToken<ArrayList<MyAuto>>() {
        });
    }

    /**
     * 获取车辆-json
     *
     * @param context
     * @return
     */
    public synchronized LinkedList<MyAuto> getAutoLocaltoLinkedList(Context context, String localUrl) {
        String autoJsonData = getAutoJsonDataLocal(context, localUrl);
        if (TextUtils.isEmpty(autoJsonData)) {
            return new LinkedList<>();
        }
        return JSONUtils.fromJson(autoJsonData, new TypeToken<LinkedList<MyAuto>>() {
        });
    }

    /**
     * @param context
     * @param autoData
     * @param localUrl
     */
    public void createAutoLocal(Context context, ArrayList<MyAuto> autoData, String localUrl) {
//        ArrayList<MyAuto> autoLocal = getAutoLocal(context, localUrl);
        String jsonTotal = "";
        if (autoData != null && !autoData.isEmpty()) {
            jsonTotal = autoDataToJSON(autoData);
        }
        boolean isSave = saveAutoLocal(context, jsonTotal, localUrl);
        if (isSave) {
            DebugLog.i(TAG, "创建成功");
        } else {
            DebugLog.i(TAG, "创建失败");
        }
    }

    /**
     * 创建BrandGroup-json
     *
     * @param context
     * @param data
     * @param localUrl
     */
    public void createBrandGroupLocal(Context context, ArrayList<BrandGroup> data, String localUrl) {
        String jsonTotal = JSONUtils.toJson(data);
        getSharedPreferences(context).edit().putString(localUrl, jsonTotal).apply();
        ArrayList<BrandGroup> brandGroupLocal = getBrandGroupLocal(context, localUrl);
        if (brandGroupLocal != null && brandGroupLocal.size() > 0) {
            DebugLog.i(TAG, "创建成功");
        } else {
            DebugLog.i(TAG, "创建失败");
        }
    }

    /**
     * 获取BrandGroup-json
     *
     * @param context
     * @return
     */
    public synchronized ArrayList<BrandGroup> getBrandGroupLocal(Context context, String localUrl) {
        String autoJsonData = getAutoJsonDataLocal(context, localUrl);
        if (TextUtils.isEmpty(autoJsonData)) {
            return new ArrayList<>();
        }
        return JSONUtils.fromJson(autoJsonData, new TypeToken<ArrayList<BrandGroup>>() {
        });
    }

    /**
     * 创建SeriesGroup-json
     *
     * @param context
     * @param data
     * @param localUrl
     */
    public void createSeriesGroupLocal(Context context, ArrayList<SeriesGroup> data, String localUrl) {
        String jsonTotal = JSONUtils.toJson(data);
        getSharedPreferences(context).edit().putString(localUrl, jsonTotal).apply();
        ArrayList<SeriesGroup> seriesGroupLocal = getSeriesGroupLocal(context, localUrl);
        if (seriesGroupLocal != null && seriesGroupLocal.size() > 0) {
            DebugLog.i(TAG, "创建成功");
        } else {
            DebugLog.i(TAG, "创建失败");
        }
    }

    /**
     * 获取SeriesGroup-json
     *
     * @param context
     * @return
     */
    public synchronized ArrayList<SeriesGroup> getSeriesGroupLocal(Context context, String localUrl) {
        String autoJsonData = getAutoJsonDataLocal(context, localUrl);
        if (TextUtils.isEmpty(autoJsonData)) {
            return new ArrayList<>();
        }
        return JSONUtils.fromJson(autoJsonData, new TypeToken<ArrayList<SeriesGroup>>() {
        });
    }

    /**
     * 创建AutoModelGroup-json
     *
     * @param context
     * @param data
     * @param localUrl
     */
    public void createAutoModelGroupLocal(Context context, ArrayList<AutoModelGroup> data, String localUrl) {
        String jsonTotal = JSONUtils.toJson(data);
        getSharedPreferences(context).edit().putString(localUrl, jsonTotal).apply();
        ArrayList<AutoModelGroup> autoModelGroupNLocal = getAutoModelGroupNLocal(context, localUrl);
        if (autoModelGroupNLocal != null && autoModelGroupNLocal.size() > 0) {
            DebugLog.i(TAG, "创建成功");
        } else {
            DebugLog.i(TAG, "创建失败");
        }
    }

    /**
     * 获取SeriesGroup-json
     *
     * @param context
     * @return
     */
    public synchronized ArrayList<AutoModelGroup> getAutoModelGroupNLocal(Context context, String localUrl) {
        String autoJsonData = getAutoJsonDataLocal(context, localUrl);
        if (TextUtils.isEmpty(autoJsonData)) {
            return new ArrayList<AutoModelGroup>();
        }
        return JSONUtils.fromJson(autoJsonData, new TypeToken<ArrayList<AutoModelGroup>>() {
        });
    }

    /**
     * 添加车辆-json
     *
     * @param context
     * @param myAuto
     * @return
     */
    public boolean addOrEditAutoLocaltoLinkedList(Context context, MyAuto myAuto, String localUrl) {
        myAuto.localAuto = "1";
        LinkedList<MyAuto> autoData = getAutoLocaltoLinkedList(context, localUrl);

        if (autoData != null && !autoData.isEmpty()) {
            autoData.addFirst(myAuto);
            String jsonTotal = autoDataToJSON(autoData);
            return saveAutoLocal(context, jsonTotal, localUrl);
        } else {
            autoData = new LinkedList<>();
            autoData.addFirst(myAuto);
            String jsonTotal = autoDataToJSON(autoData);
            return saveAutoLocal(context, jsonTotal, localUrl);
        }
    }

    /**
     * @param context
     * @param myAuto
     * @param localUrl
     */
    public void deleteSwitchAuto(Context context, MyAuto myAuto, String localUrl) {
        ArrayList<MyAuto> autoLocal = getAutoLocal(context, localUrl);

        if (autoLocal != null && !autoLocal.isEmpty()) {
            for (int i = 0; i < autoLocal.size(); i++) {
                if (!TextUtils.isEmpty(autoLocal.get(i).plateNumber)) {
                    if (myAuto.plateNumber.equals(autoLocal.get(i).plateNumber)) {
                        autoLocal.remove(i);
                    }
                }
            }
            String jsonTotal = autoDataToJSON(autoLocal);
            saveAutoLocal(context, jsonTotal, localUrl);
        }
    }

    /**
     * 清除所有本地信息
     *
     * @param context
     */
    public void clearLocalDataAll(Context context) {
        clearLocalData(context, AutoHelper.AUTO_LOCAL_INFO);
        clearLocalData(context, AutoHelper.AUTO_DETAIL_INFO);
        clearLocalData(context, AutoHelper.RESERVE_MAINTAIN_AUTO_INFO);
        clearLocalData(context, AutoHelper.SWITCH_AUTO);
    }

    /**
     * 清空数据
     *
     * @param context
     * @param localUrl
     */
    public void clearLocalData(Context context, String localUrl) {
        DebugLog.i(TAG, "清空数据");
        getSharedPreferences(context).edit().putString(localUrl, "").apply();
    }

    /**
     * 选择车辆-json
     *
     * @param context
     * @param myAuto
     * @param postion
     * @param localUrl
     */
    public void switchAutoLocal(Context context, MyAuto myAuto, int postion, String localUrl) {
        ArrayList<MyAuto> autoData = getAutoLocal(context, localUrl);
        if (autoData == null || autoData.isEmpty()) {
            autoData = new ArrayList<>();
            autoData.add(myAuto);
        } else {
//            MyAuto swtichAuto = autoData.get(0);
            autoData.set(0, myAuto);
//            autoData.set(postion, swtichAuto);
        }
        String jsonTotal = autoDataToJSON(autoData);
        boolean isSave = saveAutoLocal(context, jsonTotal, localUrl);
        if (isSave) {
            DebugLog.i(TAG, "选择保存成功");
        } else {
            DebugLog.i(TAG, "选择保存失败");
        }
    }

    /**
     * 修改车辆-json
     *
     * @param context
     * @param myAuto
     */
    public void editAutoDataLocal(Context context, MyAuto myAuto, String localUrl) {
        ArrayList<MyAuto> autoDataLocal = getAutoLocal(context, localUrl);
        if (autoDataLocal != null && !autoDataLocal.isEmpty()) {
            try {
                for (int i = 0; i < autoDataLocal.size(); i++) {
                    if (TextUtils.isEmpty(autoDataLocal.get(i).plateNumber)) {
                        if ((myAuto.brand.equals(autoDataLocal.get(i).brand)) && (myAuto.series.equals(autoDataLocal.get(i).series)) && (myAuto.autoModel.equals(autoDataLocal.get(i).autoModel))) {
                            autoDataLocal.set(i, myAuto);
                        }
                    } else {
                        if ((myAuto.brand.equals(autoDataLocal.get(i).brand)) && (myAuto.series.equals(autoDataLocal.get(i).series)) && (myAuto.autoModel.equals(autoDataLocal.get(i).autoModel)) && (myAuto.plateNumber.equals(autoDataLocal.get(i).plateNumber))) {
                            autoDataLocal.set(i, myAuto);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            autoDataLocal = new ArrayList<>();
            autoDataLocal.add(myAuto);
        }

        String jsonTotal = autoDataToJSON(autoDataLocal);
        boolean isSave = saveAutoLocal(context, jsonTotal, localUrl);
        if (isSave) {
            DebugLog.i(TAG, "选择保存成功");
        } else {
            DebugLog.i(TAG, "选择保存失败");
        }
    }

    /**
     * ArrayList<MyAuto>转JsonArray
     *
     * @param myAutos
     * @return
     */

    private String autoDataToJSON(ArrayList<MyAuto> myAutos) {
        return JSONUtils.toJson(myAutos);
    }

    /**
     * LinkedList<MyAuto>转JsonArray
     *
     * @param myAutos
     * @return
     */
    private String autoDataToJSON(LinkedList<MyAuto> myAutos) {
        return JSONUtils.toJson(myAutos);
    }

    public void saveAutoDatas(Context context, ArrayList<MyAuto> myAutos) {
        mDiskLruCacheHelper = DiskLruCacheHelper.builder(context);
        mDiskLruCacheHelper.put("save_datas", JSONUtils.toJson(myAutos));
    }

    public ArrayList<MyAuto> getAutoDatas() {
        return JSONUtils.fromJson(mDiskLruCacheHelper.getAsString("save_datas"), new TypeToken<ArrayList<MyAuto>>() {
        });
    }


}
