package com.hxqc.mall.comment.model;

import java.util.ArrayList;

/**
 * Author:  wh
 * Date:  2016/5/12
 * FIXME
 * Todo   评论列表数据
 */
public interface OnCommentDataChangeListener {
    void onCommentDataChange(ArrayList<CommentListItem> items);
}
