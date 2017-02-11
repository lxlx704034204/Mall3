package com.hxqc.mall.drivingexam.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Created by zhaofan on 2016/8/4.
 */
public class StringUtils {

    /**
     * 保留小数位
     */
    public static String DecimalFormat(Object obj, String type) {
        /*
		 * #.## 表示有0就显示0,没有0就不显示 例如 1.20 就会变成1.2 0.00表示,有没有0都会显示 例如 1.20 依然是 1.20
		 * 在使用xliff标签的%n$mf的方式的时候，m可以设置为1.n（n为要保留的小数位数，没有则补零，前面的1会完整保留当前数据，比如100
		 * .2会显示100.20，不用担心前面整数部分显示不正确）
		 */
        DecimalFormat df = new DecimalFormat(type);
        return String.valueOf(df.format(obj));
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
