package com.hxqc.newenergy.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/23.
 * Copyright:恒信汽车电子商务有限公司
 */
public class PromotionAuto implements Parcelable{

    public static final int TO_START = 0;//即将开始
    public static final int STARTED = 1;//进行中
    public static final int ENDED = 2;//已结束
    public  String itemPic=null;
    public  String itemID=null;
    public  String startTime=null;
    public  String endTime=null;
    public  String serverTime=null;
    public  String itemName=null;
    public  String itemPrice=null;
    public  String decrease=null;
    public  String subscription=null;
    public  String stock=null;
    public  String batteryLife=null;
    public String isEnded;//是否结束
    public String isStarted;//特卖开始
    public int status = -1;//状态
    public long serverL = 0;


    /**
     * 倒计时
     */
    long endL = 0;
    long startL = 0;
    public String getSubscription() {
        return subscription;
    }

    /**
     * 交易状态
     *
     * @return
     */
    public AutoDetail.TransactionStatus transactionStatus() {
        if (OtherUtil.int2Boolean(Integer.valueOf(isEnded))) {
            return AutoDetail.TransactionStatus.END;
        }
        if (OtherUtil.int2Boolean(Integer.valueOf(isStarted))) {
            return AutoDetail.TransactionStatus.NORMAL;
        } else {
            return AutoDetail.TransactionStatus.PREPARE;
        }
    }

    public long getEndTimeLong() {
        return DateUtil.getTime(endTime);
    }


    public long getStartTimeLong() {
        return DateUtil.getTime(startTime);
    }

    public synchronized String reckonByTime(Context context) {
        if (serverL < 40 * 365 * 24 * 3600) {//设置一个2010年的时间值，以免在列表页面出错
            serverL = DateUtil.getTime(serverTime);
            endL = DateUtil.getTime(endTime);
            startL = DateUtil.getTime(startTime);
        }
        long gapL = endL - serverL;
        long toStartL = startL - serverL;
        if (gapL <= 0) {
            status = ENDED;
            //服务器时间大于结束时间
            setEndPromotion();
            return context.getString(com.hxqc.mall.core.R.string.promotion_is_end);
        } else if (toStartL >= 0) {
            status = TO_START;
            int date = (int) (toStartL / (24 * 3600));
            long timeL = toStartL % (24 * 3600) * 1000;
            if (timeL < 8 * 3600 * 1000) {
                timeL += 24 * 3600 * 1000;
            }
            timeL = timeL - 8 * 3600 * 1000;
            String time = new SimpleDateFormat("HH时mm分ss秒", Locale.CHINA).format(timeL);
            if (date > 0) {
                return String.format("距离活动开始：%d天%s", date, time);
            }
            return String.format("距离活动开始：%s", time);
        } else {
            status = STARTED;
            int date = (int) (gapL / (24 * 3600));
            long timeL = gapL % (24 * 3600) * 1000;
            if (timeL < 8 * 3600 * 1000) {
                timeL += 24 * 3600 * 1000;
            }
            timeL -= 8 * 3600 * 1000;
            String time = new SimpleDateFormat("HH时mm分ss秒", Locale.CHINA).format(timeL);
            if (date > 0) {
                return String.format("剩余时间：%d天%s", date, time);
            }
            return String.format("剩余时间：%s", time);
        }
    }

    /**
     * 活动结束
     */
    public void setEndPromotion() {
        isEnded = "1";
    }




    protected PromotionAuto(Parcel in) {
        itemPic = in.readString();
        itemID = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        serverTime = in.readString();
        itemName = in.readString();
        itemPrice = in.readString();
        decrease = in.readString();
        subscription = in.readString();
        stock = in.readString();
        batteryLife = in.readString();
    }

    public static final Creator<PromotionAuto > CREATOR = new Creator<PromotionAuto >() {
        @Override
        public PromotionAuto createFromParcel(Parcel in) {
            return new PromotionAuto(in);
        }

        @Override
        public PromotionAuto[] newArray(int size) {
            return new PromotionAuto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemPic);
        dest.writeString(itemID);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(serverTime);
        dest.writeString(itemName);
        dest.writeString(itemPrice);
        dest.writeString(decrease);
        dest.writeString(subscription);
        dest.writeString(stock);
        dest.writeString(batteryLife);
    }
}
