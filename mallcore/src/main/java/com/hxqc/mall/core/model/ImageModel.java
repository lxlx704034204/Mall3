package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author: wanghao
 * Date: 2015-03-23
 * FIXME
 * 图片 url
 */
public class ImageModel implements Parcelable, Serializable {
    //全图 url
    public String largeImage;
    //缩略图 url
    public String thumbImage;
    //网络图片地址
    public String url;

    public ImageModel() {
    }

    public ImageModel(String largeImage, String thumbImage) {
        this.largeImage = largeImage;
        this.thumbImage = thumbImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.largeImage);
        dest.writeString(this.thumbImage);
        dest.writeString(this.url);

    }

    private ImageModel(Parcel in) {
        this.largeImage = in.readString();
        this.thumbImage = in.readString();
        this.url = in.readString();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        public ImageModel createFromParcel(Parcel source) {
            return new ImageModel(source);
        }

        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

}
