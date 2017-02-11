package com.hxqc.newenergy.control;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.newenergy.api.NewEnergyApiClient;
import com.hxqc.newenergy.bean.FilterCondition;
import com.hxqc.newenergy.bean.ModelAndSubsidy;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:胡俊杰
 * Date: 2015/12/3
 * FIXME
 * Todo 第三方店铺筛选控制器
 */
public class FilterController {

    private static FilterController ourInstance;
    private NewEnergyApiClient newEnergyApiClient;

    public HashMap< String, String > mFilterMap = new HashMap<>();


    public static FilterController getInstance() {
        if (ourInstance == null) {
            synchronized (FilterController.class) {
                if (ourInstance == null) {
                    ourInstance = new FilterController();
                }
            }
        }
        return ourInstance;
    }


    private FilterController() {
        this.newEnergyApiClient = new NewEnergyApiClient();
    }


    public interface FilterConditionHandler {
        void onGetFilterConditionSucceed(ArrayList< FilterCondition > filterConditions);

        void onGetFilterConditionFailed(boolean offLine);
    }

//    public interface AreaHandler {
//        void onSucceed(ArrayList< EV_FilterAreaCityAdapter.Province > provinces);
//
//        void onFailed(boolean offLine);
//    }


    public interface ModelAndSubsidyHandler {
        void onSucceed(ArrayList< ModelAndSubsidy > subsidies);

        void onFailed(boolean offLine);
    }


    /**
     * 请求地区
     */
//    public void requestArea(Context context, final AreaHandler areaHandler) {
//        newEnergyApiClient.getAreaData(new LoadingAnimResponseHandler(context, true) {
//            @Override
//            public void onSuccess(String response) {
//                ArrayList< EV_FilterAreaCityAdapter.Province > provinces = JSONUtils.fromJson(response, new TypeToken< ArrayList< EV_FilterAreaCityAdapter.Province > >() {
//                });
//                areaHandler.onSucceed(provinces);
//            }
//
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                if (throwable instanceof HttpHostConnectException) {
//                    areaHandler.onFailed(true);
//                } else {
//                    areaHandler.onFailed(false);
//                }
//            }
//        });
//    }


    /** 获取筛选条件数据 **/
    public void requestFilterData(Context context, final FilterConditionHandler filterConditionHandler) {
        newEnergyApiClient.getFilterData(new LoadingAnimResponseHandler(context, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList< FilterCondition> filterConditions = JSONUtils.fromJson(response, new TypeToken< ArrayList< FilterCondition > >(){});
                filterConditionHandler.onGetFilterConditionSucceed(filterConditions);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    filterConditionHandler.onGetFilterConditionFailed(true);
                } else {
                    filterConditionHandler.onGetFilterConditionFailed(false);
                }
            }
        });
    }


    public void requestSubsidyData(Context context, int page, int count, HashMap< String, String > mFilterMap, final ModelAndSubsidyHandler modelAndSubsidyHandler) {
        newEnergyApiClient.getModeAndSubsidyData(page, count, mFilterMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< ModelAndSubsidy > modelAndSubsidies = JSONUtils.fromJson(response, new TypeToken< ArrayList< ModelAndSubsidy > >() {
                });
                modelAndSubsidyHandler.onSucceed(modelAndSubsidies);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    modelAndSubsidyHandler.onFailed(true);
                } else {
                    modelAndSubsidyHandler.onFailed(false);
                }
            }
        });
    }


    /**
     * 添加筛选选择条件
     *
     * @param key
     * @param value
     */
    public void putValue(String key, String value) {
        mFilterMap.put(key, value);
    }


    public void destroy() {
        ourInstance = null;
    }
}
