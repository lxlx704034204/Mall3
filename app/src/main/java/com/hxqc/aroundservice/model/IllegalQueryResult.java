package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo 违章结果
 */
public class IllegalQueryResult implements Parcelable{

    public static final Creator<IllegalQueryResult> CREATOR = new Creator<IllegalQueryResult>() {
        @Override
        public IllegalQueryResult createFromParcel(Parcel in) {
            return new IllegalQueryResult(in);
        }

        @Override
        public IllegalQueryResult[] newArray(int size) {
            return new IllegalQueryResult[size];
        }
    };
    public String error_code;
    public String reason;
    public ProvinceAndCity result;

    protected IllegalQueryResult(Parcel in) {
        error_code = in.readString();
        reason = in.readString();
        result = in.readParcelable(ProvinceAndCity.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(error_code);
        dest.writeString(reason);
        dest.writeParcelable(result, flags);
    }
}
