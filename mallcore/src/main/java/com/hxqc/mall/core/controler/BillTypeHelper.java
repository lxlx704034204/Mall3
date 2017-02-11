package com.hxqc.mall.core.controler;

import android.content.Context;
import android.text.TextUtils;

import com.hxqc.mall.core.model.IBillType;
import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * Author:李烽
 * Date:2016-04-13
 * FIXME
 * Todo 账单类型管理
 */
public class BillTypeHelper {
    public static final int TYPE_SCORE = 300;
    public static final int TYPE_COMSUMPTION = 100;
    private static volatile BillTypeHelper instance = null;

    private BillTypeHelper() {
    }

    public static BillTypeHelper getInstance() {
        BillTypeHelper billTypeTool = instance;
        if (instance == null) {
            synchronized (BillTypeHelper.class) {
                billTypeTool = instance;
                if (billTypeTool == null) {
                    billTypeTool = new BillTypeHelper();
                    instance = billTypeTool;
                }
            }
        }
        return billTypeTool;
    }

    Type type = null;

    /**
     * 判断类型
     *
     * @param billDetail
     * @return
     */
    public Type getBillDetailType(IBillType billDetail) {
        if (!TextUtils.isEmpty(billDetail.getTransactionType())) {
            getBillType(billDetail);
        } else {
            type = null;
        }
        return type;
    }

    //    @Deprecated
//    private void initType(BillDetail billDetail, int billType) {
//        if (billType == TYPE_SCORE) {
//            type = Type.score;
//        } else
//            switch (billDetail.transactionType) {
//                case "10":
//                    type = Type.charge;
//                    break;
//                case "30":
//                    type = Type.refund;
//                    switch (billDetail.costTypeCode) {
//                        case "10":
//                            type = Type.refund_buy_car;
//                            break;
//                        case "40":
//                            type = Type.refund_store_activity;
//                            break;
//                        case "30":
//                            type = Type.refund_wei_zhang;
//                            break;
//                        case "31":
//                            type = Type.refund_inspection;
//                            break;
//                        case "32":
//                            type = Type.refund_replacement;
//                            break;
//                        case "50":
//                            type = Type.refund_accessory;
//                            break;
//                        case "60":
//                            type = Type.refund_maintenance;
//                            break;
//                        case "41":
//                            type = Type.refund_store_skill;
//                            break;
//                        case "42":
//                            type = Type.refund_wash_car;
//                            break;
//                        case "61":
//                            type = Type.refund_rapidMaintenance;
//                            break;
//                        case "71":
//                            type = Type.refund_rapidAppointment;
//                            break;
//                        case "51":
//                            type = Type.refund_selfAccessory;
//                            break;
//                        case "0":
//                            type = Type.refund;
//                            break;
////                        case "70":
////                            type = Type.repair;
////                            break;
////                        case "80":
////                            type = Type.work_order;
////                            break;
//                    }
//                    break;
//                default:
//                    switch (billDetail.costTypeCode) {
//                        case "10":
//                            type = Type.buy_car;
//                            break;
//                        case "40":
//                            type = Type.store_activity;
//                            break;
//                        case "30":
//                            type = Type.wei_zhang;
//                            break;
//                        case "31":
//                            type = Type.inspection;
//                            break;
//                        case "32":
//                            type = Type.replacement;
//                            break;
//                        case "50":
//                            type = Type.accessory;
//                            break;
//                        case "60":
//                            type = Type.maintenance;
//                            break;
//                        case "41":
//                            type = Type.store_skill;
//                            break;
//                        case "42":
//                            type = Type.wash_car;
//                            break;
//                        //新增
//                        case "61":
//                            type = Type.rapidMaintenance;
//                            break;
//                        case "71":
//                            type = Type.rapidAppointment;
//                            break;
//                        case "51":
//                            type = Type.selfAccessory;
//                            break;
//                        case "0":
//                            type = Type.default_value;
//                            break;
//
////                        case "70":
////                            type = Type.repair;
////                            break;
////                        case "80":
////                            type = Type.work_order;
////                            break;
//                    }
//                    break;
//            }
//    }
    private void getBillType(IBillType billDetail) {
        switch (billDetail.getTransactionType()) {
            case "10":
                type = Type.charge;
                break;
            case "30":
                type = Type.refund;
                switch (billDetail.getCostTypeCode()) {
                    case "10":
                        type = Type.refund_buy_car;
                        break;
                    case "40":
                        type = Type.refund_store_activity;
                        break;
                    case "30":
                        type = Type.refund_wei_zhang;
                        break;
                    case "31":
                        type = Type.refund_inspection;
                        break;
                    case "32":
                        type = Type.refund_replacement;
                        break;
                    case "50":
                        type = Type.refund_accessory;
                        break;
                    case "60":
                        type = Type.refund_maintenance;
                        break;
                    case "41":
                        type = Type.refund_store_skill;
                        break;
                    case "42":
                        type = Type.refund_wash_car;
                        break;
                    case "61":
                        type = Type.refund_rapidMaintenance;
                        break;
                    case "71":
                        type = Type.refund_rapidAppointment;
                        break;
                    case "51":
                        type = Type.refund_selfAccessory;
                        break;
                    case "0":
                        type = Type.refund;
                        break;
//                        case "70":
//                            type = Type.repair;
//                            break;
//                        case "80":
//                            type = Type.work_order;
//                            break;
                    default:
                        type = Type.default_value;
                        break;
                }
                break;
            default:
                switch (billDetail.getCostTypeCode()) {
                    case "10":
                        type = Type.buy_car;
                        break;
                    case "40":
                        type = Type.store_activity;
                        break;
                    case "30":
                        type = Type.wei_zhang;
                        break;
                    case "31":
                        type = Type.inspection;
                        break;
                    case "32":
                        type = Type.replacement;
                        break;
                    case "50":
                        type = Type.accessory;
                        break;
                    case "60":
                        type = Type.maintenance;
                        break;
                    case "41":
                        type = Type.store_skill;
                        break;
                    case "42":
                        type = Type.wash_car;
                        break;
                    //新增
                    case "61":
                        type = Type.rapidMaintenance;
                        break;
                    case "71":
                        type = Type.rapidAppointment;
                        break;
                    case "80":
                        type = Type.charge_other;
                        break;
                    case "51":
                        type = Type.selfAccessory;
                        break;
                    case "0":
                        type = Type.default_value;
                        break;

//                        case "70":
//                            type = Type.repair;
//                            break;
//                        case "80":
//                            type = Type.work_order;
//                            break;
                    default:
                        type = Type.default_value;
                        break;
                }
                break;
        }
    }

    /**
     * 跳转到不同的订单详情页
     *
     * @param context
     * @param orderID
     * @param billDetail
     */
    public void toDetail(Context context, String orderID, IBillType billDetail) {
        type = getBillDetailType(billDetail);
        if (type != null) {
            switch (type) {
                case charge:
                    break;
                case charge_other:
                    ActivitySwitchBase.toOrderDetail(context, orderID);
                    break;
                case refund:
                    ActivitySwitchBase.toOrderDetail(context, orderID);
                    break;
                case refund_buy_car:
                    ActivitySwitchBase.toOrderDetail(context, orderID);
                    break;
                case refund_store_activity:
//                    ActivitySwitcherThirdPartShop.toOrderDetail(context, orderID);
                    break;
                case refund_wei_zhang:
//                    ActivitySwitchAround.toOrderDetailActivity(context, orderID,
//                           OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL);
                    break;
                case refund_inspection:
//                    ActivitySwitchAround.toOrderDetailActivity(context, orderID,
//                           OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL);
                    break;
                case refund_replacement:
//                    ActivitySwitchAround.toOrderDetailActivity(context, orderID,
//                            OrderDetailContants.FRAGMENT_LICENSE_ORDER_DETAIL);
                    break;
                case refund_accessory:
//                    ActivitySwitcherAccessory4S.toOrderDetail(context, orderID);
                    ActivitySwitchBase.toAccessory4SShopOrderDetail(context, orderID);
                    break;
                case refund_maintenance:
//                    ActivitySwitcher.toMaintain4SShopOrderDetail(context, orderID);
                    ActivitySwitchBase.to4SMaintainOrderDetail(context, orderID);
                    break;
                case refund_store_skill:
//                    ActivitySwitcher.toSeckillOrderDetail(context, orderID);
                    break;
                case refund_wash_car:
//                    ActivitySwitcher.toCarWashOrderDetail(context, orderID);
                    break;
                case refund_rapidMaintenance:
//                    ActivitySwitcher.toMaintainOrderDetail(context, orderID);
                    break;
                case refund_rapidAppointment:
//                    ActivitySwitcher.toRepairOrderDetail(context, orderID);
                    break;
                case refund_selfAccessory:
//                    ActivitySwitcherAccessory.toOrderDetail(context, orderID);
                    break;
                case repair:
//                    ActivitySwitcher.toRepairOrderDetail(context, orderID);
                    break;
                case store_activity:
//                    ActivitySwitcherThirdPartShop.toOrderDetail(context, orderID);
                    break;
                case buy_car:
                    ActivitySwitchBase.toOrderDetail(context, orderID);
                    break;
                case maintenance:
//                    ActivitySwitcher.toMaintain4SShopOrderDetail(context, orderID);
                    break;
                case score:
                    break;
                case accessory:
//                    ActivitySwitcherAccessory4S.toOrderDetail(context, orderID);
                    break;
                case wei_zhang:
//                    ActivitySwitchAround.toOrderDetailActivity(context, orderID,
//                            OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL);
                    break;
                case replacement:
//                    ActivitySwitchAround.toOrderDetailActivity(context, orderID,
//                            OrderDetailContants.FRAGMENT_LICENSE_ORDER_DETAIL);
                    break;
                case inspection:
//                    ActivitySwitchAround.toOrderDetailActivity(context, orderID,
//                            OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL);
                    break;
                case wash_car:
//                    ActivitySwitcher.toCarWashOrderDetail(context, orderID);
                    ActivitySwitchBase.toCarWashOrderDetails(context, orderID);
                    break;
                case store_skill:
//                    ActivitySwitcher.toSeckillOrderDetail(context, orderID);
                    break;
                case rapidMaintenance:
//                    ActivitySwitcher.toMaintainOrderDetail(context, orderID);
                    break;
                case rapidAppointment:
                    //快修预约
                    break;
                case selfAccessory:

//                    ActivitySwitcherAccessory.toOrderDetail(context, orderID);
                    break;
                case default_value:
                    break;
            }
        } else {
            //没有数据

        }
    }

    /**
     * 销毁
     */
    public void destory() {
        if (instance != null)
            instance = null;
    }

    public enum Type {
        default_value,//默认
        /*退款*/
        refund,//退款
        refund_store_activity,//4s店预定  40
        refund_maintenance,//保养 60
        //        refund_score,//积分保养
        refund_repair,//维修
        refund_wei_zhang,//违章 30
        refund_replacement,//换证 32
        refund_inspection,//年检 31
        refund_wash_car,//洗车  42
        refund_store_skill,//4s店特价车  41
        refund_accessory,//用品  50
        refund_buy_car,//消费买车 10
        refund_rapidMaintenance,//快修保养  61
        refund_rapidAppointment,//快修预约  71
        refund_selfAccessory,//自营用品   51
        /*消费*/

        store_activity,//4s店预定  40
        maintenance,//保养 60
        score,//积分保养
        repair,//维修
        work_order,//工单
        wei_zhang,//违章 30
        replacement,//换证 32
        inspection,//年检 31
        wash_car,//洗车  42
        store_skill,//4s店特价车  41
        repair_score,//积分维修
        accessory,//用品  50
        buy_car,//消费买车 10
        charge,//充值
        charge_other,//为他人充值
//        score//积分
//        score_used

        //新增
        rapidMaintenance,//快修保养  61
        rapidAppointment,//快修预约  71
        selfAccessory,//自营用品   51
    }
}
