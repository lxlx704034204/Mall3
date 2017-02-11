package com.hxqc.mall.thirdshop.maintenance.model.promotion;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.promotion.AttachmentImageNewsModel;
import com.hxqc.socialshare.pojo.ShareContent;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2016-03-22
 * FIXME
 * Todo  店铺促销信息
 */
public class PromotionInfo_m implements Parcelable {

    public String promotionID;//促销id
    public String thumb;
    public String title;//促销标题
    public ShopInfo shopInfo;
    public String serverTime;//服务器时间
    public String publishDate;//活动发布时间
    public String startDate;//活动开始时间
    public String endDate;//活动结束四件
    public String subscription;
    public String status;//活动状态（10:未发布 20:发布 30:下线）

    @Expose
    public ArrayList<AttachmentImageNewsModel> attachments;
    public String summary;
    public String content;//(html)内容

    @Expose
    public ArrayList<Maintenance_package> maintenancePackages;
    public ShareContent share;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.promotionID);
        dest.writeString(this.thumb);
        dest.writeString(this.title);
        dest.writeParcelable(this.shopInfo, flags);
        dest.writeString(this.serverTime);
        dest.writeString(this.publishDate);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.subscription);
        dest.writeString(this.status);
        dest.writeTypedList(attachments);
        dest.writeString(this.summary);
        dest.writeString(this.content);
        dest.writeTypedList(maintenancePackages);
        dest.writeParcelable(this.share, flags);
    }

    public PromotionInfo_m() {
    }

    protected PromotionInfo_m(Parcel in) {
        this.promotionID = in.readString();
        this.thumb = in.readString();
        this.title = in.readString();
        this.shopInfo = in.readParcelable(ShopInfo.class.getClassLoader());
        this.serverTime = in.readString();
        this.publishDate = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.subscription = in.readString();
        this.status = in.readString();
        this.attachments = in.createTypedArrayList(AttachmentImageNewsModel.CREATOR);
        this.summary = in.readString();
        this.content = in.readString();
        this.maintenancePackages = in.createTypedArrayList(Maintenance_package.CREATOR);
        this.share = in.readParcelable(ShareContent.class.getClassLoader());
    }

    public static final Creator< PromotionInfo_m > CREATOR = new Creator< PromotionInfo_m >() {
        @Override
        public PromotionInfo_m createFromParcel(Parcel source) {
            return new PromotionInfo_m(source);
        }

        @Override
        public PromotionInfo_m[] newArray(int size) {
            return new PromotionInfo_m[size];
        }
    };
}
