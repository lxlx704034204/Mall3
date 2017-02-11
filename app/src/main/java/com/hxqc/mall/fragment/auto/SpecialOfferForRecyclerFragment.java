package com.hxqc.mall.fragment.auto;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.adapter.SpecialOfferAdapter;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.core.model.SpecialOffer;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hxqc.mall.R;

/**
 * 特卖列表
 */
public class SpecialOfferForRecyclerFragment extends SwipeRefreshForRecyclerFragment {

    NewAutoClient mApiClient;
    ArrayList< SpecialOffer > mSpecialOffers;//特卖数据
    Timer mTimer;
    String itemCategory;//特卖类型
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            for (int i = 0; i < mSpecialOffers.size(); i++) {
                mSpecialOffers.get(i).serverL++;
            }
            mAdapter.notifyDataSetChanged();
            return false;
        }
    });

    public SpecialOfferForRecyclerFragment() {

    }

    public static SpecialOfferForRecyclerFragment newInstance(String itemCategory) {
        SpecialOfferForRecyclerFragment fragment = new SpecialOfferForRecyclerFragment();
        Bundle args = new Bundle();
        args.putString("itemCategory", itemCategory);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemCategory = getArguments().getString("itemCategory");
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mApiClient = new NewAutoClient();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toWhere(getActivity(), getString(R.string.action_main));
            }
        });
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        mApiClient.getSpecialOffer(Integer.valueOf(itemCategory), getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        mSpecialOffers = JSONUtils.fromJson(response, new TypeToken< ArrayList< SpecialOffer > >() {
        });
        if (mSpecialOffers != null && mSpecialOffers.size() > 0) {
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
            showList();
        } else {
            showNoData();
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "暂时无特卖车辆";
    }


    @Override
    public String fragmentDescription() {
        return "特卖列表";
    }

    private void showList() {
        mAdapter = new SpecialOfferAdapter(mSpecialOffers, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
