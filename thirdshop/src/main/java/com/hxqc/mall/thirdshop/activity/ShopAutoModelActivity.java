package com.hxqc.mall.thirdshop.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.SearchModel;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.adpter.ShopAutoListAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 车型列表
 */
public class ShopAutoModelActivity extends NoBackActivity implements OnRefreshHandler, AdapterView.OnItemClickListener {
    private String brandName = null, seriesName = null;
    ListView mAutoListView;
    ShopAutoListAdapter mAutoListAdapter;
    PtrFrameLayout mPtrFrameLayoutView;
    UltraPullRefreshHeaderHelper mPtrHelper;
    RequestFailView mRequestFailView;
    int page = DEFAULT_PAGE;//页数
    private static final int DEFAULT_PAGE = 1;//默认页数

    ArrayList<SearchModel > searchModels = new ArrayList<>();

    ThirdPartShopClient client = new ThirdPartShopClient();
    static final int ItemPageCount = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_auto_model);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle(getString(R.string.title_activity_shop_brand));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        brandName = bundle.getString(ActivitySwitcherThirdPartShop.BRAND);
        seriesName = bundle.getString(ActivitySwitcherThirdPartShop.SERIESNAME);

        series = bundle.getParcelable(ActivitySwitcherThirdPartShop.SERIES);

        initView();
    }

    private void initView() {
        mAutoListView = (ListView) findViewById(R.id.auto_recycler_view);
        mAutoListAdapter = new ShopAutoListAdapter(this, searchModels);
        mAutoListView.setAdapter(mAutoListAdapter);
        mAutoListView.setOnItemClickListener(this);


        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.auto_refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);

        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSearchFactorMap.clear();
//                page = DEFAULT_PAGE;
//                doSearch(true);
                finish();
                ActivitySwitcherThirdPartShop.toMain(ShopAutoModelActivity.this, 0);
            }
        });
        mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchModels.size() <= 0) {
            page = DEFAULT_PAGE;
            doSearch(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void doSearch(boolean showAnim) {

        /*模拟数据*/
//        ArrayList<SearchModel> searchModels = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            SearchModel searchModel = new SearchModel();
//            searchModel.brandName = "brandName" + i;
//            searchModel.itemOrigPrice = "123456" + i;
//            searchModel.modelName = "modelName" + i;
//            searchModel.modelThumb = "";
//            searchModel.priceRange = "123456-234567";
//            searchModel.seriesName = "seriesName" + i;
//            searchModels.add(searchModel);
//        }
//        this.searchModels.addAll(searchModels);
//        mAutoListAdapter.notifyDataSetInvalidated();
//        mAutoListAdapter.notifyDataSetChanged();

        applyData(showAnim);
    }

    private void applyData(boolean showAnim) {
        client.searchModel(seriesName, page, ItemPageCount, new LoadingAnimResponseHandler(this, showAnim) {

            @Override
            public void onSuccess(String response) {
                ArrayList<SearchModel> autoBaseInformations = JSONUtils.fromJson(response, new TypeToken<
                                        ArrayList<SearchModel>>() {
                });

                DebugLog.d(TAG, response);

                if (autoBaseInformations == null) {
                    autoBaseInformations = new ArrayList<>();
                }
                mRequestFailView.setVisibility(View.GONE);
                //关闭加载更多
                mPtrHelper.setHasMore(autoBaseInformations.size() >= ItemPageCount);
                if (page == DEFAULT_PAGE) {
                    searchModels.clear();
                }
                page++;
                searchModels.addAll(autoBaseInformations);
                mAutoListAdapter.notifyDataSetInvalidated();
                mAutoListAdapter.notifyDataSetChanged();

                if (searchModels.size() <= 0) {
                    mAutoListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType
                            .empty));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mPtrFrameLayoutView.setVisibility(View.GONE);
                mRequestFailView.setVisibility(View.VISIBLE);
                mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPtrHelper.refreshComplete(mPtrFrameLayoutView);
            }
        });
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
        page = DEFAULT_PAGE;//初始化
        doSearch(false);
    }

    @Override
    public void onLoadMore() {
        doSearch(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_third_shop_auto_model, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_to_home) {
            ActivitySwitcherThirdPartShop.toMain(this, 0);
        }
        return false;
    }


    boolean hasShowDetail;
    Series series;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hasShowDetail = true;
        SearchModel autoBaseInformation = ((ShopAutoListAdapter) parent.getAdapter()).getItem(position);
        ActivitySwitcherThirdPartShop.toFilterThirdShopActivity(this, autoBaseInformation.brandName,
                series, autoBaseInformation.modelName);
    }
}
