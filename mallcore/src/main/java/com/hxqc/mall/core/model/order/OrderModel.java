package com.hxqc.mall.core.model.order;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.auto.PickupPointT;

/**
 * Author: wanghao
 * Date: 2015-03-27
 * FIXME
 * -20' => '客服取消',
 * '-10' => '用户取消',
 * ‘-1’ => '系统取消'
 * '0' => '等待付款',
 * '10' => '部分支付',
 * '20' => '部分支付，上门收款',
 * '25' => '上门收款受理中',
 * '26' => '分期办理受理中',
 * '27' => '分期办理失败',
 * '30' => '支付完成',
 * '40' => '送货中',
 * '45'=> '申请退货',
 * '50' => '退货受理中',
 * '60' => '申请退款',
 * '70' => '退款已受理',
 * '80' => '退款已审核',
 * '90' => '退款进行中',
 * '100' => '退款完成',
 * '55' => '退货完成',
 * '120' => '申请换货',
 * '130' => '换货受理中',
 * '140' => '完成换货',
 * '150' => '订单完成',
 * '160' => '客户拒签',
 * 订单
 */
public class OrderModel extends BaseOrder implements Parcelable {
    public OrderModel() {
        super();
    }

    @Expose
    public String orderPurchaseTax;//购置税
    @Expose
    public String orderTravelTax;//预估车船税
    @Expose
    public String canComment = "0"; //是否可以评价。 0.不可以，1.可以
    @Expose
    public String installmentInfo;//分期资料张数
    @Expose
    public String insurance;//是否办理保险 0.不办理，1.办理
    @Expose
    public String userIdentifier; //收货人证件号码
    //---------------
    @Expose
    public String city;//收货人所在市名称
    @Expose
    public String cityID;//收货人所在市ID
    @Expose
    public String district;//收货人所在区名称
    @Expose
    public String districtID;// 收货人所在区ID
    @Expose
    public String expiredTime;//订单过期时间
    @Expose
    public String hasComment;//是否已发表评论
    @Expose
    public String expressType; //配送方式：送货上门，线下自提
    @Expose
    public String orderAmount;//总计
    @Expose
    public String orderExpressFee;//运费
    @Expose
    public String orderID;//订单ID
    @Expose
    public String orderInsuranceFee;//保险费
    @Expose
    public String orderPaid;//已付款金额
    @Expose
    public String orderServiceFee;//上牌服务费
    @Expose
    public String orderStatus; //订单状态：发货中，已签收等
    @Expose
    public String orderStatusTime;//订单状态更新时间
    @Expose
    public String orderTaxFee;//税费
    @Expose
    public String orderType;//订单类型：0.一般訂單  1.特賣訂單
    @Expose
    public String orderUnpaid;//未付款金额
    @Expose
    public String paymentStatus;//付款状态：0.未付款，1.部分付款，2.已付款
    @Expose
    public String paymentType;   //支付方式：0.线上全款，2.分期，1.线下（上门收款）
    @Expose
    public String province;//收货人所在省名称
    @Expose
    public String serverTime;//服务器时间
    @Expose
    public String provinceID;//收货人所在省ID
    @Expose
    public String userAddress;//收货人地址
    @Expose
    public String userFullname="";//收货人姓名
    @Expose
    public String itemID;//商品ID
    @Expose
    public String userPhoneNumber;//收货人电话
    @Expose
    public String orderStatusCode;//订单状态码
    @Expose
    public boolean mHasPaymentType;//是否有付款方式
    @Expose
    public PickupPointT PickupPoint;//自提点
    @Expose
    public String promotionStatus;//特卖状态，10为特卖进行中，20为特卖下架
    @Expose
    public String promotionID;//特卖ID

    public String paymentID;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string
    public String paymentIDText;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string

    public boolean getPromotionStatus() {
        return "10".equals(promotionStatus);
    }

    public String getOrderPurchaseTax() {
        if (TextUtils.isEmpty(orderPurchaseTax)) {
            return "0.00";
        }
        return orderPurchaseTax;
    }

    public String getOrderTravelTax() {
        if (TextUtils.isEmpty(orderTravelTax)) {
            return "0.00";
        }
        return orderTravelTax;
    }

    public String getInstallmentInfo() {
        return installmentInfo + "张";
    }

    public String getExpressType(Context context) {
        try {
            if (Integer.parseInt(expressType) < 3) {
                String[] mExpressType = {context.getResources().getString(R.string.me_no_choice), context.getResources().getString(R.string.me_pickup_point), context.getResources().getString(R.string.me_to_home)};
                return mExpressType[Integer.parseInt(expressType)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getOrderType() {
        if (!TextUtils.isEmpty(orderType)) return orderType;
        return "0";
    }

    public String getPaymentType(Context context) {
        try {
            if (Integer.parseInt(paymentType) < 3) {
                String[] mPaymentType = {context.getResources().getString(R.string.me_online_pay), context.getResources().getString(R.string.me_pay_deposit), context.getResources().getString(R.string.me_instalments)};
                return mPaymentType[Integer.parseInt(paymentType)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getItemName() {
        return itemName;
    }

    public String getOrderStatus() {
        switch (orderStatusCode) {
            case "-20":
                return "用户取消";
            case "0":
                return "等待付款";
            case "20":
                return "已付订金";
//            case "26":
//                return "部分支付";
            case "26":
                return "分期办理受理中";
            case "70":
                return "退款已受理";
            case "80":
                return "退款已审核";
            case "90":
                return "退款进行中";
            case "55":
                return "退货完成";
            case "120":
                return "申请换货";
            case "130":
                return "换货受理中";
            case "140":
                return "换货完成";
            case "150":
                return "订单完成";
            case "160":
                return "客户拒签";
            default:
                return orderStatus;
        }

    }

    public String getUserAddress() {
        return province + city + district + userAddress;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.userIdentifier);
        dest.writeString(this.city);
        dest.writeString(this.cityID);
        dest.writeString(this.district);
        dest.writeString(this.districtID);
        dest.writeString(this.expiredTime);
        dest.writeString(this.hasComment);
        dest.writeString(this.expressType);
        dest.writeString(this.orderAmount);
        dest.writeString(this.orderExpressFee);
        dest.writeString(this.orderID);
        dest.writeString(this.orderInsuranceFee);
        dest.writeString(this.orderPaid);
        dest.writeString(this.orderServiceFee);
        dest.writeString(this.orderStatus);
        dest.writeString(this.orderStatusTime);
        dest.writeString(this.orderTaxFee);
        dest.writeString(this.orderType);
        dest.writeString(this.orderUnpaid);
        dest.writeString(this.paymentStatus);
        dest.writeString(this.paymentType);
        dest.writeString(this.province);
        dest.writeString(this.serverTime);
        dest.writeString(this.provinceID);
        dest.writeString(this.userAddress);
        dest.writeString(this.userFullname);
        dest.writeString(this.itemID);
        dest.writeString(this.userPhoneNumber);
        dest.writeString(this.orderStatusCode);
        dest.writeByte(mHasPaymentType ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.PickupPoint, 0);
    }

    protected OrderModel(Parcel in) {
        super(in);
        this.userIdentifier = in.readString();
        this.city = in.readString();
        this.cityID = in.readString();
        this.district = in.readString();
        this.districtID = in.readString();
        this.expiredTime = in.readString();
        this.hasComment = in.readString();
        this.expressType = in.readString();
        this.orderAmount = in.readString();
        this.orderExpressFee = in.readString();
        this.orderID = in.readString();
        this.orderInsuranceFee = in.readString();
        this.orderPaid = in.readString();
        this.orderServiceFee = in.readString();
        this.orderStatus = in.readString();
        this.orderStatusTime = in.readString();
        this.orderTaxFee = in.readString();
        this.orderType = in.readString();
        this.orderUnpaid = in.readString();
        this.paymentStatus = in.readString();
        this.paymentType = in.readString();
        this.province = in.readString();
        this.serverTime = in.readString();
        this.provinceID = in.readString();
        this.userAddress = in.readString();
        this.userFullname = in.readString();
        this.itemID = in.readString();
        this.userPhoneNumber = in.readString();
        this.orderStatusCode = in.readString();
        this.mHasPaymentType = in.readByte() != 0;
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        public OrderModel createFromParcel(Parcel source) {
            return new OrderModel(source);
        }

        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    @Override
    public String toString() {
        return "OrderModel{" +
                "orderPurchaseTax='" + orderPurchaseTax + '\'' +
                ", orderTravelTax='" + orderTravelTax + '\'' +
                ", canComment='" + canComment + '\'' +
                ", installmentInfo='" + installmentInfo + '\'' +
                ", insurance='" + insurance + '\'' +
                ", userIdentifier='" + userIdentifier + '\'' +
                ", city='" + city + '\'' +
                ", cityID='" + cityID + '\'' +
                ", district='" + district + '\'' +
                ", districtID='" + districtID + '\'' +
                ", expiredTime='" + expiredTime + '\'' +
                ", hasComment='" + hasComment + '\'' +
                ", expressType='" + expressType + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", orderExpressFee='" + orderExpressFee + '\'' +
                ", orderID='" + orderID + '\'' +
                ", orderInsuranceFee='" + orderInsuranceFee + '\'' +
                ", orderPaid='" + orderPaid + '\'' +
                ", orderServiceFee='" + orderServiceFee + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderStatusTime='" + orderStatusTime + '\'' +
                ", orderTaxFee='" + orderTaxFee + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderUnpaid='" + orderUnpaid + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", province='" + province + '\'' +
                ", serverTime='" + serverTime + '\'' +
                ", provinceID='" + provinceID + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userFullname='" + userFullname + '\'' +
                ", itemID='" + itemID + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", orderStatusCode='" + orderStatusCode + '\'' +
                ", mHasPaymentType=" + mHasPaymentType +
                ", PickupPoint=" + PickupPoint +
                '}';
    }
}
