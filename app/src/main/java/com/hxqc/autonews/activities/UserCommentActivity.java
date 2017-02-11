package com.hxqc.autonews.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.hxqc.autonews.adapter.CommentPhotoAdapter;
import com.hxqc.autonews.model.UserGradeComment;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * 口碑 用户评价
 * Created by huangyi on 16/10/17.
 */
public class UserCommentActivity extends BackActivity {

    public static final String COMMENT = "comment";
    UserGradeComment mComment;
    CircleImageView mPhotoView;
    TextView mNameView, mScoreView, mDateView, mContentView;
    RecyclerView mRecyclerView;
    RequestFailView mFailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment);

        mComment = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(COMMENT);
        initView();
    }

    private void initView() {
        mPhotoView = (CircleImageView) findViewById(R.id.comment_photo);
        mNameView = (TextView) findViewById(R.id.comment_name);
        mScoreView = (TextView) findViewById(R.id.comment_score);
        mDateView = (TextView) findViewById(R.id.comment_date);
        mContentView = (TextView) findViewById(R.id.comment_content);
        findViewById(R.id.comment_car).setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        mFailView = (RequestFailView) findViewById(R.id.comment_fail);

        ImageUtil.setImageSquare(this, mPhotoView, mComment.userInfo.userAvatar);
        mNameView.setText(mComment.userInfo.nickName.trim());
        mScoreView.setText(mComment.grade.average + "分");
        mDateView.setText(mComment.time.trim());
        mContentView.setText(mComment.content.trim());
        if (null == mComment.images || mComment.images.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            mRecyclerView.setAdapter(new CommentPhotoAdapter(mComment.images, this));
        }
    }

}
