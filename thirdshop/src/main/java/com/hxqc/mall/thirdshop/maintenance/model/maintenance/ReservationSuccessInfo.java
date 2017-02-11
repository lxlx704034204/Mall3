package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 25
 * FIXME
 * Todo
 */
public class ReservationSuccessInfo implements Parcelable {

    public String plateNumber; //车牌号
    public String autoModel; //车型
    public String autoModelID; //车型ID
    public String drivingDistance;//行驶里程
    public String name;//联系人姓名
    public String phone;//联系手机号
    public String shopID;//门店ID
    public String shop;//门店
    public String shopType;//店铺类型
    public String apppintmentDate;//预约时间
    public String serviceType;//服务类型
    public String serviceTypeID;//服务类型ID
    public String serviceAdviserID;//服务顾问ID
    public String mechanicID;//维修技师ID
    public String serviceAdviser;//服务顾问
    public String mechanic;//维修技师
    public String VIN;//VIN码
    public String remark;

    public ReservationSuccessInfo() {
    }

    protected ReservationSuccessInfo(Parcel in) {
        plateNumber = in.readString();
        autoModel = in.readString();
        autoModelID = in.readString();
        drivingDistance = in.readString();
        name = in.readString();
        phone = in.readString();
        shopID = in.readString();
        shop = in.readString();
        apppintmentDate = in.readString();
        serviceType = in.readString();
        serviceTypeID = in.readString();
        serviceAdviserID = in.readString();
        mechanicID = in.readString();
        VIN = in.readString();
        serviceAdviser = in.readString();
        mechanic = in.readString();
        shopType = in.readString();
        remark = in.readString();
    }

    public static final Creator<ReservationSuccessInfo> CREATOR = new Creator<ReservationSuccessInfo>() {
        @Override
        public ReservationSuccessInfo createFromParcel(Parcel in) {
            return new ReservationSuccessInfo(in);
        }

        @Override
        public ReservationSuccessInfo[] newArray(int size) {
            return new ReservationSuccessInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(plateNumber);
        dest.writeString(autoModel);
        dest.writeString(autoModelID);
        dest.writeString(drivingDistance);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(shopID);
        dest.writeString(shop);
        dest.writeString(apppintmentDate);
        dest.writeString(serviceType);
        dest.writeString(serviceTypeID);
        dest.writeString(serviceAdviserID);
        dest.writeString(mechanicID);
        dest.writeString(VIN);
        dest.writeString(serviceAdviser);
        dest.writeString(mechanic);
        dest.writeString(shopType);
        dest.writeString(remark);
    }

    @Override
    public String toString() {
        return "ReservationSuccessInfo{" +
                "plateNumber='" + plateNumber + '\'' +
                ", autoModel='" + autoModel + '\'' +
                ", autoModelID='" + autoModelID + '\'' +
                ", drivingDistance='" + drivingDistance + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", shopID='" + shopID + '\'' +
                ", shop='" + shop + '\'' +
                ", shopType='" + shopType + '\'' +
                ", apppintmentDate='" + apppintmentDate + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceTypeID='" + serviceTypeID + '\'' +
                ", serviceAdviserID='" + serviceAdviserID + '\'' +
                ", mechanicID='" + mechanicID + '\'' +
                ", serviceAdviser='" + serviceAdviser + '\'' +
                ", mechanic='" + mechanic + '\'' +
                ", VIN='" + VIN + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
