package com.hxqc.mall.activity.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.adapter.MyCouponListAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.Coupon;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.UpLoadListener;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-02
 * FIXME
 * Todo 我的优惠券
 */
public class MyCouponActivity extends BackActivity implements MyCouponListAdapter.OnCouponSelectListener {
    public static final String COUPON_COMBINATION = "com.hxqc.mall.activity.me.coupon_combination";
    public static final String COUPONS = "com.hxqc.mall.activity.me.coupons";
    public static final String MY_AUTO_ID = "my_auto_id";
    private ArrayList<Coupon> coupons = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyCouponListAdapter adapter;
    //    private ArrayList<CouponCombination> couponCombination = null;//传过来的可用优惠券组合
    private ArrayList<Coupon> couponArrayList = new ArrayList<>();
    private boolean isChooseMode = false;
    //    private boolean isSingleChoose = true;//默认是单个选择
    //    private LinearLayout emptyLayout;
    private RequestFailView mFailView;
//    private Button doNotUseCouponBtn;

    private String myAutoID;
    private int kindCode = 10;// 适用类型 10所有 20保养 默认10
    private int couponType = 10;//种类 10通用卷 默认10
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
//        couponCombination = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(COUPON_COMBINATION);

        couponArrayList = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(COUPONS);
//        isChooseMode = couponCombination != null;
        isChooseMode = couponArrayList != null;
        DebugLog.i("chooseMode", "isChooseMode:" + isChooseMode);
//        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
//        doNotUseCouponBtn = (Button) findViewById(R.id.do_not_use_coupon_btn);
//        doNotUseCouponBtn.setVisibility(isChooseMode ? View.VISIBLE : View.GONE);
//        DebugLog.i("chooseMode", "doNotUseCouponBtn:" + (doNotUseCouponBtn.getVisibility() == View.VISIBLE));
        initFailView();
        initRecyclerView();
        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null)
            myAutoID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(MY_AUTO_ID);
        else myAutoID = "";
        if (!isChooseMode)
            loaData(true);
        else {
            coupons.addAll(couponArrayList);
            if (coupons.size() <= 0) {
                mFailView.setVisibility(View.VISIBLE);
                mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
            } else {
                mFailView.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void initFailView() {
        mFailView = (RequestFailView) findViewById(R.id.fail_view);
        mFailView.setEmptyDescription("您还没有优惠券", R.drawable.ic_coupon_empty);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_coupon_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new MyCouponListAdapter(coupons, this);
//        if (isChooseMode)
//            adapter.setCouponCombinations(couponCombination);
        adapter.setCanChoose(isChooseMode);
        adapter.setOnCouponSelectListener(this);
        mRecyclerView.setAdapter(adapter);
        lastVisibleItem = 0;
        mRecyclerView.addOnScrollListener(new UpLoadListener(mLayoutManager) {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
    }

    int page = 1;

    private void loaData(final boolean isRefrash) {
        new UserApiClient().getCouponData(page, 15, myAutoID, 0, couponType,
                new LoadingAnimResponseHandler(this, isRefrash) {
                    @Override
                    public void onSuccess(String response) {
                        ArrayList<Coupon> c = JSONUtils.fromJson(response, new TypeToken<ArrayList<Coupon>>() {
                        });
                        if (c != null) {
                            if (isRefrash) {
                                coupons.clear();
                                page = 1;
                            }
                            coupons.addAll(c);
//                            if (isChooseMode) {
//                                filterCoupon();
//                            }
                            if (coupons.size() <= 0) {
//                        emptyLayout.setVisibility(View.VISIBLE);
                                mFailView.setVisibility(View.VISIBLE);
                                mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                            } else {
//                        emptyLayout.setVisibility(View.GONE);
                                mFailView.setVisibility(View.GONE);
                            }

                            adapter.notifyDataSetChanged();
                        } else {
//                    emptyLayout.setVisibility(View.VISIBLE);
                            if (isRefrash) {
                                mFailView.setVisibility(View.VISIBLE);
                                mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                            } else {
                                ToastHelper.showYellowToast(MyCouponActivity.this, "暂无更多优惠券");
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        mFailView.setVisibility(View.VISIBLE);
                        mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    }
                });
    }


    /**
     * 完成数据
     */
    private void completeAndBack() {
        Intent data = new Intent();
        //添加数据
//        ArrayList<Coupon> selectCoupons = adapter.selectCoupons;
        Coupon coupon = null;
        for (Coupon c : coupons) {
            if (c.isChoose == 1)
                coupon = c;
        }
//        float total = 0;
//        ArrayList<String> selectCouponIDs = new ArrayList<>();
//        for (int j = 0; j < selectCoupons.size(); j++) {
//            selectCouponIDs.add(selectCoupons.get(j).couponID);
//            total += Float.valueOf(selectCoupons.get(j).price);
//        }
//        CouponCombination couponCombination = new CouponCombination();
//        couponCombination.discountAmount = total;
//        couponCombination.couponIDs = selectCouponIDs;
//        if (selectCoupons.size() > 0)
//            data.putExtra("coupon", selectCoupons.get(0));
        if (coupon != null)
            DebugLog.i("selectCoupons", coupon.couponID);
        data.putExtra("coupon", coupon);
        setResult(RESULT_OK, data);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 300);
    }

    @Override
    public void onSelect(int position) {
        if (isChooseMode)
            completeAndBack();
    }

    public void doNotUseCoupon() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }

    //分页
    private void loadMore() {
        if (!isChooseMode) {
            page += 1;
            loaData(false);
        }
    }


}
