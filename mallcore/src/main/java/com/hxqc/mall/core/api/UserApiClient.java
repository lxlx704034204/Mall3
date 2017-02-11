package com.hxqc.mall.core.api;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.hxqc.mall.core.model.ImageItem;
import com.hxqc.mall.core.model.User;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


/**
 * Author:胡俊杰
 * Date: 2015/9/25
 * FIXME
 * Todo 会员中心接口
 */
public class UserApiClient extends BaseApiClient {
    @Override
    protected String completeUrl(String control) {
//        return HOST + "/Account/" + API_VERSION + control;
        return ApiUtil.getAccountURL(control);
    }

    /**
     * 个人界面辅助信息
     */
    public void assistantInfo(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/assistantInfo");
        gGetUrl(url, handler);
    }

    /**
     * 品牌活动
     */
    public String brandActivities() {
        DebugLog.i(TAG, completeUrl("/Html/appBrandActive"));
        return completeUrl("/Html/appBrandActive");
    }

    /**
     * 积分规则
     */
    public String scoreRule() {
        DebugLog.i(TAG, completeUrl("/Html/Memberbook31"));
        return completeUrl("/Html/Memberbook31");
    }

    /**
     * 会员规则
     */
    public String accountRule() {
        DebugLog.i(TAG, completeUrl("/Html/Memberbook.html"));
        return completeUrl("/Html/Memberbook.html");
    }

    /**
     * 个人中心
     */
    public void getUserInfo(String token, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 上传头像
     */
    public void updateAvatar(String token, String avatar, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/avatar");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams = getDESRequestParams(url, requestParams);
        if (!TextUtils.isEmpty(avatar)) {
            File file = new File(avatar);
            try {
                requestParams.put("avatar", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
//        gPostUrl(url, requestParams, handler);
        client.post(url, requestParams, handler);
    }

    /**
     * 设置个人资料
     */
    public void putUser(User user, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users");
        RequestParams requestParams = new RequestParams();
        requestParams.put("nickName", user.nickName);
        requestParams.put("fullname", user.fullname);
        requestParams.put("birthDay", user.birthday);
        requestParams.put("gender", user.gender);
        requestParams.put("province", user.province);
        requestParams.put("provinceID", user.provinceID);
        requestParams.put("city", user.city);
        requestParams.put("cityID", user.cityID);
        requestParams.put("district", user.district);
        requestParams.put("districtID", user.districtID);
        requestParams.put("detailedAddress", user.detailedAddress);
        gPutUrl(url, requestParams, handler);
    }


    /**
     * 获取我的钱包的数据
     */
    public void getWalletData(AsyncHttpResponseHandler handler) {
        gGetUrl(completeUrl("/wallet"), new RequestParams(), handler);
    }

    /**
     * 我的优惠券数据
     *
     * @param page  默认1
     * @param count 默认15
     */
    @Deprecated
    public void getCouponData(int page, int count, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(completeUrl("/Coupon"), requestParams, handler);
    }

    /**
     * 我的优惠券数据
     *
     * @param page  默认1
     * @param count 默认15
     */
    public void getCouponData(int page, int count, String myAutoID, int kindCode, int couponType, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        requestParams.put("count", count);
        requestParams.put("myAutoID", myAutoID);
        if (kindCode != 0)
            requestParams.put("kindCode", kindCode);
        requestParams.put("couponType", couponType);
        gGetUrl(completeUrl("/Coupon"), requestParams, handler);
    }


    /**
     * 余额账单
     *
     * @param page 按页请求
     * @param lm   最近一个月份
     */
    public void getBalanceBill(String page, String lm, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        if (!TextUtils.isEmpty(lm))
            requestParams.put("lm", lm);
        gGetUrl(completeUrl("/Bill/balanceBill"), requestParams, handler);
    }

    /**
     * 余额账单
     *
     * @param page 按页请求
     * @param lm   最近一个月份
     */
    public void getScoreBill(String myAutoID, String page, String lm, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        requestParams.put("myAutoID", myAutoID);
        if (!TextUtils.isEmpty(lm))
            requestParams.put("lm", lm);
        gGetUrl(completeUrl("/Bill/scoreBill"), requestParams, handler);
    }

    /**
     * 获取账单详情
     *
     * @param currencyType 账单类型 100余额账单 200积分账单
     */
    public void getBillDetail(String currencyType, String billID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("currencyType", currencyType);
        requestParams.put("billID", billID);
        if (!TextUtils.isEmpty(currencyType) && currencyType.equals("100"))
            gGetUrl(completeUrl("/Bill/billDetail"), requestParams, handler);
        else gGetUrl(completeUrl("/Bill/scoreDetail"), requestParams, handler);

    }

//    /**
//     * 增加车辆信息
//     *
//     * @param VIN
//     *         （可选）
//     * @param drivingDistance
//     * @param brand
//     * @param brandID
//     * @param series
//     * @param seriesID
//     * @param model
//     * @param modelID
//     * @param plateNumber
//     * @param ownPhone（可选）
//     * @param ownName（可选）
//     * @param handler
//     */
//    public void addMyAuto(String VIN, String drivingDistance, String brand, String brandID, String series
//            , String seriesID, String model, String modelID, String plateNumber,
//                          String ownPhone, String ownName, AsyncHttpResponseHandler handler) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("VIN", VIN);
//        requestParams.put("drivingDistance", drivingDistance);
//        requestParams.put("brand", brand);
//        requestParams.put("brandID", brandID);
//        requestParams.put("series", series);
//        requestParams.put("seriesID", seriesID);
//        requestParams.put("model", model);
//        requestParams.put("modelID", modelID);
//        requestParams.put("plateNumber", plateNumber);
//        requestParams.put("ownPhone", ownPhone);
//        requestParams.put("ownName", ownName);
//        gPostUrl(completeUrl("/MyAuto"), requestParams, handler);
//    }


//    /**
//     * 修改车辆信息
//     *
//     * @param myAutoID
//     * @param drivingDistance
//     * @param ownPhone
//     * @param ownName
//     * @param isDefault
//     * @param handler
//     */
//    public void resetAutoInfo(String myAutoID, String drivingDistance, String ownPhone,
//                              String ownName, boolean isDefault, AsyncHttpResponseHandler handler) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("myAutoID", myAutoID);
//        requestParams.put("drivingDistance", drivingDistance);
//        requestParams.put("ownPhone", ownPhone);
//        requestParams.put("ownName", ownName);
//        requestParams.put("isDefault", isDefault ? "20" : "10");
//        gPutUrl(completeUrl("/MyAuto"), requestParams, handler);
//    }

//    /**
//     * 获取个人车辆信息
//     *
//     * @param handler
//     */
//    public void getMyAuto(AsyncHttpResponseHandler handler) {
//        gGetUrl(completeUrl("/MyAuto"), new RequestParams(), handler);
//    }

//    /**
//     * 根据车牌VIN获取车辆信息
//     *
//     * @param plateNumber
//     * @param VIN
//     * @param handler
//     */
//    public void checkAuto(String plateNumber, String VIN, AsyncHttpResponseHandler handler) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("plateNumber", plateNumber);
//        requestParams.put("VIN", VIN);
//        gGetUrl(completeUrl("/MyAuto/checkAuto"), requestParams, handler);
//    }

    /**
     * 获取充值金额上限及充值金额分段
     */
    public void amountConfig(AsyncHttpResponseHandler handler) {
        gGetUrl(completeUrl("/Prepaid/amountConfig"), new RequestParams(), handler);
    }

    /**
     * 获取支付方式列表
     */
    public void listPayment(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Prepaid/listPayment");
        gGetUrl(url, handler);
    }


    /**
     * 获取充值详情
     */
    public void orderDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Prepaid/orderDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 支付 付款
     */
    public void payOnline(String orderID, String paymentID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Prepaid/pay");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 生成充值订单
     */
    public void orderCreat(String phoneNumber, String money, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phoneNumber", phoneNumber);
        requestParams.put("money", money);
        gGetUrl(completeUrl("/Prepaid/orderCreat"), requestParams, handler);
    }

    /**
     * 取消订单
     */
    public void orderCancel(String orderID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gPutUrl(completeUrl("/Prepaid/orderCancel"), requestParams, handler);
    }

    /**
     * 充值历史
     */
    public void rechargeHistory(AsyncHttpResponseHandler handler) {
        gGetUrl(completeUrl("/Prepaid/history"), handler);
    }

    /**
     * 获取充值可用的积分
     */
    public void score(String number, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("number", number);
        gGetUrl(completeUrl("/Prepaid/score"), requestParams, handler);
    }


    /**
     * 获取实名认证验证码,
     * 设置支付密码，
     * 修改支付密码，
     * 忘记支付密码
     * 验证码用途 10认证 20修改支付密码 30找回支付密码
     *
     * @param username 手机号
     */
    final static public String SET_PWD = "10";
    final static public String CHANGE_PWD = "20";
    final static public String FORGET_PWD = "30";

    //10短信 20语音
    final static public String SMS_CAPTCHA = "10";
    final static public String VOICE_CAPTCHA = "20";

    public void getCaptcha(String username, String useType, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Captcha/userInfo");
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("useType", useType);
        gGetUrl(url, requestParams, handler);
    }

    public void getCaptcha(String username, String useType, String sendType, AsyncHttpResponseHandler handler) {

        if (TextUtils.isEmpty(sendType)) {
            getCaptcha(username, useType, handler);
        }

        String url = completeUrl("/Captcha/userInfo");
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("useType", useType);
        requestParams.put("sendType", sendType);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 实名认证 设置、修改余额支付密码
     *
     * @param captcha     验证码
     * @param realName    真实姓名
     * @param IDNumber    身份证
     * @param payPassword 支付密码
     * @param handler     回调
     */
    public void setPaidPassword(String captcha, String realName, String IDNumber, String payPassword, LoadingAnimResponseHandler handler) {
        String url = completeUrl("/Users/realNameAuthentication");
        RequestParams requestParams = new RequestParams();
        requestParams.put("captcha", captcha);
        requestParams.put("realName", realName);
        requestParams.put("IDNumber", IDNumber);
        requestParams.put("payPassword", payPassword);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 获取语音验证码
     *
     * @param phoneNumber 电话号码
     * @param handler     回调
     */
    public void getVoiceCaptcha(String phoneNumber, LoadingAnimResponseHandler handler) {

    }

    /**
     * 忘记支付密码步骤  1 验证信息
     *
     * @param captcha  验证码
     * @param realName 真实姓名
     * @param IDNumber 身份证
     * @param handler  回调
     */
    public void verdifyUsernameForPayPWD(String captcha, String realName, String IDNumber, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/authenticateRealNameInfo");
        RequestParams requestParams = new RequestParams();
        requestParams.put("captcha", captcha);
        requestParams.put("realName", realName);
        requestParams.put("IDNumber", IDNumber);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 忘记支付密码步骤 2 重设密码
     *
     * @param captcha        验证码
     * @param realName       真实姓名
     * @param IDNumber       身份证
     * @param newPayPassword 新支付密码
     * @param handler        回调
     */
    public void forgetPayPWDReset(String captcha, String realName, String IDNumber, String newPayPassword, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/forgotPayPassword");
        RequestParams requestParams = new RequestParams();
        requestParams.put("captcha", captcha);
        requestParams.put("realName", realName);
        requestParams.put("IDNumber", IDNumber);
        requestParams.put("newPayPassword", newPayPassword);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 修改支付密码 步骤1 验证信息
     *
     * @param captcha        验证码
     * @param oldPayPassword 旧密码
     * @param handler        回调
     */
    public void changePayPWD(String captcha, String oldPayPassword, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/authenticatePayPassword");
        RequestParams requestParams = new RequestParams();
        requestParams.put("captcha", captcha);
        requestParams.put("oldPayPassword", oldPayPassword);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 修改支付密码 步骤2 重设密码
     *
     * @param captcha        验证码
     * @param oldPayPassword 旧密码
     * @param newPayPassword 新密码
     * @param handler        回调
     */
    public void resetPayPWD(String captcha, String oldPayPassword, String newPayPassword, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/changePayPassword");
        RequestParams requestParams = new RequestParams();
        requestParams.put("captcha", captcha);
        requestParams.put("oldPayPassword", oldPayPassword);
        requestParams.put("newPayPassword", newPayPassword);
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 获取我的订单列表
     */
    public void getMyOrderList(String token, String orderType, int page, int count, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("orderType", orderType);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(completeUrl("/Order/OrderList"), requestParams, handler);
    }


    /**
     * 工单的url跳转
     */
    public String getWorkOrderUrl(String workOrderID, String erpShopCode) {
//        String control = "/myAuto/workOrderDetail?" +
//                "token=" + UserInfoHelper.getInstance().getToken(BaseApplication.getInstance()) +
//                "&" +
//                "workOrderID=" + workOrderID+
//                "&" +
//                "erpShopCode=" + erpShopCode;
//        return ApiUtil.getAccountURL(control);
        RequestParams requestParams = new RequestParams();
        requestParams.put("workOrderID", workOrderID);
        requestParams.put("erpShopCode", erpShopCode);
        return getRequestUrl(ApiUtil.getAccountURL("/MyAuto/workOrderDetail"), requestParams);

    }

    /**
     * 装潢单
     */
    public String getAccesorryWorkOrderUrl(String workOrderID, String erpShopCode) {
//        String control = "/myAuto/accesorryWorkOrderDetail?" +
//                "token=" + UserInfoHelper.getInstance().getToken(BaseApplication.getInstance()) +
//                "&" +
//                "workOrderID=" + workOrderID+
//                "&" +
//                "erpShopCode="+erpShopCode;
//        return ApiUtil.getAccountURL(control);
        RequestParams requestParams = new RequestParams();
        requestParams.put("workOrderID", workOrderID);
        requestParams.put("erpShopCode", erpShopCode);
        return getRequestUrl(ApiUtil.getAccountURL("/myAuto/accesorryWorkOrderDetail"), requestParams);
    }

//    /**
//     * 发表评论
//     *
//     * @param orderID          订单ID
//     * @param technologyScore  技术评分
//     * @param serveScore       //服务评分
//     * @param environmentScore //环境评分
//     * @param content          //评论内容
//     * @param handler
//     * @throws FileNotFoundException
//     */
//    public void sendComment(String shopID, String orderID, int technologyScore, int serveScore, int environmentScore, String content, RequestParams requestParams, AsyncHttpResponseHandler handler) throws FileNotFoundException {
//        String url = completeUrl("/Comments/maintenance");
//        requestParams.put("orderID", orderID);
//        requestParams.put("shopID", shopID);
//        requestParams.put("technologyScore", technologyScore);
//        requestParams.put("serveScore", serveScore);
//        requestParams.put("environmentScore", environmentScore);
//        requestParams.put("content", content);
//        requestParams = getDESRequestParams(url, requestParams);
//
//        client.post(url, requestParams, handler);
//    }


    /**
     * 发表评论
     *
     * @param orderID          订单ID
     * @param technologyScore  技术评分
     * @param serveScore       //服务评分
     * @param environmentScore //环境评分
     * @param content          //评论内容
     * @throws FileNotFoundException
     */
    public void sendComment(Context context, String shopID, String orderID, int technologyScore, int serveScore, int environmentScore, String content, List<ImageItem> pics, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        String url = completeUrl("/Comments/maintenance");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("shopID", shopID);
        requestParams.put("technologyScore", technologyScore);
        requestParams.put("serveScore", serveScore);
        requestParams.put("environmentScore", environmentScore);
        requestParams.put("content", content);
        requestParams = getDESRequestParams(url, requestParams);
        if (pics != null && pics.size() > 0) {
            int size = pics.size();
            for (int i = 0; i < size; i++) {
                try {

//                    Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(
//                            pics.get(i).sourcePath,
//                            ScreenUtil.getScreenWidth(context),
//                            ScreenUtil.getScreenHeight(context));
                    @SuppressWarnings("ConstantConditions")
//                    String path = context.getExternalCacheDir().getPath() + "/temp" + i + ".jpg";
//                    BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 640, 640, path, 96);
                            String path = pics.get(i).sourcePath;
                    File file = new File(path);
                    requestParams.put("image" + (i + 1), file);
//                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 意见反馈
     *
     * @param mobile
     * @param content
     * @param type
     */
    public void sendAdvice(String mobile, String content, int type, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Advice");
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("content", content);
        requestParams.put("type", type);
        requestParams.put("os", "Android");
        requestParams.put("osVersion", "Android" + Build.VERSION.RELEASE);
        requestParams.put("deviceName", Build.MODEL);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 客户投诉
     *
     * @param mobile
     * @param content
     * @param feedbackID
     */
    @Deprecated
    public void sendFeedback(String mobile, String content, String feedbackID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Advice/feedback");
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("content", content);
        requestParams.put("feedbackID", feedbackID);
        requestParams.put("os", "Android");
        requestParams.put("osVersion", "Android" + Build.VERSION.RELEASE);
        requestParams.put("deviceName", Build.MODEL);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 客户投诉
     *
     * @param mobile
     * @param content
     * @param feedbackID
     */
    public void sendFeedback(String mobile, String content, String feedbackID, String siteID
            , String brand, String shopID, String employee, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Advice/feedback");
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("siteID", siteID);
        requestParams.put("brand", brand);
        requestParams.put("shopID", shopID);
        requestParams.put("employee", employee);
        requestParams.put("content", content);
        requestParams.put("feedbackID", feedbackID);
        requestParams.put("os", "Android");
        requestParams.put("osVersion", "Android" + Build.VERSION.RELEASE);
        requestParams.put("deviceName", Build.MODEL);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 投诉意见选项
     *
     * @param handler
     */
    public void feedbackOption(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Advice/feedbackOption");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);

    }

}
