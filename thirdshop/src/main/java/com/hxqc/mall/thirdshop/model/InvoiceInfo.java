package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

/**
 * 说明:发票信息
 *
 * @author: 吕飞
 * @since: 2016-07-12
 * Copyright:恒信汽车电子商务有限公司
 */
public class InvoiceInfo implements Parcelable {
    @Expose
    public String invoiceType = "0";
    @Expose
    public String invoiceContent = "-1";//内容
    @Expose
    public String invoiceTitle = "个人";//抬头
    public String companyName;//公司名字
    @Expose
    public String receivableser;//收票人姓名
    @Expose
    public String receivablesPhone;
    @Expose
    public String receivablesProvince;
    @Expose
    public String receivablesCity;
    @Expose
    public String receivablesRegion;
    @Expose
    public String receivablesAddress;

    public String getInvoiceSummary() {
        if (invoiceContent.equals("-1")) {
            return "不开发票";
        } else {
            String content;
            String title;
            if (getInvoiceContent().equals("0")) {
                content = "明细";
            } else if (getInvoiceContent().equals("1")) {
                content = "用品";
            } else {
                content = "保养";
            }
            if (getInvoiceTitle().equals("个人")) {
                title = "个人";
            } else {
                title = "单位";
            }
            return content + "-" + title;
        }
    }

    public String getCompanyName() {
        if (TextUtils.isEmpty(companyName)) {
            return "";
        } else {
            return companyName;
        }
    }

    public String getInvoiceContent() {
        if (TextUtils.isEmpty(invoiceContent)) {
            invoiceContent = "-1";
        }
        return invoiceContent;
    }

    public String getInvoiceTitle() {
        if (TextUtils.isEmpty(invoiceTitle) || invoiceTitle.equals("个人")) {
            invoiceTitle = "个人";
            return invoiceTitle;
        } else {
            return getCompanyName();
        }
    }

    public String getReceivablesAddress() {
        if (TextUtils.isEmpty(receivablesAddress)) {
            return "";
        } else {
            return receivablesAddress;
        }
    }

    public String getReceivableser() {
        if (TextUtils.isEmpty(receivableser)) {
            return "";
        } else {
            return receivableser;
        }
    }

    public String getReceivablesPhone() {
        if (TextUtils.isEmpty(receivablesPhone)) {
            return "";
        } else {
            return receivablesPhone;
        }
    }

    public String getReceivablesArea() {
        if (TextUtils.isEmpty(receivablesProvince)) {
            return "";
        } else {
            return receivablesProvince + " " + receivablesCity + " " + receivablesRegion;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.invoiceContent);
        dest.writeString(this.invoiceTitle);
        dest.writeString(this.companyName);
        dest.writeString(this.receivableser);
        dest.writeString(this.receivablesPhone);
        dest.writeString(this.receivablesProvince);
        dest.writeString(this.receivablesCity);
        dest.writeString(this.receivablesRegion);
        dest.writeString(this.receivablesAddress);
    }

    public InvoiceInfo() {
    }

    protected InvoiceInfo(Parcel in) {
        this.invoiceContent = in.readString();
        this.invoiceTitle = in.readString();
        this.companyName = in.readString();
        this.receivableser = in.readString();
        this.receivablesPhone = in.readString();
        this.receivablesProvince = in.readString();
        this.receivablesCity = in.readString();
        this.receivablesRegion = in.readString();
        this.receivablesAddress = in.readString();
    }

    public static final Parcelable.Creator<InvoiceInfo> CREATOR = new Parcelable.Creator<InvoiceInfo>() {
        @Override
        public InvoiceInfo createFromParcel(Parcel source) {
            return new InvoiceInfo(source);
        }

        @Override
        public InvoiceInfo[] newArray(int size) {
            return new InvoiceInfo[size];
        }
    };

    @Override
    public String toString() {
        return "InvoiceInfo{" +
                "invoiceContent='" + invoiceContent + '\'' +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", companyName='" + companyName + '\'' +
                ", receivableser='" + receivableser + '\'' +
                ", receivablesPhone='" + receivablesPhone + '\'' +
                ", receivablesProvince='" + receivablesProvince + '\'' +
                ", receivablesCity='" + receivablesCity + '\'' +
                ", receivablesRegion='" + receivablesRegion + '\'' +
                ", receivablesAddress='" + receivablesAddress + '\'' +
                '}';
    }
}
