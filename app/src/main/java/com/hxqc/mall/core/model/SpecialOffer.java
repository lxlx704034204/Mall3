package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.text.TextUtils;

import com.hxqc.mall.core.model.auto.BasePromotion;
import com.hxqc.mall.core.util.OtherUtil;

/**
 * 说明:特卖
 * <p/>
 * author: 吕飞
 * since: 2015-05-07
 * Copyright:恒信汽车电子商务有限公司
 */
public class SpecialOffer extends BasePromotion {
    public String itemID;
    public String decrease;//降幅
    public String itemPic;//特卖图片
    public String itemPrice;//特卖价
    public String stock;//库存
    private String itemName;//特卖名称

    public String getItemFall() {

        if (TextUtils.isEmpty(decrease)) {
            return "0元";
        } else {
            return OtherUtil.amountFormat(decrease);
        }
    }

    public String getInventory() {
        if (stock.equals("0")) {
            return "已抢光";
        } else {
            return stock + "辆";
        }
    }

    public String getItemDescription() {
        return itemName;
    }

    public String getItemPrice() {
        return OtherUtil.amountFormat(itemPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.itemID);
        dest.writeString(this.decrease);
        dest.writeString(this.itemPic);
        dest.writeString(this.itemPrice);
        dest.writeString(this.stock);
        dest.writeString(this.itemName);
    }

    public SpecialOffer() {
    }

    protected SpecialOffer(Parcel in) {
        super(in);
        this.itemID = in.readString();
        this.decrease = in.readString();
        this.itemPic = in.readString();
        this.itemPrice = in.readString();
        this.stock = in.readString();
        this.itemName = in.readString();
    }

    public static final Creator< SpecialOffer > CREATOR = new Creator< SpecialOffer >() {
        public SpecialOffer createFromParcel(Parcel source) {
            return new SpecialOffer(source);
        }

        public SpecialOffer[] newArray(int size) {
            return new SpecialOffer[size];
        }
    };
}
