package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.AutoModel;
import com.hxqc.mall.usedcar.model.Series;
import com.hxqc.util.JSONUtils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;


/**
 * 说明:选择车系
 *
 * @author: 吕飞
 * @since: 2015-07-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseSeriesFragment extends FunctionFragment {
    ArrayList<Series> mSeries;
    ChooseSeriesListener mListener;
    QuickAdapter<Series> mSeriesAdapter;
    UsedCarApiClient mUsedCarApiClient;
    TextView mTitleView;
    ImageView mBrandIconView;
    ListView mListView;
    RequestFailView mRequestFailView;
    String mBrandId;
    int mType;
    String mBrand;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_series, container, false);
        mUsedCarApiClient = new UsedCarApiClient();
        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mTitleView = (TextView) rootView.findViewById(R.id.title);
        mBrandIconView = (ImageView) rootView.findViewById(R.id.brand_icon);
        mRequestFailView = (RequestFailView) rootView.findViewById(R.id.fail_view);
        initSeries(((ChooseFragment) getParentFragment()).mBrand, ((ChooseFragment) getParentFragment()).mBrandId, ((ChooseFragment) getParentFragment()).mBrandIcon);
        return rootView;
    }

    @Override
    public String fragmentDescription() {
        return "二手车选择车系";
    }

    public void setType(int type) {
        mType = type;
    }

    private void showList() {
        mListView.setVisibility(View.VISIBLE);
        mRequestFailView.setVisibility(View.GONE);
        mSeriesAdapter = new QuickAdapter<Series>(getActivity(), R.layout.item_choose) {
            @Override
            protected void convert(BaseAdapterHelper helper, Series item) {
                helper.setText(R.id.name, item.serie_name);
            }
        };
        mSeriesAdapter.addAll(mSeries);
        mListView.setAdapter(mSeriesAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.completeChooseSeries(mSeries.get(position).serie_name, mSeries.get(position).serie_id, mSeries.get(position).model);
            }
        });
    }

    public void initSeries(String brand, String brandId, String brandIcon) {
        mListView.setVisibility(View.GONE);
        this.mBrandId = brandId;
        this.mBrand = brand;
        mTitleView.setText(brand);
        ImageUtil.setImage(getActivity(), mBrandIconView, brandIcon);
        getData();
    }

    private void getData() {
        if (mType == ChooseFragment.VALUATION) {
            mUsedCarApiClient.getValuationSeriesModel(mBrandId, new LoadingAnimResponseHandler(getActivity()) {
                @Override
                public void onSuccess(String response) {
                    mSeries = JSONUtils.fromJson(response, new TypeToken<ArrayList<Series>>() {
                    });
                    if (mSeries != null && mSeries.size() > 0) {
                        showList();
                    } else {
                        mListView.setVisibility(View.GONE);
                        mRequestFailView.setVisibility(View.VISIBLE);
                        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    showFail();
                }
            });
        } else if (mType == ChooseFragment.INTENT) {
            mUsedCarApiClient.getIntentSeriesModel(mBrand, new LoadingAnimResponseHandler(getActivity()) {
                @Override
                public void onSuccess(String response) {
                    mSeries = JSONUtils.fromJson(response, new TypeToken<ArrayList<Series>>() {
                    });
                    if (mSeries != null && mSeries.size() > 0) {
                        showList();
                    } else {
                        mListView.setVisibility(View.GONE);
                        mRequestFailView.setVisibility(View.VISIBLE);
                        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    showFail();
                }
            });
        } else {
            mUsedCarApiClient.getSeriesModel(mBrandId, new LoadingAnimResponseHandler(getActivity()) {
                @Override
                public void onSuccess(String response) {
                    mSeries = JSONUtils.fromJson(response, new TypeToken<ArrayList<Series>>() {
                    });
                    if (mSeries != null && mSeries.size() > 0) {
                        showList();
                    } else {
                        mListView.setVisibility(View.GONE);
                        mRequestFailView.setVisibility(View.VISIBLE);
                        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    showFail();
                }
            });
        }
    }

    private void showFail() {
        mListView.setVisibility(View.GONE);
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        mRequestFailView.setFailButtonClick(getResources().getString(R.string.reload), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    public void setListener(ChooseSeriesListener mListener) {
        this.mListener = mListener;
    }

    //回调
    public interface ChooseSeriesListener {
        void completeChooseSeries(String seriesName, String seriesId, ArrayList<AutoModel> autoModels);
    }
}
