package com.hxqc.security;

/**
 * 2014年12月15日
 * A.java
 *
 * author CPR011
 */

import android.annotation.SuppressLint;

import com.hxqc.util.ZAKK;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * author CPR011
 *         TODO
 */
public class AESUtils {
//    private static byte[] bytes = new byte[]{0x08, 0x08, 0x04, 0x0b,
//            0x02, 0x0f, 0x0b, 0x0c, 0x01, 0x02, 0x0f, 0x07, 0x0c, 0x03, 0x07,
//            0x0a, 0x04, 0x0f, 0x06, 0x01, 0x0e, 0x09, 0x05, 0x01, 0x0a, 0x0c,
//            0x01, 0x09, 0x06, 0x07, 0x09, 0x0d};

    /**
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        try {
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            SecretKeySpec keyspec = new SecretKeySpec(ZAKK.bytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            byte[] encrypted = cipher.doFinal(data.getBytes());

            return toHex(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * 解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data) throws Exception {
        try {
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(ZAKK.bytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec);
            byte[] original = cipher.doFinal(toByte(data));
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte aBuf : buf) {
            appendHex(result, aBuf);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        final String HEX = "0123456789ABCDEF";
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        }
        return result;
    }

}
