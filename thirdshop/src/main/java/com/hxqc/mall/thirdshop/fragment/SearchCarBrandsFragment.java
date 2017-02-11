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
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckedTextView;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.core.api.DialogResponseHandler;
//import com.hxqc.mall.core.views.DividerItemDecoration;
//import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
//import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
//import com.hxqc.mall.thirdshop.R;
//
//import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
//import com.hxqc.mall.thirdshop.model.ShopSearchAuto;
//import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
//import com.hxqc.mall.thirdshop.views.adpter.SearchCarBrandsAdapter;
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
//public class SearchCarBrandsFragment extends Fragment {
//
//    private static final String TAG = "SearchCarBrandsFragment";
//    View view;
//    RecyclerView search_car_brands_recycle_view;
//    TabLayout search_tab;search_tab
//    SearchCarBrandsAdapter searchCarBrandsAdapter;
//    int sort = 2;
//    String keyWord;
//    int page = 1;
//    HashMap<String, String> searchConditionMap;
//    boolean isRequest = false;
//    private PtrFrameLayout refresh_frame;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.t_fragment_search_car_brands, container, false);
//            search_car_brands_recycle_view = (RecyclerView) view.findViewById(R.id.search_car_brands_recycle_view);
//            search_tab = (TabLayout) view.findViewById(R.id.search_tab);
//            refresh_frame = (PtrFrameLayout) view.findViewById(R.id.refresh_frame);
//            initTabLayout();
//            initRecycleView();
//            initPtrFrame();
//        }
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            keyWord = arguments.getString("keyWord");
//            String searchConditionJson = arguments.getString("searchConditionJson");
//            if (!TextUtils.isEmpty(keyWord)) {
//                searchWithKeyWord(keyWord);
//            } else if (!TextUtils.isEmpty(searchConditionJson)) {
//                searchConditionMap = JSONUtils.fromJson(searchConditionJson, new TypeToken<HashMap<String, String>>() {
//                });
//                searchWithCondition();
//            }
//        }
//
//    }
//
//    UltraPullRefreshHeaderHelper ultraPullRefreshHeaderHelper;
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
//                searchWithSortTurning();
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
//        search_car_brands_recycle_view.setLayoutManager(manager);
//        search_car_brands_recycle_view.setItemAnimator(new DefaultItemAnimator());
//        search_car_brands_recycle_view.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
//        search_car_brands_recycle_view.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//            }
//        });
//
//        searchCarBrandsAdapter = new SearchCarBrandsAdapter() {
//            @Override
//            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//                final Holder holder = super.onCreateViewHolder(parent, viewType);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
//                        ActivitySwitcherThirdPartShop.toCarDetail(itemData.autoInfo.itemID, itemData.shopInfo.shopID, itemData.shopInfo.shopTitle, getActivity());
//                    }
//                });
//                holder.car_dial.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        com.hxqc.mall.core.util.OtherUtil.callPhone(getActivity(), getItemData(holder.getAdapterPosition()).shopInfo.shopTel);
//                    }
//                });
//                holder.ask_price.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
//                        ActivitySwitcherThirdPartShop.toAskLeastPrice(getActivity(), itemData.shopInfo.shopID, itemData.autoInfo.itemID, itemData.autoInfo.itemName, itemData.shopInfo.shopTel, true, null);
//                    }
//                });
//                return holder;
//
//            }
//        };
//        search_car_brands_recycle_view.setAdapter(searchCarBrandsAdapter);
//        search_car_brands_recycle_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && !ViewCompat.canScrollVertically(search_car_brands_recycle_view, 0)) {
//                    if (!isRequest) {
//                        searchPageTurning();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
//    }
//
//    private void initTabLayout() {
//
//        final TabLayout.Tab tab1 = search_tab.newTab();
//        final TabLayout.Tab tab2 = search_tab.newTab();
//        final CheckedTextView checkedTextView = (CheckedTextView) getActivity().getLayoutInflater().inflate(R.layout.t_item_tab_view, search_tab, false);
//        tab1.setCustomView(checkedTextView);
//        tab1.setText("价格");
//        tab2.setText("降价");
//        checkedTextView.setCheckMarkDrawable(R.drawable.ic_sortup);
//
//        search_tab.addTab(tab1, true);
//        search_tab.addTab(tab2);
//
//
////        final ViewPager search_viewpager = (ViewPager) view.findViewById(R.id.search_viewpager);
////        search_viewpager.setAdapter(new PagerAdapter() {
////            @Override
////            public int getCount() {
////                return 2;
////            }
////
////            @Override
////            public boolean isViewFromObject(View view, Object object) {
////                return view==object;
////            }
////
////            @Override
////            public Object instantiateItem(ViewGroup container, int position) {
////                RecyclerView search_car_brands_recycle_view_2 = (RecyclerView) view.findViewById(R.id.search_car_brands_recycle_view_2);
////                if (position==0) {
////                    return search_car_brands_recycle_view;
////                }else{
////                    return search_car_brands_recycle_view_2;
////                }
////            }
////
////            @Override
////            public void destroyItem(ViewGroup container, int position, Object object) {
////                super.destroyItem(container, position, object);
////            }
////
////            @Override
////            public CharSequence getPageTitle(int position) {
////                if (position==1) {
////                    return "价格";
////                }else{
////                    return "降幅";
////                }
////            }
////        });
//
////        search_tab.setupWithViewPager(search_viewpager);
//        search_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                int position = tab.getPosition();
//                if (position == 0) {
//
//                }
//                if (tab1.isSelected()) {
//                    checkedTextView.setCheckMarkDrawable(R.drawable.ic_sortup);
//                    sort = 2;
//                    checkedTextView.setChecked(true);
//                } else if (tab2.isSelected()) {
//                    sort = 3;
//                    checkedTextView.setCheckMarkDrawable(R.drawable.ic_sort);
//                    checkedTextView.setChecked(false);
//                }
//                searchWithSortTurning();
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
//                if (tab1.isSelected()) {
//                    if (sort == 1) {
//                        sort = 2;
//                        checkedTextView.setCheckMarkDrawable(R.drawable.ic_sortup);
//                    } else if (sort == 2) {
//                        sort = 1;
//                        checkedTextView.setCheckMarkDrawable(R.drawable.ic_sortdown);
//                    }
//                    searchWithSortTurning();
//                }
//            }
//        });
//
//    }
//
//    enum Type {
//        keyword, condition, pageTurning, sortTurning
//    }
//
//    /**
//     * 关键字搜索
//     */
//    public void searchWithKeyWord(String keyWord) {
//        this.keyWord = keyWord;
//        page = 1;
//        searchConditionMap = null; //条件map置空
//        searchCar(keyWord, null, 1, Type.keyword);
//
//    }
//
//    /**
//     * 条件搜索
//     */
//    private void searchWithCondition() {
//        page = 1;
//        searchCar(null, searchConditionMap, 1, Type.condition);
//    }
//
//    /**
//     * 结果翻页
//     */
//    private void searchPageTurning() {
//        page++;
//        if (searchConditionMap == null) {
//            searchCar(keyWord, null, page, Type.pageTurning);
//        } else {
//            searchCar(null, searchConditionMap, page, Type.pageTurning);
//        }
//    }
//
//    /**
//     * 换条件搜索
//     */
//    private void searchWithSortTurning() {
//        page = 1;
//        if (searchConditionMap == null) {
//            searchCar(keyWord, null, page, Type.sortTurning);
//        } else {
//            searchCar(null, searchConditionMap, page, Type.sortTurning);
//        }
//    }
//
//    private void searchCar(final String keyWord, HashMap<String, String> conditionMap, int page, final Type type) {
//        if (conditionMap == null) {
//            conditionMap = new HashMap<>();
//        }
//        if (keyWord != null) {
//            conditionMap.put("keyword", keyWord);
//        }
//        boolean showAnim = true;
//        if (type == Type.sortTurning) {
//            showAnim = false;
//        }
//        ThirdPartShopClient client = new ThirdPartShopClient();
//        client.searchCar(page, 15, sort, conditionMap, new DialogResponseHandler(getContext(), showAnim) {
//            @Override
//            public void onSuccess(String response) {
//                ArrayList<ShopSearchAuto> shopSearchAutoList = JSONUtils.fromJson(response, new TypeToken<ArrayList<ShopSearchAuto>>() {
//                });
//                if (type == Type.keyword) {
//                    if (shopSearchAutoList != null && !shopSearchAutoList.isEmpty()) {
//                        searchCarBrandsAdapter.replaceDate(shopSearchAutoList);
//                        search_car_brands_recycle_view.smoothScrollToPosition(0);
//                    } else {
//                        searchCarBrandsAdapter.clearData();
//                        ((ThirdShopSearchActivity) getActivity()).requestFail("无搜索结果");
//                    }
//                } else if (type == Type.condition) {
//                    if (shopSearchAutoList != null && !shopSearchAutoList.isEmpty()) {
//                        searchCarBrandsAdapter.replaceDate(shopSearchAutoList);
//                        search_car_brands_recycle_view.smoothScrollToPosition(0);
//                    } else {
//                        ((ThirdShopSearchActivity) getActivity()).requestFail("无搜索结果");
//                    }
//                } else if (type == Type.pageTurning) {
//                    if (shopSearchAutoList != null && !shopSearchAutoList.isEmpty()) {
//                        searchCarBrandsAdapter.addDate(shopSearchAutoList);
//                    } else {
//                        SearchCarBrandsFragment.this.page--;
//                    }
//                } else if (type == Type.sortTurning) {
//                    if (shopSearchAutoList != null && !shopSearchAutoList.isEmpty()) {
//                        searchCarBrandsAdapter.replaceDate(shopSearchAutoList);
//                        search_car_brands_recycle_view.smoothScrollToPosition(0);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                if (searchCarBrandsAdapter == null || searchCarBrandsAdapter.getItemCount() == 0) {
//                    ((ThirdShopSearchActivity) getActivity()).requestFail();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                isRequest = false;
//                ultraPullRefreshHeaderHelper.refreshComplete(refresh_frame);
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                isRequest = true;
//            }
//        });
//    }
//
//
//}
