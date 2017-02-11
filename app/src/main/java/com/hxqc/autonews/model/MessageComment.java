package com.hxqc.autonews.model;

import com.hxqc.autonews.model.pojos.AutoInformation;

/**
 * 资讯评价
 * Created by huangyi on 16/10/27.
 */
public class MessageComment {

    public Comment parentComment; //被回复评论
    public Comment myComment; //我的评论
    public AutoInformation autoInfo;
}
