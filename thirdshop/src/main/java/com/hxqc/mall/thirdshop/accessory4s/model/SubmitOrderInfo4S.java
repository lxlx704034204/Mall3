package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:提交订单的信息
 *
 * @author: 吕飞
 * @since: 2016-02-29
 * Copyright:恒信汽车电子商务有限公司
 */
public class SubmitOrderInfo4S implements Parcelable {
    public String orderID;
    public String amount;

    public SubmitOrderInfo4S(String orderID, String amount) {
        this.orderID = orderID;
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.amount);
    }

    public SubmitOrderInfo4S() {
    }

    protected SubmitOrderInfo4S(Parcel in) {
        this.orderID = in.readString();
        this.amount = in.readString();
    }

    public static final Parcelable.Creator<SubmitOrderInfo4S> CREATOR = new Parcelable.Creator<SubmitOrderInfo4S>() {
        @Override
        public SubmitOrderInfo4S createFromParcel(Parcel source) {
            return new SubmitOrderInfo4S(source);
        }

        @Override
        public SubmitOrderInfo4S[] newArray(int size) {
            return new SubmitOrderInfo4S[size];
        }
    };
}
