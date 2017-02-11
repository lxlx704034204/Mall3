package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.adapter.BaseRecyclerAdapter;
import com.hxqc.mall.core.adapter.RecyclerViewHolder;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.pullrefreshhandler.FixRequestDisallowTouchEventPtrFrameLayout;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.FourSNews;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.views.FourSRecommendItem;

import java.util.ArrayList;


/**
 * 说明:4s店推荐
 *
 * @author: 吕飞
 * @since: 2016-11-21
 * Copyright:恒信汽车电子商务有限公司
 */

public class FourSRecommendFragment extends FunctionFragment implements OnRefreshHandler {
    protected static final int DEFAULT_PATE = 1;
    protected final int PER_PAGE = 20;
    protected int mPage = DEFAULT_PATE;//当前页

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected FixRequestDisallowTouchEventPtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    protected RequestFailView mRequestFailView;
    ThirdPartShopClient mThirdPartShopClient;
    ArrayList<FourSNews> mFourSNewses = new ArrayList<>();
    AreaSiteUtil mAreaSiteUtil;
    RefreshRecommendListener mRefreshRecommendListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_4s_recommend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mThirdPartShopClient = new ThirdPartShopClient();
        mAreaSiteUtil = new AreaSiteUtil(getActivity());
        mPtrFrameLayoutView = (FixRequestDisallowTouchEventPtrFrameLayout) view.findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView, this);
        mPtrHelper.setOnRefreshHandler(this);

        mRequestFailView = (RequestFailView) view.findViewById(R.id.fail_view);
        mRequestFailView.setEmptyDescription("暂无数据");
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshRecommendListener.refreshRecommend();
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new BaseRecyclerAdapter<FourSNews>(getActivity(), mFourSNewses) {
            @Override
            protected void bindData(RecyclerViewHolder holder, int position, FourSNews fourSNews) {
                FourSRecommendItem item = (FourSRecommendItem) holder.getView(R.id.recommend_item);
                item.addData(fourSNews);
            }

            @Override
            protected int getItemLayout(int viewType) {
                return R.layout.item_4s_recommend;
            }
        };
    }

    public void showFailData() {
        mPtrFrameLayoutView.setVisibility(View.GONE);
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestFailView.setVisibility(View.GONE);
                mRefreshRecommendListener.refreshRecommend();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    @Override
    public String fragmentDescription() {
        return "4s店推荐";
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    public FixRequestDisallowTouchEventPtrFrameLayout getPtrFrameLayoutView() {
        return mPtrFrameLayoutView;
    }

    @Override
    public void onRefresh() {
//        mPage = DEFAULT_PATE;
//        refreshData(false);
    }

    @Override
    public void onLoadMore() {
//        mPage++;
//        refreshData(true);
    }

    protected void showNoData() {
        if (mPage == DEFAULT_PATE) {
            mPtrFrameLayoutView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
    }

    public void setData(ArrayList<FourSNews> listData) {
        if (mPage == DEFAULT_PATE) {
            mFourSNewses.clear();
        }
        mFourSNewses.addAll(listData);
        mAdapter.notifyDataSetChanged();
        mPtrFrameLayoutView.setVisibility(View.VISIBLE);
        mRequestFailView.setVisibility(View.GONE);
    }

    public interface RefreshRecommendListener {
        void refreshRecommend();
    }

    public void setRefreshRecommendListener(RefreshRecommendListener refreshRecommendListener) {
        mRefreshRecommendListener = refreshRecommendListener;
    }
}
