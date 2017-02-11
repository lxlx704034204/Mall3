package com.hxqc.mall.core.util.comment;


/**
 * Author: wanghao
 * Date: 2015-07-10
 * FIXME
 * 评论工具类
 */
public class CommentUtils {

    /**
     * 评分加点
     */
    public static String formatScore(String scror) {
        if (scror.length() == 1) {
            return scror + ".0";
        } else {
            return scror;
        }
    }

}
