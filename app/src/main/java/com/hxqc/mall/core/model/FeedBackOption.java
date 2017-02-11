package com.hxqc.mall.core.model;

/**
 * Author:李烽
 * Date:2016-08-16
 * FIXME
 * Todo 投诉类型
 */
public class FeedBackOption {
    public String adviceID;
    public String adviceTitle;
    public boolean isCheck;

    public FeedBackOption() {
    }

    public FeedBackOption(String adviceID, String adviceTitle) {
        this.adviceID = adviceID;
        this.adviceTitle = adviceTitle;
    }
}
