package com.hxqc.mall.usedcar.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.AutoModel;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 说明:选择车型
 *
 * @author: 吕飞
 * @since: 2015-07-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseModelFragment extends SwipeRefreshFragmentForListView {
    ChooseModelListener mListener;
    ChooseValuationModelListener mValuationListener;
    ArrayList<AutoModel> mAutoModels;
    QuickAdapter<AutoModel> mModelAdapter;
    String mSeries = "";
    String mSeriesId = "";
    int mType;
    ChooseIntentModelListener mIntentModelListener;

    @Override
    public void onRefresh() {
        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
    }

    @Override
    public void onLoadMore() {
        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
    }

    private void showList() {
        mPtrFrameLayoutView.setVisibility(View.VISIBLE);
        mRequestFailView.setVisibility(View.GONE);
        mModelAdapter = new QuickAdapter<AutoModel>(getActivity(), R.layout.item_choose, mAutoModels) {
            @Override
            protected void convert(BaseAdapterHelper helper, AutoModel item) {
                helper.setText(R.id.name, item.model_name);
            }
        };
        mListView.setAdapter(mModelAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mType == ChooseFragment.VALUATION) {
                    mValuationListener.completeValuationChooseModel(mAutoModels.get(position).model_name, mAutoModels.get(position).model_id, mAutoModels.get(position).valuation_model_id);
                } else if (mType == ChooseFragment.INTENT) {
                    mIntentModelListener.completeIntentChooseModel(mAutoModels.get(position).model_name);
                } else {
                    mListener.completeChooseModel(mAutoModels.get(position).model_name, mAutoModels.get(position).model_id);
                }
            }
        });

    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        initModel(((ChooseFragment) getParentFragment()).mSeries, ((ChooseFragment) getParentFragment()).mSeriesId, ((ChooseFragment) getParentFragment()).mAutoModels);
    }

    @Override
    protected void onSuccessResponse(String response) {

    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    protected String getEmptyDescription() {
        return getResources().getString(R.string.choose_model_no_data);
    }

    @Override
    public String fragmentDescription() {
        return "二手车选择车型";
    }

    public void initModel(String seriesName, String seriesId, ArrayList<AutoModel> autoModels) {
        mSeries = seriesName;
        mSeriesId = seriesId;
        mAutoModels = autoModels;
        if (mAutoModels != null && mAutoModels.size() > 0) {
            showList();
        } else {
            showNoData();
        }
    }

    public void setListener(ChooseModelListener mListener) {
        this.mListener = mListener;
    }

    public void setValuationListener(ChooseValuationModelListener mListener) {
        this.mValuationListener = mListener;
    }

    public void setIntentListener(ChooseIntentModelListener mListener) {
        this.mIntentModelListener = mListener;
    }

    //回调
    public interface ChooseModelListener {
        void completeChooseModel(String model, String modelId);
    }

    //回调
    public interface ChooseValuationModelListener {
        void completeValuationChooseModel(String model, String modelId, String valuationModelId);
    }

    //回调
    public interface ChooseIntentModelListener {
        void completeIntentChooseModel(String model);
    }
}
