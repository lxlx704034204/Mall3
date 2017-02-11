package com.hxqc.mall.config.update;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡俊杰
 * Date:2015-11-09
 * FIXME
 * Todo 版本
 */
public class VersionPo implements Parcelable {

	public String url="";//新版本的URL
	public String versionNum="";//最新启用的版本号
	public String description="";//版本描述
	public int forceUpdate;//此版本是否需要强制更新：0.不需要；1.需要
	public String minVersion="";//最小版本号

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.url);
		dest.writeString(this.versionNum);
		dest.writeString(this.description);
		dest.writeInt(this.forceUpdate);
		dest.writeString(this.minVersion);
	}

	public VersionPo() {
	}

	protected VersionPo(Parcel in) {
		this.url = in.readString();
		this.versionNum = in.readString();
		this.description = in.readString();
		this.forceUpdate = in.readInt();
		this.minVersion = in.readString();
	}

	public static final Parcelable.Creator<VersionPo> CREATOR = new Parcelable.Creator<VersionPo>() {
		@Override
		public VersionPo createFromParcel(Parcel source) {
			return new VersionPo(source);
		}

		@Override
		public VersionPo[] newArray(int size) {
			return new VersionPo[size];
		}
	};

	@Override
	public String toString() {
		return "VersionPo{" +
				"url='" + url + '\'' +
				", versionNum='" + versionNum + '\'' +
				", description='" + description + '\'' +
				", forceUpdate=" + forceUpdate +
				", minVersion='" + minVersion + '\'' +
				'}';
	}
}

