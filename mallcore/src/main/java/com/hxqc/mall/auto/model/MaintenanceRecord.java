package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 18
 * FIXME
 * Todo 保养记录信息
 */
public class MaintenanceRecord implements Parcelable {

    public MyAuto myAuto;
    public NextMaintenance nextMaintenance;
    public ArrayList<Record> record;

    protected MaintenanceRecord(Parcel in) {
        myAuto = in.readParcelable(MyAuto.class.getClassLoader());
        nextMaintenance = in.readParcelable(NextMaintenance.class.getClassLoader());
        record = in.createTypedArrayList(Record.CREATOR);
    }

    public static final Creator<MaintenanceRecord> CREATOR = new Creator<MaintenanceRecord>() {
        @Override
        public MaintenanceRecord createFromParcel(Parcel in) {
            return new MaintenanceRecord(in);
        }

        @Override
        public MaintenanceRecord[] newArray(int size) {
            return new MaintenanceRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(myAuto, flags);
        dest.writeParcelable(nextMaintenance, flags);
        dest.writeTypedList(record);
    }
}
