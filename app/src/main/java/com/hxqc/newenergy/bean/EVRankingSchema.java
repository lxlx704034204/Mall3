package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by CPR193 on 2016/3/9.
 */
public class EVRankingSchema implements Parcelable{


    public ArrayList<EVNewenergyAutoSample> attention;
    public ArrayList<EVNewenergyAutoSample> batteryLife;
    public ArrayList<EVNewenergyAutoSample> subsidy;


    protected EVRankingSchema(Parcel in) {
    }

    public static final Creator<EVRankingSchema > CREATOR = new Creator<EVRankingSchema >() {
        @Override
        public EVRankingSchema createFromParcel(Parcel in) {
            return new EVRankingSchema(in);
        }

        @Override
        public EVRankingSchema[] newArray(int size) {
            return new EVRankingSchema[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}

