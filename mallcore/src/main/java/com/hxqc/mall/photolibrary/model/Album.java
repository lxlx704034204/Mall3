package com.hxqc.mall.photolibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-05-12
 * FIXME
 * Todo
 */
public class Album implements Serializable {

	private static final long serialVersionUID = 1l;
	private String mId;
	private String mName;
	private ArrayList<ImageItem> mImages = new ArrayList<>();
	private boolean isCheck;

	public void addImage(ImageItem imageItem) {
		if (mImages != null)
			mImages.add(imageItem);
	}


	public void setId(String mId) {
		this.mId = mId;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public void setImages(ArrayList<ImageItem> mImages) {
		this.mImages = mImages;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public ArrayList<ImageItem> getImages() {
		return mImages;
	}

	public boolean isCheck() {
		return isCheck;
	}

	@Override
	public String toString() {
		return "Album{" +
				"mId='" + mId + '\'' +
				", mName='" + mName + '\'' +
				", mImages=" + mImages +
				", isCheck=" + isCheck +
				'}';
	}
}
