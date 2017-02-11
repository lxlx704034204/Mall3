package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 26
 * FIXME
 * Todo 付款方式
 */
public class Payment implements Parcelable {

    /**
     * title : 名称 string
     * paymentID : 付款方式 'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'OFFLINE'线下 string
     * thumbImage : 图标 string
     */

    public String title;
    public String paymentID;
    public String thumb;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.paymentID);
        dest.writeString(this.thumb);
    }

    public Payment() {
    }

    protected Payment(Parcel in) {
        this.title = in.readString();
        this.paymentID = in.readString();
        this.thumb = in.readString();
    }

    public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel source) {
            return new Payment(source);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };
}
