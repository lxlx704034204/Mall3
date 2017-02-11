package com.hxqc.mall.core.model.comment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-12-25
 * FIXME  商家回复
 * Todo
 */
public class CommentReply implements Parcelable {
    public String replier;
    public String content;
    public String createTime;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.replier);
        dest.writeString(this.content);
        dest.writeString(this.createTime);
    }

    public CommentReply() {
    }

    protected CommentReply(Parcel in) {
        this.replier = in.readString();
        this.content = in.readString();
        this.createTime = in.readString();
    }

    public static final Creator< CommentReply > CREATOR = new Creator< CommentReply >() {
        public CommentReply createFromParcel(Parcel source) {
            return new CommentReply(source);
        }

        public CommentReply[] newArray(int size) {
            return new CommentReply[size];
        }
    };
}
