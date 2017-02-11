package com.hxqc.mall.core.model.comment;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.model.ImageModel;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-05-04
 * FIXME
 * 评论详情
 */
public class CommentDetailResponse implements Parcelable {

    public String commentID;
    public String content;
    public String createTime;
    public ArrayList<ImageModel > images;
    public String published;
    public String refusedReason;
    public String score;
    public CommentReply reply;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commentID);
        dest.writeString(this.content);
        dest.writeString(this.createTime);
        dest.writeTypedList(images);
        dest.writeString(this.published);
        dest.writeString(this.refusedReason);
        dest.writeString(this.score);
        dest.writeParcelable(this.reply, 0);
    }

    public CommentDetailResponse() {
    }

    protected CommentDetailResponse(Parcel in) {
        this.commentID = in.readString();
        this.content = in.readString();
        this.createTime = in.readString();
        this.images = in.createTypedArrayList(ImageModel.CREATOR);
        this.published = in.readString();
        this.refusedReason = in.readString();
        this.score = in.readString();
        this.reply = in.readParcelable(CommentReply.class.getClassLoader());
    }

    public static final Creator< CommentDetailResponse > CREATOR = new Creator< CommentDetailResponse >() {
        public CommentDetailResponse createFromParcel(Parcel source) {
            return new CommentDetailResponse(source);
        }

        public CommentDetailResponse[] newArray(int size) {
            return new CommentDetailResponse[size];
        }
    };
}
