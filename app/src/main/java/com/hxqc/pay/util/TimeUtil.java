package com.hxqc.pay.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: wanghao
 * Date: 2015-04-07
 * FIXME
 * 时间转换
 */
public class TimeUtil {

    //转化成时间戳
    public static long turnToT(String t) throws Exception {
        if (TextUtils.isEmpty(t)) {
            return 1;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date date = format.parse(t);
            return date.getTime();
//            return date.getTime() / 1000;
        }
    }


    //转换成秒
    public static long getLeftTime(String startT, String expiredT) throws Exception {
        return turnToT(expiredT) - turnToT(startT);
    }


    //转换成所需时间
//    public static String getDTime(String startT, String expiredT) throws Exception {
//        int interval = (int)getLeftTime(startT, expiredT);
//        String day = "";
//        String hour = "";
//        String minute = "";
//        int days = interval / 86400;
//        interval %= 86400;
//        int hours = interval / 3600;
//        interval %= 3600;
//        int minutes = interval / 60;
//
//        if (days >= 1) {
//            day = days + "天";
//        }
//        if (hours >= 1) {
//            hour = hours + "小时";
//        }
//        if (minutes >= 1) {
//            minute = minutes + "分钟";
//        }
//
//        return day + hour + minute;
//    }

    public static String getDTime(long m) throws Exception {
        int interval = (int) (m / 1000);
        String day = "";
        String hour = "";
        String minute = "";
        int days = interval / 86400;
        interval %= 86400;
        int hours = interval / 3600;
        interval %= 3600;
        int minutes = interval / 60;

        if (days >= 1) {
            day = days + "天";
        }
        if (hours >= 1) {
            hour = hours + "小时";
        }
        if (minutes >= 1) {
            minute = minutes + "分钟";
        } else {
            minute = interval + "秒";
        }

        return day + hour + minute;
    }

}
