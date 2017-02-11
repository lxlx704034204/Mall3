package com.hxqc.mall.comment.model;

import com.hxqc.mall.core.model.ImageModel;

import java.util.ArrayList;

/**
 * Author:  wh
 * Date:  2016/5/12
 * FIXME
 * Todo
 */
public interface CommentListItem {
    String userImage();
    String userNickname();
    String content();
    String dateTime();
    int scores();
    ArrayList<ImageModel> imageItems();

    String itemColor();
    String itemInterior();
//    String reply();
}
