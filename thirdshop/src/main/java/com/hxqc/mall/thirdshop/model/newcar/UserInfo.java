package com.hxqc.mall.thirdshop.model.newcar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 回复人
 * Created by huangyi on 16/10/24.
 */
public class UserInfo implements Parcelable {

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String userAvatar; //头像URL
    public String nickName; //昵称
    public String userID; //用户id

    protected UserInfo(Parcel in) {
        userAvatar = in.readString();
        nickName = in.readString();
        userID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userAvatar);
        dest.writeString(nickName);
        dest.writeString(userID);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
