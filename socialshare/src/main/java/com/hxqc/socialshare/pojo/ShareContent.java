package com.hxqc.socialshare.pojo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author:胡俊杰
 * Date: 2015/9/14
 * FIXME
 * Todo
 */
public class ShareContent implements Parcelable {
    @Expose
    String content;
    @Expose
    String title;
    @Expose
    String url;

    Context context;
    @Expose
    String thumb;
    int defaultResource;

    public ShareContent(Context context, String title, String content,
                        String url, String thumb) {
        this.content = content;
        this.title = title;
        this.url = url;
        this.context = context;
        this.thumb = thumb;
    }

    public ShareContent(Context context, String title, String content, String url, int defaultResource) {
        this.content = content;
        this.title = title;
        this.url = url;
        this.context = context;
        this.defaultResource = defaultResource;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return context;
    }

    public String getThumb() {
        return thumb;
    }

    public int getDefaultResource() {
        return defaultResource;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setDefaultResource(int defaultResource) {
        this.defaultResource = defaultResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.thumb);
        dest.writeInt(this.defaultResource);
    }

    protected ShareContent(Parcel in) {
        this.content = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.thumb = in.readString();
        this.defaultResource = in.readInt();
    }

    public static final Parcelable.Creator<ShareContent> CREATOR = new Parcelable.Creator<ShareContent>() {
        public ShareContent createFromParcel(Parcel source) {
            return new ShareContent(source);
        }

        public ShareContent[] newArray(int size) {
            return new ShareContent[size];
        }
    };
}
