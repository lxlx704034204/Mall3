package com.hxqc.mall.core.model.advisory;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-05-11
 * FIXME
 * Todo 咨询回复
 */
public class Answer implements Parcelable {
    public String AUser;
    public String AContent;
    public String ATime;//回答时间 Y-m-d string

    protected Answer(Parcel in) {
        AUser = in.readString();
        AContent = in.readString();
        ATime = in.readString();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AUser);
        dest.writeString(AContent);
        dest.writeString(ATime);
    }
}
