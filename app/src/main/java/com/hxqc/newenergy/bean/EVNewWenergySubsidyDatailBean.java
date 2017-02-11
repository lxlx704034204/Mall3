package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/21.
 * Copyright:恒信汽车电子商务有限公司
 */
public class EVNewWenergySubsidyDatailBean implements Parcelable{

    public  String autoName;//车辆名称
    public  String autoID;//车辆ID
    public  float price;//实际销售价
    public  float origPrice;//厂家指导价
    public  float subsidyCountry;//国家补贴
    public  float subsidyManufacturer;//厂家补贴
    public  float subsidyLocal;//地方补贴
    public  float subsidyDealer;//恒信补贴
    public  float subsidyTotal;//补贴总额
    public  String batteryLife;//续航
    public  String hasPurchaseTax;//是否含有购置税
    public  String area;//地区
    public  String localPolicy;//地方政策
    public  String modelType;//车型分类
    public  String modelSize;//尺寸
    public  String power;//功率
    public  String torque;//扭矩
    public  String fastChargingTime;//快充时间
    public  String slowChargingTime;//慢充时间
    public  String maximumSpeed;//最高时速
    public  String batteryType;//电池类型
    public  String batteryID;//电池ID
    public  String batteryURL;//电池URL
    public  String batteryCapacity;//电池容量
    public  String chargingInHome;//是否家冲
    public  String guaranteePeriod;//整车质保
    public  String maintenancePeriod;//保养周期


    protected EVNewWenergySubsidyDatailBean(Parcel in) {
        autoName = in.readString();
        autoID = in.readString();
        price = in.readFloat();
        origPrice = in.readFloat();
        subsidyCountry = in.readFloat();
        subsidyManufacturer = in.readFloat();
        subsidyLocal = in.readFloat();
        subsidyDealer = in.readFloat();
        subsidyTotal = in.readFloat();
        batteryLife = in.readString();
        hasPurchaseTax = in.readString();
        area = in.readString();
        localPolicy = in.readString();
        modelType = in.readString();
        modelSize = in.readString();
        power = in.readString();
        torque = in.readString();
        fastChargingTime = in.readString();
        slowChargingTime = in.readString();
        maximumSpeed = in.readString();
        batteryType = in.readString();
        batteryID = in.readString();
        batteryCapacity = in.readString();
        chargingInHome = in.readString();
        guaranteePeriod = in.readString();
        maintenancePeriod = in.readString();
        batteryURL = in.readString();
    }

    public static EVNewWenergySubsidyDatailBean objectFromData(String str) {

        return new Gson().fromJson(str, EVNewWenergySubsidyDatailBean.class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(autoName);
        dest.writeString(autoID);
        dest.writeFloat(price);
        dest.writeFloat(origPrice);
        dest.writeFloat(subsidyCountry);
        dest.writeFloat(subsidyManufacturer);
        dest.writeFloat(subsidyLocal);
        dest.writeFloat(subsidyDealer);
        dest.writeFloat(subsidyTotal);
        dest.writeString(batteryLife);
        dest.writeString(hasPurchaseTax);
        dest.writeString(area);
        dest.writeString(localPolicy);
        dest.writeString(modelType);
        dest.writeString(modelSize);
        dest.writeString(power);
        dest.writeString(torque);
        dest.writeString(fastChargingTime);
        dest.writeString(slowChargingTime);
        dest.writeString(maximumSpeed);
        dest.writeString(batteryType);
        dest.writeString(batteryID);
        dest.writeString(batteryCapacity);
        dest.writeString(chargingInHome);
        dest.writeString(guaranteePeriod);
        dest.writeString(batteryURL);
        dest.writeString(maintenancePeriod);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EVNewWenergySubsidyDatailBean> CREATOR = new Creator<EVNewWenergySubsidyDatailBean>() {
        @Override
        public EVNewWenergySubsidyDatailBean createFromParcel(Parcel in) {
            return new EVNewWenergySubsidyDatailBean(in);
        }

        @Override
        public EVNewWenergySubsidyDatailBean[] newArray(int size) {
            return new EVNewWenergySubsidyDatailBean[size];
        }
    };
}
