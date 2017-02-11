package com.hxqc.pay.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-03-31
 * FIXME
 * 订单id
 */
public class OrderIDResponse implements Parcelable {
    public String orderID;
    public String expiredTime;
    public String serverTime;

    @Override
    public String toString() {
        return "OrderIDResponse{" +
                "orderID='" + orderID + '\'' +
                ", expiredTime='" + expiredTime + '\'' +
                ", serverTime='" + serverTime + '\'' +
                '}';
    }

    public OrderIDResponse(String orderID, String expiredTime, String serverTime) {
        this.orderID = orderID;
        this.expiredTime = expiredTime;
        this.serverTime = serverTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.expiredTime);
        dest.writeString(this.serverTime);
    }

    public OrderIDResponse() {
    }

    private OrderIDResponse(Parcel in) {
        this.orderID = in.readString();
        this.expiredTime = in.readString();
        this.serverTime = in.readString();
    }

    public static final Creator< OrderIDResponse > CREATOR = new Creator< OrderIDResponse >() {
        public OrderIDResponse createFromParcel(Parcel source) {
            return new OrderIDResponse(source);
        }

        public OrderIDResponse[] newArray(int size) {
            return new OrderIDResponse[size];
        }
    };
}
