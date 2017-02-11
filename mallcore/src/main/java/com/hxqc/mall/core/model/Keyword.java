package com.hxqc.mall.core.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:热门搜索
 *
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class Keyword {
    @Expose
    public String keyword = "";

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Keyword && keyword.equals(((Keyword) o).keyword);
    }
}
