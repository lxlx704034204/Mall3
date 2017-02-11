package com.hxqc.mall.auto.controler;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.api.AutoInfoClient;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-11
 * FIXME
 * Todo 个人车辆的管理帮助类
 */
public class MyAutoInfoHelper {
    public static final String SHARED_PREFERENCES_NAME = "com.hxqc.hxqcmall.myautoinfo";
    protected static final String AUTO_INFO =
// "com.hxqc.hxqcmall.my_auto_info_json";
            UserInfoHelper.getInstance().getPhoneNumber(SampleApplicationContext.application);
    protected static final String OFF_LINE = "com.hxqc.hxqcmall.off_line";
    private static final String TAG = "MyAutoInfoHelper";
    private static MyAutoInfoHelper instance;
    private static AutoInfoClient client;
    ArrayList<MyAuto> myAutos = null;
    private Context context;
    private SharedPreferences sharedPreferences;

    private MyAutoInfoHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    private static AutoInfoClient getClient() {
        if (client == null)
            client = new AutoInfoClient();
        return client;
    }

    public static MyAutoInfoHelper getInstance(Context context) {
        if (instance == null)
            synchronized (MyAutoInfoHelper.class) {
                if (instance == null)
                    instance = new MyAutoInfoHelper(context);
            }
        return instance;
    }

    public SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 获取匹配的车辆
     *
     * @param callBack
     */
    /*public void getMatchAuto(final ArrayList<BrandGroup> shopBrandGroups, final LoadDataCallBack<MyAuto> callBack) {
        getAutoDataFromWeb(new LoadDataCallBack<ArrayList<MyAuto>>() {
            @Override
            public void onDataNull(String message) {
                callBack.onDataNull(message);
            }

            @Override
            public void onDataGot(ArrayList<MyAuto> obj) {
                if (obj != null && obj.size() > 0)
                    checkBrand(obj, shopBrandGroups, new LoadDataCallBack<MyAuto>() {
                        @Override
                        public void onDataNull(String message) {
                            callBack.onDataNull(message);
                        }

                        @Override
                        public void onDataGot(MyAuto obj) {
                            callBack.onDataGot(obj);
                        }
                    });
                else {
                    checkBrand(getAutoDataLocal(), shopBrandGroups, new LoadDataCallBack<MyAuto>() {
                        @Override
                        public void onDataNull(String message) {
                            callBack.onDataNull(message);
                        }

                        @Override
                        public void onDataGot(MyAuto obj) {
                            callBack.onDataGot(obj);
                        }
                    });

                }
            }
        });
    }*/

    /**
     * 保存车辆信息
     *
     * @param autoInfoJson
     */
    public void saveAutoDataLocal(String autoInfoJson) {
        getSharedPreferences(context).edit().putString(AUTO_INFO, autoInfoJson).apply();
    }

    /**
     * 保存车辆信息
     *
     * @param myAutos
     */
    /*private void saveAutoDataLocal(ArrayList<MyAuto> myAutos) {
//        String json = autoDataToJSON(myAutos);
        String json = JSONUtils.toJson(myAutos);
        DebugLog.i(TAG, json);
        saveAutoDataLocal(json);
    }*/

    /**
     * 添加车辆
     *
     * @param myAuto
     */
    /*public void addAutoLocal(MyAuto myAuto) {
        ArrayList<MyAuto> autoData = getAutoDataLocal();
        if (!isContain(autoData, myAuto)) {
            autoData.add(myAuto);
            String jsonTotal = autoDataToJSON(autoData);
//            String jsonTotal = JSONUtils.toJson(myAutos);
            saveAutoDataLocal(jsonTotal);
        } else {
            DebugLog.i(TAG, "该车已存在");
        }


    }*/

    /*private boolean isContain(ArrayList<MyAuto> myAutos, MyAuto myAuto) {
        for (MyAuto auto : myAutos) {
            if (!TextUtils.isEmpty(myAuto.plateNumber) && !TextUtils.isEmpty(auto.plateNumber))
                if (myAuto.plateNumber.equals(auto.plateNumber))
                    return true;
        }
        return false;
    }*/

    /**
     * 获取车辆信息
     *
     * @return
     */
    public synchronized String getAutoJsonDataLocal() {
        return getSharedPreferences(context).getString(AUTO_INFO, null);
    }

    /**
     * 获取车辆信息
     *
     * @return
     */
    public synchronized ArrayList<MyAuto> getAutoDataLocal() {
        String autoJsonData = getAutoJsonDataLocal();

        if (TextUtils.isEmpty(autoJsonData)) {
            return new ArrayList<>();
        }

        return JSONUtils.fromJson(autoJsonData, new TypeToken<ArrayList<MyAuto>>() {
        });
    }

    /**
     * 删除车辆信息
     *
     * @return
     */
    /*public synchronized ArrayList<MyAuto> deleteAutoDataLocal(int position) {
        String autoJsonData = getAutoJsonDataLocal();

        if (TextUtils.isEmpty(autoJsonData)) {
            return new ArrayList<>();
        }
        ArrayList<MyAuto> myAutos = JSONUtils.fromJson(autoJsonData, new TypeToken<ArrayList<MyAuto>>() {
        });
        myAutos.remove(position);
        return myAutos;
    }
*/
    /**
     * 修改车辆信息
     *
     * @param position
     * @param newAuto
     * @return
     */
    /*public synchronized ArrayList<MyAuto> resetAutoDataLocal(int position, MyAuto newAuto) {
        String autoJsonData = getAutoJsonDataLocal();
        if (TextUtils.isEmpty(autoJsonData)) {
            return new ArrayList<>();
        }
        ArrayList<MyAuto> myAutos = JSONUtils.fromJson(autoJsonData, new TypeToken<ArrayList<MyAuto>>() {
        });
        if (myAutos.size() == 0 || myAutos.size() <= position)
            return new ArrayList<>();
        myAutos.set(position, newAuto);
        return myAutos;
    }*/


    /**
     * 从服务器获取个人车辆信息
     *
     * @return
     */
    /*public void getAutoDataFromWeb(final LoadDataCallBack<ArrayList<MyAuto>> callBack) {
        getClient().requestAutoInfo(new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                myAutos = JSONUtils.fromJson(responseString, new TypeToken<ArrayList<MyAuto>>() {
                });
                if (myAutos != null)
                    callBack.onDataGot(myAutos);
                else callBack.onDataNull("");
            }
        });
    }*/

    /**
     * 检索车辆
     *
     * @param plateNumber
     * @param VIN
     * @param callBack
     */
    @Deprecated
    /*public void checkAuto(final ArrayList<BrandGroup> brandGroups, String plateNumber, String VIN,
                          final LoadDataCallBack<MyAuto> callBack) {
        getClient().checkAutoInfo(VIN, plateNumber, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                MyAuto myAuto = JSONUtils.fromJson(responseString, new TypeToken<MyAuto>() {
                });
                if (myAuto != null) {
//                    boolean checkBrand = checkBrand(myAuto, brandGroups);
//                    if (checkBrand)
                    callBack.onDataGot(myAuto);
//                    else callBack.onDataNull("");
                } else callBack.onDataNull("");
            }
        });

    }*/

    /***
     * 保存车辆信息
     *
     * @param callBack
     */
    /*public void uploadAutosData(final LoadDataCallBack<String> callBack) {
        ArrayList<MyAuto> autoDataLocal = getAutoDataLocal();
        if (autoDataLocal != null)
            for (MyAuto auto : autoDataLocal) {
                uploadAutoData(auto, new LoadDataCallBack<String>() {
                    @Override
                    public void onDataNull(String message) {
                        callBack.onDataNull(message);
                        DebugLog.d(TAG, "uploadAutosData——>onDataNull");
                    }

                    @Override
                    public void onDataGot(String obj) {
                        DebugLog.d(TAG, "uploadAutosData——>onDataGot");
                    }
                });
            }
        clearAllLocalAutoData();
        callBack.onDataGot("uploadSuccess");
    }*/

    public void clearAllLocalAutoData() {
        saveAutoDataLocal("");
    }

    /**
     * 上传车辆信息
     *
     * @param auto
     * @param callBack
     */
    /*public void uploadAutoData(MyAuto auto, final LoadDataCallBack<String> callBack) {
        client = getClient();
        client.addAutoInfo(auto.myAutoID,auto.drivingDistance, auto.brand,auto.brandName, auto.brandID
                , auto.series, auto.seriesID, auto.autoModel, auto.autoModelID
                , auto.plateNumber, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        callBack.onDataGot("");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Error error = JSONUtils.fromJson(responseString, new TypeToken<Error>() {
                        });
                        if (error != null) {
                            callBack.onDataNull(error.message);
                            DebugLog.i(TAG, error.message);
                        }
                        callBack.onDataNull("resultCode is " + statusCode);

                    }
                });
    }*/



    /**
     * 检查该车品牌是否是符合品牌要求
     *
     * @param auto
     * @param brandGroups
     */
    public boolean checkBrand(MyAuto auto, ArrayList<BrandGroup> brandGroups) {
        DebugLog.i(TAG,"Myauto: " + auto.toString());
        if (auto == null) {
            return false;
        }
        if (brandGroups == null) {
            return false;
        }
//        String brandID = auto.brandID;
        String brandName = auto.brand;
        if (TextUtils.isEmpty(brandName) ||
                TextUtils.isEmpty(auto.drivingDistance) ||
                TextUtils.isEmpty(auto.seriesID) ||
                TextUtils.isEmpty(auto.autoModelID)
               /* || TextUtils.isEmpty(auto.plateNumber)*/
                ) {
            return false;
        }
        for (BrandGroup group : brandGroups) {
            ArrayList<Brand> brands = group.group;
            if (brands != null) {
                for (Brand brand : brands) {
                    if (brandName.equals(brand.brandName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 销毁
     */
    public void destory() {
        if (instance != null)
            instance = null;
    }

    /**
     * 获取品牌名
     *
     * @param shopBrandGroups
     * @return
     */
    public String getBrands(ArrayList<BrandGroup> shopBrandGroups) {
        String brands = "";
        for (BrandGroup brandGroup : shopBrandGroups) {
            for (Brand brand : brandGroup.group) {
                if (brands.isEmpty())
                    brands = brand.brandName;
                else
                    brands = brands + "、" + brand.brandName;
            }
        }
        return brands;
    }

    /**
     * 筛选合适的车辆
     *
     * @param autoData
     * @param obj
     * @param callBack
     */
    /*public void checkBrand(ArrayList<MyAuto> autoData, ArrayList<BrandGroup> obj,
                           final LoadDataCallBack<MyAuto> callBack) {
        if (autoData == null || autoData.size() == 0) {
            callBack.onDataNull("本地车辆为空");
            return;
        }
        for (final MyAuto auto : autoData) {
            boolean checkBrand = checkBrand(auto, obj);
            if (checkBrand) {
                callBack.onDataGot(auto);
                return;
            }
        }
//            String brandID = auto.brandID;
//            if (!TextUtils.isEmpty(brandID) &&
//                    !TextUtils.isEmpty(auto.seriesID) &&
//                    !TextUtils.isEmpty(auto.autoModelID) &&
//                    !TextUtils.isEmpty(auto.plateNumber) &&
//                    !TextUtils.isEmpty(auto.drivingDistance)) {
//                for (BrandGroup group : obj) {
//                    ArrayList<Brand> brands = group.getGroup();
//                    if (brands != null) {
//                        for (Brand brand : brands) {
//                            if (brandID.equals(brand.getBrandID())) {
//                                callBack.onDataGot(auto);
//                                return;
//                            }
//                        }
//                    }
//                }
//            }
        callBack.onDataNull("车辆数据无效");
    }*/


    /**
     * ArrayList<MyAuto>转JsonArray
     *
     * @param myAutos
     * @return
     */

    /*private String autoDataToJSON(ArrayList<MyAuto> myAutos) {
        return JSONUtils.toJson(myAutos);
    }*/
//
//


    /**
     * MyAuto类转json
     *
     * @param stringBuilder
     * @param myAuto
     */
//    private StringBuilder myAutoBeanToJsonStr(StringBuilder stringBuilder, MyAuto myAuto) {
//        stringBuilder.append("{")
//                .append("\"myAutoID\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.myAutoID) ? "" : myAuto.myAutoID)
//                .append("\",");
//
//        stringBuilder.append("\"brand\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.brand) ? "" : myAuto.brand)
//                .append("\",");
//
//        stringBuilder.append("\"brandID\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.brandID) ? "" : myAuto.brandID)
//                .append("\",");
//
//        stringBuilder.append("\"brandThumb\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.brandThumb) ? "" : myAuto.brandThumb)
//                .append("\",");
//
//        stringBuilder.append("\"series\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.series) ? "" : myAuto.series)
//                .append("\",");
//
//        stringBuilder.append("\"seriesID\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.seriesID) ? "" : myAuto.seriesID)
//                .append("\",");
//
//        stringBuilder.append("\"autoModel\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.autoModel) ? "" : myAuto.autoModel)
//                .append("\",");
//
//        stringBuilder.append("\"autoModelID\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.autoModelID) ? "" : myAuto.autoModelID)
//                .append("\",");
//
//        stringBuilder.append("\"plateNumber\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.plateNumber) ? "" : myAuto.plateNumber)
//                .append("\",");
//
//        stringBuilder.append("\"VIN\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.VIN) ? "" : myAuto.VIN)
//                .append("\",");
//
//        stringBuilder.append("\"drivingDistance\"").append(":")
//                .append(myAuto.drivingDistance)
//                .append(",");
//
//        stringBuilder.append("\"lastMaintenanceDistance\"").append(":")
//                .append(myAuto.lastMaintenanceDistance)
//                .append(",");
//
//        stringBuilder.append("\"ownName\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.ownName) ? "" : myAuto.ownName)
//                .append("\",");
//
//        stringBuilder.append("\"ownPhone\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.ownPhone) ? "" : myAuto.ownPhone)
//                .append("\",");
//
//        stringBuilder.append("\"lastMaintenancDate\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.lastMaintenancDate) ? "" : myAuto.lastMaintenancDate)
//                .append("\",");
//
//        stringBuilder.append("\"nextInsurance\"").append(":")
//                .append("\"")
//                .append(myAuto.nextInsurance)
//                .append("\",");
//
//        stringBuilder.append("\"nextMaintenanceDistance\"").append(":")
//                .append(myAuto.nextMaintenanceDistance)
//                .append(",");
//        //车辆是否是默认的车辆
//        stringBuilder.append("\"isDefault\"").append(":");
//        stringBuilder.append(myAuto.isDefault);
//        stringBuilder.append(",");
//
//        stringBuilder.append("\"guaranteePeriod\"").append(":")
//                .append("\"")
//                .append(TextUtils.isEmpty(myAuto.guaranteePeriod) ? "" : myAuto.guaranteePeriod)
//                .append("\"}");
//        return stringBuilder;
//    }


}
