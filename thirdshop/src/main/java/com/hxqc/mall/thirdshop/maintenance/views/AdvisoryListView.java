package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.mall.core.adapter.CommonAdapter;
import com.hxqc.mall.core.adapter.ViewHolder;
import com.hxqc.mall.core.model.advisory.Advisory;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.views.CommonTitleView;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-11
 * FIXME
 * Todo 咨询列表
 */
public class AdvisoryListView extends LinearLayout {
    private final CommonTitleView topTitle;
    private ListViewNoSlide advisoryList;

    public View.OnClickListener onClickListener;

    public AdvisoryListView(Context context) {
        this(context, null);
    }

    public AdvisoryListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvisoryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.advisory_list_view, this);
        advisoryList = (ListViewNoSlide) findViewById(R.id.advisory_list);
        topTitle = (CommonTitleView) findViewById(R.id.top_title);
        topTitle.setOnClickListener(new CommonTitleView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onClickListener)
                    onClickListener.onClick(AdvisoryListView.this);
            }
        });
    }

    public void addData(final ArrayList<Advisory> advisories) {
        if (advisories == null || advisories.size() == 0)
            setVisibility(GONE);
        else {
            setVisibility(VISIBLE);
            //添加数据
            advisoryList.setAdapter(new CommonAdapter<Advisory>(getContext(), advisories, R.layout.item_advisors_list) {
                @Override
                public void convert(ViewHolder helper, Advisory item, int position) {
                    AdvisoryQuestionView questionView = helper.getView(R.id.question);
                    AdvisoryAnswerView answerView = helper.getView(R.id.answer);
                    questionView.addData(item.question);
                    answerView.addData(item.answer);
                }

                @Override
                public int getCount() {
                    return advisories.size() > 3 ? 3 : advisories.size();
                }
            });
        }
    }
}
