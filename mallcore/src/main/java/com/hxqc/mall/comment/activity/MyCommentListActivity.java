package com.hxqc.mall.comment.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.comment.fragment.MyCommentListFragment;
import com.hxqc.mall.core.R;

/**
 * @Author : 钟学东
 * @Since : 2016-05-27
 * FIXME
 * Todo 我的评论列表
 */
public class MyCommentListActivity extends BackActivity{

    private MyCommentListFragment myCommentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment_list);
        myCommentListFragment = new MyCommentListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl,myCommentListFragment).commit();
    }

}
