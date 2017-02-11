package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * liaoguilong
 * 发票model
 * Created by CPR113 on 2016/7/19.
 */
public class InvoiceModel implements Parcelable {
   public String  billingStatus;//(普/增)发票状态 0：无发票 1：未开票 2:已开票 number
    public String  billingStatusText;//(普/增)发票状态 0：无发票 1：未开票 2:已开票 number
    public String  invoiceType;//(普/增)发票类型 string
    public String  invoiceTypeText;//(普/增)发票类型 0普通发票 1增值税发票 string
    public String  invoiceTitleText;//(普/增)发票抬头，0：个人；1：单位 string
    public String  invoiceTitle;//(普/增)发票抬头，0：个人；1：单位 string
    public String  invoiceContentText;//(普/增)发票内容，默认为-1，-1：不开发票；0：明细；1：用品；2：保养 string
    public String  invoiceContent;//(普/增)发票内容，默认为-1，-1：不开发票；0：明细；1：用品；2：保养 string
    public String   receivableser;//(普/增)收票人 string
    public String    receivablesPhone;//(普/增)收票人手机号 string
    public String   receivablesProvince;//(普/增)省 string
    public String  receivablesCity;//(普/增)市 string
    public String   receivablesRegion;//(普/增)区 string
    public String   receivablesAddress;//(普/增)收票人地址 string
    public String   invoiceCode;//(普/增)票号 string
    public String   expressName;//(普/增)快递公司 string
    public String   expressCode;//(普/增)快递单号 string
    public String   expressRemark;//(普/增)其他说明 string
    public String  companyName;//(增)单位名称 string
    public String  identityCode;//(增)纳税人识别码 string
    public String    registAddress;//(增)注册地址 string
    public String  registPhone;//(增)注册电话 string
    public String    registBank;//(增)开户行 string
    public String   registBankNo;//(增)银行账号 string

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
            if (getInvoiceTitle().equals("0")) {
                title = "个人";
            } else {
                title = "单位";
            }
            return content + "-" + title;
        }
    }
    public String getInvoiceContent() {
        if (TextUtils.isEmpty(invoiceContent)) {
            invoiceContent = "-1";
        }
        return invoiceContent;
    }

    public String getInvoiceTitle() {
        if (TextUtils.isEmpty(invoiceTitle)) {
            invoiceTitle = "0";
        }
        return invoiceTitle;
    }


    protected InvoiceModel(Parcel in) {
        billingStatus = in.readString();
        billingStatusText = in.readString();
        invoiceType = in.readString();
        invoiceTypeText = in.readString();
        invoiceTitleText = in.readString();
        invoiceTitle = in.readString();
        invoiceContentText = in.readString();
        invoiceContent = in.readString();
        receivableser = in.readString();
        receivablesPhone = in.readString();
        receivablesProvince = in.readString();
        receivablesCity = in.readString();
        receivablesRegion = in.readString();
        receivablesAddress = in.readString();
        invoiceCode = in.readString();
        expressName = in.readString();
        expressCode = in.readString();
        expressRemark = in.readString();
        companyName = in.readString();
        identityCode = in.readString();
        registAddress = in.readString();
        registPhone = in.readString();
        registBank = in.readString();
        registBankNo = in.readString();
    }

    public static final Creator<InvoiceModel> CREATOR = new Creator<InvoiceModel>() {
        @Override
        public InvoiceModel createFromParcel(Parcel in) {
            return new InvoiceModel(in);
        }

        @Override
        public InvoiceModel[] newArray(int size) {
            return new InvoiceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(billingStatus);
        dest.writeString(billingStatusText);
        dest.writeString(invoiceType);
        dest.writeString(invoiceTypeText);
        dest.writeString(invoiceTitleText);
        dest.writeString(invoiceTitle);
        dest.writeString(invoiceContentText);
        dest.writeString(invoiceContent);
        dest.writeString(receivableser);
        dest.writeString(receivablesPhone);
        dest.writeString(receivablesProvince);
        dest.writeString(receivablesCity);
        dest.writeString(receivablesRegion);
        dest.writeString(receivablesAddress);
        dest.writeString(invoiceCode);
        dest.writeString(expressName);
        dest.writeString(expressCode);
        dest.writeString(expressRemark);
        dest.writeString(companyName);
        dest.writeString(identityCode);
        dest.writeString(registAddress);
        dest.writeString(registPhone);
        dest.writeString(registBank);
        dest.writeString(registBankNo);
    }
}
