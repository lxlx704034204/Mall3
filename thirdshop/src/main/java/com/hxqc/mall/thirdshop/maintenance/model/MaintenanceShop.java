package com.hxqc.mall.thirdshop.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.model.auto.PickupPointT;

import java.util.ArrayList;

/**
 * Function: 保养列表bean
 *
 * @author 袁秉勇
 * @since 2016年02月29日
 */
public class MaintenanceShop implements Parcelable {
    public String shopID;
    public String shopPhoto;
    public int used;
    public String brandThumb;
    public String shopTel;
    public String shopTitle;
    public double distance;
    public String promotionTitle;
    public String rescueTel;
    public PickupPointT shopPoint;
    public ArrayList<MaintenancePriceOrTitle> promotionList;
    public ArrayList<MaintenancePriceOrTitle> maintenanceList;
    public String shopType;


    public MaintenanceShop() {
    }


    protected MaintenanceShop(Parcel in) {
        shopID = in.readString();
        shopPhoto = in.readString();
        used = in.readInt();
        brandThumb = in.readString();
        shopTel = in.readString();
        shopTitle = in.readString();
        distance = in.readDouble();
        promotionTitle = in.readString();
        rescueTel = in.readString();
        shopPoint = in.readParcelable(PickupPointT.class.getClassLoader());
        promotionList = in.createTypedArrayList(MaintenancePriceOrTitle.CREATOR);
        maintenanceList = in.createTypedArrayList(MaintenancePriceOrTitle.CREATOR);
        shopType = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopID);
        dest.writeString(shopPhoto);
        dest.writeInt(used);
        dest.writeString(brandThumb);
        dest.writeString(shopTel);
        dest.writeString(shopTitle);
        dest.writeDouble(distance);
        dest.writeString(promotionTitle);
        dest.writeString(rescueTel);
        dest.writeParcelable(shopPoint, flags);
        dest.writeTypedList(promotionList);
        dest.writeTypedList(maintenanceList);
        dest.writeString(shopType);
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator< MaintenanceShop > CREATOR = new Creator< MaintenanceShop >() {
        @Override
        public MaintenanceShop createFromParcel(Parcel in) {
            return new MaintenanceShop(in);
        }


        @Override
        public MaintenanceShop[] newArray(int size) {
            return new MaintenanceShop[size];
        }
    };
}
