package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * FIXME
 * Todo 车辆信息
 */

//@DatabaseTable(tableName = "db_auto")
public class MyAuto implements Parcelable {

    //    @DatabaseField(generatedId = true)
    private int id;
    @Expose
//    @DatabaseField(columnName = "myAutoID")
    public String myAutoID;//我的车辆ID string
    @Expose
//    @DatabaseField(columnName = "brand")
    public String brand;//品牌名称 string
    @Expose
    public String brandName;//厂家名字
    @Expose
//    @DatabaseField(columnName = "brandID")
    public String brandID;//品牌ID string
    @Expose
//    @DatabaseField(columnName = "brandThumb")
    public String brandThumb;//品牌图标 string
    @Expose
//    @DatabaseField(columnName = "series")
    public String series;//车系名称 string
    @Expose
//    @DatabaseField(columnName = "seriesID")
    public String seriesID;//车系ID string
    @Expose
//    @DatabaseField(columnName = "autoModel")
    public String autoModel;//车型名称 string
    @Expose
//    @DatabaseField(columnName = "autoModelID")
    public String autoModelID;//车型ID string
    @Expose
//    @DatabaseField(columnName = "plateNumber")
    public String plateNumber;//车牌号 (鄂A58945) string
    @Expose
//    @DatabaseField(columnName = "VIN")
    public String VIN;//VIN码 string
    @Expose
//    @DatabaseField(columnName = "drivingDistance")
    public String drivingDistance;//行驶里程 单位(千米) number
    @Expose
//    @DatabaseField(columnName = "ownName")
    public String ownName;//车主姓名 string
    @Expose
//    @DatabaseField(columnName = "ownPhone")
    public String ownPhone;//车主手机号 string
    @Expose
//    @DatabaseField(columnName = "lastMaintenancDate")
    public String lastMaintenanceDate;//上次维修时间 string
    @Expose
//    @DatabaseField(columnName = "isDefault")
    public String isDefault;//是否默认 20是 10不是 number
    @Expose
//    @DatabaseField(columnName = "guaranteePeriod")
    public String guaranteePeriod;//质保期 y-m-n string
    @Expose
//    @DatabaseField(columnName = "nextMaintenanceDistance")
    public long nextMaintenanceDistance;//下次保养公里数
    @Expose
//    @DatabaseField(columnName = "nextInsurance")
    public long nextInsurance;//下次保险
    @Expose
//    @DatabaseField(columnName = "lastMaintenanceDistance")
    public long lastMaintenanceDistance;//上次行驶里程
    @Expose
//    @DatabaseField(columnName = "registerTime")
    public String registerTime;//注册日期
    @Expose
//    @DatabaseField(columnName = "engineNum")
    public String engineNum;//发动机号
    @Expose
//    @DatabaseField(columnName = "score")
    public String score;//积分
    @Expose
//    @DatabaseField(columnName = "Level")
    public String Level;//等级
    @Expose
//    @DatabaseField(columnName = "LevelText")
    public String LevelText;//等级名称
    @Expose
//    @DatabaseField(columnName = "LevelIcon")
    public String LevelIcon;//等级图标
    @Expose
//    @DatabaseField(columnName = "couponCount")
    public String couponCount;//优惠卷
    @Expose
//    @DatabaseField(columnName = "localAuto")
    public String localAuto;
    @Expose
//    @DatabaseField(columnName = "nextMaintenancDate")
    public String nextMaintenanceDate;//下次保养时间
    @Expose
//    @DatabaseField(columnName = "expirationOfPolicy")
    public String expirationOfPolicy;//保单到期日期
    @Expose
//    @DatabaseField(columnName = "examineDate")
    public String examineDate;//年审到期时间
    @Expose
    public int authenticated;//是否认证

    public MyAuto() {
    }

    protected MyAuto(Parcel in) {
        id = in.readInt();
        myAutoID = in.readString();
        brand = in.readString();
        brandName = in.readString();
        brandID = in.readString();
        brandThumb = in.readString();
        series = in.readString();
        seriesID = in.readString();
        autoModel = in.readString();
        autoModelID = in.readString();
        plateNumber = in.readString();
        VIN = in.readString();
        drivingDistance = in.readString();
        ownName = in.readString();
        ownPhone = in.readString();
        lastMaintenanceDate = in.readString();
        isDefault = in.readString();
        guaranteePeriod = in.readString();
        nextMaintenanceDistance = in.readLong();
        nextInsurance = in.readLong();
        lastMaintenanceDistance = in.readLong();
        registerTime = in.readString();
        engineNum = in.readString();
        score = in.readString();
        Level = in.readString();
        LevelText = in.readString();
        LevelIcon = in.readString();
        couponCount = in.readString();
        localAuto = in.readString();
        nextMaintenanceDate = in.readString();
        expirationOfPolicy = in.readString();
        examineDate = in.readString();
        authenticated = in.readInt();
    }

    public static final Creator<MyAuto> CREATOR = new Creator<MyAuto>() {
        @Override
        public MyAuto createFromParcel(Parcel in) {
            return new MyAuto(in);
        }

        @Override
        public MyAuto[] newArray(int size) {
            return new MyAuto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMyAutoID() {
        return myAutoID;
    }

    public void setMyAutoID(String myAutoID) {
        this.myAutoID = myAutoID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getBrandThumb() {
        return brandThumb;
    }

    public void setBrandThumb(String brandThumb) {
        this.brandThumb = brandThumb;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public String getAutoModel() {
        return autoModel;
    }

    public void setAutoModel(String autoModel) {
        this.autoModel = autoModel;
    }

    public String getAutoModelID() {
        return autoModelID;
    }

    public void setAutoModelID(String autoModelID) {
        this.autoModelID = autoModelID;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getDrivingDistance() {
        return drivingDistance;
    }

    public void setDrivingDistance(String drivingDistance) {
        this.drivingDistance = drivingDistance;
    }

    public String getOwnName() {
        return ownName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }

    public String getOwnPhone() {
        return ownPhone;
    }

    public void setOwnPhone(String ownPhone) {
        this.ownPhone = ownPhone;
    }

    public String getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(String lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getGuaranteePeriod() {
        return guaranteePeriod;
    }

    public void setGuaranteePeriod(String guaranteePeriod) {
        this.guaranteePeriod = guaranteePeriod;
    }

    public long getNextMaintenanceDistance() {
        return nextMaintenanceDistance;
    }

    public void setNextMaintenanceDistance(long nextMaintenanceDistance) {
        this.nextMaintenanceDistance = nextMaintenanceDistance;
    }

    public long getNextInsurance() {
        return nextInsurance;
    }

    public void setNextInsurance(long nextInsurance) {
        this.nextInsurance = nextInsurance;
    }

    public long getLastMaintenanceDistance() {
        return lastMaintenanceDistance;
    }

    public void setLastMaintenanceDistance(long lastMaintenanceDistance) {
        this.lastMaintenanceDistance = lastMaintenanceDistance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(myAutoID);
        dest.writeString(brand);
        dest.writeString(brandName);
        dest.writeString(brandID);
        dest.writeString(brandThumb);
        dest.writeString(series);
        dest.writeString(seriesID);
        dest.writeString(autoModel);
        dest.writeString(autoModelID);
        dest.writeString(plateNumber);
        dest.writeString(VIN);
        dest.writeString(drivingDistance);
        dest.writeString(ownName);
        dest.writeString(ownPhone);
        dest.writeString(lastMaintenanceDate);
        dest.writeString(isDefault);
        dest.writeString(guaranteePeriod);
        dest.writeLong(nextMaintenanceDistance);
        dest.writeLong(nextInsurance);
        dest.writeLong(lastMaintenanceDistance);
        dest.writeString(registerTime);
        dest.writeString(engineNum);
        dest.writeString(score);
        dest.writeString(Level);
        dest.writeString(LevelText);
        dest.writeString(LevelIcon);
        dest.writeString(couponCount);
        dest.writeString(localAuto);
        dest.writeString(nextMaintenanceDate);
        dest.writeString(expirationOfPolicy);
        dest.writeString(examineDate);
        dest.writeInt(authenticated);
    }

    @Override
    public String toString() {
        return "MyAuto{" +
                "id=" + id +
                ", myAutoID='" + myAutoID + '\'' +
                ", brand='" + brand + '\'' +
                ", brandID='" + brandID + '\'' +
                ", brandThumb='" + brandThumb + '\'' +
                ", series='" + series + '\'' +
                ", seriesID='" + seriesID + '\'' +
                ", autoModel='" + autoModel + '\'' +
                ", autoModelID='" + autoModelID + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", VIN='" + VIN + '\'' +
                ", drivingDistance='" + drivingDistance + '\'' +
                ", ownName='" + ownName + '\'' +
                ", ownPhone='" + ownPhone + '\'' +
                ", lastMaintenanceDate='" + lastMaintenanceDate + '\'' +
                ", isDefault='" + isDefault + '\'' +
                ", guaranteePeriod='" + guaranteePeriod + '\'' +
                ", nextMaintenanceDistance=" + nextMaintenanceDistance +
                ", nextInsurance=" + nextInsurance +
                ", lastMaintenanceDistance=" + lastMaintenanceDistance +
                ", registerTime='" + registerTime + '\'' +
                ", engineNum='" + engineNum + '\'' +
                ", score='" + score + '\'' +
                ", Level='" + Level + '\'' +
                ", LevelText='" + LevelText + '\'' +
                ", LevelIcon='" + LevelIcon + '\'' +
                ", couponCount='" + couponCount + '\'' +
                ", localAuto='" + localAuto + '\'' +
                ", nextMaintenanceDate='" + nextMaintenanceDate + '\'' +
                ", expirationOfPolicy='" + expirationOfPolicy + '\'' +
                ", examineDate='" + examineDate + '\'' +
                ", authenticated=" + authenticated +
                ", brandName=" + brandName +
                '}';
    }
}
