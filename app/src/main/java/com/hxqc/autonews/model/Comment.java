package com.hxqc.autonews.model;

/**
 * 主评论 子评论 共用
 * Created by huangyi on 16/10/25.
 */
public class Comment {

    /**
     * 共有部分
     **/
    public UserInfo commentUser; //回复人
    public String time; //评论时间
    public String commentID; //主评论id
    public String content; //评论内容

    /**
     * 主评论特有
     **/
    public int chileCommentCount; //子评论条数

    /**
     * 子评论特有
     **/
    public UserInfo toUser; //回复人 可能为null

    /**
     * 资讯评价特有
     **/
    public String status; //状态
    public String statusText; //状态
}
