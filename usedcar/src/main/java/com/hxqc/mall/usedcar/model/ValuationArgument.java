package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:估价参数
 *
 * @author: 吕飞
 * @since: 2015-08-10
 * Copyright:恒信汽车电子商务有限公司
 */
public class ValuationArgument implements Parcelable {
    public static final Parcelable.Creator<ValuationArgument> CREATOR = new Parcelable.Creator<ValuationArgument>() {
        @Override
        public ValuationArgument createFromParcel(Parcel source) {
            return new ValuationArgument(source);
        }

        @Override
        public ValuationArgument[] newArray(int size) {
            return new ValuationArgument[size];
        }
    };
    public String title;//标题
    public String brandId;//品牌id
    public String seriesId;//车系id
    public String valuationModelId;//车型id
    public String modelId;//车型id
    public String city;//所在城市
    public String range;//里程
    public String licensing_date;//上牌日期
    public String new_car_price;//新车价

    public ValuationArgument(String brandId, String city, String licensing_date, String modelId, String range, String seriesId, String title, String valuationModelId) {
        this.brandId = brandId;
        this.city = city;
        this.licensing_date = licensing_date;
        this.modelId = modelId;
        this.range = range;
        this.seriesId = seriesId;
        this.title = title;
        this.valuationModelId = valuationModelId;
    }

    protected ValuationArgument(Parcel in) {
        this.title = in.readString();
        this.brandId = in.readString();
        this.seriesId = in.readString();
        this.valuationModelId = in.readString();
        this.modelId = in.readString();
        this.city = in.readString();
        this.range = in.readString();
        this.licensing_date = in.readString();
        this.new_car_price = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.brandId);
        dest.writeString(this.seriesId);
        dest.writeString(this.valuationModelId);
        dest.writeString(this.modelId);
        dest.writeString(this.city);
        dest.writeString(this.range);
        dest.writeString(this.licensing_date);
        dest.writeString(this.new_car_price);
    }
}
