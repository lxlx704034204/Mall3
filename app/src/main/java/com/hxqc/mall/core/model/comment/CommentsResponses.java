package com.hxqc.mall.core.model.comment;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-23
 * FIXME
 * 评论列表
 */
public class CommentsResponses extends BaseModel {

    //平均分
    public String averageScore;
    //评论数
    public String commentCount;
    //评论列表
    public ArrayList< CommentModel > comments;

}
