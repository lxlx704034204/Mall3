package com.hxqc.mall.core.model.advisory;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-05-11
 * FIXME
 * Todo 咨询问题
 */
public class Question implements Parcelable {
    public String QUser;
    public String QAutoID;
    public String QAutoName;
    public String QContent;
    public String QTime;//提问时间 Y-m-d string

    protected Question(Parcel in) {
        QUser = in.readString();
        QAutoID = in.readString();
        QAutoName = in.readString();
        QContent = in.readString();
        QTime = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(QUser);
        dest.writeString(QAutoID);
        dest.writeString(QAutoName);
        dest.writeString(QContent);
        dest.writeString(QTime);
    }
}
