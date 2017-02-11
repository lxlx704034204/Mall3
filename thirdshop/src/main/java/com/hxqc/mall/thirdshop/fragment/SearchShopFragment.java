//package com.hxqc.mall.thirdshop.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewCompat;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.OnScrollListener;
//import android.text.TextUtils;
//import android.view.GestureDetector;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.amap.api.maps.model.LatLng;
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.core.api.DialogResponseHandler;
//import com.hxqc.mall.core.util.MapUtils;
//import com.hxqc.mall.core.views.DividerItemDecoration;
//import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
//import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.activity.ThirdShopSearchActivity;
//import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
//import com.hxqc.mall.thirdshop.model.ShopSearchShop;
//import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
//import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
//import com.hxqc.mall.thirdshop.views.adpter.SearchShopAdapter;
//import com.hxqc.util.JSONUtils;
//
//import cz.msebera.android.httpclient.Header;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import in.srain.cube.views.ptr.PtrFrameLayout;
//
///**
// * Author :liukechong
// * Date : 2015-12-01
// * FIXME
// * Todo
// */
//@Deprecated
//public class SearchShopFragment extends Fragment {
//    private static final String TAG = "SearchShopFragment";
//    private View view;
//
//    private RecyclerView search_shop_recycler_view;
//    private SearchShopAdapter searchShopAdapter;
//    private TabLayout search_tab;
//
//    private PtrFrameLayout refresh_frame;
//    private UltraPullRefreshHeaderHelper ultraPullRefreshHeaderHelper;
//
//    /**
//     * 搜索结果排序 |综合 1|距离升序 2|
//     */
//    private int sort = 1;
//    private int page = 1;
//    private String keyWord;
//    private HashMap<String, String> searchConditionMap;
//
//    GestureDetector gestureDetector;
//    GestureDetector.SimpleOnGestureListener gestureListener;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.t_fragment_search_shop, container, false);
//            search_shop_recycler_view = (RecyclerView) view.findViewById(R.id.search_shop_recycler_view);
//            search_tab = (TabLayout) view.findViewById(R.id.search_tab);
//            refresh_frame = (PtrFrameLayout) view.findViewById(R.id.refresh_frame);
//
//            initRecycleView();
//            initTabLayout();
//            initPtrFrame();
//            gestureListener = new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onDoubleTap(MotionEvent e) {
//                    return true;
//                }
//
//            };
//            gestureDetector = new GestureDetector(getContext(), gestureListener);
//
//        }
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            keyWord = arguments.getString("keyWord");
//            String searchConditionJson = arguments.getString("searchConditionJson");
//
//            if (keyWord != null) {
//                searchShopWithKeyWord(keyWord);
//            } else if (searchConditionJson != null) {
//                searchConditionMap = JSONUtils.fromJson(searchConditionJson, new TypeToken<HashMap<String, String>>() {
//                });
//                searchShopWithCondition();
//            }
//        }
//
//    }
//
//    private void initPtrFrame() {
//        ultraPullRefreshHeaderHelper = new UltraPullRefreshHeaderHelper(getContext(), refresh_frame);
//        ultraPullRefreshHeaderHelper.setOnRefreshHandler(new OnRefreshHandler() {
//            @Override
//            public boolean hasMore() {
//                return false;
//            }
//
//            @Override
//            public void onRefresh() {
//                searchShopWithSortTurning();
//            }
//
//            @Override
//            public void onLoadMore() {
//
//            }
//        });
//    }
//
//    private void initRecycleView() {
//        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        search_shop_recycler_view.setLayoutManager(manager);
////        search_shop_recycler_view.setHasFixedSize(true);
//
//        search_shop_recycler_view.setItemAnimator(new DefaultItemAnimator());
//        search_shop_recycler_view.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
//        searchShopAdapter = new SearchShopAdapter() {
//            @Override
//            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//                final Holder holder = super.onCreateViewHolder(parent, viewType);
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ShopSearchShop itemData = getItemData(holder.getAdapterPosition());
//                        ActivitySwitcherThirdPartShop.toShopDetails(getActivity(), itemData.shopID);
//                    }
//                });
//                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return gestureDetector.onTouchEvent(event);
//                    }
//                });
//
//                return holder;
//            }
//        };
//        search_shop_recycler_view.setAdapter(searchShopAdapter);
//
//        search_shop_recycler_view.addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && !ViewCompat.canScrollVertically(search_shop_recycler_view, 0)) {
//                    if (!isRequest) {
//                        searchPageTurning();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//            }
//        });
//
//    }
//
//    private void initTabLayout() {
//        final TabLayout.Tab tab1 = search_tab.newTab();
//
//        final TabLayout.Tab tab2 = search_tab.newTab();
//
//        tab1.setText("综合");
//        tab2.setText("距离");
//        search_tab.setTabMode(TabLayout.MODE_FIXED);
//        search_tab.setTabGravity(TabLayout.GRAVITY_FILL);
//
//
//        search_tab.addTab(tab1, true);
//        search_tab.addTab(tab2);
//        search_tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.text_color_orange));
//        search_tab.setTabTextColors(getResources().getColor(R.color.text_gray), getResources().getColor(R.color.text_color_orange));
//        search_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab1.isSelected()) {
//                    sort = 1;
//                } else if (tab2.isSelected()) {
//                    sort = 2;
//                }
//                searchShopWithSortTurning();
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//    }
//
//    enum Type {
//        keyword, condition, pageTurning, sortTurning
//    }
//
//    public void searchShopWithKeyWord(String keyWord) {
//        this.keyWord = keyWord;
//        page = 1;
//        searchConditionMap = null;
//        searchShop(keyWord, null, 1, Type.keyword);
//    }
//
//    private void searchShopWithCondition() {
//        page = 1;
//        searchShop(null, searchConditionMap, 1, Type.condition);
//    }
//
//    private void searchPageTurning() {
//        page++;
//        if (searchConditionMap == null) {
//            searchShop(keyWord, null, page, Type.pageTurning);
//        } else {
//            searchShop(null, searchConditionMap, page, Type.pageTurning);
//        }
//    }
//
//    private void searchShopWithSortTurning() {
//        page = 1;
//        if (searchConditionMap == null) {
//            searchShop(keyWord, null, 1, Type.sortTurning);
//        } else {
//            searchShop(null, searchConditionMap, 1, Type.sortTurning);
//        }
//    }
//
//    boolean isRequest = false;
//    SharedPreferencesHelper sharedPreferencesHelper;
//
//    private void searchShop(String text, HashMap<String, String> searchConditionMap, int page, final Type type) {
//
//        if (searchConditionMap == null) {
//            searchConditionMap = new HashMap<>();
//        }
//        if (!TextUtils.isEmpty(text)) {
//            searchConditionMap.put("keyword", text);
//        }
//        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());
//        String latitude = sharedPreferencesHelper.getLatitudeBD();
//        String longitude = sharedPreferencesHelper.getLongitudeBD();
//        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
//            LatLng latLng = null;
//            try {
//                latLng = MapUtils.bd_encrypt(Double.parseDouble(latitude), Double.parseDouble(longitude));
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//            if (latLng != null) {
//                searchConditionMap.put("latitude", Double.toString(latLng.latitude));
//                searchConditionMap.put("longitude", Double.toString(latLng.longitude));
//            }
//        }
//
//        boolean showAnim = true;
//        if (type == Type.sortTurning) {
//            showAnim = false;
//        }
//        ThirdPartShopClient client = new ThirdPartShopClient();
//        client.searchShop(page, 15, sort, searchConditionMap, new DialogResponseHandler(getContext(), showAnim) {
//            @Override
//            public void onSuccess(String response) {
//                ArrayList<ShopSearchShop> shopSearchShopList = JSONUtils.fromJson(response, new TypeToken<ArrayList<ShopSearchShop>>() {
//                });
//
////                if (pageTurning) {
////                    if (shopSearchShopList != null && !shopSearchShopList.isEmpty()) {
////                        searchShopAdapter.addDate(shopSearchShopList);
////                    } else {
////                        SearchShopFragment.this.page--;
////                    }
////                } else {
////                    if (shopSearchShopList != null && !shopSearchShopList.isEmpty()) {
////                        searchShopAdapter.replaceData(shopSearchShopList);
////                        search_shop_recycler_view.smoothScrollToPosition(0);
////                    }
////                }
//                if (type == Type.keyword) {
//                    if (shopSearchShopList != null && !shopSearchShopList.isEmpty()) {
//                        searchShopAdapter.replaceDate(shopSearchShopList);
//                        search_shop_recycler_view.smoothScrollToPosition(0);
//                    } else {
//                        searchShopAdapter.clearData();
//                        ((ThirdShopSearchActivity) getActivity()).requestFail("无搜索结果");
//                    }
//                } else if (type == Type.condition) {
//                    if (shopSearchShopList != null && !shopSearchShopList.isEmpty()) {
//                            searchShopAdapter.replaceDate(shopSearchShopList);
//                        search_shop_recycler_view.smoothScrollToPosition(0);
//                    } else {
//                        ((ThirdShopSearchActivity) getActivity()).requestFail("无搜索结果");
//                    }
//                } else if (type == Type.pageTurning) {
//                    if (shopSearchShopList != null && !shopSearchShopList.isEmpty()) {
//                        searchShopAdapter.addDate(shopSearchShopList);
//                    } else {
//                        SearchShopFragment.this.page--;
//                    }
//                } else if (type == Type.sortTurning) {
//                    if (shopSearchShopList != null && !shopSearchShopList.isEmpty()) {
//                        searchShopAdapter.replaceDate(shopSearchShopList);
//                        search_shop_recycler_view.smoothScrollToPosition(0);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                if (searchShopAdapter == null || searchShopAdapter.getItemCount() == 0) {
//                    ((ThirdShopSearchActivity) getActivity()).requestFail();
//                }
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                isRequest = true;
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                isRequest = false;
//                ultraPullRefreshHeaderHelper.refreshComplete(refresh_frame);
//            }
//        });
//    }
//
//
//}
