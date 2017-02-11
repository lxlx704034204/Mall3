package com.hxqc.mall.core.model.bill;

import com.hxqc.mall.core.model.IBillType;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-05
 * FIXME
 * Todo 账单详情
 */
public class BillDetail implements IBillType {
    public String description;//内容信息
    public String costTypeCode;//消费类型Code 0默认|10新车|40 4s店预订|50用品|60保养|70维修
    public String costType;//消费类型Code 0默认|10新车|40 4s店预订|50用品|60保养|70维修
    public String transactionType;//类型 10 充值| 20消费|30 退款
    public String amountToPay;//付款金额
    public String orderCreateTime;//订单创建时间
    public String payTime;//付款时间
    public String orderID;//订单ID
    public String paymentID;//支付宝，微信，到店付款等等
    public String tradeID;//交易单号
    public String shopName;//门店名称
    public String payerID;//付款人ID（手机号）
    public String storedID;//充值ID(手机号)
    public String note;//备注
    public String autoPrice;//车价
    public String total;//总计
    public String workOrder;//工单
    public String refundAmount;//退款金额
    public String refundReason;//退款原因
    public String refundTime;//退款时间
    public String score;//分数
    public String erpCode;//查看工单详情使用
    //    public ArrayList<Preferential> couponList;
    public ArrayList<Preferential> preferential;

    @Override
    public String getTransactionType() {
        return transactionType;
    }

    @Override
    public String getCostTypeCode() {
        return costTypeCode;
    }

    @Override
    public String toString() {
        return "BillDetail{" +
                "description='" + description + '\'' +
                ", costTypeCode='" + costTypeCode + '\'' +
                ", costType='" + costType + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amountToPay='" + amountToPay + '\'' +
                ", orderCreateTime='" + orderCreateTime + '\'' +
                ", payTime='" + payTime + '\'' +
                ", orderID='" + orderID + '\'' +
                ", paymentID='" + paymentID + '\'' +
                ", tradeID='" + tradeID + '\'' +
                ", shopName='" + shopName + '\'' +
                ", payerID='" + payerID + '\'' +
                ", storedID='" + storedID + '\'' +
                ", note='" + note + '\'' +
                ", autoPrice='" + autoPrice + '\'' +
                ", total='" + total + '\'' +
                ", workOrder='" + workOrder + '\'' +
                ", refundAmount='" + refundAmount + '\'' +
                ", refundReason='" + refundReason + '\'' +
                ", refundTime='" + refundTime + '\'' +
                ", score='" + score + '\'' +
                ", erpCode='" + erpCode + '\'' +
                ", preferential=" + preferential +
                '}';
    }
}
