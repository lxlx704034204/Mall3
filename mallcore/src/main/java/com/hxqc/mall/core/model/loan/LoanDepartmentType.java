package com.hxqc.mall.core.model.loan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-11-02
 * FIXME
 * Todo 金融机构类型
 */
public class LoanDepartmentType implements Parcelable {
    public ArrayList< LoanItemFinanceModel > finance;
    public String title;

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
        return "选择"+title;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(finance);
        dest.writeString(this.title);
    }

    public LoanDepartmentType() {
    }

    protected LoanDepartmentType(Parcel in) {
        this.finance = in.createTypedArrayList(LoanItemFinanceModel.CREATOR);
        this.title = in.readString();
    }

    public static final Parcelable.Creator< LoanDepartmentType > CREATOR = new Parcelable.Creator< LoanDepartmentType >() {
        public LoanDepartmentType createFromParcel(Parcel source) {
            return new LoanDepartmentType(source);
        }

        public LoanDepartmentType[] newArray(int size) {
            return new LoanDepartmentType[size];
        }
    };
}
