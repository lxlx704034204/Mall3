package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图集
 * Created by huangyi on 15/10/23.
 */
public class Image implements Parcelable {

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String path; //大图
    public String small_path; //小图
    public String title;

    private Image(Parcel in) {
        path = in.readString();
        small_path = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(path);
        out.writeString(small_path);
        out.writeString(title);
    }

}
