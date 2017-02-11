package com.hxqc.mall.fragment.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.control.SearchProvider;
import com.hxqc.mall.core.model.HotKeyword;
import com.hxqc.mall.core.views.MultipleTextView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie Date: 2015-03-30 FIXME Todo 热门搜索关键字
 */
public class SearchHotKeywordsFragment extends SearchKeywordFragment implements MultipleTextView.OnMultipleTVItemClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_hotkeyword, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMultipleTextView = (MultipleTextView) view.findViewById(
                R.id.search_hot_keywords_multiple_text);
        mMultipleTextView.setOnMultipleTVItemClickListener(this);

    }


    @Override
    void getKeywords() {
        SearchProvider.getInstance().setOnRequestHotKeywordListener(
                new SearchProvider.RequestHotKeywordListener() {
                    @Override
                    public void onHotKeyword(HotKeyword hotKeyword) {
                        mKeyWordList = hotKeyword.hotKeywords;
                        if (mKeyWordList == null) {
                            mKeyWordList = new ArrayList<>();
                        }
                        notifyData();
                    }

                }).requestHotKeyword();
    }

}
