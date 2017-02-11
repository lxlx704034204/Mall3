package com.hxqc.mall.activity.auto;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.auto.adapter.FilterAutoAdapter;
import com.hxqc.mall.auto.model.Filter;
import com.hxqc.mall.auto.model.FilterGroup;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.fragment.auto.FilterAutoBrandFragment;
import com.hxqc.mall.thirdshop.fragment.FilterContentFragment;
import com.hxqc.mall.thirdshop.interfaces.FilterAction;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-03-25
 * FIXME
 * Todo 筛选
 */
public class FilterAutoActivity extends AppBackActivity implements AdapterView.OnItemClickListener,
        FilterAction {
    static final String BRAND_FRAGMENT = "BrandFragment";
    static final String FILTER_CONTENT_FRAGMENT = "filterContentFragment";
    OverlayDrawer mOverlayDrawer;
    ListView mFilterFactorView;
    ArrayList< FilterGroup > mFilterGroups = new ArrayList<>();
    FilterAutoAdapter mAdapter;
    NewAutoClient client;
    FilterContentFragment filterContentFragment;
    FilterAutoBrandFragment mMainAutoBrandFragment;//品牌
    Map< String, String > mFilterMap = new HashMap<>();
    Button mResponseView;
    String itemCategory;//分类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_auto);
        itemCategory = getIntent().getStringExtra(AutoItem.ItemCategory);//类型

        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mOverlayDrawer.setSidewardCloseMenu(false);

        mFilterFactorView = (ListView) findViewById(R.id.filter_factor);
        mFilterFactorView.setOnItemClickListener(this);
        client = new NewAutoClient();
        client.filterFactor(itemCategory, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                createListView(response);
            }
        });
        FilterGroup filterGroup = new FilterGroup("品牌");
        mFilterGroups.add(0, filterGroup);


        mMainAutoBrandFragment = FilterAutoBrandFragment.instantiate(Integer.valueOf(itemCategory));

        mMainAutoBrandFragment.setFilterGroup(filterGroup);

        filterContentFragment = new FilterContentFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mdMenu, mMainAutoBrandFragment, BRAND_FRAGMENT).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mdMenu, filterContentFragment, FILTER_CONTENT_FRAGMENT).commit();

        mResponseView = (Button) findViewById(R.id.filter_response);
    }

    protected void createListView(String response) {
        ArrayList< FilterGroup > FilterGroups = JSONUtils.fromJson(response, new TypeToken< ArrayList< FilterGroup > >() {
        });
        this.mFilterGroups.addAll(FilterGroups);
        if (mAdapter == null) {
            mAdapter = new FilterAutoAdapter(this, mFilterGroups);
            mFilterFactorView.setAdapter(mAdapter);
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        openMenu();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (position == 0) {
            ft.hide(filterContentFragment).show(mMainAutoBrandFragment).commit();
        } else {
            ft.hide(mMainAutoBrandFragment).show(filterContentFragment).commit();
            filterContentFragment.notifyFilterFactor(this, mFilterGroups.get(position));
        }

    }

    void openMenu() {
        if (!mOverlayDrawer.isMenuVisible()) {
            mOverlayDrawer.openMenu();
        }
    }

    @Override
    public void filterListener(int position, Filter filter, FilterGroup filterGroup) {
        if (position == 0) {
            if (mFilterMap.containsKey(filter.filterKey)) {
                mFilterMap.remove(filter.filterKey);
            }
            filter = null;
        } else {
            mFilterMap.put(filter.filterKey, filter.filterValue);
        }
        filterGroup.setDefaultFilter(filter);
        mAdapter.notifyDataSetChanged();
        mOverlayDrawer.closeMenu();

        client.filterExamine(itemCategory, mFilterMap, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mResponseView.setText("正在查询中...");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.has("itemCount")) {
                        String count = response.getString("itemCount");
                        changeResponseViewState(Integer.valueOf(count));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                changeResponseViewState(0);
            }
        });
    }

    /**
     * 修改车型按钮
     *
     * @param count
     */
    void changeResponseViewState(int count) {
        mResponseView.setText(String.format("找到%d个车型", count));
        if (count <= 0) {
            mResponseView.setEnabled(false);
        } else {
            mResponseView.setEnabled(true);
        }

    }


    public void toAutoList(View view) {
        if (mFilterMap.keySet().size() <= 0) {
            ToastHelper.showYellowToast(this, "请选择筛选条件.");
            return;
        }
        mFilterMap.put(AutoItem.ItemCategory,itemCategory);
        ActivitySwitcher.toAutoList(this, mFilterMap);
    }
}
