package com.hxqc.aroundservice.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.api.AroundServiceApiClient;
import com.hxqc.aroundservice.model.EstimatePrice;
import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.aroundservice.model.IllegalQueryProvinceAndCity;
import com.hxqc.aroundservice.model.IllegalQueryRequestData;
import com.hxqc.aroundservice.model.IllegalQueryResult;
import com.hxqc.aroundservice.model.SpeciesNumber;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * Des: 违章查询控制器
 * FIXME
 * Todo
 */
public class IllegalQueryControl {

    private static final String TAG = AutoInfoContants.LOG_J;
    private static IllegalQueryControl ourInstance;
    private AroundServiceApiClient imIllegalQueryClient;

    private IllegalQueryControl() {
        imIllegalQueryClient = new AroundServiceApiClient();
    }

    public static IllegalQueryControl getInstance() {
        if (ourInstance == null) {
            synchronized (IllegalQueryControl.class) {
                if (ourInstance == null) {
                    ourInstance = new IllegalQueryControl();
                }
            }
        }
        return ourInstance;

    }

    /**
     * 请求违章查询城市
     *
     * @param context
     * @param callBack
     */
    public void requestIllegalQueryCity(Context context, @NonNull final CallBackControl.CallBack<IllegalQueryProvinceAndCity> callBack) {
        imIllegalQueryClient.getIllegalQueryCity(new LoadingAnimResponseHandler(context, true, true, "") {
            @Override
            public void onSuccess(String response) {
                IllegalQueryProvinceAndCity mIllegalQueryProvinceAndCity = JSONUtils.fromJson(response, new TypeToken<IllegalQueryProvinceAndCity>() {
                });
                callBack.onSuccess(mIllegalQueryProvinceAndCity);
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
     * 消除
     */
    public void killInstance() {
        if (imIllegalQueryClient != null) {
            imIllegalQueryClient = null;
        }
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    /**
     * 请求（号牌）种类编号
     *
     * @param context
     * @param callBack
     */
    public void requestSpeciesNumber(Context context, @NonNull final CallBackControl.CallBack<SpeciesNumber> callBack) {
        imIllegalQueryClient.getSpeciesNumber(new LoadingAnimResponseHandler(context, true, false, "") {
            @Override
            public void onSuccess(String response) {
                SpeciesNumber mSpeciesNumber = JSONUtils.fromJson(response, new TypeToken<SpeciesNumber>() {
                });
                callBack.onSuccess(mSpeciesNumber);
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
     * 违章查询-武汉
     *
     * @param context
     * @param illegalQueryRequestData
     * @param callBack
     */
    public void requestIllegalQueryWH(Context context, IllegalQueryRequestData illegalQueryRequestData, @NonNull final CallBackControl.CallBack<IllegalQueryResult> callBack) {
        imIllegalQueryClient.getIllegalQueryWH(illegalQueryRequestData, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                IllegalQueryResult mIllegalQueryResult = JSONUtils.fromJson(response, new TypeToken<IllegalQueryResult>() {
                });
                callBack.onSuccess(mIllegalQueryResult);
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
     * 违章查询-全国
     *
     * @param context
     * @param illegalQueryRequestData
     * @param callBack
     */
    public void requestIllegalQuery(Context context, IllegalQueryRequestData illegalQueryRequestData, @NonNull final CallBackControl.CallBack<IllegalQueryResult> callBack) {
        imIllegalQueryClient.getIllegalQuery(illegalQueryRequestData, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                IllegalQueryResult mIllegalQueryResult = JSONUtils.fromJson(response, new TypeToken<IllegalQueryResult>() {
                });
                callBack.onSuccess(mIllegalQueryResult);
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
     * 违章处理预估价格
     *
     * @param context
     * @param callBack
     * @param plateNumber
     * @param choseWZID
     */
    public void getIllegalEstimatePrice(Context context, @NonNull final CallBackControl.CallBack<EstimatePrice> callBack, String plateNumber, String choseWZID) {
        imIllegalQueryClient.getIllegalEstimatePrice(new LoadingAnimResponseHandler(context, true, false, "") {
            @Override
            public void onSuccess(String response) {
                EstimatePrice estimatePrice = JSONUtils.fromJson(response, new TypeToken<EstimatePrice>() {
                });
                callBack.onSuccess(estimatePrice);
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
        }, plateNumber, choseWZID);
    }

    /**
     * 违章处理预估价格
     *
     * @param context
     * @param callBack
     * @param plateNumber
     * @param choseWZID
     */
    public void getIllegalEstimatePrice(Context context, @NonNull final CallBackControl.CallBack<EstimatePrice> callBack, String plateNumber, String choseWZID, String city, String hpzl, String engineno, String classno) {
        imIllegalQueryClient.getIllegalEstimatePrice(new LoadingAnimResponseHandler(context, true, false, "") {
            @Override
            public void onSuccess(String response) {
                EstimatePrice estimatePrice = JSONUtils.fromJson(response, new TypeToken<EstimatePrice>() {
                });
                callBack.onSuccess(estimatePrice);
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
        }, plateNumber, choseWZID, city, hpzl, engineno, classno);
    }

    /**
     * 违章订单提交
     *
     * @param context
     * @param callBack
     * @param illegalOrderInfo
     * @param choseWZID
     */
    public void postIllegalOrder(Context context, @NonNull final CallBackControl.CallBack<String> callBack, IllegalOrderDetail illegalOrderInfo, String choseWZID) {
        try {
            illegalOrderInfo.drivingLicenseFile1 = imIllegalQueryClient.getFilePath(context, illegalOrderInfo.drivingLicenseFile1);
            illegalOrderInfo.drivingLicenseFile2 = imIllegalQueryClient.getFilePath(context, illegalOrderInfo.drivingLicenseFile2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imIllegalQueryClient.postIllegalOrder(new LoadingAnimResponseHandler(context, true, false, "") {
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
        }, illegalOrderInfo, choseWZID);
    }

    /**
     * 违章订单修改
     *
     * @param context
     * @param orderID
     * @param drivingLicenseFile1
     * @param drivingLicenseFile2
     * @param callBack
     */
    public void editIllegalOrder(Context context, String orderID, String drivingLicenseFile1, String drivingLicenseFile2, @NonNull final CallBackControl.CallBack<String> callBack) {
        imIllegalQueryClient.putIllegalOrder(orderID, drivingLicenseFile1, drivingLicenseFile2, new LoadingAnimResponseHandler(context, true, false, "") {
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
     * 违章订单详情
     *
     * @param context
     * @param orderID
     * @param callBack
     */
    public void getIllegalOrderDetail(Context context, String orderID, @NonNull final CallBackControl.CallBack<IllegalOrderDetail> callBack) {
        imIllegalQueryClient.getIllegalOrderDetail(orderID, new LoadingAnimResponseHandler(context, true, false, "") {
            @Override
            public void onSuccess(String response) {
                IllegalOrderDetail illegalOrderDetail = JSONUtils.fromJson(response, new TypeToken<IllegalOrderDetail>() {
                });
                callBack.onSuccess(illegalOrderDetail);
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
     * 取消违章订单
     *
     * @param context
     * @param orderID
     * @param reason
     * @param callBack
     */
    public void cancelIllegalOrder(Context context, String orderID, String reason, @NonNull final CallBackControl.CallBack<String> callBack) {
        imIllegalQueryClient.deleteIllegalOrder(orderID, reason, new LoadingAnimResponseHandler(context, true, false) {
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
     * 违章查询图片
     *
     * @param context
     * @param wzxh
     * @param callBack
     */
    public void requestWeiZhangPhoto(Context context, String wzxh, @NonNull final CallBackControl.CallBack<ImageModel> callBack) {
        imIllegalQueryClient.weizhangQueryPhoto(wzxh, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ImageModel imageModel = JSONUtils.fromJson(response, new TypeToken<ImageModel>() {
                });
                callBack.onSuccess(imageModel);
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
}
