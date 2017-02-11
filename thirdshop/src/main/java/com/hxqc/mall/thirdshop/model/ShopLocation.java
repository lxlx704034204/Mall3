//package com.hxqc.mall.core.model.thirdpartshop;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.text.TextUtils;
//
//
///**
// * Author:李烽
// * Date:2015-12-02
// * FIXME
// * Todo 店铺地理位置
// */
//public class ShopLocation implements Parcelable {
//    public String name;
//    public String address;
//    public String tel;
//    public String latitude;//经度
//    public String longitude;//纬度
//
//    public double getLongitude() {
//        try {
//            if (TextUtils.isEmpty(longitude)) {
//                return 0;
//            } else {
//                return Double.parseDouble(longitude);
//            }
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    public double getLatitude() {
//        try {
//            if (TextUtils.isEmpty(latitude)) {
//                return 0;
//            } else {
//                return Double.parseDouble(latitude);
//            }
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    public String getTel() {
//        return tel;
//    }
//
//
//    public String getAddress() {
//        return address;
//    }
//
//
//    public String getName() {
//        return name;
//    }
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.name);
//        dest.writeString(this.address);
//        dest.writeString(this.tel);
//        dest.writeString(this.latitude);
//        dest.writeString(this.longitude);
//    }
//
//    public ShopLocation() {
//    }
//
//    protected ShopLocation(Parcel in) {
//        this.name = in.readString();
//        this.address = in.readString();
//        this.tel = in.readString();
//        this.latitude = in.readString();
//        this.longitude = in.readString();
//    }
//
//    public static final Parcelable.Creator< ShopLocation > CREATOR = new Parcelable.Creator< ShopLocation >() {
//        public ShopLocation createFromParcel(Parcel source) {
//            return new ShopLocation(source);
//        }
//
//        public ShopLocation[] newArray(int size) {
//            return new ShopLocation[size];
//        }
//    };
//}
