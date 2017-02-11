package com.hxqc.mall.activity.comment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.api.CommentApiClient;
import com.hxqc.mall.core.adapter.comment.CommentDetailAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailViewUtil;
import com.hxqc.mall.core.model.comment.CommentDetailResponse;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import hxqc.mall.R;


public class MyCommentDetailActivity extends BackActivity {

    CommentApiClient apiClient;
    public String commentID = "";
    ListView listView;
    CommentDetailAdapter commentDetailAdapter;
    String sku;
    View mBlankView;//抠头页面
    LinearLayout llDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment_detail);
        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.toast_yellow))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
        getSupportActionBar().setTitle("查看评论");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        llDetail = (LinearLayout) findViewById(R.id.ll_comment_detail);
//        , "未获取评论详情"
        mBlankView = new RequestFailViewUtil().getFailView(MyCommentDetailActivity.this);
        listView = (ListView) findViewById(R.id.lv_detail);
        apiClient = new CommentApiClient();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("comment_id"))) {
            DebugLog.i("comments", getIntent().getStringExtra("comment_id"));
            commentID = getIntent().getStringExtra("comment_id");
        }
        sku = getIntent().getStringExtra("comment_sku");

        getData();


    }

    boolean isBlankViewExsit = false;

    private void getData() {
        apiClient.getThisCommentDetail(sku, commentID, new LoadingAnimResponseHandler(MyCommentDetailActivity.this) {
            @Override
            public void onSuccess(String response) {
                //TODO  评论详情  设置
                if (isBlankViewExsit) {
                    llDetail.removeAllViews();
                    llDetail.addView(listView);
                    isBlankViewExsit = false;
                }
                try {
                    CommentDetailResponse commentDetailResponse = JSONUtils.fromJson(response, new TypeToken< CommentDetailResponse >() {
                    });
                    commentDetailAdapter = new CommentDetailAdapter(commentDetailResponse, MyCommentDetailActivity.this);
                    listView.setAdapter(commentDetailAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                llDetail.removeAllViews();
                llDetail.addView(mBlankView);
                isBlankViewExsit = true;
                mBlankView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                });
            }
        });
    }
}
