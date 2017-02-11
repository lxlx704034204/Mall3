package com.hxqc.mall.thirdshop.maintenance.model.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author : 钟学东
 * @Since : 2016-03-16
 * FIXME
 * Todo
 */
public class CreateOrder implements Parcelable {

    public String orderID;
    public float amount;
    public float orderAmount;
    public String paymentType;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeFloat(this.amount);
        dest.writeFloat(this.orderAmount);
        dest.writeString(this.paymentType);
    }

    public CreateOrder() {
    }

    protected CreateOrder(Parcel in) {
        this.orderID = in.readString();
        this.amount = in.readFloat();
        this.orderAmount = in.readFloat();
        this.paymentType = in.readString();
    }

    public static final Creator<CreateOrder> CREATOR = new Creator<CreateOrder>() {
        @Override
        public CreateOrder createFromParcel(Parcel source) {
            return new CreateOrder(source);
        }

        @Override
        public CreateOrder[] newArray(int size) {
            return new CreateOrder[size];
        }
    };
}
