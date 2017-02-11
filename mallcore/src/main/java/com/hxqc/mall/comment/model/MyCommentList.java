package com.hxqc.mall.comment.model;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-05-30
 * FIXME
 * Todo 我的评论列表model
 */
public class MyCommentList  {
    public String orderID;  //订单号 string
    public String orderType; //订单类型 10洗车评论 20保养评论 string
    public String shopID;  //店铺ID string
    public String shopType;  //店铺类型 10洗车店 20快修店 30 4S店 string
    public String shopName;  //店铺名称 string
    public String commentID; //评论ID string
    public String content;  //评论内容 string
    public String userNickname; //用户昵称 string
    public String createTime;  //评论时间 string
    public String score;  //评分 number
    public String userAvatar;  //头像Url string
    public ArrayList<String> tags;  //评论标签[]
    public ArrayList<MyCommentImage> images; //用户晒单图数组[]
    public int positionTag;


    public class MyCommentImage {
        public String thumbImage;  //缩略图url string
        public String largeImage;  //全图url string
    }
}
