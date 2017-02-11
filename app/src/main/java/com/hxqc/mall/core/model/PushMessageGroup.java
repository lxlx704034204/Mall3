package com.hxqc.mall.core.model;

import java.util.ArrayList;

/**
 * 说明:活动消息组
 *
 * author: 吕飞
 * since: 2015-04-01
 * Copyright:恒信汽车电子商务有限公司
 */
public class PushMessageGroup {
    public String date;
    public ArrayList< PushMessage > pushMessages;

    public PushMessageGroup(String date, ArrayList< PushMessage > pushMessages) {
        this.date = date;
        this.pushMessages = pushMessages;
    }

}
