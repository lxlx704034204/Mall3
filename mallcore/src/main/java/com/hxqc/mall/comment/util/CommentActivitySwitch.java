package com.hxqc.mall.comment.util;

import android.content.Context;
import android.content.Intent;

import com.hxqc.mall.comment.activity.MyCommentListActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * @Author : 钟学东
 * @Since : 2016-06-06
 * FIXME
 * Todo
 */
public class CommentActivitySwitch extends ActivitySwitchBase {

    public static void toMyCommentList(Context context){
        Intent intent = new Intent(context, MyCommentListActivity.class);
        context.startActivity(intent);
    }
}
