package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 15
 * FIXME
 * Todo 下次保养信息
 */
public class NextMaintenance implements Parcelable {

    public String nextMaintenanceDistance;//距离下次保养公里数
    public String maintenanceItemsTotal;//保养项目名称合计


    protected NextMaintenance(Parcel in) {
        nextMaintenanceDistance = in.readString();
        maintenanceItemsTotal = in.readString();
    }

    public static final Creator<NextMaintenance> CREATOR = new Creator<NextMaintenance>() {
        @Override
        public NextMaintenance createFromParcel(Parcel in) {
            return new NextMaintenance(in);
        }

        @Override
        public NextMaintenance[] newArray(int size) {
            return new NextMaintenance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nextMaintenanceDistance);
        dest.writeString(maintenanceItemsTotal);
    }
}
