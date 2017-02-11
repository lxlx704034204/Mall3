package com.hxqc.mall.thirdshop.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2017 - 02 - 07
 * Des:
 * FIXME
 * TODO
 */

public class ApppintmentDateNew implements Parcelable {

//    time:    时间 string
//    enable:    是否可选 boolean

    public String time;
    public int enable;

    public ApppintmentDateNew(String time, int enable) {
        this.time = time;
        this.enable = enable;
    }

    protected ApppintmentDateNew(Parcel in) {
        time = in.readString();
        enable = in.readInt();
    }

    public static final Creator<ApppintmentDateNew> CREATOR = new Creator<ApppintmentDateNew>() {
        @Override
        public ApppintmentDateNew createFromParcel(Parcel in) {
            return new ApppintmentDateNew(in);
        }

        @Override
        public ApppintmentDateNew[] newArray(int size) {
            return new ApppintmentDateNew[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeInt(enable);
    }

    @Override
    public String toString() {
        return "ApppintmentDateNew{" +
                "time='" + time + '\'' +
                ", enable=" + enable +
                '}';
    }
}
