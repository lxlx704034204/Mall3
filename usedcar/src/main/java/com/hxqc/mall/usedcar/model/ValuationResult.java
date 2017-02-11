package com.hxqc.mall.usedcar.model;

/**
 * 说明:估价结果
 *
 * @author: 吕飞
 * @since: 2015-08-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class ValuationResult {

    /**
     * buy_price : string,车商收车价：4个价格，分别是车况良好、正常耗损、车况较差、厂商指导价，用英文逗号隔开
     * estimate_price : string,价格
     * sell_price : string,车商售车价：4个价格，分别是车况良好、正常耗损、车况较差、厂商指导价，用英文逗号隔开
     * future_salvage : string,未来5年残值，用英文逗号隔开
     * future_price : string,未来5年价格，用英文逗号隔开
     * brand_logo : string,品牌logo
     */

    public String buy_price;
    public String estimate_price;
    public String sell_price;
    public String match;
    public String future_salvage;
    public String future_price;
    public String brand_logo;
}
