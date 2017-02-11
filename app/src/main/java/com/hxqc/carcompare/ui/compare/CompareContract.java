package com.hxqc.carcompare.ui.compare;

import com.hxqc.carcompare.model.CompareNew;
import com.hxqc.carcompare.model.comparedisc.Discuss;
import com.hxqc.mall.core.base.mvp.BaseModel;
import com.hxqc.mall.core.base.mvp.BasePresenter;
import com.hxqc.mall.core.base.mvp.BaseView;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;


/**
 * Contract
 * Created by zhaofan on 2016/12/6.
 */
public interface CompareContract {

    /**
     * Model接口
     */
    interface Model extends BaseModel {
        /**
         * 获取网络数据
         */
        void getCompareData(int type, String modelId, AsyncHttpResponseHandler handler);

        /**
         * 保存基本参数对比的缓存数据
         */
        void UpdataBasicParmListCache(List<List<CompareNew>> data);

        /**
         * 获取基本参数对比的缓存数据
         */
        List<List<CompareNew>> getBasicParmListCache();

        /**
         * 保存评论对比的缓存数据
         */
        void saveDiscussDatas(List<Discuss> reponse_dics);

        /**
         * 获取评论对比的缓存数据
         */
        List<Discuss> getDiscussDatas();

        void clearCache();
    }

    /**
     * View接口
     */
    interface View extends BaseView {
        /**
         * 网络请求失败
         */
        void requestFail();

        /**
         * 刷新listciew数据
         */
        void refreshListView(boolean isAdd);

        /**
         * 加载动画
         */
        void showLoading();

        /**
         * 关闭加载动画
         */
        void hideLoading();

        /**
         * 添加头部滑动车辆信息
         */
        void addHeaderViews();

        /**
         * 头部滑动headerScroll位置还原
         */
        void headerScrollReset();

        /**
         * Adapter初始化
         */
        void initAdapter();

        void setAdapterData(List<List<CompareNew>> data);

        void ShowStickHeader(int tab_pos);

        void setAdapterMode(int tab_pos);

        void resetAdapterPosition(boolean isReset);

        void deleteAdapterList(boolean isDel, int listSize);

        void setChooseModeButtonIndex(int firstVisibleItem);

        void adapterDataNotifyChanged();
    }

    /**
     * Presenter接口
     */
    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getCompareData(int type, boolean isAdd, String modelId);

        public abstract void addCarJumpTo();

        public abstract void addCar(ModelInfo data);

        public abstract void deleteCar(int delPos);

        public abstract void changeDisplayMode();

        public abstract void hideCommonParam();

        public abstract void showCommonParam();

        public abstract void toDiscDetail(int parantPos, int childPos);

        public abstract void toDiscAll(int parantPos, int childPos);

        public abstract void initListView();

        public abstract void changeTab(int pos);

        public abstract void refreshListWhenScroll();

    }
}