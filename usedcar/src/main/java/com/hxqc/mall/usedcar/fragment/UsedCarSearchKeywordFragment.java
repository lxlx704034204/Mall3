package com.hxqc.mall.usedcar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hxqc.adapter.ObjectAdapter;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.core.views.MultipleTextView;
import com.hxqc.mall.usedcar.R;

import java.util.ArrayList;


/**
 * Author: HuJunJie
 * Date: 2015-03-30
 * FIXME
 * Todo
 */
public abstract class UsedCarSearchKeywordFragment extends Fragment implements MultipleTextView.OnMultipleTVItemClickListener {
    MultipleTextView mMultipleTextView;
    SearchKeywordCallBack mCallBack;
    ArrayList< Keyword > mKeyWordList;
    ObjectAdapter mAdapter;

    abstract void getKeywords();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBack = (SearchKeywordCallBack) activity;
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

    public void notifyData() {
        if (mAdapter == null) {
            mAdapter = new ObjectAdapter<>(getActivity(), mKeyWordList, Keyword.class,
                    R.layout.item_used_car_search_keyword, new String[]{"keyword"}, new int[]{R.id.item_keyword});
            mMultipleTextView.setAdapter(mAdapter);
            return;
        }
        mMultipleTextView.notifyData();
    }

    public interface SearchKeywordCallBack {
        /**
         * 点击关键字
         */
        void clickKeyword(Keyword keyword);
    }

}
