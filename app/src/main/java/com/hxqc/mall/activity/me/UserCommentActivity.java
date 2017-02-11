package com.hxqc.mall.activity.me;

import android.os.Bundle;

import com.hxqc.mall.fragment.me.CommentedForRecyclerFragment;
import com.hxqc.mall.fragment.me.UncommentForRecyclerFragment;
import com.hxqc.mall.views.indicater.IndicatorFragmentActivity;

import java.util.List;

import hxqc.mall.R;

/**
 * 说明:我的评论
 *
 * author: 吕飞
 * since: 2015-04-02
 * Copyright:恒信汽车电子商务有限公司
 */
public class    UserCommentActivity extends IndicatorFragmentActivity {
    public static final int FRAGMENT_UNCOMMENT = 0;
    public static final int FRAGMENT_COMMENTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int supplyTabs(List< TabInfo > tabs) {
        tabs.add(new TabInfo(FRAGMENT_UNCOMMENT, getString(R.string.me_uncomment),
                UncommentForRecyclerFragment.class));
        tabs.add(new TabInfo(FRAGMENT_COMMENTED, getString(R.string.me_commented),
                CommentedForRecyclerFragment.class));
        return FRAGMENT_UNCOMMENT;
    }

}
