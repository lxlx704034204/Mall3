package com.hxqc.mall.core.util.utils.disklurcache;

/**
 * 缓存有效期管理
 * Created by zhaofan.
 */
public class CacheValidManager {
    public static final String SEPARATOR = "@###ValidTime:";

    public static String StringWithTimeInfo(int timeLimit, String value) {
        String nowTime = System.currentTimeMillis() + "";
        String timeInfo = SEPARATOR + nowTime + "-" + timeLimit;
        return value + timeInfo;
    }

    public static byte[] BytesWithTimeInfo(int timeLimit, byte[] data1) {
        String nowTime = System.currentTimeMillis() + "";
        String timeInfo = SEPARATOR + nowTime + "-" + timeLimit;
        byte[] data2 = timeInfo.getBytes();
        byte[] retdata = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, retdata, 0, data1.length);
        System.arraycopy(data2, 0, retdata, data1.length, data2.length);
        return retdata;
    }


    /**
     * 判断缓存是否过期
     * Created by zf.
     *
     * @param value
     * @return true 过期
     */
    public static String isDue(String value) {
        String timeInfo = "";
        if (value.contains(SEPARATOR + "")) {
            String[] a = value.split(SEPARATOR + "");
            timeInfo = a[a.length - 1];
            long oldTime = Long.parseLong(timeInfo.split("-")[0]);
            int timeLimit = Integer.parseInt((timeInfo.split("-")[1]));
            return (System.currentTimeMillis() - oldTime) / 1000 > timeLimit ? "" : value.replace(SEPARATOR + timeInfo, "");

        } else
            return value;
    }


    public static byte[] isDue(byte[] res) {
        int index = indexOf(res);
        if (index == -1) {
            return res;
        } else {
            byte[] deleteAfter = copyOfRange(res, 0, index);
            byte[] timeVaild = copyOfRange(res, index, res.length);
            String timeInfo = new String(timeVaild).replace(SEPARATOR, "");

            long oldTime = Long.parseLong(timeInfo.split("-")[0]);
            int timeLimit = Integer.parseInt((timeInfo.split("-")[1]));
            return (System.currentTimeMillis() - oldTime) / 1000 > timeLimit ?
                   null : deleteAfter;

        }
    }


    /**
     * 判断字节流中 {@link CacheValidManager#SEPARATOR} 分隔符的位置
     * Created by zf.
     */
    private static int indexOf(byte[] data) {
        int flag = 0;
        String c = CacheValidManager.SEPARATOR;
        for (int i = data.length - 1; i >= 0; i--) {
            if (data[i] == c.charAt(0)) {
                for (int j = 1; j < c.length(); j++) {
                    if ((i + j) < data.length - 1 && data[i + j] == c.charAt(j)) {
                        flag++;
                    }
                    if (j == c.length() - 1)
                        if (flag == c.length() - 1)
                            return i;
                }
            }
        }
        return -1;
    }


    /**
     * 截取字节流
     * Created by zf.
     *
     * @param original
     * @param from
     * @param to
     * @return
     */
    private static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

   /* *//**
     * 获取实际value值
     *
     * @param value
     * @return
     *//*
    public static String getRealValue(String value) {
        if (value.contains(SEPARATOR + "")) {
            value = value.split(SEPARATOR + "")[0];
            return value;
        }
        return value;
    }

    public static byte[] getRealBytesValue(String value) {
        String str = CacheValidManager.getRealValue(value);
        return str.getBytes();
    }
*/

}
