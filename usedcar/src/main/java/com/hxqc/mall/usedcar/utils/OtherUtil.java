package com.hxqc.mall.usedcar.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.util.DebugLog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : 钟学东
 * @Since : 2015-08-07
 * FIXME
 * Todo
 */
public class OtherUtil extends com.hxqc.mall.core.util.OtherUtil {

//    发布时间格式：
//            1、一小时内，显示具体分钟，例如：45分钟前发布；
//            2、一天内，显示具体小时，例如：18小时前发布；
//            3、一天以上，显示具体日期（格式MM-DD）；
//            4、特殊情况（发布时间和现在时间跨过年底时间）：
// 当发布时间距现在少于七天大于一天，则优先显示天数，
// 例如：5天前发布，当发布时间距现在小于一天，优先显示小时；当发布时间距现在小于一小时，
// 优先显示分钟；当发布时间距现在多余七天，则显示年份日期（格式YY-MM-DD）；

    public static String getTime(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(time);
        String nowStr1 = format1.format(new Date());
        long delta = System.currentTimeMillis() - date.getTime();
        if (delta < 60 * 1000) {
            return "刚刚发布";
        } else if (delta <= 60 * 60 * 1000) {
            return (int) delta / 60 / 1000 + "分钟";
        } else if (delta > 5 * 60 * 1000 && delta <= 24 * 60 * 60 * 1000) {
            return (int) delta / 60 / 60 / 1000 + "小时";
        } else if (delta > 24 * 60 * 60 * 1000 && nowStr1.substring(0, 4).equals(time.substring(0, 4))) {
            return time.substring(5, 10);
        } else {
            return time.substring(2, 10);
        }
    }


    //返回最后一个指定字符串之前的所有字符
    public static String substringBeforeLast(String str, String separator) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(separator))
            return str;
        int pos = str.lastIndexOf(separator);
        if (pos == -1)
            return str;
        else
            return str.substring(0, pos);
    }

    //返回最后一个指定字符串之后的所有字符
    public static String substringAfterLast(String str, String separator) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(separator))
            return str;
        int pos = str.lastIndexOf(separator);
        if (pos == -1)
            return str;
        else
            return str.substring(pos + separator.length(), str.length());
    }

    //控制两位小数
    public static void control2Decimal(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 格式化余额保留俩位小数 且0 为 0
     *  by 李烽
     * @param balance
     * @return
     */
    public static String formatBalance(String balance) {
        if (TextUtils.isEmpty(balance))
            return "";
        try {
            float v = Float.parseFloat(balance);
            return formatBalance(v);
        } catch (Exception e) {
            DebugLog.e("Exception", "balance not be number");
            return "-1";
        }
    }


    public static String formatBalance(float balance) {
        if (balance == 0)
            return "0";
        else {
            float f = (float) (Math.round(balance * 100)) / 100;//保留两位小数
            return f + "";
        }
    }

    /**
     * float转String 避免科学计数
     *
     * @param value
     * @return
     */
    public static String floatToString(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//保留2位小数
        return decimalFormat.format(value);
    }

    /**
     * 日期传输格式
     * 返回xxxx-xx-xx
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        String[] str = date.split("-");
        String year = str[0];
        String month = str[1];
        String day = str[2];
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    //里程显示规则
    public static String formatRange(String range) {
        if (Float.parseFloat(range) == 0) {
            return "0";
        }
        if (range.contains(".")) {
            if (range.endsWith("0") || range.endsWith(".")) {
                range = range.substring(0, range.length() - 1);
                if (range.endsWith("0")) {
                    range = range.substring(0, range.length() - 2);
                }
            }
        }
        return range;
    }

    /**
     * uri解析本地路径
     *
     * @param mContext
     * @param uri
     * @return
     */
    public static String convertFileUri(Context mContext, Uri uri) {
        if (uri != null && "content".equals(uri.getScheme())) {
            Cursor cursor = mContext.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            cursor.moveToFirst();
            String mFilePath = cursor.getString(0);
            cursor.close();
            return mFilePath;
        } else {
            return uri.getPath();
        }
    }

    //清空选中的图片
    public static void removeTempFromPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CustomConstants.APPLICATION_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).remove(CustomConstants.AVAILABLE_IMAGE_SIZE).apply();
    }
}
