package com.hxqc.mall.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.PushMessage;
import com.hxqc.mall.core.model.PushMessageGroup;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.fragment.BaseFragment;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 说明:消息管理基类fragment
 *
 * author: 吕飞
 * since: 2015-04-01
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class MessageFragment extends BaseFragment implements
        ExpandableListView.OnChildClickListener, OnRefreshHandler {
    protected PinnedHeaderExpandableListView mMessageListView;
    protected int mPage = DefaultPage;//当前页
    protected final int PER_PAGE = 15;
    protected final static int DefaultPage = 1;
    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_only_pinned_header_expandable_list_view, container, false);

        mPtrFrameLayoutView = (PtrFrameLayout) rootView.findViewById(com.hxqc.mall.core.R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);

        mMessageListView = (PinnedHeaderExpandableListView) rootView.findViewById(R.id.message_list);
        mMessageListView.setOnChildClickListener(this);
        mMessageListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        }, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData(true);
    }

    protected abstract void getData(boolean showAnim);

    protected abstract void onSuccessResponse(String response);

    @Override
    public void onRefresh() {
        mPage = DefaultPage;
        getData(false);
    }

    @Override
    public void onLoadMore() {
        getData(true);
    }

    protected LoadingAnimResponseHandler getLoadingAnimResponseHandler(boolean isShowAnim) {
        return new LoadingAnimResponseHandler(mContext, isShowAnim) {
            @Override
            public void onSuccess(String response) {
                onSuccessResponse(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPage++;
                mPtrHelper.refreshComplete(mPtrFrameLayoutView);
            }
        };
    }

    protected ArrayList< PushMessageGroup > initGroup(ArrayList< PushMessage > pushMessages) {
        ArrayList< PushMessageGroup > mGroups = new ArrayList<>();
        ArrayList< String > mDates = new ArrayList<>();
        for (int i = 0; i < pushMessages.size(); i++) {
            if (i == 0) {
                mDates.add(pushMessages.get(i).date);
            } else {
                if (!pushMessages.get(i).date.equals(pushMessages.get(i - 1).date)) {
                    mDates.add(pushMessages.get(i).date);
                }
            }
        }
        for (int i = 0; i < mDates.size(); i++) {
            ArrayList< PushMessage > mTempPushMessages = new ArrayList<>();
            for (int j = 0; j < pushMessages.size(); j++) {
                if (pushMessages.get(j).date.equals(mDates.get(i))) {
                    mTempPushMessages.add(pushMessages.get(j));
                }
            }
            mGroups.add(new PushMessageGroup(mDates.get(i), mTempPushMessages));
        }
        return mGroups;
    }
}
