package com.hxqc.autonews.model.pojos;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.hxqc.autonews.widget.Gallery;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 图片（汽车资讯部分）
 */
public class AutoImage implements Parcelable {
    @Expose
    public String largeURL;
    @Expose
    public String description;

    public AutoImage() {
    }

    protected AutoImage(Parcel in) {
        largeURL = in.readString();
        description = in.readString();
    }

    public static final Creator<AutoImage> CREATOR = new Creator<AutoImage>() {
        @Override
        public AutoImage createFromParcel(Parcel in) {
            return new AutoImage(in);
        }

        @Override
        public AutoImage[] newArray(int size) {
            return new AutoImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(largeURL);
        dest.writeString(description);
    }

    public Gallery.Data toGalleryData(Context context, @Nullable String title) {
        Gallery.Data galleryData = new Gallery(context).new Data();
        if (!TextUtils.isEmpty(title)) {
            galleryData.title = title;
        } else {
            galleryData.title = "";
        }
        galleryData.picUrl = this.largeURL;
        galleryData.info = this.description;
        return galleryData;
    }
}
