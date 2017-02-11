package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * FIXME
 * Todo 服务类型子项
 */
public class ServiceItem implements Parcelable {

    public String thumb; //项目图片 string
    public String itemName; //项目名称 string
    public String itemID; //项目ID string
    public String itemComment;
    public String url;

    public ServiceItem() {
    }

    protected ServiceItem(Parcel in) {
        thumb = in.readString();
        itemName = in.readString();
        itemID = in.readString();
        itemComment = in.readString();
        url = in.readString();
    }

    public static final Creator<ServiceItem> CREATOR = new Creator<ServiceItem>() {
        @Override
        public ServiceItem createFromParcel(Parcel in) {
            return new ServiceItem(in);
        }

        @Override
        public ServiceItem[] newArray(int size) {
            return new ServiceItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumb);
        dest.writeString(itemName);
        dest.writeString(itemID);
        dest.writeString(itemComment);
        dest.writeString(url);
    }
}
