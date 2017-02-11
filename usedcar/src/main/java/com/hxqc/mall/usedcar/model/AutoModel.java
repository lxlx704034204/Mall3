package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:品牌
 *
 * @author: 吕飞
 * @since: 2015-07-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class AutoModel implements Parcelable {
    public static final Parcelable.Creator<AutoModel> CREATOR = new Parcelable.Creator<AutoModel>() {
        @Override
        public AutoModel createFromParcel(Parcel source) {
            return new AutoModel(source);
        }

        @Override
        public AutoModel[] newArray(int size) {
            return new AutoModel[size];
        }
    };
    public String model_name;
    public String model_id;
    public String valuation_model_id;

    public AutoModel() {
    }

    private AutoModel(Parcel in) {
        this.model_name = in.readString();
        this.model_id = in.readString();
        this.valuation_model_id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.model_name);
        dest.writeString(this.model_id);
        dest.writeString(this.valuation_model_id);
    }
}
