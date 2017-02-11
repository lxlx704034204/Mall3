package com.hxqc.mall.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.me.ComplaintsActivity;
import com.hxqc.mall.core.adapter.CommonAdapter;
import com.hxqc.mall.core.adapter.ViewHolder;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.thirdshop.model.AreaCategory;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-24
 * FIXME
 * Todo 选择投诉的站点
 */

public class FilterSiteListFragment extends FunctionFragment {
    private ListView list;
    private RequestFailView requestFailView;
    private List<AreaCategory> mDatas = new ArrayList<>();
    private BaseSharedPreferencesHelper sharedPreferencesHelper;

    public static FilterSiteListFragment newInstance() {
        Bundle args = new Bundle();
        FilterSiteListFragment fragment = new FilterSiteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback_options, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        sharedPreferencesHelper = new BaseSharedPreferencesHelper(getContext());
        loadData();
    }

    private void loadData() {
        ArrayList<AreaCategory> areaCategories = JSONUtils.fromJson(sharedPreferencesHelper.getSpecialCarHistoryData(), new TypeToken<ArrayList<AreaCategory>>() {
        });
        if (areaCategories == null || areaCategories.isEmpty()) {
            requestFailView.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            mDatas.clear();
            AreaCategory areaCategory = new AreaCategory();
            areaCategory.areaName = "不限";
            areaCategory.areaGroup = new ArrayList<>();
            areaCategory.pinyin = "buxiam";
            areaCategory.siteID = "";
            mDatas.add(areaCategory);//添加不限
            mDatas.addAll(areaCategories);
            ((CommonAdapter) list.getAdapter()).notifyDataSetChanged();
            requestFailView.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String fragmentDescription() {
        return "feedbackOption";
    }

    private void initView(View view) {
        list = (ListView) view.findViewById(R.id.list);
        requestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选
        list.setAdapter(new CommonAdapter<AreaCategory>(getContext(), mDatas, R.layout.item_feedback_options_2) {
            @Override
            public void convert(ViewHolder helper, AreaCategory item, int position) {
                helper.setText(R.id.feedback_option_name, item.areaName);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() instanceof ComplaintsActivity)
                    ((ComplaintsActivity) getActivity()).onSiteSelected(mDatas.get(position));
                list.setSelection(position);
            }
        });
    }
}
