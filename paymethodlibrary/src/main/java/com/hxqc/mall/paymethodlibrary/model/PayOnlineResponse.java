package com.hxqc.mall.paymethodlibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-04-03
 * FIXME
 * 返回支付链接
 */
public class PayOnlineResponse implements Parcelable {
    public String paymentID;
    public String url;
    public String tradeID;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentID);
        dest.writeString(this.url);
    }

    public PayOnlineResponse() {
    }

    private PayOnlineResponse(Parcel in) {
        this.paymentID = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator< PayOnlineResponse > CREATOR = new Parcelable.Creator< PayOnlineResponse >() {
        public PayOnlineResponse createFromParcel(Parcel source) {
            return new PayOnlineResponse(source);
        }

        public PayOnlineResponse[] newArray(int size) {
            return new PayOnlineResponse[size];
        }
    };

    @Override
    public String toString() {
        return "PayOnlineResponse{" +
                "paymentID='" + paymentID + '\'' +
                ", url='" + url + '\'' +
                ", tradeID='" + tradeID + '\'' +
                '}';
    }
}
