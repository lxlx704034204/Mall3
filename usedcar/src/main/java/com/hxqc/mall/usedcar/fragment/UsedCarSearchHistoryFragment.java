package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.core.views.MultipleTextView;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;


/**
 * Author: HuJunJie
 * Date: 2015-03-30
 * FIXME
 * Todo
 */
public class UsedCarSearchHistoryFragment extends UsedCarSearchKeywordFragment implements
        MultipleTextView.OnMultipleTVItemClickListener, View.OnClickListener {

    protected UsedCarSPHelper mSharedPreferencesHelper;
    TextView mClearHistoryView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_used_car_search_history_keyword, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSharedPreferencesHelper = new UsedCarSPHelper(getActivity());
        mMultipleTextView = (MultipleTextView) view.findViewById(R.id.search_hot_keywords_multiple_text);
        mMultipleTextView.setOnMultipleTVItemClickListener(this);
        mClearHistoryView = (TextView) view.findViewById(R.id.clear_search_history);
        mClearHistoryView.setOnClickListener(this);

    }


    @Override
    void getKeywords() {
        if (mKeyWordList == null) {
            String historyKeyWord = mSharedPreferencesHelper.getSearchHistory();
            mKeyWordList = JSONUtils.fromJson(historyKeyWord, new TypeToken<ArrayList< Keyword >>() {
            });
        }
        notifyData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear_search_history) {
            clearHistoryKeyWord();
        }
    }

    private void clearHistoryKeyWord() {
        mSharedPreferencesHelper.clearSearchHistoryKeyWord();
        mKeyWordList.clear();
        notifyData();
    }

    public void saveHistoryKeyWord(Keyword keyWord) {
        for (Keyword s : mKeyWordList) {
            if (s.equals(keyWord)) {
                return;
            }
        }
        mKeyWordList.add(0, keyWord);
        if (mKeyWordList.size() > 5) mKeyWordList.remove(5);
        mSharedPreferencesHelper.saveSearchHistory(JSONUtils.toJson(mKeyWordList));
        notifyData();
    }
}
