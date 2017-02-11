package com.hxqc.mall.core.model;

import com.hxqc.mall.views.MarkableImageView;

/**
 * Author:李烽
 * Date:2016-01-21
 * FIXME
 * Todo 最新消息
 */
public class NewestMessage {
    public String title;
    public String date;
    public int hasUnread;
    public int messageType;

    public MarkableImageView.IconState getMessageState() {
        return hasUnread == 1 ? MarkableImageView.IconState.UNREAD : MarkableImageView.IconState.READ;
    }

//    /**
//     * 获取日期显示
//     *
//     * @return
//     */
//    public String getDate() {
//       return getDate(this.date);
//    }

//    public static String getDate(String date) {
//        final long ONE_DAY = 1000 * 60 * 60 * 24;
//        if (TextUtils.isEmpty(date))
//            return dateFormat(date);
//
//        if (date.length() > 10)
//            date = date.substring(0, 10);
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        long l = System.currentTimeMillis();
//        String now = simpleDateFormat.format(new Date(l));
//        DebugLog.i(NewestMessage.class.getSimpleName(), now);
//        if (now.equals(date))
//            return "今天";
//
//        long longTime = getLongTime(date);
//        long longTime1 = getLongTime(now);
//
//        if (longTime > 0 && longTime1 > 0) {
//            long gap = longTime1 - longTime;
//            if (gap > ONE_DAY)
//                return dateFormat(date);
//            else if (gap <= ONE_DAY && gap > 0)
//                return "昨天";
//            else if (gap < 0)
//                return dateFormat(date);
//            else return "今天";
//        } else {
//            return dateFormat(date);
//        }
//
//    }
//
//    private static String dateFormat(String date) {
//        if (TextUtils.isEmpty(date))
//            return "";
//        else {
//            date = date.replace("-", "/");
//            try {
//                date = date.substring(2);
//                return date;
//            } catch (StringIndexOutOfBoundsException e) {
//                e.printStackTrace();
//                return date;
//            }
//        }
//    }
//
//    /**
//     * String时间格式转为long格式
//     *
//     * @param date
//     * @return
//     */
//    private static long getLongTime(String date) {
//        if (TextUtils.isEmpty(date))
//            return 0;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date parse = null;
//        try {
//            parse = simpleDateFormat.parse(date + " 00:00:00");
//            return parse.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
}
