package com.hxqc.mall.core.model;

/**
 * 说明:基本信息（关注列表用）
 * <p>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class CollectInfo extends NewAutoForHome {
    public int collectCount;//关注数
    public String itemDescription = "";//商品名称描述
    public boolean collectState = true;//关注状态

    public String getItemDescription() {
        return itemDescription;
    }
}
