package com.hxqc.mall.paymethodlibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-04-08
 * FIXME
 * Todo
 */
public class AlipayResponse implements Parcelable {
    public String paymentID;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentID);
        dest.writeString(this.url);
    }

    public AlipayResponse() {
    }

    private AlipayResponse(Parcel in) {
        this.paymentID = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator< AlipayResponse > CREATOR = new Parcelable.Creator< AlipayResponse >() {
        public AlipayResponse createFromParcel(Parcel source) {
            return new AlipayResponse(source);
        }

        public AlipayResponse[] newArray(int size) {
            return new AlipayResponse[size];
        }
    };
}
