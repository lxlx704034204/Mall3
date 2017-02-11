package com.hxqc.mall.core.model.advisory;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-05-11
 * FIXME
 * Todo 咨询 列表
 */
public class Advisory implements Parcelable {
    public Question question;
    public Answer answer;

    protected Advisory(Parcel in) {
        question = (Question) in.readParcelable(Question.class.getClassLoader());
        answer = (Answer) in.readParcelable(Answer.class.getClassLoader());
    }

    public static final Creator<Advisory> CREATOR = new Creator<Advisory>() {
        @Override
        public Advisory createFromParcel(Parcel in) {
            return new Advisory(in);
        }

        @Override
        public Advisory[] newArray(int size) {
            return new Advisory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(question,flags);
        dest.writeParcelable(answer,flags);
    }
}
