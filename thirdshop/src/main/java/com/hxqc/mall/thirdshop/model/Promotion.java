package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 最新促销活动
 */
@Deprecated
public class Promotion implements Parcelable {
    public String promotionID;
    public String title;//标题
    public String summary;//简介
    public String shopTitle;//店铺简称
    public String thumb;
    public String publishDate;//发布时间
    public String serverTime;//服务器时间
    public String startDate;//活动开始时间
    public String endDate;//结束时间
    public String status;//活动状态（10:未发布 20:发布 30:下线） string
    public int newsType;//讯息类型（10：促销 其他：新闻）
    private float paymentAvailable;////是否可付款 20不可付  10可付

    public String getEndDate() {
        return endDate;
    }

    public String getEndDateByTime() {
        return endDate + " 23:59:59";
    }

    public boolean getPaymentAvailable() {
        return paymentAvailable == 10;
    }

    public void setPaymentAvailable(float paymentAvailable) {
        this.paymentAvailable = paymentAvailable;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "img='" + thumb + '\'' +
                ", title='" + title + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", summary='" + summary + '\'' +
                ", promotionID='" + promotionID + '\'' +
                ", shopTitle='" + shopTitle + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.promotionID);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.shopTitle);
        dest.writeString(this.thumb);
        dest.writeString(this.publishDate);
        dest.writeString(this.serverTime);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.status);
        dest.writeInt(this.newsType);
        dest.writeFloat(this.paymentAvailable);
    }

    public Promotion() {
    }

    protected Promotion(Parcel in) {
        this.promotionID = in.readString();
        this.title = in.readString();
        this.summary = in.readString();
        this.shopTitle = in.readString();
        this.thumb = in.readString();
        this.publishDate = in.readString();
        this.serverTime = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.status = in.readString();
        this.newsType = in.readInt();
        this.paymentAvailable = in.readFloat();
    }

    public static final Parcelable.Creator<Promotion> CREATOR = new Parcelable.Creator<Promotion>() {
        @Override
        public Promotion createFromParcel(Parcel source) {
            return new Promotion(source);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };
}
