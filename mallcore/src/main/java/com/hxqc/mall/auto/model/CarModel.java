package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo
 */
@Deprecated
public class CarModel extends Filter implements Parcelable {
    public String modelName;

    protected CarModel(Parcel in) {
        modelName = in.readString();
    }

    public static final Creator<CarModel> CREATOR = new Creator<CarModel>() {
        @Override
        public CarModel createFromParcel(Parcel source) {
            return new CarModel(source);
        }

        @Override
        public CarModel[] newArray(int size) {
            return new CarModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modelName);
    }
}
