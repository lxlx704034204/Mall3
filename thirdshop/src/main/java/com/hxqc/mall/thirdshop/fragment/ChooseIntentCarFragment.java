package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.promotion.SalesItem;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.adpter.ChooseIntentCarTypeAdapter;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月03日
 */
public class ChooseIntentCarFragment extends SwipeRefreshForRecyclerFragment implements ChooseIntentCarTypeAdapter.IntentCarItemChooseListener{
    public final static String TAG = ChooseIntentCarFragment.class.getSimpleName();
    ThirdPartShopClient ThirdPartShopClient;
    ArrayList<SalesItem > salesItems = new ArrayList<>();
    ChooseIntentCarListener chooseIntentCarListener;
    String shopID;
    String seriesID;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "---------- onAttach");
        super.onAttach(context);
        ThirdPartShopClient = new ThirdPartShopClient();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "----------- onCreate");
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        shopID = bundle.getString("shopID");
        seriesID = bundle.getString("seriesID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "------------ onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "---------- onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData(true);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "--------- onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        salesItems = getArguments().getParcelableArrayList(ActivitySwitcherThirdPartShop.INTENT_TYPE);
        assert salesItems != null;
        if(salesItems.size() > 0) {
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
            ChooseIntentCarTypeAdapter chooseIntentCarTypeAdapter = new ChooseIntentCarTypeAdapter(salesItems, mContext);
            chooseIntentCarTypeAdapter.setIntentCarItemChooseListener(this);
            mAdapter = chooseIntentCarTypeAdapter;
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            showNoData();
        }
    }

    @Override
    public void onRefresh() {
//        super.onRefresh();
        if (mPtrFrameLayoutView.isRefreshing()) mPtrHelper.refreshComplete(mPtrFrameLayoutView);
    }

    @Override
    protected void onSuccessResponse(String response) {

    }

    public void setChooseIntentCarListener(ChooseIntentCarListener chooseIntentCarListener) {
        this.chooseIntentCarListener = chooseIntentCarListener;
    }

    @Override
    protected String getEmptyDescription() {
        return "再去看看";
    }

    @Override
    public String fragmentDescription() {
        return "选择意向车型Fragment";
    }

    @Override
    public void onIntentCarChoosed(SalesItem salesItem) {
        chooseIntentCarListener.onChooseIntentCar(salesItem);
    }

    @Override
    public boolean hasMore() {
//        return mPtrHelper.isHasMore();
        return false;
    }

    public interface ChooseIntentCarListener {
        void onChooseIntentCar(SalesItem salesItem);
    }
}
