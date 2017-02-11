package com.hxqc.fastreqair.model;

import java.util.List;

/**
 * liaoguilong
 * Created by CPR113 on 2016/5/20.
 * 评论标签
 */
public class CarWashTagBean {
    public int score;
    public List<Tags> tags;
    public static class Tags{
        public String tagTitle;//标签内容
        public String tagID;//标签ID
    }


}
