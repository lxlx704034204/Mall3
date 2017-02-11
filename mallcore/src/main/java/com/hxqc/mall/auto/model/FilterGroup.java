package com.hxqc.mall.auto.model;

import java.util.ArrayList;

/**
 * 说明:找车条件列表
 *
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class FilterGroup {
    public ArrayList< Filter > filterItem;//筛选内容
    public String filterLabel;//条件标签
    private Filter defaultFilter;

    public FilterGroup(String filterLabel) {
        this.filterLabel = filterLabel;
    }

    public Filter getDefaultFilter() {
        return defaultFilter;
    }

    public void setDefaultFilter(Filter defaultFilter) {
        this.defaultFilter = defaultFilter;
    }
}
