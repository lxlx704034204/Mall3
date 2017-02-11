package com.hxqc.pay.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-04-08
 * FIXME
 * 支付请求
 */
public class PayOnlineRequest implements Parcelable {
    public String orderID;
    public String paymentID;
    public String money;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.paymentID);
        dest.writeString(this.money);
    }

    public PayOnlineRequest() {
    }

    private PayOnlineRequest(Parcel in) {
        this.orderID = in.readString();
        this.paymentID = in.readString();
        this.money = in.readString();
    }

    public static final Creator< PayOnlineRequest > CREATOR = new Creator< PayOnlineRequest >() {
        public PayOnlineRequest createFromParcel(Parcel source) {
            return new PayOnlineRequest(source);
        }

        public PayOnlineRequest[] newArray(int size) {
            return new PayOnlineRequest[size];
        }
    };
}
