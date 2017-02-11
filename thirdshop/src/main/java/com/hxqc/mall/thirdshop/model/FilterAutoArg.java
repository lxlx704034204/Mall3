package com.hxqc.mall.thirdshop.model;

import java.util.ArrayList;

/**
 * 说明:4s店新车销售筛选条件。除品牌和车系除外的筛选条件
 *
 * @author: 吕飞
 * @since: 2016-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class FilterAutoArg {
    public String filterLabel;//条件标签
    public ArrayList<FilterItem> filterItem;//筛选内容
}
