package com.hxqc.mall.photolibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author: wanghao
 * Date: 2015-05-15
 * FIXME
 * Todo
 */
public class EventImages implements Parcelable {

	public List<ImageItem> imageItems;
	public int _type;

	public EventImages(List<ImageItem> imageItems, int _type) {
		this.imageItems = imageItems;
		this._type = _type;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(imageItems);
		dest.writeInt(this._type);
	}

	private EventImages(Parcel in) {
		in.readTypedList(imageItems, ImageItem.CREATOR);
		this._type = in.readInt();
	}

	public static final Creator<EventImages> CREATOR = new Creator<EventImages>() {
		public EventImages createFromParcel(Parcel source) {
			return new EventImages(source);
		}

		public EventImages[] newArray(int size) {
			return new EventImages[size];
		}
	};
}
