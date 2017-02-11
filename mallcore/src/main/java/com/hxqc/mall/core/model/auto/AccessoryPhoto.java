package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:套餐中用品图片
 * <p/>
 * author: 吕飞
 * since: 2015-10-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class AccessoryPhoto implements Parcelable {
    public String large;//大图
    public String thumb;//缩略图
    public String mini;//最小


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.large);
        dest.writeString(this.thumb);
        dest.writeString(this.mini);
    }

    public AccessoryPhoto() {
    }

    protected AccessoryPhoto(Parcel in) {
        this.large = in.readString();
        this.thumb = in.readString();
        this.mini = in.readString();
    }

    public static final Creator< AccessoryPhoto > CREATOR = new Creator< AccessoryPhoto >() {
        public AccessoryPhoto createFromParcel(Parcel source) {
            return new AccessoryPhoto(source);
        }

        public AccessoryPhoto[] newArray(int size) {
            return new AccessoryPhoto[size];
        }
    };
}
