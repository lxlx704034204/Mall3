package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:特卖
 *
 * @author: 吕飞
 * @since: 2016-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class SingleSeckillItem implements Parcelable {
   public String itemID;//车辆ID
    public String shopID;//所卖的店铺ID
    public String shopTitle;//店铺简称
    public int isStarted;//特卖是否开始
    public int isEnded;//特卖是否结束
    public String serverTime;//服务器时间 时间戳
    public String startTime;//开始时间 时间戳
    public String endTime;//结束时间 时间戳
    public float subscription;//订金 单位 元
    public int store;//剩余库存
    public float itemPrice;//活动价格
    public String title;//活动标题
    public float itemFall;//降价
    public String itemName;//车辆名称
    public String itemThumb;//车辆缩略图
    public int salesArea;//销售范围 10全国 20本省
    public float itemOrigPrice;//厂家指导价

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemID);
        dest.writeString(this.shopID);
        dest.writeString(this.shopTitle);
        dest.writeInt(this.isStarted);
        dest.writeInt(this.isEnded);
        dest.writeString(this.serverTime);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeFloat(this.subscription);
        dest.writeInt(this.store);
        dest.writeFloat(this.itemPrice);
        dest.writeString(this.title);
        dest.writeFloat(this.itemFall);
        dest.writeString(this.itemName);
        dest.writeString(this.itemThumb);
        dest.writeInt(this.salesArea);
        dest.writeFloat(this.itemOrigPrice);
    }

    public SingleSeckillItem() {
    }

    protected SingleSeckillItem(Parcel in) {
        this.itemID = in.readString();
        this.shopID = in.readString();
        this.shopTitle = in.readString();
        this.isStarted = in.readInt();
        this.isEnded = in.readInt();
        this.serverTime = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.subscription = in.readFloat();
        this.store = in.readInt();
        this.itemPrice = in.readFloat();
        this.title = in.readString();
        this.itemFall = in.readFloat();
        this.itemName = in.readString();
        this.itemThumb = in.readString();
        this.salesArea = in.readInt();
        this.itemOrigPrice = in.readFloat();
    }

    public static final Parcelable.Creator<SingleSeckillItem> CREATOR = new Parcelable.Creator<SingleSeckillItem>() {
        @Override
        public SingleSeckillItem createFromParcel(Parcel source) {
            return new SingleSeckillItem(source);
        }

        @Override
        public SingleSeckillItem[] newArray(int size) {
            return new SingleSeckillItem[size];
        }
    };
}
