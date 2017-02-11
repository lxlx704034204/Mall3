package com.hxqc.mall.thirdshop.interfaces;

import com.hxqc.mall.auto.model.Filter;
import com.hxqc.mall.auto.model.FilterGroup;

/**
 * Author: HuJunJie
 * Date: 2015-04-03
 * FIXME
 * Todo 筛选回调
 */
public interface FilterAction {
    void filterListener(int position, Filter filter, FilterGroup filterGroup);
}
