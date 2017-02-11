package com.hxqc.newenergy.bean.position;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.db.area.TCity;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月22日
 */
public class Province implements Parcelable {
	public static final Creator<Province> CREATOR = new Creator<Province>() {
		@Override
		public Province createFromParcel(Parcel source) {
			return new Province(source);
		}

		@Override
		public Province[] newArray(int size) {
			return new Province[size];
		}
	};
	public String provinceName;
    public String provinceInitial;
    public int provinceID;
	public ArrayList<TCity> areaGroup;


    public Province(String provinceName) {
        this.provinceName = provinceName;
    }


    public Province(String provinceName, int provinceID) {
        this.provinceName = provinceName;
        this.provinceID = provinceID;
    }

    protected Province(Parcel in) {
	    this.provinceName = in.readString();
	    this.provinceInitial = in.readString();
	    this.provinceID = in.readInt();
	    this.areaGroup = new ArrayList<TCity>();
	    in.readList(this.areaGroup, TCity.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	    dest.writeString(this.provinceName);
	    dest.writeString(this.provinceInitial);
	    dest.writeInt(this.provinceID);
	    dest.writeList(this.areaGroup);
    }
}
