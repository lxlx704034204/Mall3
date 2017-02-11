package com.hxqc.pay.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-04-08
 * FIXME
 * 拿到bankid
 */
public class EventUpLoadFile implements Parcelable {

    public static int has_saved = 1;
    public static int do_nothing = 0;

    public int operateStatus;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.operateStatus);
    }

    public EventUpLoadFile() {
    }

    protected EventUpLoadFile(Parcel in) {
        this.operateStatus = in.readInt();
    }

    public static final Creator< EventUpLoadFile > CREATOR = new Creator< EventUpLoadFile >() {
        public EventUpLoadFile createFromParcel(Parcel source) {
            return new EventUpLoadFile(source);
        }

        public EventUpLoadFile[] newArray(int size) {
            return new EventUpLoadFile[size];
        }
    };
}
