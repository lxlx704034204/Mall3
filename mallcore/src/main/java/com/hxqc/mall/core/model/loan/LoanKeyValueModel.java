package com.hxqc.mall.core.model.loan;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: wanghao
 * Date: 2015-10-16
 * FIXME  金融公司内容条目键值对
 * Todo
 */
public class LoanKeyValueModel implements Parcelable {

    public String key;
    public String title;
    public String value;

    public String getTitle() {

        String end = title.substring(title.length() - 1);
        if (!end.equals("：")) {
            if (end.equals(":")) {
                String c = title.substring(0, title.length() - 1);
                title = c+"：";
            } else {
                this.title += "：";
            }
        }
        return title;

    }

    public String getValue() {

        if (TextUtils.isEmpty(value)){
            return "";
        }else {
            String dest = "";
            if (value!=null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(value);
                dest = m.replaceAll("");
            }
            return dest;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.title);
        dest.writeString(this.value);
    }

    public LoanKeyValueModel() {
    }

    protected LoanKeyValueModel(Parcel in) {
        this.key = in.readString();
        this.title = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator< LoanKeyValueModel > CREATOR = new Parcelable.Creator< LoanKeyValueModel >() {
        public LoanKeyValueModel createFromParcel(Parcel source) {
            return new LoanKeyValueModel(source);
        }

        public LoanKeyValueModel[] newArray(int size) {
            return new LoanKeyValueModel[size];
        }
    };
}
