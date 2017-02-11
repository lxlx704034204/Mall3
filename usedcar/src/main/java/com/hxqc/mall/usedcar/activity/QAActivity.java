package com.hxqc.mall.usedcar.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.QAAdapter;
import com.hxqc.mall.usedcar.model.QA;
import com.hxqc.mall.usedcar.model.Table;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * 恒信质保
 * Created by huangyi on 16/6/18.
 */
public class QAActivity extends BackActivity implements QAAdapter.OnItemClickListener {

    public static final String QA = "qa";
    public static final String TABLE = "table";
    public static final String QA_ID = "qa_id";
    public static final String URL = "url";
    public static final int RESULT_CODE = 2;

    ListViewNoSlide mListView;
    WebView mWebView;

    ArrayList<QA> mQA;
    ArrayList<Table> mTable;
    QAAdapter mAdapter;
    int mLastPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.toolbar_background)));
        ((ScrollView) findViewById(R.id.qa_scroll)).smoothScrollTo(0, 0);
        mListView = (ListViewNoSlide) findViewById(R.id.qa_list);
        mWebView = (WebView) findViewById(R.id.qa_web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(URL));
        findViewById(R.id.qa_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsedCarActivitySwitcher.toQAStandard(QAActivity.this, mTable, mQA);
            }
        });

        mQA = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(QA);
        mTable = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(TABLE);
        String id = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(QA_ID);
        for (int i = 0; i < mQA.size(); i++) {
            QA qa = mQA.get(i);
            if (qa.item_id.equals(id)) {
                qa.isChecked = true;
                mLastPosition = i;
                break;
            }
        }
        mAdapter = new QAAdapter(this, this, mQA);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(int position) {
        for (QA q : mQA) {
            q.isChecked = false;
        }
        if (mLastPosition == position) {
            mLastPosition = -1;
        } else {
            mQA.get(position).isChecked = true;
            mLastPosition = position;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ok) {
            Bundle bundle = new Bundle();
            if (mLastPosition == -1) { //一个都没有选
                bundle.putString(QA_ID, "");
            } else {
                bundle.putString(QA_ID, mQA.get(mLastPosition).item_id);
            }
            setResult(RESULT_CODE, new Intent().putExtra(ActivitySwitchBase.KEY_DATA, bundle));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
