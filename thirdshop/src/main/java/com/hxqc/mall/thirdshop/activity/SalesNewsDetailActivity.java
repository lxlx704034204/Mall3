package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.control.T_NewImageViewControl;
import com.hxqc.mall.thirdshop.model.promotion.SalesNewsDetail;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.InfoIntroduceView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * 新闻资讯 详情  h5
 */
public class SalesNewsDetailActivity extends BackActivity {

    CallBar mCallBar;
    RequestFailView mRequestFailView;
    private String newsID;
    ThirdPartShopClient apiClient;

    SalesNewsDetail salesNewsDetail;

    TextView mNewsTitleView;
    TextView mPostTimeView;
    TextView mShopNameView;
    InfoIntroduceView mContentView;
    T_NewImageViewControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_sales_news_detail);
        apiClient = new ThirdPartShopClient();
        control = T_NewImageViewControl.getInstance();

        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            newsID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA)
                    .getString(ActivitySwitcherThirdPartShop.NEWS_ID);
        } else {
            newsID = getIntent().getStringExtra(ActivitySwitcherThirdPartShop.NEWS_ID);
        }
        initView();
        getApiData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        control.onDestroy();
    }

    private void initView() {
        mCallBar = (CallBar) findViewById(R.id.call_bar);
        mRequestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);
        mNewsTitleView = (TextView) findViewById(R.id.tv_news_detail_title);
        mPostTimeView = (TextView) findViewById(R.id.tv_news_detail_post_time);
        mShopNameView = (TextView) findViewById(R.id.tv_news_detail_shop_name);
        mContentView = (InfoIntroduceView) findViewById(R.id.v_detail_img_info);
    }

    private void getApiData() {
        apiClient.newsDetail(newsID, new LoadingAnimResponseHandler(SalesNewsDetailActivity.this, true) {
            @Override
            public void onSuccess(String response) {
                salesNewsDetail = JSONUtils.fromJson(response, new TypeToken< SalesNewsDetail >() {
                });
                if (salesNewsDetail != null) {
                    setUpData();
                    mRequestFailView.setVisibility(View.GONE);
                    mCallBar.setVisibility(View.VISIBLE);
                } else {
                    requestFailView();
                }
                DebugLog.i("test_s_p_list", response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                requestFailView();
            }
        });
    }

    private void setUpData() {
        if (TextUtils.isEmpty(salesNewsDetail.shopTel)) {
            mCallBar.setVisibility(View.GONE);
        } else {
            mCallBar.setTitle("咨询电话");
            mCallBar.setNumber(salesNewsDetail.shopTel);
        }
        mNewsTitleView.setText(salesNewsDetail.title);
        mPostTimeView.setText(String.format("发布时间：%s", salesNewsDetail.publishDate));
        mShopNameView.setText(String.format("经销商：%s", salesNewsDetail.shopTitle));
        mContentView.setNewsDetailInfo(salesNewsDetail.content, salesNewsDetail.attachments);
    }

    /**
     * 获取数据失败  刷新显示
     */
    private void requestFailView() {
        mRequestFailView.setEmptyDescription("获取数据失败");
        mRequestFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiData();
            }
        });
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiData();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        mRequestFailView.setVisibility(View.VISIBLE);
    }

    /**
     * 回到主页
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sales_news_detail, menu);
        return true;
    }

    /**
     * 菜单条目点击
     * ActivitySwitcherThirdPartShop.toShopDetails(SalesPDetailActivity.this, salesDetail.shopID);
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int i = item.getItemId();
        if (i == R.id.action_to_home) {
            ActivitySwitchBase.toMain(SalesNewsDetailActivity.this, 0);

        }
        return false;
    }

}
