package com.hxqc.mall.core.model;

/**
 * 说明:首页推荐活动
 *
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class SlideShowItem {
    public String slide;//活动图片的URL
    public int type;//1. 活动页 2.产品详情页
    public String url;//对应的URL。活动页采用HTML5，则直接返回URL。产品详情页返回的格式为hxmall://ProductDetail/sku。（注意，不是http开头，前两部分保持不变，只变更sku）
}
