package com.hxqc.mall.paymethodlibrary.util;

/**
 * Author: wanghao
 * Date: 2015-04-02
 * FIXME
 * 判断返回结果 是否包含success = true
 */
public class AlipayResultUtil {

    public static boolean isSuccess(String url) {
        boolean flag = false;
        if (url.contains("success=\"true\"")) {
            flag = true;
        }
        return flag;
    }
}
