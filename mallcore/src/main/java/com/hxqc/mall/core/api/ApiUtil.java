package com.hxqc.mall.core.api;

/**
 * Author:胡俊杰
 * Date: 2016/4/9
 * FIXME
 * Todo 请求链接url
 */
public class ApiUtil {
    public static final String Account_VERSION = "V2";
    public static final String Maintain_VERSION = "v1";
    public static final String Accessory_VERSION = "v1";
    public static final String Accessory_4s_VERSION = "v2";
    public static final String UsedCar_VERSION = "v1";
    public static boolean isDebug = false;
    public static String Host_Insurance = "https://value-added.m.hxqc.com";//买保险

    public static String AccountHostURL = "https://app-interface.hxqc.com";//商城 会员，自营，第三方店铺，周边,洗车
    public static String MaintainHostURL = "https://maintain-interface-goods.admin.hxqc.com";//保养,维修,救援
    public static String AccessoryHostURL = "https://accessory-goods.admin.hxqc.com";//用品，
    public static String UsedCarHostURL = "https://interface-usedcar.admin.hxqc.com";//二手车
    public static String APPConfigHostURL, RNBsdiffURL = "http://appconf.hxqc.com";//商城配置
    public static String ConfigAppKey = "6d91569d-173b-48a9-82bd-4e51c9abbc6b";//后台app key
    public static String UsedCarAuctionHostURL = "http://usedcar.m.hxqc.com";//二手车竞拍


    //    public static String UsedCarSellCarHostURL;//二手车
    public static String getInsuranceURL(String insuracneC) {
        return Host_Insurance + insuracneC;
    }

    public static void setTestHostUrl(String mallHostUrl) {
        AccountHostURL = mallHostUrl;
    }

    /**
     * 配置信息
     */
    public static String getAPPConfigHostURL(String control) {
        return APPConfigHostURL + control;
    }

    /**
     * 新能源
     */
    public static String getEcarURL(String control) {
        return AccountHostURL + "/Ecar/" + Account_VERSION + control;
    }

    /**
     * 4S店
     */
    public static String getShopURL(String control) {
        return AccountHostURL + "/Shop/" + Account_VERSION + control;
    }

    /**
     * 会员中心
     */
    public static String getAccountURL(String control) {
        return AccountHostURL + "/Account/" + Account_VERSION + control;
    }

    /**
     * 商城
     */
    public static String getMallUrl(String control) {
        return AccountHostURL + "/Mall/" + Account_VERSION + control;
    }

    /**
     * 商城
     */
    public static String getMallUrl() {
        return getMallUrl("");
    }

    /**
     * 保养
     */
    public static String getMaintainURL(String control) {
        return MaintainHostURL + "/" + Maintain_VERSION + control;
    }

    /**
     * 用品
     */
    public static String getAccessoryURL(String control) {
        return AccessoryHostURL + "/" + Accessory_VERSION + control;
    }

    /**
     * 用品4s店
     */
    public static String getAccessory4SURL(String control) {
        return AccessoryHostURL + "/" + Accessory_4s_VERSION + control;
    }

    /**
     * 二手车
     */
    public static String getUsedCarURL(String control) {
        return UsedCarHostURL + "/" + UsedCar_VERSION + "/interface/" + control;
    }

    /**
     * 二手车竞拍host
     */
    public static String getUsedCarAuctionHostURL() {
        return UsedCarAuctionHostURL;
    }
    //
//    /**
//     * 二手车卖车
//     */
//    public static String getUsedCarSellCarURL(String control) {
//        return UsedCarSellCarHostURL + "/" + UsedCar_VERSION + control;
//    }


    /**
     * 周边服务
     */
    public static String getAroundURL(String control) {
        return AccountHostURL + "/Service/" + Account_VERSION + control;
    }

    /**
     * 洗车
     */
    public static String getCarWashURL(String control) {
        return AccountHostURL + "/Carwash/" + Account_VERSION + control;
    }


    /**
     * 首页 请求
     */
    public static String getAroundURL() {
        return AccountHostURL + "/Service/" + Account_VERSION;
    }


    /**
     * 汽车资讯
     */
    public static String getAutoInfoURL(String control) {
        return AccountHostURL + "/Info/" + Account_VERSION + control;
    }

    //rn 汽车资讯 host
    public static String getAccountURL() {
        return getAutoInfoURL("/Index");
    }

    /**
     * 首页更新
     */
    public static String getHomeRNURLHost() {
        return RNBsdiffURL;
    }

    /**
     * tinker更新
     */
    public static String getTinkerURLHost() {
        return RNBsdiffURL;
    }
}
