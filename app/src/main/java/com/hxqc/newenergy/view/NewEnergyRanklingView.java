package com.hxqc.newenergy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxqc.newenergy.adapter.EVRankingAdapter;
import com.hxqc.newenergy.bean.EVNewenergyAutoSample;
import com.hxqc.newenergy.bean.EVRankingSchema;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/14.
 * Copyright:恒信汽车电子商务有限公司
 */
public class NewEnergyRanklingView extends EvHomeModuleView {


    public NewEnergyRanklingView(Context context) {
        super(context);
    }

    public NewEnergyRanklingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewEnergyRanklingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected ExpandCollapseView createRankingView(String title, ArrayList< EVNewenergyAutoSample > attention) {

        ExpandCollapseView mConcernedExpandableView = new ExpandCollapseView(getContext());

        View mConcernedHeadView = LayoutInflater.from(getContext()).inflate(R.layout.activity_ev_rankingviewheade, null);
        TextView mTitleView = ((TextView) mConcernedHeadView.findViewById(R.id.rankingtitle));
        mTitleView.setText(title);
        mConcernedExpandableView.addTitleView(mConcernedHeadView);

        ListViewNoSlide mRankingConcernedview = (ListViewNoSlide) LayoutInflater.from(getContext()).inflate(R.layout.activity_ev_rakingviewcontent, null).findViewById(R.id.raking_list);
        mRankingConcernedview.setFocusable(false);
//        ((ViewGroup) mRankingConcernedview.getParent()).removeAllViews();
        EVRankingAdapter MEvRankingConcernedAdapter = new EVRankingAdapter(getContext());
        MEvRankingConcernedAdapter.setData(attention, title);
        mRankingConcernedview.setAdapter(MEvRankingConcernedAdapter);
        mConcernedExpandableView.addContentView(mRankingConcernedview);
        mContentView.addView(mConcernedExpandableView);

        return mConcernedExpandableView;
    }


    public void setData(EVRankingSchema data) {
        if (mContentView.getChildCount() > 0) mContentView.removeAllViews();
        if (data.attention != null) {
            final ExpandCollapseView expandCollapseView = createRankingView("用户关注", data.attention);
            expandCollapseView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandCollapseView.expand();
                }
            }, 500);
        }

        if (data.batteryLife != null) {
            final ExpandCollapseView expandCollapseView = createRankingView("纯电续航", data.batteryLife);
            expandCollapseView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandCollapseView.expand();
                }
            }, 500);
        }

        if (data.subsidy != null) {
            final ExpandCollapseView expandCollapseView = createRankingView("购车补贴", data.subsidy);
            expandCollapseView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandCollapseView.expand();
                }
            }, 500);
        }
    }


}
