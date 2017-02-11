package com.hxqc.mall.core.model;

/**
 * Author: 胡俊杰
 * Date: 2015-07-16
 * FIXME
 * Todo  消息列表
 */
public class PushMessage {
    public int isRead;//消息是否已读
    public int messageID;//消息ID
    /**
     * 通知类型           integer   0:活动    1：订单
     */
    public int notificationType;
    /**
     * 消息标题           string
     */
    public String title;
    /**
     * 消息内容           string
     */
    public String content;
    /**
     * 消息推送时间     string （YYYY-mm-dd HH:ii:ss）
     */
    public String date;
    /**
     * 活动页面URL
     */
    public String url;
    /**
     * 订单ID
     */
    public String orderID;

}
