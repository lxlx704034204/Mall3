package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.adapter.BaseRecyclerAdapter;
import com.hxqc.mall.core.adapter.RecyclerViewHolder;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.model.advisory.Advisory;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.views.AdvisoryAnswerView;
import com.hxqc.mall.thirdshop.maintenance.views.AdvisoryQuestionView;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-12
 * FIXME
 * Todo 咨询列表
 */
public class AdvisoryListActivity extends BackActivity {

    public static final String ITEMS = "advisories_items";

    public ArrayList<Advisory> mAdvisories;
    private RecyclerView advisoryList;
    private RequestFailView requestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisory_list);
        Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        if (bundleExtra != null)
            if (bundleExtra.getParcelableArrayList(ITEMS) != null)
                mAdvisories = getIntent().getParcelableArrayListExtra(ITEMS);
            else mAdvisories = new ArrayList<>();

        advisoryList = (RecyclerView) findViewById(R.id.advisory_list);
        requestView = (RequestFailView) findViewById(R.id.request_view);
        advisoryList.setLayoutManager(new LinearLayoutManager(this));
        advisoryList.setAdapter(new BaseRecyclerAdapter<Advisory>(this, mAdvisories) {

            @Override
            protected void bindData(RecyclerViewHolder holder, int position, Advisory item) {
                AdvisoryQuestionView questionView = (AdvisoryQuestionView) holder.getView(R.id.question);
                AdvisoryAnswerView answerView = (AdvisoryAnswerView) holder.getView(R.id.answer);
                questionView.addData(item.question);
                answerView.addData(item.answer);
            }

            @Override
            protected int getItemLayout(int viewType) {
                return R.layout.item_advisors_list;
            }
        });

    }
}
