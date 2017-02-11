package com.hxqc.mall.core.bsdiff;

import android.text.TextUtils;

import com.hxqc.util.DebugLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author:  wh
 * Date:  2016/8/16
 * FIXME   获取文件md5值
 * Todo
 */
public class MD5Util {

    /**
     * 通过md5值判断文件完整性
     * @param originalMD5  文件原始md5
     * @param downloadFile 下载下来的文件
     * @return 是否一致
     */
    public static boolean verdictMD5isEqual(String originalMD5, File downloadFile) {
        if (TextUtils.isEmpty(originalMD5)) {
            return false;
        }

        if (downloadFile==null) {
            return false;
        }

        if (!downloadFile.exists()){
            return false;
        }

        boolean isEqual = false;
        if (originalMD5.equals(getFileMD5(downloadFile))) {
            isEqual = true;
        }
        return isEqual;
    }

    public static boolean verdictMD5isEqual(String originalMD5, String downloadFilePath) {
        File file = new File(downloadFilePath);
        return verdictMD5isEqual(originalMD5,file);
    }


    /**
     * 获取文件md5值
     */
    private static String getFileMD5(File file) {
        String value = md5File(file);
        DebugLog.w("bsdiff_rn","文件  md5 ："+value);
        return value;
    }


    /***
     * Get MD5 of one file！test ok!
     *
     * @param filepath
     * @return
     */
    public static String getFileMD5(String filepath) {
        File file = new File(filepath);
        return getFileMD5(file);
    }

    private static final int BUFFER_SIZE = 4 * 1024;

    public static String md5Bytes(byte[] data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data, 0, data.length);
            return bytesToString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * md5File ：取文件md5值
     *
     *            文件全路径
     * @return md5串
     */
    private static String md5File(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return bytesToString(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String bytesToString(byte[] data) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] temp = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
            temp[i * 2 + 1] = hexDigits[b & 0x0f];
        }
        return new String(temp);
    }

    /**
     * md5String ：计算md5，是按照和clock mod recovery的 rom manager同样的做法计算的
     * 如果首位是0，会被直接忽略
     *
     * @param str
     * @return
     */
    public static String md5String(String str) {
        try {
            byte[] strBytes = str.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strBytes);
            BigInteger bigInt = new BigInteger(1, md5.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5String32 ：计算md5，即使首位是0，不被忽略掉
     *
     * @param str
     * @return
     */
    public static String md5String32(String str) {
        try {
            byte[] strBytes = str.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strBytes);
            return bytesToString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
