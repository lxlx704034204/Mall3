package com.hxqc.mall.paymethodlibrary.util;

import com.hxqc.security.AESUtils;

/**
 * Author: wanghao
 * Date: 2015-09-18
 * FIXME     各种支付 常量参数
 * Todo
 */
public class PayKeyConstants {

    /**
     *   WX_API_KEY:
     *   A15D37EABC771F543A4238E93417E2CD5B778C6A2EB0FEF3A50BD1A0C297E62FFFAED6019C1C4D28994EE1AFC5A0AB38
     *   原始数据：hxqc847182956074527784654928hxqc
     WX_APP_ID:
     28C0BFC9987A8F6A9D3CF94EEDE65DF293F7179A8EAF72E604FBC2DCE028A84F
     原始数据：wx94d9df1ff64535c9
     WX_MCH_ID:
     934B9FBF936B4D53BF2539C2583C0545
     原始数据：1271944201
     YJF_PARTNER_ID:
     184464A168D4E77DB527955CF2D2A94460EC83BCD2E0D99CF443A10B68613383
     原始数据：20141226020000099880
     YJF_SECURITY_ID:
     B71422D736C626B668923BE660BFB4B78AE20513EDED44B6290EE778635BFF3CFFAED6019C1C4D28994EE1AFC5A0AB38
     原始数据：d588dd8f67237dfb81656a6ba3757d08
     YJF_SERVER_URL:
     47FF4D7FC16CBC1CA44CFF70F0D9853635616F7DD61C4F782A45B6D7C544EF2629B7295F1791DD1F00FC5E74EFEBC165
     原始数据：https://openapi.yijifu.net/gateway
     */


    /**
     * 微信------------------------------------------------------------------------------------------------
     */
    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
//    public static final String WX_APP_ID = "wx94d9df1ff64535c9";
//    //商户号
//    public static final String WX_MCH_ID = "1271944201";
//    //  API密钥，在商户平台设置
//    public static final  String WX_API_KEY ="hxqc847182956074527784654928hxqc";
    private static final String WX_APP_ID = "28C0BFC9987A8F6A9D3CF94EEDE65DF293F7179A8EAF72E604FBC2DCE028A84F";
    //商户号
    private static final String WX_MCH_ID = "934B9FBF936B4D53BF2539C2583C0545";
    //  API密钥，在商户平台设置
    private static final String WX_API_KEY = "A15D37EABC771F543A4238E93417E2CD5B778C6A2EB0FEF3A50BD1A0C297E62FFFAED6019C1C4D28994EE1AFC5A0AB38";

    /**
     * 易极付----------------------------------------------------------------------------------------------
     */
    //服务地址
//    public static final String YJF_SERVER_URL = "https://openapi.yijifu.net/gateway";
//    //合作id
//    public static final String YJF_PARTNER_ID = "20141226020000099880";
//    //保密id
//    public static final String YJF_SECURITY_ID = "d588dd8f67237dfb81656a6ba3757d08";

    private static final String YJF_SERVER_URL = "47FF4D7FC16CBC1CA44CFF70F0D9853635616F7DD61C4F782A45B6D7C544EF2629B7295F1791DD1F00FC5E74EFEBC165";
    //合作id
    private static final String YJF_PARTNER_ID = "184464A168D4E77DB527955CF2D2A94460EC83BCD2E0D99CF443A10B68613383";
    //保密id
    private static final String YJF_SECURITY_ID = "B71422D736C626B668923BE660BFB4B78AE20513EDED44B6290EE778635BFF3CFFAED6019C1C4D28994EE1AFC5A0AB38";


    public static String getWxAppId() throws Exception {
        return AESUtils.desEncrypt(WX_APP_ID);
    }

    public static String getWxMchId() throws Exception {
        return AESUtils.desEncrypt(WX_MCH_ID);
    }

    public static String getWxApiKey() throws Exception {
        return AESUtils.desEncrypt(WX_API_KEY);
    }

    public static String getYjfServerUrl() throws Exception {
        return AESUtils.desEncrypt(YJF_SERVER_URL);
    }

    public static String getYjfPartnerId() throws Exception {
        return AESUtils.desEncrypt(YJF_PARTNER_ID);
    }

    public static String getYjfSecurityId() throws Exception {
        return AESUtils.desEncrypt(YJF_SECURITY_ID);
    }
}
