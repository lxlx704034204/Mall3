package com.hxqc.autonews.model;

import java.util.ArrayList;

/**
 * 评论详情
 * Created by huangyi on 16/10/25.
 */
public class CommentDetail {

    public Comment parentComment; //主评论
    public ArrayList<Comment> chileCommentlist; //子评论列表
}
