package com.hxqc.mall.core.model.order;

/**
 * Author:胡俊杰
 * Date: 2016/6/17
 * FIXME
 * Todo
 */
public enum OrderType {
    other(0),
    /** 4s店预定 */
    store_activity(40),//4s店预定  40
    /** 保养 */
    maintenance(60),//保养 60
    /** 洗车 */
    wash_car(42),//洗车  42
    /** 违章 */
    wei_zhang(30),//违章 30
    /** 换证 */
    replacement(32),//换证 32
    /** 年检 */
    inspection(31),//年检 31

    /** 4s店特价车 */
    store_skill(41),//4s店特价车  41
    /** 用品 */
    accessory(50),//用品  50
    /** 消费买车 */
    buy_car(10),//消费买车 10
    /** 充值 */
    charge(80),//充值
    /** 快修保养 */

    rapidMaintenance(61),//快修保养  61
    /** 快修预约 */

    rapidAppointment(71),//快修预约  71
    /** 自营用品 */
    selfAccessory(51);//   51

    private final int orderType;

    OrderType(int s) {
        orderType= s ;
    }

    public String toString() {
        return String.valueOf(orderType);
    }

    public static OrderType getOrderType(int index) {
        for (OrderType c : OrderType.values()) {
            if (c.orderType == index) {
                return c;
            }
        }
        return other;
    }
}
