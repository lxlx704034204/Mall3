package com.hxqc.carcompare.ui.compare;

import android.app.Activity;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.carcompare.control.CompareCarsCtrl;
import com.hxqc.carcompare.control.ErrorViewCtrl;
import com.hxqc.carcompare.db.DAO;
import com.hxqc.carcompare.model.CompareNew;
import com.hxqc.carcompare.model.comparebasic.CompareParm;
import com.hxqc.carcompare.model.comparedisc.Discuss;
import com.hxqc.carcompare.model.comparegrade.CarGrade;
import com.hxqc.carcompare.model.comparegrade.GradeEntity;
import com.hxqc.carcompare.model.comparenews.AutoNews;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel_Table;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.mall.thirdshop.model.newcar.UserDiscussDetail;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;
import com.raizlabs.android.dbflow.sql.language.Condition;

import cz.msebera.android.httpclient.Header;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter
 * Created by zhaofan on 2016/12/6.
 */
public class ComparePresenter extends CompareContract.Presenter {
    initActivity base;
    private int tab_pos = 0;
    private int firstVisibleItem = 0;
    private List<ChooseCarModel> ChooseCarArray;  //已选择的车型
    private List<List<CompareNew>>[] datas = new List[4];
    private List<CompareParm> reponse_datas;  //基本参数
    private List<AutoNews> reponse_news;   //资讯推荐
    private List<CarGrade> reponse_grade;  //用户评价
    private List<Discuss> reponse_dics;   //评论

    private boolean refreshWhenDel = false;  //删除车辆时 刷新一次listview

    /**
     * 获取网络数据
     *
     * @param type
     * @param isAdd
     * @param modelId
     */
    @Override
    public void getCompareData(final int type, final boolean isAdd, String modelId) {
        mView.showLoading();
        mModel.getCompareData(type, modelId, new LoadingAnimResponseHandler(mContext, false) {
            @Override
            public void onSuccess(String responseString) {
                if (!isAdd)
                    mView.headerScrollReset(); //headerScroll.scrollTo(0, 0);
                if (type == 0) {
                    getDataOfBasicParam(isAdd, responseString);
                } else if (type == 1) {
                    getDataOfNews(isAdd, responseString);
                } else if (type == 2) {
                    getDataOfReputation(isAdd, responseString);
                } else if (type == 3) {
                    getDataOfDiscussion(isAdd, responseString);
                }
                mView.addHeaderViews();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
                super.onFailure(statusCode, headers, responseBytes, throwable);
                mView.requestFail();
                mView.hideLoading();
            }
        });

    }


    /**
     * 基本参数对比
     */
    private void getDataOfBasicParam(boolean isAdd, String response) {
        if (!isAdd) {
            reponse_datas = new Gson().fromJson(response, new TypeToken<List<CompareParm>>() {
            }.getType());
        } else {
            List<CompareParm> addData = new Gson().fromJson(response, new TypeToken<List<CompareParm>>() {
            }.getType());
            reponse_datas.addAll(addData);
        }
        if (isShowEmpty(reponse_datas)) {
            return;
        }
        toNewDatas();
        adjustLabeltoNewDatas();
        mModel.UpdataBasicParmListCache(datas[0]);
        changeDisplayModeCtrl(datas[0]);
        mView.refreshListView(isAdd);
    }

    // 将接口返回的的嵌套list数据转化为 List<CompareNew> datas
    private void toNewDatas() {
        ChooseCarArray = new ArrayList<>();
        for (CompareParm i : reponse_datas) {
            ChooseCarArray.add(new ChooseCarModel(i.getBrand(), i.getModel(), i.getExtID(), 0));
        }
        datas[0] = CompareCarsCtrl.toNewDatas(datas, reponse_datas);
    }

    //使各款车型的label数量保持一致，并组成新的new_datas
    private void adjustLabeltoNewDatas() {
        List<List<CompareNew>> new_datas = CompareCarsCtrl.scheduleNewDatas(datas);
        datas[0] = new ArrayList<>(new_datas);
    }

    /**
     * 资讯推荐对比
     */
    private void getDataOfNews(boolean isAdd, String responseString) {
        if (!isAdd) {
            datas[1] = new ArrayList<>();
        }
        reponse_news = new Gson().fromJson(responseString, new TypeToken<List<AutoNews>>() {
        }.getType());
        adjustNewsDatas(reponse_news);
        mView.refreshListView(isAdd);
    }

    //资讯保留4格
    private void adjustNewsDatas(List<AutoNews> reponse_news) {
        CompareCarsCtrl.adjustNewsDatas(datas[1], reponse_news);
    }


    /**
     * 口碑评分对比
     */
    private void getDataOfReputation(boolean isAdd, String responseString) {
        if (!isAdd) {
            datas[2] = new ArrayList<>();
        }
        reponse_grade = new Gson().fromJson(responseString, new TypeToken<List<CarGrade>>() {
        }.getType());
        adjustUserGradeDatas(reponse_grade);
        mView.refreshListView(isAdd);
    }

    private void adjustUserGradeDatas(List<CarGrade> reponse_grade) {
        for (CarGrade i : reponse_grade) {
            List<CompareNew> data = new ArrayList<>();
            for (GradeEntity j : i.getAutoNews()) {
                data.add(new CompareNew("", j.label, j.value + "分", 0));
            }
            datas[2].add(data);
        }
    }

    /**
     * 用户评论对比
     */
    private void getDataOfDiscussion(boolean isAdd, String responseString) {
        reponse_dics = new Gson().fromJson(responseString, new TypeToken<List<Discuss>>() {
        }.getType());
        if (isAdd) {
            List<Discuss> mList = mModel.getDiscussDatas();
            mList.addAll(reponse_dics);
            reponse_dics = new ArrayList<>(mList);
        }
        mModel.saveDiscussDatas(reponse_dics);
        adjustDiscussDatas(isAdd, reponse_dics);
        mView.refreshListView(isAdd);
    }


    //保留3条评论
    private void adjustDiscussDatas(boolean isAdd, List<Discuss> reponse_dics) {
        datas[3] = CompareCarsCtrl.adjustDiscussDatas(isAdd, reponse_dics);
    }

    /**
     * 添加对比车辆
     *
     * @param data
     */
    @Override
    public void addCar(ModelInfo data) {
        boolean duplicate = false;
        for (ChooseCarModel i : ChooseCarArray) {
            if (i.getExtId().equals(data.getExtID()))
                duplicate = true;
        }
        if (!duplicate) {
            datas[0] = mModel.getBasicParmListCache();
            for (int i = 0; i < datas.length; i++) {
                if (i != tab_pos && datas[i] != null)
                    datas[i].clear();
            }
            //增加一个list
            getCompareData(tab_pos, true, data.getExtID());
            ChooseCarArray.add(new ChooseCarModel(data.getBrand(), data.getModelName(), data.getExtID(), 1));
        }
    }

    @Override
    public void addCarJumpTo() {
        Condition[] mConditions = new Condition[ChooseCarArray.size()];
        int pos = 0;
        for (ChooseCarModel i : ChooseCarArray) {
            mConditions[pos] = ChooseCarModel_Table.extId.notEq(i.getExtId());
            pos++;
        }
        List<ChooseCarModel> list = DAO.queryCarCompareList(mConditions);// DbHelper.query(ChooseCarModel.class, array);
        if (list.isEmpty())
            ActivitySwitcherThirdPartShop.toFilterAllShopBrand(mContext, true);
        else {
            EventBus.getDefault().postSticky(new Event(list, CarCompareDetailActivity.TAG));
            ActivitySwitchAutoInformation.toCarChooseList(mContext);
        }

    }

    /**
     * 删除对比车辆
     *
     * @param delPos
     */
    @Override
    public void deleteCar(int delPos) {
        mView.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.hideLoading();
            }
        }, 400);
        ChooseCarArray.remove(delPos);
        if (isShowEmpty(ChooseCarArray))
            return;
        //删除一列数据
        for (List<List<CompareNew>> data : datas) {
            if (data != null && data.size() > delPos)
                data.remove(delPos);
        }

        if (reponse_datas != null && reponse_datas.size() > delPos)
            reponse_datas.remove(delPos);

        if (reponse_dics != null && reponse_dics.size() > delPos)
            reponse_dics.remove(delPos);

        //  if (tab_pos == 0 && datas[0] != null) {
        List<List<CompareNew>> original_datas = mModel.getBasicParmListCache();
        if (original_datas != null && delPos < original_datas.size()) {
            original_datas.remove(delPos);
            mModel.UpdataBasicParmListCache(original_datas);
        }

        //评论对比 删除数据时要重新计算list
        List<Discuss> mList = mModel.getDiscussDatas();
        if (mList != null && !mList.isEmpty() && delPos < mList.size()) {
            mList.remove(delPos);
            mModel.saveDiscussDatas(mList);
            adjustDiscussDatas(false, mList);
        }

        //重新计算label标签
        adjustLabeltoNewDatas();
        // addListView();
        changeDisplayModeCtrl(original_datas);

        mView.addHeaderViews();
        mView.deleteAdapterList(true, ChooseCarArray.size());
        mView.setChooseModeButtonIndex(firstVisibleItem);
        mView.setAdapterData(datas[tab_pos]);
        refreshWhenDel = true;

    }


    /**
     * 更改基本参数显示模式
     */
    @Override
    public void changeDisplayMode() {
        changeDisplayModeCtrl(mModel.getBasicParmListCache());
        mView.setAdapterData(datas[tab_pos]);
        mView.setChooseModeButtonIndex(firstVisibleItem);
    }

    public void changeDisplayModeCtrl(List<List<CompareNew>> data) {
        if (data != null)
            datas[0] = data;
        if (CarCompareDetailActivity.chooseMode.equals(CarCompareDetailActivity.DEFAULT_LABEL)) {
            CompareCarsCtrl.showDefault(datas[0]);
        } else if (CarCompareDetailActivity.chooseMode.equals(CarCompareDetailActivity.DIF_LABEL)) {
            hideCommonParam();
        }
    }

    /**
     * 隐藏相同value
     */
    @Override
    public void hideCommonParam() {
        List<List<CompareNew>> original_datas = mModel.getBasicParmListCache();
        if (original_datas != null)
            datas[0] = new ArrayList<>(CompareCarsCtrl.hideCommonParam(datas[0], original_datas));
    }

    @Override
    public void showCommonParam() {
        datas[0] = mModel.getBasicParmListCache();
        mView.setAdapterData(datas[0]);
    }

    /**
     * 跳转评论详情
     *
     * @param parantPos
     * @param childPos
     */
    @Override
    public void toDiscDetail(int parantPos, int childPos) {
        UserDiscussDetail data = reponse_dics.get(parantPos).getUserGrade().get(childPos);
        DebugLog.i("data", data.toString());
        EventBus.getDefault().postSticky(data);
        ActivitySwitchAutoInformation.toPublicCommentDetail2(mContext);
    }

    /**
     * 跳转全部评论
     *
     * @param parantPos
     * @param childPos
     */
    @Override
    public void toDiscAll(int parantPos, int childPos) {
        String brand = ChooseCarArray.get(parantPos).brand;
        String extID = ChooseCarArray.get(parantPos).extId;
        ActivitySwitchAutoInformation.toPublicCommentList2(mContext, extID, brand, reponse_dics.get(parantPos).getSeries());
        DebugLog.i("data", brand + "  " + extID);
    }

    @Override
    public void initListView() {
        mView.initAdapter();
        mView.setAdapterData(datas[tab_pos]);
        mView.ShowStickHeader(tab_pos);
        mView.setAdapterMode(tab_pos);
        mView.resetAdapterPosition(true);
    }

    /**
     * @param pos 0：基本参数 1：资讯 2：评分 3：评论
     */
    @Override
    public void changeTab(int pos) {
        if (datas[pos] == null || datas[pos].isEmpty()) {
            getCompareData(pos, false, getIds());
        } else {
            mView.headerScrollReset();
            mView.setAdapterData(datas[tab_pos]);
            mView.ShowStickHeader(tab_pos);
            mView.setAdapterMode(tab_pos);
        }
    }

    /**
     * onScrollStateChanged监听,用来刷新adapter参数
     */
    @Override
    public void refreshListWhenScroll() {
        mView.resetAdapterPosition(false);
        mView.deleteAdapterList(false, 0);
        mView.setChooseModeButtonIndex(0);
        if (refreshWhenDel) {
            DebugLog.i("onScrollStateChanged", "refresh00");
            mView.deleteAdapterList(true, ChooseCarArray.size());
            mView.setChooseModeButtonIndex(0);
            mView.adapterDataNotifyChanged();
            refreshWhenDel = false;
        }
    }


    /**
     * @param mList
     * @param <T>
     * @return
     */
    private <T> boolean isShowEmpty(List<T> mList) {
        if (mList.isEmpty()) {
            ErrorViewCtrl.CarEmpty((Activity) mContext, null);
            mView.hideLoading();
            return true;
        }
        return false;
    }

    public void setChooseCarArray(List<ChooseCarModel> array) {
        this.ChooseCarArray = array;
    }

    public List<ChooseCarModel> getChooseCarArray() {
        return ChooseCarArray;
    }

    /**
     * 已选车型id数组
     */
    public String getIds() {
        List<String> ids = new ArrayList<>();
        for (ChooseCarModel i : ChooseCarArray)
            ids.add(i.getExtId());
        return ids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();
    }


    /**
     * 得到tab的位置
     *
     * @param pos 0：基本参数 1：资讯 2：评分 3：评论
     */
    public void setTabPosition(int pos) {
        this.tab_pos = pos;
    }


    public void setFirstVisibleItem(int firstVisibleItem) {
        this.firstVisibleItem = firstVisibleItem;
    }


    @Override
    public void onStart() {
        super.onStart();
        base = (initActivity) mContext;
        mModel.init(mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModel.clearCache();
    }


}
