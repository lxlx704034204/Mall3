package com.hxqc.pay.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-04-02
 * FIXME
 * 当前订单 已经支付 未支付 总额
 */
public class PaidAmountModel implements Parcelable {

    public String amount;//总额
    public String paid;//已付款金额
    public String unpaid;//未付款金额
    /**
     * 2015.9.28
     */
    public String subscription;//订金金额

    @Override
    public String toString() {
        return "PaidAmountModel{" +
                "amount='" + amount + '\'' +
                ", paid='" + paid + '\'' +
                ", unpaid='" + unpaid + '\'' +
                ", subscription='" + subscription + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amount);
        dest.writeString(this.paid);
        dest.writeString(this.unpaid);
        dest.writeString(this.subscription);
    }

    public PaidAmountModel() {
    }

    protected PaidAmountModel(Parcel in) {
        this.amount = in.readString();
        this.paid = in.readString();
        this.unpaid = in.readString();
        this.subscription = in.readString();
    }

    public static final Creator< PaidAmountModel > CREATOR = new Creator< PaidAmountModel >() {
        public PaidAmountModel createFromParcel(Parcel source) {
            return new PaidAmountModel(source);
        }

        public PaidAmountModel[] newArray(int size) {
            return new PaidAmountModel[size];
        }
    };
}
