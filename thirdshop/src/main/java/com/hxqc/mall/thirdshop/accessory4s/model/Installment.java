package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 22
 * FIXME
 * Todo 分期计算
 */

public class Installment implements Parcelable {

//    price:    价格基数 number
//    downPaymentScale:    首付比例 number
//    month:    分期时间 number
//    installmentType:    分期计算方式 10万利率 20月利率 number
//    rateOfInterest:    利率 number
//    dataDirectoryType:    资料类型 与dataDirectory中dataDirectoryType对应 number
//    projectName:    方案名称 string
//    finalPrice:    尾款金额 number

    public int price;
    public int downPaymentScale;
    public int month;
    public int installmentType;
    public int rateOfInterest;
    public int dataDirectoryTyp;
    public String projectName;
    public int finalPrice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.price);
        dest.writeInt(this.downPaymentScale);
        dest.writeInt(this.month);
        dest.writeInt(this.installmentType);
        dest.writeInt(this.rateOfInterest);
        dest.writeInt(this.dataDirectoryTyp);
        dest.writeString(this.projectName);
        dest.writeInt(this.finalPrice);
    }

    public Installment() {
    }

    protected Installment(Parcel in) {
        this.price = in.readInt();
        this.downPaymentScale = in.readInt();
        this.month = in.readInt();
        this.installmentType = in.readInt();
        this.rateOfInterest = in.readInt();
        this.dataDirectoryTyp = in.readInt();
        this.projectName = in.readString();
        this.finalPrice = in.readInt();
    }

    public static final Parcelable.Creator<Installment> CREATOR = new Parcelable.Creator<Installment>() {
        @Override
        public Installment createFromParcel(Parcel source) {
            return new Installment(source);
        }

        @Override
        public Installment[] newArray(int size) {
            return new Installment[size];
        }
    };
}
