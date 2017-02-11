package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * FIXME
 * Todo
 */
public class RMMyAuto implements Parcelable{

    public String myAutoID; //我的车辆ID string
    public String brand; //品牌名称 string
    public String brandID; //品牌ID string
    public String brandThumb; //品牌图标 string
    public String series; //车系名称 string
    public String seriesID; //车系ID string
    public String autoModel; //车型名称 string
    public String autoModelID; //车型ID string
    public String plateNumber; //车牌号 (鄂A58945) string
    public String vin; //VIN码 string
    public String drivingDistance; //行驶里程 单位(千米) number
    public String ownName; //车主姓名 string
    public String ownPhone; //车主手机号 string
    public String lastMaintenanceDistance; //上次保养行驶距离 number
    public String lastMaintenanceDate; //上次保养时间 yyyy.MM.dd string
    public String nextMaintenanceDistance; //距离下次保养公里数 number
    public String nextInsurance; //距离下次购买保险天数 (如果没有保险数据返回-1，保险过期返回0) number
    public String isDefault; //是否默认 0不是 1是 number
    public String guaranteePeriod; //质保期 y-m-n string

    protected RMMyAuto(Parcel in) {
        myAutoID = in.readString();
        brand = in.readString();
        brandID = in.readString();
        brandThumb = in.readString();
        series = in.readString();
        seriesID = in.readString();
        autoModel = in.readString();
        autoModelID = in.readString();
        plateNumber = in.readString();
        vin = in.readString();
        drivingDistance = in.readString();
        ownName = in.readString();
        ownPhone = in.readString();
        lastMaintenanceDistance = in.readString();
        lastMaintenanceDate = in.readString();
        nextMaintenanceDistance = in.readString();
        nextInsurance = in.readString();
        isDefault = in.readString();
        guaranteePeriod = in.readString();
    }

    public static final Creator<RMMyAuto> CREATOR = new Creator<RMMyAuto>() {
        @Override
        public RMMyAuto createFromParcel(Parcel in) {
            return new RMMyAuto(in);
        }

        @Override
        public RMMyAuto[] newArray(int size) {
            return new RMMyAuto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(myAutoID);
        dest.writeString(brand);
        dest.writeString(brandID);
        dest.writeString(brandThumb);
        dest.writeString(series);
        dest.writeString(seriesID);
        dest.writeString(autoModel);
        dest.writeString(autoModelID);
        dest.writeString(plateNumber);
        dest.writeString(vin);
        dest.writeString(drivingDistance);
        dest.writeString(ownName);
        dest.writeString(ownPhone);
        dest.writeString(lastMaintenanceDistance);
        dest.writeString(lastMaintenanceDate);
        dest.writeString(nextMaintenanceDistance);
        dest.writeString(nextInsurance);
        dest.writeString(isDefault);
        dest.writeString(guaranteePeriod);
    }
}
