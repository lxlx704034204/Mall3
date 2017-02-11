//package com.hxqc.mall.activity.me;
//
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//
//
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.views.dialog.NormalDialog;
//import hxqc.mall.R;
//import com.hxqc.mall.activity.RecyclerViewActivity;
//import com.hxqc.mall.core.adapter.HistoryAdapter;
//import com.hxqc.mall.core.dao.AutoSQLDataHelper;
//import com.hxqc.mall.core.model.auto.AutoBaseInformation;
//import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
//import com.hxqc.mall.views.DividerForRecyclerView;
//
//import java.util.ArrayList;
//
///**
// * 说明:最近浏览
// *
// * author: 吕飞
// * since: 2015-04-13
// * Copyright:恒信汽车电子商务有限公司
// */
//@Deprecated
//public class HistoryActivity extends RecyclerViewActivity {
//    ArrayList<AutoBaseInformation> mHistories;
//    AutoSQLDataHelper mDataBaseHelper;
//    RequestFailView mRequestFailView;
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        showHistories();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mDataBaseHelper = new AutoSQLDataHelper(this);
//        showHistories();
//    }
//
//    private void showHistories() {
//        mHistories = mDataBaseHelper.findAutoHistory();
//        if (mHistories != null && mHistories.size() > 0) {
//            showList();
//        } else {
//            showBlank();
//        }
//    }
//
//    private void showBlank() {
//        setContentView(R.layout.activity_history_no_data);
//        mRequestFailView = (RequestFailView)findViewById(com.hxqc.mall.core.R.id.fail_view);
//        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivitySwitcher.toMain(HistoryActivity.this,0);
//            }
//        });
//        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
//    }
//
//
//    private void showList() {
//        mAdapter = new HistoryAdapter(mHistories, this);
//        mRecyclerView.addItemDecoration(new DividerForRecyclerView(this));
//        mRecyclerView.setAdapter(mAdapter);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_history, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.action_clear && mHistories != null && mHistories.size() > 0) {
//            new NormalDialog(this, getResources().getString(R.string.me_clean_history)) {
//                @Override
//                protected void doNext() {
//                    mDataBaseHelper.clearAutoHistory();
//                    mHistories.clear();
//                    mAdapter.notifyDataSetChanged();
//                    showBlank();
//                }
//            }.show();
//        }
//        return false;
//    }
//}
