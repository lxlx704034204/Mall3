package com.hxqc.mall.core.model.order;

import android.content.Context;
import android.text.TextUtils;

import com.hxqc.mall.core.R;


/**
 * liaoguilong
 * Created by CPR113 on 2016/5/17.
 * 订单状态类
 */
public class BaseOrderStatus {

    /**
     * 状态颜色(文字判断)
     *
     * @param context
     * @return
     */
    public static int getStatusColor(Context context, String StatusText) {
        if (
                StatusText.equals("待确认") ||
                        StatusText.equals("待付款") ||
                        StatusText.equals("订单关闭") ||
                        StatusText.equals("订单待受理") ||
                        StatusText.equals("待退款") ||
                        StatusText.equals("退款关闭") ||
                        StatusText.equals("部分退款") ||
                        StatusText.equals("待发货") ||
                        StatusText.equals("申请退款") ||
                        StatusText.equals("已下单")) {
            return context.getResources().getColor(R.color.order_status_red);
        } else if (
                StatusText.equals("已付款") ||
                        StatusText.equals("订单已受理") ||
                        StatusText.equals("订单完成") ||
                        StatusText.equals("预约成功") ||
                        StatusText.equals("退款完成") ||
                        StatusText.equals("服务完成") ||
                        StatusText.equals("已付订金") ||
                        StatusText.equals("已完成")) {
            return context.getResources().getColor(R.color.order_status_green);
        } else if (
                StatusText.equals("订单已取消") ||
                        StatusText.equals("订单取消") ||
                        StatusText.equals("退款中") ||
                        StatusText.equals("客户取消") ||
                        StatusText.equals("客服取消") ||
                        StatusText.equals("系统取消") ||
                        StatusText.equals("用户取消")) {
            return context.getResources().getColor(R.color.order_status_orange);
        } else {
            return context.getResources().getColor(R.color.order_status_red);
        }
    }

    /**
     * 洗车订单状态
     */
    public static class CarWashOrderStatus {
        public final static String ORDER_YFK = "10";//20已付款
        public final static String ORDER_DDWC = "20";//30订单完成

        /**
         * 状态颜色
         *
         * @param context
         * @return
         */
        public int getStatusColor(Context context) {
            return context.getResources().getColor(R.color.order_status_green);
        }

        public String orderStatusText; //订单状态 20已付款 30已完成 40退款 string
        public String orderStatus;
        public String refundStatus;
        public String refundStatusText;//退款状态 10-待退款 20-退款中 30-退款完成 number
    }

    /**
     * 保养订单状态
     */
    public static class MaintenanceOrderStatus {
        public final static String ORDER_WFK = "0";//待付款
        public final static String ORDER_YFK = "10";//已付款
        public final static String ORDER_DSL = "30";//订单待受理
        public final static String ORDER_YQX = "50";//订单已取消
        public final static String ORDER_YSL = "70";  //订单已受理
        public final static String ORDER_YSL1 = "72";  //订单已受理
        public final static String ORDER_YSL2 = "73";  //订单已受理
        public final static String ORDER_YSL3 = "74";  //订单已受理
        public final static String ORDER_YSL4 = "76";  //订单已受理
        public final static String ORDER_YSL5 = "77";  //订单已受理
        public final static String ORDER_DDFWWC = "78";  //订单服务完成
        public final static String ORDER_DDWC = "80";  //订单完成

        public final static String REFUND_DTK = "10";  //待退款
        public final static String REFUND_TKZ = "20";  //退款中
        public final static String REFUND_TKWC = "30";  //退款完成
        public final static String REFUND_TKGB = "40";  //退款关闭

        /**
         * 订单状态颜色
         *
         * @param context
         * @return
         */
        public int getStatusColor(Context context) {
            if (orderStatus.equals(ORDER_WFK) ||
                    orderStatus.equals(ORDER_DSL)) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (orderStatus.equals(ORDER_YFK) ||
                    orderStatus.equals(ORDER_YSL) ||
                    orderStatus.equals(ORDER_YSL1) ||
                    orderStatus.equals(ORDER_YSL2) ||
                    orderStatus.equals(ORDER_YSL3) ||
                    orderStatus.equals(ORDER_YSL4) ||
                    orderStatus.equals(ORDER_YSL5) ||
                    orderStatus.equals(ORDER_DDFWWC) ||
                    orderStatus.equals(ORDER_DDWC)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (orderStatus.equals(ORDER_YQX)) {
                return context.getResources().getColor(R.color.order_status_orange);
            } else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }


        /**
         * 退款状态颜色
         *
         * @param context
         * @return
         */
        public int getRefundStatusColor(Context context) {
            if (refundStatus.equals(REFUND_DTK) ) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (refundStatus.equals(REFUND_TKWC) ||
                    refundStatus.equals(REFUND_TKGB)) {
                return context.getResources().getColor(R.color.order_status_green);
            }else if (refundStatus.equals(REFUND_TKZ)) {
                return context.getResources().getColor(R.color.order_status_orange);
            } else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }


        public String orderStatusText;//订单状态 0-待付款，10-已付款，30-订单待受理，50-订单已取消 70-订单已受理' 72订单已受理' 74订单已受理' 76订单已受理' 78订单服务完成 80订单完成 string
        public String orderStatus;
        public String refundStatus;
        public String refundStatusText;//10-待退款 20-退款中 30-退款完成 40-退款关闭 string

        /**
         * 获取显示状态(保养订单详情用)
         * 如果订单状态为10（已付款）
         * 并且退款状态不为空时，显示退款状态
         *
         * @return
         */
        public String getStatusText() {
            if (orderStatus.equals(ORDER_YFK) && !TextUtils.isEmpty(refundStatus) && !TextUtils.isEmpty(refundStatusText)) {
                return refundStatusText;
            } else {
                return orderStatusText;
            }
        }
    }


    /**
     * 保养订单状态
     */
    public static class Maintenance4SShopOrderStatus {
        public final static String ORDER_WFK = "0";//待付款
        public final static String ORDER_YFK = "10";//已付款(线上付款)
        public final static String ORDER_DQR = "20";//待确认（线下支付)
        public final static String ORDER_DSL = "30";//订单待受理
        public final static String ORDER_YQX = "50";//订单已取消
        public final static String ORDER_YQX1 = "51";//订单已取消(ERP未到店)
        public final static String ORDER_YQX2 = "52";//订单已取消(ERP到店)
        public final static String ORDER_YQX3 = "53";//订单已取消(订单超时)
        public final static String ORDER_YSL = "70";  //订单已受理（到店验证）
        public final static String ORDER_DDWC = "80";  //订单完成

        public final static String REFUND_DTK = "10";  //待退款
        public final static String REFUND_TKZ = "20";  //退款中
        public final static String REFUND_TKWC = "30";  //退款完成
        public final static String REFUND_TKGB = "40";  //退款关闭


        /**
         * 订单状态颜色
         *
         * @param context
         * @return
         */
        public int getStatusColor(Context context) {
            if (orderStatus.equals(ORDER_WFK)) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (orderStatus.equals(ORDER_YFK) ||
                    orderStatus.equals(ORDER_YSL) ||
                    orderStatus.equals(ORDER_DDWC)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (orderStatus.equals(ORDER_YQX)||
                    orderStatus.equals(ORDER_YQX1)||
                    orderStatus.equals(ORDER_YQX2)||
                    orderStatus.equals(ORDER_YQX3)
                    ) {
                return context.getResources().getColor(R.color.order_status_orange);
            } else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }

        /**
         * 退款状态颜色
         *
         * @param context
         * @return
         */
        public int getRefundStatusColor(Context context) {
            if (refundStatus.equals(REFUND_DTK) ) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (refundStatus.equals(REFUND_TKWC) ||
                    refundStatus.equals(REFUND_TKGB)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (refundStatus.equals(REFUND_TKZ)) {
                return context.getResources().getColor(R.color.order_status_orange);
            }else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }


        public String orderStatusText;//订单状态 0-待付款，10-已付款(线上付款)，20(线下支付)待确认 30-ERP订单确认， 50-订单已取消 51-订单已取消(ERP未到店) 52订单已取消(ERP到店) 53订单已取消(订单超时) 70-订单已受理(到店验证) 80订单完成 （状态有10和30可申请退款，70以前可以取消订单，） string
        public String orderStatus;
        public String refundStatus;
        public String refundStatusText;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 订单状态为10的时候判断 number

    }


    /**
     * 违章服务，驾驶证换证，车辆年检 （3个订单状态一样）
     */
    public static class WCAOrderStatus {
        public final static String ORDER_DDGB = "-50";//订单关闭
        public final static String ORDER_KFFQX = "-30";//客服取消
        public final static String ORDER_KFQX = "-20";//客户取消
        public final static String ORDER_DDQX = "-10";//订单取消
        public final static String ORDER_DSL = "10";//待受理
        public final static String ORDER_DFK = "20";//待付款
        public final static String ORDER_YFK = "30";//已付款
        public final static String ORDER_WC = "40";  //完成

        public final static String REFUND_WTK = "10";  //未退款
        public final static String REFUND_DTK = "20";  //待退款
        public final static String REFUND_TKZ = "30";  //退款中
        public final static String REFUND_TKWC = "40";  //退款完成
        public final static String REFUND_BFTK = "50";  //部分退款
        public final static String REFUND_BFYTK = "60";  //部分已退款

        /**
         * 状态颜色
         *
         * @param context
         * @return
         */
        public int getStatusColor(Context context) {
            if (orderStatus.equals(ORDER_DSL) ||
                    orderStatus.equals(ORDER_DFK) ||
                    orderStatus.equals(ORDER_DDGB)) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (orderStatus.equals(ORDER_YFK) ||
                    orderStatus.equals(ORDER_WC)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (orderStatus.equals(ORDER_DDQX) ||
                    orderStatus.equals(ORDER_KFQX) ||
                    orderStatus.equals(ORDER_KFFQX)) {
                return context.getResources().getColor(R.color.order_status_orange);
            } else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }

        /**
         * 退款状态颜色
         *
         * @param context
         * @return
         */
        public int getRefundStatusColor(Context context) {
            if (refundStatus.equals(REFUND_DTK)||
                 refundStatus.equals(REFUND_BFTK)) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (refundStatus.equals(REFUND_TKWC)||
                    refundStatus.equals(REFUND_BFYTK)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (refundStatus.equals(REFUND_TKZ)) {
                return context.getResources().getColor(R.color.order_status_orange);
            }else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }


        public String orderStatusText;//  -10订单取消 10待受理 20待付款 30已付款 40完成 number
        public String orderStatus;
        public String refundStatus;
        public String refundStatusText;// 10-待退款 20-退款中 30-退款完成 不用判断订单状态 number
    }

    /**
     * 修车预约
     */
    public static class RepairOrderStatus {
        public final static String ORDER_YYCG = "10";//订单待确认
        public final static String ORDER_DSL = "30";//待受理
        public final static String ORDER_YQX = "50";//已取消
        public final static String ORDER_YQX1 = "51";//已取消
        public final static String ORDER_YSL = "70";//已受理
        public final static String ORDER_YWC = "80";//已完成

        /**
         * 状态颜色
         *
         * @param context
         * @return
         */
        public int getStatusColor(Context context) {
            if (orderStatus.equals(ORDER_YSL)||
                    orderStatus.equals(ORDER_YWC)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (orderStatus.equals(ORDER_YQX) ||
                    orderStatus.equals(ORDER_YQX1)) {
                return context.getResources().getColor(R.color.order_status_orange);
            } else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }

        public String orderStatus;//订单状态 10-预约成功 50-已取消, 70-已受理 number
        public String orderStatusText;// 订单状态 10-预约成功 50-已取消, 70-已受理 string
    }

    /**
     * 特价车订单状态
     */
    public static class SeckillOrderStatus {
        public final static String ORDER_DDGB = "-40";//40订单关闭
        public final static String ORDER_KFQX = "-30";//客服取消
        public final static String ORDER_YHQX = "-20";//用户取消
        public final static String ORDER_DDQX = "-10";//10订单取消
        public final static String ORDER_WFK = "0";//0未付款
        public final static String ORDER_YFK = "10";//10已付款
        public final static String ORDER_YWC = "20";//20已完成

        public final static String REFUND_DTK = "10";  //未退款
        public final static String REFUND_TKZ = "20";  //待退款
        public final static String REFUND_TKWC = "30";  //退款中
        public final static String REFUND_TKGB = "40";  //退款完成

        /**
         * 状态颜色
         *
         * @param context
         * @return
         */
        public int getStatusColor(Context context) {
            if (orderStatusCode.equals(ORDER_DDGB) ||
                    orderStatusCode.equals(ORDER_WFK)) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (orderStatusCode.equals(ORDER_YFK) ||
                    orderStatusCode.equals(ORDER_YWC)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (orderStatusCode.equals(ORDER_DDQX) ||
                    orderStatusCode.equals(ORDER_KFQX) ||
                    orderStatusCode.equals(ORDER_YHQX)) {
                return context.getResources().getColor(R.color.order_status_orange);
            } else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }

        /**
         * 退款状态颜色
         *
         * @param context
         * @return
         */
        public int getRefundStatusColor(Context context) {
            if (refundStatus.equals(REFUND_DTK) || refundStatus.equals(REFUND_TKZ)) { //未退款，待退款
                return context.getResources().getColor(R.color.order_status_red);
            } else if (refundStatus.equals(REFUND_TKGB)) { //退款完成
                return context.getResources().getColor(R.color.order_status_green);
            } else if (refundStatus.equals(REFUND_TKWC) ) {  //退款中
                return context.getResources().getColor(R.color.order_status_orange);
            }
            else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }


        public String orderStatus;//
        public String orderStatusCode;// -40订单关闭 -10订单取消 0未付款 10已付款 20已完成 number
        public String refundStatus;
        public String refundStatusText;// 10-待退款 20-退款中 30-退款完成 40-退款关闭 string

        public String getOrderStatus() {
            if(TextUtils.isEmpty(refundStatusText))
                return  orderStatus;
            else
                return  refundStatusText;
        }
    }

    /**
     * 用品订单状态
     */
    public static class AccessoryOrderStatus {
        public final static String ORDER_DFK = "0";//待付款
        public final static String ORDER_YFK = "10";//已付款
        public final static String ORDER_DQR = "20";//待确认
        public final static String ORDER_DSL = "30";//订单待受理
        public final static String ORDER_YQX = "50";//订单已取消
        public final static String ORDER_YSL = "70";  //订单已受理
        public final static String ORDER_YSL1 = "72";  //订单已受理
        public final static String ORDER_YSL2 = "73";  //订单已受理
        public final static String ORDER_YSL3 = "74";  //订单已受理
        public final static String ORDER_YSL4 = "76";  //订单已受理
        public final static String ORDER_YSL5 = "77";  //订单已受理
        public final static String ORDER_DDFWWC = "78";  //订单服务完成
        public final static String ORDER_DDWC = "80";  //订单完成


        public final static String REFUND_DTK = "10";  //退款中
        public final static String REFUND_TKZ = "20";  //退款中
        public final static String REFUND_TKWC = "30";  //退款完成
        public final static String REFUND_TKGB = "40";  //退款关闭

        /**
         * 订单状态颜色
         *
         * @param context
         * @return
         */
        public int getStatusColor(Context context) {
            if (orderStatus.equals(ORDER_DFK) ||
                    orderStatus.equals(ORDER_DSL) ||
                    orderStatus.equals(ORDER_DQR)) {
                return context.getResources().getColor(R.color.order_status_red);
            } else if (orderStatus.equals(ORDER_YFK) ||
                    orderStatus.equals(ORDER_YSL) ||
                    orderStatus.equals(ORDER_YSL1) ||
                    orderStatus.equals(ORDER_YSL2) ||
                    orderStatus.equals(ORDER_YSL3) ||
                    orderStatus.equals(ORDER_YSL4) ||
                    orderStatus.equals(ORDER_YSL5) ||
                    orderStatus.equals(ORDER_DDFWWC) ||
                    orderStatus.equals(ORDER_DDWC)) {
                return context.getResources().getColor(R.color.order_status_green);
            } else if (orderStatus.equals(ORDER_YQX)) {
                return context.getResources().getColor(R.color.order_status_orange);
            } else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }

        /**
         * 退款状态颜色
         *
         * @param context
         * @return
         */
        public int getRefundStatusColor(Context context) {
          if (refundStatus.equals(REFUND_TKWC) ||
                    refundStatus.equals(REFUND_TKGB)) {
                return context.getResources().getColor(R.color.order_status_green);
            }else if (refundStatus.equals(REFUND_DTK) || orderStatus.equals(REFUND_TKZ)) {
                return context.getResources().getColor(R.color.order_status_orange);
            }
            else {
                return context.getResources().getColor(R.color.order_status_red);
            }
        }


        public String orderStatusText;//订单状态 0-待付款，10-已付款，30-订单待受理，50-订单已取消 70-订单已受理' 72订单已受理' 74订单已受理' 76订单已受理' 78订单服务完成 80订单完成 string
        public String orderStatus;//订单状态 0-待付款，10-已付款，30-订单待受理，50-订单已取消 70-订单已受理' 72订单已受理' 74订单已受理' 76订单已受理' 78订单服务完成 80订单完成 number

        public String refundStatus;
        public String refundStatusText;//10-待退款 20-退款中 30-退款完成 40-退款关闭 string
    }




}
