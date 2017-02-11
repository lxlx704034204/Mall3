package com.hxqc.mall.core.model.loan;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: liukechong
 * Date: 2015-10-16
 * FIXME 分期资料
 * Todo
 */
public class InstallmentInfo implements Parcelable{
    /**
     * 品牌
     */
    public String brand;
    /**
     * 购车人
     */
    public String buyerName;
    /**
     * 城市(带上省)
     */
    public String city;
    /**
     * 办理分期所需的字段
     */
    public InstallmentData fields;
    /**
     * 办理分期的金融机构
     */
    public LoanItemFinanceModel finance;
    /**
     * 房产性质
     */
    public String houseProperty;
    /**
     * 经营行业
     */
    public String industry;
    /**
     * 身份证-正url
     */
    public String info1;
    public String info1Large;
    /**
     * 身份证-反url
     */
    public String info2;
    public String info2Large;
    /**
     * 收入证明url
     */
    public String info3;
    public String info3Large;
    /**
     * 近半年较大银行流水url
     */
    public String info4;
    public String info4Large;
    /**
     * 近一年社保缴费记录ur
     */
    public String info5;
    public String info5Large;
    /**
     * 婚姻状态
     */
    public String marriageStatus;
    /**
     * 月收入
     */
    public String monthSalary;
    /**
     * 购车时间
     */
    public String orderTime;
    /**
     * 手机号
     */
    public String phoneNumber;
    /**
     * 裸车价格
     */
    public String price;

    /**
     * 性别
     */
    public String sex;

    @Override
    public String toString() {
        return "InstallmentInfo{" +
                "brand='" + brand + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", city='" + city + '\'' +
                ", fields=" + fields +
                ", finance=" + finance +
                ", houseProperty=" + houseProperty +
                ", industry=" + industry +
                ", marriageStatus=" + marriageStatus +
                ", monthSalary=" + monthSalary +
                ", orderTime='" + orderTime + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", price='" + price + '\'' +
                ", sex=" + sex +
                ", info1='" + info1 + '\'' +
                ", info2='" + info2 + '\'' +
                ", info3='" + info3 + '\'' +
                ", info4='" + info4 + '\'' +
                ", info5='" + info5 + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brand);
        dest.writeString(this.buyerName);
        dest.writeString(this.city);
        dest.writeParcelable(this.fields, 0);
        dest.writeParcelable(this.finance, 0);
        dest.writeString(this.houseProperty);
        dest.writeString(this.industry);
        dest.writeString(this.info1);
        dest.writeString(this.info1Large);
        dest.writeString(this.info2);
        dest.writeString(this.info2Large);
        dest.writeString(this.info3);
        dest.writeString(this.info3Large);
        dest.writeString(this.info4);
        dest.writeString(this.info4Large);
        dest.writeString(this.info5);
        dest.writeString(this.info5Large);
        dest.writeString(this.marriageStatus);
        dest.writeString(this.monthSalary);
        dest.writeString(this.orderTime);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.price);
        dest.writeString(this.sex);
    }

    public InstallmentInfo() {
    }

    protected InstallmentInfo(Parcel in) {
        this.brand = in.readString();
        this.buyerName = in.readString();
        this.city = in.readString();
        this.fields = in.readParcelable(InstallmentData.class.getClassLoader());
        this.finance = in.readParcelable(LoanItemFinanceModel.class.getClassLoader());
        this.houseProperty = in.readString();
        this.industry = in.readString();
        this.info1 = in.readString();
        this.info1Large = in.readString();
        this.info2 = in.readString();
        this.info2Large = in.readString();
        this.info3 = in.readString();
        this.info3Large = in.readString();
        this.info4 = in.readString();
        this.info4Large = in.readString();
        this.info5 = in.readString();
        this.info5Large = in.readString();
        this.marriageStatus = in.readString();
        this.monthSalary = in.readString();
        this.orderTime = in.readString();
        this.phoneNumber = in.readString();
        this.price = in.readString();
        this.sex = in.readString();
    }

    public static final Creator<InstallmentInfo> CREATOR = new Creator<InstallmentInfo>() {
        public InstallmentInfo createFromParcel(Parcel source) {
            return new InstallmentInfo(source);
        }

        public InstallmentInfo[] newArray(int size) {
            return new InstallmentInfo[size];
        }
    };
}
