package com.hxqc.mall.fragment.me;


import com.hxqc.mall.core.controler.UserInfoHelper;

/**
 * 说明:待评论
 *
 * author: 吕飞
 * since: 2015-04-02
 * Copyright:恒信汽车电子商务有限公司
 */
public class UncommentForRecyclerFragment extends UserCommentForRecyclerFragment {

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        mApiClient.getMyComments(UserInfoHelper.getInstance().getToken(getContext()), UNCOMMENT, mPage, getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected String getEmptyDescription() {
        return null;
    }

    @Override
    public String fragmentDescription() {
        return "待评论";
    }
}
