package com.hxqc.mall.thirdshop.model.promotion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-12-01
 * FIXME 促销信息
 * Todo
 */
public class SalesPModel implements Parcelable {

    final public static String P_UNPUBLISH = "10";
    final public static String P_IS_PUBLISH = "20";
    final public static String P_OFFLINE = "30";

    final public static int CAR_P = 10;
    final public static int MAINTENANCE_P = 40;

    public String promotionID;//促销id
    public String shopTitle;//店铺简称
    public String thumb;
    public String title;//促销标题
    public String publishDate;//活动发布时间
    public String startDate;//活动开始时间
    public String endDate;//活动结束四件
    public String summary;
    public String serverTime;//服务器时间
    public String status;//活动状态（10:未发布 20:发布 30:下线） string
    public float paymentAvailable;////是否可付款 0不可付  1可付
    public int type;//促销类型 10车辆促销 60保养促销


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.promotionID);
        dest.writeString(this.shopTitle);
        dest.writeString(this.thumb);
        dest.writeString(this.title);
        dest.writeString(this.publishDate);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.summary);
        dest.writeString(this.serverTime);
        dest.writeString(this.status);
        dest.writeFloat(this.paymentAvailable);
        dest.writeInt(this.type);
    }

    public SalesPModel() {
    }

    protected SalesPModel(Parcel in) {
        this.promotionID = in.readString();
        this.shopTitle = in.readString();
        this.thumb = in.readString();
        this.title = in.readString();
        this.publishDate = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.summary = in.readString();
        this.serverTime = in.readString();
        this.status = in.readString();
        this.paymentAvailable = in.readFloat();
        this.type = in.readInt();
    }

    public static final Creator< SalesPModel > CREATOR = new Creator< SalesPModel >() {
        @Override
        public SalesPModel createFromParcel(Parcel source) {
            return new SalesPModel(source);
        }

        @Override
        public SalesPModel[] newArray(int size) {
            return new SalesPModel[size];
        }
    };
}
