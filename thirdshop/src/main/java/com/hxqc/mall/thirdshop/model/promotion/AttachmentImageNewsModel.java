package com.hxqc.mall.thirdshop.model.promotion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-12-09
 * FIXME  咨询图片附件
 * Todo
 */
public class AttachmentImageNewsModel implements Parcelable {
    /**
     * 图片链接
     */
    public String url;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public AttachmentImageNewsModel() {
    }

    protected AttachmentImageNewsModel(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator< AttachmentImageNewsModel > CREATOR = new Parcelable.Creator< AttachmentImageNewsModel >() {
        public AttachmentImageNewsModel createFromParcel(Parcel source) {
            return new AttachmentImageNewsModel(source);
        }

        public AttachmentImageNewsModel[] newArray(int size) {
            return new AttachmentImageNewsModel[size];
        }
    };
}
