package com.example.product;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created 胡俊杰
 * 2016/11/9.
 * Todo:
 */

public class AValue implements Parcelable, Action {

	public String value;

	public AValue(String value) {
		this.value = value;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.value);
	}

	protected AValue(Parcel in) {
		this.value = in.readString();
	}

	public static final Parcelable.Creator<AValue> CREATOR = new Parcelable.Creator<AValue>() {
		@Override
		public AValue createFromParcel(Parcel source) {
			return new AValue(source);
		}

		@Override
		public AValue[] newArray(int size) {
			return new AValue[size];
		}
	};

	@Override
	public void doSth() {
		Log.i("Tag", "这是AAAA    " + value);
	}
}
