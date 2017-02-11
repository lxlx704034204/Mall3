package com.hxqc.mall.fragment.me;

import android.view.View;
import android.widget.ExpandableListView;

import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.PushMessage;
import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * 说明:订单消息
 *
 * author: 吕飞
 * since: 2015-03-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderMessageFragment extends PromotionMessageFragment {
    @Override
    public String fragmentDescription() {
        return "订单消息";
    }

    @Override
    protected void getData(boolean showAnim) {
        mApiClient.getOrderMessage(UserInfoHelper.getInstance().getToken(getContext()), mPage,
                getLoadingAnimResponseHandler(showAnim));
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        PushMessage pushMessage = mPromotionMessageGroups.get(groupPosition).pushMessages.get(childPosition);
        if (pushMessage.isRead == 0) {
            pushMessage.isRead = 1;
            mApiClient.setMessageStatus(UserInfoHelper.getInstance().getToken(getContext()), 0,
                    pushMessage.messageID,
                    pushMessage.isRead, new BaseMallJsonHttpResponseHandler(mContext) {
                        @Override
                        public void onSuccess(String response) {
                        }
                    });
            mPromotionMessageAdapter.notifyDataSetInvalidated();
        }
        ActivitySwitchBase.toOrderDetail(mContext, pushMessage.orderID);
        return false;
    }
}
