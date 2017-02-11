package com.hxqc.mall.auto.controler;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.api.AutoInfoClient;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MaintenanceRecord;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.hxqc.mall.auto.config.AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME;
import static com.hxqc.mall.auto.controler.AutoSPControl.getDialogCount;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * Des: 车辆信息控制器
 * FIXME
 * Todo
 */
public class AutoInfoControl {

    private static final String TAG = AutoInfoContants.LOG_J;
    private static AutoInfoControl ourInstance;
    private AutoInfoClient mAutoInfoClient;
    private ArrayList<MyAuto> myAutoGroup;

    public ArrayList<MyAuto> getMyAutoGroup() {
        return myAutoGroup;
    }

    public static AutoInfoControl getInstance() {
        if (ourInstance == null) {
            synchronized (AutoInfoControl.class) {
                if (ourInstance == null) {
                    ourInstance = new AutoInfoControl();
                }
            }
        }
        return ourInstance;
    }

    private AutoInfoControl() {
        mAutoInfoClient = new AutoInfoClient();
    }

    public void killInstance() {
        if (mAutoInfoClient != null) {
            mAutoInfoClient = null;
        }

        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    /**
     * 请求车辆信息
     *
     * @param context
     * @param callBack
     */
    public void requestAutoInfo(Context context, @NonNull final CallBackControl.CallBack<ArrayList<MyAuto>> callBack) {
        mAutoInfoClient.requestAutoInfo(new LoadingAnimResponseHandler(context, true, false, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<MyAuto> tMyAutoGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<MyAuto>>() {
                });
                myAutoGroup = tMyAutoGroups;
                callBack.onSuccess(tMyAutoGroups);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 修改车辆信息
     *
     * @param context
     * @param myAuto
     * @param callBack
     */
    public void editAutoInfo(Context context, MyAuto myAuto, @NonNull final CallBackControl.CallBack<String> callBack) {
        mAutoInfoClient.editAutoInfo(myAuto, new LoadingAnimResponseHandler(context, true, false, "") {

            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 修改车辆信息
     *
     * @param context
     * @param myAuto
     * @param callBack
     */
    public void editAutoInfoSQ(Context context, MyAuto myAuto, @NonNull final CallBackControl.CallBack<String> callBack) {
        mAutoInfoClient.editAutoInfo(myAuto, new LoadingAnimResponseHandler(context, false, false, false) {

            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 添加车辆信息
     *
     * @param context
     * @param myAuto
     * @param callBack
     */
    public void addAutoInfo(Context context, MyAuto myAuto, @NonNull final CallBackControl.CallBack<String> callBack) {
        mAutoInfoClient.addAutoInfo(myAuto, new BaseMallJsonHttpResponseHandler(context) {

            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 删除车辆
     *
     * @param context
     * @param myAutoID
     * @param callBack
     */
    public void deleteAutoInfo(Context context, String myAutoID, @NonNull final CallBackControl.CallBack<String> callBack) {
        DebugLog.i(TAG, "删除开始:" + System.currentTimeMillis());
        mAutoInfoClient.deleteAutoInfo(myAutoID, new LoadingAnimResponseHandler(context, true, false) {

            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DebugLog.i(TAG, "删除结束:" + System.currentTimeMillis());
                DebugLog.i(TAG, statusCode + "-----------" + responseString + "----------" + throwable.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 检查车辆
     *
     * @param context
     * @param VIN
     * @param plateNumber
     * @param callBack
     */
    @Deprecated
    public void checkAutoInfo(Context context, String VIN, String plateNumber, @NonNull final CallBackControl.CallBack<MyAuto> callBack) {
        mAutoInfoClient.checkAutoInfo(VIN, plateNumber, new LoadingAnimResponseHandler(context, false) {

            @Override
            public void onSuccess(String response) {
                MyAuto tMyAuto = JSONUtils.fromJson(response, new TypeToken<MyAuto>() {
                });
                callBack.onSuccess(tMyAuto);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 保养记录
     *
     * @param context
     * @param myAuto
     * @param callBack
     */
    public void maintenancRecord(Context context, MyAuto myAuto,
                                 @NonNull final CallBackControl.CallBack<MaintenanceRecord> callBack) {
        mAutoInfoClient.getMaintenanceRecord(myAuto.myAutoID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                MaintenanceRecord tMyMaintenanceRecord = JSONUtils.fromJson(response, new TypeToken<MaintenanceRecord>() {
                });
                callBack.onSuccess(tMyMaintenanceRecord);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 工单请求
     *
     * @param workOrderID
     * @return
     */
    public String workOrderDetail(String workOrderID, String erpShopCode) {
        String control = "/MyAuto/workOrderDetail?" +
                "token=" +
                UserInfoHelper.getInstance().getToken(SampleApplicationContext.application) +
                "&" +
                "workOrderID=" +
                workOrderID +
                "&" +
                "erpShopCode=" +
                erpShopCode;
        return ApiUtil.getAccountURL(control);

    }

    /**
     * 优惠卷规则
     *
     * @return
     */
    public String couponRules() {
        return mAutoInfoClient.couponRules();
    }

    /**
     * 保养页面分发
     *
     * @param context
     */
    public void isHaveAuto(final Context context) {

        if (UserInfoHelper.getInstance().isLogin(SampleApplicationContext.context)) {
            DebugLog.i(TAG, "登录情况下获取本地车辆数据");
            ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.SWITCH_AUTO);
            if (autoLocal != null && !autoLocal.isEmpty()) {
                DebugLog.i(TAG, "登录情况下获取选择车辆数据");
                ActivitySwitchAutoInfo.toShopQuoteActivity(context, autoLocal.get(0));
            } else {
                requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
                    @Override
                    public void onSuccess(ArrayList<MyAuto> response) {
                        int k = -1;
                        if (response != null && !response.isEmpty()) {
                            for (int i = 0; i < response.size(); i++) {
                                if (!TextUtils.isEmpty(response.get(i).brandID) && !TextUtils.isEmpty(response.get(i).brand) && !TextUtils.isEmpty(response.get(i).seriesID) && !TextUtils.isEmpty(response.get(i).series) && !TextUtils.isEmpty(response.get(i).autoModelID) && !TextUtils.isEmpty(response.get(i).autoModel)) {
                                    k = i;
                                    break;
                                }
                            }
                            DebugLog.i(TAG, "k " + k);
                            if (k == -1) {
                                if (getDialogCount() != 1) {
                                    ActivitySwitchAutoInfo.toDialogActivity(context, response.get(0), AutoInfoContants.FLAG_MODULE_MAINTAIN);
                                } else {
                                    ActivitySwitchAutoInfo.toAddAuto(context, null, -1);
                                }
                            } else {
                                ActivitySwitchAutoInfo.toShopQuoteActivity(context, response.get(k));
                            }
                        } else {
                            getAutoLocation(context, -1);
                        }
                    }

                    @Override
                    public void onFailed(boolean offLine) {
                        getAutoLocation(context, -1);
                    }
                });
            }
        } else {
            DebugLog.i(TAG, "未登录情况下获取本地车辆数据");
            getAutoLocation(context, -1);
        }
    }

    /**
     * 本地车辆的获取
     *
     * @param context
     */
    /*public void getAutoLocation(Context context) {
        ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.AUTO_LOCAL_INFO);
        if (autoLocal != null && !autoLocal.isEmpty()) {
            if (mFagActivity == FLAG_ACTIVITY_APPOINTMENT_HOME) {
                //TODO 首页-带本地车辆去预约修车店铺页面
                ActivitySwitchAutoInfo.toAppiontmentMaintenance(context, autoLocal.get(0));
            } else {
                //带本地车辆去常规保养页面
                ActivitySwitchAutoInfo.toShopQuoteActivity(context, autoLocal.get(0));
            }
        } else {
//            ActivitySwitchAutoInfo.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.HOME_PAGE, true);
            if (mFagActivity == FLAG_ACTIVITY_APPOINTMENT_HOME) {
                //TODO 首页-去选择品牌页面
                ActivitySwitchBase.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME, "", true);
            } else {
                //去常规保养添加车辆页面
                ActivitySwitchAutoInfo.toMaintainEditAutoInfo(context);
            }
        }
    }*/

    /**
     * 本地车辆的获取
     *
     * @param context
     * @param flagActivity
     */
    public void getAutoLocation(Context context, int flagActivity) {
        ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.AUTO_LOCAL_INFO);
        if (autoLocal != null && !autoLocal.isEmpty()) {
            if (flagActivity == FLAG_ACTIVITY_APPOINTMENT_HOME) {
                //TODO 首页-带本地车辆去预约修车店铺页面
                ActivitySwitchAutoInfo.toAppiontmentMaintenance(context, autoLocal.get(0));
            } else {
                //带本地车辆去常规保养页面
                ActivitySwitchAutoInfo.toShopQuoteActivity(context, autoLocal.get(0));
            }
        } else {
//            ActivitySwitchAutoInfo.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.HOME_PAGE, true);
            if (flagActivity == FLAG_ACTIVITY_APPOINTMENT_HOME) {
                //TODO 首页-去选择品牌页面
                ActivitySwitchBase.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME, "", true);
            } else {
                //去常规保养添加车辆页面
                ActivitySwitchAutoInfo.toMaintainEditAutoInfo(context);
            }
        }
    }

    /**
     * 校验本地车辆
     *
     * @param context
     * @param myAuto
     */
    public void checkAuto(Context context, MyAuto myAuto, boolean isChange) {
        ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.SWITCH_AUTO);
        boolean isMatch = false;
        if (autoLocal != null && !autoLocal.isEmpty()) {
            for (int i = 0; i < autoLocal.size(); i++) {
                if (myAuto.plateNumber.equals(autoLocal.get(i).plateNumber)) {
                    isMatch = true;
                }
            }
        }
        if (isMatch) {
            if (isChange) {
                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(context, myAuto, AutoHelper.SWITCH_AUTO);
            } else {
//                AutoHelper.getInstance().clearLocalData(context, AutoHelper.SWITCH_AUTO);
            }
        }
    }

    public interface CheckDataCallBack {
        void checkData(boolean isCheck);
    }

    /**
     * 校验网络车辆
     *
     * @param context
     * @param myAuto
     * @param checkData
     */
    public void checkAutoNetWork(Context context, final MyAuto myAuto, final CheckDataCallBack checkData) {

        if (myAuto.authenticated != 1) {
            requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
                @Override
                public void onSuccess(ArrayList<MyAuto> response) {
                    boolean isCheck = false;
                    if (response != null && !response.isEmpty()) {
                        DebugLog.i(TAG, "myAuto: " + myAuto.plateNumber);
                        for (MyAuto myAutoN : response) {
                            if (myAuto.plateNumber.equals(myAutoN.plateNumber)) {
                                DebugLog.i(TAG, "myAutoN: " + myAutoN.plateNumber);
                                isCheck = true;
                            }
                        }
                        checkData.checkData(isCheck);
                    } else {
                        checkData.checkData(isCheck);
                    }
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        }
    }

    /**
     * 换车逻辑
     *
     * @param context
     * @param myAuto
     * @param shopID
     * @param flagActivity
     * @param brands
     */
    public void chooseAuto(final Context context, final MyAuto myAuto, final String shopID, final int flagActivity, final ArrayList<Brand> brands) {
        requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
            @Override
            public void onSuccess(ArrayList<MyAuto> response) {
                if (response != null && !response.isEmpty()) {
                    if (response.size() >= 2) {
                        DebugLog.i(TAG, "数量大于等于2");
                        ActivitySwitchAutoInfo.toMaintainAutoInfo((Activity) context, shopID, response, flagActivity, brands);
                    } else if (response.size() == 1) {
                        DebugLog.i(TAG, "数量等于1");
                        if (TextUtils.isEmpty(myAuto.plateNumber)) {
                            ActivitySwitchAutoInfo.toMaintainAutoInfo((Activity) context, shopID, response, flagActivity, brands);
                        } else {
                            if (myAuto.plateNumber.equals(response.get(0).plateNumber)) {
                                ActivitySwitchBase.toChooseBrandActivity(context, new MyAuto(), flagActivity, shopID, true);
                            } else {
                                ActivitySwitchAutoInfo.toMaintainAutoInfo((Activity) context, shopID, response, flagActivity, brands);
                            }
                        }
                    } else {
                        DebugLog.i(TAG, "数量等于0");
                        ActivitySwitchAutoInfo.toChooseBrandActivity(context, new MyAuto(), flagActivity, shopID, true);
                    }
                } else {
                    ActivitySwitchAutoInfo.toChooseBrandActivity(context, new MyAuto(), flagActivity, shopID, true);
                }
            }

            @Override
            public void onFailed(boolean offLine) {
                ActivitySwitchAutoInfo.toMaintainAutoInfo((Activity) context, shopID, null, flagActivity, brands);
            }
        });
    }

    /**
     * 获取匹配的车辆
     *
     * @param context
     * @param shopID
     * @param myAutoCallBack
     */
    public void getMatchAuto(final Context context, String shopID, final CallBackControl.AutoCallBack<MyAuto> myAutoCallBack) {
        AutoTypeControl.getInstance().requestBrand(context, shopID,
                new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
                    @Override
                    public void onFailed(boolean offLine) {
                        DebugLog.e(TAG, "店铺品牌列表为空！");
                    }

                    @Override
                    public void onSuccess(final ArrayList<BrandGroup> brandGroup) {
                        final ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.SWITCH_AUTO);
                        DebugLog.i(TAG, "autoLocal: " + autoLocal.size());
                        final ArrayList<Brand> allBrands = AutoHelper.getInstance().getAllBrands(brandGroup);
                        ArrayList<MyAuto> matchSwitchAutoList = getMatchAutoList(allBrands, autoLocal);
                        if (matchSwitchAutoList != null && !matchSwitchAutoList.isEmpty()) {
                            myAutoCallBack.onSuccess(matchSwitchAutoList.get(0), brandGroup);
                        } else {
                            if (UserInfoHelper.getInstance().isLogin(context)) {
                                requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
                                    @Override
                                    public void onSuccess(final ArrayList<MyAuto> myAutos) {
                                        if (myAutos != null && !myAutos.isEmpty()) {
                                            ArrayList<MyAuto> matchNetAutoList = getMatchAutoList(allBrands, myAutos);
                                            if (matchNetAutoList != null && !matchNetAutoList.isEmpty()) {
                                                myAutoCallBack.onSuccess(matchNetAutoList.get(0), brandGroup);
                                            } else {
                                                getLocationMatchAuto(context, allBrands, new CallBackControl.CallBack<MyAuto>() {
                                                    @Override
                                                    public void onSuccess(MyAuto response) {
                                                        myAutoCallBack.onSuccess(response, brandGroup);
                                                    }

                                                    @Override
                                                    public void onFailed(boolean offLine) {
                                                        myAutoCallBack.onFailed(offLine, brandGroup);
                                                    }
                                                });
                                            }
                                        } else {
                                            getLocationMatchAuto(context, allBrands, new CallBackControl.CallBack<MyAuto>() {
                                                @Override
                                                public void onSuccess(MyAuto response) {
                                                    myAutoCallBack.onSuccess(response, brandGroup);
                                                }

                                                @Override
                                                public void onFailed(boolean offLine) {
                                                    myAutoCallBack.onFailed(offLine, brandGroup);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailed(boolean offLine) {
                                        DebugLog.e(TAG, "车辆列表为空！");
                                        myAutoCallBack.onFailed(offLine, brandGroup);
                                    }
                                });
                            } else {
                                getLocationMatchAuto(context, allBrands, new CallBackControl.CallBack<MyAuto>() {
                                    @Override
                                    public void onSuccess(MyAuto response) {
                                        myAutoCallBack.onSuccess(response, brandGroup);
                                    }

                                    @Override
                                    public void onFailed(boolean offLine) {
                                        myAutoCallBack.onFailed(offLine, brandGroup);
                                    }
                                });

                            }
                        }
                    }
                });
    }

    /**
     * 获取匹配的车辆
     *
     * @param context
     * @param shopID
     * @param brandGroups
     * @param myAutos
     * @param myAutoCallBack
     */
    public void getMatchAuto(Context context, String shopID, ArrayList<BrandGroup> brandGroups, ArrayList<MyAuto> myAutos, CallBackControl.AutoCallBack<MyAuto> myAutoCallBack) {
        if (myAutos != null && !myAutos.isEmpty()) {
            ArrayList<Brand> allBrands = AutoHelper.getInstance().getAllBrands(brandGroups);
            ArrayList<MyAuto> matchSwitchAutoList = getMatchAutoList(allBrands, myAutos);
            if (matchSwitchAutoList != null && !matchSwitchAutoList.isEmpty()) {
                myAutoCallBack.onSuccess(matchSwitchAutoList.get(0), brandGroups);
            } else {
                myAutoCallBack.onFailed(false, brandGroups);
            }
        } else {
            myAutoCallBack.onFailed(false, brandGroups);
        }
    }

    /**
     * 获取本地匹配的车
     *
     * @param context
     * @param brands
     * @param myAutoCallBack
     */
    public void getLocationMatchAuto(Context context, ArrayList<Brand> brands, final CallBackControl.CallBack<MyAuto> myAutoCallBack) {

        ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.AUTO_LOCAL_INFO);
        ArrayList<MyAuto> matchLocationAutoList = getMatchAutoList(brands, autoLocal);
        if (matchLocationAutoList != null && !matchLocationAutoList.isEmpty()) {
            myAutoCallBack.onSuccess(matchLocationAutoList.get(0));
        } else {
            myAutoCallBack.onFailed(false);
        }


    }

    /**
     * 筛选符合品牌的车辆
     *
     * @param brands
     * @param myAutoList
     * @return
     */
    public ArrayList<MyAuto> getMatchAutoList(ArrayList<Brand> brands, ArrayList<MyAuto> myAutoList) {
        ArrayList<MyAuto> matchMyAutoList = new ArrayList<>();
        for (int i = 0; i < brands.size(); i++) {
            for (int j = 0; j < myAutoList.size(); j++) {
                if (!TextUtils.isEmpty(myAutoList.get(j).brand)) {
                    if (myAutoList.get(j).brand.equals(brands.get(i).brandName)) {
                        matchMyAutoList.add(myAutoList.get(j));
                    }
                }
            }
        }
        return matchMyAutoList;
    }

    /**
     * 预约修车页面分发
     *
     * @param context
     * @param flagActivity
     * @param myAuto
     */
    public void toActivityInter(final Context context, final int flagActivity, final MyAuto myAuto) {
        if (flagActivity == FLAG_ACTIVITY_APPOINTMENT_HOME) {
            if (UserInfoHelper.getInstance().isLogin(SampleApplicationContext.context)) {
                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.SWITCH_AUTO);
                if (autoLocal != null && !autoLocal.isEmpty()) {
                    //TODO 带选择车辆跳转预约修车店铺
//                    ActivitySwitchAutoInfo.toShopQuoteActivity(context, autoLocal.get(0));
                    ActivitySwitchAutoInfo.toAppiontmentMaintenance(context, autoLocal.get(0));

                } else {
                    AutoInfoControl.getInstance().requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {

                        @Override
                        public void onSuccess(ArrayList<MyAuto> response) {
                            int k = -1;
                            if (response != null && !response.isEmpty()) {
                                k = isCompleteAutoInfo(response, k);
                                DebugLog.i(TAG, "k " + k);
                                if (k == -1) {
                                    if (AutoSPControl.getDialogCount() != 1) {
                                        ActivitySwitchAutoInfo.toDialogActivity(context, response.get(0), AutoInfoContants.FLAG_MODULE_APPOINTMENT);
                                    } else {
                                        ActivitySwitchBase.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME, "", true);
//                                        ActivitySwitchAutoInfo.toAddAuto(context, null, -1);
                                    }
                                } else {
                                    //TODO 首页-带车辆去预约修车店铺
                                    ActivitySwitchAutoInfo.toAppiontmentMaintenance(context, response.get(k));
                                }
                            } else {
                                getAutoLocation(context, flagActivity);
                            }
                        }


                        @Override
                        public void onFailed(boolean offLine) {
                            getAutoLocation(context, flagActivity);
                        }
                    });
                }
            } else {
                getAutoLocation(context, flagActivity);
            }

        } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S) {
            //TODO 4S-带车辆去预约修车店铺
            final Map<String, String> appointmentInfo = (Map<String, String>) AutoSPControl.getAppointmentInfo(context);

            getMatchAuto(context, appointmentInfo.get("shopID"), new CallBackControl.AutoCallBack<MyAuto>() {
                @Override
                public void onSuccess(MyAuto response, ArrayList<BrandGroup> brandGroup) {
                    ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(context, appointmentInfo.get("shopType"), appointmentInfo.get("shopID"), response);
                }

                @Override
                public void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup) {
                    ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(context, appointmentInfo.get("shopType"), appointmentInfo.get("shopID"), null);
                }
            });
            /*if (UserInfoHelper.getInstance().isLogin(SampleApplicationContext.context)) {
                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.SWITCH_AUTO);
                getMatchAuto(context, appointmentInfo.get("shopID"), autoLocal, new CallBackControl.AutoCallBack<MyAuto>() {
                    @Override
                    public void onSuccess(MyAuto response, ArrayList<BrandGroup> brandGroup) {
                        ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(context, appointmentInfo.get("shopType"), appointmentInfo.get("shopID"), response);
                    }

                    @Override
                    public void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup) {
                        requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
                            @Override
                            public void onSuccess(ArrayList<MyAuto> response) {
                                getMatchAuto(context, appointmentInfo.get("shopID"), response, new CallBackControl.AutoCallBack<MyAuto>() {
                                    @Override
                                    public void onSuccess(MyAuto response, ArrayList<BrandGroup> brandGroup) {
                                        ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(context, appointmentInfo.get("shopType"), appointmentInfo.get("shopID"), response);
                                    }

                                    @Override
                                    public void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup) {
                                        ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.AUTO_LOCAL_INFO);
                                        getMatchAuto(context, appointmentInfo.get("shopID"), autoLocal, new CallBackControl.AutoCallBack<MyAuto>() {
                                            @Override
                                            public void onSuccess(MyAuto response, ArrayList<BrandGroup> brandGroup) {
                                                ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(context, appointmentInfo.get("shopType"), appointmentInfo.get("shopID"), response);
                                            }

                                            @Override
                                            public void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup) {
                                                ActivitySwitchBase.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S, appointmentInfo.get("shopID"), true);
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onFailed(boolean offLine) {
                                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.AUTO_LOCAL_INFO);
                                getMatchAuto(context, appointmentInfo.get("shopID"), autoLocal, new CallBackControl.AutoCallBack<MyAuto>() {
                                    @Override
                                    public void onSuccess(MyAuto response, ArrayList<BrandGroup> brandGroup) {
                                        ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(context, appointmentInfo.get("shopType"), appointmentInfo.get("shopID"), response);
                                    }

                                    @Override
                                    public void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup) {
                                        ActivitySwitchBase.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S, appointmentInfo.get("shopID"), true);
                                    }
                                });
                            }
                        });
                    }
                });
            } else {
                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(context, AutoHelper.AUTO_LOCAL_INFO);
                getMatchAuto(context, appointmentInfo.get("shopID"), autoLocal, new CallBackControl.AutoCallBack<MyAuto>() {
                    @Override
                    public void onSuccess(MyAuto response, ArrayList<BrandGroup> brandGroup) {
                        ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(context, appointmentInfo.get("shopType"), appointmentInfo.get("shopID"), response);
                    }

                    @Override
                    public void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup) {
                        ActivitySwitchBase.toChooseBrandActivity(context, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S, appointmentInfo.get("shopID"), true);
                    }
                });
            }*/
        } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_AUTO_DETAIL) {
            //TODO 车辆详情-带车辆去预约修车店铺
            ActivitySwitchAutoInfo.toAppiontmentMaintenance(context, myAuto);
        }
    }

    /**
     * 完善车辆的判断
     *
     * @param response
     * @param k
     * @return
     */
    private int isCompleteAutoInfo(ArrayList<MyAuto> response, int k) {
        for (int i = 0; i < response.size(); i++) {
            if (!TextUtils.isEmpty(response.get(i).brandID) && !TextUtils.isEmpty(response.get(i).brand) && !TextUtils.isEmpty(response.get(i).seriesID) && !TextUtils.isEmpty(response.get(i).series) && !TextUtils.isEmpty(response.get(i).autoModelID) && !TextUtils.isEmpty(response.get(i).autoModel)) {
                k = i;
                break;
            }
        }
        return k;
    }

    /**
     * 获取完善车辆信息
     *
     * @param myAutos
     * @return
     */
    public ArrayList<MyAuto> getIsCompleteAuto(ArrayList<MyAuto> myAutos) {
        ArrayList<MyAuto> newMyAutos = new ArrayList<>();
        for (int i = 0; i < myAutos.size(); i++) {
            if (!TextUtils.isEmpty(myAutos.get(i).brandID) && !TextUtils.isEmpty(myAutos.get(i).brand) && !TextUtils.isEmpty(myAutos.get(i).seriesID) && !TextUtils.isEmpty(myAutos.get(i).series) && !TextUtils.isEmpty(myAutos.get(i).autoModelID) && !TextUtils.isEmpty(myAutos.get(i).autoModel)) {
                newMyAutos.add(myAutos.get(i));
            }
        }
        return newMyAutos;
    }

}
