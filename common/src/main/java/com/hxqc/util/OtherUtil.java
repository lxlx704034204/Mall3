/**
 * author 胡俊杰 Todo
 */
package com.hxqc.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;


import com.hxqc.apache.ByteArrayOutputStream;
import com.hxqc.apache.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * author Administrator
 */
public class OtherUtil {

    /**
     * 获取ass图片
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    /**
     * 获取设备信息
     *
     * @param ctx
     * @return
     */
    public static Map< String, String > collectDeviceInfo(Context ctx) {
        Map< String, String > infos = new HashMap< String, String >();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return infos;
    }

    // 从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName,AssetManager.ACCESS_BUFFER);
            result = readTextFile(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String readTextFile(InputStream inputStream) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            IOUtils.closeQuietly(outputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    /**
     * 金额格式化
     *
     * @param symbol
     *         是否有羊角符  ¥
     * @return 1w以下单位为元，1w以上单位为w
     */
    public static String amountFormat(String value, boolean symbol) {
        try {
            return amountFormat(Float.valueOf(value), symbol);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return amountFormat(0F, symbol);
        }
    }


    /**
     * 金额格式化
     *
     * @param symbol
     *         是否有羊角符  ¥
     * @return 1w以下单位为元，1w以上单位为w
     */
    public static String amountFormat(float value, boolean symbol) {

        if (symbol) {
            //有羊角符
            if (value >= 10000) {
                DecimalFormat format = new DecimalFormat("#####0.00");
                return String.format("¥ %s万", format.format(Math.floor(value / 100.00) / 100.00));
            } else {
                return String.format("¥ %.2f", value);
            }
        } else {
            DecimalFormat format = new DecimalFormat("######.##");
            if (value >= 10000) {
                return String.format("%s万", format.format(Math.floor(value / 100.00) / 100.00));
            } else {
                return String.format("%s元", format.format(value));
            }
        }
    }


    /**
     * 金额格式化
     *
     * @return 1w以下单位为元，1w以上单位为w
     */
    public static String amountFormat(float value) {
        if (value == 0) {
            return 0 + "元";
        }
        NumberFormat nf = NumberFormat.getInstance();
        if (value >= 10000) {
            nf.setMinimumFractionDigits(2);
            String str = nf.format(Math.floor(value / 100.00) / 100.00);
            return String.format("%s万", str);
        } else {
            DecimalFormat format = new DecimalFormat("###,###,###,###,##0.00");
//            return String.format("%s元", nf.format(value / 1.00));
            return String.format("%s元", format.format(value));
        }
    }


    public static String amountFormat(String value) {
        try {
            return amountFormat(Float.valueOf(value));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }
    }

}
