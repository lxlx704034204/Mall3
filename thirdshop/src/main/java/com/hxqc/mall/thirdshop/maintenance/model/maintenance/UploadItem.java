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
public class UploadItem implements Parcelable {

    @Expose
    public String itemID;

    @Expose
    public ArrayList<String> goodsID;

    public boolean isInPackage = false ;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemID);
        dest.writeStringList(this.goodsID);
        dest.writeByte(isInPackage ? (byte) 1 : (byte) 0);
    }

    public UploadItem() {
    }

    protected UploadItem(Parcel in) {
        this.itemID = in.readString();
        this.goodsID = in.createStringArrayList();
        this.isInPackage = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UploadItem> CREATOR = new Parcelable.Creator<UploadItem>() {
        public UploadItem createFromParcel(Parcel source) {
            return new UploadItem(source);
        }

        public UploadItem[] newArray(int size) {
            return new UploadItem[size];
        }
    };
}
