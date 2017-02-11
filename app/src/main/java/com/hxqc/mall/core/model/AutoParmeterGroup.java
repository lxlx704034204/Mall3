package com.hxqc.mall.core.model;

import java.util.ArrayList;

/**
 * 说明:产品参数列表
 *
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class AutoParmeterGroup {
    public ArrayList< AutoParameter > chileParameter;
    public String groupLabel;//参数组名

    /**
     * 说明:产品参数
     *
     * author: 吕飞
     * since: 2015-03-19
     * Copyright:恒信汽车电子商务有限公司
     */
    public static class AutoParameter {
        public String label;//名称 车款，品牌....
        public String value;//内容 奥迪A4，奥迪。。。
    }
}
