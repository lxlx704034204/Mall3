package com.hxqc.mall.thirdshop.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Function: 新维修保养列表的bean
 *
 * @author 袁秉勇
 * @since 2016年04月22日
 */
public class NewMaintenanceShop implements Parcelable {
    public String shopID;
    public int used;
    public ArrayList< String > shopPhoto;
    public String shopTel;
    public String brandThumb;
    public String shopTitle;
    public String shopName;
    public double distance;
    public String address;
    public double latitude;
    public double longitude;
    public String level;
    public String evaluate;
    public String score;
    public ArrayList<Payment> pays;
    public double amount;
    public long shopType;
    public String queryShop;
    public String brand;  //店铺品牌 string
    public String brandID; //品牌ID string
    public String rescueTel; //救援电话 string
    public String shopInstruction; //公司简介（URL） string
    public int orderCount; //接单量 number

    public NewMaintenanceShop(){}


    protected NewMaintenanceShop(Parcel in) {
        shopID = in.readString();
        used = in.readInt();
        shopPhoto = in.createStringArrayList();
        shopTel = in.readString();
        brandThumb = in.readString();
        shopTitle = in.readString();
        shopName = in.readString();
        distance = in.readDouble();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        level = in.readString();
        evaluate = in.readString();
        score = in.readString();
        pays = in.createTypedArrayList(Payment.CREATOR);
        amount = in.readDouble();
        shopType = in.readLong();
        queryShop = in.readString();
        brand = in.readString();
        brandID = in.readString();
        rescueTel = in.readString();
        shopInstruction = in.readString();
        orderCount = in.readInt();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopID);
        dest.writeInt(used);
        dest.writeStringList(shopPhoto);
        dest.writeString(shopTel);
        dest.writeString(brandThumb);
        dest.writeString(shopTitle);
        dest.writeString(shopName);
        dest.writeDouble(distance);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(level);
        dest.writeString(evaluate);
        dest.writeString(score);
        dest.writeTypedList(pays);
        dest.writeDouble(amount);
        dest.writeLong(shopType);
        dest.writeString(queryShop);
        dest.writeString(brand);
        dest.writeString(brandID);
        dest.writeString(rescueTel);
        dest.writeString(shopInstruction);
        dest.writeInt(orderCount);
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator< NewMaintenanceShop > CREATOR = new Creator< NewMaintenanceShop >() {
        @Override
        public NewMaintenanceShop createFromParcel(Parcel in) {
            return new NewMaintenanceShop(in);
        }


        @Override
        public NewMaintenanceShop[] newArray(int size) {
            return new NewMaintenanceShop[size];
        }
    };
}
