package com.hxqc.mall.usedcar.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.paymethodlibrary.wechat.MD5;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.ImageUtils;
import com.hxqc.util.BitmapCompress;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

/**
 * 说明:接口访问
 *
 * @author: 吕飞
 * @since: 2015-07-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class UsedCarApiClient extends BaseApiClient {
    //    public static final String USED_CAR_HOST = "http://10.0.14.203:86";
//    public static final String CRM_HOST = USED_CAR_HOST;
//    public static final String API_HOST = HOST;
//    public static final String USED_CAR_API_HOST = USED_CAR_HOST + "/interface";
//    public static final String CRM_API_HOST = CRM_HOST + "/interface";
    public static final String SELL_CAR = "*31)_LoY5%2Ps^488(*l1*9gU3l!@d&0";
    public static final String BUY_CAR = "&MiTb(3O2P145%sk2@@3jhs^1kmzz!8$";
    public static final String ONSALE_CAR_OPERATION = "#$11&nYYesOiIl2*64^2msWq2Re3&AnV";
    public static final String COMPLAIN_CAR = "(UjU29$12o(*DmIw53kl(67#@2k26&^G";
    public static final String SUBSCRIBE = "UM6Oslw%3)(11oEBfdCyz^4(*2KaOw90";
    public static final String ATTENTION = "&kSj2#iu*oO1%slM3sp@lla%aQdBbndD";
    public static final String APPEAL = "lHtM3)2I%6xc#2mHe1*6kOqpLSny3*bs";
    public static final String APPLYOFFSALE = "zOwrmD1#wl5%192P9!z4Gly1q&l6a@cs";
    public static final String PLATFORMSELL = "e48b@574aHeO7e&1cE134c8B7*e0624!";
    public static final String CENTER = "hfghe54yhfua#tr$2;@sdksdfiksl;d3";
    public static final String HOST_WEB = "http://usedcar.hxqc.com";
    public static final int VC_PLATFORM = 10;
    public static final int VC_APPOINTMENT = 20;

    public UsedCarApiClient() {
        super();
//        ApiUtil.isDebug=false;
    }

    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getUsedCarURL(control);
    }


    /**
     * 获取买车搜索结果
     */
    public void getBuyCar(HashMap<Integer, String[]> hashMap, String city, String keyword, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/buyCarlist");
        RequestParams requestParams = new RequestParams();
        for (Object o : hashMap.entrySet()) {
            HashMap.Entry entry = (HashMap.Entry) o;
            Integer i = (Integer) entry.getKey();
            if (i.equals(-1)) {
                requestParams.put("order_key", hashMap.get(-1)[1]);
                requestParams.put("order_value", hashMap.get(-1)[0]);

            } else if (i.equals(0)) {
                requestParams.put("price", hashMap.get(0)[0]);

            } else if (i.equals(1)) {
                requestParams.put("brand", hashMap.get(1)[0]);

            } else if (i.equals(2)) {
                requestParams.put("age_limit", hashMap.get(2)[0]);

            } else if (i.equals(3)) {
                requestParams.put("level", hashMap.get(3)[0]);

            } else if (i.equals(4)) {
                requestParams.put("publish_from", hashMap.get(4)[0]);

            } else if (i.equals(5)) {
                requestParams.put("displacement", hashMap.get(5)[0]);

            } else if (i.equals(6)) {
                requestParams.put("gearbox", hashMap.get(6)[0]);

            } else if (i.equals(7)) {
                requestParams.put("mileage", hashMap.get(7)[0]);

            }

        }
        requestParams.put("page", page);
        requestParams.put("page_size", 15);
        requestParams.put("city", city);
        requestParams.put("keyword", keyword);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取筛选车辆数目
     */
    public void getFilterCount(String price, String brand, String age_limit, String level, String publish_from, String displacement, String gearbox, String mileage,
                               String keyword, String city, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/buyCarlist");
        RequestParams requestParams = new RequestParams();
        requestParams.put("price", price);
        requestParams.put("brand", brand);
        requestParams.put("age_limit", age_limit);
        requestParams.put("level", level);
        requestParams.put("publish_from", publish_from);
        requestParams.put("displacement", displacement);
        requestParams.put("gearbox", gearbox);
        requestParams.put("mileage", mileage);
        requestParams.put("keyword", keyword);
        requestParams.put("city", city);
        requestParams.put("is_count", 1);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 根据手机号获取卖家的车辆列表
     */
    public void getSellerCarList(String sellerMobile, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getSellerCarList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", sellerMobile);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 更新价格
     */
    public void getNewPrice(String qaId, String instalmentId, String carSourceNo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getNewPrice");
        RequestParams requestParams = new RequestParams();
        requestParams.put("qa_id", qaId);
        requestParams.put("instalment_id", instalmentId);
        requestParams.put("car_source_no", carSourceNo);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取品牌列表
     */
    public void getBrands(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getGroupBrand");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取意向品牌列表
     */
    public void getIntentBrands(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getIntentBrand");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取估价品牌列表
     */
    public void getValuationBrands(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getValuationBrand");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取筛选数据
     */
    public void getFilter(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getSearchParam");
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageParam", "buyCarPage");
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取城市列表
     */
    public void getCities(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getHotCityList");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取我的卖车信息列表
     */
    public void getSellCarList(String phoneNumber, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getMySellCarInfo");
        RequestParams requestParams = new RequestParams();
        addCheckInfo(requestParams, phoneNumber, "CENTER", CENTER);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取热门搜索
     */
    public void getHotSearch(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getSearch");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取车型车系
     */
    public void getSeriesModel(String brandId, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getSerieModel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand_id", brandId);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取估价车型车系
     */
    public void getValuationSeriesModel(String brandId, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getValuationSerieModel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand_id", brandId);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取意向车型车系
     */
    public void getIntentSeriesModel(String brand, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getIntentSerieModel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand", brand);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取举报原因
     */
    public void getReportReason(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getTemplate");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取下架原因
     */
    public void getOffsaleReason(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/offsaleReasonTemplete");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取商品详情
     */
    public void getProductDetail(String phoneNumber, String carSourceNo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/queryItemDetailByCarSourceNo");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        requestParams.put("phone_no", phoneNumber);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 提交举报
     */
    public void sendReport(String code, String comments, String phoneNumber, String carSourceNo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/saveComplain");
        RequestParams requestParams = new RequestParams();
        requestParams.put("code", code);
        requestParams.put("comments", comments);
        requestParams.put("car_source_id", carSourceNo);
        addCheckInfo(requestParams, phoneNumber, "COMPLAIN_CAR", COMPLAIN_CAR);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 旧车置换
     */
    public void exchange(String name, String gender, String mobile, String sellBrand, String sellSeries, String province,
                         String provinceId, String city, String cityId, String firstOnCard, String mileage, String sellRemark,
                         String intentBrand, String intentSeries, String intentModel, String intentRemark, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/exchange");
        RequestParams requestParams = new RequestParams();
        requestParams.put("name", name);
        requestParams.put("gender", gender);
        requestParams.put("mobile", mobile);
        requestParams.put("sell_brand", sellBrand);
        requestParams.put("sell_series", sellSeries);
        requestParams.put("province", province);
        requestParams.put("province_id", provinceId);
        requestParams.put("city", city);
        requestParams.put("city_id", cityId);
        if (!TextUtils.isEmpty(firstOnCard)) {
            requestParams.put("first_on_card", firstOnCard);
        }
        if (!TextUtils.isEmpty(mileage)) {
            requestParams.put("mileage", mileage);
        }
        if (!TextUtils.isEmpty(sellRemark)) {
            requestParams.put("sell_remark", sellRemark);
        }
        requestParams.put("intent_brand", intentBrand);
        requestParams.put("intent_series", intentSeries);
        requestParams.put("intent_model", intentModel);
        if (!TextUtils.isEmpty(intentRemark)) {
            requestParams.put("intent_remark", intentRemark);
        }
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 买车预约
     */
    public void appointment(String phoneNumber, String captcha, String time, String carSourceNo, String instalment_id, String qa_id, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/buyCar");
        RequestParams requestParams = new RequestParams();
        requestParams.put("captcha", captcha);
        requestParams.put("time", time);
        requestParams.put("carSourceId", carSourceNo);
        if (!TextUtils.isEmpty(instalment_id))
            requestParams.put("instalment_id", instalment_id);
        if (!TextUtils.isEmpty(qa_id))
            requestParams.put("qa_id", qa_id);
        addCheckInfo(requestParams, phoneNumber, "BUY_CAR", BUY_CAR);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 卖车详情
     */
    public void getSellCarDetail(String phoneNumber, String carSourceNo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/singleSellCarInfo");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        addCheckInfo(requestParams, phoneNumber, "CENTER", CENTER);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 根据车型拿价格
     */
    public void getInfoByModel(String modelId, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getInfoByModel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("model_id", modelId);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 修改获取详情
     */
    public void getSellDetail(String phoneNumber, String carSourceNo, AsyncHttpResponseHandler handler) {
//        String url = "http://10.0.12.186:94/interface/getDetail";
        String url = completeUrl("/getDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        addCheckInfo(requestParams, phoneNumber, "CENTER", CENTER);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 清空订阅
     */
    public void clearSubscription(String phoneNumber, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/deleteAubscibe");
        RequestParams requestParams = new RequestParams();
        addCheckInfo(requestParams, phoneNumber, "SUBSCRIBE", SUBSCRIBE);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 上架
     */
    public void onSale(String phoneNumber, String carSourceNo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/onsale");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        addCheckInfo(requestParams, phoneNumber, "ONSALE_CAR_OPERATION", ONSALE_CAR_OPERATION);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 下架
     */
    public void offSale(String phoneNumber, String carSourceNo, String reasonId, String reasonContents, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/offsale");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        requestParams.put("reason", reasonId);
        requestParams.put("reasoncontents", reasonContents);
        addCheckInfo(requestParams, phoneNumber, "ONSALE_CAR_OPERATION", ONSALE_CAR_OPERATION);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 申请下架
     */
    public void applyOffSale(String phoneNumber, String carSourceNo, String reasonId, String reasonContents, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/applyoffsale");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        requestParams.put("reason", reasonId);
        requestParams.put("reasoncontents", reasonContents);
        addCheckInfo(requestParams, phoneNumber, "APPLYOFFSALE", APPLYOFFSALE);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 已售
     */
    public void sold(String phoneNumber, String carSourceNo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/sold");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        addCheckInfo(requestParams, phoneNumber, "ONSALE_CAR_OPERATION", ONSALE_CAR_OPERATION);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 删除
     */
    public void delete(String phoneNumber, String carSourceNo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/deleteitem");
        RequestParams requestParams = new RequestParams();
        requestParams.put("car_source_no", carSourceNo);
        addCheckInfo(requestParams, phoneNumber, "ONSALE_CAR_OPERATION", ONSALE_CAR_OPERATION);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 平台代卖
     */
    public void platformSell(String phoneNumber, String brandId, String seriesId, String province, String city, String other, String name, String captcha, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/platformSell");
//        String url ="http://10.0.14.186:94/v1/interface/platformSell";
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand", brandId);
        requestParams.put("serie", seriesId);
        requestParams.put("province", province);
        requestParams.put("city", city);
        requestParams.put("other", other);
        requestParams.put("name", name);
        requestParams.put("captcha", captcha);
        addCheckInfo(requestParams, phoneNumber, "PLATFORMSELL", PLATFORMSELL);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 获取省市
     */
    public void getProvinceCity(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getPCR");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取验证码
     *
     * @param type 10平台帮卖 20预约看车
     */
    public void getVoiceCaptcha(String phone, int type, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/captcha");
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", phone);
        requestParams.put("useType", type);
        requestParams.put("sendType", 20);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取颜色
     */
    public void getChoose(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/getChoose");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 提交卖车
     */
    public void sendSellCar(Context context,
                            String level,
                            String gearbox,
                            String displacement,
                            String newCarPrice,
                            String carSourceNo,
                            String mobile,//登录手机号
                            String brandId,
                            String seriesId,
                            String modelId,
                            String addBrand,
                            String addSeries,
                            String addModel,
                            String colorCode,
                            String province,
                            String city,
                            String range,
                            String firstOnCard,
                            String estimatePrice,
                            String contacts,
                            String phoneNumber,
                            String owners,
                            ArrayList<ImageItem> imageItems,
                            String delete,
                            String inspection_date,//年检有效期
                            String sali_date,//交强险到期时间
                            String location_province,//看车省
                            String location_city,//看车市
                            String location_region,//看车区
                            String warranty_date,//质保到期时间
                            String insurance_date,//商业险
                            String car_license_no,//车牌号
                            String license,//行驶证
                            String license1,//登记证
                            String license2,//购车发票
                            int cover,//封面的position
                            AsyncHttpResponseHandler handler) throws IOException {
        String url = completeUrl("/sellCar");
        RequestParams requestParams = new RequestParams();
        if (imageItems != null && imageItems.size() > 0) {
            int size = imageItems.size();
            for (int i = 0; i < size; i++) {
                if (!TextUtils.isEmpty(imageItems.get(i).sourcePath) && !imageItems.get(i).sourcePath.substring(0, 4).equals("http")) {
                    imageItems.get(i).sourcePath = getFilePath(imageItems.get(i).sourcePath, context);
                    DebugLog.i("TAG", i + "");
                }
                if (i == cover) {
                    String[] str = imageItems.get(i).sourcePath.split("/");
                    requestParams.put("cover", str[str.length - 1]);
                }
            }
        }
        requestParams.put("level", level);
        requestParams.put("gearbox", gearbox);
        requestParams.put("displacement", displacement);
        requestParams.put("new_car_price", newCarPrice);
        requestParams.put("car_source_no", carSourceNo);
        requestParams.put("mobile", mobile);
        requestParams.put("brand", brandId);
        requestParams.put("serie", seriesId);
        requestParams.put("model", modelId);
        requestParams.put("addbrand", addBrand);
        requestParams.put("addserie", addSeries);
        requestParams.put("addmodel", addModel);
        requestParams.put("car_color", colorCode);
        requestParams.put("province", province);
        requestParams.put("city", city);
        requestParams.put("car_mileage", range);
        requestParams.put("first_on_card", firstOnCard);
        requestParams.put("estimate_price", estimatePrice);
        requestParams.put("contacts", contacts);
        requestParams.put("phone_num", phoneNumber);
        requestParams.put("owners", owners);
        requestParams.put("delete", delete);
        requestParams.put("inspection_date", inspection_date);
        requestParams.put("sali_date", sali_date);
        requestParams.put("location_province", location_province);
        requestParams.put("location_city", location_city);
        requestParams.put("location_region", location_region);
        requestParams.put("warranty_date", warranty_date);
        requestParams.put("insurance_date", insurance_date);
        requestParams.put("car_license_no", car_license_no);
        addCheckInfo(requestParams, mobile, "SELL_CAR", SELL_CAR);
        requestParams = getDESRequestParams(url, requestParams);
        if (imageItems != null && imageItems.size() > 0) {
            int size = imageItems.size();
            for (int i = 0; i < size; i++) {
                if (!TextUtils.isEmpty(imageItems.get(i).sourcePath) && !imageItems.get(i).sourcePath.startsWith("http")) {
                    requestParams.put("image" + (i + 1), new File(imageItems.get(i).sourcePath));
                }
            }
        }
        if (!TextUtils.isEmpty(license)) {
            requestParams.put("license", new File(getFilePath(license, context)));
        }
        if (!TextUtils.isEmpty(license1)) {
            requestParams.put("license1", new File(getFilePath(license1, context)));
        }
        if (!TextUtils.isEmpty(license2)) {
            requestParams.put("license2", new File(getFilePath(license2, context)));
        }
        client.post(url, requestParams, handler);
    }

    //    private String getFilePath(String sourcePath, Context context) throws IOException {
//        Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(sourcePath, DisplayTools.getScreenWidth(context), DisplayTools.getScreenHeight(context));
//        @SuppressWarnings("ConstantConditions")
//        String path = context.getExternalCacheDir().getPath() + "/" + System.currentTimeMillis() / 1000 + "" + (int) (Math.random() * 9000 + 1000) + ".jpg";
//        BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 1340, 760, path, 96);
//        bitmap.recycle();
//        return path;
//    }
    public String getFilePath(String sourcePath, Context context) throws IOException {
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

    private String getSign(String mobile, String operation, String timestamp, String key) {
        Comparator<Object> comparator = Collator.getInstance(Locale.ENGLISH);
        ArrayList<String> signs = new ArrayList<>();
        signs.add(mobile);
        signs.add(operation);
        signs.add(timestamp);
        Collections.sort(signs, comparator);
        String sign = signs.get(0) + key + signs.get(1) + key + signs.get(2);
        return MD5.md5Password(sign);
    }

    private void addCheckInfo(RequestParams requestParams, String mobile, String operation, String key) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        requestParams.put("mobile", mobile);
        requestParams.put("timestamp", timestamp);
        requestParams.put("sign", getSign(mobile, operation, timestamp, key));
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    /**
     * 估价
     */
    public void getValuation(String brandId, String seriesId, String modelId, String cityName, String firstOnCard, String mileage, String valuationModelId, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/carEvaluate");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand_id", brandId);
        requestParams.put("serie_id", seriesId);
        requestParams.put("model_id", modelId);
        requestParams.put("city_code", cityName);
        requestParams.put("first_on_card", firstOnCard);
        requestParams.put("mileage", mileage);
        requestParams.put("valuation_model_id", valuationModelId);
        gGetUrl(url, requestParams, handler);
    }
}
