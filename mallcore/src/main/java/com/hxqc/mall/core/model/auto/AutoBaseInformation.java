package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:基本信息（商品详情）
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class AutoBaseInformation implements Parcelable {

    public String extID;//车型ID 详细参数ID
    public String itemName;//商品名称
    public String itemPrice;//价格
    private String itemID;//商品ID
    private String itemThumb;//车辆图片
    private String itemDescription = "";//商品名称描述

    private String promotionID;//特卖ID
    private String subscription;//订金
    private String seriesID;//车系ID
    public String itemPic;
    private String itemOriginalPrice = "0.00";//车辆原价
    private ArrayList< ColorInformation > appearance;//车身颜色
    private String introduce = "";//图文介绍 url
    private int inventory = 0;//库存
    private String itemAvailable;//商品是否可购买
    private String itemCommentCount = "0";//评论数
    private String itemFall = "0";//降幅
    private String itemSales = "0";//销量
    private String itemColor;//车身颜色
    private String itemColorDescription = "";//车身颜色文本
    private String itemInterior;//内饰颜色
    private String itemInteriorDescription = "";//内饰颜色文本
    public int itemCategory;//车辆类别 (10.汽车，20.电动车)



    public String getItemOriginalPrice() {
        if (TextUtils.isEmpty(itemOriginalPrice)) return "";
        return OtherUtil.amountFormat(itemOriginalPrice);
    }

    public String getItemPic() {
        return itemPic;
    }

    public ArrayList< ColorInformation > getAppearance() {
        return appearance;
    }

    public String[] getItemColor() {
        return getColors(itemColor);
    }

    public String[] getColors(String color) {

        if (TextUtils.isEmpty(color)){
            return new String[]{"#ffffff"};
        }

        if (!color.contains(",")) {
            return new String[]{color};
        }
        return color.split(",");
    }


    public String[] getItemInteriorArray() {
        return getColors(itemInterior);
    }

    public String getItemInterior() {
        return itemInterior;
    }


    public String getItemColorDescription() {
        return itemColorDescription;
    }


    public String getItemInteriorDescription() {
        return itemInteriorDescription;
    }

    public String getIntroduce() {
        return introduce;
    }


    public String getInventory() {
        return String.valueOf(inventory);

    }

    public boolean isItemAvailable() {
        return OtherUtil.int2Boolean(Integer.valueOf(itemAvailable));
    }


    public String getItemCommentCount() {
        return itemCommentCount;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemFall() {
        return Float.valueOf(itemFall);
    }

    public String getItemFallU() {
        return OtherUtil.amountFormat(itemFall);
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    /**
     * 车辆价格
     */
    public float getItemPrice() {
        try {
            return Float.valueOf(itemPrice);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f;
        }
    }


    /**
     * 车辆价格 带单位  万
     */
    public String getItemPriceU() {
        return OtherUtil.amountFormat(itemPrice);
    }

    public String getItemSales() {
        return itemSales;
    }

    public String getItemThumb() {
        return itemThumb;
    }

    public String getExtID() {
        return extID;
    }

    public String getItemDescription() {
        if (!TextUtils.isEmpty(itemName)) return itemName;
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    /**
     * 交易状态
     *
     * @return
     */
    public AutoDetail.TransactionStatus transactionStatus() {
        if (inventory > 0) {
            return AutoDetail.TransactionStatus.NORMAL;
        } else {
            return AutoDetail.TransactionStatus.SELLOUT;
        }
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public String getPromotionID() {
        return promotionID;
    }

    public String getSubscription() {
        return subscription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.extID);
        dest.writeString(this.itemName);
        dest.writeString(this.itemPrice);
        dest.writeString(this.itemID);
        dest.writeString(this.itemThumb);
        dest.writeString(this.itemDescription);
        dest.writeString(this.promotionID);
        dest.writeString(this.subscription);
        dest.writeString(this.seriesID);
        dest.writeString(this.itemPic);
        dest.writeString(this.itemOriginalPrice);
        dest.writeList(this.appearance);
        dest.writeString(this.introduce);
        dest.writeInt(this.inventory);
        dest.writeString(this.itemAvailable);
        dest.writeString(this.itemCommentCount);
        dest.writeString(this.itemFall);
        dest.writeString(this.itemSales);
        dest.writeString(this.itemColor);
        dest.writeString(this.itemColorDescription);
        dest.writeString(this.itemInterior);
        dest.writeString(this.itemInteriorDescription);
    }

    public AutoBaseInformation() {
    }

    protected AutoBaseInformation(Parcel in) {
        this.extID = in.readString();
        this.itemName = in.readString();
        this.itemPrice = in.readString();
        this.itemID = in.readString();
        this.itemThumb = in.readString();
        this.itemDescription = in.readString();
        this.promotionID = in.readString();
        this.subscription = in.readString();
        this.seriesID = in.readString();
        this.itemPic = in.readString();
        this.itemOriginalPrice = in.readString();
        this.appearance = new ArrayList<  >();
        in.readList(this.appearance, List.class.getClassLoader());
        this.introduce = in.readString();
        this.inventory = in.readInt();
        this.itemAvailable = in.readString();
        this.itemCommentCount = in.readString();
        this.itemFall = in.readString();
        this.itemSales = in.readString();
        this.itemColor = in.readString();
        this.itemColorDescription = in.readString();
        this.itemInterior = in.readString();
        this.itemInteriorDescription = in.readString();
    }

}
