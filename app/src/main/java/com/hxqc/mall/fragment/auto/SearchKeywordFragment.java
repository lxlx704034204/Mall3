package com.hxqc.mall.fragment.auto;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hxqc.adapter.ObjectAdapter;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.core.views.MultipleTextView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-03-30
 * FIXME
 * Todo
 */
public abstract class SearchKeywordFragment extends Fragment implements MultipleTextView.OnMultipleTVItemClickListener {
    public interface SearchKeywordCallBack {
        /**
         * 点击关键字
         */
        void clickKeyword(Keyword keyword);
    }

    MultipleTextView mMultipleTextView;
    SearchKeywordCallBack mCallBack;
    ArrayList< Keyword > mKeyWordList;

    abstract void getKeywords();

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if( activity instanceof SearchKeywordCallBack){
            mCallBack = (SearchKeywordCallBack) activity;
        }

    }

    @Override
    public void onMultipleTVItemClick(View view, int position) {
        mCallBack.clickKeyword(mKeyWordList.get(position));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getKeywords();
    }

    ObjectAdapter mAdapter;

    public void notifyData() {
        if (mAdapter == null) {
            mAdapter = new ObjectAdapter<>(getContext(), mKeyWordList, Keyword.class,
                    R.layout.item_search_keyword, new String[]{"keyword"}, new int[]{R.id.item_keyword});
            mMultipleTextView.setAdapter(mAdapter);
            return;
        }
        mMultipleTextView.notifyData();
    }

}
