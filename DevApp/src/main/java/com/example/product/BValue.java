package com.example.product;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created 胡俊杰
 * 2016/11/9.
 * Todo:
 */

public class BValue implements Parcelable, Action {

	public String value;

	public BValue(String value) {
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

	protected BValue(Parcel in) {
		this.value = in.readString();
	}

	public static final Parcelable.Creator<BValue> CREATOR = new Parcelable.Creator<BValue>() {
		@Override
		public BValue createFromParcel(Parcel source) {
			return new BValue(source);
		}

		@Override
		public BValue[] newArray(int size) {
			return new BValue[size];
		}
	};

	@Override
	public void doSth() {
		Log.i("Tag", "这是BBBB" + value);
	}
}
