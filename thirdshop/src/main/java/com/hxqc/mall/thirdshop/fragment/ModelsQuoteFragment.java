package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.RecyclerViewOnScrollListener;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.shop.ModelsOfferActivity;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.model.ModelsQuote;
import com.hxqc.mall.thirdshop.views.adpter.ModelsQuoteRecyclerHeadersAdapter;
import com.hxqc.util.JSONUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * 店铺详情--车型报价列表Fragment
 *
 * @author liaoguilong
 * @since 2015年11月30日
 */
public class ModelsQuoteFragment extends FunctionFragment implements OnRefreshHandler {
    private RecyclerView mRecyclerView;
    private RequestFailView mRequestFailView;
    private ModelsQuoteRecyclerHeadersAdapter mModelsQuoteRecyclerHeadersAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    ThirdPartShopClient ThirdPartShopClient;
    ShopDetailsController mShopDetailsController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ThirdPartShopClient=new ThirdPartShopClient();
        mShopDetailsController=ShopDetailsController.getInstance();
        return inflater.inflate(R.layout.t_fragment_modelsquote, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.modelsquote_type_list);
      //  mLinearLayoutManager=new FullyLinearLayoutManager(getContext());
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerViewOnScrollListener(this));
        mRequestFailView = (RequestFailView) view.findViewById(R.id.modelsquote_refresh_fail_view);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position==0)
                    return;
                 toCarTypeFragment((ModelsQuote.Series) view.getTag());
            }
        }));
        BindData(true);
    }

    /**
     * 滚动到指定车系
     *
     * @param SeriesID
     */
    public void ScrollToSeriesID(String SeriesID) {
        if (mModelsQuoteRecyclerHeadersAdapter != null) {
            int Position=mModelsQuoteRecyclerHeadersAdapter.getIndexBySeriesID(SeriesID);
            if(Position>=0) {
                //+1是因为，Adapter中有一个head头部
               mLinearLayoutManager.scrollToPositionWithOffset((Position+1), mModelsQuoteRecyclerHeadersAdapter.getOffSet());
              //  mRecyclerView.scrollToPosition(Position);
                toCarTypeFragment(mModelsQuoteRecyclerHeadersAdapter.getSeriesByPosition(Position));
            }else{
                ToastHelper.showYellowToast(getContext(),"该系列车型不存在");
            }
        }
    }

    /**
     * 打开侧滑，并绑定数据
     * @param mSeries
     */
    public void toCarTypeFragment(ModelsQuote.Series mSeries){
        ((ModelsOfferActivity) getActivity()).openMenu();
        ((ModelsOfferActivity) getActivity()).mCarTypeFragment.BindData(mSeries, mShopDetailsController.getShopID(), mSeries.seriesID, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mModelsQuoteRecyclerHeadersAdapter !=null)
            mModelsQuoteRecyclerHeadersAdapter.notifyDataSetChanged();
    }

    private void BindData(boolean showAnim) {
            ThirdPartShopClient.getModelsQuote(mShopDetailsController.getShopID(), new LoadingAnimResponseHandler(getActivity(), showAnim) {


                @Override
                public void onSuccess(String response) {
                    ArrayList<ModelsQuote> mModelsQuotes = JSONUtils.fromJson(response, new TypeToken<ArrayList<ModelsQuote>>() {
                    });
                    if (mModelsQuotes != null && mModelsQuotes.size() > 0)
                        mRequestFailView.setVisibility(View.GONE);
                    else
                        noData();

                    mModelsQuoteRecyclerHeadersAdapter = new ModelsQuoteRecyclerHeadersAdapter(mModelsQuotes, mShopDetailsController.getThirdPartShop().getShopInfo(), getActivity());
                    mRecyclerView.setAdapter(mModelsQuoteRecyclerHeadersAdapter);
                    mRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mModelsQuoteRecyclerHeadersAdapter));
                    if (getActivity().getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ShopDetailsController.SERIESID_KEY) != null)
                        ScrollToSeriesID(getActivity().getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ShopDetailsController.SERIESID_KEY));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    requestFailView();
                }

            });

    }

    private void noData() {
        mRequestFailView.setEmptyDescription("无车型报价数据");
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    }


    private void requestFailView() {
        mRequestFailView.setEmptyDescription("获取数据失败");
        mRequestFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindData(true);
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    }


    @Override
    public String fragmentDescription() {
        return "车型报价列表";
    }
 
    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    /**
     * 定义Item点击事件
     */
    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;
        private OnItemClickListener mListener;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            // do nothing
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

    }




}
