package com.hxqc.mall.thirdshop.model.promotion;

import android.os.Parcel;

import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.socialshare.pojo.ShareContent;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-12-02
 * FIXME  促销详情
 * Todo
 */
public class SalesDetail extends SalesPModel {


    public ShareContent share;
    public ShopInfo shopInfo;
    public String shopID;
    public String shopName;//店铺全称
    public String shopTel;//店铺电话
    public float subscription;//订金金额
    public ArrayList< AttachmentImageNewsModel > attachments;
    public String content;//(html)内容
    public String itemsTableHeader;
    public String itemsTableFooter;
    public ArrayList< SalesItem > items;

    @Override
    public String toString() {
        return "SalesDetail{" +
                "share=" + share +
                ", shopInfo=" + shopInfo +
                ", shopID='" + shopID + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopTel='" + shopTel + '\'' +
                ", subscription=" + subscription +
                ", attachments=" + attachments +
                ", content='" + content + '\'' +
                ", itemsTableHeader='" + itemsTableHeader + '\'' +
                ", itemsTableFooter='" + itemsTableFooter + '\'' +
                ", items=" + items +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.shopInfo, 0);
        dest.writeString(this.shopID);
        dest.writeString(this.shopName);
        dest.writeString(this.shopTel);
        dest.writeFloat(this.subscription);
        dest.writeTypedList(attachments);
        dest.writeString(this.content);
        dest.writeString(this.itemsTableHeader);
        dest.writeString(this.itemsTableFooter);
        dest.writeTypedList(items);
    }

    public SalesDetail() {
    }

    protected SalesDetail(Parcel in) {
        super(in);
        this.shopInfo = in.readParcelable(ShopInfo.class.getClassLoader());
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.shopTel = in.readString();
        this.subscription = in.readFloat();
        this.attachments = in.createTypedArrayList(AttachmentImageNewsModel.CREATOR);
        this.content = in.readString();
        this.itemsTableHeader = in.readString();
        this.itemsTableFooter = in.readString();
        this.items = in.createTypedArrayList(SalesItem.CREATOR);
    }

    public static final Creator< SalesDetail > CREATOR = new Creator< SalesDetail >() {
        public SalesDetail createFromParcel(Parcel source) {
            return new SalesDetail(source);
        }

        public SalesDetail[] newArray(int size) {
            return new SalesDetail[size];
        }
    };
}
