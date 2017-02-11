package com.hxqc.mall.fragment.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.adapter.PromotionMessageAdapter;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.PushMessage;
import com.hxqc.mall.core.model.PushMessageGroup;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;


/**
 * 说明:推荐活动
 * <p/>
 * author: 吕飞
 * since: 2015-03-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class PromotionMessageFragment extends MessageFragment {
    protected ArrayList<PushMessageGroup> mPromotionMessageGroups = new ArrayList<>();
    protected PromotionMessageAdapter mPromotionMessageAdapter;
    ArrayList<PushMessage> mPromotionMessages;//现有消息

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public String fragmentDescription() {
        return "推荐活动";
    }

    @Override
    protected void getData(boolean showAnim) {
        mApiClient.getPromotionMessage(UserInfoHelper.getInstance().getToken(getContext()), mPage,
                getLoadingAnimResponseHandler(showAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<PushMessage> mMorePromotionMessages = JSONUtils.fromJson(response, new TypeToken<ArrayList<PushMessage>>() {
        });
        if (mMorePromotionMessages == null) return;
        if (mPage == DefaultPage) {
            mPromotionMessages = mMorePromotionMessages;
        } else {
            mPromotionMessages.addAll(mMorePromotionMessages);
        }
        mPtrHelper.setHasMore(mMorePromotionMessages.size() >= 15);
        showList(initGroup(mPromotionMessages));
    }

    private void showList(ArrayList<PushMessageGroup> promotionMessageGroups) {
        mPromotionMessageGroups.clear();
        if (mPage == DefaultPage) {
//            mPromotionMessageGroups = promotionMessageGroups;
            mPromotionMessageAdapter = new PromotionMessageAdapter(mContext, mPromotionMessageGroups);
            mMessageListView.setAdapter(mPromotionMessageAdapter);
            mMessageListView.setOnHeaderUpdateListener(mPromotionMessageAdapter);
        }
        mPromotionMessageGroups.addAll(promotionMessageGroups);
        mPromotionMessageAdapter.notifyDataSetInvalidated();
        OtherUtil.openAllChild(mPromotionMessageAdapter, mMessageListView);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (mPromotionMessageGroups.get(groupPosition).pushMessages.get(childPosition).isRead == 0) {
            mPromotionMessageGroups.get(groupPosition).pushMessages.get(childPosition).isRead = 1;
            mApiClient.setMessageStatus(UserInfoHelper.getInstance().getToken(getContext()), 0, mPromotionMessageGroups.get(groupPosition).pushMessages.get(childPosition).messageID,
                    mPromotionMessageGroups.get(groupPosition).pushMessages.get(childPosition).isRead, new BaseMallJsonHttpResponseHandler(mContext) {
                        @Override
                        public void onSuccess(String response) {
                        }
                    });
            mPromotionMessageAdapter.notifyDataSetInvalidated();
        }

        PushMessage promotionMessage = mPromotionMessageGroups.get(groupPosition).pushMessages.get(childPosition);
        DebugLog.i("Tag", "url    " + promotionMessage.url);
        if (!TextUtils.isEmpty(promotionMessage.url))
            ActivitySwitcher.toEventDetail(mContext, promotionMessage.url);
        return false;
    }

    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }
}
