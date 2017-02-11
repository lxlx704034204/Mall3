package com.hxqc.mall.core.model.auto;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;


import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.OtherUtil;

import com.hxqc.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 说明:促销特卖基类
 *
 * author: 吕飞
 * since: 2015-05-07
 * Copyright:恒信汽车电子商务有限公司
 */
public class BasePromotion implements Parcelable {
    public static final int TO_START = 0;//即将开始
    public static final int STARTED = 1;//进行中
    public static final int ENDED = 2;//已结束
    public String promotionID;
    public String subscription;//订金
    public String serverTime;//服务器时间
    public String startTime;//开始时间
    public String endTime;//结束时间
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
            return context.getString(R.string.promotion_is_end);
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.promotionID);
        dest.writeString(this.subscription);
        dest.writeString(this.serverTime);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.isEnded);
        dest.writeString(this.isStarted);
        dest.writeInt(this.status);
        dest.writeLong(this.serverL);
        dest.writeLong(this.endL);
        dest.writeLong(this.startL);
    }

    public BasePromotion() {
    }

    protected BasePromotion(Parcel in) {
        this.promotionID = in.readString();
        this.subscription = in.readString();
        this.serverTime = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.isEnded = in.readString();
        this.isStarted = in.readString();
        this.status = in.readInt();
        this.serverL = in.readLong();
        this.endL = in.readLong();
        this.startL = in.readLong();
    }

    public static final Creator< BasePromotion > CREATOR = new Creator< BasePromotion >() {
        public BasePromotion createFromParcel(Parcel source) {
            return new BasePromotion(source);
        }

        public BasePromotion[] newArray(int size) {
            return new BasePromotion[size];
        }
    };
}
