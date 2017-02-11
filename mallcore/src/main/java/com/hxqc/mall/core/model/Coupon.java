package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-02-24
 * FIXME
 * Todo 优惠券
 */
public class Coupon implements Parcelable {
    public String startDate;//有效开始时间
    public String endDate;//过期时间
    public String serverTime;//服务器时间
    public String couponID;//id
    public String statusCode;//优惠卷状态 10可使用|20过期|30已使用|40未开始(开始时间大于服务器时间)
    public String status;//优惠卷状态 10可使用|20过期|30已使用|40未开始(开始时间大于服务器时间)
    public String kindCode;//适用类型 10 保养
    public String kind;//适用类型 10 保养
    public String couponType;//10 现金 20 项目 30 单品
    public String name;//优惠卷名称 "免费更换机油一次"
    public String shopName;//适用店铺名称，多个店铺用中文逗号隔开
    public String price;//优惠卷金额
    //    public CouponRule rule;//使用规则
//    public String preferentialType;//1 满减 例如： 满100元减10元 2 赠送项目 3直接金额
    public String description;//y优惠说明
    public float usePrice;  //实际优惠金额 用于订单准备金额显示 number
    public int isChoose; //是否被选中 boolean

    public Coupon() {

    }

    public String getCouponType() {
        switch (couponType) {
            case "10":
                return "现金券";
            case "20":
                return "项目券";
            case "30":
                return "单品券";
            default:
                return "优惠券";
        }
    }


//    /**
//     * 获取优惠券的状态
//     *
//     * @return
//     */
//    public Status getStatus() {
//        if (TextUtils.isEmpty(status))
//            return null;
//        switch (status) {
//            case "10":
//                return Status.usable;
//            case "20":
//                return Status.expired;
//            case "30":
//                return Status.used;
//            case "40":
//                return Status.notstart;
//            default:
//                return null;
//        }
//
//    }


//
//    public enum Status {
//        usable, used, expired, notstart
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.serverTime);
        dest.writeString(this.couponID);
        dest.writeString(this.statusCode);
        dest.writeString(this.status);
        dest.writeString(this.kindCode);
        dest.writeString(this.kind);
        dest.writeString(this.couponType);
        dest.writeString(this.name);
        dest.writeString(this.shopName);
        dest.writeString(this.price);
        dest.writeString(this.description);
        dest.writeFloat(this.usePrice);
        dest.writeInt(this.isChoose);
    }

    protected Coupon(Parcel in) {
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.serverTime = in.readString();
        this.couponID = in.readString();
        this.statusCode = in.readString();
        this.status = in.readString();
        this.kindCode = in.readString();
        this.kind = in.readString();
        this.couponType = in.readString();
        this.name = in.readString();
        this.shopName = in.readString();
        this.price = in.readString();
        this.description = in.readString();
        this.usePrice = in.readFloat();
        this.isChoose = in.readInt();
    }

    public static final Parcelable.Creator<Coupon> CREATOR = new Parcelable.Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel source) {
            return new Coupon(source);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };
}
