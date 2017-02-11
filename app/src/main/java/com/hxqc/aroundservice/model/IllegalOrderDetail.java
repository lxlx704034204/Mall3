package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 25
 * FIXME
 * Todo 违章订单详情
 */
public class IllegalOrderDetail implements Parcelable {

    /**
     * orederID: 订单号
     * plateNumber 车牌号
     * username : 联系人姓名string
     * phone : 联系人电话string
     * fine : 罚款金额number
     * serviceCharge : 服务费number
     * amount : 总额number
     * count : 违章条数 number
     * fen : 违章总分number
     * drivingLicenseFile1 : 行驶证正面string
     * drivingLicenseFile2 : 行驶证反面string
     * orderStatus : 订单状态0待受理10可付款30已付款50已取消number
     * orderStatusText : 订单状态0待受理10可付款30已付款50已取消number
     * refundStatus:	退款状态 10-待退款 20-退款中 30-退款完成 不用判断订单状态 number
     * refundStatusText:	退款状态 10-待退款 20-退款中 30-退款完成 string
     * refundType:退款方式
     * refundNumber:退款单号
     * orderCreateTime:订单创建时间
     * tradeID: 交易单号
     * paidTime: 付款时间
     * paymentID: 付款方式
     * wzList : [{"wzID":"违章IDstring","date":"违章时间string","area":"违章地点string","act":"违章行为string","code":"违章代码string","fen":"违章扣分number","money":"违章罚款string","handled":"是否处理string","PayNo":"交款编号string","longitude":"经度number","latitude":"纬度number"}]
     * city 城市
     * hpzl 号牌种类编号
     * engineno 发动机号
     * classno 车架号
     *
     */

    public String orederID;
    public String plateNumber;
    public String username;
    public String phone;
    public String fine;
    public String serviceCharge;
    public String amount;
    public String count;
    public String fen;
    public String drivingLicenseFile1;
    public String drivingLicenseFile2;
    public String orderStatus;
    public String orderStatusText;
    public String refundStatus;
    public String refundStatusText;
    public String refundType;
    public String refundNumber;
    public String orderCreateTime;
    public String tradeID;
    public String paidTime;
    public String paymentID;
    public ArrayList<IllegalQueryResultInfo> wzList;
    public String city;
    public String cityName;
    public String provinceName;
    public String hpzl;
    public String engineno;
    public String classno;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orederID);
        dest.writeString(this.plateNumber);
        dest.writeString(this.username);
        dest.writeString(this.phone);
        dest.writeString(this.fine);
        dest.writeString(this.serviceCharge);
        dest.writeString(this.amount);
        dest.writeString(this.count);
        dest.writeString(this.fen);
        dest.writeString(this.drivingLicenseFile1);
        dest.writeString(this.drivingLicenseFile2);
        dest.writeString(this.orderStatus);
        dest.writeString(this.orderStatusText);
        dest.writeString(this.refundStatus);
        dest.writeString(this.refundStatusText);
        dest.writeString(this.refundType);
        dest.writeString(this.refundNumber);
        dest.writeString(this.orderCreateTime);
        dest.writeString(this.tradeID);
        dest.writeString(this.paidTime);
        dest.writeString(this.paymentID);
        dest.writeTypedList(wzList);
        dest.writeString(this.city);
        dest.writeString(this.hpzl);
        dest.writeString(this.engineno);
        dest.writeString(this.classno);

    }

    public IllegalOrderDetail() {
    }

    protected IllegalOrderDetail(Parcel in) {
        this.orederID = in.readString();
        this.plateNumber = in.readString();
        this.username = in.readString();
        this.phone = in.readString();
        this.fine = in.readString();
        this.serviceCharge = in.readString();
        this.amount = in.readString();
        this.count = in.readString();
        this.fen = in.readString();
        this.drivingLicenseFile1 = in.readString();
        this.drivingLicenseFile2 = in.readString();
        this.orderStatus = in.readString();
        this.orderStatusText = in.readString();
        this.refundStatus = in.readString();
        this.refundStatusText = in.readString();
        this.refundType = in.readString();
        this.refundNumber = in.readString();
        this.orderCreateTime = in.readString();
        this.tradeID = in.readString();
        this.paidTime = in.readString();
        this.paymentID = in.readString();
        this.wzList = in.createTypedArrayList(IllegalQueryResultInfo.CREATOR);
        this.city = in.readString();
        this.hpzl = in.readString();
        this.engineno = in.readString();
        this.classno = in.readString();
    }

    public static final Parcelable.Creator<IllegalOrderDetail> CREATOR = new Parcelable.Creator<IllegalOrderDetail>() {
        @Override
        public IllegalOrderDetail createFromParcel(Parcel source) {
            return new IllegalOrderDetail(source);
        }

        @Override
        public IllegalOrderDetail[] newArray(int size) {
            return new IllegalOrderDetail[size];
        }
    };

    @Override
    public String toString() {
        return "IllegalOrderDetail{" +
                "orederID='" + orederID + '\'' +
                "plateNumber='" + plateNumber + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", fine='" + fine + '\'' +
                ", serviceCharge='" + serviceCharge + '\'' +
                ", amount='" + amount + '\'' +
                ", count='" + count + '\'' +
                ", fen='" + fen + '\'' +
                ", drivingLicenseFile1='" + drivingLicenseFile1 + '\'' +
                ", drivingLicenseFile2='" + drivingLicenseFile2 + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderStatusText='" + orderStatusText + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", refundStatusText='" + refundStatusText + '\'' +
                ", refundType='" + refundType + '\'' +
                ", refundNumber='" + refundNumber + '\'' +
                ", orderCreateTime='" + orderCreateTime + '\'' +
                ", tradeID='" + tradeID + '\'' +
                ", paidTime='" + paidTime + '\'' +
                ", paymentID='" + paymentID + '\'' +
                ", wzList=" + wzList +
                ", city='" + city + '\'' +
                ", hpzl='" + hpzl + '\'' +
                ", engineno='" + engineno + '\'' +
                ", classno='" + classno + '\'' +
                '}';
    }
}
