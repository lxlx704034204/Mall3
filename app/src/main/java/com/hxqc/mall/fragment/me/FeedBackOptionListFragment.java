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
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.FeedBackOption;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-24
 * FIXME
 * Todo 选择投诉的类型
 */

public class FeedBackOptionListFragment extends FunctionFragment {
    private android.widget.ListView list;
    private com.hxqc.mall.core.api.RequestFailView requestFailView;
    private List<FeedBackOption> mDatas = new ArrayList<>();

    public static FeedBackOptionListFragment newInstance() {
        Bundle args = new Bundle();
        FeedBackOptionListFragment fragment = new FeedBackOptionListFragment();
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
        loadData();
    }

    private void loadData() {
        new UserApiClient().feedbackOption(new LoadingAnimResponseHandler(getContext(), false) {
            @Override
            public void onSuccess(String response) {
                final ArrayList<FeedBackOption> feedBackOptions = JSONUtils.fromJson(response, new TypeToken<ArrayList<FeedBackOption>>() {
                });
                mDatas.clear();
                mDatas.addAll(feedBackOptions);
                mDatas.add(new FeedBackOption("0", "其他"));
                ((CommonAdapter) list.getAdapter()).notifyDataSetChanged();
                requestFailView.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                requestFailView.setVisibility(View.VISIBLE);
//                list.setVisibility(View.GONE);
                mDatas.clear();
                mDatas.add(new FeedBackOption("0", "其他"));
                ((CommonAdapter) list.getAdapter()).notifyDataSetChanged();
                requestFailView.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public String fragmentDescription() {
        return "feedbackOption";
    }

    private void initView(View view) {
        list = (ListView) view.findViewById(R.id.list);
        requestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选
        list.setAdapter(new CommonAdapter<FeedBackOption>(getContext(), mDatas, R.layout.item_feedback_options_2) {
            @Override
            public void convert(ViewHolder helper, FeedBackOption item, int position) {
                helper.setText(R.id.feedback_option_name, item.adviceTitle);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() instanceof ComplaintsActivity)
                    ((ComplaintsActivity) getActivity()).onfeedbackOptionSelected(mDatas.get(position));
                list.setSelection(position);
            }
        });
    }
}
