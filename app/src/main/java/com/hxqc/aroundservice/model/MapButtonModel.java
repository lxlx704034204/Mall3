package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:  wh
 * Date:  2016/4/18
 * FIXME  地图按钮 Model
 * Todo
 */
public class MapButtonModel implements Parcelable {

    final  public static String BTN_ID_GAS = "gas";
    final  public static String BTN_ID_PARK = "park";
    final  public static String BTN_ID_CHARGER = "charger";
    final  public static String BTN_ID_test= "test";

    public String buttonTag;
    public String buttonName;
    public int buttonImgSrc;
    public boolean isChoose = false;//是否被选择

    public MapButtonModel(int buttonImgSrc,String btnTag) {
        this.buttonImgSrc = buttonImgSrc;
        this.buttonTag = btnTag;
    }

    @Override
    public String toString() {
        return "MapButtonModel{" +
                "buttonTag='" + buttonTag + '\'' +
                ", buttonName='" + buttonName + '\'' +
                ", buttonImgSrc=" + buttonImgSrc +
                ", isChoose=" + isChoose +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.buttonTag);
        dest.writeString(this.buttonName);
        dest.writeInt(this.buttonImgSrc);
        dest.writeByte(isChoose ? (byte) 1 : (byte) 0);
    }

    protected MapButtonModel(Parcel in) {
        this.buttonTag = in.readString();
        this.buttonName = in.readString();
        this.buttonImgSrc = in.readInt();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<MapButtonModel> CREATOR = new Creator<MapButtonModel>() {
        @Override
        public MapButtonModel createFromParcel(Parcel source) {
            return new MapButtonModel(source);
        }

        @Override
        public MapButtonModel[] newArray(int size) {
            return new MapButtonModel[size];
        }
    };
}
