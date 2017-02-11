package com.hxqc.mall.paymethodlibrary.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: wanghao
 * Date: 2015-04-08
 * FIXME
 * 得到还可支付的金额
 */
public class MoneyUtil {


    public static String getUnpaid(String unpaid) {
        Log.i("list_test", unpaid);
        double m = Double.parseDouble(unpaid.substring(1));
        return m + "";
    }

    /**
     * 易极付返回参数
     */
    public static Map< String, String > getPayData(String url) {

        try {

            Map< String, String > data = new HashMap<>();

            String[] split = url.split("&");

            for (String s : split) {
                data.put(s.split("=")[0], s.split("=")[1]);
            }

            return data;

        } catch (Exception e) {

            return null;
        }


    }

    /**
     * 生成微信支付 数据
     * @param url
     * @return
     */
    public static Map<String,String> getWeChatPayData(String url){
        try {

            Map< String, String > data = new HashMap<>();

            String[] split = url.split("&");

            for (String s : split) {
                String[] ss = s.split("=");
                String  key = ss[0];
                String  content = ss[1];

                if (ss.length>2){
                    for (int i =2;i<ss.length;i++){
                        content += "="+ss[i];
                    }
                }
                data.put(key, content);
            }

            return data;

        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 交易id
     */
    final public static String TRADE_NO = "tradeNo";
    /**
     * 账号
     */
    final public static String MEMBER_ID = "memberID";


}
