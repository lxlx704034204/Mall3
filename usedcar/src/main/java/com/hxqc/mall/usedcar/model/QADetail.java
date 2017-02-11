package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 质保 项目明细
 * Created by huangyi on 16/6/18.
 */
public class QADetail implements Parcelable {

    public static final Parcelable.Creator<QADetail> CREATOR = new Parcelable.Creator<QADetail>() {
        public QADetail createFromParcel(Parcel in) {
            return new QADetail(in);
        }

        public QADetail[] newArray(int size) {
            return new QADetail[size];
        }
    };

    public String value;
    public String key;

    public QADetail() {
    }

    public QADetail(String value, String key) {
        this.value = value;
        this.key = key;
    }

    private QADetail(Parcel in) {
        value = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(value);
        out.writeString(key);
    }

}
