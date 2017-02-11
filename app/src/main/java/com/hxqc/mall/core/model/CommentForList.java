package com.hxqc.mall.core.model;


import com.hxqc.mall.core.model.order.BaseOrder;

/**
 * 说明:我的评论列表中的评论
 *
 * author: 吕飞
 * since: 2015-04-02
 * Copyright:恒信汽车电子商务有限公司
 */
public class CommentForList extends BaseOrder {
    public String commentID;//评论ID
    public int commentStatus;//评论状态 0.没有评论 1.已有评论 2.已经追加过评论
    public String itemID;//商品ID
    public String itemTitle;//商品名称
    public String orderID;//订单ID
    public String sku;//商品SKU


    public String getItemDescription() {
        return itemTitle;
    }

    public String getItemName() {
        return itemTitle;
    }

}
