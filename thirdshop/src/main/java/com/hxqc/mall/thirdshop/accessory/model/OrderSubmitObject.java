package com.hxqc.mall.thirdshop.accessory.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:订单提交类
 *
 * @author: 吕飞
 * @since: 2016-04-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderSubmitObject extends ShoppingCartSubmitObject{
    @Expose
    public String notes;//备注
}
