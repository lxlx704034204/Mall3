package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.core.views.MultipleTextView;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author: HuJunJie Date: 2015-03-30 FIXME Todo 热门搜索关键字
 */
public class UsedCarSearchHotKeywordsFragment extends UsedCarSearchKeywordFragment implements MultipleTextView.OnMultipleTVItemClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_used_car_search_hotkeyword, container, false);
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
        mKeyWordList = new UsedCarSPHelper(getActivity()).getHotKeywords();
        if (mKeyWordList != null && mKeyWordList.size() > 0) {
            notifyData();
        } else {
            new UsedCarApiClient().getHotSearch(new LoadingAnimResponseHandler(getActivity()) {
                @Override
                public void onSuccess(String response) {
                    mKeyWordList = JSONUtils.fromJson(response, new TypeToken<ArrayList<Keyword>>() {
                    });
                    if (mKeyWordList != null && mKeyWordList.size() > 0) {
                        notifyData();
                        new UsedCarSPHelper(getActivity()).saveHotKeywords(response);
                    }
                }
            });
        }
    }
}
