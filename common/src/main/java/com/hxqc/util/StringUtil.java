package com.hxqc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringUtil {
    public static final String US_ASCII = "US-ASCII";
    public static final String GB2312 = "gb2312";

    public static final String ISO_8859_1 = "ISO-8859-1";

    public static final String UTF_8 = "UTF-8";

    public static final String UTF_16BE = "UTF-16BE";

    public static final String UTF_16LE = "UTF-16LE";

    public static final String UTF_16 = "UTF-16";

    public static final String GBK = "GBK";

    public static String utf8Togb2312(String str) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);

            switch (c) {

                case '+':

                    sb.append(' ');

                    break;

                case '%':

                    try {

                        sb.append((char) Integer.parseInt(

                                str.substring(i + 1, i + 3), 16));

                    } catch (NumberFormatException e) {

                        throw new IllegalArgumentException();

                    }

                    i += 2;

                    break;

                default:

                    sb.append(c);

                    break;

            }

        }

        String result = sb.toString();

        String res = null;

        try {

            byte[] inputBytes = result.getBytes("8859_1");

            res = new String(inputBytes, "UTF-8");

        } catch (Exception e) {
        }

        return res;

    }

    // 将 GB2312 编码格式的字符串转换为 UTF-8 格式的字符串：

    public static String gb2312ToUtf8(String str) {

        String urlEncode = "";

        try {

            urlEncode = URLEncoder.encode(str, "UTF-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return urlEncode;

    }

    public static String toASCII(String str) throws UnsupportedEncodingException {
        return changeCharset(str, US_ASCII);
    }

    public static String toISO_8859_1(String str) throws UnsupportedEncodingException {
        return changeCharset(str, ISO_8859_1);
    }

    public static String toGB2312(String str) throws UnsupportedEncodingException {
        return changeCharset(str, GB2312);
    }

    public static String toUTF_8(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_8);
    }

    public static String toUTF_16BE(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_16BE);
    }

    public static String toUTF_16LE(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_16LE);
    }

    public static String toUTF_16(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_16);
    }

    public static String toGBK(String str) throws UnsupportedEncodingException {
        return changeCharset(str, GBK);
    }

    public static String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    public static String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    // 数字小写转大写
    public static String convert(int d) {
        String s = String.valueOf(d);
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};// 可以吧“一”换成“壹”
        String ss[] = new String[]{"", "十", "百", "千"};// 数值过大，可以继续加“万”，“十万”....
        StringBuffer sb = new StringBuffer();
        for (int i = 0, j = (s.length() - 1); i < s.length(); i++, j--) {
            String index = String.valueOf(s.charAt(i));
            // 遇到零的时候的处理，例如：405
            if (str[Integer.parseInt(index)].equals("零")) {
                if (i == (s.length() - 1)) {
                    continue;
                }
                sb = sb.append(str[Integer.parseInt(index)]);
            } else {
                sb = sb.append(str[Integer.parseInt(index)]).append(ss[j]);
            }
        }
        String numble = sb.toString();
        // 清除sb中的字符值
        sb.delete(0, sb.length());

        return numble;
    }
}
