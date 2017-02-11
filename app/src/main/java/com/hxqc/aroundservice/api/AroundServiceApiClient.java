package com.hxqc.aroundservice.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.aroundservice.model.IllegalQueryRequestData;
import com.hxqc.mall.amap.api.AroundApiClient;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.photolibrary.util.ImageUtils;
import com.hxqc.util.BitmapCompress;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Author : 钟学东
 * @Since : 2016-04-09
 * FIXME
 * Todo
 */
public class AroundServiceApiClient extends AroundApiClient {

    /**
     * 尾号限行城市列表
     *
     * @param handler
     */
    public void weizhangXianxingCity(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Service/weizhangXianxingCity");
        gGetUrl(url, handler);
    }


    /**
     * 限行号码查询
     *
     * @param cityId
     * @param handler
     */
    public void weizhangXianxingQuery(String cityId, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Service/weizhangXianxingQuery");
        RequestParams requestParams = new RequestParams();
        requestParams.put("cityId", cityId);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 违章查询城市
     *
     * @param handler
     */
    public void getIllegalQueryCity(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Service/weizhangCitys");
        gGetUrl(url, handler);
    }

    /**
     * （号牌）种类编号查询
     *
     * @param handler
     */
    public void getSpeciesNumber(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Service/weizhangHpzl");
        gGetUrl(url, handler);
    }

    /**
     * 违章查询-武汉
     *
     * @param illegalQueryRequestData
     * @param handler
     */
    public void getIllegalQueryWH(IllegalQueryRequestData illegalQueryRequestData, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Service/weizhangQueryWH");
        RequestParams requestParams = new RequestParams();
        requestParams.put("city", illegalQueryRequestData.cityCode);
        requestParams.put("hphm", illegalQueryRequestData.hphm);
        requestParams.put("hpzl", illegalQueryRequestData.hpzl);
        requestParams.put("engineno", illegalQueryRequestData.engineno);
        requestParams.put("classno", illegalQueryRequestData.classno);
        requestParams.put("registno", illegalQueryRequestData.registno);
        requestParams.put("handled", illegalQueryRequestData.handled);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 违章查询-全国
     *
     * @param illegalQueryRequestData
     * @param handler
     */
    public void getIllegalQuery(IllegalQueryRequestData illegalQueryRequestData, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Service/weizhangQuery");
        RequestParams requestParams = new RequestParams();
        requestParams.put("city", illegalQueryRequestData.cityCode);
        requestParams.put("hphm", illegalQueryRequestData.hphm);
        requestParams.put("hpzl", illegalQueryRequestData.hpzl);
        requestParams.put("engineno", illegalQueryRequestData.engineno);
        requestParams.put("classno", illegalQueryRequestData.classno);
        requestParams.put("registno", illegalQueryRequestData.registno);
        requestParams.put("handled", illegalQueryRequestData.handled);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 违章处理预估价格
     *
     * @param handler
     * @param choseWZID
     */
    public void getIllegalEstimatePrice(AsyncHttpResponseHandler handler, String plateNumber, String choseWZID) {
        String url = completeUrl("/Estimate/breakRules");
        RequestParams requestParams = new RequestParams();
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("choseWZID", choseWZID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 违章处理预估价格
     *
     * @param handler
     * @param choseWZID
     */
    public void getIllegalEstimatePrice(AsyncHttpResponseHandler handler, String plateNumber, String choseWZID, String city, String hpzl, String engineno, String classno) {
        String url = completeUrl("/Estimate/breakRules");
        RequestParams requestParams = new RequestParams();
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("choseWZID", choseWZID);
        //2016.5.25 新加字段
        requestParams.put("city", city);
        requestParams.put("hpzl", hpzl);
        requestParams.put("engineno", engineno);
        requestParams.put("classno", classno);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 违章订单提交
     *
     * @param handler
     * @param illegalOrderInfo
     * @param choseWZID
     */
    public void postIllegalOrder(AsyncHttpResponseHandler handler, IllegalOrderDetail illegalOrderInfo, String choseWZID) {
        String url = completeUrl("/Order/breakRulesSubmit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("choseWZID", choseWZID);
        requestParams.put("plateNumber", illegalOrderInfo.plateNumber);
        requestParams.put("phone", illegalOrderInfo.phone);
        requestParams.put("name", illegalOrderInfo.username);
        //2016.5.25 新加字段
        requestParams.put("city", illegalOrderInfo.city);
        requestParams.put("hpzl", illegalOrderInfo.hpzl);
        requestParams.put("engineno", illegalOrderInfo.engineno);
        requestParams.put("classno", illegalOrderInfo.classno);
        //2016.11.28 新加字段
        requestParams.put("provinceName", illegalOrderInfo.provinceName);
        requestParams.put("cityName", illegalOrderInfo.cityName);
        requestParams = getDESRequestParams(url, requestParams);

        if (!TextUtils.isEmpty(illegalOrderInfo.drivingLicenseFile1)) {
            File file1 = new File(illegalOrderInfo.drivingLicenseFile1);
            try {
                requestParams.put("drivingLicenseFile1", file1);
                DebugLog.i("Tag", "file 1  " + illegalOrderInfo.drivingLicenseFile1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(illegalOrderInfo.drivingLicenseFile2)) {
            File file2 = new File(illegalOrderInfo.drivingLicenseFile2);
            try {
                requestParams.put("drivingLicenseFile2", file2);
                DebugLog.i("Tag", "file 2  " + illegalOrderInfo.drivingLicenseFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    public String getFilePath(Context context, String sourcePath) throws IOException {
        int degree = BitmapCompress.readPictureDegree(sourcePath);
        DebugLog.i(AutoInfoContants.LOG_J, "degree:" + degree);
        Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(sourcePath, DisplayTools.getScreenWidth(context), DisplayTools.getScreenHeight(context));
        @SuppressWarnings("ConstantConditions")
        String path = context.getExternalCacheDir().getPath() + "/" + System.currentTimeMillis() / 1000 + "" + (int) (Math.random() * 9000 + 1000) + ".jpg";
//        BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 480, 800, path, 90,degree);
        BitmapCompress.compressImageToSpecifySizeAndSaveFile(sourcePath, path, 90, degree);
        bitmap.recycle();
        return path;
    }

    /**
     * 违章订单修改
     *
     * @param orderID
     * @param drivingLicenseFile1
     * @param drivingLicenseFile2
     * @param handler
     */
    public void putIllegalOrder(String orderID, String drivingLicenseFile1, String drivingLicenseFile2, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/breakRulesEdit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams = getDESRequestParams(url, requestParams);
        if (!TextUtils.isEmpty(drivingLicenseFile1)) {
            File file1 = new File(drivingLicenseFile1);
            try {
                requestParams.put("drivingLicenseFile1", file1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(drivingLicenseFile2)) {
            File file2 = new File(drivingLicenseFile2);
            try {
                requestParams.put("drivingLicenseFile2", file2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 违章订单详情
     *
     * @param orderID
     * @param handler
     */
    public void getIllegalOrderDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/breakRulesDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

   /* *//**
     * 付款方式
     *
     * @param handler
     *//*
    public void getPayment(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/listPayment");
        gGetUrl(url, handler);
    }

    *//**
     * 付款
     *
     * @param orderID
     * @param paymentID
     * @param money
     * @param handler
     *//*
    public void postPay(String orderID, String paymentID, String money, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/pay");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("money", money);
        gPostUrl(url, requestParams, handler);
    }*/

    /**
     * 订单取消 订单状态为10 或者0可以取消
     *
     * @param orderID
     * @param reason
     * @param handler
     */
    public void deleteIllegalOrder(String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/breakRulesCancel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
        gDeleteUrl(url, requestParams, handler);
    }

    /**
     * 年检服务价格
     */
    public void getAnnualnspection(String registerDate, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Estimate/annualnspection");
        RequestParams requestParams = new RequestParams();
        requestParams.put("registerDate", registerDate);
        requestParams.put("area", "武汉");
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 年检订单提交
     */
    public void postAnnualnspectionSubmit(Context context, String plateNumber, String registerDate, String name, String phone,
                                          String province, String city, String district, String address, String path1, String path2, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/annualnspectionSubmit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("registerDate", registerDate);
        requestParams.put("name", name);
        requestParams.put("phone", phone);
        requestParams.put("province", province);
        requestParams.put("city", city);
        requestParams.put("district", district);
        requestParams.put("address", address);

        requestParams = getDESRequestParams(url, requestParams);
        if (!TextUtils.isEmpty(path1)) {
            try {
                requestParams.put("drivingLicenseFile1", new File(getFilePath(context, path1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(path2)) {
            try {
                requestParams.put("drivingLicenseFile2", new File(getFilePath(context, path2)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 年检订单详情
     */
    public void getAnnualnspectionDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/annualnspectionDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 年检订单修改
     */
    public void postAnnualnspectionEdit(String orderID, String path1, String path2, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/annualnspectionEdit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);

        requestParams = getDESRequestParams(url, requestParams);
        if (!TextUtils.isEmpty(path1)) {
            try {
                File file1 = new File(path1);
                requestParams.put("drivingLicenseFile1", file1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(path2)) {
            try {
                File file2 = new File(path2);
                requestParams.put("drivingLicenseFile2", file2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 年检订单取消 订单状态为10 或者0可以取消
     */
    public void deleteAnnualnspectionCancel(String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/annualnspectionCancel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
        gDeleteUrl(url, requestParams, handler);
    }

    /**
     * 驾驶证换证价格
     */
    public void getLicence(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Estimate/licence");
        gGetUrl(url, handler);
    }

    /**
     * 驾驶证换证提交
     */
    public void postLicenceSubmit(Context context, String name, String phone, String province, String city, String district, String address,
                                  String path1, String path2, String path3, String path4, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/licenceSubmit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("name", name);
        requestParams.put("phone", phone);
        requestParams.put("province", province);
        requestParams.put("city", city);
        requestParams.put("district", district);
        requestParams.put("address", address);

        requestParams = getDESRequestParams(url, requestParams);
        if (!TextUtils.isEmpty(path1)) {
            try {
                requestParams.put("drivingLicenseFile1", new File(getFilePath(context, path1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(path2)) {
            try {
                requestParams.put("drivingLicenseFile2", new File(getFilePath(context, path2)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(path3)) {
            try {
                requestParams.put("IDCardFile1", new File(getFilePath(context, path3)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(path4)) {
            try {
                requestParams.put("IDCardFile2", new File(getFilePath(context, path4)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 驾驶证换证订单详情
     */
    public void getLicenceDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/licenceDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 驾驶证换证修改
     */
    public void postLicenceEdit(String orderID, String path1, String path2, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/licenceEdit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);

        requestParams = getDESRequestParams(url, requestParams);
        if (!TextUtils.isEmpty(path1)) {
            try {
                File file1 = new File(path1);
                requestParams.put("drivingLicenseFile1", file1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(path2)) {
            try {
                File file2 = new File(path2);
                requestParams.put("drivingLicenseFile2", file2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 驾驶证换证修改
     */
    public void postLicenceEdit(String orderID, String path1, String path2, String IDCardFile1, String IDCardFile2, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/licenceEdit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);

        requestParams = getDESRequestParams(url, requestParams);
        if (!TextUtils.isEmpty(path1)) {
            try {
                File file1 = new File(path1);
                requestParams.put("drivingLicenseFile1", file1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(path2)) {
            try {
                File file2 = new File(path2);
                requestParams.put("drivingLicenseFile2", file2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(IDCardFile1)) {
            try {
                File file3 = new File(IDCardFile1);
                requestParams.put("IDCardFile1", file3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(IDCardFile2)) {
            try {
                File file4 = new File(IDCardFile2);
                requestParams.put("IDCardFile2", file4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 驾驶证换证订单取消 订单状态为10 或者0可以取消
     */
    public void deleteLicenceCancel(String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/licenceCancel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
        gDeleteUrl(url, requestParams, handler);
    }

    /**
     * 违章查询图片
     *
     * @param wzxh
     * @param handler
     */
    public void weizhangQueryPhoto(String wzxh, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Service/weizhangQueryPhoto");
        RequestParams requestParams = new RequestParams();
        requestParams.put("wzxh", wzxh);
        gGetUrl(url, requestParams, handler);
    }

}
