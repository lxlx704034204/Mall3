package com.hxqc.mall.qr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:  wh
 * Date:  2016/11/14
 * FIXME
 * Todo  线下扫码工单信息
 */

public class OffLineWorkOrderQRModel implements Parcelable {

    public String erpCode;
    public String orderID;
    public String workOrderID;
    public String orderAmount;
    public String amountPayable;
    public String shopName;
    public String shopPhotoL;

    @Override
    public String toString() {
        return "OffLineWorkOrderQRModel{" +
                "erpCode='" + erpCode + '\'' +
                ", orderID='" + orderID + '\'' +
                ", workOrderID='" + workOrderID + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", amountPayable='" + amountPayable + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopPhotoL='" + shopPhotoL + '\'' +
                '}';
    }

    public OffLineWorkOrderQRModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.erpCode);
        dest.writeString(this.orderID);
        dest.writeString(this.workOrderID);
        dest.writeString(this.orderAmount);
        dest.writeString(this.amountPayable);
        dest.writeString(this.shopName);
        dest.writeString(this.shopPhotoL);
    }

    protected OffLineWorkOrderQRModel(Parcel in) {
        this.erpCode = in.readString();
        this.orderID = in.readString();
        this.workOrderID = in.readString();
        this.orderAmount = in.readString();
        this.amountPayable = in.readString();
        this.shopName = in.readString();
        this.shopPhotoL = in.readString();
    }

    public static final Parcelable.Creator<OffLineWorkOrderQRModel> CREATOR = new Parcelable.Creator<OffLineWorkOrderQRModel>() {
        @Override
        public OffLineWorkOrderQRModel createFromParcel(Parcel source) {
            return new OffLineWorkOrderQRModel(source);
        }

        @Override
        public OffLineWorkOrderQRModel[] newArray(int size) {
            return new OffLineWorkOrderQRModel[size];
        }
    };
}
