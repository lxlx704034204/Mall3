package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 22
 * FIXME
 * Todo 分期购车车型
 */

public class InstallmentBuyingModel implements Parcelable {

    public Series serie;
    public ArrayList<InstallmentBuyingSeries> models;


    protected InstallmentBuyingModel(Parcel in) {
        serie = in.readParcelable(Series.class.getClassLoader());
        models = in.createTypedArrayList(InstallmentBuyingSeries.CREATOR);
    }

    public static final Creator<InstallmentBuyingModel> CREATOR = new Creator<InstallmentBuyingModel>() {
        @Override
        public InstallmentBuyingModel createFromParcel(Parcel in) {
            return new InstallmentBuyingModel(in);
        }

        @Override
        public InstallmentBuyingModel[] newArray(int size) {
            return new InstallmentBuyingModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(serie, flags);
        dest.writeTypedList(models);
    }
}
