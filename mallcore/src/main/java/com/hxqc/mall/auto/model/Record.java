package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 18
 * FIXME
 * Todo 保养记录model
 */
public class Record implements Parcelable {

    public String amount;//保养金额
    public String distance;//保养公里数
    public String maintenanceItem;//保养项目,多个项目用都好分割
    public String maintenancDate;//保养公里数
    public String shopID;//店铺ID
    public String shopName;//保养店铺名称
    public String workOrderID;//工单号
    public String erpShopCode;//erp的店铺Code，用于查看工单详情

    protected Record(Parcel in) {
        amount = in.readString();
        distance = in.readString();
        maintenanceItem = in.readString();
        maintenancDate = in.readString();
        shopID = in.readString();
        shopName = in.readString();
        workOrderID = in.readString();
        erpShopCode = in.readString();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeString(distance);
        dest.writeString(maintenanceItem);
        dest.writeString(maintenancDate);
        dest.writeString(shopID);
        dest.writeString(shopName);
        dest.writeString(workOrderID);
        dest.writeString(erpShopCode);
    }
}
