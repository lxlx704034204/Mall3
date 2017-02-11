package com.hxqc.newenergy.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月16日
 */
public class ModelAndSubsidy implements Parcelable {
    private final static String TAG = ModelAndSubsidy.class.getSimpleName();
    private Context mContext;

    public String autoName;//车辆名称
    public String autoID;//车辆ID
    public double price;//实际销售价
    public double origPrice;//厂家指导价
    public double subsidyCountry;//国家补贴
    public double subsidyManufacturer;//厂家补贴
    public double subsidyLocal;//地方补贴
    public double subsidyDealer;//恒信补贴
    public double subsidyTotal;//补贴总额
    public String batteryLife;//续航
    public String hasPurchaseTax;//是否含有购置税


    protected ModelAndSubsidy(Parcel in) {
        autoName = in.readString();
        autoID = in.readString();
        price = in.readDouble();
        origPrice = in.readDouble();
        subsidyCountry = in.readDouble();
        subsidyManufacturer = in.readDouble();
        subsidyLocal = in.readDouble();
        subsidyDealer = in.readDouble();
        subsidyTotal = in.readDouble();
        batteryLife = in.readString();
        hasPurchaseTax = in.readString();
    }


    public static final Creator< ModelAndSubsidy > CREATOR = new Creator< ModelAndSubsidy >() {
        @Override
        public ModelAndSubsidy createFromParcel(Parcel in) {
            return new ModelAndSubsidy(in);
        }


        @Override
        public ModelAndSubsidy[] newArray(int size) {
            return new ModelAndSubsidy[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(autoName);
        dest.writeString(autoID);
        dest.writeDouble(price);
        dest.writeDouble(origPrice);
        dest.writeDouble(subsidyCountry);
        dest.writeDouble(subsidyManufacturer);
        dest.writeDouble(subsidyLocal);
        dest.writeDouble(subsidyDealer);
        dest.writeDouble(subsidyTotal);
        dest.writeString(batteryLife);
        dest.writeString(hasPurchaseTax);
    }
}
