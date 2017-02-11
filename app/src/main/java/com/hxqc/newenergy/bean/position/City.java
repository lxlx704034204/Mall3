package com.hxqc.newenergy.bean.position;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月22日
 */
@Deprecated
public class City implements Parcelable {
    public static final Creator< City > CREATOR = new Creator< City >() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }


        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
	public String province;
	public String id;


	public City(String province) {
		this.province = province;
	}


	protected City(Parcel in) {
		province = in.readString();
		id = in.readString();
	}

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(province);
        dest.writeString(id);
    }
}
