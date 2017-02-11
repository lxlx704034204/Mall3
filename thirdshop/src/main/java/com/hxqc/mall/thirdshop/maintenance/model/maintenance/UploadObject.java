package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-03-17
 * FIXME
 * Todo
 */
public class UploadObject implements Parcelable {

    @Expose
    public ArrayList<String> packages;

    @Expose
    public ArrayList<UploadItem> item;

    @Expose
    public ArrayList<String> itemGroupID;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.packages);
        dest.writeTypedList(item);
        dest.writeStringList(this.itemGroupID);
    }

    public UploadObject() {
    }

    protected UploadObject(Parcel in) {
        this.packages = in.createStringArrayList();
        this.item = in.createTypedArrayList(UploadItem.CREATOR);
        this.itemGroupID = in.createStringArrayList();
    }

    public static final Parcelable.Creator<UploadObject> CREATOR = new Parcelable.Creator<UploadObject>() {
        public UploadObject createFromParcel(Parcel source) {
            return new UploadObject(source);
        }

        public UploadObject[] newArray(int size) {
            return new UploadObject[size];
        }
    };
}
