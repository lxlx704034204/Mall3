package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-05-15
 * FIXME
 * Todo
 */
public class ActiveEventJSR implements Parcelable {

    public String itemID;
    public String type;
    public String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemID);
        dest.writeString(this.type);
        dest.writeString(this.title);
    }

    public ActiveEventJSR() {
    }

    protected ActiveEventJSR(Parcel in) {
        this.itemID = in.readString();
        this.type = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<ActiveEventJSR> CREATOR = new Parcelable.Creator<ActiveEventJSR>() {
        public ActiveEventJSR createFromParcel(Parcel source) {
            return new ActiveEventJSR(source);
        }

        public ActiveEventJSR[] newArray(int size) {
            return new ActiveEventJSR[size];
        }
    };
}
