package com.hxqc.mall.core.model.loan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-10-16
 * FIXME 金融公司 item
 * Todo
 */
public class LoanItemFinanceModel implements Parcelable {

    public String financeID;
    public ArrayList< LoanKeyValueModel > infomation;
    public String logo;
    public String title;

    public boolean isChoose = false;//是否被选择

    public long typeHeadID ;//头分类id


    @Override
    public String toString() {
        return "LoanItemFinanceModel{" +
                "financeID='" + financeID + '\'' +
                ", infomation=" + infomation +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", isChoose=" + isChoose +
                '}';
    }

    public LoanItemFinanceModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.financeID);
        dest.writeTypedList(infomation);
        dest.writeString(this.logo);
        dest.writeString(this.title);
        dest.writeByte(isChoose ? (byte) 1 : (byte) 0);
    }

    protected LoanItemFinanceModel(Parcel in) {
        this.financeID = in.readString();
        this.infomation = in.createTypedArrayList(LoanKeyValueModel.CREATOR);
        this.logo = in.readString();
        this.title = in.readString();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator< LoanItemFinanceModel > CREATOR = new Creator< LoanItemFinanceModel >() {
        public LoanItemFinanceModel createFromParcel(Parcel source) {
            return new LoanItemFinanceModel(source);
        }

        public LoanItemFinanceModel[] newArray(int size) {
            return new LoanItemFinanceModel[size];
        }
    };
}
