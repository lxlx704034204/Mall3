package com.hxqc.mall.thirdshop.model;

/**
 * Function: 特价车中预付款时的选择条件Bean
 *
 * @author 袁秉勇
 * @since 2016年05月06日
 * <p/>
 * <p/>
 * 购车方式[
 *          {
 *              key:
 *                  用于显示例如 分期 置换等 string
 *              value:
 *                  用于生成订单时上传 string
 *              isDefault:
 *                  是否默认 boolean
 *          }
 *         ]
 */
public class PurchaseArgDetail {
    public String key;
    public String value;
    public boolean isDefault;
}
