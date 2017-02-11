package com.hxqc.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.poisearch.PoiSearch;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.adapter.BaseMapViewPagerAdapter;
import com.hxqc.mall.core.adapter.MapListFilterAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.PullListView;
import com.hxqc.mall.core.views.SlidingUpView.SlidingUpLayout;
import com.hxqc.mall.core.views.dialog.LoadingDialog;
import com.hxqc.mall.core.views.dialog.SubmitDialog;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function: 用于在地图上选择店铺的基类Activity
 *
 * @author 袁秉勇
 * @since 2016年06月02日
 */
public abstract class BaseItemChooseOnMapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener, OnRefreshHandler, AMap.OnCameraChangeListener, AMap.OnMapClickListener, ViewPager.OnPageChangeListener, View.OnClickListener, PoiSearch.OnPoiSearchListener, DistrictSearch.OnDistrictSearchListener, RadioGroup.OnCheckedChangeListener, CheckBox.OnCheckedChangeListener, PullListView.LoadingDataCallBack {
    private final static String TAG = BaseItemChooseOnMapActivity.class.getSimpleName();
    private Context mContext;

    protected boolean dataFormGD = false; // 数据是否来自高德Api

    private SubmitDialog mProgressDialog; // 定位前显示加载Dialog
    private LoadingDialog loadingDialog;

    /** 地图设置相关 **/
    protected AMap aMap;
    private UiSettings mUiSettings;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private MarkerOptions markerOption;
    protected Marker lastClickMarker; // 历史点击的marker
    protected boolean addMarkerMove = false;
    protected AMapLocation mAMapLocation;
    protected PoiSearch.Query query;
    protected String userFilterInput = "";
    protected String title;
    protected LinearLayout mFilterContentView;
    protected TextView mFilterTextView;
    protected ImageView mFilterIconView;
    protected PopupWindow mPopupWindow;
    protected ArrayList< DistrictItem > districtItems = new ArrayList<>();
    protected boolean requestGasData = false;
    protected String areaCode;

    private LatLngBounds wholeLatLngBounds;
    private LatLngBounds visibleLatLngBounds;

    protected SlidingUpLayout mSlidingUpLayout;
    protected CardView mSearchView;
    protected EditText mSearchEditText;
    protected ImageButton mBackView;
    protected boolean showSearchView = false;
    protected ImageView mDeleteView, mTrafficTipView;
    protected TextView mTitleView;
    protected boolean showToolView = false;
    protected boolean showFilterContailerView = false;
    protected RadioGroup mRadioGroup;

    protected MapView mapView;
    protected LinearLayout mDragView;
    protected PullListView mListView;
    protected ViewPager mViewPager;

    protected CheckBox mRoadView;
    protected ImageButton mRelocView;
    protected ImageButton mIncreaseView;
    protected ImageButton mReduceView;

    protected static final int DEFAULT_PATE = 1;
    protected int mPage = DEFAULT_PATE; // 当前页
    protected final int PER_PAGE = 10;
    protected int mRecentMaxPage = 1; // 历史加载到的最大数据页数

    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    protected RequestFailView mRequestFailView;

    protected int listItemSelectedPos = -1;

    protected ArrayList< LatLng > latLngs = new ArrayList<>();
    protected ArrayList< Marker > markers = new ArrayList<>();
    private int mapViewWidth;
    private int mapViewHeight;

    protected boolean clearAllDataFlag = false;

    protected boolean typeIsRote = false; // 是否是路径规划

    protected boolean canLoadData = false;

    protected Handler mHandler = new Handler();

    protected int mapViewVisiableHeight;


    /** 初始化ApiClient用于请求数据 **/
    protected abstract void initApi();

    /** 初始化构造器，该方法中一定要设置mListView.setAdapter方法 **/
    protected abstract void initAdapter();

    /** 设置锚点 **/
    protected abstract void setAnchorPoint();

    /** 当前为返回上一个分段数据时走的方法 **/
    protected abstract void refreshData(boolean hasLoadingAnim);

    /** 初次进入页面或者加载更多时走该方法法 **/
    protected abstract void getData(boolean showLoadingAnim);

    /** 请求数据后返回时所走的方法 **/
    protected abstract void onSuccessResponse(String response);

    /** 组装需要在地图上展示的Marker的坐标数据 **/
    protected abstract ArrayList< LatLng > assemblyMarkerData();

    /** 点击详细条目时跳转 **/
    protected abstract void clickItemToActivity(int position);

    /** 设置ListView当前展示的是不是第一页的数据，以及其它的一些状态设置 **/
    protected abstract void setListViewIsAtFirstPage(boolean isAtFirstPage);


    @Override
    public void onRefresh() {
        if (mPage == DEFAULT_PATE) {
            return;
        }

        mPage = --mPage > DEFAULT_PATE ? mPage : DEFAULT_PATE;
        refreshData(false);
    }


    @Override
    public void onLoadMore() {
        ++mPage;
        if (mRecentMaxPage < mPage) {
            getData(true);
        } else {
            refreshData(true);
        }
    }


    @Override
    public void loadMore() {
        onLoadMore();
    }


    @Override
    public boolean hasMore() {
        // 不通过PtrFrameLayout来处理ListView的上拉加载时间（canLoadData一直被设置为false）
        return mPtrHelper.isHasMore() && canLoadData;
    }


    protected LoadingAnimResponseHandler getLoadingAnimResponseHandler(boolean isShowAnim) {
        return new LoadingAnimResponseHandler(mContext, isShowAnim) {
            @Override
            public void onSuccess(final String response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccessResponse(response);
                    }
                });
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                onSuccessResponse("");
                if (mPage == DEFAULT_PATE) {
                    mRequestFailView.setVisibility(View.VISIBLE);
                    mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
                    mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mRequestFailView.setVisibility(View.GONE);
                            getData(true);
                        }
                    });
                }
                mPage = mPage > 1 ? --mPage : mPage;
                setListViewIsAtFirstPage(mPage == DEFAULT_PATE);
            }
        };
    }


    protected void showNoData(String desc) {
        if (mPage == DEFAULT_PATE) {
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setEmptyDescription(desc);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
    }


    protected void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
            loadingDialog.setCancelable(true);
        }
        loadingDialog.show();
    }


    protected void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_choose_item_on_map);

        mSlidingUpLayout = (SlidingUpLayout) findViewById(R.id.slidinguplayout);
        if (typeIsRote) {
            mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.HIDDEN);
        } else {
            setAnchorPoint();
        }

        mSearchEditText = (EditText) findViewById(R.id.search_text);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mSearchEditText.clearFocus();
                    if (mAMapLocation == null) {
                        showPermissionTip();
                        return true;
                    }

                    if (TextUtils.isEmpty(mSearchEditText.getText())) {
                        ToastHelper.showRedToast(mContext, "请输入查询内容");
                        return true;
                    }

                    mPage = DEFAULT_PATE;
                    userFilterInput = mSearchEditText.getText().toString();
                    getData(true);
                    return true;
                }
                return false;
            }
        });

        mSearchView = (CardView) findViewById(R.id.search_view);
        mBackView = (ImageButton) findViewById(R.id.around_map_back);

        mSearchView.findViewById(R.id.back_in_search_view).setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(this);
        mBackView.setOnClickListener(this);

        if (showSearchView) {
            mSearchView.setVisibility(View.VISIBLE);
            mBackView.setVisibility(View.GONE);
        }

        if (showToolView) {
            mSlidingUpLayout.setShowToolView(true);
            findViewById(R.id.tool_view).setVisibility(View.VISIBLE);
        }

        mDeleteView = (ImageView) findViewById(R.id.delete_input);
        mDeleteView.setOnClickListener(this);

        mTrafficTipView = (ImageView) findViewById(R.id.traffic_tip);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        initMap();

        mRoadView = (CheckBox) findViewById(R.id.around_map_road);
        mRelocView = (ImageButton) findViewById(R.id.around_map_reloc);
        mIncreaseView = (ImageButton) findViewById(R.id.around_map_increase);
        mReduceView = (ImageButton) findViewById(R.id.around_map_reduce);

        mRoadView.setOnCheckedChangeListener(this);
        mRelocView.setOnClickListener(this);
        mIncreaseView.setOnClickListener(this);
        mReduceView.setOnClickListener(this);

        mDragView = (LinearLayout) findViewById(R.id.dragView);
        mDragView.findViewById(R.id.back_in_drag_view).setOnClickListener(this);
        mTitleView = (TextView) mDragView.findViewById(R.id.title);
        mTitleView.setText(title);
        mFilterContentView = (LinearLayout) mDragView.findViewById(R.id.filter_content);
        mFilterContentView.setOnClickListener(this);
        mFilterContentView.setClickable(false);
        mFilterTextView = (TextView) mFilterContentView.findViewById(R.id.filter_text);
        mFilterIconView = (ImageView) mFilterContentView.findViewById(R.id.filter_icon);

        if (showFilterContailerView) {
            mFilterContentView.setVisibility(View.VISIBLE);
        }

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(mContext, mPtrFrameLayoutView, this);
        mPtrHelper.setOnRefreshHandler(this);

        mRequestFailView = (RequestFailView) findViewById(R.id.fail_view);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(this);
//        mViewPager.setCurrentItem(PER_PAGE * 100, false);

        mListView = (PullListView) findViewById(R.id.list_view);
        mListView.setLoadingDataCallBack(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, final int position, long id) {
                if (listItemSelectedPos == position) {
                    clickItemToActivity(position);
                } else {
//                    /** 改变最近一个marker的样式 **/
//                    if (lastClickMarker != null) {
//                        lastClickMarker.setIcon(BitmapDescriptorFactory.fromView(getMarkerView(lastClickMarker, false, Integer.valueOf(lastClickMarker.getObject().toString()) + 1)));
//                        lastClickMarker = null;
//                    }

                    findMarkerAndChangeStyle(position);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.EXPANDED) {
                                mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.ANCHORED);
                            }

                            ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(position);
                            if (position == 0) ((BaseMapListAdapter) mListView.getAdapter()).notifyDataSetChanged();
                            mListView.setSelection(position);
                        }
                    });

                    judgeMarkerPosition(position);

                    listItemSelectedPos = position;
                    mSlidingUpLayout.setCanMoveToBottom(listItemSelectedPos == -1);
                    mViewPager.setCurrentItem(position, false);
                }
            }
        });

        initApi();
        initAdapter();

        if (dataFormGD || requestGasData) {
            showProgressDialog("初始化定位", true);
        }

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mapViewWidth = getResources().getDisplayMetrics().widthPixels;

                        mapViewVisiableHeight = mSlidingUpLayout.getmTopView().getHeight();

                        DebugLog.e("mapVisibleHeight", " mapViewVisibleHeight : " + mapViewVisiableHeight);

                        int[] location = new int[2];
                        mapView.getLocationOnScreen(location);

                        Rect rect = new Rect();
                        mapView.getGlobalVisibleRect(rect);

                        mapViewHeight = rect.bottom - rect.top;


                        /** 获取数据放这里的原因：防止当数据返回时，而MapViewHeight值为零，在addMarkersToMap出现除数为零的情况 **/
                        if (!dataFormGD && !requestGasData) {
                            getData(true);
                        }
                    }
                });
            }
        });
    }


    /**
     * 根据带选择Marker是否在屏幕可是区域内，如果在就直接改变样式，否则将该Marker直接移动到可视区域中心点
     *
     * @param position
     */
    private void judgeMarkerPosition(int position) {
        if (lastClickMarker == null) {
            moveMapToMarkerCenter(position);
        } else {
            try {
                if (mSearchView.isShown()) {
                    visibleLatLngBounds = new LatLngBounds(aMap.getProjection().fromScreenLocation(new Point(0, mapViewVisiableHeight)), aMap.getProjection().fromScreenLocation(new Point(mapViewWidth, mSearchView.getBottom())));
                } else {
                    visibleLatLngBounds = new LatLngBounds(aMap.getProjection().fromScreenLocation(new Point(0, mapViewVisiableHeight)), aMap.getProjection().fromScreenLocation(new Point(mapViewWidth, 0)));
                }
            } catch (IllegalArgumentException e) {
                visibleLatLngBounds = wholeLatLngBounds;
            } catch (Exception e) {
                visibleLatLngBounds = wholeLatLngBounds;
            }

            if (!visibleLatLngBounds.contains(lastClickMarker.getPosition())) {
                moveMapToMarkerCenter(position);
            } else {
                findMarkerAndChangeStyle(position);
            }
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog(String text, boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new SubmitDialog(this);
            mProgressDialog.setText(text);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setCanceledOnTouchOutside(cancelable);
        }

        if (!mProgressDialog.isShowing()) mProgressDialog.show();
    }


    /**
     * 隐藏进度框
     */
    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    /** 移动Map到Maker在DragView上方可视区域的中心 **/
    private void moveMapToMarkerCenter(final int position) {
        LatLng latLng = latLngs.get(position);
        // 当前屏幕中DragView上方区域的中心点坐标
        LatLng tmpLatLng = aMap.getProjection().fromScreenLocation(new Point(mapViewWidth / 2, mapViewVisiableHeight / 2));

        // 当前MapView的中心点坐标
        LatLng curCenterLatLng = aMap.getCameraPosition().target;

        // 所需移动到的MapView的中心点坐标
        LatLng centerLatLng = new LatLng((curCenterLatLng.latitude + latLng.latitude - tmpLatLng.latitude), (curCenterLatLng.longitude + latLng.longitude - tmpLatLng.longitude));

        aMap.animateCamera(CameraUpdateFactory.changeLatLng(centerLatLng), 500, new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
                findMarkerAndChangeStyle(position);
            }


            @Override
            public void onCancel() {

            }
        });
    }


    /** 找到当前条目对应的Marker **/
    private void findMarkerAndChangeStyle(int position) {
        DebugLog.e(TAG, " 找到对应Marker，并改变Marker样式");
//        if (aMap != null && aMap.getMapScreenMarkers() != null && aMap.getMapScreenMarkers().size() > 0) {
//            for (int i = 0; i < aMap.getMapScreenMarkers().size(); i++) {
//                if (aMap.getMapScreenMarkers().get(i).getObject() != null && aMap.getMapScreenMarkers().get(i).getObject().equals(position)) {
//                    aMap.getMapScreenMarkers().get(i).setIcon(BitmapDescriptorFactory.fromView(getMarkerView(aMap.getMapScreenMarkers().get(i), true, position + 1)));
//                    lastClickMarker = aMap.getMapScreenMarkers().get(i);
//                    break;
//                }
//            }
//        }
        if (lastClickMarker != null && (int) lastClickMarker.getObject() == position) return;
        if (lastClickMarker != null) lastClickMarker.setIcon(setMarkerIcon((Integer) lastClickMarker.getObject(), false));
        lastClickMarker = markers.get(position);
        lastClickMarker.setIcon(setMarkerIcon(position, true));
        lastClickMarker.setToTop();
    }


    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);

            aMap.setLocationSource(this); // 设置定位监听
            mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
            aMap.setMyLocationEnabled(true); // 是否可触发定位并显示定位层

            aMap.setOnMapLoadedListener(this); // 设置amap加载成功事件监听器
            aMap.setOnMarkerClickListener(this); // 设置点击marker事件监听器
            aMap.setOnCameraChangeListener(this); // 设置
            aMap.setOnMapClickListener(this); // 设置地图点击事件
        }
    }


    protected void clearMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        if (markers.size() > 0) markers.clear();
        if (lastClickMarker != null) lastClickMarker = null;
    }


    /**
     * 在地图上添加marker
     */
    protected void addMarkersToMap(ArrayList< LatLng > latLngs) {
        if (aMap != null) {
            clearMarkers();
            listItemSelectedPos = -1;
            mSlidingUpLayout.setCanMoveToBottom(listItemSelectedPos == -1);
        }

        if (latLngs.size() <= 0) return;

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (int i = 0; i < latLngs.size(); i++) {
            markerOption = new MarkerOptions();
            markerOption.position(latLngs.get(i));
            builder.include(latLngs.get(i));
            markerOption.icon(setMarkerIcon(i, false));
            Marker marker = aMap.addMarker(markerOption);
            marker.setObject(i);
            markers.add(marker);
        }

        LatLngBounds bounds = builder.build();

        if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.EXPANDED) {
            wholeLatLngBounds = bounds;
        } else {
            if (showSearchView) {
                double latitude = 2 * bounds.southwest.latitude - bounds.northeast.latitude;
                wholeLatLngBounds = new LatLngBounds(new LatLng(latitude, bounds.southwest.longitude), new LatLng(bounds.northeast.latitude + (mSearchView.getBottom() / ((mapViewHeight - mapViewVisiableHeight) * 1.0) * (bounds.northeast.latitude - latitude)), bounds.northeast.longitude));
            } else {
                wholeLatLngBounds = new LatLngBounds(new LatLng(bounds.southwest.latitude - (mapViewHeight - mapViewVisiableHeight) / (mapViewVisiableHeight * 1.0) * (bounds.northeast.latitude - bounds.southwest.latitude), bounds.southwest.longitude), bounds.northeast);
            }
        }

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(wholeLatLngBounds, 200));

        DebugLog.e(TAG, mSlidingUpLayout.getTop() + " SlidingUpLayout Top ============ " + " searchViewHeight : " + mSearchView.getHeight() + "     " + mSearchView.getBottom() + "   " + mapViewVisiableHeight);

        DebugLog.e(TAG, "================" + bounds.toString()); // southwest=lat/lng: (30.59275,114.255621),northeast=lat/lng: (30.77275,114.975621)
        DebugLog.e(TAG, "================1" + wholeLatLngBounds.toString()); // southwest=lat/lng: (30.59275,114.255621),northeast=lat/lng: (30.77275,114.975621)
    }


    @Override
    protected void onStart() {
        DebugLog.e(TAG, "----------- onStart");
        super.onStart();
    }


    @Override
    protected void onResume() {
        DebugLog.e(TAG, "----------- onResume");
        super.onResume();

        mapView.onResume();
    }


    @Override
    protected void onPause() {
        DebugLog.e(TAG, "----------- onPause");
        super.onPause();

        mapView.onResume();
    }


    @Override
    protected void onStop() {
        DebugLog.e(TAG, "----------- onStop");
        super.onStop();
    }


    @Override
    public void finish() {
        DebugLog.e(TAG, "----------- finish");
        super.finish();
    }


    @Override
    protected void onDestroy() {
        DebugLog.e(TAG, "----------- onDestroy");
        super.onDestroy();

        mapView.onDestroy();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    protected LatLng latLng;

    protected String defAreaCode;


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation); // 显示系统小蓝点
                mAMapLocation = amapLocation;
                latLng = MapUtils.bd_encrypt(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());

                DebugLog.e(TAG, " xxxxxxxxxxxxxxx=============== : " + mAMapLocation.toString());
                dismissProgressDialog();
                if (dataFormGD || requestGasData) {
                    defAreaCode = areaCode = amapLocation.getCityCode();
                    getData(true);
                    getFilterData();
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                DebugLog.e("AmapErr", errText);

                dismissLoadingDialog();

                showPermissionTip();
            }
        }
    }


    protected void getFilterData() {
        if (mAMapLocation == null) return;
        DistrictSearch districtSearch = new DistrictSearch(this);
        DistrictSearchQuery districtSearchQuery = new DistrictSearchQuery();
        districtSearchQuery.setKeywords(mAMapLocation.getCityCode());
        districtSearchQuery.setKeywordsLevel(DistrictSearchQuery.KEYWORDS_DISTRICT);
        districtSearch.setQuery(districtSearchQuery);
        districtSearch.setOnDistrictSearchListener(this);
        districtSearch.searchDistrictAsyn();
    }


    protected AlertDialog alertDialog;


    protected void showPermissionTip() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this, R.style.MaterialDialog).setTitle("定位服务未开启").setMessage("请在系统设置中开启定位服务").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 2);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((Activity) mContext).finish();
                }
            }).create();
            alertDialog.setCancelable(false);
        }
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (gPSIsOPen(this)) {
                mLocationClient.startLocation();
            } else {
                showPermissionTip();
            }
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public boolean gPSIsOPen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中

//        LatLngBounds bounds = new LatLngBounds.Builder().build();
//
//        for (int i = 0; i < latLngs.length; i++) {
//            bounds.including(latLngs[i]);
//        }
//
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));


//        LatLngBounds bounds = new LatLngBounds.Builder().include(Constants.Test1).include(Constants.Test2).include(Constants.Test3).include(Constants.Test4).include(Constants.Test5).include(Constants.Test6).include(Constants.Test7).include(Constants.Test8).include(Constants.Test9).include(Constants.Test10).build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        DebugLog.e(TAG, " ID ：" + marker.getId() + " object :  " + marker.getObject());

//        if (lastClickMarker == null) {
//            DebugLog.e(TAG, "Marker 为空");
//
//
//        } else {

        if (lastClickMarker != null) {
            DebugLog.e(TAG, "Marker 不为空 " + marker.toString() + "  " + lastClickMarker.toString());

            if (lastClickMarker.equals(marker)) {
                DebugLog.e(TAG, "Marker 相等");
                if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.HIDDEN) {
                    mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.BOTTOMANCHORED);
                }
                return true;
            }
//            if (!lastClickMarker.equals(marker)) {
            DebugLog.e(TAG, "Marker 不相等");
//                lastClickMarker.setIcon(BitmapDescriptorFactory.fromView(getMarkerView(lastClickMarker, false, Integer.valueOf(lastClickMarker.getObject().toString()) + 1)));
//            } else {
//                if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.HIDDEN) {
//                    mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.BOTTOMANCHORED);
//                }
//                return true;
//            }
        }

        findMarkerAndChangeStyle((Integer) marker.getObject());

        if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.HIDDEN || mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.COLLAPSED) {
            mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.BOTTOMANCHORED);
        }

        ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(listItemSelectedPos = (int) marker.getObject());
        if (listItemSelectedPos == 0) ((BaseMapListAdapter) mListView.getAdapter()).notifyDataSetChanged();
        mListView.setSelection(Integer.valueOf(marker.getObject().toString()));

//        mViewPager.setCurrentItem(listItemSelectedPos + PER_PAGE * 100, false);
        mViewPager.setCurrentItem(listItemSelectedPos, false);
//        marker.setIcon(BitmapDescriptorFactory.fromView(getMarkerView(marker, true, listItemSelectedPos + 1)));
//        lastClickMarker = marker;
        mSlidingUpLayout.setCanMoveToBottom(listItemSelectedPos == -1);
        return true;
    }


//    private View getMarkerView(Marker marker, boolean isSelected, int position) {
//        View view = LayoutInflater.from(this).inflate(R.layout.layout_marker_background, null);
//        if (isSelected) {
//            view.findViewById(R.id.view_text).setBackgroundResource(R.drawable.location_blue);
//            if (marker != null) marker.setToTop();
//        } else {
//            view.findViewById(R.id.view_text).setBackgroundResource(R.drawable.location_red);
//        }
//
//        ((TextView) view.findViewById(R.id.view_text)).setText(position + "");
//        return view;
//    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        DebugLog.e(TAG, " onCameraChange--------------");
    }


    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        // cameraPosition toString : lat/lng: (30.682748,114.615623)
        DebugLog.e(TAG, " ================== cameraPosition toString : " + cameraPosition.target);

        DebugLog.e(TAG, " ================  zoom : " + cameraPosition.zoom);

        DebugLog.e(TAG, " top : " + mapView.getTop() + " right : " + mapView.getRight() + " bottom : " + mapView.getBottom() + " MapView右上定点坐标转地理坐标 : " + aMap.getProjection().fromScreenLocation(new Point(mapView.getRight(), mapView.getTop())).toString());

        DebugLog.e(TAG, "屏幕中心点的坐标 ： " + aMap.getProjection().fromScreenLocation(new Point(mapView.getBottom() / 2, mapView.getRight() / 2)));

        DebugLog.e(TAG, "通过cameraPosition获取到的当前地图中心点在屏幕上的像素坐标 : " + aMap.getProjection().toScreenLocation(cameraPosition.target) + "  MapView中心点坐标 ： Point( " + mapView.getRight() / 2 + " , " + mapView.getBottom() / 2 + ")");

        DebugLog.e(TAG, mSlidingUpLayout.getTop() + " SlidingUpLayout Top ============ this is at CameraChangeFinish");

//        aMap.addMarker(new MarkerOptions().position(aMap.getProjection().fromScreenLocation(new Point(mapView.getRight() / 2, mapView.getBottom() / 2))));

//        aMap.addMarker(new MarkerOptions().position(cameraPosition.target).icon(BitmapDescriptorFactory.fromResource(R.drawable.umeng_socialize_location_off)));
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (typeIsRote) return;
        if (listItemSelectedPos == -1) {
            if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.COLLAPSED) {
                mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.HIDDEN);
            } else {
                mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.COLLAPSED);
            }
        } else {
            if (mSlidingUpLayout.getPanelState() == SlidingUpLayout.PanelState.BOTTOMANCHORED) {
                mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.HIDDEN);
            } else {
                mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.BOTTOMANCHORED);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }
        if (!typeIsRote && mSlidingUpLayout.getPanelState() != SlidingUpLayout.PanelState.EXPANDED && mSlidingUpLayout.getPanelState() != SlidingUpLayout.PanelState.ANCHORED) {
            mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.ANCHORED);
        } else {
            super.onBackPressed();
            finish();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        ((BaseMapViewPagerAdapter) mViewPager.getAdapter()).setSelectedPosition(position);
        mViewPager.getAdapter().notifyDataSetChanged();

        ((BaseMapListAdapter) mListView.getAdapter()).setSelectedPosition(position);
        ((BaseMapListAdapter) mListView.getAdapter()).notifyDataSetChanged();
        mListView.setSelection(position);

        /** 改变最近一个marker的样式 **/
//        if (lastClickMarker != null) {
//            lastClickMarker.setIcon(BitmapDescriptorFactory.fromView(getMarkerView(lastClickMarker, false, Integer.valueOf(lastClickMarker.getObject().toString()) + 1)));
//            lastClickMarker = null;
//        }

        findMarkerAndChangeStyle(position);

        judgeMarkerPosition(position);

        listItemSelectedPos = position;
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.around_map_road) {
            //路况
            if (mRoadView.isChecked()) {
                if (aMap != null) {
                    aMap.setTrafficEnabled(true);
                    mTrafficTipView.setVisibility(View.VISIBLE);
                }
            } else {
                if (aMap != null) {
                    aMap.setTrafficEnabled(false);
                    mTrafficTipView.setVisibility(View.GONE);
                }
            }
        }
    }


    protected MapListFilterAdapter mapListFilterAdapter;


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_input) {
            mSearchEditText.clearFocus();
            mSearchEditText.setText("");

            if (mAMapLocation == null) {
                return;
            }
            mPage = DEFAULT_PATE;
            userFilterInput = "";
            getData(true);
        } else if (v.getId() == R.id.around_map_reloc) {
            //重新定位
            if (mAMapLocation != null) {
                LatLng latLng = aMap.getProjection().fromScreenLocation(new Point(mapViewWidth / 2, mapViewVisiableHeight / 2));
                LatLng centerLatLng = aMap.getProjection().fromScreenLocation(new Point(mapViewWidth / 2, mapViewHeight / 2));
                if (aMap != null) aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(mAMapLocation.getLatitude() - latLng.latitude + centerLatLng.latitude, mAMapLocation.getLongitude() - latLng.longitude + centerLatLng.longitude)));
            } else {
                if (mLocationClient != null) mLocationClient.startLocation();
            }
        } else if (v.getId() == R.id.around_map_increase) {
            //放大地图
            if (aMap != null) aMap.animateCamera(CameraUpdateFactory.zoomIn(), 200, null);
        } else if (v.getId() == R.id.around_map_reduce) {
            //缩小地图
            if (aMap != null) aMap.animateCamera(CameraUpdateFactory.zoomOut(), 200, null);
        } else if (v.getId() == R.id.around_map_back || v.getId() == R.id.back_in_search_view) {
            // 点击返回按钮
            if (!typeIsRote && mSlidingUpLayout.getPanelState() != SlidingUpLayout.PanelState.ANCHORED) {
                mSlidingUpLayout.setPanelState(SlidingUpLayout.PanelState.ANCHORED);
            } else {
                finish();
            }
        } else if (v.getId() == R.id.back_in_drag_view) {
            finish();
        } else if (v.getId() == R.id.filter_content) {
            if (mPopupWindow == null) {
                View popupContent = LayoutInflater.from(this).inflate(R.layout.layout_map_filter_view, null);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics().heightPixels / 2);
                popupContent.findViewById(R.id.hidden_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
                ListView listView = (ListView) popupContent.findViewById(R.id.list_view);
                listView.setLayoutParams(params);

                final MapListFilterAdapter mapListFilterAdapter = new MapListFilterAdapter(this, districtItems);
                listView.setAdapter(mapListFilterAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                        mFilterTextView.setText(districtItems.get(position).getName());
                        areaCode = districtItems.get(position).getAdcode();
                        clearAllDataFlag = true;
                        mPage = DEFAULT_PATE;
                        mRecentMaxPage = mPage;
                        getData(true);
                        mPopupWindow.dismiss();
                        mapListFilterAdapter.setLastClickPos(position);
                        mapListFilterAdapter.notifyDataSetChanged();
                    }
                });

                mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.setContentView(popupContent);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x88000000));
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);
            }
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.showAsDropDown(mSlidingUpLayout.findViewById(R.id.dragView));
            }
        }
    }


    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        if (districtResult.getDistrict() != null && districtResult.getDistrict().size() > 0) {
            districtItems = districtResult.getDistrict();
            mFilterContentView.setClickable(true);
        } else {
            mFilterContentView.setVisibility(View.GONE);
        }
    }


    /** 单独设置每个marker的样式 **/
    private BitmapDescriptor setMarkerIcon(int position, boolean selected) {
        BitmapDescriptor bitmapDescriptor = null;
        switch (position) {
            case 0:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_1_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_1_red);
                break;

            case 1:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_2_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_2_red);
                break;

            case 2:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_3_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_3_red);
                break;

            case 3:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_4_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_4_red);
                break;

            case 4:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_5_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_5_red);
                break;

            case 5:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_6_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_6_red);
                break;

            case 6:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_7_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_7_red);
                break;

            case 7:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_8_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_8_red);
                break;

            case 8:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_9_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_9_red);
                break;

            case 9:
                bitmapDescriptor = selected ? BitmapDescriptorFactory.fromResource(R.drawable.poi_10_blue) : BitmapDescriptorFactory.fromResource(R.drawable.poi_10_red);
                break;
        }
        return bitmapDescriptor;
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        DebugLog.e(TAG, "onTouchEvent ===============");
//        if (mPopupWindow != null && mPopupWindow.isShowing()) {
//            mPopupWindow.dismiss();
//            return true;
//        }
//        return super.onTouchEvent(event);
//    }
//
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mPopupWindow != null && mPopupWindow.isShowing()) {
//            return onTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}