package com.hxqc.mall.thirdshop.maintenance.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月29日
 */
public class MaintenancePriceOrTitle implements Parcelable {
    public static final Creator<MaintenancePriceOrTitle> CREATOR = new Creator<MaintenancePriceOrTitle>() {
        @Override
        public MaintenancePriceOrTitle createFromParcel(Parcel in) {
            return new MaintenancePriceOrTitle(in);
        }


        @Override
        public MaintenancePriceOrTitle[] newArray(int size) {
            return new MaintenancePriceOrTitle[size];
        }
    };
    private final static String TAG = MaintenancePriceOrTitle.class.getSimpleName();
    public double price;
    public String title;
    private Context mContext;


    public MaintenancePriceOrTitle() {
    }


    protected MaintenancePriceOrTitle(Parcel in) {
        price = in.readDouble();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeString(title);
    }
}
