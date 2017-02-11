package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月21日
 */
public class FilterItem implements Parcelable {
    public String label;
    public String picUrl;
    public String filterKey;
    public String filterValue;


    public FilterItem(String label) {
        this.label = label;
    }


    protected FilterItem(Parcel in) {
        label = in.readString();
        picUrl = in.readString();
        filterKey = in.readString();
        filterValue = in.readString();
    }


    public static final Creator< FilterItem > CREATOR = new Creator< FilterItem >() {
        @Override
        public FilterItem createFromParcel(Parcel in) {
            return new FilterItem(in);
        }


        @Override
        public FilterItem[] newArray(int size) {
            return new FilterItem[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(picUrl);
        dest.writeString(filterKey);
        dest.writeString(filterValue);
    }
}
