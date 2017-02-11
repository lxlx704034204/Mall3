package com.hxqc.mall.usedcar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.fragment.UsedCarSearchHistoryFragment;
import com.hxqc.mall.usedcar.fragment.UsedCarSearchHotKeywordsFragment;
import com.hxqc.mall.usedcar.fragment.UsedCarSearchKeywordFragment;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;


public class UsedCarSearchActivity extends NoBackActivity implements UsedCarSearchKeywordFragment.SearchKeywordCallBack,
        TextView.OnEditorActionListener, TextWatcher {

    UsedCarSearchHotKeywordsFragment mHotKeyWordFragment;
    UsedCarSearchHistoryFragment mHistoryFragment;

    EditText mSearchView;
    ImageView mCancel;

    View mDefaultLayout;//热门搜索，历史记录layout
    FrameLayout mHotKeyWordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_car_search);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


        mHotKeyWordLayout = (FrameLayout) findViewById(R.id.hot_keyword_layout);
        mCancel = (ImageView) findViewById(R.id.tv_cancel_search);
        mSearchView = (EditText) findViewById(R.id.search_edit);
        mDefaultLayout = findViewById(R.id.default_keyword_layout);

        mHotKeyWordFragment = (UsedCarSearchHotKeywordsFragment) getSupportFragmentManager().findFragmentById(
                R.id.hot_keyword_fragment);
        mHistoryFragment = (UsedCarSearchHistoryFragment) getSupportFragmentManager().findFragmentById(
                R.id.history_keyword_fragment);

        //清空 输入数据
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setText("");
            }
        });

        mSearchView.setOnEditorActionListener(this);
        mSearchView.addTextChangedListener(this);
    }


    @Override
    public void clickKeyword(Keyword keyword) {
        mHistoryFragment.saveHistoryKeyWord(keyword);
        UsedCarActivitySwitcher.toBuyCar(this, keyword.keyword);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //点击搜索
            String keyword = v.getText().toString();
            if (TextUtils.isEmpty(keyword)) return false;
            mHistoryFragment.saveHistoryKeyWord(new Keyword(keyword));
            UsedCarActivitySwitcher.toBuyCar(this, keyword);
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            mCancel.setVisibility(View.VISIBLE);
        } else {
            mCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
