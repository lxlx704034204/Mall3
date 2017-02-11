package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo （号牌）种类编号查询
 */
public class SpeciesNumber implements Parcelable{

    public static final Creator<SpeciesNumber> CREATOR = new Creator<SpeciesNumber>() {
        @Override
        public SpeciesNumber createFromParcel(Parcel in) {
            return new SpeciesNumber(in);
        }

        @Override
        public SpeciesNumber[] newArray(int size) {
            return new SpeciesNumber[size];
        }
    };
    public String error_code;
    public String reason;
    public ArrayList<AutoSpeciesType> result;

    protected SpeciesNumber(Parcel in) {
        error_code = in.readString();
        reason = in.readString();
        result = in.createTypedArrayList(AutoSpeciesType.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(error_code);
        dest.writeString(reason);
        dest.writeTypedList(result);
    }

    @Override
    public String toString() {
        return "SpeciesNumber{" +
                "error_code='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
