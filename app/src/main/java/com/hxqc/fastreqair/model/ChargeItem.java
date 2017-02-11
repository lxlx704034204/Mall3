package com.hxqc.fastreqair.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function: 洗车收费小项实体Bean
 *
 * @author 袁秉勇
 * @since 2016年05月18日
 */
public class ChargeItem implements Parcelable {
    public String name;

    public double amount;

    public String chargeID;


    protected ChargeItem(Parcel in) {
        name = in.readString();
        amount = in.readDouble();
        chargeID = in.readString();
    }


    public static final Creator< ChargeItem > CREATOR = new Creator< ChargeItem >() {
        @Override
        public ChargeItem createFromParcel(Parcel in) {
            return new ChargeItem(in);
        }


        @Override
        public ChargeItem[] newArray(int size) {
            return new ChargeItem[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(amount);
        dest.writeString(chargeID);
    }
}
