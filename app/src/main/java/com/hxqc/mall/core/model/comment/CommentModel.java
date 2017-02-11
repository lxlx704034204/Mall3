package com.hxqc.mall.core.model.comment;

import com.hxqc.mall.core.model.ImageModel;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-23
 *
 * FIXME
 * 评论
 */
public class CommentModel {


    //评论id
    public String commentID;
    //评论内容
    public String content;
    //评论时间
    public String createTime;
    //图组
    public ArrayList< ImageModel > images;
    //平分
    public String score;
    //头像
    public String userAvatar;
    //昵称
    public String userNickname;
    //追加评论
    public AdditionModel addition;
    //车身颜色
    public String itemColor;
    //内饰颜色
    public String itemInterior;

    public CommentReply reply;//商家回复

}
