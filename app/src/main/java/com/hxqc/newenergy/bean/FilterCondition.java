package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月21日
 */
public class FilterCondition implements Parcelable {
    public String filterLabel;
    public ArrayList<FilterItem> filterItem;


    protected FilterCondition(Parcel in) {
        filterLabel = in.readString();
    }


    public static final Creator< FilterCondition > CREATOR = new Creator< FilterCondition >() {
        @Override
        public FilterCondition createFromParcel(Parcel in) {
            return new FilterCondition(in);
        }


        @Override
        public FilterCondition[] newArray(int size) {
            return new FilterCondition[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filterLabel);
    }
}
