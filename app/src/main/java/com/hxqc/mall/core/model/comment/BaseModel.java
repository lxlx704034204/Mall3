package com.hxqc.mall.core.model.comment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-03-23
 * FIXME
 * Todo
 */
public class BaseModel implements Parcelable {

    public int code = -1;

    public String message;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.code);
        dest.writeString(this.message);

    }

    public BaseModel() {
    }

    private BaseModel(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
    }

    public static final Creator< BaseModel > CREATOR = new Creator< BaseModel >() {
        public BaseModel createFromParcel(Parcel source) {
            return new BaseModel(source);
        }

        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
}
