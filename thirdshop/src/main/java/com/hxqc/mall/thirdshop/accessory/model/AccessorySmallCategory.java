package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月23日
 */
public class AccessorySmallCategory implements Parcelable {
    public static final Creator< AccessorySmallCategory > CREATOR = new Creator< AccessorySmallCategory >() {
        @Override
        public AccessorySmallCategory createFromParcel(Parcel in) {
            return new AccessorySmallCategory(in);
        }


        @Override
        public AccessorySmallCategory[] newArray(int size) {
            return new AccessorySmallCategory[size];
        }
    };
    private final static String TAG = AccessorySmallCategory.class.getSimpleName();
    public String class2ndName;
    public String class2ndID;
    public boolean isChecked = false; //当前是否选中 hy


    protected AccessorySmallCategory(Parcel in) {
        class2ndName = in.readString();
        class2ndID = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(class2ndName);
        dest.writeString(class2ndID);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }


    @Override
    public String toString() {
        return "AccessorySmallCategory{" +
                "class2ndName='" + class2ndName + '\'' +
                ", class2ndID='" + class2ndID + '\'' +
                '}';
    }
}
