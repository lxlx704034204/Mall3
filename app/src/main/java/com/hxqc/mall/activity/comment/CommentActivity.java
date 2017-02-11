package com.hxqc.mall.activity.comment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.api.CommentApiClient;
import com.hxqc.mall.core.adapter.comment.MyCommentListAdapterHolder;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.model.comment.CommentModel;
import com.hxqc.mall.core.model.comment.CommentsResponses;
import com.hxqc.mall.core.util.comment.CommentUtils;
import com.hxqc.mall.core.views.LineTranslateAnimView;
import com.hxqc.mall.core.views.ThirdRadioButton;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class CommentActivity extends NoBackActivity implements OnRefreshHandler, RadioGroup.OnCheckedChangeListener,
        ThirdRadioButton.OnThirdStatusChangeListener {


    //按评分排序标识
    final int SCORE_NORMAL = 0;
    final int SCORE_SORT_UP = 3;
    final int SCORE_SORT_DOWN = 2;


    public String itemID = "";
    String seriesID = "";
    int scoreStatus = SCORE_NORMAL;

    RatingBar scoreBar;
    TextView mScoreText;
    TextView mCommentSum;
//    ImageView mScoreImage;

    //是否加载数据失败
    RequestFailView mFailView;
    //刷新
    PtrFrameLayout mPtrFrameLayoutView;
    UltraPullRefreshHeaderHelper mPtrHelper;

    RecyclerView mCommentRecycleView;
    MyCommentListAdapterHolder myCommentListAdapterHolder;
    ArrayList< CommentModel > mData = new ArrayList<>();
    LinearLayoutManager layoutManager;

    CommentApiClient apiClient;

    LineTranslateAnimView mLineAnimView;

    static int DefaultPage = 1;
    int page_refresh = DefaultPage;

    public Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        /**
         * tool bar
         */
        mToolBar = (Toolbar) findViewById(R.id.toolbar_comment);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle(getTitle());
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        itemID = getIntent().getStringExtra("item_id");
        seriesID = getIntent().getStringExtra("series_id");
        DebugLog.i("comments", getIntent().getStringExtra("item_id"));

        apiClient = new CommentApiClient();

        mFailView = (RequestFailView) findViewById(R.id.comment_fail_notice_view);

        mLineAnimView = (LineTranslateAnimView) findViewById(R.id.line_anim);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.factor_group);
        radioGroup.setOnCheckedChangeListener(this);
        ((ThirdRadioButton) findViewById(R.id.sort_2)).setOnThirdStatusChangeListener(this);
        //刷新
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_c_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(CommentActivity.this, mPtrFrameLayoutView,this);

        //评分评论数 标题栏-------------------------------------------------
        mScoreText = (TextView) findViewById(R.id.tv_score_avg);
        scoreBar = (RatingBar) findViewById(R.id.ratingbar_score_bar);
        mCommentSum = (TextView) findViewById(R.id.tv_comment_sum);
        scoreBar.setEnabled(false);
        //列表---------------------------------------------------
        layoutManager = new LinearLayoutManager(this);
        mCommentRecycleView = (RecyclerView) findViewById(R.id.rlv_comment_list);
        mCommentRecycleView.setLayoutManager(layoutManager);
        mCommentRecycleView.setHasFixedSize(true);
        mCommentRecycleView.setItemAnimator(new DefaultItemAnimator());
        getApiData(SCORE_NORMAL, true);
    }

    static int ItemCount = 10;


    /**
     * @param dataSortType
     *         排序方式
     */
    public void getApiData(int dataSortType, boolean showAnim) {
        if (TextUtils.isEmpty(itemID)) return;
        if (TextUtils.isEmpty(seriesID)) return;

        apiClient.getComments(seriesID, itemID, dataSortType, page_refresh, ItemCount, new LoadingAnimResponseHandler(this, showAnim) {
            @Override
            public void onSuccess(String response) {
                CommentsResponses commentsResponses = JSONUtils.fromJson(response, CommentsResponses.class);
                if (commentsResponses == null) {
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    return;
                }
                setScore(commentsResponses);

                //更新列表
                ArrayList< CommentModel > tData = commentsResponses.comments;

                if (tData == null || tData.size() == 0) {
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    return;
                }

                mFailView.setVisibility(View.GONE);
                mPtrHelper.setHasMore(tData.size() >= ItemCount);
                DebugLog.d("onLoad", "getApiData!"+mPtrHelper.isHasMore());
                if (page_refresh == DefaultPage) {
                    mData.clear();
                    myCommentListAdapterHolder = new MyCommentListAdapterHolder(mData);
                    mCommentRecycleView.setAdapter(myCommentListAdapterHolder);
                }
                page_refresh++;
                mData.addAll(tData);
                myCommentListAdapterHolder.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPtrHelper.refreshComplete(mPtrFrameLayoutView);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                mFailView.setVisibility(View.VISIBLE);
                mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
        });
    }


    /**
     * 设置分数
     *
     * @param commentsResponses
     */
    @SuppressLint("SetTextI18n")
    private void setScore(CommentsResponses commentsResponses) {

        //平均评分
        if (TextUtils.isEmpty(commentsResponses.averageScore)) {
            scoreBar.setProgress(30);
            mScoreText.setText(5.0 + "");
        } else {

            float s = Float.parseFloat(commentsResponses.averageScore);
            int first_s = (int) s;
            int second_s = (int) (s * 10 - first_s * 10);
            if (second_s == 0) {
                second_s = 0;
            } else if (second_s > 0 && second_s < 5) {
                second_s = 1;
            } else if (second_s == 5) {
                second_s = 2;
            } else if (second_s <= 9) {
                second_s = 3;
            } else {
                second_s = 5;
            }
            int ss = first_s * 5 + second_s;

            scoreBar.setProgress(ss);
            mScoreText.setText(CommentUtils.formatScore(commentsResponses.averageScore));

        }
        mCommentSum.setText("" + commentsResponses.commentCount);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_comment, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.action_settings) {
//            SwitchHelper.toUserComment(getApplicationContext());
//        }
//        return false;
//    }


    @Override
    public boolean hasMore() {
        DebugLog.d("onLoad", "hasMore!"+mPtrHelper.isHasMore());
        return mPtrHelper.isHasMore();
    }

    @Override
    public void onRefresh() {
        page_refresh = DefaultPage;
        DebugLog.d("onRefresh", "ignore manually onRefresh!");
        getApiData(scoreStatus, false);
    }

    @Override
    public void onLoadMore() {
        DebugLog.d("onLoad", "onLoadMore!");
        getApiData(scoreStatus, false);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        page_refresh = DefaultPage;
        if (checkedId == R.id.sort_1) {
            //时间
            mLineAnimView.startUnderlineAnim(0);
            scoreStatus = SCORE_NORMAL;
            getApiData(scoreStatus, true);
        } else if (checkedId == R.id.sort_2) {
            //评分
            mLineAnimView.startUnderlineAnim(1);
        }
    }

    @Override
    public void onThirdStatusChange(CompoundButton buttonView, boolean isChecked, int status) {
        page_refresh = DefaultPage;
        switch (status) {
            case 1:
                scoreStatus = SCORE_SORT_DOWN;
                getApiData(scoreStatus, true);
                break;
            case 2:
                scoreStatus = SCORE_SORT_UP;
                getApiData(scoreStatus, true);
                break;
        }

    }
}
