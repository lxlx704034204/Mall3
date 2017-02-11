package com.hxqc.fastreqair.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author : 钟学东
 * @Since : 2016-05-18
 * FIXME
 * Todo
 */
public class CommentImage implements Parcelable {
    public String thumbImage;
    public String largeImage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbImage);
        dest.writeString(this.largeImage);
    }

    public CommentImage() {
    }

    protected CommentImage(Parcel in) {
        this.thumbImage = in.readString();
        this.largeImage = in.readString();
    }

    public static final Parcelable.Creator<CommentImage> CREATOR = new Parcelable.Creator<CommentImage>() {
        @Override
        public CommentImage createFromParcel(Parcel source) {
            return new CommentImage(source);
        }

        @Override
        public CommentImage[] newArray(int size) {
            return new CommentImage[size];
        }
    };
}
