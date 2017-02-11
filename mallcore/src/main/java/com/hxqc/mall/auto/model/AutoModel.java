package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo 车型
 */
public class AutoModel implements Parcelable {

    @Expose
    public String autoModel;    //车型名称 string
    @Expose
    public String autoModelID;
    @Expose
    public String autoModelThumb;
    @Expose
    public String modelYear;

    public AutoModel(String autoModel, String autoModelID) {
        this.autoModel = autoModel;
        this.autoModelID = autoModelID;
    }

    protected AutoModel(Parcel in) {
        autoModel = in.readString();
        autoModelID = in.readString();
        autoModelThumb = in.readString();
        modelYear = in.readString();
    }

    public static final Creator<AutoModel> CREATOR = new Creator<AutoModel>() {
        @Override
        public AutoModel createFromParcel(Parcel in) {
            return new AutoModel(in);
        }

        @Override
        public AutoModel[] newArray(int size) {
            return new AutoModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(autoModel);
        dest.writeString(autoModelID);
        dest.writeString(autoModelThumb);
        dest.writeString(modelYear);
    }

    @Override
    public String toString() {
        return "AutoModel{" +
                "autoModel='" + autoModel + '\'' +
                ", autoModelID='" + autoModelID + '\'' +
                ", autoModelThumb='" + autoModelThumb + '\'' +
                ", modelYear='" + modelYear + '\'' +
                '}';
    }
}