package com.hxqc.autonews.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Scroller;

import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.model.AutoCalendarModel;
import com.hxqc.autonews.model.pojos.AutoCalendar;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.util.ToAutoInfoDetailUtil;
import com.hxqc.autonews.view.IView;
import com.hxqc.autonews.widget.NewAutoDateScrollBar;
import com.hxqc.autonews.widget.ScrollMonth;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.adapter.BaseRecyclerAdapter;
import com.hxqc.mall.core.adapter.RecyclerViewHolder;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author:李烽
 * todo 新车日历
 */
public class NewAutoCalendarActivity extends BackActivity implements ScrollMonth.OnMonthSelectListener, NewAutoDateScrollBar.OnYearSelectListener, IView<ArrayList<AutoCalendar>> {
    private NewAutoDateScrollBar scrollBar;
    private RecyclerView newAutoList;
    private List<AutoCalendar> newAutos;
    private AutoInformationApiClient client;
    private AutoCalendarModel autoCalendarModel;
    private String[] months;
    private LinearLayoutManager linearLayoutManager;
    private Scroller mScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_auto);
        mScroller = new Scroller(this);
        newAutos = new ArrayList<>();
        client = new AutoInformationApiClient();
        autoCalendarModel = new AutoCalendarModel(client, this);
        months = getResources().getStringArray(R.array.month);
        initView();
    }

    private void initView() {
        scrollBar = (NewAutoDateScrollBar) findViewById(R.id.scroll_bar);
        scrollBar.initData(this);
        scrollBar.setOnMonthSelectListener(this);
        scrollBar.setOnYearSelectListener(this);
        newAutoList = (RecyclerView) findViewById(R.id.new_auto_list);
        linearLayoutManager = new LinearLayoutManager(this);
        newAutoList.setLayoutManager(linearLayoutManager);
        newAutoList.setAdapter(new BaseRecyclerAdapter<AutoCalendar>(this, newAutos) {

            @Override
            protected void bindData(RecyclerViewHolder holder, int position, AutoCalendar newAuto) {
                holder.setText(R.id.model_name, newAuto.shorTitle);
                holder.setText(R.id.auto_level, newAuto.modelType);
                holder.setText(R.id.date, newAuto.dateText + newAuto.statusText);
                holder.setText(R.id.price, OtherUtil.formatPriceRange(newAuto.priceRange));
                ImageView pic = holder.getImageView(R.id.pic);
                ImageUtil.setImage(mContext, pic, newAuto.thumbImage);
            }

            @Override
            protected int getItemLayout(int viewType) {
                return R.layout.item_new_auto_list;
            }

            @Override
            protected OnItemClickListener getOnItemClickListener() {
                return new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //点击去往汽车资讯
                        ToAutoInfoDetailUtil.onToNextPage(newAutos.get(position).infoID,
                                AutoInformation.Type.autoCalendar, NewAutoCalendarActivity.this);
                    }
                };
            }
        });
    }

    @Override
    public void onMonthSelected(String month) {
        //滚动到指定的item
        final int position = getPosition(month);
        DebugLog.i(getClass().getSimpleName(), position + "");
        int fItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lItem = linearLayoutManager.findLastVisibleItemPosition();
        if (position <=fItem) {
            newAutoList.smoothScrollToPosition(position);
        } else {
            linearLayoutManager.scrollToPositionWithOffset(newAutos.size() - 1, 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    newAutoList.smoothScrollToPosition(position);
                }
            }, 50);
        }
    }


    private int getPosition(String month) {
        if (TextUtils.isEmpty(month))
            return newAutos.size() - 1;
        for (int i = 0; i < newAutos.size(); i++) {
            String dateText = newAutos.get(i).dateText;
            String substring = dateText.substring(0, dateText.lastIndexOf("月") + 1);
            if (month.equals(substring))
                return i;
        }
        String nextMonth = getNextMonth(month);
        return getPosition(nextMonth);
    }

    private String getNextMonth(String month) {
        for (int i = 0; i < months.length; i++) {
            boolean equals = months[i].equals(month);
            if (equals && !month.equals("12月")) {
                return months[i + 1];
            }
        }
        return "";
    }

    @Override
    public void onYearSelected(String year) {
        loadData(year);
    }

    private void loadData(String year) {
        Presenter presenter = new Presenter();
        presenter.getAutoCalendar(year, this, autoCalendarModel);
    }

    @Override
    public void onDataBack(ArrayList<AutoCalendar> data) {
        this.newAutos.clear();
        newAutos.addAll(data);
        newAutoList.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onDataNull(String message) {
        //获取为空这一年都没有
    }

}
