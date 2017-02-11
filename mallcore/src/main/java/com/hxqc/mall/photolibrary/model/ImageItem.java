package com.hxqc.mall.photolibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Author: wanghao
 * Date: 2015-05-12
 * FIXME
 * Todo
 */
public class ImageItem implements Serializable, Parcelable {

	@Expose
	private static final long serialVersionUID = -7188270558443739436L;
	@Expose
	public String imageId;
	@Expose
	public String thumbnailPath;
	@Expose
	public String sourcePath;
	@Expose
	public boolean isSelected = false;


	//二手车的设置首图
	@Expose
	public boolean isFigure = false;

	public void setIsFigure(boolean isFigure) {
		this.isFigure = isFigure;
	}

	public boolean isFigure() {
		return isFigure;
	}

	//    private static final long serialVersionUID = 1l;
	public int id;
	public String mThumbPath;
	public String mPath;
	public String mTitle;
	public boolean mSelected;

	public void setId(int id) {
		this.id = id;
	}

	public void setThumbPath(String mThumbPath) {
		this.mThumbPath = mThumbPath;
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public void setSelected(boolean mSelected) {
		this.mSelected = mSelected;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public String getThumbPath() {
		return mThumbPath;
	}

	public String getPath() {
		return mPath;
	}

	public String getTitle() {
		return mTitle;
	}

	public boolean isSelected() {
		return mSelected;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.imageId);
		dest.writeString(this.thumbnailPath);
		dest.writeString(this.sourcePath);
		dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
		dest.writeInt(this.id);
		dest.writeString(this.mThumbPath);
		dest.writeString(this.mPath);
		dest.writeString(this.mTitle);
		dest.writeByte(mSelected ? (byte) 1 : (byte) 0);
	}

	public ImageItem(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	private ImageItem(Parcel in) {
		this.imageId = in.readString();
		this.thumbnailPath = in.readString();
		this.sourcePath = in.readString();
		this.isSelected = in.readByte() != 0;
		this.id = in.readInt();
		this.mThumbPath = in.readString();
		this.mPath = in.readString();
		this.mTitle = in.readString();
		this.mSelected = in.readByte() != 0;
	}

	public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
		public ImageItem createFromParcel(Parcel source) {
			return new ImageItem(source);
		}

		public ImageItem[] newArray(int size) {
			return new ImageItem[size];
		}
	};
}
