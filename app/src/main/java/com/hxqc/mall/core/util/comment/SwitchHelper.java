package com.hxqc.mall.core.util.comment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hxqc.mall.activity.comment.MyCommentDetailActivity;


/**
 * Author: wanghao
 * Date: 2015-04-01
 * FIXME
 * 跳转
 */
public class SwitchHelper {



    /**
     * 我的评论
     *
     * @param context
     */
    public static void toUserComment(Context context) {
        Intent intent = new Intent();
        intent.setAction("UserComment");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 评论详情
     */
    public static void toCommentDetail(String comment_sku,String commentID, Context context) {
        Intent intent = new Intent(context, MyCommentDetailActivity.class);
        intent.putExtra("comment_id", commentID);
        intent.putExtra("comment_sku", comment_sku);
        Log.i("comments", commentID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
