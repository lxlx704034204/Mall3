package com.hxqc.newenergy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.newenergy.adapter.EVRecommendedMoreAdapter;
import com.hxqc.newenergy.bean.RecommendBean;
import com.hxqc.widget.ListViewNoSlide;

import hxqc.mall.R;


/**
 * Created by CPR193 on 2016/3/23.
 */
public class RecommendViewList extends FrameLayout {


    ListViewNoSlide mRecommendedView;
    TextView mRecommendedMore_Button;
    TextView mRecommendedTitle;

    EVRecommendedMoreAdapter mRecommendMoreAdapter;

    RecommendBean mRecommendBeanList;
    public RecommendViewList(Context context) {
        super(context);
        inIt();
    }

    public RecommendViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }

    public RecommendViewList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inIt();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendViewList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inIt();
    }

    public void inIt() {
        inflate(getContext(), R.layout.view_recommened, this);
        mRecommendedView = (ListViewNoSlide) findViewById(R.id.recommened_listview);
        mRecommendedMore_Button = (TextView) findViewById(R.id.recommended_more_button);
        mRecommendedTitle = (TextView) findViewById(R.id.recommened_title);
        mRecommendMoreAdapter = new EVRecommendedMoreAdapter(getContext());
        mRecommendedView.setFocusable(false);
        mRecommendedMore_Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecommendBeanList!=null&&mRecommendBeanList.recommendType!=null){

                }

            }
        });


        mRecommendedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivitySwitcher.toAutoItemDetail(getContext(),mRecommendBeanList.recommendType,
                        mRecommendBeanList.recommend.get(position).itemID,"车辆详情");
            }
        });


    }

    public void setData(RecommendBean RecommendList) {
        mRecommendedTitle.setText(RecommendList.title);
        mRecommendMoreAdapter.setData(RecommendList.recommend);
        mRecommendBeanList = RecommendList;
        mRecommendedView.setAdapter(mRecommendMoreAdapter);


    }

}
