package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:套餐中的用品
 *
 * author: 吕飞
 * since: 2015-10-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class Accessory implements Parcelable{
    @Expose
    public String accessoryID;//用品ID
    public String desc;//用品描述
    public String price;//用品价格
    public String title;//用品名称
    public ArrayList< AccessoryPhoto > photo;//图片数组
    @Expose
    public int count;//用品数量

    public float getSubtotal() {
        try {
            float p = 0;
            if (!TextUtils.isEmpty(this.price)) {
                p = Float.parseFloat(this.price);
            }
            return p*count;
        } catch (Exception e) {
            return 0;
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessoryID);
        dest.writeString(this.desc);
        dest.writeString(this.price);
        dest.writeString(this.title);
        dest.writeTypedList(photo);
        dest.writeInt(this.count);
    }

    public Accessory() {
    }

    protected Accessory(Parcel in) {
        this.accessoryID = in.readString();
        this.desc = in.readString();
        this.price = in.readString();
        this.title = in.readString();
        this.photo = in.createTypedArrayList(AccessoryPhoto.CREATOR);
        this.count = in.readInt();
    }

    public static final Creator< Accessory > CREATOR = new Creator< Accessory >() {
        public Accessory createFromParcel(Parcel source) {
            return new Accessory(source);
        }

        public Accessory[] newArray(int size) {
            return new Accessory[size];
        }
    };
}
