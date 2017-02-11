package com.hxqc.mall.thirdshop.accessory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.activity.ProductDetailActivity;
import com.hxqc.mall.thirdshop.accessory.adapter.CustomConditionAdapter;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.ProductFitList;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * 选车型Fragment
 * Created by huangyi on 16/2/24.
 */
public class SelectCarModelFragment extends FunctionFragment {

    String mProductID = ""; //商品ID

    ProductDetailActivity mActivity;
    CustomConditionAdapter mAdapter;
    ArrayList<ProductFitList> mFitList = new ArrayList<>();
    ListView mListView;
    RequestFailView mFailView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_car_model, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (ProductDetailActivity) getActivity();
        mAdapter = new CustomConditionAdapter(getContext(), CustomConditionAdapter.Type.PRODUCT_FIT_LIST, mFitList);
        mListView = (ListView) view.findViewById(R.id.select_car_model_list);
        mListView.setAdapter(mAdapter);
        mFailView = (RequestFailView) view.findViewById(R.id.select_car_model_fail);
        mFailView.setEmptyDescription("暂无数据");
        mFailView.setEmptyButtonClick("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.drawerView.closeMenu();
            }
        });
        mFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    public void initData(String productID) {
        if (mProductID.equals(productID)) return;
        mProductID = productID;
        loadData();
    }

    private void loadData() {
        new AccessoryApiClient().getProductFitList(mProductID, new LoadingAnimResponseHandler(mContext, true, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<ProductFitList> temp = JSONUtils.fromJson(response, new TypeToken<ArrayList<ProductFitList>>() {
                });
                if (null == temp || temp.size() == 0) {
                    mListView.setVisibility(View.GONE);
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                } else {
                    mListView.setVisibility(View.VISIBLE);
                    mFailView.setVisibility(View.GONE);

                    mFitList.clear();
                    mFitList.addAll(temp);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mListView.setVisibility(View.GONE);
                mFailView.setVisibility(View.VISIBLE);
                mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
        });
    }

    @Override
    public String fragmentDescription() {
        return "选车型Fragment";
    }

}
