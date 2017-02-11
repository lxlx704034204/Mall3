package com.hxqc.aroundservice.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.aroundservice.adapter.IllegalQueryResultAdapter;
import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.control.IllegalQueryControl;
import com.hxqc.aroundservice.control.IllegalSharedPreferencesHelper;
import com.hxqc.aroundservice.model.EstimatePrice;
import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.aroundservice.model.IllegalQueryRequestData;
import com.hxqc.aroundservice.model.IllegalQueryResult;
import com.hxqc.aroundservice.model.IllegalQueryResultInfo;
import com.hxqc.aroundservice.model.ProvinceAndCity;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.aroundservice.view.QueryResultBarV2;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 07
 * FIXME
 * Todo 违章查询结果
 */
public class IllegalQueryResultActivity extends BackActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private RecyclerView mQueryResultView;
    //    private TextView mHintView;
    private RequestFailView mNoDataView;
    private IllegalQueryResultAdapter illegalQueryResultAdapter;
    private ActionBar supportActionBar;
    private MenuItem mIllegalProcessingCancelView;
    private MenuItem mIllegalProcessingView;
    private MenuItem mIllegalHistoryView;
    private QueryResultBarV2 mBottomBarView;
    private boolean isEr = true;
    private String plateNumber;
    private String substring;//缓存已勾选违章查询结果字符串
    private View mBlackView;
    private boolean isFirst = true;//第一的判断
    private String cityCode;
    private String cityName;
    private String provinceName;
    private String hpzl;
    private String engineno;
    private String classno;
    private IllegalSharedPreferencesHelper imIllegalSharedPreferencesHelper = null;
    private TextView mTotalNumberView;
    private String total;
    private LinearLayout mTotalTitelView;
    private IllegalQueryRequestData illegalQueryRequestData;
    private int flagActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illegal_query_result);

        initView();

        initData();

        initEvent();
    }

    private void initEvent() {
        mBottomBarView.setOnClickProcessingListener(clickProcessingListener);
        mBottomBarView.setOnClickShowInfoListener(clickShowInfoListener);
        mBlackView.setOnClickListener(clickBlackListener);
        if (illegalQueryResultAdapter != null) {
            illegalQueryResultAdapter.setOnItemClickListener(itemClickListener);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundleExtra = null;
        if (getIntent() != null) {
            bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
//            IllegalOrderDetail illegalOrderDetail = getIntent().getParcelableExtra("illegalOrderDetail");
            flagActivity = bundleExtra.getInt("flagActivity");
            imIllegalSharedPreferencesHelper = new IllegalSharedPreferencesHelper(this);
            if (flagActivity == OrderDetailContants.ILLEGAL_DETAIL_LIST) {
                DebugLog.i(TAG, "ILLEGAL_DETAIL_LIST");
                IllegalOrderDetail illegalOrderDetail = bundleExtra.getParcelable("illegalOrderDetail");
                if (illegalOrderDetail != null) {
                    imLists = illegalOrderDetail.wzList;
                    mTotalTitelView.setVisibility(View.GONE);
//                    mHintView.setVisibility(View.GONE);
                    if (illegalQueryResultAdapter == null) {
                        illegalQueryResultAdapter = new IllegalQueryResultAdapter(this, illegalOrderDetail.wzList, isEr, true);
                        mQueryResultView.setLayoutManager(new LinearLayoutManager(this));
                        mQueryResultView.setAdapter(illegalQueryResultAdapter);
//                    illegalQueryResultAdapter.setOnCheckQueryListener(checkQueryListener);
                    } else {
                        illegalQueryResultAdapter.notifyData(illegalOrderDetail.wzList, true);
                    }
                    setListParams();
                    mBottomBarView.setVisibility(View.GONE);
//                mBottomBarView.setBarVisibility(View.GONE, View.GONE, View.GONE);
                    supportActionBar.setTitle(illegalOrderDetail.plateNumber);
                    mBottomBarView.setV1Text(illegalOrderDetail.count, OtherUtil.amountFormat(illegalOrderDetail.fine, true), illegalOrderDetail.fen);
                    plateNumber = illegalOrderDetail.plateNumber;
                    DebugLog.i(TAG, "plateNumber: " + plateNumber);
                    supportActionBar.setTitle(plateNumber);
                }
            } else if (flagActivity == OrderDetailContants.ILLEGAL_NEW_LIST) {
                //                ProvinceAndCity provinceAndCity = getIntent().getParcelableExtra("provinceAndCity");
                DebugLog.i(TAG, "ILLEGAL_NEW_LIST");
                IllegalQueryResult illegalQueryResult = bundleExtra.getParcelable("illegalQueryResult");
                illegalQueryRequestData = bundleExtra.getParcelable("illegalQueryRequestData");
                if (illegalQueryResult.reason.equals("数据为空")) {
                    mNoDataView.setVisibility(View.VISIBLE);
                    mNoDataView.showEmptyPage("您目前没有未处理的交通违章记录", R.drawable.ic_empty_illegal, false, "", null);
//                    mNoDataView.setRequestViewType(RequestFailView.RequestViewType.empty);
//                    mNoDataView.setEmptyDescription("您目前没有未处理的交通违章记录");
                    if (illegalQueryResult != null && illegalQueryResult.result != null) {
                        if (!TextUtils.isEmpty(illegalQueryResult.result.hphm)) {
                            plateNumber = illegalQueryResult.result.hphm;
                        } else {
                            plateNumber = imIllegalSharedPreferencesHelper.getHPHM();
                        }
                    } else {
                        plateNumber = imIllegalSharedPreferencesHelper.getHPHM();
                    }
                    DebugLog.i(TAG, "plateNumber: " + plateNumber);
                    isEr = checkPlateNum(plateNumber);
                    DebugLog.i(TAG, "isCheck:" + isEr);
                    if (!isEr) {
                        mTotalTitelView.setVisibility(View.GONE);
                    }
                } else {
                    ProvinceAndCity provinceAndCity = illegalQueryResult.result;
                    if (provinceAndCity != null) {
                        DebugLog.i(TAG, provinceAndCity.toString());
                        mNoDataView.setVisibility(View.GONE);
//            IllegalQueryControl.getInstance().requestIllegalQuery(this, testData(), this);
                        if (!TextUtils.isEmpty(provinceAndCity.hphm)) {
                            plateNumber = provinceAndCity.hphm;
                        } else {
                            plateNumber = imIllegalSharedPreferencesHelper.getHPHM();
                        }
//                        cityCode = provinceAndCity.cityCode;
                        cityCode = "HUB_WuHan";
                        cityName = "武汉";
                        provinceName = "湖北";
//                        hpzl = provinceAndCity.hpzl;
                        hpzl = "02";
                        engineno = imIllegalSharedPreferencesHelper.getEngineno();
                        classno = imIllegalSharedPreferencesHelper.getClassno();
                        supportActionBar.setTitle(plateNumber);
                        if (!TextUtils.isEmpty(provinceAndCity.hphm)) {
                            isEr = checkPlateNum(provinceAndCity.hphm);
                        } else {
                            isEr = checkPlateNum(plateNumber);
                        }
                        DebugLog.i(TAG, "isCheck:" + isEr);
                        if (!isEr) {
                            mTotalTitelView.setVisibility(View.GONE);
                        }
                        mNoDataView.setVisibility(View.GONE);
//        if (true) {
                        imLists = provinceAndCity.lists;
                        DebugLog.i(TAG, "size:" + imLists.size());
                        if (imLists != null && !imLists.isEmpty()) {
                            mBottomBarView.setV2Text(OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true));
                            if (illegalQueryResultAdapter == null) {
                                illegalQueryResultAdapter = new IllegalQueryResultAdapter(this, imLists, isEr, false);
                                mQueryResultView.setLayoutManager(new LinearLayoutManager(this));
                                mQueryResultView.setAdapter(illegalQueryResultAdapter);
                                illegalQueryResultAdapter.setOnCheckQueryListener(checkQueryListener);
                            } else {
                                illegalQueryResultAdapter.notifyData(imLists, false);
                            }
                            if (isEr) {
                                checkIllegalEstimatePrice();
                            }
                            totalData(provinceAndCity);
                            if (isEr) {
                                mBottomBarView.setBarVisibility(View.GONE, View.VISIBLE, View.GONE);
                                if (illegalQueryResultAdapter != null) {
                                    illegalQueryResultAdapter.notifyState(true, false);
                                }
                            } else {
                                setListParams();
                                mBottomBarView.setVisibility(View.GONE);
                                if (illegalQueryResultAdapter != null) {
                                    illegalQueryResultAdapter.notifyState(false, false);
                                }
                            }
                        } else {
                            mNoDataView.setVisibility(View.VISIBLE);
                            mNoDataView.showEmptyPage("您目前没有未处理的交通违章记录", R.drawable.ic_empty_illegal, false, "", null);
//                        mNoDataView.setRequestViewType(RequestFailView.RequestViewType.empty);
//                        mNoDataView.setEmptyDescription("您目前没有未处理的交通违章记录");
                        }
                    }
                }
            } else if (flagActivity == OrderDetailContants.ILLEGAL_HISTORY_LIST) {
                DebugLog.i(TAG, "ILLEGAL_HISTORY_LIST");
                IllegalQueryResult illegalQueryResult = bundleExtra.getParcelable("illegalQueryResult");
                if (!TextUtils.isEmpty(illegalQueryResult.result.hphm)) {
                    plateNumber = illegalQueryResult.result.hphm;
                } else {
                    plateNumber = imIllegalSharedPreferencesHelper.getHPHM();
                }
                DebugLog.i(TAG, "plateNumber: " + plateNumber);
                supportActionBar.setTitle(plateNumber);
                imLists = illegalQueryResult.result.lists;
                if (imLists.isEmpty()) {
                    mNoDataView.setVisibility(View.VISIBLE);
                    mNoDataView.showEmptyPage("您目前没有历史的交通违章记录", R.drawable.ic_empty_illegal, false, "", null);
                } else {
                    mNoDataView.setVisibility(View.GONE);
                    if (illegalQueryResultAdapter == null) {
                        illegalQueryResultAdapter = new IllegalQueryResultAdapter(this, imLists, isEr, true);
                        mQueryResultView.setLayoutManager(new LinearLayoutManager(this));
                        mQueryResultView.setAdapter(illegalQueryResultAdapter);
//                    illegalQueryResultAdapter.setOnCheckQueryListener(checkQueryListener);
                    } else {
                        illegalQueryResultAdapter.notifyData(imLists, true);
                    }
                    setListParams();
                }
                mTotalTitelView.setVisibility(View.GONE);
//                mHintView.setVisibility(View.GONE);
//                mBottomBarView.setVisibility(View.GONE);
                mBottomBarView.setBarVisibility(View.GONE, View.GONE, View.GONE);
//                supportActionBar.setTitle(illegalOrderDetail.plateNumber);
//                mBottomBarView.setV1Text(illegalOrderDetail.count, OtherUtil.amountFormat(illegalOrderDetail.fine, true), illegalOrderDetail.fen);

            }

            /*IllegalOrderDetail illegalOrderDetail = bundleExtra.getParcelable("illegalOrderDetail");
            if (illegalOrderDetail != null) {
                mTotalTitelView.setVisibility(View.GONE);
                mHintView.setVisibility(View.GONE);
                if (illegalQueryResultAdapter == null) {
                    illegalQueryResultAdapter = new IllegalQueryResultAdapter(this, illegalOrderDetail.wzList, isEr, true);
                    mQueryResultView.setLayoutManager(new LinearLayoutManager(this));
                    mQueryResultView.setAdapter(illegalQueryResultAdapter);
//                    illegalQueryResultAdapter.setOnCheckQueryListener(checkQueryListener);
                } else {
                    illegalQueryResultAdapter.notifyData(illegalOrderDetail.wzList, true);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, 0);
                mQueryResultView.setLayoutParams(layoutParams);
                mBottomBarView.setVisibility(View.GONE);
//                mBottomBarView.setBarVisibility(View.GONE, View.GONE, View.GONE);
                supportActionBar.setTitle(illegalOrderDetail.plateNumber);
                mBottomBarView.setV1Text(illegalOrderDetail.count, OtherUtil.amountFormat(illegalOrderDetail.fine, true), illegalOrderDetail.fen);

            } else {
//                ProvinceAndCity provinceAndCity = getIntent().getParcelableExtra("provinceAndCity");
                bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
                IllegalQueryResult illegalQueryResult = bundleExtra.getParcelable("illegalQueryResult");
                illegalQueryRequestData = bundleExtra.getParcelable("illegalQueryRequestData");
                if (illegalQueryResult.reason.equals("数据为空")) {
                    mNoDataView.setVisibility(View.VISIBLE);
                    mNoDataView.showEmptyPage("您目前没有未处理的交通违章记录", R.drawable.ic_empty_illegal, false, "", null);
//                    mNoDataView.setRequestViewType(RequestFailView.RequestViewType.empty);
//                    mNoDataView.setEmptyDescription("您目前没有未处理的交通违章记录");
                } else {
                    ProvinceAndCity provinceAndCity = illegalQueryResult.result;
                    if (provinceAndCity != null) {
                        DebugLog.i(TAG, provinceAndCity.toString());
                        mNoDataView.setVisibility(View.GONE);
                        imIllegalSharedPreferencesHelper = new IllegalSharedPreferencesHelper(this);
//            IllegalQueryControl.getInstance().requestIllegalQuery(this, testData(), this);
                        plateNumber = provinceAndCity.hphm;
//                        cityCode = provinceAndCity.cityCode;
                        cityCode = "HUB_WuHan";
//                        hpzl = provinceAndCity.hpzl;
                        hpzl = "02";
                        engineno = imIllegalSharedPreferencesHelper.getEngineno();
                        classno = imIllegalSharedPreferencesHelper.getClassno();
                        supportActionBar.setTitle(provinceAndCity.hphm);
                        if (!TextUtils.isEmpty(provinceAndCity.hphm)) {
                            isEr = checkPlateNum(provinceAndCity.hphm);
                        }
                        DebugLog.i(TAG, "isCheck:" + isEr);
                        if (isEr) {
                            mHintView.setVisibility(View.GONE);
                        } else {
                            mHintView.setVisibility(View.VISIBLE);
                        }
                        mNoDataView.setVisibility(View.GONE);
//        if (true) {
                        lists = provinceAndCity.lists;
                        if (lists != null && !lists.isEmpty()) {
                            mBottomBarView.setV2Text(OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true));
                            if (illegalQueryResultAdapter == null) {
                                illegalQueryResultAdapter = new IllegalQueryResultAdapter(this, lists, isEr, false);
                                mQueryResultView.setLayoutManager(new LinearLayoutManager(this));
                                mQueryResultView.setAdapter(illegalQueryResultAdapter);
                                illegalQueryResultAdapter.setOnCheckQueryListener(checkQueryListener);
                            } else {
                                illegalQueryResultAdapter.notifyData(lists, false);
                            }
                            checkIllegalEstimatePrice();
                            totalData(provinceAndCity);
                            if (isEr) {
                                mBottomBarView.setBarVisibility(View.GONE, View.VISIBLE, View.GONE);
                                if (illegalQueryResultAdapter != null) {
                                    illegalQueryResultAdapter.notifyState(true, false);
                                }
                            } else {

                                mBottomBarView.setBarVisibility(View.VISIBLE, View.GONE, View.GONE);
                                if (illegalQueryResultAdapter != null) {
                                    illegalQueryResultAdapter.notifyState(false, false);
                                }
                            }
                        } else {
                            mNoDataView.setVisibility(View.VISIBLE);
                            mNoDataView.showEmptyPage("您目前没有未处理的交通违章记录", R.drawable.ic_empty_illegal, false, "", null);
//                        mNoDataView.setRequestViewType(RequestFailView.RequestViewType.empty);
//                        mNoDataView.setEmptyDescription("您目前没有未处理的交通违章记录");
                        }
                    }
                }
            }*/
        }
    }

    /**
     * 设置布局
     */
    private void setListParams() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        mQueryResultView.setLayoutParams(layoutParams);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        supportActionBar = getSupportActionBar();
//        DebugLog.i(TAG, supportActionBar.getTitle().toString());
//        mHintView = (TextView) findViewById(R.id.illegal_query_result_hint);
        mQueryResultView = (RecyclerView) findViewById(R.id.illegal_query_result_list);
        mNoDataView = (RequestFailView) findViewById(R.id.illegal_query_result_no_data);
        mBottomBarView = (QueryResultBarV2) findViewById(R.id.illegal_query_result_total);
        mBottomBarView.setBarVisibility(View.GONE, View.GONE, View.GONE);
        mBottomBarView.setInfoBtnViewState(View.GONE);
        mBottomBarView.setProcessingViewEnabled(false);

        mBlackView = findViewById(R.id.illegal_query_result_half_black);

        mTotalNumberView = (TextView) findViewById(R.id.illegal_query_result_total_number);
        mTotalTitelView = (LinearLayout) findViewById(R.id.illegal_query_result_total_title);
    }

    private boolean isShow = false;
    /**
     * 详情
     */
    private QueryResultBarV2.OnClickShowInfoListener clickShowInfoListener = new QueryResultBarV2.OnClickShowInfoListener() {
        @Override
        public void onClickShowInfo(View v) {
            DebugLog.i(TAG, "showinfo");
            if (isShow) {
                DebugLog.i(TAG, "隐藏");
                mBottomBarView.setInfoViewState(View.GONE, !isShow);
                mBlackView.setVisibility(View.GONE);
                isShow = false;
            } else {
                DebugLog.i(TAG, "显示");
                mBottomBarView.setInfoViewState(View.VISIBLE, !isShow);
                mBlackView.setVisibility(View.VISIBLE);
                isShow = true;
            }
        }
    };

    /**
     * 黑色背景点击事件
     */
    private View.OnClickListener clickBlackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isShow) {
                mBottomBarView.setInfoViewState(View.GONE, !isShow);
                mBlackView.setVisibility(View.GONE);
                isShow = false;
            }
        }
    };

    /**
     * 去处理按钮监听
     */
    private QueryResultBarV2.OnClickProcessingListener clickProcessingListener = new QueryResultBarV2.OnClickProcessingListener() {
        @Override
        public void clickProcessingListener(View v) {
            if (imResults == null || imResults.isEmpty()) {
                ToastHelper.showRedToast(IllegalQueryResultActivity.this, "请选择");
            } else {
                UserInfoHelper.getInstance().loginAction(IllegalQueryResultActivity.this, onLoginListener);
            }
        }
    };

    private UserInfoHelper.OnLoginListener onLoginListener = new UserInfoHelper.OnLoginListener() {
        @Override
        public void onLoginSuccess() {
            DebugLog.i(TAG, "substring:" + substring);
            ActivitySwitchAround.toQueryProcessingActivity(IllegalQueryResultActivity.this, supportActionBar.getTitle().toString(), substring);
        }
    };

    private boolean isHave = false;
    private StringBuilder imWzID = null;//缓存的字符串
    private ArrayList<IllegalQueryResultInfo> imResults = null;//缓存的违章结果
    private boolean isSelected;//是否勾选的判断
    private int imPosition = -1;//是否勾选的标识项目
    /**
     * 违章条目的选择监听
     */
    private IllegalQueryResultAdapter.OnCheckQueryListener checkQueryListener = new IllegalQueryResultAdapter.OnCheckQueryListener() {
        @Override
        public void checkQuery(int position) {
    /*        if (illegalQueryResultAdapter.getmIllegalQueryResultInfos().get(position).fen.equals("12")) {

            }*/
            imPosition = position;
            isSelected = illegalQueryResultAdapter.getIsSelected().get(position);
            illegalQueryResultAdapter.getIsSelected().put(position, !isSelected);
            illegalQueryResultAdapter.notifyDataSetChanged();
//            String wzID = "";
            if (imWzID == null) {
                imWzID = new StringBuilder();
            }
            DebugLog.i(TAG, "有值");
            if (imResults == null) {
                imResults = new ArrayList<>();
            }

            if (!isSelected) {
                imResults.add(imLists.get(position));
            } else {
                imResults.remove(imLists.get(position));
            }
            DebugLog.i(TAG, "size:" + imResults.size());
            for (int i = 0; i < imResults.size(); i++) {
                DebugLog.i(TAG, imResults.get(i).date);
                imWzID.append(imResults.get(i).wzID + ",");
            }
            if (!imResults.isEmpty()) {
                DebugLog.i(TAG, "有选择");
                mBottomBarView.setInfoBtnViewState(View.VISIBLE);
                mBottomBarView.setProcessingViewEnabled(true);
            } else {
                DebugLog.i(TAG, "无选择");
                mBottomBarView.setV2Text(OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true));
                mBottomBarView.setInfoBtnViewState(View.GONE);
                imWzID = null;
                mBottomBarView.setProcessingViewEnabled(false);
            }
            if (imWzID != null) {
                DebugLog.i(TAG, "wzID:" + imWzID.toString());
                if (!TextUtils.isEmpty(imWzID.toString())) {
                    substring = imWzID.substring(0, imWzID.length() - 1);
                    IllegalQueryControl.getInstance().getIllegalEstimatePrice(IllegalQueryResultActivity.this, illegalEstimatePriceCallBack, plateNumber, substring, cityCode, hpzl, engineno, classno);
                    imWzID = null;
                }
            }
        }
    };

    private IllegalQueryResultAdapter.OnItemClickListener itemClickListener = new IllegalQueryResultAdapter.OnItemClickListener() {
        @Override
        public void itemClick(int position) {
            if (imLists != null && !imLists.isEmpty()) {
                if (flagActivity == OrderDetailContants.ILLEGAL_HISTORY_LIST) {
                    ActivitySwitchAround.toIllegalDetailActivity(IllegalQueryResultActivity.this, imLists.get(position), plateNumber, true);
                } else {
                    ActivitySwitchAround.toIllegalDetailActivity(IllegalQueryResultActivity.this, imLists.get(position), plateNumber);
                }
            }
        }
    };

    /**
     * 第一次进入时的服务金额请求及第一次进入时获取失败的重新请求
     */
    private void checkIllegalEstimatePrice() {
        if (imWzID == null) {
            imWzID = new StringBuilder();
        }
        if (imResults == null) {
            imResults = new ArrayList<>();
        } else {
            imResults.clear();
        }
        for (int i = 0; i < imLists.size(); i++) {
            if (!imLists.get(i).wfjfs.equals("12")) {
                imResults.add(imLists.get(i));
            }
        }
        mBottomBarView.setInfoBtnViewState(View.VISIBLE);
        mBottomBarView.setProcessingViewEnabled(true);
        DebugLog.i(TAG, imResults.get(0).toString());
        for (int i = 0; i < imResults.size(); i++) {
            DebugLog.i(TAG, imResults.get(i).date);
            imWzID.append(imResults.get(i).wzID + ",");
        }
        if (imWzID != null) {
            DebugLog.i(TAG, "wzID:" + imWzID.toString());
            if (!TextUtils.isEmpty(imWzID.toString())) {
                substring = imWzID.substring(0, imWzID.length() - 1);
                IllegalQueryControl.getInstance().getIllegalEstimatePrice(IllegalQueryResultActivity.this, illegalEstimatePriceCallBack, plateNumber, substring, cityCode, hpzl, engineno, classno);
                imWzID = null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_illegal_processing, menu);
        mIllegalProcessingView = menu.findItem(R.id.illegal_processing_btn);
        mIllegalHistoryView = menu.findItem(R.id.illegal_history_btn);
        mIllegalProcessingCancelView = menu.findItem(R.id.illegal_processing_cancel);
        if (isEr) {
            mIllegalProcessingView.setVisible(false);
            mIllegalHistoryView.setVisible(false);
            mIllegalProcessingCancelView.setVisible(false);
        } else {
            mIllegalHistoryView.setVisible(false);
            mIllegalProcessingView.setVisible(false);
            mIllegalProcessingCancelView.setVisible(false);
        }
        if (flagActivity == OrderDetailContants.ILLEGAL_HISTORY_LIST) {
            mIllegalHistoryView.setVisible(false);
            mIllegalProcessingView.setVisible(false);
            mIllegalProcessingCancelView.setVisible(false);
        } else if (flagActivity == OrderDetailContants.ILLEGAL_DETAIL_LIST) {
            mIllegalHistoryView.setVisible(false);
            mIllegalProcessingView.setVisible(false);
            mIllegalProcessingCancelView.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.illegal_processing_btn) {
            /*mIllegalProcessingView.setVisible(false);
            mIllegalProcessingCancelView.setVisible(true);
            mBottomBarView.setBarVisibility(View.GONE, View.VISIBLE, View.GONE);
            if (illegalQueryResultAdapter != null) {
                illegalQueryResultAdapter.notifyState(true);
            }*/
            DebugLog.i(TAG, OrderDetailContants.SERVICE_EXPLAIN);
            ActivitySwitchBase.toH5Activity(this, "服务说明", OrderDetailContants.SERVICE_EXPLAIN);

        } else if (item.getItemId() == R.id.illegal_history_btn) {
            illegalQueryRequestData.handled = "1";
            if (!TextUtils.isEmpty(plateNumber)) {
                if (plateNumber.substring(0, 2).equals("鄂A")) {
                    /*IllegalQueryControl.getInstance().requestIllegalQueryWH(this, illegalQueryRequestData, new CallBack<IllegalQueryResult>() {
                        @Override
                        public void onSuccess(IllegalQueryResult response) {
                            ActivitySwitchAround.toIllegalQueryResultActivity(IllegalQueryResultActivity.this, OrderDetailContants.ILLEGAL_HISTORY_LIST, response, null);
                        }

                        @Override
                        public void onFailed(boolean offLine) {

                        }
                    });*/
                    IllegalQueryControl.getInstance().requestIllegalQuery(this, illegalQueryRequestData, new CallBackControl.CallBack<IllegalQueryResult>() {
                        @Override
                        public void onSuccess(IllegalQueryResult response) {
                            ActivitySwitchAround.toIllegalQueryResultActivity(IllegalQueryResultActivity.this, OrderDetailContants.ILLEGAL_HISTORY_LIST, response, null);
                        }

                        @Override
                        public void onFailed(boolean offLine) {

                        }
                    });
                } else {
                    IllegalQueryControl.getInstance().requestIllegalQuery(this, illegalQueryRequestData, new CallBackControl.CallBack<IllegalQueryResult>() {
                        @Override
                        public void onSuccess(IllegalQueryResult response) {
                            ActivitySwitchAround.toIllegalQueryResultActivity(IllegalQueryResultActivity.this, OrderDetailContants.ILLEGAL_HISTORY_LIST, response, null);
                    /*mTotalTitelView.setVisibility(View.GONE);
                    mHintView.setVisibility(View.GONE);
                    if (illegalQueryResultAdapter == null) {
                        illegalQueryResultAdapter = new IllegalQueryResultAdapter(IllegalQueryResultActivity.this, response.result.lists, isEr, true);
                        mQueryResultView.setLayoutManager(new LinearLayoutManager(IllegalQueryResultActivity.this));
                        mQueryResultView.setAdapter(illegalQueryResultAdapter);
//                    illegalQueryResultAdapter.setOnCheckQueryListener(checkQueryListener);
                    } else {
                        illegalQueryResultAdapter.notifyData(response.result.lists, true);
                    }
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(0, 0, 0, 0);
                    mQueryResultView.setLayoutParams(layoutParams);
//                    mBottomBarView.setVisibility(View.GONE);
                    mBottomBarView.setBarVisibility(View.GONE, View.GONE, View.GONE);
//                    supportActionBar.setTitle(illegalOrderDetail.plateNumber);
//                    mBottomBarView.setV1Text(illegalOrderDetail.count, OtherUtil.amountFormat(illegalOrderDetail.fine, true), illegalOrderDetail.fen);
                    mIllegalProcessingView.setVisible(false);*/
                        }

                        @Override
                        public void onFailed(boolean offLine) {

                        }
                    });
                }
            }
      /*  else if (item.getItemId() == R.id.illegal_processing_cancel) {
            mIllegalProcessingView.setVisible(true);
            mIllegalProcessingCancelView.setVisible(false);
            mBottomBarView.setBarVisibility(View.VISIBLE, View.GONE, View.GONE);
            if (illegalQueryResultAdapter != null) {
                illegalQueryResultAdapter.notifyState(false);
            }
        }*/
        }

        return false;
    }

    private ArrayList<IllegalQueryResultInfo> imLists = null;//违章查询结果集

/*    @Override
    public void onIllegalQuerySucceed(IllegalQueryResult illegalQueryResult) {

        //TODO 假数据
     *//*   lists = new ArrayList<>();
        IllegalQueryResultInfo illegalQueryResultInfo = new IllegalQueryResultInfo();
        illegalQueryResultInfo.date = "2015-01-25 15:41:00";
        illegalQueryResultInfo.area = "60110:树藩大街(零公里处)M树藩大街(中医院路口)M";
        illegalQueryResultInfo.act = "10392:机动车违反禁止停车禁令标志指示的";
        illegalQueryResultInfo.code = "10392";
        illegalQueryResultInfo.fen = "0";
        illegalQueryResultInfo.money = "100";
        illegalQueryResultInfo.handled = "未缴费";
        illegalQueryResultInfo.longitude = "114.030942";
        illegalQueryResultInfo.PayNo = "";
        lists.add(illegalQueryResultInfo);*//*

        mNoDataView.setVisibility(View.GONE);
        if (illegalQueryResult != null) {
//        if (true) {
            lists = illegalQueryResult.result.lists;
            if (lists != null) {
                if (!lists.isEmpty()) {
                    mBottomBarView.setV2Text(OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true), OtherUtil.amountFormat("0", true));
                    if (illegalQueryResultAdapter == null) {
                        illegalQueryResultAdapter = new IllegalQueryResultAdapter(this, lists, isEr);
                        mQueryResultView.setLayoutManager(new LinearLayoutManager(this));
                        mQueryResultView.setAdapter(illegalQueryResultAdapter);
                        illegalQueryResultAdapter.setOnCheckQueryListener(checkQueryListener);
                    } else {
                        illegalQueryResultAdapter.notifyData(lists);
                    }
                    totalData(illegalQueryResult);
                    if (isEr) {
                        mIllegalProcessingView.setVisible(true);
                        mIllegalProcessingCancelView.setVisible(false);
                        mBottomBarView.setBarVisibility(View.GONE, View.VISIBLE, View.GONE);
                        if (illegalQueryResultAdapter != null) {
                            illegalQueryResultAdapter.notifyState(true);
                        }
                    } else {
                        mIllegalProcessingView.setVisible(false);
                        mIllegalProcessingCancelView.setVisible(false);
                        mBottomBarView.setBarVisibility(View.VISIBLE, View.GONE, View.GONE);
                        if (illegalQueryResultAdapter != null) {
                            illegalQueryResultAdapter.notifyState(false);
                        }
                    }
                } else {
                    mNoDataView.showFailInfo(getResources().getString(R.string.query_illegal_no_data));
                    hideMenuBtn();
                }
            } else {
                mNoDataView.showFailInfo(getResources().getString(R.string.query_illegal_no_data));
                hideMenuBtn();
            }
        } else {
            mNoDataView.showFailInfo(getResources().getString(R.string.query_no_data));
            hideMenuBtn();
        }
    }

    @Override
    public void onIllegalQueryFailed(boolean offLine) {
        mNoDataView.showFailInfo(getResources().getString(R.string.query_no_data));
    }*/

    /**
     * 隐藏菜单按钮
     */
    private void hideMenuBtn() {
        mIllegalProcessingView.setVisible(false);
        mIllegalProcessingCancelView.setVisible(false);
    }

    /**
     * 统计数据
     *
     * @param provinceAndCity
     */
    private void totalData(ProvinceAndCity provinceAndCity) {
        ArrayList<IllegalQueryResultInfo> lists = provinceAndCity.lists;
        int money = 0;
        int fen = 0;
        for (int i = 0; i < lists.size(); i++) {
            if (!TextUtils.isEmpty(lists.get(i).money)) {
                money += Integer.valueOf(lists.get(i).money);
            }

            if (!TextUtils.isEmpty(lists.get(i).wfjfs)) {
                fen += Integer.valueOf(lists.get(i).wfjfs);
            }
        }
        DebugLog.i(TAG, money + "----" + fen);
        String s = "共" + lists.size() + "条" + " " + "罚款" + money + "元" + " " + "记分" + fen + "分";
        mTotalNumberView.setText(setTextStyle(s));
        mBottomBarView.setV1Text(lists.size() + "", OtherUtil.amountFormat(money, true), fen + "");
    }

    /**
     * 测试数据
     */
    private IllegalQueryRequestData testData() {
//        http://apis.haoservice.com/weizhang/query?key=f7d64e767211476f8b17a19a78285e11&city=HUB_WuHan&hphm=%E9%84%82AV85H6&hpzl=02&classno=69802
        IllegalQueryRequestData illegalQueryRequestData = new IllegalQueryRequestData();
        illegalQueryRequestData.cityCode = "HUB_WuHan";
        illegalQueryRequestData.hphm = "鄂AV85H6";
        illegalQueryRequestData.hpzl = "02";
        illegalQueryRequestData.engineno = "";
        illegalQueryRequestData.classno = "69802";
        illegalQueryRequestData.registno = "";
        return illegalQueryRequestData;
    }

    @Override
    protected void onDestroy() {
        IllegalQueryControl.getInstance().killInstance();
        if (imWzID != null) {
            imWzID = null;
        }
        if (imResults != null) {
            imResults = null;
        }
        if (imAlertDialog != null) {
            imAlertDialog.dismiss();
        }
        if (imIllegalSharedPreferencesHelper != null) {
            imIllegalSharedPreferencesHelper = null;
        }
        super.onDestroy();
    }

    /**
     * 检验是否是鄂A
     *
     * @param plateNum
     * @return
     */
    private boolean checkPlateNum(String plateNum) {
        if (plateNum.substring(0, 2).equals("鄂A")) {
            return true;
        }
        return false;
    }

    private EstimatePrice mEstimatePrice = null;

    /**
     * 违章预估金额回调
     *//*
    private IllegalQueryControl.IllegalEstimatePriceHandler illegalEstimatePriceHandler = new IllegalQueryControl.IllegalEstimatePriceHandler() {
        @Override
        public void onIllegalEstimatePriceSucceed(EstimatePrice estimatePrice) {
            if (estimatePrice != null) {
                mEstimatePrice = estimatePrice;
//                mBottomBarView.setV2Text("￥" + estimatePrice.fine, "￥" + estimatePrice.serviceCharge, estimatePrice.count + "条", "￥" + estimatePrice.amount);
                mBottomBarView.setV2Text(OtherUtil.amountFormat(estimatePrice.fine, true), OtherUtil.amountFormat(estimatePrice.serviceCharge, true), estimatePrice.count + "条", OtherUtil.amountFormat(estimatePrice.amount, true));
            }
        }

        @Override
        public void onIllegalEstimatePriceFailed(boolean offLine) {

        }
    };*/

    private AlertDialog imAlertDialog = null;//请求失败弹窗
    /**
     * 违章预估金额回调
     */
    private CallBackControl.CallBack<EstimatePrice> illegalEstimatePriceCallBack = new CallBackControl.CallBack<EstimatePrice>() {

        @Override
        public void onSuccess(EstimatePrice response) {
            mNoDataView.setVisibility(View.GONE);
            if (response != null) {
                mEstimatePrice = response;
//                mBottomBarView.setV2Text("￥" + estimatePrice.fine, "￥" + estimatePrice.serviceCharge, estimatePrice.count + "条", "￥" + estimatePrice.amount);
                mBottomBarView.setV2Text(OtherUtil.amountFormat(response.fine, true), OtherUtil.amountFormat(response.serviceCharge, true), response.count + "条", OtherUtil.amountFormat(response.amount, true));
            }
            isFirst = false;
        }

        @Override
        public void onFailed(boolean offLine) {
            if (isFirst) {
                DebugLog.i(TAG, "第一次失败");
                mNoDataView.setVisibility(View.VISIBLE);
                mNoDataView.setEmptyDescription("请求失败");
                mNoDataView.showFailedPage("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkIllegalEstimatePrice();
                    }
                });
//                mNoDataView.setEmptyButtonClick("重试", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        checkIllegalEstimatePrice();
//                    }
//                });
//                mNoDataView.setRequestViewType(RequestFailView.RequestViewType.empty);
            } else {
                DebugLog.i(TAG, "第一次失败");
                imAlertDialog = new AlertDialog.Builder(IllegalQueryResultActivity.this, R.style.MaterialDialog)
                        .setTitle("请求失败")
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reCheckIllegalEstimatePrice();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                illegalQueryResultAdapter.getIsSelected().put(imPosition, isSelected);
                                illegalQueryResultAdapter.notifyDataSetChanged();
                            }
                        })
                        .create();
                imAlertDialog.show();
            }
        }
    };

    /**
     * 请求失败弹窗的重试功能
     */
    private void reCheckIllegalEstimatePrice() {
        if (imWzID == null) {
            imWzID = new StringBuilder();
        }
        DebugLog.i(TAG, "size:" + imResults.size());
        for (int i = 0; i < imResults.size(); i++) {
            DebugLog.i(TAG, imResults.get(i).date);
            imWzID.append(imResults.get(i).wzID + ",");
        }
        if (imWzID != null) {
            DebugLog.i(TAG, "wzID:" + imWzID.toString());
            if (!TextUtils.isEmpty(imWzID.toString())) {
                substring = imWzID.substring(0, imWzID.length() - 1);
                IllegalQueryControl.getInstance().getIllegalEstimatePrice(IllegalQueryResultActivity.this, illegalEstimatePriceCallBack, plateNumber, substring, cityCode, hpzl, engineno, classno);
                imWzID = null;
            }
        }
    }

    /**
     * 违章查询结果标头文字样式
     *
     * @param strings
     * @return
     */
    private SpannableStringBuilder setTextStyle(String strings) {
        SpannableStringBuilder style = new SpannableStringBuilder(strings);
        String[] split = strings.split(" ");
        style.setSpan(new ForegroundColorSpan(Color.RED), 1, split[0].length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.RED), split[0].length() + 3, split[0].length() + 1 + split[1].length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.RED), split[0].length() + split[1].length() + 4, strings.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

}
