package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-04-17
 * FIXME
 * 图集
 */
public class AtlasModel implements Parcelable {

    public String largeURL;
    public String thumbURL;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.largeURL);
        dest.writeString(this.thumbURL);
    }

    public AtlasModel() {
    }

    private AtlasModel(Parcel in) {
        this.largeURL = in.readString();
        this.thumbURL = in.readString();
    }

    public static final Parcelable.Creator< AtlasModel > CREATOR = new Parcelable.Creator< AtlasModel >() {
        public AtlasModel createFromParcel(Parcel source) {
            return new AtlasModel(source);
        }

        public AtlasModel[] newArray(int size) {
            return new AtlasModel[size];
        }
    };
}
