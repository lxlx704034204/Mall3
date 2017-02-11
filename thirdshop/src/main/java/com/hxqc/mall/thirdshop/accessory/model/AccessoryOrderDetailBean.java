package com.hxqc.mall.thirdshop.accessory.model;

import android.text.TextUtils;

import com.hxqc.mall.core.model.InvoiceModel;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.model.order.BaseOrderStatus;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * liaoguilong
 * Created by CPR113 on 2016/6/6.
 * 用品订单详情bean
 */
public class AccessoryOrderDetailBean extends BaseOrderStatus.AccessoryOrderStatus  {

    public String captcha;//验证码 string
    public String  orderCreateTime;//下单时间，yyyy-mm-dd hh:mm:ss string
    public String contactPhone;//联系人手机 string
    public String contactName;//联系人姓名 string
    public String  orderID;//订单号 string
    public ArrayList<RefundInfo>  refund;//退款信息[]
    public String shopTitle;//店铺简称 string
    public String  orderAmount;//订单金额 string
    public String  transactionNumber;//交易单号 string
    public String  payment;//支付方式 string
    public String  shopPhoto;//店铺图片url string
    public String  shopID;//店铺ID string
    public String  paymentIDText;//支付方式 string
    public String  paymentID;//支付方式
    public String  amountPayable;//优惠价格
    public PickupPointT shopLocation;//店铺地址信息 { }
    public PickupPointT shopInfo;//店铺地址信息 { }
    public ArrayList<ShoppingCartItem> itemList;
    public InvoiceModel invoice; //发票信息
    public ArrayList<ShoppingCartItem> productList;//(4s店用品)
    public String hasComment;// 是否有评论 String
    public String  erpShopCode;
    public String  workOrderID;

    public boolean getHasComment() {
        return hasComment.equals("1")?true:false;
    }



    public  boolean isCancel(){
        if(orderStatus.equals(ORDER_DFK) || (orderStatus.equals(ORDER_DQR)  && paymentID.equals(PayConstant.INSHOP)) || (orderStatus.equals(ORDER_DSL) && paymentID.equals(PayConstant.INSHOP)))
            return true;
        return false;
    }

    public  boolean isRefund(){
        if((orderStatus.equals(ORDER_YFK) || (orderStatus.equals(ORDER_DSL) && !paymentID.equals(PayConstant.INSHOP)))
                && !refundStatus.equals(REFUND_DTK)
                && !refundStatus.equals(REFUND_TKZ)
                && !refundStatus.equals(REFUND_TKWC)
                && !refundStatus.equals(REFUND_TKGB)
                && TextUtils.isEmpty(workOrderID)
                && isInsideRefundTime(orderCreateTime))
            return true;
        return false;
    }

    /**
     * 满足3个月退款时间以内
     * @param orderCreateTime
     * @return
     */
    public boolean isInsideRefundTime(String orderCreateTime){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        if(DateUtil.str2Date(orderCreateTime).after(calendar.getTime()))
        return true;
        return false;
    }

    public class RefundInfo{
        public String  refundStatus;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 订单状态为10的时候判断 number
        public String  refundStatusText;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 string
        public String  refundAmount;//退款金额 number
        public String  refundTime;//退款时间 y-m-d h:m:s string
        public String  refundNumber;//退款单号 string
        public String  refundType;//退款方式 支付宝 微信 线下 string
        public String   refundDescription;//退款原因描述 string
    }
}
