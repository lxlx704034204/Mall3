package com.hxqc.mall.core.model;

import java.util.ArrayList;

/**
 * 说明:订单消息组
 *
 * author: 吕飞
 * since: 2015-04-01
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class OrderMessageGroup {
    public String date;
    public ArrayList< OrderMessage > orderMessages;

    public OrderMessageGroup(String date, ArrayList< OrderMessage > orderMessages) {
        this.date = date;
        this.orderMessages = orderMessages;
    }
}
