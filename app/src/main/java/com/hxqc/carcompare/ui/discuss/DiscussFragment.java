package com.hxqc.carcompare.ui.discuss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.hxqc.carcompare.control.ErrorViewCtrl;
import com.hxqc.mall.core.fragment.SwipeRefreshFragment;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.core.util.utils.disklurcache.DiskLruCacheHelper;
import com.hxqc.mall.thirdshop.api.InfoApiClient;
import com.hxqc.mall.thirdshop.model.newcar.GradeScore;
import com.hxqc.mall.thirdshop.model.newcar.PublicComment;
import com.hxqc.mall.thirdshop.views.adpter.UserGradeAdapter2;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hxqc.mall.R;

/**
 * 口碑评价 列表
 * Created by zhaofan on 16/11/1.
 */
public class DiscussFragment extends SwipeRefreshFragmentForListView implements SwipeRefreshFragment.onFailureResponse {

    private String extID, brand, series;
    private GridView userGradeGv;
    UserGradeAdapter2 mUserGradeAdapter;
    private DiskLruCacheHelper mDiskLruCacheHelper;

    public static DiscussFragment newInstance(String extID, String brand, String series) {
        DiscussFragment f = new DiscussFragment();
        Bundle args = new Bundle();
        args.putString("extID", extID);
        args.putString("brand", brand);
        args.putString("series", series);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extID = getArguments().getString("extID");
        brand = getArguments().getString("brand");
        series = getArguments().getString("series");
        mDiskLruCacheHelper = DiskLruCacheHelper.builder(getActivity());
        setOnFailureListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //addHeader
        View header = View.inflate(getContext(), R.layout.fragment_discuss, null);
        mListView.addHeaderView(header);
        userGradeGv = (GridView) header.findViewById(R.id.user_grade_gv);
        mUserGradeAdapter = new UserGradeAdapter2(getActivity(), false);
        mListView.setAdapter(mUserGradeAdapter);
        //加载缓存
        loadCacheData();
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        new InfoApiClient().getAutoGrade(extID, brand, series, mPage, PER_PAGE, getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        PublicComment mUserGrade = JSONUtils.fromJson(response, PublicComment.class);

        //初次加载
        if (mPage == DEFAULT_PATE) {
            if (mUserGrade == null || mUserGrade.count == 0) {
                ErrorViewCtrl.DiscussEmpty(getActivity());
            } else {
                saveCache(response);
                setUserGrade(mUserGrade);
                refreshListView(mUserGrade, true);
            }
        }
        //加载更多
        else {
            refreshListView(mUserGrade, false);
        }

        setLoadMore(mUserGrade);
    }

    private void setLoadMore(PublicComment mUserGrade) {
        if (mUserGrade == null || mUserGrade.count == 0 || mUserGrade.userGradeComment.size() < PER_PAGE)
            mPtrHelper.setHasMore(false);
        else
            mPtrHelper.setHasMore(true);
    }

    @Override
    public void onFailureResponse() {
        PublicComment cacheData = getCache();
        //有缓存 请求失败不显示错误界面
        if (mPage == DEFAULT_PATE && cacheData != null && cacheData.count != 0) {
            mRequestFailView.setVisibility(View.GONE);
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            setLoadMore(cacheData);
        }
    }


    private void saveCache(String response) {
        mDiskLruCacheHelper.put(extID + brand + series, response);
    }

    private PublicComment getCache() {
        return JSONUtils.fromJson(mDiskLruCacheHelper.getAsString(extID + brand + series), PublicComment.class);
    }

    private void loadCacheData() {
        PublicComment cacheData = getCache();
        if (cacheData != null) {
            setUserGrade(cacheData);
            refreshListView(cacheData, true);
        }
    }

    /**
     * 用户评价
     *
     * @param mUserGrade
     */
    private void setUserGrade(PublicComment mUserGrade) {

        //评分GridView
        GradeScore gradeBean = mUserGrade.grade;
        String[] labels = new String[]{"空间", "动力", "油耗", "舒适性", "外观", "内饰"};
        String[] grades = new String[]{gradeBean.space, gradeBean.power, gradeBean.fuelConsumption,
                gradeBean.comfort, gradeBean.appearance, gradeBean.interiorTrimming};

        List<Map<String, Object>> mList = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", labels[i]);
            map.put("grade", grades[i] + "分");
            mList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), mList,
                com.hxqc.mall.thirdshop.R.layout.item_user_grade, new String[]{"label", "grade"},
                new int[]{com.hxqc.mall.thirdshop.R.id.label, com.hxqc.mall.thirdshop.R.id.grade});
        userGradeGv.setAdapter(adapter);

    }

    private void refreshListView(PublicComment mUserGrade, boolean isClear) {
        //评论列表
        mUserGradeAdapter.setData(mUserGrade.userGradeComment, isClear);
    }

    @Override
    protected String getEmptyDescription() {
        return "搜索无结果";
    }

    @Override
    public String fragmentDescription() {
        return "口碑评价列表Fragment";
    }


}
