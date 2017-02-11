package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 26
 * FIXME
 * Todo 违章处理预估价格
 */
public class EstimatePrice implements Parcelable {

    /**
     * fen : 违章总分number
     * fine : 罚款金额number
     * serviceCharge : 服务费number
     * count : 违章条数number
     * amount : 预估总计number
     */

   public String fen;
   public String fine;
   public String serviceCharge;
   public String count;
   public String amount;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fen);
        dest.writeString(this.fine);
        dest.writeString(this.serviceCharge);
        dest.writeString(this.count);
        dest.writeString(this.amount);
    }

    public EstimatePrice() {
    }

    protected EstimatePrice(Parcel in) {
        this.fen = in.readString();
        this.fine = in.readString();
        this.serviceCharge = in.readString();
        this.count = in.readString();
        this.amount = in.readString();
    }

    public static final Parcelable.Creator<EstimatePrice> CREATOR = new Parcelable.Creator<EstimatePrice>() {
        @Override
        public EstimatePrice createFromParcel(Parcel source) {
            return new EstimatePrice(source);
        }

        @Override
        public EstimatePrice[] newArray(int size) {
            return new EstimatePrice[size];
        }
    };
}
