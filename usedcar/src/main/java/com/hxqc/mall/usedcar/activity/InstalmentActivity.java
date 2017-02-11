package com.hxqc.mall.usedcar.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.InstalmentAdapter;
import com.hxqc.mall.usedcar.model.Instalment;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * 分期购车
 * Created by huangyi on 16/6/18.
 */
public class InstalmentActivity extends BackActivity implements AdapterView.OnItemClickListener {

    public static final String INSTALMENT = "instalment";
    public static final String INSTALMENT_ID = "instalment_id";
    public static final String URL = "url";
    public static final int RESULT_CODE = 1;

    ListViewNoSlide mListView;
    WebView mWebView;

    ArrayList<Instalment> mInstalment;
    InstalmentAdapter mAdapter;
    int mLastPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalment);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.toolbar_background)));
        mListView = (ListViewNoSlide) findViewById(R.id.instalment_list);
        mWebView = (WebView) findViewById(R.id.instalment_web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(URL));

        mInstalment = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(INSTALMENT);
        String id = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(INSTALMENT_ID);
        for (int i = 0; i < mInstalment.size(); i++) {
            Instalment instalment = mInstalment.get(i);
            if (instalment.item_id.equals(id)) {
                instalment.isChecked = true;
                mLastPosition = i;
                break;
            }
        }
        mAdapter = new InstalmentAdapter(this, mInstalment);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (Instalment i : mInstalment) {
            i.isChecked = false;
        }
        if (mLastPosition == position) {
            mLastPosition = -1;
        } else {
            mInstalment.get(position).isChecked = true;
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
                bundle.putString(INSTALMENT_ID, "");
            } else {
                bundle.putString(INSTALMENT_ID, mInstalment.get(mLastPosition).item_id);
            }
            setResult(RESULT_CODE, new Intent().putExtra(ActivitySwitchBase.KEY_DATA, bundle));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
