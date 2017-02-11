package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wanghao
 * Date: 2015-12-02
 * FIXME   第三方店铺 车辆信息 基本信息
 * Todo
 */
public class AutoDetailThirdShop extends AutoBaseInfoThirdShop implements Parcelable {


    public float itemTotalPrice;//参考总价

    public ArrayList< Appearance > appearance;//车身颜色

    public String extID;//车型id

    public String introduce;//图文介绍

    public String promotionEnd;//活动结束时间

    public String itemGift;

//    public float getItemPrice() {
//        return itemPrice;
//    }
//    public float getItemOrigPrice() {
//        return itemOrigPrice;
//    }

    public String getStringItemPrice(){
        return "¥" +OtherUtil.amountFormat(itemPrice);
    }



    public String getStringItemOrigPrice(){
        return "¥" +OtherUtil.amountFormat(itemOrigPrice);
    }

    public float getItemTotalPrice() {
        return itemTotalPrice;
    }

    public boolean getIsInPromotion() {
        return OtherUtil.int2Boolean(isInPromotion);
    }

    public AutoDetailThirdShop() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.itemTotalPrice);
        dest.writeList(this.appearance);
        dest.writeString(this.extID);
        dest.writeString(this.introduce);
        dest.writeString(this.promotionEnd);
        dest.writeString(this.itemGift);
    }

    protected AutoDetailThirdShop(Parcel in) {
        super(in);
        this.itemTotalPrice = in.readFloat();
        this.appearance = new ArrayList<Appearance>();
        in.readList(this.appearance, List.class.getClassLoader());
        this.extID = in.readString();
        this.introduce = in.readString();
        this.promotionEnd = in.readString();
        this.itemGift = in.readString();
    }

    public static final Creator<AutoDetailThirdShop> CREATOR = new Creator<AutoDetailThirdShop>() {
        public AutoDetailThirdShop createFromParcel(Parcel source) {
            return new AutoDetailThirdShop(source);
        }

        public AutoDetailThirdShop[] newArray(int size) {
            return new AutoDetailThirdShop[size];
        }
    };
}
