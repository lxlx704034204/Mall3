package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;

/**
 * Author: wanghao
 * Date: 2015-04-07
 * FIXME
 * 特卖订单请求
 */
public class OrderRequest implements Parcelable {
    /**
     * 线上全款
     */
    public static final String PAYMENTTYPE_FULL_AMOUNT = "0";
    /**
     * 分期付款
     */
    public static final String PAYMENTTYPE_INSTALLMENT = "2";
    /**
     * 预付订金
     */
    public static final String PAYMENTTYPE_OFF_LINE = "1";

    //是否上牌 0.没有，1.上牌
    public String isLicense = "0";
    //是否办理保险 0.不办理，1.办理
    public String insurance = "0";

    //支付方式：0.全款，1.分期，2.线下
    public String paymentType = OrderPayRequest.PAYMENTTYPE_OFF_LINE;
    //具体车型
    public String itemID;
    public String itemType;
    public String packageID;
    public String accessoryID;
    /**
     * 10.14加入获取正式合同参数
     */
    //0.一般    ， 1.特卖
    public String isSeckill;
    //订单id
    public String orderID;
    //分期银行ID
    public String financeID;

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public void setAccessoryID(String accessoryID) {
        this.accessoryID = accessoryID;
    }

    public OrderRequest() {
    }


    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setItem(String itemID, String itemType) {
        this.itemID = itemID;
        this.itemType = itemType;
    }

    public int getPayType() {
        if (itemType.equals(AutoItem.AUTO_COMMON)) {
            if (paymentType.equals(PAYMENTTYPE_FULL_AMOUNT))
                //正常线上
                return PayConstant.PAY_FLOW_NORMAL_ONLINE;
            else
                //正常线下/分期
                return PayConstant.PAY_FLOW_DEPOSIT_ONLINE;
        } else {
            //特卖
            if (paymentType.equals(PAYMENTTYPE_FULL_AMOUNT))
                //特卖线上
                return PayConstant.PAY_ONLY_DEPOSIT_ONLINE;
            else
                //特卖线下/分期
                return PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE;
        }
    }

    public boolean isInsurance() {
        return OtherUtil.tBoolean(insurance);
    }

    /**
     * 设置保险
     */
    public void setInsurance(boolean insurance) {
        this.insurance = insurance ? "1" : "0";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isLicense);
        dest.writeString(this.insurance);
        dest.writeString(this.paymentType);
        dest.writeString(this.itemID);
        dest.writeString(this.itemType);
        dest.writeString(this.packageID);
        dest.writeString(this.accessoryID);
        dest.writeString(this.isSeckill);
        dest.writeString(this.orderID);
        dest.writeString(this.financeID);
    }

    protected OrderRequest(Parcel in) {
        this.isLicense = in.readString();
        this.insurance = in.readString();
        this.paymentType = in.readString();
        this.itemID = in.readString();
        this.itemType = in.readString();
        this.packageID = in.readString();
        this.accessoryID = in.readString();
        this.isSeckill = in.readString();
        this.orderID = in.readString();
        this.financeID = in.readString();
    }

    public static final Creator< OrderRequest > CREATOR = new Creator< OrderRequest >() {
        public OrderRequest createFromParcel(Parcel source) {
            return new OrderRequest(source);
        }

        public OrderRequest[] newArray(int size) {
            return new OrderRequest[size];
        }
    };
}
