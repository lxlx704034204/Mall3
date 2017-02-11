package com.hxqc.mall.core.model.order;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/1.
 * 4S店促销活动Bean
 */
public class ShopPormotionBean {
   public String shopTel;//店铺电话 string
    public String orderID;//订单ID string
    public ShopPormation promotion;//店铺促销信息(数据结构同 第三方店铺 店铺促销信息) { }
    public int orderStatusCode;//订单状态码 number
    public String orderStatus;//订单状态 string
    public int paymentStatusCode;//付款状态码 number
    public String paymentStatus;//付款状态 string
    public String subscription;//订金金额（无订金返回0） number



    /**
     * 店铺促销信息
     */
    public static class ShopPormation{
        public String promotionID;//促销ID string
        public String  title;//标题 string
        public String  summary;//简介 string
        public String  shopTitle;//店铺简称 string
        public String  thumb;//促销图片URL string
        public String   publishDate;//发布时间(Y-m-d) string
        public String serverTime;//服务器时间 string Y-m-d H:i:s格式返回
        public String   startDate;//开始时间(Y-m-d) string
        public String  endDate;//结束时间(Y-m-d) string
        public int  status;//活动状态（10:未发布 20:发布 30:下线） number
        public int   paymentAvailable;//是否可付款（10:不可付 20:可付） number

        public String getEndDateByTime() {
            return endDate + " 23:59:59";
        }
    }
}
