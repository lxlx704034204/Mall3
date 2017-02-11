package com.hxqc.mall.core.model;

/**
 * 说明:个人界面附加信息
 * <p/>
 * author: 吕飞
 * since: 2015-03-20
 * Copyright:恒信汽车电子商务有限公司
 */
public class SubjoinInfo {
    public String collectCount;//关注条数
    public String messageCount;//未读消息条数
    public int mallOrderCount;//订单条数
    public int shopOrderCount;//店铺条数

    public String getCollectCount() {
        return collectCount.equals("0") ? "" : collectCount;
    }

    public String getOrderCount() {
        int count = mallOrderCount + shopOrderCount;
        return count == 0 ? "" : String.valueOf(count);
    }


    public String getMessageCount() {
        return Integer.parseInt(messageCount) < 100 ? messageCount : "99";
    }
}
