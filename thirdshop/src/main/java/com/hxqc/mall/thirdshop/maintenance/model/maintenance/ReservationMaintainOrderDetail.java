package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * FIXME
 * Todo
 */
public class ReservationMaintainOrderDetail implements Parcelable{

    private String orderID; //订单ID string
    private String serviceType; //服务类型 string
    private String apppintmentDate; //预约时间 string
    private String name; //联系人 string
    private String phone; //联系人手机号 string
    private String autoModel; //车型名称 string
    private String serviceAdviserName; //服务顾问名称 string
    private String mechanicName;//维修技师名称 string
    private ShopPoint shopPoint; //店铺地点 { }
    private String workOrderID; //维修工单号 string

    protected ReservationMaintainOrderDetail(Parcel in) {
        orderID = in.readString();
        serviceType = in.readString();
        apppintmentDate = in.readString();
        name = in.readString();
        phone = in.readString();
        autoModel = in.readString();
        serviceAdviserName = in.readString();
        mechanicName = in.readString();
        shopPoint = in.readParcelable(ShopPoint.class.getClassLoader());
        workOrderID = in.readString();
    }

    public static final Creator<ReservationMaintainOrderDetail> CREATOR = new Creator<ReservationMaintainOrderDetail>() {
        @Override
        public ReservationMaintainOrderDetail createFromParcel(Parcel in) {
            return new ReservationMaintainOrderDetail(in);
        }

        @Override
        public ReservationMaintainOrderDetail[] newArray(int size) {
            return new ReservationMaintainOrderDetail[size];
        }
    };

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getApppintmentDate() {
        return apppintmentDate;
    }

    public void setApppintmentDate(String apppintmentDate) {
        this.apppintmentDate = apppintmentDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAutoModel() {
        return autoModel;
    }

    public void setAutoModel(String autoModel) {
        this.autoModel = autoModel;
    }

    public String getServiceAdviserName() {
        return serviceAdviserName;
    }

    public void setServiceAdviserName(String serviceAdviserName) {
        this.serviceAdviserName = serviceAdviserName;
    }

    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
        this.mechanicName = mechanicName;
    }

    public ShopPoint getShopPoint() {
        return shopPoint;
    }

    public void setShopPoint(ShopPoint shopPoint) {
        this.shopPoint = shopPoint;
    }

    public String getWorkOrderID() {
        return workOrderID;
    }

    public void setWorkOrderID(String workOrderID) {
        this.workOrderID = workOrderID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderID);
        dest.writeString(serviceType);
        dest.writeString(apppintmentDate);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(autoModel);
        dest.writeString(serviceAdviserName);
        dest.writeString(mechanicName);
        dest.writeParcelable(shopPoint, flags);
        dest.writeString(workOrderID);
    }
}
