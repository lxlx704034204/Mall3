package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.DropDownMenu;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintenanceListFilterAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintenanceShopListAdapter;
import com.hxqc.mall.thirdshop.maintenance.control.FilterController;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function: 保养列表数据
 *
 * @author 袁秉勇
 * @since 2016年04月18日
 */
public class MaintenanceShopListActivity extends NoBackActivity implements OnRefreshHandler, FilterController.MaintenanceShopListHandler, View.OnClickListener {
    private final static String TAG = MaintenanceShopListActivity.class.getSimpleName();
    public static String TYPE = "type";
    public static String BRANDID = "brandID";
    public static String SERIESID = "seriesID";
    public static String AUTOMODELID = "autoModelID";
    public static String MYAUTOID = "myAutoID";
    public static String ITEMS = "items";
    public static String SORT = "sort";
    private static int INTERNALSHOP = 0; // 4S店
    private static int OUTERSHOP = 1; // 快修店
    private Context mContext;
    private int shopType;

    protected Toolbar toolbar;
    protected TextView mChangCity;
    protected TextView mServiceLayoutView;

//    private RelativeLayout mRelativeLayout;
//    private LineTranslateAnimView mLineAnimView;

    private DropDownMenu mDropDownMenu;
    private MaintenanceListFilterAdapter maintenanceListFilterAdapter1, maintenanceListFilterAdapter2;
    private String[] titles = {"全部城区", "距离最近"};
    private ArrayList< Object > fragments = new ArrayList<>();

    private PtrFrameLayout mPtrFrameLayoutView;
    private UltraPullRefreshHeaderHelper mPtrHelper;
    private RequestFailView mRequestFailView;

    private RecyclerView recyclerView;

    private FilterController filterController;

    private boolean hasMore = false;
    private int PER_PAGE = 15;
    private int mPage = 1;
    private int DEFAULT_PATE = 1;
    private MaintenanceShopListAdapter maintenanceShopListAdapter;

    private String brandID;
    private String seriesID;
    private String autoModelID;
    private String myAutoID;
    private String items;
    private BaseSharedPreferencesHelper sharedPreferencesHelper;

    private ArrayList< String > filterTip1s;
    private ArrayList< String > filterTip2s = new ArrayList() {{
        add("距离最近");
        add("价格最低");
    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        shopType = bundle.getInt(TYPE, 0);
        brandID = bundle.getString(BRANDID, "123");
        seriesID = bundle.getString(SERIESID, "101711793");
        autoModelID = bundle.getString(AUTOMODELID, "101637027");
        myAutoID = bundle.getString(MYAUTOID, "");
        items = bundle.getString(ITEMS, "{\"itemGroupID\":[\"hhs \"]}");

        setContentView(R.layout.activity_maintenance_shop_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mChangCity = (TextView) findViewById(R.id.change_city);
        mChangCity.setOnClickListener(this);

        mServiceLayoutView = (TextView) findViewById(R.id.service);
        mServiceLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MaterialDialog);
                builder.setTitle("拨打电话").setMessage("400-1868-555").setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri uri = Uri.parse("tel:" + "400-1868-555");
                        intent.setData(uri);
                        mContext.startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });
        if (shopType == 0) {
            toolbar.setTitle("4S店");
        } else if (shopType == 1) {
            toolbar.setTitle("快修店");
            mServiceLayoutView.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        mRelativeLayout = (RelativeLayout) findViewById(R.id.sort_group_layout);
//        if (shopType == 1) mRelativeLayout.setVisibility(View.VISIBLE);

//        mLineAnimView = (LineTranslateAnimView) findViewById(R.id.line_anim);

//        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.factor_group);
//        radioGroup.setOnCheckedChangeListener(this);
//        ((ThirdRadioButton) findViewById(R.id.sort_2)).setOnThirdStatusChangeListener(this);

        mDropDownMenu = (DropDownMenu) findViewById(R.id.drop_down_menu);

        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.auto_refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView, this);

        maintenanceShopListAdapter = new MaintenanceShopListAdapter(this, shopType == 0 ? false : true) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewMaintenanceShop newMaintenanceShop = getItemData(viewHolder.getAdapterPosition());
                        if (shopType == 0) {
                            ActivitySwitcherMaintenance.toFourSShopMaintanence(mContext, newMaintenanceShop);
                        } else if (shopType == 1) {
                            ActivitySwitcherMaintenance.toFourSAndQuickConfirmOrder(mContext, newMaintenanceShop);
                        }
                    }
                });
                return viewHolder;
            }
        };
        if (shopType == 0) maintenanceShopListAdapter.setShowDistance(true);

        recyclerView = (RecyclerView) findViewById(R.id.auto_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(maintenanceShopListAdapter);

        filterController = FilterController.getInstance();

        sharedPreferencesHelper = new BaseSharedPreferencesHelper(mContext);
        filterController.mFilterMap.put("sort", "50");
        filterController.mFilterMap.put("latitude", sharedPreferencesHelper.getLatitudeBD());
        filterController.mFilterMap.put("longitude", sharedPreferencesHelper.getLongitudeBD());

        initFragment();
        getData(true);
    }


    private ListFragment listFragment1;
    private ListFragment listFragment2;


    private void initFragment() {
        filterTip1s = new ArrayList<>();
        String[] tips = getResources().getStringArray(R.array.maintenanceCondition);
        Collections.addAll(filterTip1s, tips);

        maintenanceListFilterAdapter1 = new MaintenanceListFilterAdapter(filterTip1s, mContext) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(getResources().getColor(R.color.white));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDropDownMenu.setTabText(0, filterTip1s.get(position));
                        mDropDownMenu.closeMenu();
                        mPage = DEFAULT_PATE;
                        if (position == 0) {
                            filterController.mFilterMap.remove("province");
                            filterController.mFilterMap.remove("area");
                            filterController.mFilterMap.remove("district");
                        } else {
                            filterController.mFilterMap.put("province", sharedPreferencesHelper.getProvince());
                            filterController.mFilterMap.put("area", sharedPreferencesHelper.getCity());
                            filterController.mFilterMap.put("district", filterTip1s.get(position));
                        }
                        getData(true);
                        maintenanceListFilterAdapter1.setLastClickPos(position);
                        maintenanceListFilterAdapter1.notifyDataSetChanged();
                    }
                });
                return view;
            }
        };
        listFragment1 = new ListFragment();
        listFragment1.setListAdapter(maintenanceListFilterAdapter1);
        fragments.add(listFragment1);

        maintenanceListFilterAdapter2 = new MaintenanceListFilterAdapter(filterTip2s, mContext) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(getResources().getColor(R.color.white));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDropDownMenu.setTabText(1, filterTip2s.get(position));
                        mDropDownMenu.closeMenu();
                        mPage = DEFAULT_PATE;
                        if (position == 2) {
                            filterController.mFilterMap.put("sort", "40");
                        } else if (position == 1) {
                            filterController.mFilterMap.put("sort", "30");
                        } else if (position == 0) {
                            filterController.mFilterMap.put("sort", "50");
                        }
                        getData(true);
                        maintenanceListFilterAdapter2.setLastClickPos(position);
                        maintenanceListFilterAdapter2.notifyDataSetChanged();
                    }
                });
                return view;
            }
        };
        listFragment2 = new ListFragment();
        listFragment2.setListAdapter(maintenanceListFilterAdapter2);
        fragments.add(listFragment2);

        mDropDownMenu.setInflateFragment(true);
        mDropDownMenu.setDropDownMenu(Arrays.asList(titles), fragments);
    }


    /** 获取对应类型的店铺数据 **/
    private void getData(boolean showAnim) {
        if (shopType == INTERNALSHOP) { // 4S店接口
            filterController.getNewMaintenanceListData(mContext, mPage, PER_PAGE, brandID, seriesID, autoModelID, myAutoID, items, 10, filterController.mFilterMap, showAnim, this);
        } else if (shopType == OUTERSHOP) { // 快修店接口
            filterController.getNewMaintenanceListData(mContext, mPage, PER_PAGE, brandID, seriesID, autoModelID, myAutoID, items, 20, filterController.mFilterMap, showAnim, this);
        }
    }


//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        mPage = DEFAULT_PATE; // 初始化
//        if (checkedId == R.id.sort_1) {
//            mLineAnimView.startUnderlineAnim(0);
//            maintenanceShopListAdapter.setShowDistance(false);
//            recombineFilterCondition("sort", "10");
//
//        } else if (checkedId == R.id.sort_2) {
//            maintenanceShopListAdapter.setShowDistance(false);
//            mLineAnimView.startUnderlineAnim(1);
//
//        } else if (checkedId == R.id.sort_3) {
//            mLineAnimView.startUnderlineAnim(2);
//            maintenanceShopListAdapter.setShowDistance(false);
//            recombineFilterCondition("sort", "40");
//
//        } else if (checkedId == R.id.sort_4) {
//            mLineAnimView.startUnderlineAnim(3);
//            maintenanceShopListAdapter.setShowDistance(true);
//            recombineFilterCondition("sort", "50");
//        }
//    }


//    @Override
//    public void onThirdStatusChange(CompoundButton buttonView, boolean isChecked, int status) {
//        mPage = DEFAULT_PATE; // 初始化
//        switch (status) {
//            case 1:
//                maintenanceShopListAdapter.setShowDistance(false);
//                recombineFilterCondition("sort", "30");
//                break;
//
//            case 2:
//                maintenanceShopListAdapter.setShowDistance(false);
//                recombineFilterCondition("sort", "20");
//                break;
//        }
//    }


    /** 重组请求数据 **/
//    public void recombineFilterCondition(String key, String value) {
//        if (!TextUtils.isEmpty(key)) {
//            if (TextUtils.isEmpty(value)) {
//                filterController.mFilterMap.remove(key);
//            } else {
//                filterController.mFilterMap.put(key, value);
//            }
//        }
//        mPage = DEFAULT_PATE;
//        getData(true); // 发起请求
//    }
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPtrFrameLayoutView.isRefreshing()) mPtrFrameLayoutView.refreshComplete();
        filterController.destroy();
    }


    private ArrayList< NewMaintenanceShop > constructData() {
        ArrayList< NewMaintenanceShop > maintenanceShops = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NewMaintenanceShop newMaintenanceShop = new NewMaintenanceShop();
            newMaintenanceShop.shopTitle = "这是测试店铺 " + i;
            newMaintenanceShop.distance = i * 100;
            newMaintenanceShop.level = i + "级";
            newMaintenanceShop.evaluate = 4.39 + "";
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.ALIPAY = true;
//            }
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.WEIXIN = true;
//            }
//
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.offline = true;
//            }
//
//            if (Math.random() > 0.5) {
//                newMaintenanceShop.BALANCE = true;
//            }

            newMaintenanceShop.amount = i * 100;
            maintenanceShops.add(newMaintenanceShop);
        }
        return maintenanceShops;
    }


    @Override
    public void onSucceed(ArrayList< NewMaintenanceShop > maintenanceShops) {
        if (null == maintenanceShops) {
            if (mPage == DEFAULT_PATE) onFailed(false);
            return;
//            maintenanceShops = constructData();
        }

        if (mPage == DEFAULT_PATE) {
            if (maintenanceShops.size() <= 0) {
                onFailed(false);
                return;
            }

            maintenanceShopListAdapter.clearData();
        }

        hasMore = maintenanceShops.size() >= PER_PAGE;

        if (mRequestFailView.getVisibility() == View.VISIBLE) mRequestFailView.setVisibility(View.GONE);
        maintenanceShopListAdapter.addData(maintenanceShops);
    }


    @Override
    public void onFailed(boolean offLine) {
//        onSucceed(constructData());
//        return;
        if (offLine) {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        } else {
            mRequestFailView.setEmptyButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPage = DEFAULT_PATE;
                    getData(true);
                }
            });

            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
        if (mRequestFailView.getVisibility() == View.GONE) mRequestFailView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onFinish() {
        if (mPtrFrameLayoutView.isRefreshing()) mPtrFrameLayoutView.refreshComplete();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_city) {
            ActivitySwitcherMaintenance.toMapShopList(mContext, brandID, seriesID, autoModelID, myAutoID, items, shopType, filterController.mFilterMap);
        }
    }
}
