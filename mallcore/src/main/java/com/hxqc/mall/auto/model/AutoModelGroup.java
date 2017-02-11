package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo 车型组
 */
public class AutoModelGroup implements Parcelable {

    @Expose
    public String seriesID;//    车系ID string
    @Expose
    public String seriesName;
    @Expose
    public ArrayList<AutoModel> autoModelGroup;
    @Expose
    public String year;// 年份 string
    @Expose
    public ArrayList<AutoModel> autoModel;

    public AutoModelGroup() {
    }

    public AutoModelGroup(String seriesID, String seriesName, ArrayList<AutoModel> autoModelGroup) {
        this.seriesID = seriesID;
        this.seriesName = seriesName;
        this.autoModelGroup = autoModelGroup;
    }

    protected AutoModelGroup(Parcel in) {
        seriesID = in.readString();
        seriesName = in.readString();
        autoModelGroup = in.createTypedArrayList(AutoModel.CREATOR);
        year = in.readString();
        autoModel = in.createTypedArrayList(AutoModel.CREATOR);
    }

    public static final Creator<AutoModelGroup> CREATOR = new Creator<AutoModelGroup>() {
        @Override
        public AutoModelGroup createFromParcel(Parcel in) {
            return new AutoModelGroup(in);
        }

        @Override
        public AutoModelGroup[] newArray(int size) {
            return new AutoModelGroup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seriesID);
        dest.writeString(seriesName);
        dest.writeTypedList(autoModelGroup);
        dest.writeString(year);
        dest.writeTypedList(autoModel);
    }
}
