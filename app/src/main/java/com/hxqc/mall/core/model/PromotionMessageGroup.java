package com.hxqc.mall.core.model;

import java.util.ArrayList;

/**
 * 说明:活动消息组
 *
 * author: 吕飞
 * since: 2015-04-01
 * Copyright:恒信汽车电子商务有限公司
 */
public class PromotionMessageGroup {
    public String date;
    public ArrayList< PromotionMessage > promotionMessages;

    public PromotionMessageGroup(String date, ArrayList< PromotionMessage > promotionMessages) {
        this.date = date;
        this.promotionMessages = promotionMessages;
    }

}
