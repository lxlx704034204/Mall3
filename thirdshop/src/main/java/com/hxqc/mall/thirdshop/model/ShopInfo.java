package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;

/**
 * Author:李烽
 * Date:2015-12-02
 * FIXME
 * Todo 店铺信息
 */
public class ShopInfo implements Parcelable {
    public static final Parcelable.Creator<ShopInfo> CREATOR = new Parcelable.Creator<ShopInfo>() {
        public ShopInfo createFromParcel(Parcel source) {
            return new ShopInfo(source);
        }

        public ShopInfo[] newArray(int size) {
            return new ShopInfo[size];
        }
    };
    public String shopID; //店铺ID
    public String brand;//品牌
    public String brandID;//品牌id
    public String shopLogoThumb; //店铺logo url
    public String shopName; //店铺全称
    public String shopTitle; //店铺简称
    public String shopTel; //店铺电话
    public String rescueTel; //救援电话
    public String serviceHotline; //售后电话
    public String shopInstruction;//公司简介url
    private PickupPointT shopLocation; //地理信息
    public String promotion;  //最新的一条与当前车系相关的促销信息

    //站点信息
    public String siteID;
    public String siteName;


    public ShopInfo() {
    }

    protected ShopInfo(Parcel in) {
        this.shopID = in.readString();
        this.shopLogoThumb = in.readString();
        this.shopName = in.readString();
        this.shopTitle = in.readString();
        this.shopTel = in.readString();
        this.shopInstruction = in.readString();
        this.shopLocation = in.readParcelable(PickupPointT.class.getClassLoader());
        this.promotion = in.readString();
    }

    public PickupPointT getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(PickupPointT shopLocation) {
        this.shopLocation = shopLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopID);
        dest.writeString(this.shopLogoThumb);
        dest.writeString(this.shopName);
        dest.writeString(this.shopTitle);
        dest.writeString(this.shopTel);
        dest.writeString(this.shopInstruction);
        dest.writeParcelable(this.shopLocation, 0);
        dest.writeString(this.promotion);
    }


    public NewMaintenanceShop toNMS() {
        NewMaintenanceShop newMaintenanceShop = new NewMaintenanceShop();
        newMaintenanceShop.shopID = shopID;
        newMaintenanceShop.brand = brand;
        newMaintenanceShop.brandID = brandID;
        newMaintenanceShop.shopName = shopName;
        newMaintenanceShop.shopTitle = shopTitle;
        newMaintenanceShop.shopTel = shopTel;
        newMaintenanceShop.rescueTel = rescueTel;
        newMaintenanceShop.shopInstruction = shopInstruction;
        newMaintenanceShop.address = shopLocation.address;
        newMaintenanceShop.latitude = Double.parseDouble(shopLocation.latitude);
        newMaintenanceShop.longitude = Double.parseDouble(shopLocation.longitude);
        newMaintenanceShop.distance = shopLocation.distance;
        return newMaintenanceShop;
    }
}
