package com.hxqc.mall.comment.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 评论列表基类
 */

public abstract class BaseCommentListActivity extends BackActivity implements OnRefreshHandler {

    protected LinearLayout mRootView;

    //是否加载数据失败
    RequestFailView mFailView;
    //刷新
    PtrFrameLayout mPtrFrameLayoutView;
    UltraPullRefreshHeaderHelper mPtrHelper;

    RecyclerView mCommentRecycleView;
    LinearLayoutManager layoutManager;

    static int DefaultPage = 1;
    int page_refresh = DefaultPage;

    public Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_comment_list);
        mRootView = (LinearLayout) findViewById(R.id.ll_comment_parent);
        mRootView.addView(getHeadViews(), 0);
        initView();
    }

    /**
     * 获取头部 自定义view
     */
    protected abstract View getHeadViews();

    /**
     * 刷新
     */
    protected abstract void refreshData();

    /**
     * 加载更多
     */
    protected abstract void loadMoreData();

    /**
     * 首次加载
     */
    protected abstract void initDataFirst();

    /**
     * 页面bar名称
     */
    protected abstract String getBarTitle();

    void initView() {

        /**
         * tool bar
         */
        mToolBar = (Toolbar) findViewById(R.id.toolbar_comment);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle(getBarTitle());
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFailView = (RequestFailView) findViewById(R.id.comment_fail_notice_view);
        //刷新
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_c_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(BaseCommentListActivity.this, mPtrFrameLayoutView,this);
        //列表---------------------------------------------------
        layoutManager = new LinearLayoutManager(this);
        mCommentRecycleView = (RecyclerView) findViewById(R.id.rlv_comment_list);
        mCommentRecycleView.setLayoutManager(layoutManager);
        mCommentRecycleView.setHasFixedSize(true);
        mCommentRecycleView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }

    @Override
    public void onRefresh() {
        refreshData();
//        page_refresh = DefaultPage;
//
//        getApiData(scoreStatus, false);
    }

    @Override
    public void onLoadMore() {
        loadMoreData();
//        getApiData(scoreStatus, false);
    }

    void showFailViewEmpty(){
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
    }

    void showFailViewFail(){
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }
}
