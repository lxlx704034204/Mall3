package com.hxqc.pay.model;//package com.hxqc.pay.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
///**
// * Author: wanghao
// * Date: 2015-04-10
// * FIXME
// * 特卖订单补充
// */
//public class SeckillUpdateRequest implements Parcelable {
//
//    public String orderID;
//    public String fullname;
//    public String cellphone;
//    public String identifier;
//    public String province;
//    public String city;
//    public String district;
//    public String address;
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.orderID);
//        dest.writeString(this.fullname);
//        dest.writeString(this.cellphone);
//        dest.writeString(this.identifier);
//        dest.writeString(this.province);
//        dest.writeString(this.city);
//        dest.writeString(this.district);
//        dest.writeString(this.address);
//    }
//
//    public SeckillUpdateRequest() {
//    }
//
//    private SeckillUpdateRequest(Parcel in) {
//        this.orderID = in.readString();
//        this.fullname = in.readString();
//        this.cellphone = in.readString();
//        this.identifier = in.readString();
//        this.province = in.readString();
//        this.city = in.readString();
//        this.district = in.readString();
//        this.address = in.readString();
//    }
//
//    public static final Parcelable.Creator< SeckillUpdateRequest > CREATOR = new Parcelable.Creator< SeckillUpdateRequest >() {
//        public SeckillUpdateRequest createFromParcel(Parcel source) {
//            return new SeckillUpdateRequest(source);
//        }
//
//        public SeckillUpdateRequest[] newArray(int size) {
//            return new SeckillUpdateRequest[size];
//        }
//    };
//
//
//    @Override
//    public String toString() {
//        return "SeckillUpdateRequest{" +
//                "orderID='" + orderID + '\'' +
//                ", fullname='" + fullname + '\'' +
//                ", cellphone='" + cellphone + '\'' +
//                ", identifier='" + identifier + '\'' +
//                ", province='" + province + '\'' +
//                ", city='" + city + '\'' +
//                ", district='" + district + '\'' +
//                ", address='" + address + '\'' +
//                '}';
//    }
//}
