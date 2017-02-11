package com.hxqc.mall.activity.auto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.adapter.AutoListAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.model.auto.AutoBaseInformation;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.LineTranslateAnimView;
import com.hxqc.mall.core.views.ThirdRadioButton;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author: HuJunJie
 * Date: 2015-04-01
 * FIXME
 * Todo 车辆列表 用于特卖及新能源
 */
public class AutoListActivity extends NoBackActivity implements RadioGroup.OnCheckedChangeListener, ThirdRadioButton.OnThirdStatusChangeListener,
        AdapterView.OnItemClickListener, OnRefreshHandler, View.OnClickListener {
    public static final String KEYWORD = "keyword";
    public static final String SERIES = "seriesName";
    public static final String SORT = "sort";
    private static final String sort_default = null;//默认
    private static final String sort_sales = "1";//销量
    private static final String sort_price_down = "2";//价格降序
    private static final String sort_price_ups = "6";//价格升序
    private static final String sort_fall = "3";//降幅
    private static final String sort_putaway_up = "4";//上架时间升序
    private static final String sort_putaway_down = "5";//上架时间升序
    LineTranslateAnimView mLineAnimView;
    ListView mAutoListView;
    NewAutoClient client = new NewAutoClient();
    //搜索条件map
    Map< String, String > mSearchFactorMap = new HashMap<>();
    int page = DEFAULT_PAGE;//页数
    private static final int DEFAULT_PAGE = 1;//默认页数

    //  java初始化顺序 静态变量 ——> 静态初始化块 ——> 成员变量 ——> 非静态初始化块 ——> 构造器
    ArrayList< AutoBaseInformation > allAutoBaseInformationes;

    {
        allAutoBaseInformationes = new ArrayList<>();       //  车辆列表内容
        DebugLog.d(getClass().getName(), "---------------> 重新初始化一下");
    }

    AutoListAdapter mAutoListAdapter;

    PtrFrameLayout mPtrFrameLayoutView;
    UltraPullRefreshHeaderHelper mPtrHelper;
    static final int ItemPageCount = 15;

    TextView mSearchView;
    RequestFailView mRequestFailView;

    boolean hasShowDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_list);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        mLineAnimView = (LineTranslateAnimView) findViewById(R.id.line_anim);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.factor_group);
        radioGroup.setOnCheckedChangeListener(this);
        mAutoListView = (ListView) findViewById(R.id.auto_recycler_view);
        mAutoListView.setOnItemClickListener(this);
        mAutoListAdapter = new AutoListAdapter(this, allAutoBaseInformationes);
        mAutoListView.setAdapter(mAutoListAdapter);
        ((ThirdRadioButton) findViewById(R.id.sort_3)).setOnThirdStatusChangeListener(this);
        mSearchView = (TextView) findViewById(R.id.toolbar_search_textview);
        mSearchView.setOnClickListener(this);

        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.auto_refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);

        mRequestFailView.setEmptyButtonClick("看看别的", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchFactorMap.clear();
                page = DEFAULT_PAGE;
                doSearch(true);
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
        //--------------获取搜索条件
        String key = null;
        String value = null;
        Bundle bundle = getIntent().getBundleExtra(ActivitySwitcher.KEY_DATA);
        if (bundle != null) {
            mSearchFactorMap.clear();
            allAutoBaseInformationes.clear();
            if (bundle.containsKey(ActivitySwitcher.KEYWORD)) {
                //  关键字(从搜索界面跳转而来)
                value = bundle.getString(ActivitySwitcher.KEYWORD);
                key = KEYWORD;
            }
//            else if (bundle.containsKey(ActivitySwitcher.SERIESNAME)) {
//                //  车系(从车辆品牌列表界面跳转而来)
//                value = bundle.getString(ActivitySwitcher.SERIESNAME);
//                key = SERIESNAME;
//                String itemCategory = bundle.getString("itemCategory", String.valueOf(AutoItem.CATEGORY_AUTOMOBILE));//类型  新车/电动车
//                mSearchFactorMap.put("itemCategory", itemCategory);
//            }
            else if (bundle.containsKey(ActivitySwitcher.FILTER)) {
                //  筛选条件(从车辆品牌列表按条件筛选跳转而来)
                String filter = bundle.getString(ActivitySwitcher.FILTER);
                Map< String, String > filterMap = JSONUtils.fromJson(filter, new TypeToken< Map< String, String > >() {
                });
                mSearchFactorMap.putAll(filterMap);
            }
            page = DEFAULT_PAGE;
            doSearch(key, value);
        } else {
            if (allAutoBaseInformationes.size() <= 0) {
                mSearchFactorMap.clear();
                page = DEFAULT_PAGE;
                doSearch(true);
            }
        }

        if (!TextUtils.isEmpty(value)) {
            mSearchView.setText(value);
        }
    }

    void doSearch(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            if (TextUtils.isEmpty(value)) {
                mSearchFactorMap.remove(key);
            } else {
                mSearchFactorMap.put(key, value);
            }
        }
        doSearch(true);
    }

    synchronized void doSearch(boolean showAnim) {
        client.search(this, mSearchFactorMap, page, ItemPageCount, new LoadingAnimResponseHandler(this, showAnim) {

            @Override
            public void onSuccess(String response) {
                ArrayList< AutoBaseInformation > autoBaseInformations = JSONUtils.fromJson(response, new TypeToken< ArrayList< AutoBaseInformation > >() {
                });
                if (autoBaseInformations == null) {
                    autoBaseInformations = new ArrayList<>();
                }
                mRequestFailView.setVisibility(View.GONE);
                //关闭加载更多
                mPtrHelper.setHasMore(autoBaseInformations.size() >= ItemPageCount);
                if (page == DEFAULT_PAGE) {
                    allAutoBaseInformationes.clear();
                }
                page++;
                allAutoBaseInformationes.addAll(autoBaseInformations);
                mAutoListAdapter.notifyDataSetInvalidated();
                mAutoListAdapter.notifyDataSetChanged();

                if (allAutoBaseInformationes.size() <= 0) {
                    mAutoListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty));
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        getIntent().removeExtra(ActivitySwitcher.KEY_DATA);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getIntent().removeExtra(ActivitySwitcher.KEY_DATA);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        page = DEFAULT_PAGE;//初始化
        switch (checkedId) {
            case R.id.sort_1:
                mLineAnimView.startUnderlineAnim(0);
                doSearch(SORT, sort_default);
                break;
            case R.id.sort_2:
                mLineAnimView.startUnderlineAnim(1);
                doSearch(SORT, sort_sales);
                break;
            case R.id.sort_3:
                mLineAnimView.startUnderlineAnim(2);
                break;
            case R.id.sort_4:
                mLineAnimView.startUnderlineAnim(3);
                doSearch(SORT, sort_fall);
                break;
        }
    }

    @Override
    public void onThirdStatusChange(CompoundButton buttonView, boolean isChecked, int status) {
        page = DEFAULT_PAGE;//初始化
        switch (status) {
            case 1:
                doSearch(SORT, sort_price_down);
                break;
            case 2:
                doSearch(SORT, sort_price_ups);
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void toSearch(View view) {
        ActivitySwitcher.toSearch(this);
    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        hasShowDetail = true;
        AutoBaseInformation autoBaseInformation = ((AutoListAdapter) parent.getAdapter()).getItem(position);
        ActivitySwitcher.toAutoItemDetail(this, AutoItem.AUTO_COMMON, autoBaseInformation.getItemID(), autoBaseInformation.getItemDescription());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_search_textview:
                toSearch(v);
                break;
        }
    }


    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
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
        getMenuInflater().inflate(R.menu.menu_to_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_to_home:
                ActivitySwitcher.toMain(this, 0);
                finish();
                break;
            case R.id.action_message:
                ActivitySwitcher.toMyMessageActivity(this);
                break;
        }
        return false;
    }
}
