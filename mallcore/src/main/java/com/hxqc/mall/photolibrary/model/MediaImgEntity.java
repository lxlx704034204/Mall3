package com.hxqc.mall.photolibrary.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class MediaImgEntity implements Parcelable {

	public int _id;
	/**
	 * 图片地址
	 */
	public String img_path;
	/**
	 * 缩略图地址
	 */
	public String thumbnails_path;
	/**
	 * 缩略图id
	 */
	public int thumbnails_id;
	/**
	 * 文件夹ID
	 */
	public int dir_id;
	/**
	 * 文件夹名称
	 */
	public String dir_name;
	/**
	 * 文件夹图片数量
	 */
	public int count;
	/**
	 * 创建时间
	 */
	public long addDate;

	public MediaImgEntity() {

	}

	public MediaImgEntity(String img_path) {
		this.img_path = img_path;
	}

	@Override
	public String toString() {
		return "MediaImgEntity{" +
				"_id=" + _id +
				", img_path='" + img_path + '\'' +
				", thumbnails_path='" + thumbnails_path + '\'' +
				", thumbnails_id=" + thumbnails_id +
				", dir_id=" + dir_id +
				", dir_name='" + dir_name + '\'' +
				", count=" + count +
				", addDate=" + addDate +
				'}';
	}

	/**
	 * @param _id
	 * @param img_path
	 * @param dir_id
	 * @param dir_name
	 * @param count
	 */

	public MediaImgEntity(int _id, String img_path, int dir_id,
	                      String dir_name, int count) {
		super();
		this._id = _id;
		this.img_path = img_path;
		this.dir_id = dir_id;
		this.dir_name = dir_name;
		this.count = count;
	}

	public MediaImgEntity(int thumbnails_id, String thumbnails_path) {
		super();
		this.thumbnails_id = thumbnails_id;
		this.thumbnails_path = thumbnails_path;
	}

	public MediaImgEntity(int _id, String img_path, long addDate) {
		super();
		this._id = _id;
		this.img_path = img_path;
		this.addDate = addDate;
	}

	public void setContentPath(int _id, String img_path, long addDate) {
		this._id = _id;
		this.img_path = img_path;
		this.addDate = addDate;
	}

	public String getThumbnails_path() {
		if (TextUtils.isEmpty(thumbnails_path)) {
			return img_path;
		}
		return thumbnails_path;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(this._id);
		dest.writeString(this.img_path);
		dest.writeString(this.thumbnails_path);
		dest.writeInt(this.thumbnails_id);
		dest.writeInt(this.dir_id);
		dest.writeString(this.dir_name);
		dest.writeInt(this.count);
		dest.writeLong(this.addDate);

	}

	private MediaImgEntity(Parcel in) {
		this._id = in.readInt();
		this.img_path = in.readString();
		this.thumbnails_path = in.readString();
		this.thumbnails_id = in.readInt();
		this.dir_id = in.readInt();
		this.dir_name = in.readString();
		this.count = in.readInt();
		this.addDate = in.readLong();
	}

	public static final Creator<MediaImgEntity> CREATOR = new Creator<MediaImgEntity>() {
		public MediaImgEntity createFromParcel(Parcel source) {
			return new MediaImgEntity(source);
		}

		public MediaImgEntity[] newArray(int size) {
			return new MediaImgEntity[size];
		}
	};
}
