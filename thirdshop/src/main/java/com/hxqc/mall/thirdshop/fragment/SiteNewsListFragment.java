package com.hxqc.mall.thirdshop.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.adapter.BaseRecyclerAdapter;
import com.hxqc.mall.core.adapter.RecyclerViewHolder;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.FourSNews;
import com.hxqc.mall.thirdshop.views.FourSNewsItem;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-13
 * FIXME
 * Todo 4s店铺资讯列表
 */
public class SiteNewsListFragment extends SwipeRefreshForRecyclerFragment {
    public static final String SITE_ID = "site_id";
    ThirdPartShopClient client;
    private String siteID;
    private ArrayList<FourSNews> fourSNewses = new ArrayList<>();
    private BaseRecyclerAdapter<FourSNews> adapter;
    private int count = 15;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        client = new ThirdPartShopClient();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        siteID = getArguments().getString(SITE_ID);
        adapter = new BaseRecyclerAdapter<FourSNews>(getActivity(), fourSNewses) {
            @Override
            protected void bindData(RecyclerViewHolder holder, int position, FourSNews fourSNews) {
                FourSNewsItem item = (FourSNewsItem) holder.getView(R.id.news_item);
                item.addData(fourSNews);
            }

            @Override
            protected int getItemLayout(int viewType) {
                return R.layout.item_four_s_news;
            }
        };
    }

    public static SiteNewsListFragment newInstance(String siteID) {
        Bundle args = new Bundle();
        args.putString(SITE_ID, siteID);
        SiteNewsListFragment fragment = new SiteNewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        client.getSiteNews(siteID, mPage, getLoadingAnimResponseHandler(true));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<FourSNews> fourSNewses = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSNews>>() {
        });
        if (fourSNewses != null) {
            if (fourSNewses.size() < count)
                mPtrHelper.setHasMore(false);
            else mPtrHelper.setHasMore(true);

            if (mPage == DEFAULT_PATE)
                this.fourSNewses.clear();
            this.fourSNewses.addAll(fourSNewses);
            adapter.notifyDataSetChanged();
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
        } else {
            mPtrHelper.setHasMore(false);
        }
        if (this.fourSNewses.size() == 0) {
            mPtrFrameLayoutView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }

    }

    @Override
    protected String getEmptyDescription() {
        return "资讯列表为空";
    }

    @Override
    public String fragmentDescription() {
        return "资讯列表";
    }
}
