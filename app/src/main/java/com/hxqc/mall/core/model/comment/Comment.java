package com.hxqc.mall.core.model.comment;

/**
 * Author: wanghao
 * Date: 2015-03-19
 * FIXME
 * Todo
 */
public class Comment {
    public String comment_this;
    public boolean hasAppend;
    public String comment_append;
    public int image_num;
    public int append_image_num;
    public int stars;



    public Comment() {
    }

    public Comment(String comment_this, boolean hasAppend, String comment_append, int image_num, int append_image_num) {
        this.comment_this = comment_this;
        this.hasAppend = hasAppend;
        this.comment_append = comment_append;
        this.image_num = image_num;
        this.append_image_num = append_image_num;
    }
}
