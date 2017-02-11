package com.hxqc.mall.activity.me.bill;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import hxqc.mall.R;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.control.BillDataManager;
import com.hxqc.mall.core.adapter.me.MyBalanceBillListAdapter;
import com.hxqc.mall.core.adapter.me.MyBillListAdapter;
import com.hxqc.mall.core.adapter.me.MyScoreBillListAdapter;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.model.bill.BalanceBill;
import com.hxqc.mall.core.model.bill.BalanceBillList;
import com.hxqc.mall.core.model.bill.BalanceBillMatter;
import com.hxqc.mall.core.model.bill.ScoreBill;
import com.hxqc.mall.core.model.bill.ScoreBillList;
import com.hxqc.mall.core.model.bill.ScoreBillMatter;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.recyclerview.MFooter;
import com.hxqc.mall.core.views.recyclerview.VRecyclerView;
import com.hxqc.util.DebugLog;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo 我的账单列表（余额和积分两种）
 */
public class MyBillListActivity extends BackActivity implements OnRefreshHandler {
    public static final String TYPE = "com.hxqc.mall.activity.me.MyBillListActivity_data_type";
    private boolean isBalance = true;//是否是余额账单
    private VRecyclerView recyclerView;
    //    private Object myBill;
    private MyBillListAdapter adapter;
    private Context context;

    private PtrFrameLayout mPtrFrameLayoutView;
    private String month = "";//当前月
    private int page = 1;//默认第一页
    ArrayList<ScoreBillList> scoreBillLists = new ArrayList<>();
    ArrayList<BalanceBillList> balanceBillLists = new ArrayList<>();
    BalanceBill balanceBill = new BalanceBill();
    ScoreBill scoreBill = new ScoreBill();
    private LinearLayoutManager layoutManager;
    private UltraPullRefreshHeaderHelper mPtrHelper;
    private String myAutoID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill_list_1);
        context = this;
        initData();
        initView();
        initEvent();
        loadData(true, true);
    }

    private void initData() {
        isBalance = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(TYPE, true);
//        scoreBillLists = new ArrayList<>();
//        scoreBill = new ScoreBill();
        scoreBill.billList = scoreBillLists;
//        balanceBillLists = new ArrayList<>();
//        balanceBill = new BalanceBill();
        balanceBill.billList = balanceBillLists;
    }


    private void initView() {
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView, this);
//        mPtrHelper.setOnRefreshHandler(this);

        recyclerView = (VRecyclerView) findViewById(R.id.my_bill_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
        if (isBalance) {
            getSupportActionBar().setTitle(getString(R.string.comsumer_detail));
            adapter = new MyBalanceBillListAdapter(new MFooter(this), balanceBill, balanceBillLists, context);
        } else {
            getSupportActionBar().setTitle("积分明细");
            adapter = new MyScoreBillListAdapter(new MFooter(this), scoreBill, scoreBillLists, context);
            myAutoID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("myAutoID");
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
    }

    private void initEvent() {
        recyclerView.addVRecyclerViewLoader(new VRecyclerView.VRecyclerViewLoader() {
            @Override
            public void loadMore() {
                MyBillListActivity.this.loadMore();
            }
        });
    }


    /**
     * 上拉加载
     */
    private void loadMore() {
        if (isBalance)
            month = balanceBill.end_month;
        else month = scoreBill.end_month;
        page += 1;
        loadData(false, false);
        DebugLog.e("onScrolled", "done");
    }

    private void loadData(boolean refresh, boolean showAnim) {
        if (isBalance) loadBalanceData(refresh, showAnim);
        else loadScoreData(myAutoID, refresh, showAnim);
    }

    //积分请求
    private void loadScoreData(String myAutoID, final boolean refresh, boolean showAnim) {
        BillDataManager.getInstance(this).getScoreBillList(myAutoID, page + "", month, showAnim,
                new LoadDataCallBack<ScoreBill>() {
                    @Override
                    public void onDataNull(String message) {
                        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
                        recyclerView.loadComplete();
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDataGot(ScoreBill bill) {
                        recyclerView.loadComplete();
                        recyclerView.setVisibility(View.VISIBLE);
                        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
                        long billCount = bill.billCount;/*总条数*/
                        scoreBill = bill;
                        if (refresh)
                            scoreBillLists.clear();
                        ArrayList<ScoreBillList> scoreBills = new ArrayList<>();
                        scoreBills.addAll(scoreBillLists);
                        scoreBills.addAll(bill.billList);
                        DebugLog.i("getScoreLength", getScoreLength(scoreBills) + "||" + billCount);
                        if (getScoreLength(scoreBills) <= billCount) {
                            if (getScoreLength(scoreBills) == 0)
                                scoreBills.get(0).matter = new ArrayList<>();
//                    DebugLog.i("getScoreLength", getScoreLength(scoreBills) + "||" + billCount);
                            scoreBillLists.addAll(bill.billList);
                            ((MyScoreBillListAdapter) adapter).notifyData(bill, scoreBills);
                        }
                        if (getScoreLength(scoreBillLists) >= billCount)
                            recyclerView.loadAllDataOk();
//                    hasMore = false;
//                    mPtrHelper.setHasMore(false);
                    }
                });
    }


    //余额请求
    private void loadBalanceData(final boolean refresh, boolean showAnim) {
        BillDataManager.getInstance(this).getBalaceBillList(page + "", month, showAnim,
                new LoadDataCallBack<BalanceBill>() {
                    @Override
                    public void onDataNull(String message) {
                        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
                        recyclerView.loadComplete();
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDataGot(BalanceBill bill) {
                        recyclerView.loadComplete();
                        recyclerView.setVisibility(View.VISIBLE);
                        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
                        long billCount = bill.billCount;/*总条数*/
                        balanceBill = bill;
                        if (refresh)
                            balanceBillLists.clear();
                        ArrayList<BalanceBillList> billLists = new ArrayList<>();
                        billLists.addAll(balanceBillLists);
                        billLists.addAll(bill.billList);
                        if (getBalanceLength(billLists) <= billCount) {
                            if (getBalanceLength(billLists) == 0)
                                billLists.get(0).matter = new ArrayList<>();
                            balanceBillLists.addAll(bill.billList);
                            ((MyBalanceBillListAdapter) adapter).notifyData(bill, billLists);
                        }
                        if (getBalanceLength(balanceBillLists) >= billCount)
                            recyclerView.loadAllDataOk();
//                    hasMore = false;
//                    mPtrHelper.setHasMore(false);
                    }
                });
    }

    private long getScoreLength(ArrayList<ScoreBillList> scoreBillLists) {
        long length = 0;
        if (scoreBillLists == null)
            return length;
        for (int i = 0; i < scoreBillLists.size(); i++) {
            ArrayList<ScoreBillMatter> matter = scoreBillLists.get(i).matter;
            if (matter != null) {
                if (matter.size() == 1)
                    if (matter.get(0).isNull())
                        continue;
                length += matter.size();
            }
        }
        return length;
    }

    private long getBalanceLength(ArrayList<BalanceBillList> scoreBillLists) {
        long length = 0;
        if (scoreBillLists == null)
            return length;
        for (int i = 0; i < scoreBillLists.size(); i++) {
            ArrayList<BalanceBillMatter> matter = scoreBillLists.get(i).matter;
            if (matter != null) {
                if (matter.size() == 1)
                    if (matter.get(0).isNull())
                        continue;
                length += matter.size();
            }
        }
        return length;
    }


    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }

    @Override
    public void onRefresh() {
        month = "";
        page = 1;
        recyclerView.refreshDataState();
        loadData(true, false);
    }

    @Override
    public void onLoadMore() {
//      loadMore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BillDataManager.getInstance(this).destory();
    }
}
