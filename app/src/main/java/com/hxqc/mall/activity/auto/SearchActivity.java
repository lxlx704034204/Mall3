package com.hxqc.mall.activity.auto;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.AppNoBackActivity;
import com.hxqc.mall.control.SearchProvider;
import com.hxqc.mall.core.model.HintMode;
import com.hxqc.mall.core.model.HotKeyword;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.fragment.auto.SearchHintFragment_2;
import com.hxqc.mall.fragment.auto.SearchHistoryFragment;
import com.hxqc.mall.fragment.auto.SearchHotKeywordsFragment;
import com.hxqc.mall.fragment.auto.SearchKeywordFragment;
import com.hxqc.mall.interfaces.EditChanged;

import hxqc.mall.R;

public class SearchActivity extends AppNoBackActivity implements SearchKeywordFragment.SearchKeywordCallBack,
        TextView.OnEditorActionListener,
        SearchHintFragment_2.SearchHintListener,
        ExpandableListView.OnChildClickListener {
    private static final String HintKey = "Hint";
    private static final int HintMessage = 1;
    private static final int DelayedSearchTime = 500;//延迟提示时间

    SearchHotKeywordsFragment mHotKeyWordFragment;
    SearchHistoryFragment mHistoryFragment;
    SearchHintFragment_2 mHintFragment;

    EditText mSearchView;
    TextView mCancel;

    View mDefaultLayout;//热门搜索，历史记录layout
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HintMessage) {
                String keyValue = msg.getData().getString(HintKey);
                if (!TextUtils.isEmpty(keyValue)) {
                    mHintFragment.findHint(keyValue);
                } else {
                    showHintFragment(false);
                }
            }
            return false;
        }
    });
    FrameLayout mHotKeyWordLayout;
    FrameLayout mHintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        mHintLayout = (FrameLayout) findViewById(R.id.hint_layout);
        mHotKeyWordLayout = (FrameLayout) findViewById(R.id.hot_keyword_layout);
        mCancel = (TextView) findViewById(R.id.tv_cancel_search);
        mSearchView = (EditText) findViewById(R.id.search_edit);
        mDefaultLayout = findViewById(R.id.default_keyword_layout);

        mHotKeyWordFragment = (SearchHotKeywordsFragment) getSupportFragmentManager().findFragmentById(
                R.id.hot_keyword_fragment);
        mHistoryFragment = (SearchHistoryFragment) getSupportFragmentManager().findFragmentById(
                R.id.history_keyword_fragment);
        mHintFragment = (SearchHintFragment_2) getSupportFragmentManager().findFragmentById(
                R.id.hint_keyword_fragment);

        mSearchView.addTextChangedListener(new EditChanged() {
            @Override
            public void onEditChange(CharSequence s, int start, int before, int count) {

                //根据是否输入字符 显示取消按钮
                if (count > 0) {
                    mCancel.setVisibility(View.VISIBLE);
                } else {
                    mCancel.setVisibility(View.GONE);
                }

                //输入监听
                toHintMessage(s.toString());
            }
        });

        //清空 输入数据
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setText("");
            }
        });

        mSearchView.setOnEditorActionListener(this);
        SearchProvider.getInstance().setOnRequestHotKeywordListener(
                new SearchProvider.RequestHotKeywordListener() {
                    @Override
                    public void onHotKeyword(HotKeyword hotKeyword) {
                        mHotKeyWordLayout.setVisibility(View.VISIBLE);
                        mSearchView.setHint(hotKeyword.getRecommand());
                    }
                }).requestHotKeyword();
        mSearchView.performClick();
    }

    /**
     * 延迟搜索
     *
     * @param hintString
     */
    void toHintMessage(String hintString) {
        handler.removeMessages(HintMessage);
        Message message = new Message();
        message.what = HintMessage;
        message.getData().putString(HintKey, hintString);
        handler.sendMessageDelayed(message, DelayedSearchTime);
    }


    @Override
    public void clickKeyword(Keyword keyword) {
        mHistoryFragment.saveHistoryKeyWord(keyword);
        ActivitySwitcher.toAutoList(this, keyword.keyword);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //点击搜索
            handler.removeMessages(HintMessage);
            String keyword = v.getText().toString();
            if (TextUtils.isEmpty(keyword)) {
                if (v.getHint().toString().equals(getResources().getString(R.string.search_hint))) {
                    return false;
                } else {
                    keyword = v.getHint().toString();
                }
            }
            mHistoryFragment.saveHistoryKeyWord(new Keyword(keyword));
            ActivitySwitcher.toAutoList(this, keyword);
        }
        return false;
    }


    /**
     * 显示搜索提示
     *
     * @param showHintFragment
     *         true 显示提示  false 隐藏提示
     */
    @Override
    public void showHintFragment(boolean showHintFragment) {
        if (showHintFragment) {
            mHintLayout.setVisibility(View.VISIBLE);
            mDefaultLayout.setVisibility(View.GONE);
        } else {
            mHintLayout.setVisibility(View.GONE);
            mDefaultLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id) {
        String keyValue = ((HintMode.Suggestion) v.getTag()).suggestion;
        Keyword searchKeyword = new Keyword(keyValue);
        mHistoryFragment.saveHistoryKeyWord(searchKeyword);
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (mHintLayout.isShown()) {
            showHintFragment(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SearchProvider.getInstance().onDestroy();
    }
}
