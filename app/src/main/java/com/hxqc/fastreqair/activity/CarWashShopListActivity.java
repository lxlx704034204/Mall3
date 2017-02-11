package com.hxqc.fastreqair.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.fastreqair.adapter.CarWashListFilterAdapter;
import com.hxqc.fastreqair.adapter.CarWashShopListAdapter;
import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CarWashListFilterBean;
import com.hxqc.fastreqair.model.CarWashShopListBean;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.DropDownMenu;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function: 洗车店铺列表
 *
 * @author 袁秉勇
 * @since 2016年05月16日
 */
public class CarWashShopListActivity extends NoBackActivity implements OnRefreshHandler, View.OnClickListener {
    private final static String TAG = CarWashShopListActivity.class.getSimpleName();
    private Context mContext;

    private CarWashApiClient mCarWashApiClient;
    private DropDownMenu mDropDownMenu;

    private PtrFrameLayout mPtrFrameLayoutView;
    private RequestFailView mRequestFailView;

    private HashMap< String, String > hashMap = new HashMap<>();

    private CarWashShopListAdapter mCarWashShopListAdapter;

    private ArrayList< CarWashListFilterBean > carWashListFilterBeans;

    private boolean hasMore = false;
    private int PER_PAGE = 15;
    private int mPage = 1;
    private int DEFAULT_PATE = 1;

    private String[] titles = {"全部城区", "洗车项目", "距离最近"};
    private ArrayList< Object > fragments = new ArrayList<>();
    private CarWashListFilterAdapter carWashListFilterAdapter1, carWashListFilterAdapter2, carWashListFilterAdapter3;
    private ListFragment listFragment2;

    private ArrayList< String > filterTip1s;
    private ArrayList< String > filterTip2s = new ArrayList<>();
    private ArrayList< String > filterTip3s = new ArrayList() {{
        add("距离最近");
        add("价格最低");
        add("评分最高");
        add("接单量最多");
    }};
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mCarWashApiClient = new CarWashApiClient();


        setContentView(R.layout.activity_car_wash);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView mChangeCityView = (TextView) findViewById(R.id.change_city);
        mChangeCityView.setOnClickListener(this);

        mDropDownMenu = (DropDownMenu) findViewById(R.id.drop_down_menu);
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.auto_refresh_frame);
        UltraPullRefreshHeaderHelper mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView, this);
        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        mCarWashShopListAdapter = new CarWashShopListAdapter(this) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CarWashShopListBean carWashShopListBean = getItemData(viewHolder.getAdapterPosition());
                        CarWashActivitySwitcher.toWashCarShop(mContext, carWashShopListBean.shopID);
                    }
                });
                return viewHolder;
            }
        };
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.auto_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(mCarWashShopListAdapter);

        hashMap.put("sort", "10");
        BaseSharedPreferencesHelper baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(mContext);
        hashMap.put("latitude", TextUtils.isEmpty(baseSharedPreferencesHelper.getLatitudeBD()) ? "0" : baseSharedPreferencesHelper.getLatitudeBD());
        hashMap.put("longitude", TextUtils.isEmpty(baseSharedPreferencesHelper.getLongitudeBD()) ? "0" : baseSharedPreferencesHelper.getLongitudeBD());

        initFragment();
        getData(true);
        getFilterData(true);
    }

    @SuppressLint("ValidFragment")
    private void initFragment() {
        filterTip1s = new ArrayList<>();
        String[] tips = getResources().getStringArray(R.array.carWashCondition);
        Collections.addAll(filterTip1s, tips);


        carWashListFilterAdapter1 = new CarWashListFilterAdapter(filterTip1s, mContext);
        @SuppressLint("ValidFragment")
        ListFragment listFragment1 = new ListFragment() {
            @Override
            public void onViewCreated(View view, Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }


            @Override
            public void onListItemClick(ListView l, View v, int position, long id) {
                super.onListItemClick(l, v, position, id);
                mDropDownMenu.setTabText(0, filterTip1s.get(position));
                mDropDownMenu.closeMenu();
                mPage = DEFAULT_PATE;
                if (position == 0) {
                    hashMap.remove("district");
                } else {
                    hashMap.put("district", filterTip1s.get(position));
                }
                getData(true);
                carWashListFilterAdapter1.setLastClickPos(position);
                carWashListFilterAdapter1.notifyDataSetChanged();
            }
        };
        listFragment1.setListAdapter(carWashListFilterAdapter1);
        fragments.add(listFragment1);


        listFragment2 = new ListFragment() {
            @Override
            public void onViewCreated(View view, Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }


            @Override
            public void onListItemClick(ListView l, View v, int position, long id) {
                super.onListItemClick(l, v, position, id);
                mDropDownMenu.setTabText(1, filterTip2s.get(position));
                mDropDownMenu.closeMenu();
                mPage = DEFAULT_PATE;
                if (position == 0) {
                    hashMap.remove("chargeID");
                } else {
                    hashMap.put("chargeID", carWashListFilterBeans.get(position - 1).chargeID);
                }
                getData(true);
                carWashListFilterAdapter2.setLastClickPos(position);
                carWashListFilterAdapter2.notifyDataSetChanged();
            }
        };
        fragments.add(listFragment2);


        carWashListFilterAdapter3 = new CarWashListFilterAdapter(filterTip3s, mContext);
        @SuppressLint("ValidFragment")
        ListFragment listFragment3 = new ListFragment() {
            @Override
            public void onViewCreated(View view, Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }


            @Override
            public void onListItemClick(ListView l, View v, int position, long id) {
                super.onListItemClick(l, v, position, id);
                mDropDownMenu.setTabText(2, filterTip3s.get(position));
                mDropDownMenu.closeMenu();
                mPage = DEFAULT_PATE;
                hashMap.put("sort", (position + 1) * 10 + "");
                getData(true);
                carWashListFilterAdapter3.setLastClickPos(position);
                carWashListFilterAdapter3.notifyDataSetChanged();
            }
        };
        listFragment3.setListAdapter(carWashListFilterAdapter3);
        fragments.add(listFragment3);

        mDropDownMenu.setInflateFragment(true);
        mDropDownMenu.setDropDownMenu(Arrays.asList(titles), fragments);
        mDropDownMenu.setTabClickable(1, false);
    }


    /**
     * 获取洗车列表数据
     **/
    private void getData(boolean showAnim) {
        hasMore = false;
        mCarWashApiClient.getCarWashShopListData(mPage, PER_PAGE, hashMap, new LoadingAnimResponseHandler(this, showAnim) {
            @Override
            public void onSuccess(String response) {
                ArrayList< CarWashShopListBean > carWashListFilterBeans = JSONUtils.fromJson(response, new TypeToken< ArrayList< CarWashShopListBean > >() {
                });

                if (null == carWashListFilterBeans) {
                    if (mPage == DEFAULT_PATE) onFailed(false);
                    return;
                }

                if (mPage == DEFAULT_PATE) {
                    if (carWashListFilterBeans.size() <= 0) {
                        onFailed(false);
                        return;
                    }
                    mCarWashShopListAdapter.clearData();
                }

                hasMore = carWashListFilterBeans.size() >= PER_PAGE;

                if (mRequestFailView.getVisibility() == View.VISIBLE) mRequestFailView.setVisibility(View.GONE);
                mCarWashShopListAdapter.addData(carWashListFilterBeans);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    onFailed(true);
                } else {
                    onFailed(false);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
                if (mPtrFrameLayoutView.isRefreshing()) mPtrFrameLayoutView.refreshComplete();
            }
        });
    }


    private void getFilterData(boolean showAnim) {
        mCarWashApiClient.getCarWashListFilterData(new LoadingAnimResponseHandler(this, showAnim) {
            @Override
            public void onSuccess(String response) {
                carWashListFilterBeans = JSONUtils.fromJson(response, new TypeToken< ArrayList< CarWashListFilterBean > >() {
                });

                if (carWashListFilterBeans != null && carWashListFilterBeans.size() > 0) {
                    for (int i = 0; i < carWashListFilterBeans.size() + 1; i++) {
                        if (i == 0) {
                            filterTip2s.add("不限");
                        } else {
                            filterTip2s.add(carWashListFilterBeans.get(i - 1).name);
                        }
                    }

                    carWashListFilterAdapter2 = new CarWashListFilterAdapter(filterTip2s, mContext);
                    listFragment2.setListAdapter(carWashListFilterAdapter2);
                    mDropDownMenu.setTabClickable(1, true);
                }
            }
        });
    }


    private void onFailed(boolean isOffLine) {
        if (isOffLine) {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        } else {
//            mRequestFailView.setEmptyButtonClick("重新加载", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPage = DEFAULT_PATE;
//                    getData(true);
//                }
//            });

            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
        if (mRequestFailView.getVisibility() == View.GONE) mRequestFailView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean hasMore() {
        return hasMore;
    }


    @Override
    public void onRefresh() {
        mPage = DEFAULT_PATE;
        getData(false);
    }


    @Override
    public void onLoadMore() {
        ++mPage;
        getData(true);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_city) {
            CarWashActivitySwitcher.toChooseShopOnMap(this, hashMap);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.getBackground().setAlpha(255);
    }
}
