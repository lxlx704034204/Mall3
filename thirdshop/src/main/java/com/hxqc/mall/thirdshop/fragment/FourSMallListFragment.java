package com.hxqc.mall.thirdshop.fragment;

import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;

/**
 * 说明:4s首页资讯列表
 *
 * @author: 吕飞
 * @since: 2016-11-16
 * Copyright:恒信汽车电子商务有限公司
 */

public class FourSMallListFragment extends SwipeRefreshForRecyclerFragment {

    @Override
    public void refreshData(boolean hasLoadingAnim) {

    }

    @Override
    protected void onSuccessResponse(String response) {

    }

    @Override
    protected String getEmptyDescription() {
        return "暂无相关资讯";
    }

    @Override
    public String fragmentDescription() {
        return "4s首页资讯列表";
    }
}
