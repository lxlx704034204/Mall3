package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * 说明:筛选条件
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class Filter implements Parcelable {
    @Expose
    public String filterKey;//与服务器交互时，筛选条件key
    @Expose
    public String filterValue;//与服务器交互时，筛选条件选项值
    @Expose
    public String label;//选项名称

    public Filter() {
    }

    protected Filter(Parcel in) {
        filterKey = in.readString();
        filterValue = in.readString();
        label = in.readString();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    public String getLabel() {
        return label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filterKey);
        dest.writeString(filterValue);
        dest.writeString(label);
    }
}
