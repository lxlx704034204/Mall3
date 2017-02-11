package com.hxqc.mall.usedcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.CarFilterAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.fragment.BuyCarFilterDetailFragment;
import com.hxqc.mall.usedcar.model.CarFilterCountModel;
import com.hxqc.mall.usedcar.model.CarFilterTip;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 车辆条件筛选界面
 * Created by huangyi on 15/10/21.
 */
public class BuyCarFilterActivity extends BackActivity implements View.OnClickListener {

    UsedCarApiClient mUsedCarApiClient;

    DrawerLayout mDrawerView;
    LinearLayout mKeywordParentView; //关键字 父容器
    TextView mKeywordView; //关键字
    ListViewNoSlide mListView;
    Button mResultView;
    BuyCarFilterDetailFragment mFragment;

    CarFilterAdapter mAdapter;
    ArrayList<CarFilterTip> mFilterTipsData; //mListView 数据源

    String mKeyword;
    int mTotal = -1, mFlag = -1; //mFlag相当 HashMap<Integer, String[]>中的 Integer
    HashMap<Integer, String[]> mSelectedTipMap = new HashMap(); //Integer(区分筛选条件)   String[](记录筛选值)第一个String id, 第二个String Value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_car_filter);

        mUsedCarApiClient = new UsedCarApiClient();
        initData();
        initView();
    }

    //初始化 mListView 数据源
    private void initData() {
        mTotal = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt("total", 0);
        mKeyword = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("keyword");
        mSelectedTipMap.putAll((HashMap<Integer, String[]>) getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getSerializable(UsedCarActivitySwitcher.SELECTED_TIP_MAP));
        String[] carFilterItems = new String[]{"价格", "品牌", "车龄", "级别", "来源", "排量", "变速箱", "里程"}; //Integer 0+
        mFilterTipsData = new ArrayList<>();
        for (int i = 0; i < carFilterItems.length; i++) {
            CarFilterTip carFilterTip = new CarFilterTip();
            if (mSelectedTipMap.size() != 0) {
                for (Object o : mSelectedTipMap.entrySet()) {
                    HashMap.Entry entry = (HashMap.Entry) o;
                    if ((Integer) entry.getKey() == i) {
                        carFilterTip.carFilterSelectedTipItem = ((String[]) entry.getValue())[1]; //获取上个页面已选条件名
                        break;
                    } else {
                        carFilterTip.carFilterSelectedTipItem = "不限"; //默认为不限
                    }
                }
            } else {
                carFilterTip.carFilterSelectedTipItem = "不限";
            }
            carFilterTip.carFilterTipItem = carFilterItems[i];
            mFilterTipsData.add(carFilterTip);
        }
    }

    private void initView() {
        mDrawerView = (DrawerLayout) findViewById(R.id.buy_car_filter_drawer);
        OtherUtil.setDrawerMode(mDrawerView);
        mKeywordParentView = (LinearLayout) findViewById(R.id.buy_car_filter_keyword_parent);
        mKeywordView = (TextView) findViewById(R.id.buy_car_filter_keyword);
        findViewById(R.id.buy_car_filter_cancel).setOnClickListener(this);

        mListView = (ListViewNoSlide) findViewById(R.id.buy_car_filter_list);
        mResultView = (Button) findViewById(R.id.buy_car_filter_result);
        mResultView.setOnClickListener(this);
        findViewById(R.id.buy_car_filter_right).setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) / 5 * 4, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        mFragment = new BuyCarFilterDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.buy_car_filter_right, mFragment).commit();

        mFragment.setOnCompleteChooseListener(new BuyCarFilterDetailFragment.OnCompleteChooseListener() {
            @Override
            public void onCompleteChoose(String itemName, String itemId, boolean isSelectedUnlimited) {
                if (isSelectedUnlimited) { //true  -> 不限 -> 移除已经筛选的条件
                    mSelectedTipMap.remove(mFlag);
                } else {
                    mSelectedTipMap.put(mFlag, new String[]{itemId, itemName});
                }
                mFilterTipsData.get(mFlag).carFilterSelectedTipItem = itemName;
                mAdapter.notifyDataSetChanged(); //mListView 条目name改变
                asyncGetFilterCount(mSelectedTipMap, new UsedCarSPHelper(BuyCarFilterActivity.this).getCity()); //查询新数据
                mDrawerView.closeDrawer(Gravity.RIGHT);
            }
        });

        mAdapter = new CarFilterAdapter(this, mFilterTipsData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFlag = position;
                mFragment.initData(position);
                mDrawerView.openDrawer(Gravity.RIGHT);
            }
        });

        if(! TextUtils.isEmpty(mKeyword)) {
            mKeywordView.setText(mKeyword);
            mKeywordParentView.setVisibility(View.VISIBLE);
        }
        if(mTotal > 0) {
            showResultButton(1);
        }else {
            showResultButton(2);
        }
    }

    private void asyncGetFilterCount(HashMap<Integer, String[]> hashMap, String city) {
        String price = "";
        String brand = "";
        String age_limit = "";
        String level = "";
        String publish_from = "";
        String displacement = "";
        String gearbox = "";
        String mileage = "";
        if (hashMap.size() != 0) {
            for (Object o : hashMap.entrySet()) {
                HashMap.Entry entry = (HashMap.Entry) o;
                switch ((Integer) entry.getKey()) {
                    case 0:
                        price = hashMap.get(0)[0];
                        break;
                    case 1:
                        brand = hashMap.get(1)[0];
                        break;
                    case 2:
                        age_limit = hashMap.get(2)[0];
                        break;
                    case 3:
                        level = hashMap.get(3)[0];
                        break;
                    case 4:
                        publish_from = hashMap.get(4)[0];
                        break;
                    case 5:
                        displacement = hashMap.get(5)[0];
                        break;
                    case 6:
                        gearbox = hashMap.get(6)[0];
                        break;
                    case 7:
                        mileage = hashMap.get(7)[0];
                        break;
                }
            }
        }

        mUsedCarApiClient.getFilterCount(price, brand, age_limit, level, publish_from, displacement, gearbox, mileage, mKeyword, city, new LoadingAnimResponseHandler(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                showResultButton(0);
            }

            @Override
            public void onSuccess(String response) {
                CarFilterCountModel carFilterCountModel = JSONUtils.fromJson(response, CarFilterCountModel.class);
                if (carFilterCountModel == null) {
                    showResultButton(2);
                    return;
                }

                if (Integer.valueOf(carFilterCountModel.total) == 0) {
                    showResultButton(2);
                } else {
                    mTotal = Integer.valueOf(carFilterCountModel.total);
                    showResultButton(1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showResultButton(2);
            }
        });
    }

    /** flag: 0=正在筛选中 1=返回查看 2=返回订阅 **/
    private void showResultButton(int flag) {
        switch (flag) {
            case 0:
                mResultView.setEnabled(false);
                mResultView.setText("正在筛选中...");
                break;
            case 1:
                mResultView.setEnabled(true);
                mResultView.setText("查看" + mTotal + "条符合条件车源");
                break;
            case 2:
                mResultView.setEnabled(true);
                mResultView.setText("暂无车源 点击返回");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_buycar_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_selected_tips) {
            for (int i = 0; i < mFilterTipsData.size(); i++) {
                mFilterTipsData.get(i).carFilterSelectedTipItem = "不限";
            }
            mAdapter.notifyDataSetChanged();
            mSelectedTipMap.clear();
            mKeywordParentView.setVisibility(View.GONE);
            mKeyword = "";
            asyncGetFilterCount(mSelectedTipMap, new UsedCarSPHelper(this).getCity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buy_car_filter_result) {
            Intent intent = new Intent();
            intent.putExtra("keyword", mKeyword);
            intent.putExtra("selectedFilter", mSelectedTipMap);
            setResult(RESULT_OK, intent);
            this.finish();
        } else if (i == R.id.buy_car_filter_cancel) {
            mKeywordParentView.setVisibility(View.GONE);
            mKeyword = "";
            asyncGetFilterCount(mSelectedTipMap, new UsedCarSPHelper(BuyCarFilterActivity.this).getCity()); //查询新数据
        }
    }
}
