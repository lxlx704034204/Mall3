package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * FIXME
 * Todo 服务类型
 */
public class ServiceType implements Parcelable{

    public String thumb; //类型图片 string
    public String kindTitle; //类型种类名称 （维修，保养，钣喷，检查故障） string
    public String serviceType; //服务类型 string
    public String remark;//其他原因
    public ArrayList<ServiceItem> items;

    public ServiceType() {
    }

    protected ServiceType(Parcel in) {
        thumb = in.readString();
        kindTitle = in.readString();
        serviceType = in.readString();
        remark = in.readString();
        items = in.createTypedArrayList(ServiceItem.CREATOR);
    }

    public static final Creator<ServiceType> CREATOR = new Creator<ServiceType>() {
        @Override
        public ServiceType createFromParcel(Parcel in) {
            return new ServiceType(in);
        }

        @Override
        public ServiceType[] newArray(int size) {
            return new ServiceType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumb);
        dest.writeString(kindTitle);
        dest.writeString(serviceType);
        dest.writeString(remark);
        dest.writeTypedList(items);
    }

    @Override
    public String toString() {
        return "ServiceType{" +
                "img='" + thumb + '\'' +
                ", kindTitle='" + kindTitle + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", remark='" + remark + '\'' +
                ", items=" + items +
                '}';
    }
}
