package com.hxqc.carcompare.ui.compare;


import android.graphics.Color;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.carcompare.control.CompareCarsCtrl;
import com.hxqc.carcompare.model.CompareNew;
import com.hxqc.carcompare.ui.addcar.CarCompareListActivity;
import com.hxqc.carcompare.ui.compare.adapter.TheAdapter;
import com.hxqc.carcompare.ui.view.CHScrollView;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.mall.core.views.ErrorView;
import com.hxqc.mall.core.views.dialog.DialogFragment;
import com.hxqc.mall.core.views.sticklistviewbyzf.StickyListHeadersListView;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * 车型对比
 * Created by zhaofan on 2016/10/9.
 */
public class CarCompareDetailActivity extends initActivity<ComparePresenter, CompareModel> implements CompareContract.View,
        View.OnClickListener, AbsListView.OnScrollListener {
    public static final String TAG = CarCompareDetailActivity.class.getSimpleName();
    public static final String SHOW_DISC_DETAIL = "show_discuss_detail";
    public static final String ALL_DISCUSS = "show_all_discuss";
    public static final String ALL_LABEL = "显示全部";
    public static final String DEFAULT_LABEL = "显示默认参数";
    public static final String DIF_LABEL = "显示更多不同";
    public static final String TO_NEWS = "TO_NEWS";
    public static final int MAX_MODEL = 10;
    private CustomToolBar mToolbar;
    private TheAdapter adapter;
    private StickyListHeadersListView mListView;
    public HorizontalScrollView mTouchView;
    private CHScrollView headerScroll;
    //装入所有的HScrollView
    protected List<CHScrollView> mHScrollViews = new ArrayList<>();
    private TextView hide;
    private CheckBox hideCb;
    private RelativeLayout hideLay;
    private RelativeLayout mModelChooseLay;
    private RelativeLayout mScrollLay;
    private DialogFragment cpg;
    private TextView[] tab = new TextView[4];
    //   private int tab_pos = 0;
    public static String chooseMode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_compare;
    }

    @Override
    public void bindView() {
        mToolbar = getView(R.id.topbar);
        hide = getView(R.id.hide);
        mScrollLay = getView(R.id.scroll_lay);
        headerScroll = getView(R.id.item_scroll_title);
        mModelChooseLay = getView(R.id.lay_model_choose);
        mListView = getView(R.id.scroll_list);
        hideCb = getView(R.id.checkbox);
        hideLay = getView(R.id.hide_lay);
        tab[0] = getView(R.id.jbcs);
        tab[1] = getView(R.id.bjtj);
        tab[2] = getView(R.id.kbdb);
        tab[3] = getView(R.id.yhpj);
    }

    @Override
    public void init() {
        mPresenter.setVM(this, mModel);  //mvp
        chooseMode = DEFAULT_LABEL;
        mEventBus.register(this);
        cpg = DialogFragment.builder();
        bindListener();
        initAdapter();
        changeText(0);
        getData(0, false, mPresenter.getIds());
    }

    private void bindListener() {
        hide.setOnClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setOnScrollListener(this);
    }

    /**
     * 从上级页面获取勾选的车型
     * From {@link CarCompareListActivity#onClick }
     */
    @Subscribe(sticky = true)
    public void getModelId(Event msg) {
        if (msg.what.equals(CarCompareListActivity.TAG)) {
            List<ChooseCarModel> array = (List<ChooseCarModel>) msg.obj;
            mPresenter.setChooseCarArray(array);
        }
    }

    /**
     * 获取网络数据
     */
    private void getData(int type, boolean isAdd, String modelId) {
        mPresenter.getCompareData(type, isAdd, modelId);
    }


    /**
     * 网络请求失败
     */
    @Override
    public void requestFail() {
        ErrorView.builder(this).showFailed(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(0, false, mPresenter.getIds());
            }
        });
    }

    @Override
    public void refreshListView(boolean isAdd) {
        if (!isAdd) {
            mPresenter.initListView();
        } else {
            addListView();
        }
        hideLoading();
    }


    @Override
    public void showLoading() {
        cpg.show(mContext);
    }

    @Override
    public void hideLoading() {
        cpg.hide();
    }

    @Override
    public void initAdapter() {
        adapter = new TheAdapter(mContext, mHScrollViews, mListView);
        mListView.setAdapter(adapter);
        adapter.bindListener();
    }

    /**
     * 是否显示listview悬停头部
     */
    @Override
    public void ShowStickHeader(int tab_pos) {
        adapter.ShowStickHeader(tab_pos == 0);
    }

    /**
     * @param tab_pos 0：基本参数 1：资讯 2：评分 3：评论
     */
    @Override
    public void setAdapterMode(int tab_pos) {
        adapter.setMode(tab_pos);
    }

    @Override
    public void resetAdapterPosition(boolean isReset) {
        int scrollX = headerScroll.getScrollX();
        adapter.resetPosition(isReset, scrollX);
    }

    /**
     * 删除车辆时刷新adapter
     */
    @Override
    public void deleteAdapterList(boolean isDel, int listSize) {
        adapter.delete(isDel, listSize);
    }

    /**
     * 选择模式按钮
     */
    @Override
    public void setChooseModeButtonIndex(int firstVisibleItem) {
        adapter.chooseModeButtonIndex(firstVisibleItem);
    }

    @Override
    public void adapterDataNotifyChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setAdapterData(List<List<CompareNew>> datas) {
        adapter.setData(datas);
    }


    /**
     * 头部车辆信息HScrollViews
     */
    @Override
    public void addHeaderViews() {
        headerScroll.removeAllViews();
        mHScrollViews.add(headerScroll);
        headerScroll.addView(getTitleRow());
    }

    @Override
    public void headerScrollReset() {
        headerScroll.scrollTo(0, 0);
        onScrollChanged(0, 0, 0, 0);
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * TheAdapter item点击
     * from {@link  TheAdapter.MyOnclickListener#onClick}
     */
    @Subscribe
    public void ItemClickCallBack(Event msg) {
        //跳转评论详情
        if (msg.what.equals(SHOW_DISC_DETAIL)) {
            TheAdapter.Position pos = (TheAdapter.Position) msg.obj;
            mPresenter.toDiscDetail(pos.parantPos, pos.childPos);
        }
        //跳转全部评论
        else if (msg.what.equals(ALL_DISCUSS)) {
            TheAdapter.Position pos = (TheAdapter.Position) msg.obj;
            mPresenter.toDiscAll(pos.parantPos, pos.childPos);
        }
    }

    /**
     * 更改显示模式
     */
    @Subscribe
    public void chooseMode(Event msg) {
        if (msg.what.equals(TheAdapter.CHOOSE_MODE)) {
            chooseMode = (String) msg.obj;
            mPresenter.changeDisplayMode();
        }
    }


    /**
     * 已选车型的头部label View
     */
    private LinearLayout getTitleRow() {
        mModelChooseLay.setClickable(false);
        mModelChooseLay.setVisibility(View.VISIBLE);
        return CompareCarsCtrl.initModelTab(this, mPresenter.getChooseCarArray(), dListener, addListener);
    }

    /**
     * 增加车型
     */
    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.addCarJumpTo();
        }
    };

    /**
     * 添加车型
     * from {@link com.hxqc.mall.thirdshop.views.adpter.FilterCarModelAdapter#getView}
     */
    @Subscribe
    public void addCar(ModelInfo data) {
        DebugLog.i("ModelInfo", data.toString());
        mPresenter.addCar(data);
    }

    /**
     * 删除车型
     */
    private CompareCarsCtrl.onDeleteListener dListener = new CompareCarsCtrl.onDeleteListener() {
        @Override
        public void onClick(View v, int delPos) {
            v.setClickable(false);
            mPresenter.deleteCar(delPos);
        }
    };


    /**
     * Tab切换
     */
    private class TabChangeListener implements View.OnClickListener {
        int pos;

        public TabChangeListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            hideLay.setVisibility(pos == 0 ? View.INVISIBLE : View.INVISIBLE);
            changeText(pos);
            mPresenter.changeTab(pos);
        }
    }

    private void changeText(int pos) {
        mPresenter.setTabPosition(pos);
        for (int i = 0; i < tab.length; i++) {
            tab[i].setBackgroundResource(0);
            tab[i].setTextColor(Color.parseColor("#777777"));
            tab[i].setOnClickListener(new TabChangeListener(i));
        }
        tab[pos].setBackgroundResource(R.drawable.bg_flag_red_bottom);
        tab[pos].setTextColor(getResources().getColor(R.color.text_color_orange));
    }


    /**
     * 有添加\删除数据时刷新listview
     */
    private void addListView() {
        addHeaderViews();
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.initListView();
                int position = mListView.getFirstVisiblePosition();
                mListView.setSelection(position == 0 ? position : position + 1);
            }
        }, 0);
    }


    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (CHScrollView scrollView : mHScrollViews) {
            //防止重复滑动
            if (mTouchView != scrollView)
                scrollView.smoothScrollTo(l, t);
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        DebugLog.i("onScrollStateChanged", scrollState + "");
        if (scrollState == 1) {
            mPresenter.refreshListWhenScroll();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        DebugLog.i("onStickyHeaderChanged", "firstVisibleItem :  " + firstVisibleItem);
        mPresenter.setFirstVisibleItem(firstVisibleItem);
    }


}
