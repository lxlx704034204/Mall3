package com.hxqc.mall.activity.order;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.model.loan.LoanDepartmentType;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.stickylist.StickyListHeadersListView;
import com.hxqc.mall.views.loan.LoanAdapter;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import hxqc.mall.R;

public class LoanBankChooseActivity extends BackActivity implements
        AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    final public static int LOAN_RESULT_CODE = 888;//返回结果码
    final public static String LOAN_RESULT_TAG = "loan_file";//贷款机构数据返回tag

    ArrayList< LoanDepartmentType > financeModels;
    private StickyListHeadersListView stickyListView;
    LoanAdapter adapter;
    RequestFailView mRequestFailView;

    String financeID = "";
    private NewAutoClient newAutoClient;
    private boolean fadeHeader = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_bank_choose);
        financeID = getIntent().getStringExtra(ActivitySwitchBase.KEY_DATA);
        stickyListView = (StickyListHeadersListView) findViewById(R.id.loan_list);
        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);
        newAutoClient = new NewAutoClient();
        requestData();
    }

    /**
     * 获取经融机构信息
     */
    private void requestData() {
        newAutoClient.financeList(new LoadingAnimResponseHandler(LoanBankChooseActivity.this) {
            @Override
            public void onSuccess(String response) {

                if (TextUtils.isEmpty(response)) {
                    ToastHelper.showRedToast(LoanBankChooseActivity.this, "获取金融列表失败");
                } else {
                    financeModels = JSONUtils.fromJson(response, new TypeToken< ArrayList< LoanDepartmentType > >() {
                    });
                    if (financeModels == null) {
                        onFail();
                        ToastHelper.showRedToast(LoanBankChooseActivity.this, "获取金融列表失败");
                    } else {
                        initData();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onFail();
            }
        });
    }

    private void onFail() {
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    public void initData() {
        adapter = new LoanAdapter(LoanBankChooseActivity.this, financeModels, financeID);
        stickyListView.setOnItemClickListener(this);
        stickyListView.setOnHeaderClickListener(this);
        stickyListView.setOnStickyHeaderChangedListener(this);
        stickyListView.setOnStickyHeaderOffsetChangedListener(this);
        stickyListView.addHeaderView(getLayoutInflater().inflate(R.layout.view_loan_header_tips, null));
//        stickyList.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
//        stickyList.setEmptyView(findViewById(R.id.empty));
        stickyListView.setDrawingListUnderStickyHeader(true);
        stickyListView.setAreHeadersSticky(true);
        stickyListView.setAdapter(adapter);
        stickyListView.setStickyHeaderTopOffset(-20);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_choose_package, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_confirm) {
            finish();
        }
        return false;
    }

    /**
     * 返回数据
     */
    @Override
    public void finish() {
        if (adapter != null) {
            if (adapter.getCheckedModel() != null) {
                setResult(LOAN_RESULT_CODE, new Intent().putExtra(LOAN_RESULT_TAG, adapter.getCheckedModel()));
            } else {
                setResult(LOAN_RESULT_CODE);
            }
        } else {
            setResult(LOAN_RESULT_CODE);
        }
        super.finish();
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {

    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(0.9f);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (fadeHeader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(0.9f);
//            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

}
