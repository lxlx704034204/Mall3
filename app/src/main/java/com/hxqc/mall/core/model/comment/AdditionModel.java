package com.hxqc.mall.core.model.comment;

import com.hxqc.mall.core.model.ImageModel;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-23
 * FIXME
 * 追加评论
 */

public class AdditionModel {

    //评论 id
    public int commentID;
    //内容
    public String content;
    //时间
    public String createTime;
    //图组
    public ArrayList< ImageModel > images;

}
