package com.hxqc.autonews.model.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author:李烽
 * Date:2016-09-29
 * FIXME
 * Todo 汽车资讯标签
 */

public class InfoTag  implements Parcelable {
    @Expose
    public String tagName;
    @Expose
    public String tagCode;



    protected InfoTag(Parcel in) {
        tagName = in.readString();
        tagCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tagName);
        dest.writeString(tagCode);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InfoTag> CREATOR = new Creator<InfoTag>() {
        @Override
        public InfoTag createFromParcel(Parcel in) {
            return new InfoTag(in);
        }

        @Override
        public InfoTag[] newArray(int size) {
            return new InfoTag[size];
        }
    };
}
