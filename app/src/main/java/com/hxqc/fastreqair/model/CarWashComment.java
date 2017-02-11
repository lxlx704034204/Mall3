package com.hxqc.fastreqair.model;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-05-18
 * FIXME
 * Todo 洗车店铺评论列表
 */
public class CarWashComment {
    public String commentID;  //评论ID string
    public String content;    // 评论内容 string
    public String userNickname; //用户昵称 string
    public String createTime; // 评论时间 string
    public int score; //评分 number
    public String userAvatar;  //头像Url string
    public ArrayList<CommentImage> images;
    public ArrayList<String> tags ;

    public int positionTag;


    @Override
    public String toString() {
        return "CarWashComment{" +
                "commentID='" + commentID + '\'' +
                ", content='" + content + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", createTime='" + createTime + '\'' +
                ", score=" + score +
                ", userAvatar='" + userAvatar + '\'' +
                ", images=" + images +
                ", tags=" + tags +
                ", positionTag=" + positionTag +
                '}';
    }
}
