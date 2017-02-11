package com.hxqc.mall.drivingexam.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 选项
 * Created by zhaofan on 2016/8/1.
 */
public class Options implements Parcelable {
    public Long id;
    public String content;   //选项内容
    public String choose;

    public Options(String content, String choose) {
        this.content = content;
        this.choose = choose;
    }


    @Override
    public String toString() {
        return "Options{" +
                "content='" + content + '\'' +
                ", choose='" + choose + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.content);
        dest.writeString(this.choose);
    }

    protected Options(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.content = in.readString();
        this.choose = in.readString();
    }

    public static final Creator<Options> CREATOR = new Creator<Options>() {
        @Override
        public Options createFromParcel(Parcel source) {
            return new Options(source);
        }

        @Override
        public Options[] newArray(int size) {
            return new Options[size];
        }
    };
}
