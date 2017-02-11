package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月23日
 */
public class AccessoryBigCategory implements Parcelable {
    public static final Creator< AccessoryBigCategory > CREATOR = new Creator< AccessoryBigCategory >() {
        @Override
        public AccessoryBigCategory createFromParcel(Parcel in) {
            return new AccessoryBigCategory(in);
        }


        @Override
        public AccessoryBigCategory[] newArray(int size) {
            return new AccessoryBigCategory[size];
        }
    };
    public String class1stIcon;
    public String class1stName;
    public String class1stID;
    public ArrayList<AccessorySmallCategory> class2nd;
    public boolean isChecked = false; //当前是否选中 hy


    public AccessoryBigCategory() {
    }


    public AccessoryBigCategory(String class1stName, String class1stID, ArrayList< AccessorySmallCategory > class2nd) {
        this.class1stName = class1stName;
        this.class1stID = class1stID;
        this.class2nd = class2nd;
    }


    protected AccessoryBigCategory(Parcel in) {
        class1stIcon = in.readString();
        class1stName = in.readString();
        class1stID = in.readString();
        class2nd = in.createTypedArrayList(AccessorySmallCategory.CREATOR);
        this.isChecked = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(class1stIcon);
        dest.writeString(class1stName);
        dest.writeString(class1stID);
        dest.writeTypedList(class2nd);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }


//    @Override
//    public String toString() {
//        return "AccessoryBigCategory{" +
//                "class1stIcon='" + class1stIcon + '\'' +
//                ", class1stName='" + class1stName + '\'' +
//                ", class1stID='" + class1stID + '\'' +
//                ", class2nd=" + class2nd.get(0) +
//                '}';
//    }
}
