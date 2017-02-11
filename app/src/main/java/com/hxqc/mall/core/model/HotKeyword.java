package com.hxqc.mall.core.model;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Author: 胡俊杰
 * Date: 2015-07-02
 * FIXME
 * Todo 热门关键字
 */
public class HotKeyword {
    //热门关键字
    public ArrayList<Keyword> hotKeywords;
    //推荐位
    private String recommand;

    public String getRecommand() {
        if (TextUtils.isEmpty(recommand)) {
            return "搜索";
        } else {
            return recommand;
        }
    }
}
